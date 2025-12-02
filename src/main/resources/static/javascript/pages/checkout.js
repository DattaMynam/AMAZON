document.addEventListener("DOMContentLoaded", async () => {
  const customerObj = JSON.parse(localStorage.getItem("customer"));
  const customerId = customerObj.id;

  if (!customerId) {
    alert("You must log in first");
    window.location.href = "/login.html";
    return;
  }

  await loadSummary(customerId);

  document.getElementById("pay-btn").addEventListener("click", async () => {
    await startCheckout(customerId);
  });
});

// ---------------------------------
// STEP 1: Load order summary
// ---------------------------------
async function loadSummary(customerId) {
  try {
    const cart = await httpGet(`${BASE_URL}/cart/${customerId}`);
    const total = cart.cartTotal || 0;

    document.getElementById("order-summary").innerHTML = `
      <h3>Order Summary</h3>
      <p>Total: <strong>₹${total}</strong></p>
    `;
  } catch (err) {
    console.error("Summary load error:", err);
  }
}


// ---------------------------------
// STEP 2: Start Checkout
// ---------------------------------
async function startCheckout(customerId) {
  try {
    // 1) create backend order
    const orderRes = await createOrder(customerId);

    if (!orderRes || !orderRes.id) {
      alert("Order creation failed");
      return;
    }

    // 2) create Razorpay payment
    const razorpayOrder = await createPayment(orderRes.id, orderRes.totalAmount * 100); // in paisa

    if (!razorpayOrder || !razorpayOrder.razorpayOrderId) {
      alert("Payment order creation failed");
      return;
    }

    // 3) open Razorpay
    openRazorpay(razorpayOrder);

  } catch (err) {
    console.error("Checkout error:", err);
    alert("Something went wrong during checkout");
  }
}

// ---------------------------------
// STEP 3: Open Razorpay Checkout
// ---------------------------------
function openRazorpay(razorpayOrder) {
  const options = {
    key: razorpayOrder.key,
    amount: razorpayOrder.amount,
    currency: "INR",
    name: "My Store",
    description: "Order Payment",
    order_id: razorpayOrder.razorpayOrderId,

    handler: async function (resp) {
      await verifyPayment(resp);
    },

    theme: { color: "#3399cc" },
  };

  const rzp = new Razorpay(options);
  rzp.open();
}

// ---------------------------------
// STEP 4: Verify Payment
// ---------------------------------
async function verifyPayment(resp) {
  try {
    const body = {
      razorpayOrderId: resp.razorpay_order_id,
      razorpayPaymentId: resp.razorpay_payment_id,
      razorpaySignature: resp.razorpay_signature,
    };

    console.log("Sending verify request body:", body);

	const result = await httpPost(`${BASE_URL}/payment/verify`, body);
	console.log("Verify response:", result);
	if (result.status === "success") {
	    alert(result.message);
	    window.location.href = "/orders.html";
	} else {
	    alert(result.message || "Verification failed");
	}
  } catch (err) {
    console.error("Verify error:", err);
    alert("Payment verification error. Check console for details.");
  }
}