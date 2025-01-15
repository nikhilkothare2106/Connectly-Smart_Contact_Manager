let a = document.querySelector("#image_file_input");
a.addEventListener("change", function (event) {
  let file = event.target.files[0];
  let reader = new FileReader();
  reader.onload = function () {
    document.querySelector("#upload_image_preview").src = reader.result;
  };
  reader.readAsDataURL(file);
});
