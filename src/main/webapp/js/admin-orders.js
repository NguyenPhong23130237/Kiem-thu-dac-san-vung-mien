/**
 * admin-orders.js
 * Page-specific behavior for `admin-orders.jsp`.
 *
 * Requires: admin-core.js (openModal/closeModal/showAlert)
 */

(function (window, document) {
    'use strict';

    function byId(id) {
        return document.getElementById(id);
    }

    function mapOrderStatus(status) {
        if (!status) return 'Đang xử lý';
        const raw = String(status).trim();
        if (!raw) return 'Đang xử lý';
        if (raw === 'Đang xử lý' || raw === 'Đang giao' || raw === 'Đã giao' || raw === 'Đã hủy') return raw;

        const low = raw.toLowerCase();
        if (low.indexOf('completed') !== -1 || low.indexOf('đã giao') !== -1 || low.indexOf('da giao') !== -1
            || low.indexOf('delivered') !== -1 || low.indexOf('done') !== -1) return 'Đã giao';
        if (low.indexOf('cancel') !== -1 || low.indexOf('hủy') !== -1 || low.indexOf('huy') !== -1) return 'Đã hủy';
        if (low.indexOf('processing') !== -1 || low.indexOf('xử') !== -1 || low.indexOf('xu ly') !== -1
            || low.indexOf('pending') !== -1) return 'Đang xử lý';
        if (low.indexOf('shipping') !== -1 || low.indexOf('ship') !== -1 || low.indexOf('đang giao') !== -1
            || low.indexOf('giao') !== -1) return 'Đang giao';
        return 'Đang xử lý';
    }

    function isDeliveredStatus(status) {
        return mapOrderStatus(status) === 'Đã giao';
    }

    function createOrderItemRow() {
        const row = document.createElement('div');
        row.className = 'order-item-row';
        row.style.display = 'flex';
        row.style.gap = '10px';
        row.style.alignItems = 'center';

        row.innerHTML = ''
            + '<input type="number" name="productId" min="1" required placeholder="Product ID" style="flex: 1;" />'
            + '<input type="number" name="quantity" min="1" required placeholder="Số lượng" style="width: 140px;" />'
            + '<button type="button" class="btn btn-danger btn-sm" title="Xóa dòng">'
            + '  <i class="fas fa-trash"></i>'
            + '</button>';

        const delBtn = row.querySelector('button');
        if (delBtn) {
            delBtn.addEventListener('click', function () {
                row.remove();
            });
        }
        return row;
    }

    function addOrderItemRow() {
        const wrap = byId('addOrderItems');
        if (!wrap) return;
        wrap.appendChild(createOrderItemRow());
    }

    function initAddOrderModal() {
        const form = byId('addOrderForm');
        const wrap = byId('addOrderItems');
        if (!form || !wrap) return;

        // Ensure at least one row exists
        if (!wrap.children || wrap.children.length === 0) {
            addOrderItemRow();
        }

        form.addEventListener('submit', function (e) {
            // HTML5 validation first
            if (!form.checkValidity()) {
                e.preventDefault();
                form.reportValidity();
                return;
            }

            // Custom: require at least 1 item row
            if (!wrap.children || wrap.children.length === 0) {
                e.preventDefault();
                if (window.showAlert) window.showAlert('Vui lòng thêm ít nhất 1 sản phẩm cho đơn hàng.', 'error');
                return;
            }

            // Custom: avoid duplicated productId (simple safeguard)
            const ids = [];
            const inputs = wrap.querySelectorAll('input[name="productId"]');
            for (let i = 0; i < inputs.length; i++) {
                const v = String(inputs[i].value || '').trim();
                if (!v) continue;
                if (ids.indexOf(v) !== -1) {
                    e.preventDefault();
                    if (window.showAlert) window.showAlert('Product ID bị trùng trong danh sách. Vui lòng gộp số lượng vào 1 dòng.', 'error');
                    inputs[i].focus();
                    return;
                }
                ids.push(v);
            }
        });
    }

    function showOrderDetail(orderId) {
        try {
            if (!orderId) return;
            const tpl = document.getElementById('order-template-' + orderId);
            const modal = document.getElementById('detailModal');
            if (!modal) return;

            const titleEl = modal.querySelector('.modal-header h3');
            if (titleEl) titleEl.innerHTML = '<i class="fas fa-eye"></i> Chi tiết đơn hàng #DH' + orderId;

            const body = modal.querySelector('.modal-body');
            if (body) body.innerHTML = tpl ? tpl.innerHTML : '<p>Không có thông tin chi tiết.</p>';

            const editBtn = modal.querySelector('.modal-footer .btn-warning');
            if (editBtn) {
                const sigStatus = tpl ? tpl.getAttribute('data-sig-status') : '';
                const orderStatus = tpl ? tpl.getAttribute('data-order-status') : '';
                if (sigStatus === 'VALID' && !isDeliveredStatus(orderStatus)) {
                    editBtn.style.display = '';
                    editBtn.onclick = function () {
                        window.closeModal('detailModal');
                        showOrderEdit(orderId, orderStatus);
                    };
                } else {
                    editBtn.style.display = 'none';
                }
            }

            window.openModal('detailModal');
        } catch (err) {
            console.error('[admin-orders] showOrderDetail error', err);
        }
    }

    function showOrderEdit(orderId, status) {
        try {
            if (!orderId) return;
            const modal = document.getElementById('orderEditModal') || document.getElementById('updateModal');
            const form = document.getElementById('editOrderForm') || document.getElementById('updateOrderForm') || (modal ? modal.querySelector('form') : null);
            if (!form) return;

            // Set the hidden id that will be submitted by the form
            const hiddenId = document.getElementById('edit_id');
            if (hiddenId && hiddenId.tagName && hiddenId.tagName.toLowerCase() === 'input') {
                hiddenId.value = String(orderId);
            }

            // (Optional fallback) also set within the form if another id input exists
            const idInput = form.querySelector('input[name="orderId"], input[name="id"]');
            if (idInput && idInput !== hiddenId) idInput.value = String(orderId);

            const statusSelect = form.querySelector('select[name="status"]');
            const mapped = mapOrderStatus(status || '');
            if (isDeliveredStatus(mapped)) {
                if (window.showAlert) window.showAlert('Đơn hàng đã giao không thể cập nhật trạng thái.', 'error');
                return;
            }
            if (statusSelect) {
                const opt = statusSelect.querySelector('option[value="' + mapped + '"]');
                statusSelect.value = opt ? mapped : 'Đang xử lý';
            }

            if (modal) window.openModal(modal.id);
            else window.openModal('orderEditModal');
        } catch (err) {
            console.error('[admin-orders] showOrderEdit error', err);
        }
    }

    // Delegated handlers for table buttons
    document.addEventListener('click', function (e) {
        try {
            if (e.target && e.target.closest) {
                const copySigBtn = e.target.closest('.copy-signature');
                if (copySigBtn) {
                    e.preventDefault();
                    const container = copySigBtn.closest('div');
                    const span = container ? container.querySelector('.shortSignature') : null;
                    const fullText = span ? (span.dataset.full || span.getAttribute('data-full')) : '';
                    if (fullText) {
                        navigator.clipboard.writeText(fullText).then(() => {
                            if (window.showAlert) window.showAlert('Đã copy chữ ký số vào clipboard!', 'success');
                        }).catch(err => {
                            console.error('Copy failed', err);
                            if (window.showAlert) window.showAlert('Không thể copy tự động, vui lòng xem chi tiết.', 'error');
                        });
                    }
                    return;
                }

                const viewSigBtn = e.target.closest('.view-full-signature');
                if (viewSigBtn) {
                    e.preventDefault();
                    const container = viewSigBtn.closest('div');
                    const span = container ? container.querySelector('.shortSignature') : null;
                    const fullText = span ? (span.dataset.full || span.getAttribute('data-full')) : '';
                    if (fullText) {
                        window.prompt('Chi tiết Chữ ký số:', fullText);
                    }
                    return;
                }

                const copyKeyBtn = e.target.closest('.copy-public-key');
                if (copyKeyBtn) {
                    e.preventDefault();
                    const container = copyKeyBtn.closest('div');
                    const span = container ? container.querySelector('.shortPublicKey') : null;
                    const fullText = span ? (span.dataset.full || span.getAttribute('data-full')) : '';
                    if (fullText) {
                        navigator.clipboard.writeText(fullText).then(() => {
                            if (window.showAlert) window.showAlert('Đã copy public key vào clipboard!', 'success');
                        }).catch(err => {
                            console.error('Copy failed', err);
                            if (window.showAlert) window.showAlert('Không thể copy tự động, vui lòng xem chi tiết.', 'error');
                        });
                    }
                    return;
                }

                const viewKeyBtn = e.target.closest('.view-full-public-key');
                if (viewKeyBtn) {
                    e.preventDefault();
                    const container = viewKeyBtn.closest('div');
                    const span = container ? container.querySelector('.shortPublicKey') : null;
                    const fullText = span ? (span.dataset.full || span.getAttribute('data-full')) : '';
                    if (fullText) {
                        window.prompt('Chi tiết Public key:', fullText);
                    }
                    return;
                }
            }

            const el = e.target && e.target.closest ? e.target.closest('.btn-view-order, .btn-edit-order') : null;
            if (!el) return;

            e.preventDefault();

            if (el.classList.contains('btn-view-order')) {
                const orderId = (el.dataset && el.dataset.orderId) ? el.dataset.orderId : el.getAttribute('data-order-id');
                if (orderId) showOrderDetail(orderId);
                return;
            }

            if (el.classList.contains('btn-edit-order')) {
                const orderId = (el.dataset && el.dataset.orderId) ? el.dataset.orderId : el.getAttribute('data-order-id');
                const status = (el.dataset && (el.dataset.status || el.dataset.orderStatus)) || el.getAttribute('data-status') || '';
                if (orderId) {
                    // pre-fill hidden id as early as possible
                    const hiddenId = document.getElementById('edit_id');
                    if (hiddenId && hiddenId.tagName && hiddenId.tagName.toLowerCase() === 'input') {
                        hiddenId.value = String(orderId);
                    }
                    showOrderEdit(orderId, status);
                }
            }
        } catch (err) {
            console.error('[admin-orders] order button delegation error', err);
        }
    });

    // Init add order modal helpers
    if (document.readyState === 'loading') {
        document.addEventListener('DOMContentLoaded', initAddOrderModal);
    } else {
        initAddOrderModal();
    }

    // expose for inline onclick
    window.addOrderItemRow = addOrderItemRow;

    function printOrder() {
        window.print();
    }

    function updateOrderStatus() {
        const form = document.getElementById('editOrderForm')
            || document.getElementById('updateOrderForm')
            || (document.querySelector('#orderEditModal form'));

        if (!form) {
            console.warn('[admin-orders] updateOrderStatus: form not found');
            window.showAlert('Form cập nhật đơn hàng không tìm thấy.', 'error');
            return;
        }

        // Always read from the real hidden field first
        const idInput = document.getElementById('edit_id')
            || form.querySelector('input[name="orderId"], input[name="id"]');
        const statusSelect = form.querySelector('select[name="status"]');
        const orderId = idInput ? String(idInput.value || idInput.getAttribute('value') || '').trim() : '';
        const status = statusSelect ? String(statusSelect.value || statusSelect.getAttribute('value') || '').trim() : '';

        if (!orderId) {
            console.warn('[admin-orders] updateOrderStatus: orderId empty');
            window.showAlert('Không tìm thấy mã đơn hàng.', 'error');
            return;
        }
        if (!status) {
            console.warn('[admin-orders] updateOrderStatus: status empty');
            window.showAlert('Vui lòng chọn trạng thái.', 'error');
            return;
        }

        const action = (window.APP_CTX ? window.APP_CTX + '/UpdateOrderAdminServlet' : '/UpdateOrderAdminServlet');

        const fd = new FormData();
        fd.append('orderId', orderId);
        fd.append('status', status);

        fetch(action, {
            method: 'POST',
            body: fd,
            headers: {'X-Requested-With': 'XMLHttpRequest'},
            credentials: 'same-origin'
        }).then(function (resp) {
            const ct = String(resp.headers.get('content-type') || '').toLowerCase();
            if (ct.indexOf('application/json') !== -1) return resp.json();
            return resp.text().then(function (t) {
                return {success: false, message: t};
            });
        }).then(function (json) {
            if (json && json.success) {
                window.showAlert(json.message || 'Cập nhật trạng thái thành công', 'success');
                window.closeModal('orderEditModal');
                setTimeout(function () {
                    window.location.reload();
                }, 600);
            } else {
                window.showAlert((json && json.message) ? json.message : 'Cập nhật trạng thái thất bại', 'error');
                console.warn('[admin-orders] updateOrderStatus failed response', json);
            }
        }).catch(function (err) {
            console.error('[admin-orders] updateOrderStatus fetch error', err);
            window.showAlert('Có lỗi khi cập nhật trạng thái, thử submit form.', 'error');
            try {
                // Ensure fields are present for servlet in non-AJAX fallback too
                const hid = document.getElementById('edit_id');
                if (hid && hid.tagName && hid.tagName.toLowerCase() === 'input') hid.value = orderId;
                const stSel = form.querySelector('select[name="status"]');
                if (stSel) stSel.value = status;
                if (form && typeof form.submit === 'function') form.submit();
            } catch (e) {
                console.error('[admin-orders] fallback submit failed', e);
            }
        });
    }

    window.printOrder = printOrder;
    window.updateOrderStatus = updateOrderStatus;
    window.showOrderDetail = showOrderDetail;
    window.showOrderEdit = showOrderEdit;

})(window, document);
