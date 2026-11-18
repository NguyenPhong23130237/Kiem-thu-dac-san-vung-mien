class GenKeyModal {
    constructor() {
        this.modal = document.getElementById("generateKeyModal");
        this.init();
    }
    init() {
        const openBtn = document.getElementById("openGenerateKeyBtn");
        const closeBtn = document.getElementById("closeGenerateKeyBtn");
        if (openBtn) {
            openBtn.addEventListener("click", () => {
                this.openModal();
            });
        }
        if (closeBtn) {
            closeBtn.addEventListener("click", () => {
                this.closeModal();
            });
        }
        window.addEventListener("click", (event) => {
            if (event.target === this.modal) {
                this.closeModal();
            }
        });
    }
    openModal() {
        this.modal.style.display = "flex";
    }
    closeModal() {
        this.modal.style.display = "none";
    }
}

class ResultKeyModal {
    constructor() {
        this.modal = document.getElementById("resultKeyModal");
        this.init();
    }

    init() {
        if (!this.modal) return;
        this.modal.style.display = "flex";
        const closeBtn = document.getElementById("closeResultModalBtn");
        if (closeBtn) {
            closeBtn.addEventListener("click", () => {
                const url = new URL(window.location.href);
                url.searchParams.set("clearNew", "1");
                window.location.href = url.toString();
            });
        }
        window.addEventListener("click", (event) => {
            if (event.target === this.modal) {
                this.closeModal();
            }
        });
    }
    closeModal() {
        this.modal.style.display = "none";
    }
}
document.addEventListener("DOMContentLoaded", () => {
    new GenKeyModal();
    new ResultKeyModal();
});

function confirmLostKey() {
    return window.confirm("Bạn chắc chắn muốn báo mất key? Key sẽ bị vô hiệu hóa ngay lập tức.");
}

document.addEventListener("DOMContentLoaded", () => {
    new GenKeyModal();
    new ResultKeyModal();

    const triggerUploadBtn = document.getElementById("triggerUploadBtn");
    const directUploadForm = document.getElementById("directUploadForm");

    if (triggerUploadBtn && directUploadForm) {
        // 1. Tạo một thẻ input file ẩn để chứa dữ liệu chọn file
        const fileInput = document.createElement("input");
        fileInput.type = "file";
        fileInput.name = "keyFile";
        fileInput.accept = ".pem";
        fileInput.style.display = "none";

        directUploadForm.appendChild(fileInput);

        // 2. Bắt sự kiện: Click vào nút thì mở hộp thoại chọn file
        triggerUploadBtn.addEventListener("click", () => {
            fileInput.click();
        });

        // 3. Bắt sự kiện: Ngay khi người dùng chọn file xong thì tự động Submit form
        fileInput.addEventListener("change", () => {
            if (fileInput.files && fileInput.files.length > 0) {
                directUploadForm.submit();
            }
        });
    }
});
