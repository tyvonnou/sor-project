/**
 * Options to {@link fetchFormData}
 * @typedef FetchFormDataOptions
 * @property {string} method Method to use by default "POST"
 */

/**
 * Send data
 * @param {string} url URL to fetch
 * @param {BodyInit} body Data to send
 * @param {FetchFormDataOptions} options
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