async function httpGet(url) {
	const res = await fetch(url, {
	    method: "GET",
	    credentials: "include"  
	  });
  if (!res.ok) throw new Error(`GET failed: ${res.status} ${res.statusText}`);
  return res.json();
}

async function httpPost(url, body) {
  const res = await fetch(url, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
	credentials: "include",
    body: JSON.stringify(body),
  });
  if (!res.ok) throw new Error(`POST failed: ${res.status} ${res.statusText}`);
  return res.json();
}

async function httpPut(url, data) {
  const res = await fetch(url, {
    method: "PUT",               
    headers: { "Content-Type": "application/json" },
	credentials: "include",
    body: JSON.stringify(data)
  });

  if (!res.ok) {
    throw new Error(`PUT failed: ${res.status} ${res.statusText}`);
  }

  return await res.json();
}



async function httpDelete(url) {
  const res = await fetch(url, { method: "DELETE",credentials: "include" });
  if (!res.ok) throw new Error(`DELETE failed: ${res.status} ${res.statusText}`);
  return res.text();
}