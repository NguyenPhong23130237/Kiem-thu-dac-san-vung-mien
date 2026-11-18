/**
 * admin-banners.js
 * Page-specific behavior for `admin-banners.jsp`.
 */

(function (window, document) {
    'use strict';

    function byId(id) {
        return document.getElementById(id);
    }

    function isImageFile(file) {
        return !!(file && file.type && file.type.indexOf('image/') === 0);
    }

    function setPreviewImage(img, file) {
        if (!img || !file) return;

        // Cleanup previous object URL (if any)
        const prev = img.getAttribute('data-object-url');
        if (prev) {
            try { URL.revokeObjectURL(prev); } catch (e) { /* ignore */ }
            img.removeAttribute('data-object-url');
        }

        const url = URL.createObjectURL(file);
        img.src = url;
        img.style.display = 'block';
        img.setAttribute('data-object-url', url);
    }

    function bindClosestPreview(inputId) {
        const input = byId(inputId);
        if (!input) return;

        input.addEventListener('change', function () {
            const file = input.files && input.files[0] ? input.files[0] : null;
            if (!file) return;

            if (!isImageFile(file)) {
                if (window.showAlert) window.showAlert('Vui lòng chọn file hình ảnh (PNG/JPG/WebP).', 'error');
                return;
            }

            const wrap = input.closest ? input.closest('.file-upload') : null;
            const img = wrap ? wrap.querySelector('img.image-preview') : null;
            if (!img) return;
            setPreviewImage(img, file);
        });
    }

    function initBannerImagePreviews() {
        // Add modal preview
        bindClosestPreview('bannerImage');
        // Edit modal "new image" preview
        bindClosestPreview('editBannerImage');
    }

    function saveBanner() {
        const form = document.getElementById('addBannerForm');
        if (form && form.checkValidity()) {
            window.showAlert('Đã thêm banner thành công!', 'success');
            window.closeModal('addModal');
            form.reset();
        } else if (form) {
            form.reportValidity();
        }
    }

    function updateBanner() {
        window.showAlert('Đã cập nhật banner thành công!', 'success');
        window.closeModal('editModal');
    }

    // Fill edit modal from table row button attributes
    document.addEventListener('click', function (e) {
        const btn = e.target && e.target.closest ? e.target.closest('.js-edit-banner') : null;
        if (!btn) return;

        const idEl = byId('editBannerId');
        if (idEl) idEl.value = btn.getAttribute('data-id') || (btn.dataset ? btn.dataset.id : '') || '';

        const preview = document.getElementById('editBannerPreview');
        if (preview) preview.src = btn.getAttribute('data-image') || btn.dataset.image || '';

        window.openModal('editModal');
    });

    if (document.readyState === 'loading') {
        document.addEventListener('DOMContentLoaded', initBannerImagePreviews);
    } else {
        initBannerImagePreviews();
    }

    window.saveBanner = saveBanner;
    window.updateBanner = updateBanner;

})(window, document);

