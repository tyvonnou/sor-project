/**
 * Options to {@link fetchFormData}
 * @typedef {object} FetchFormDataOptions
 * @property {string} [method="POST"] Method to use by default "POST"
 * @property {boolean} [convertFormData=true] If FormData have to be create
 */

/**
 * Default options of fetchFormData
 * @type {FetchFormDataOptions}
 */
const fetchFormDataDefaultOptions = {
  method: "POST",
  convertFormData: true,
}

/**
 * Send data
 * @param {string} url URL to fetch
 * @param {BodyInit} body Data to send
 * @param {FetchFormDataOptions} [options]
 * @returns Response promise
 */
function fetchFormData(url, body, options) {
  options = { ...fetchFormDataDefaultOptions, ...options };
  if (options.convertFormData) {
    body = formDataFromObject(body);
  }
  return fetch(url, {
    method: options.method,
    body,
  })
}

/**
 * Convert a object to FormData
 * @param {object} obj Object to convert into FormData
 */
function formDataFromObject(obj) {
  const data = new FormData();
  for (const [key, value] of Object.entries(obj)) {
    data.set(key, value);
  }
  return data;
}