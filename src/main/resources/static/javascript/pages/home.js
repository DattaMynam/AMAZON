const productList = document.getElementById("product-list");
const searchBox = document.getElementById("search-box");
const searchBtn = document.getElementById("search-btn");

// Load all products when page loads
loadProducts();

// ==========================
// Load all products
// ==========================
async function loadProducts() {
  try {
    const products = await getAllProducts();

    renderProducts(products);
  } catch (error) {
    console.error("Error fetching products:", error);
    productList.innerHTML = "<p>Failed to load products.</p>";
  }
}

// ==========================
// Render product cards
// ==========================
function renderProducts(products) {
  if (!products || products.length === 0) {
    productList.innerHTML = "<p>No products available.</p>";
    return;
  }

  productList.innerHTML = products
    .map(
      (p) => `
        <div class="product-card" onclick="viewProduct(${p.id})">
          <img src="${p.imageUrl}" alt="${p.name}" />

          <h3>${p.name}</h3>

          <p class="price">₹${p.price}</p>

          <button onclick="event.stopPropagation(); viewProduct(${p.id})">
            View
          </button>
        </div>
      `
    )
    .join("");
}

// ==========================
// Search functionality
// ==========================
searchBtn.addEventListener("click", async () => {
  const key = searchBox.value.trim().toLowerCase();

  if (key === "") {
    loadProducts();
    return;
  }

  try {
    const products = await getAllProducts();
    const filtered = products.filter((p) => p.name.toLowerCase().includes(key));

    renderProducts(filtered);
  } catch (err) {
    console.error("Search error:", err);
  }
});

// ==========================
// Redirect to productDetails
// ==========================
function viewProduct(id) {
  window.location.href = `productDetails.html?id=${id}`;
}

const hamburger = document.getElementById("hamburger");
const menu = document.getElementById("side-menu");

hamburger.addEventListener("click", () => {
  menu.style.right = menu.style.right === "0px" ? "-250px" : "0px";
});

// Close menu when clicking outside
document.addEventListener("click", (e) => {
  if (!menu.contains(e.target) && !hamburger.contains(e.target)) {
    menu.style.right = "-250px";
  }
});



const logoutBtn = document.getElementById("logout-btn");

logoutBtn?.addEventListener("click", async (e) => {
    e.preventDefault(); // prevent default link navigation

    try {
        const res = await fetch(`${BASE_URL}/customer/logout`, {
            method: "POST",
            credentials: "include", 
        });

        if (!res.ok) throw new Error("Logout failed");

        localStorage.removeItem("customer");
		localStorage.removeItem("customerId");

		const message = await res.text();
		alert(message);    

        window.location.href = "index.html";
    } catch (err) {
        console.error(err);
        alert("Logout failed. Try again.");
    }
});
