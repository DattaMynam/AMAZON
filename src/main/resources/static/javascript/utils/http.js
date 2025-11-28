async function httpGet(url) {
  const res = await fetch(url);
  if (!res.ok) throw new Error(`GET failed: ${res.status} ${res.statusText}`);
  return res.json();
}

async function httpPost(url, body) {
  const response = await fetch(url, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(body),
  });
  if (!response.ok) throw new Error(`POST failed: ${response.status} ${response.statusText}`);
  return response.json();
}

async function httpDelete(url) {
  const res = await fetch(url, { method: "DELETE" });
  if (!res.ok) throw new Error(`DELETE failed: ${res.status} ${res.statusText}`);
  return res.text();
}