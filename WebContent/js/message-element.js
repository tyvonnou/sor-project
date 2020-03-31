/**
 * MessageElement options
 * @typedef {object} MessageElementOptions
 * @property {boolean} [isHidden=true] If the message has to be hide by default
 * @property {number} [childIndex=0] The index where the message should be insert (insertBefore)
 * @property {string} [element="article"] Tag name of the message element
 * @property {string} [message="message"] Class name of the message element
 * @property {string} [messageBody="messsage-body"] Class name of the message body element
 * @property {string} [messageHeader="message-header"] Class name of the message header element
 * @property {string} [delete="delete"] Class name of the hide button element
 * @property {string} [hidden="is-hidden"] Class name to use to hide message element
 * @property {string} [success="is-success"] Class name to use to display a success message
 * @property {string} [info="is-info"] Class name to use to display an info message
 * @property {string} [error="is-danger"] Class name to use to display a error message
 * @property {string} [warning="is-warning"] Class name to use to display a warning message
 */

 /**
  * Default options to create a message element
  * @type {MessageElementOptions}
  */
 const defaultOptions = {
  isHidden: true,
  childIndex: 0,
  element: "article",
  message: "message",
  messageBody: "message-body",
  messageHeader: "message-header",
  delete: "delete",
  hidden: "is-hidden",
  success: "is-success",
  info: "is-info",
  error: "is-danger",
  warning: "is-warning",
};

/**
 * A message with a title, body, hide button
 */
export default class MessageElement {

  /**
   * Whether this component is hidden or not
   * @name MessageElement#isHidden
   * @type boolean
   * @readonly
   * @public
   */
  get isHidden() {
    return this._message.classList.contains(this._hidden);
  }

  /**
    * Create a new message element
    * @param {HTMLElement} parent Parent of the message element
    * @param {MessageElementOptions} [options]
   */
  constructor(parent, options) {
    if (options) {
      options = { ...defaultOptions, ...options };
    } else {
      options = defaultOptions;
    }
    this._hidden = options.hidden;
    this._success = options.success;
    this._info = options.info;
    this._error = options.error;
    this._warning = options.warning;
    this._message = document.createElement(options.element);
    this._message.classList.add(options.message);
    if (options.isHidden) {
      this.hide();
    }
    this._createHeader();
    this._createBody();
    parent.insertBefore(this._message, parent.children[options.childIndex]);
  }

  /**
   * Show the messsage
   */
  show() {
    this._message.classList.remove(this._hidden);
  }

  /**
   * Hide the message
   */
  hide() {
    this._message.classList.add(this._hidden);
  }

  /**
   * Write the content of the message in success mode
   * @param {...Node | string} elements Elements to append in body
   */
  success(...elements) {
    this._title = "Succès";
    this._setClass(this._success);
    this._setBody(elements);
  }

  /**
   * Write the content of the message in info mode
   * @param {...Node | string} elements Elements to append in body
   */
  info(...elements) {
    this._title = "Info";
    this._setClass(this._info);
    this._setBody(elements)
  }

  /**
   * Write the content of the message in error mode
   * @param {...Node | string} elements Elements to append in body
   */
  error(...elements) {
    this._title = "Erreur";
    this._setClass(this._error);
    this._setBody(elements)
  }

  /**
   * Write the content of the message in warning mode
   * @param {...Node | string} elements Elements to append in body
   */
  warning(...elements) {
    this._title = "Avertissement";
    this._setClass(this._warning);
    this._setBody(elements)
  }

  /**
   * Create message header
   * @protected
   */
  _createHeader() {
    const header = document.createElement("div");
    const del = document.createElement("button");
    this._titleElement = document.createElement("p");
    del.addEventListener("click", this.hide.bind(this));
    del.classList.add("delete");
    header.classList.add("message-header");
    header.appendChild(this._titleElement);
    header.appendChild(del);
    this._message.appendChild(header);
  }

  /**
   * Create message body
   * @protected
   */
  _createBody() {
    this._body = document.createElement("div");
    this._body.classList.add("message-body");
    this._message.appendChild(this._body);
  }

  /**
   * Remove all content in message body
   * @protected
   */
  _resetBody() {
    while (this._body.hasChildNodes()) {
      this._body.removeChild(this._body.lastChild);
    }
  }

  /**
   * Set message body contents
   * @param {(Node | string)[]} elements 
   */
  _setBody(elements) {
    this._resetBody();
    for (const el of elements) {
      this._body.append(el);
    }
    this.show();
  }

  /**
   * Remove all classes
   * @protected
   */
  _resetClass() {
    this._message.classList.remove(this._success, this._error, this._info);
  }

  /**
   * Set class name of the message
   * @param {string} className Class name to add
   * @protected
   */
  _setClass(className) {
    this._resetClass();
    this._message.classList.add(className);
  }

  /**
   * Title of the message
   * @name MessageElement#_title
   * @type string
   * @protected
   */
  get _title() {
    return this._titleElement.textContent;
  }
  set _title(title) {
    this._titleElement.textContent = title;
  }
}
