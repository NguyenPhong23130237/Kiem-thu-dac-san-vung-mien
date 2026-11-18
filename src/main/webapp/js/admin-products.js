/**
 * admin-products.js
 * Page-specific behavior for `admin-products.jsp`.
 *
 * Requires: admin-core.js (openModal/closeModal/showAlert)
 * Uses TinyMCE if available.
 */

(function (window, document) {
    'use strict';

    function byId(id) {
        return document.getElementById(id);
    }

    function setVal(id, v) {
        if (id === 'editProductDescription' && window.tinymce && tinymce.get(id)) {
            tinymce.get(id).setContent(v ?? '');
            return true;
        }
        const el = byId(id);
        if (!el) return false;
        el.value = (v ?? '');
        return true;
    }

    function setText(id, v) {
        const el = byId(id);
        if (!el) return false;
        el.textContent = (v ?? '');
        return true;
    }

    function buildCtx() {
        return (window.APP_CTX || '');
    }

    function fetchProduct(id) {
        const ctx = buildCtx();
        const url = ctx + '/admin/products/view?id=' + encodeURIComponent(id);
        return fetch(url, {
            method: 'GET',
            headers: {'Accept': 'application/json'}
        }).then(function (res) {
            if (!res.ok) throw new Error('HTTP ' + res.status);
            return res.json();
        });
    }

    function saveProduct() {
        const form = byId('addProductForm');
        if (form && form.checkValidity()) {
            window.showAlert('Đã thêm sản phẩm thành công!', 'success');
            window.closeModal('addModal');
            form.reset();
        } else if (form) {
            form.reportValidity();
        }
    }

    function updateProduct() {
        const form = byId('editProductForm');
        if (form && form.checkValidity()) {
            window.showAlert('Đã cập nhật sản phẩm thành công!', 'success');
            window.closeModal('editModal');
        } else if (form) {
            form.reportValidity();
        }
    }

    function bindImagePreview(inputId, imgSelectorOrId) {
        const input = byId(inputId);
        if (!input) return;

        const img = (function () {
            if (!imgSelectorOrId) return null;
            if (imgSelectorOrId.charAt && imgSelectorOrId.charAt(0) === '#') {
                return document.querySelector(imgSelectorOrId);
            }
            const by = byId(imgSelectorOrId);
            if (by) return by;
            return null;
        })();

        if (!img) return;

        input.addEventListener('change', function () {
            const file = input.files && input.files[0] ? input.files[0] : null;
            if (!file) {
                // do not hide existing image for edit; just keep as-is
                return;
            }
            if (!file.type || file.type.indexOf('image/') !== 0) {
                window.showAlert('Vui lòng chọn đúng định dạng ảnh.', 'error');
                return;
            }

            const url = URL.createObjectURL(file);
            img.src = url;
            img.style.display = 'block';

            // cleanup previous object URL to prevent memory leaks
            const prev = img.getAttribute('data-object-url');
            if (prev) {
                try { URL.revokeObjectURL(prev); } catch (e) { /* ignore */ }
            }
            img.setAttribute('data-object-url', url);
        });
    }

    function initAddEditImagePreviews() {
        // Add product: input inside addModal, preview is the img.image-preview inside the same file-upload
        const addInput = byId('addProductImage');
        if (addInput) {
            const addPreview = addInput.closest('.file-upload') ? addInput.closest('.file-upload').querySelector('img.image-preview') : null;
            if (addPreview) {
                // bind directly
                addInput.addEventListener('change', function () {
                    const file = addInput.files && addInput.files[0] ? addInput.files[0] : null;
                    if (!file) return;
                    if (!file.type || file.type.indexOf('image/') !== 0) {
                        window.showAlert('Vui lòng chọn đúng định dạng ảnh.', 'error');
                        return;
                    }
                    const url = URL.createObjectURL(file);
                    addPreview.src = url;
                    addPreview.style.display = 'block';
                    const prev = addPreview.getAttribute('data-object-url');
                    if (prev) {
                        try { URL.revokeObjectURL(prev); } catch (e) { /* ignore */ }
                    }
                    addPreview.setAttribute('data-object-url', url);
                });
            }
        }

        // Edit product: input is editProductImageFile, preview img is #editProductImage
        bindImagePreview('editProductImageFile', 'editProductImage');
    }

    if (document.readyState === 'loading') {
        document.addEventListener('DOMContentLoaded', initAddEditImagePreviews);
    } else {
        initAddEditImagePreviews();
    }

    // Handle view/edit buttons
    document.addEventListener('click', function (e) {
        const editBtn = e.target && e.target.closest ? e.target.closest('.js-edit-product') : null;
        const viewBtn = e.target && e.target.closest ? e.target.closest('.js-view-product') : null;
        if (!editBtn && !viewBtn) return;

        const hasProductUI = !!(byId('editModal') || byId('viewModal') || byId('addProductForm') || byId('editProductForm'));
        if (!hasProductUI) return;

        if (editBtn) {
            if (!byId('editModal') || !byId('editProductId')) return;

            const id = (editBtn.dataset && editBtn.dataset.id) ? editBtn.dataset.id : editBtn.getAttribute('data-id');
            if (!id) {
                console.warn('[admin-products] Missing data-id on .js-edit-product button', editBtn);
                return;
            }

            e.preventDefault();

            fetchProduct(id).then(function (p) {
                setVal('editProductId', p.id);
                setVal('editProductName', p.name);
                setVal('editProductPrice', p.price);
                setVal('editProductQuantity', p.quantity);
                setVal('editProductSoldQuantity', p.soldQuantity ?? '0');
                setVal('editProductBrandId', p.brandId);
                setVal('editProductCategoryId', p.categoryId);
                setVal('editProductRegion', p.region);
                setVal('editProductSpecification', (p.specification ?? '').toString().trim());
                setVal('editProductDescription', p.description);

                const img = byId('editProductImage');
                if (img) img.src = p.image || '';

                window.openModal('editModal');
            }).catch(function (err) {
                console.error('[admin-products] failed to load product for edit', err);

                // Fallback: use dataset values if API fails
                setVal('editProductId', editBtn.dataset.id);
                setVal('editProductName', editBtn.dataset.name);
                setVal('editProductPrice', editBtn.dataset.price);
                setVal('editProductQuantity', editBtn.dataset.quantity);
                setVal('editProductSoldQuantity', editBtn.dataset.soldQuantity || '0');
                setVal('editProductBrandId', editBtn.dataset.brandId);
                setVal('editProductCategoryId', editBtn.dataset.categoryId);
                setVal('editProductRegion', editBtn.dataset.region);
                setVal('editProductSpecification', (editBtn.dataset.specification || '').trim());
                setVal('editProductDescription', editBtn.dataset.description);

                const img2 = byId('editProductImage');
                if (img2) img2.src = editBtn.dataset.image || '';

                window.openModal('editModal');
            });
            return;
        }

        if (viewBtn) {
            const id2 = viewBtn.dataset.id || viewBtn.getAttribute('data-id');
            if (!id2) return;

            e.preventDefault();

            fetchProduct(id2).then(function (p) {
                if (!byId('viewModal')) {
                    console.warn('[admin-products] viewModal not found on page');
                    return;
                }

                setText('viewProductId', p.id);
                setText('viewProductName', p.name);
                setText('viewProductPrice', (p.price ?? '') + 'đ');
                setText('viewProductBrand', p.brandName);
                setText('viewProductCategory', (p.categoryName ?? p.categoryId ?? ''));
                setText('viewProductRegion', p.region);
                setText('viewProductSpec', p.specification);
                setText('viewProductQuantity', p.quantity);
                setText('viewProductSold', p.soldQuantity);
                setText('viewProductCreated', p.createdAt);

                const desc = byId('viewProductDescription');
                if (desc) desc.innerHTML = p.description || '';

                const elImg = byId('viewProductImage');
                if (elImg) {
                    const ctx = buildCtx();
                    const imgStr = (p.image || '').toString();
                    const imgSrc = imgStr && !imgStr.startsWith('http') ? (ctx + '/' + imgStr.replace(/^\/+/, '')) : imgStr;
                    elImg.src = imgStr ? imgSrc : '';
                    elImg.alt = p.name ?? '';
                }

                // Optional prefill edit form for the "Chỉnh sửa" button inside view modal
                if (byId('editProductId')) {
                    setVal('editProductId', p.id);
                    setVal('editProductName', p.name);
                    setVal('editProductPrice', p.price);
                    setVal('editProductQuantity', p.quantity);
                    setVal('editProductSoldQuantity', p.soldQuantity ?? '0');
                    setVal('editProductBrandId', p.brandId);
                    setVal('editProductCategoryId', p.categoryId);
                    setVal('editProductRegion', p.region);
                    setVal('editProductSpecification', p.specification);
                    setVal('editProductDescription', p.description || '');

                    const img3 = byId('editProductImage');
                    if (img3) img3.src = p.image || '';
                }

                window.openModal('viewModal');
            }).catch(function (err) {
                console.error('[admin-products] failed to load product details', err);
                alert('Không thể tải chi tiết sản phẩm.');
            });
        }
    });

    // Expose for inline onclick
    window.saveProduct = saveProduct;
    window.updateProduct = updateProduct;

})(window, document);


