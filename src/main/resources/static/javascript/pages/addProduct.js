import { addProduct } from "../api/productApi.js";

document.getElementById("addProductForm").addEventListener("submit", async (e) => {
  e.preventDefault();

  const product = {
    name: document.getElementById("name").value,
    price: document.getElementById("price").value,
    stockQuantity: document.getElementById("stockQuantity").value,
    imageUrl: document.getElementById("imageUrl").value
  };

  await addProduct(product);
  alert("Product added!");
  window.location.href = "../index.html"; // Go back to home
});
