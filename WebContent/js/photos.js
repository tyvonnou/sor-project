const worker = new Worker("./js/buffer.js")

/**
 * Get the help paragraph from input element name
 * @param {string} name Input name
 * @returns {HTMLElement} Help paragraph
 */
function getHelp(name) {
  return document.getElementById(`help-${name}`);
}

/**
 * Write validation message of an input element in its help paragraph
 * @param {HTMLInputElement} param0 Input element
 */
function writeHelp({ name, validationMessage }) {
  const help = getHelp(name);
  help.textContent = validationMessage;
  help.classList.remove("is-hidden");
}

/**
 * Hide help paragraph from input element name
 * @param {HTMLInputElement} param0 Input element
 */
function hideHelp({ name }) {
  const help = getHelp(name);
  help.classList.add("is-hidden");
}

/**
 * Show the right icon of an input element
 * @param {HTMLInputElement} param0 Input element
 */
function showRightIcon({ name }) {
  const icon = document.getElementById(`icon-right-${name}`);
  icon.classList.remove("is-hidden");
}

/**
 * Hide the right icon of an input element
 * @param {HTMLInputElement} param0 Input element
 */
function hideRightIcon({ name }) {
  const icon = document.getElementById(`icon-right-${name}`);
  icon.classList.add("is-hidden");
}

/**
 * Remove any help for all input elements of a form
 * @param {FocusEvent} param0 Form element
 */
function removeHelp({ target }) {
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

class MessageElement {
  /**
   * 
   * @param {HTMLElement} parent
   */
  constructor(parent, isHidden = true) {
    this._message = document.createElement("article");
    this._message.classList.add("message");
    if (isHidden) {
      this.hide();
    }
    this._createHeader();
    this._createBody();
    parent.insertBefore(this._message, parent.children[1]);
  }

  show() {
    this._message.classList.remove("is-hidden");
  }

  hide() {
    this._message.classList.add("is-hidden");
  }

  /**
   * 
   * @param {...HTMLElement | string} elements Elements to append in body
   */
  success(...elements) {
    this._title.textContent = "Succès";
    this._setBody("is-success", elements);
  }

  /**
   * 
   * @param {...HTMLElement | string} elements Elements to append in body
   */
  info(...elements) {
    this._title.textContent = "Info";
    this._setBody("is-info", elements)
  }

  /**
   * 
   * @param {...HTMLElement | string} elements Elements to append in body
   */
  error(...elements) {
    this._title.textContent = "Erreur";
    this._setBody("is-error", elements)
  }

  _createHeader() {
    const header = document.createElement("div");
    const del = document.createElement("button");
    this._title = document.createElement("p");
    del.addEventListener("click", this.hide.bind(this));
    del.classList.add("delete");
    header.classList.add("message-header");
    header.appendChild(this._title);
    header.appendChild(del);
    this._message.appendChild(header);
  }

  _createBody() {
    this._body = document.createElement("div");
    this._body.classList.add("message-body");
    this._message.appendChild(this._body);
  }

  _reset() {
    while (this._body.hasChildNodes()) {
      this._body.removeChild(this._body.lastChild);
    }
    this._message.classList.remove("is-success", "is-info", "is-error");
  }

  _setBody(className, elements) {
    this._reset();
    this._message.classList.add(className);
    for (const el of elements) {
      this._body.append(el);
    }
    this.show();
  }
}

(function() {
  const formPicture = document.getElementById("form-picture");
  const inputTitle = document.getElementById("input-title");
  const inputPicture = document.getElementById("input-picture");
  const imgPicture = document.getElementById("img-picture");
  const message = new MessageElement(document.getElementById("root"));
  const progressBar = document.createElement("progress");
  let imageName = "";
  let progressBarMax;

  progressBar.classList.add("progress", "is-medium");

  inputTitle.addEventListener("invalid", (ev) => {
    const { target } = ev;
    if (!(target instanceof HTMLInputElement)) {
      return;
    }
    writeHelp(target);
    target.classList.add("is-danger");
    showRightIcon(target);
    ev.preventDefault();
  });

  inputPicture.addEventListener("invalid", (ev) => {
    writeHelp(ev.target);
    ev.target.parentElement.parentElement.classList.add("is-danger");
    ev.preventDefault();
  });
  inputPicture.addEventListener("change", () => {
    const file = inputPicture.files[0];
    if (!inputTitle.value || inputTitle.value === imageName) {
      const { name } = file;
      imageName = inputTitle.value = name.substring(0, name.lastIndexOf(".") - 1);
    }
    imgPicture.classList.remove("is-hidden");
    imgPicture.src = URL.createObjectURL(file);
  });
  formPicture.addEventListener("reset", (ev) => {
    removeHelp(ev);
    imageName = "";
    imgPicture.classList.add("is-hidden");
    message.hide();
  });
  formPicture.addEventListener("submit", (ev) => {
    removeHelp(ev);
    const title = inputTitle.value;
    const picture = inputPicture.files[0];
    progressBar.classList.remove("is-success");
    progressBar.classList.add("is-info");
    progressBar.value = 0;
    progressBarMax = progressBar.max = picture.size;
    progressBar.classList.add("is-info");
    message.info(
      "Photos en cours d’envoi",
      progressBar,
    );
    worker.postMessage({ title, picture });
    ev.preventDefault();
  });
  worker.addEventListener("message", (ev) => {
    const { size } = ev.data;
    progressBar.value = size;
    if (size >= progressBarMax) {
      progressBar.classList.replace("is-info", "is-success");
      message.success(
        "La photo a été correctement envoyé",
        progressBar,
      );
    }
  });
})();