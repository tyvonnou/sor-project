import { writeHelp, showRightIcon, removeHelp } from "./helpers.js";
import MessageElement from "./message-element.js";

(function() {
  const worker = new Worker("./js/worker/viewer.js")
  const formPicture = document.getElementById("form-picture");
  const inputTitle = document.getElementById("input-title");
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
  formPicture.addEventListener("reset", (ev) => {
    removeHelp(ev);
    imageName = "";
    imgPicture.classList.add("is-hidden");
    message.hide();
  });
  formPicture.addEventListener("submit", (ev) => {
    removeHelp(ev);
    imageName = inputTitle.value;
    message.info("Recherche…");
    worker.postMessage({ title: inputTitle.value });
    ev.preventDefault();
  });
  worker.addEventListener("message", (ev) => {
    const { error, picture } = ev.data;
    if (error) {
      message.error(error);
      return;
    }
    if (picture) {
      imgPicture.src = picture;
    } else {
      message.warning(`L'image ${imageName} n'a pas été trouvé.`)
    }
  });
})();