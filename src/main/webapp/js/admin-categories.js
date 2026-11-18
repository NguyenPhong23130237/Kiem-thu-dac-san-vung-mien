/**
 * admin-categories.js
 * Page-specific behavior for `admin-categories.jsp`.
 */

(function (window, document) {
    'use strict';

    // Open edit modal and populate form fields
    document.addEventListener('click', function (e) {
        const btn = e.target && e.target.closest ? e.target.closest('.js-edit-category') : null;
        if (!btn) return;

        e.preventDefault();

        const id = btn.getAttribute('data-id') || (btn.dataset ? btn.dataset.id : '') || '';
        const name = btn.getAttribute('data-name') || (btn.dataset ? btn.dataset.name : '') || '';

        const idEl = document.getElementById('editCategoryId');
        const nameEl = document.getElementById('editCategoryName');
        if (idEl) idEl.value = id;
        if (nameEl) nameEl.value = name;

        window.openModal('editModal');
    });

    function saveCategory() {
        const form = document.getElementById('addCategoryForm');
        if (form && form.checkValidity()) {
            window.showAlert('Đã thêm danh mục thành công!', 'success');
            window.closeModal('addModal');
            form.reset();
        } else if (form) {
            form.reportValidity();
        }
    }

    function updateCategory() {
        const form = document.getElementById('editCategoryForm');
        if (form && form.checkValidity()) {
            window.showAlert('Đã cập nhật danh mục thành công!', 'success');
            window.closeModal('editModal');
        } else if (form) {
            form.reportValidity();
        }
    }

    window.saveCategory = saveCategory;
    window.updateCategory = updateCategory;

})(window, document);

