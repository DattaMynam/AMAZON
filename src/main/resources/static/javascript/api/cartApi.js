async function addToCart(customerId, productId, quantity) {
  return await httpPost(`${BASE_URL}/cart/add`, {
    customerId,
    productId,
    quantity,
  });
}

async function updateCartItem(customerId, productId, quantity) {
  return await httpPost(`${BASE_URL}/cart/update`, {
    customerId,
    productId,
    quantity,
  });
}

function getCart(customerId) {
  return httpGet(`${BASE_URL}/cart/${customerId}`);
}

function deleteCartItem(itemId) {
  return httpDelete(`${BASE_URL}/cart/${itemId}`);
}