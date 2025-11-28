document.getElementById("signupBtn").addEventListener("click", async () => {
  const name = document.getElementById("name").value;
  const email = document.getElementById("email").value;
  const password = document.getElementById("password").value;

  const res = await fetch("http://localhost:8081/api/customer/signup", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ name, email, password }),
  });
  if (res.ok) {
    alert("Registered Successfully");
    window.location.href = "index.html";
  } else {
    alert("Signup failed");
  }
});
