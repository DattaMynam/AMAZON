document.querySelector("button").addEventListener("click", async () => {
  const email = document.getElementById("email").value;
  const password = document.getElementById("password").value;

  const res = await fetch("http://localhost:8081/api/customer/login", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ email, password }),
  });

  if (!res.ok) {
    alert("Login failed");
    window.location.href = "signup.html";
    return;
  }

  const customer = await res.json();

  localStorage.setItem("customerId", customer.id);

  alert("Login success!");

  window.location.href = "index.html";
});
