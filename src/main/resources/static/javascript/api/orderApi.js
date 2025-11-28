async function createOrder(customerId) {
  return await httpPost(`${BASE_URL}/orders/create`, { customerId });
}