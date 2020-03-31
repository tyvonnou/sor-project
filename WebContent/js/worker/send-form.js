importScripts("./helpers.js");

addEventListener("message", (ev) => {
  fetchFormData("/photos", data).then(() => {
    postMessage({});
  }).catch((error) => {
    postMessage({ error: error.message });
  });
});