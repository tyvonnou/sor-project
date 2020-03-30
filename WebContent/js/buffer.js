/**
 * Send data
 * @param {string} url URL to fetch
 * @param {BodyInit} body Data to send
 * @param {{ method: "POST" | "PUT" | "PATCH" }} options
 * @returns Response promise
 */
function fetchFormData(url, body, { method } = { method: "POST" }) {
  if (!method) {
    method = "POST";
  }
  return fetch(url, {
    method,
    body,
  })
}

/**
 * Send the picture to the server
 * @param {string} title Picture title
 * @param {Blob} picture Picture JPEG
 * @param {(data: { size: number }) => void} cb Callback at every request
 */
async function send(title, picture, cb = () => {}) {
  const data = new FormData();
  const bufferSize = 1000;
  let begin = 0;
  data.set("title", title);
  do {
    const end = begin + bufferSize;
    const buffer = picture.slice(begin, end, "application/octet-stream");
    data.set("begin", begin)
    data.set("size", buffer.size);
    data.set("picture", buffer, title);
    begin += bufferSize;
    await fetchFormData("/photos", data);
    cb({ size: begin + buffer.size });
  } while (begin < picture.size);
}

addEventListener("message", (ev) => {
  const { title, picture } = ev.data;
  send(title, picture, (data) => postMessage(data));
});
