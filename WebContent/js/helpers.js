/**
 * Get the help paragraph from input element name
 * @param {string} name Input name
 * @returns {HTMLElement} Help paragraph
 */
export function getHelp(name) {
  return document.getElementById(`help-${name}`);
}

/**
 * Write validation message of an input element in its help paragraph
 * @param {HTMLInputElement} param0 Input element
 */
export function writeHelp({ name, validationMessage }) {
  const help = getHelp(name);
  help.textContent = validationMessage;
  help.classList.remove("is-hidden");
}

/**
 * Hide help paragraph from input element name
 * @param {HTMLInputElement} param0 Input element
 */
export function hideHelp({ name }) {
  const help = getHelp(name);
  help.classList.add("is-hidden");
}

/**
 * Show the right icon of an input element
 * @param {HTMLInputElement} param0 Input element
 */
export function showRightIcon({ name }) {
  const icon = document.getElementById(`icon-right-${name}`);
  icon.classList.remove("is-hidden");
}

/**
 * Hide the right icon of an input element
 * @param {HTMLInputElement} param0 Input element
 */
export function hideRightIcon({ name }) {
  const icon = document.getElementById(`icon-right-${name}`);
  icon.classList.add("is-hidden");
}

/**
 * Remove any help for all input elements of a form
 * @param {FocusEvent} param0 Form element
 */
export function removeHelp({ target }) {
  const inputs = Array.from(target.elements).filter((el) => el.tagName === "INPUT");
  const texts = inputs.filter((input) => input.type === "text");
  const files = inputs.filter((input) => input.type === "file");
  for (const text of texts) {
    hideHelp(text);
    hideRightIcon(text);
    text.classList.remove("is-danger");      
  }
  for (const file of files) {
    hideHelp(file);
    file.parentElement.parentElement.classList.remove("is-danger");
  }
}