document.addEventListener("DOMContentLoaded", () => {
    // Lấy tất cả sản phẩm
    const cartItems = document.querySelectorAll(".cart-item");
    const form = document.querySelector("form[data-stock]");
    const stock = parseInt(form.dataset.stock, 10);

    // Gán sự kiện tăng/giảm cho từng sản phẩm
    cartItems.forEach(item => {
        const minusBtn = item.querySelector(".decrease-button");
        const plusBtn = item.querySelector(".increase-button");
        const quantityInput = item.querySelector(".quantity-input");

        minusBtn.addEventListener("click", () => {
            let current = parseInt(quantityInput.value);
            if (current > 1) {
                quantityInput.value = current - 1;
            }
        });

        plusBtn.addEventListener("click", () => {
            let current = parseInt(quantityInput.value);
            if (current < stock) {
                quantityInput.value = current + 1;
            } else {
                alert(`Sản phẩm chỉ còn ${stock} sản phẩm trong kho`);
            }
        });
    });
});
