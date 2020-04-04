import  MessageElement from "./message-element.js";
import { writeHelp, showRightIcon, removeHelp } from "./helpers.js";

(function() {
  const worker = new Worker("./js/worker/send-form.js")
  const formPicture = document.getElementById("form-picture");
  const inputTitle = document.getElementById("input-title");
  const inputPicture = document.getElementById("input-picture");
  const imgPicture = document.getElementById("img-picture");
  const message = new MessageElement(document.getElementById("root"), { childIndex: 1 });
  let imageName = "";

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
    message.info("Photos en cours d’envoi");
    worker.postMessage({ title, picture });
    ev.preventDefault();
  });
  worker.addEventListener("message", (ev) => {
    const { error } = ev.data;
    if (error) {
      message.error(error);
      return;
    }
    message.success("La photo a été correctement envoyé");
  });
})();