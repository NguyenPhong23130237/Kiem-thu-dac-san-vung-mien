const giamButton = document.querySelector(".decrease-button");
const tangButton = document.querySelector(".increase-button");
const soLuong = document.querySelector(".quantity-input");
const form = document.querySelector("form[data-stock]");

const stock = parseInt(form.dataset.stock, 10);

giamButton.addEventListener("click", () => {
    let currentSoLuong = parseInt(soLuong.value);
    if (currentSoLuong > 1) {
        soLuong.value = currentSoLuong - 1;
    }
})

tangButton.addEventListener("click", () => {
    let currentSoLuong = parseInt(soLuong.value);
    if (currentSoLuong < stock) {
        soLuong.value = currentSoLuong + 1;
    } else {
        alert(`Sản phẩm chỉ còn ${stock} sản phẩm trong kho`);
    }
})