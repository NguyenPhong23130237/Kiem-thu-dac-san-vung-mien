/**
 * admin-brands.js
 * Page-specific behavior for `admin-brands.jsp`.
 */

(function (window, document) {
    'use strict';

    function saveBrand() {
        const form = document.getElementById('addBrandForm');
        if (form && form.checkValidity()) {
            window.showAlert('Đã thêm thương hiệu thành công!', 'success');
            window.closeModal('addModal');
            form.reset();
        } else if (form) {
            form.reportValidity();
        }
    }

    function updateBrand() {
        const form = document.getElementById('editBrandForm');
        if (form && form.checkValidity()) {
            window.showAlert('Đã cập nhật thương hiệu thành công!', 'success');
            window.closeModal('editModal');
        } else if (form) {
            form.reportValidity();
        }
    }

    // Fill edit modal from table row button attributes
    document.addEventListener('click', function (e) {
        const btn = e.target && e.target.closest ? e.target.closest('.js-edit-brand') : null;
        if (!btn) return;

        const idEl = document.getElementById('editBrandId');
        const nameEl = document.getElementById('editBrandName');
        const infoEl = document.getElementById('editBrandInfo');

        if (idEl) idEl.value = btn.getAttribute('data-id') || btn.dataset.id || '';
        if (nameEl) nameEl.value = btn.getAttribute('data-name') || btn.dataset.name || '';
        if (infoEl) infoEl.value = btn.getAttribute('data-info') || btn.dataset.info || '';

        window.openModal('editModal');
    });

    window.saveBrand = saveBrand;
    window.updateBrand = updateBrand;

})(window, document);

