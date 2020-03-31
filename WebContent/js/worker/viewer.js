importScripts("./helpers.js");

async function getPic(title) {
  const result = await fetchFormData("/photos", { title });
  if (result.status >= 400) {
    return null;
  }
  return result.blob();
}

addEventListener("message", (ev) => {
  getPic(ev.data.title).then((res) => {
    const picture = res ? URL.createObjectURL(res.blob()) : null; 
    postMessage({ picture });
  }).catch((error) => {
    postMessage({ error: error.message });
  });
});
