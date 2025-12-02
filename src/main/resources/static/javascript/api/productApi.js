function getAllProducts() {
  return httpGet(`${BASE_URL}/products`);
}

function getProductById(id) {
  return httpGet(`${BASE_URL}/products/${id}`);
}

function addProduct(product) {
  return httpPost(`${BASE_URL}/products`, product);
}

function deleteProduct(id) {
  return httpDelete(`${BASE_URL}/products/${id}`);
}
