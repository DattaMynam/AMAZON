const customer = JSON.parse(localStorage.getItem("customer"));

if (!customer) {
  alert("Login required!");
  window.location.href = "login.html";
} else {
  document.getElementById("customer-name").innerText = customer.name;
  document.getElementById("customer-email").innerText = customer.email;
  document.getElementById("customer-id").innerText = customer.id;
}
