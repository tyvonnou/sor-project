importScripts("./helpers.js");

addEventListener("message", (ev) => {
  const { title, picture } = ev.data;
  const data = new FormData();
  data.set("title", title);
  data.set("picture", picture, title);
  fetchFormData("/photos", data).then(() => {
    console.log("oui")
    setTimeout(() => {
      postMessage({});
    }, 1000);
  }).catch((error) => {
    console.log(error)
    postMessage({error: error.message});
  });
});