(async function () {
  const customerId = localStorage.getItem("customerId");

  if (!customerId) {
    alert("You must log in first");
    window.location.href = "login.html";
    return;
  }

  loadCart();

  async function loadCart() {
    try {
      const items = await getCart(customerId);
      if (!items || items.length === 0) {
        document.getElementById("cart-container").innerHTML =
          "<p>Your cart is empty.</p>";
        document.getElementById("checkout-btn").disabled = true; // Disable checkout if empty
        return;
      }

      document.getElementById("cart-container").innerHTML = items
        .map(
          (i) => `
          <div class="cart-item">
            <img src="${i.product.imageUrl}" class="cart-img">
            <div class="info">
              <h3>${i.product.name}</h3>
              <p>Price: ₹${i.product.price}</p>
              <div class="qty-box">
                <button class="qty-btn" onclick="updateQty(${i.product.id}, ${
            i.quantity - 1
          })">−</button>
                <input type="number" value="${
                  i.quantity
                }" min="1" onchange="updateQty(${i.product.id}, this.value)">
                <button class="qty-btn" onclick="updateQty(${i.product.id}, ${
            i.quantity + 1
          })">+</button>
              </div>
              <button class="remove-btn" onclick="removeItem(${
                i.id
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

  // ... (rest of cart.js remains the same)

window.updateQty = async function (productId, newQty) {
  if (newQty <= 0) return;
  console.log(`Updating product ${productId} to quantity ${newQty}`);
  try {
    await updateCartItem(customerId, productId, Number(newQty));
    loadCart();
  } catch (err) {
    console.error("Error updating quantity:", err);
    alert("Failed to update quantity.");
  }
};

// ... (rest remains the same)

  window.removeItem = async function (itemId) {
    try {
      await deleteCartItem(itemId);
      loadCart();
    } catch (err) {
      console.error("Error removing item:", err);
      alert("Failed to remove item.");
    }
  };
})();

document.getElementById("checkout-btn").addEventListener("click", async () => {
  const customerId = localStorage.getItem("customerId");
  try {
    const order = await createOrder(customerId);
    if (!order || !order.id) {
      alert("Order creation failed. Please try again.");
      return;
    }
    // Redirect to checkout.html with order details
    window.location.href = `checkout.html?orderId=${order.id}&amount=${order.totalAmount}`;
  } catch (err) {
    console.error("Checkout error:", err);
    alert(
      "Checkout failed: " +
        (err.message || "Server error. Check console for details.")
    );
  }
});

// ... (rest of cart.js remains the same)

document.getElementById("checkout-btn").addEventListener("click", async () => {
  const customerId = localStorage.getItem("customerId");
  try {
    const order = await createOrder(customerId);
    if (!order || !order.id) {
      alert("Order creation failed. Please try again.");
      return;
    }
    // Updated: Redirect to checkout.html (your existing file)
    window.location.href = `checkout.html?orderId=${order.id}&amount=${order.totalAmount}`;
  } catch (err) {
    console.error("Checkout error:", err);
    alert(
      "Checkout failed: " +
        (err.message || "Server error. Check console for details.")
    );
  }
});
