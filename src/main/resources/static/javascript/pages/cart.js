(async function() {
	const customer = JSON.parse(localStorage.getItem("customer"));

	if (!customer || !customer.id) {
		alert("You must log in first");
		window.location.href = "login.html";
		return;
	}

	const customerId = customer.id;

	loadCart();

	async function loadCart() {
		try {
			const cart = await getCart(customerId);
			const items = cart.items;

			if (!items || items.length === 0) {
				document.getElementById("cart-container").innerHTML =
					"<p>Your cart is empty.</p>";
				document.getElementById("checkout-btn").disabled = true;
				return;
			}

			document.getElementById("cart-container").innerHTML = items
				.map(
					(i) => `
          <div class="cart-item">
            <img src="${i.imageUrl}" class="cart-img">
            <div class="info">
              <h3>${i.name}</h3>
              <p>Price: ₹${i.price}</p>
              <div class="qty-box">
                <button class="qty-btn" onclick="updateQty(${i.productId}, ${i.quantity - 1
						})">−</button>
                <input type="number" value="${i.quantity
						}" min="1" onchange="updateQty(${i.productId}, this.value)">
                <button class="qty-btn" onclick="updateQty(${i.productId}, ${i.quantity + 1
						})">+</button>
              </div>
              <button class="remove-btn" onclick="removeItem(${i.productId
						})">Remove</button>
            </div>
          </div>`
				)
				.join("");
		} catch (err) {
			console.error("Error loading cart:", err);
			alert("Failed to load cart.");
		}
	}

	window.updateQty = async function(productId, newQty) {
		if (newQty <= 0) return;

		const customer = JSON.parse(localStorage.getItem("customer"));
		const customerId = customer.id;

		console.log("Updating cart:", { customerId, productId, quantity: Number(newQty) });

		try {
			const updatedCart = await updateCartItem(customerId, productId, Number(newQty));
			console.log("Updated cart response:", updatedCart);
			loadCart(); // refresh cart after update
		} catch (err) {
			console.error("Error updating quantity:", err);
			alert("Failed to update quantity. Check console for details.");
		}
	};

	window.removeItem = async function(productId) {
		const customer = JSON.parse(localStorage.getItem("customer"));
		const customerId = customer.id;

		try {
			// Send customerId + productId to backend
			await deleteCartItem(customerId, productId);
			loadCart(); // refresh cart after remove
		} catch (err) {
			console.error("Error removing item:", err);
			alert("Failed to remove item.");
		}
	};


})();

document.getElementById("checkout-btn").addEventListener("click", async () => {
    const customer = JSON.parse(localStorage.getItem("customer"));
    const customerId = customer.id;

    try {
        const order = await createOrder(customerId);

		console.log("ORDER RESPONSE FROM BACKEND:", order);  // ← ADD THIS
		
        if (!order || !order.id) {
            alert("Order creation failed. Please try again.");
            return;
        }

		window.location.href = `checkout.html?orderId=${order.id}&amount=${order.totalAmount}`;

        
    } catch (err) {
        console.error("Checkout error:", err);
        alert("Checkout failed: " + (err.message || "Server error. Check console for details."));
    }
});
