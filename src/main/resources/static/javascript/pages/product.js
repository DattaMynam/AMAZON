const params = new URLSearchParams(window.location.search);
const id = params.get("id");

if (!id) {
  console.error("Product ID missing in URL");
}

fetch(`http://localhost:8081/api/products/${id}`)
  .then((res) => res.json())
  .then((p) => {
    document.getElementById("product-details").innerHTML = `
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

          <button class="btn-primary" onclick="addToCart(${p.id})">
            Add to Cart
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

function addToCart(productId) {
  const customerId = localStorage.getItem("customerId");

  if (!customerId) {
    alert("Login required");
    window.location.href = "login.html";
    return;
  }

  const quantity = parseInt(document.getElementById("qty-input").value);

  fetch("http://localhost:8081/api/cart/add", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({
      customerId,
      productId,
      quantity,
    }),
  })
    .then((res) => res.json())
    .then(() => {
      alert("Added to cart");
      window.location.href = "cart.html";
    })
    .catch((err) => console.error(err));
}
