importScripts("./helpers.js");

addEventListener("message", (ev) => {
  const { title, picture } = ev.data;
  fetchFormData("/Photos-Tomcat/image_onepart", { title, picture, size: picture.size } ).then(() => {
    postMessage({});
  }).catch((error) => {
    postMessage({ error: error.message });
  });
});