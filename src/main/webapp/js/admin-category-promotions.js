/**
 * admin-category-promotions.js
 * Page-specific behavior for `admin-category-promotions.jsp`.
 */

(function (window, document) {
    'use strict';

    function saveCategoryDiscount() {
        const form = document.getElementById('addCategoryDiscountForm');
        if (form && form.checkValidity()) {
            window.showAlert('Đã thêm khuyến mãi danh mục thành công!', 'success');
            window.closeModal('addModal');
            form.reset();
        } else if (form) {
            form.reportValidity();
        }
    }

    function updateCategoryDiscount() {
        const form = document.getElementById('editCategoryDiscountForm');
        if (form && form.checkValidity()) {
            window.showAlert('Đã cập nhật khuyến mãi danh mục thành công!', 'success');
            window.closeModal('editModal');
        } else if (form) {
            form.reportValidity();
        }
    }

    // Handle category discount edit buttons (class .js-edit-category-discount)
    document.addEventListener('DOMContentLoaded', function () {
        document.body.addEventListener('click', function (e) {
            const btn = e.target && e.target.closest ? e.target.closest('.js-edit-category-discount') : null;
            if (!btn) return;
            e.preventDefault();

            try {
                const id = btn.getAttribute('data-id') || btn.dataset.id || '';
                const categoryId = btn.getAttribute('data-category') || btn.dataset.category || '';
                const discountPercentage = btn.getAttribute('data-discount') || btn.dataset.discount || '';
                const start = btn.getAttribute('data-start') || btn.dataset.start || '';
                const end = btn.getAttribute('data-end') || btn.dataset.end || '';

                const form = document.getElementById('editCategoryDiscountForm');
                if (form) {
                    const idEl = document.getElementById('editId');
                    if (idEl) idEl.value = id;
                    const categoryEl = document.getElementById('editCategory');
                    if (categoryEl) categoryEl.value = categoryId;
                    const discountEl = document.getElementById('editDiscount');
                    if (discountEl) discountEl.value = discountPercentage;
                    const startEl = document.getElementById('editStart');
                    if (startEl) startEl.value = start;
                    const endEl = document.getElementById('editEnd');
                    if (endEl) endEl.value = end;
                    window.openModal('editModal');
                    return;
                }

                if (id) {
                    const ctx = (window.APP_CTX || '');
                    window.location.href = ctx + '/admin/category-discounts?action=edit&id=' + encodeURIComponent(id);
                }
            } catch (err) {
                console.error('[admin-category-promotions] js-edit-category-discount handler error', err);
            }
        });
    });

    window.saveCategoryDiscount = saveCategoryDiscount;
    window.updateCategoryDiscount = updateCategoryDiscount;

})(window, document);

