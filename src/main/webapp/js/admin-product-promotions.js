/**
 * admin-product-promotions.js
 * Page-specific behavior for `admin-product-promotions.jsp`.
 */

(function (window, document) {
    'use strict';

    function saveProductDiscount() {
        const form = document.getElementById('addDiscountForm');
        if (form && form.checkValidity()) {
            window.showAlert('Đã thêm khuyến mãi sản phẩm thành công!', 'success');
            window.closeModal('addModal');
            form.reset();
        } else if (form) {
            form.reportValidity();
        }
    }

    function updateProductDiscount() {
        const form = document.getElementById('editDiscountForm');
        if (form && form.checkValidity()) {
            window.showAlert('Đã cập nhật khuyến mãi sản phẩm thành công!', 'success');
            window.closeModal('editModal');
        } else if (form) {
            form.reportValidity();
        }
    }

    // Handle edit buttons (class .js-edit-discount)
    document.addEventListener('DOMContentLoaded', function () {
        document.body.addEventListener('click', function (e) {
            const btn = e.target && e.target.closest ? e.target.closest('.js-edit-discount') : null;
            if (!btn) return;
            e.preventDefault();

            try {
                const id = btn.getAttribute('data-id') || btn.dataset.id || '';
                const productId = btn.getAttribute('data-product') || btn.dataset.product || '';
                const discountPercentage = btn.getAttribute('data-discount') || btn.dataset.discount || '';
                const start = btn.getAttribute('data-start') || btn.dataset.start || '';
                const end = btn.getAttribute('data-end') || btn.dataset.end || '';

                const form = document.getElementById('editDiscountForm');
                if (form) {
                    const idEl = form.querySelector('input[name="id"]');
                    if (idEl) idEl.value = id;
                    const productEl = form.querySelector('select[name="product"]');
                    if (productEl) productEl.value = productId;
                    const discountEl = form.querySelector('input[name="discount_percentage"]');
                    if (discountEl) discountEl.value = discountPercentage;
                    const startEl = form.querySelector('input[name="start_date"]');
                    if (startEl) startEl.value = start;
                    const endEl = form.querySelector('input[name="end_date"]');
                    if (endEl) endEl.value = end;
                    window.openModal('editModal');
                    return;
                }

                if (id) {
                    const ctx = (window.APP_CTX || '');
                    window.location.href = ctx + '/admin/product-discounts?action=edit&id=' + encodeURIComponent(id);
                }
            } catch (err) {
                console.error('[admin-product-promotions] js-edit-discount handler error', err);
            }
        });
    });

    window.saveProductDiscount = saveProductDiscount;
    window.updateProductDiscount = updateProductDiscount;

})(window, document);

