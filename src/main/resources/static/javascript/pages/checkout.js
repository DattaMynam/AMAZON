(async function () {
  const urlParams = new URLSearchParams(window.location.search);
  const orderId = urlParams.get("orderId");
  const amount = urlParams.get("amount");

  if (!orderId || !amount) {
    alert("Invalid order details. Redirecting to cart.");
    window.location.href = "cart.html";
    return;
  }

  document.getElementById("order-summary").innerHTML = `
    <p>Order ID: ${orderId}</p>
    <p>Total Amount: ₹${amount}</p>
  `;

  document.getElementById("pay-btn").addEventListener("click", async () => {
    const customerId = localStorage.getItem("customerId");
    try {
      const payment = await createPayment(orderId, amount);
      console.log("Payment data from backend:", payment);

      if (
        !payment ||
        !payment.key ||
        !payment.amount ||
        !payment.razorpayOrderId
      ) {
        alert("Invalid payment data from server. Check backend.");
        return;
      }

      var options = {
        key: payment.key,
        amount: payment.amount,
        currency: "INR",
        order_id: payment.razorpayOrderId,

        handler: function (resp) {
          console.log("Payment response:", resp);

          // =========================
          //   IMPORTANT FIX: VERIFY
          // =========================
          const verifyUrl =
            `${BASE_URL}/payment/verify` +
            `?razorpay_order_id=${resp.razorpay_order_id}` +
            `&razorpay_payment_id=${resp.razorpay_payment_id}` +
            `&razorpay_signature=${resp.razorpay_signature}`;

          fetch(verifyUrl, { method: "POST" })
            .then((res) => res.text())
            .then((msg) => {
              console.log("Verify response:", msg);
              alert("Payment Verified");
              window.location.href = "index.html";
            })
            .catch((err) => {
              console.error("Verify error:", err);
              alert("Verify failed! Check backend logs.");
            });
        },

        prefill: {
          name: "Customer Name",
          email: "customer@example.com",
        },

        theme: { color: "#3399cc" },
      };

      new Razorpay(options).open();
    } catch (err) {
      console.error("Payment error:", err);
      alert("Payment failed: " + (err.message || "Server error."));
    }
  });
})();
