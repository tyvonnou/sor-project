importScripts("./helpers.js");

async function getPic(title) {
  const result = await fetch(`/Photos-Tomcat/image/${title}`);
  if (result.status >= 400) {
    return null;
  }
  return result.blob();
}

addEventListener("message", (ev) => {
  getPic(ev.data.title).then((blob) => {
    const picture = blob ? URL.createObjectURL(blob) : null; 
    postMessage({ picture });
  }).catch((error) => {
    postMessage({ error: error.message });
  });
});
