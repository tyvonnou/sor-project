importScripts("./helpers.js");

/**
 * See {@link sendCallback}
 * @typedef {object} SendDataCallback
 * @property {number} size Sent buffer size
 */

/**
 * Callback after every request
 * @callback sendCallback
 * @param {SendDataCallback} data
 */

/**
 * Send the picture to the server
 * @param {string} title Picture title
 * @param {Blob} picture Picture JPEG
 * @param {sendCallback} [cb=() => undefined] Callback at every request 
 */
async function send(title, picture, cb = () => undefined) {
  const data = new FormData();
  const bufferSize = 1000;
  let begin = 0;
  data.set("title", title);
  data.set("picture-size", picture.size);
  do {
    const end = begin + bufferSize;
    const buffer = picture.slice(begin, end, "application/octet-stream");
    data.set("begin", begin)
    data.set("size", buffer.size);
    data.set("picture", buffer, title);
    begin += bufferSize;
    await fetchFormData("/Photos-Tomcat/image", data, { convertFormData: false });
    cb({ size: begin + buffer.size });
  } while (begin < picture.size);
}

addEventListener("message", (ev) => {
  const { title, picture } = ev.data;
  send(title, picture, (data) => postMessage({ data })).catch(err => postMessage({ error: err.message }));
});
