(function () {
  const formPicture = document.getElementById("form-picture");
  const inputPicture = document.getElementById("input-picture");
  const inputTitle = document.getElementById("input-title");
  const resetButton = document.getElementById("button-reset");
  const imgPicture = document.getElementById("img-picture");
  let imageName = "";
  formPicture.addEventListener("submit", (ev) => {
    ev.preventDefault();
  })
  inputPicture.addEventListener("change", () => {
    const file = inputPicture.files[0];
    if (!inputTitle.value || inputTitle.value === imageName) {
      const { name } = file;
      imageName = inputTitle.value = name.substring(0, name.lastIndexOf(".") - 1);
    }
    imgPicture.classList.remove("is-hidden");
    imgPicture.src = URL.createObjectURL(file);
  });
  resetButton.addEventListener("click", () => {
    imageName = "";
    imgPicture.classList.add("is-hidden");
  })
})()