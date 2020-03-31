importScripts("./helpers.js");

addEventListener("message", (ev) => {
  fetchFormData("/photos", ev.data).then(() => {
    postMessage({});
  }).catch((error) => {
    postMessage({ error: error.message });
  });
});