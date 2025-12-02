const params = new URLSearchParams(window.location.search);
const id = params.get("id");

if (!id) {
	console.error("Product ID missing in URL");
}

fetch(`${BASE_URL}/products/${id}`)
	.then((res) => res.json())
	.then((p) => {
		document.getElementById("product-details-container").innerHTML = `
      <div class="product-box">
          <img src="${p.imageUrl}" class="big-img" />
          <h2>${p.name}</h2>
          <p>${p.description ?? "No description"}</p>
          <h3>₹${p.price}</h3>

          <div class="qty-box">
            <button class="qty-btn" onclick="changeQty(-1)">-</button>
            <input id="qty-input" type="number" value="1" min="1" />
            <button class="qty-btn" onclick="changeQty(1)">+</button>
          </div>
		  
		  <button class="btn-primary" onclick="handleAddToCart(${p.id})">
		    Add to Cart
		  </button>
		  <button class="btn-primary" onclick="handleBuyNow(${p.id})">
		            Buy Now
		          </button>
      </div>
    `;
	})
	.catch((e) => console.error("Error loading product:", e));

function changeQty(val) {
	const input = document.getElementById("qty-input");
	let num = parseInt(input.value);
	num = num + val;
	if (num < 1) num = 1;
	input.value = num;
}

async function handleAddToCart(productId) { // renamed to avoid conflict
	const customer = JSON.parse(localStorage.getItem("customer"));

	if (!customer || !customer.id) {
		alert("Login required");
		window.location.href = "login.html";
		return;
	}

	const customerId = customer.id;
	const quantity = parseInt(document.getElementById("qty-input").value);

	try {
		await addToCart(customerId, productId, quantity); // this now calls cartApi.js's method
		alert("Added to cart");
		window.location.href = "cart.html";
	} catch (err) {
		console.error("Error adding to cart:", err);
		alert("Failed to add to cart. Check console for details.");
	}
}

async function handleBuyNow(productId) {
    const customer = JSON.parse(localStorage.getItem("customer"));
    if (!customer) {
        alert("Login required");
        window.location.href = "login.html";
        return;
    }

    const quantity = parseInt(document.getElementById("qty-input").value) || 1;

    // STEP 1: Create order
    const res = await fetch(`${BASE_URL}/orders/buy`, {
        method: "POST",
        credentials: "include",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ customerId: customer.id, productId, quantity })
    });

    if (!res.ok) {
        alert("Failed to create order");
        return;
    }

    const paymentData = await res.json(); // contains Razorpay order info + our orderId

    // STEP 2: Setup Razorpay
    const options = {
        key: paymentData.key,
        amount: paymentData.amount,
        currency: paymentData.currency,
        name: "Your Store",
		order_id: paymentData.razorpayOrderId,

        handler: async function (response) {
			
			console.log("Handler response:", response);  // ← PUT IT HERE
            // STEP 3: Verify payment
            const verifyBody = {
                razorpayOrderId: response.razorpay_order_id,
                razorpayPaymentId: response.razorpay_payment_id,
                razorpaySignature: response.razorpay_signature
            };

            const verifyRes = await fetch(`${BASE_URL}/payment/verify`, {
                method: "POST",
				credentials: "include",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(verifyBody)
            });

            const result = await verifyRes.json().catch(() => null);

            if (!verifyRes.ok || !result || result.status !== "success") {
                console.error("Payment verification failed:", result);
                alert(result?.message || "Payment verification failed");
                return;
            }

            // STEP 4: Redirect after verification success
            alert("Payment Successful!");
            window.location.href = `/orderDetails.html?orderId=${paymentData.orderId}`;
        }
    };

    new Razorpay(options).open();
}