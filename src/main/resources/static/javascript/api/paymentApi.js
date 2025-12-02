async function createPayment(orderId, amount) {
  return await httpPost(`${BASE_URL}/payment/create`, { orderId, amount });
}
