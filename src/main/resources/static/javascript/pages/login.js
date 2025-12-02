document.querySelector("button").addEventListener("click", async () => {
  const email = document.getElementById("email").value;
  const password = document.getElementById("password").value;

  const res = await fetch(`${BASE_URL}/customer/login`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
	credentials: "include",
    body: JSON.stringify({ email, password }),
  });

  if (!res.ok) {
    alert("Login failed");
    return;
  }

  const customer = await res.json();

localStorage.setItem("customer", JSON.stringify(customer));

alert("Login success!");
window.location.href = "index.html";
});


