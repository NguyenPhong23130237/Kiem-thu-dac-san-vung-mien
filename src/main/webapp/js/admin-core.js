/**
 * admin-core.js
 * Shared utilities for all admin JSP pages.
 *
 * This project loads scripts directly in the browser (no bundler),
 * so shared functions are attached to `window` to keep existing
 * JSP inline `onclick="..."` working.
 */

(function (window, document) {
    'use strict';

    const modalAliasMap = {
        viewModal: 'userViewModal',
        editModal: 'userEditModal'
    };

    function resolveModalEl(modalId) {
        let el = document.getElementById(modalId);
        if (!el && modalAliasMap[modalId]) {
            const alias = modalAliasMap[modalId];
            console.debug('[admin-core] modal id', modalId, 'not found, trying alias', alias);
            el = document.getElementById(alias);
            modalId = alias;
        }
        return {el, modalId};
    }

    function openModal(modalId) {
        const resolved = resolveModalEl(modalId);
        const el = resolved.el;
        modalId = resolved.modalId;

        if (!el) {
            console.warn('[admin-core] openModal: modal not found:', modalId);
            return;
        }
        el.classList.add('active');
        document.body.style.overflow = 'hidden';
    }

    function closeModal(modalId) {
        const resolved = resolveModalEl(modalId);
        const el = resolved.el;
        modalId = resolved.modalId;

        if (!el) {
            console.warn('[admin-core] closeModal: modal not found:', modalId);
            return;
        }
        el.classList.remove('active');
        document.body.style.overflow = 'auto';
    }

    function showAlert(message, type) {
        if (!type) type = 'success';
        const alertDiv = document.createElement('div');
        alertDiv.className = 'alert alert-' + type;
        alertDiv.innerHTML =
            '<i class="fas fa-' + (type === 'success' ? 'check-circle' : 'exclamation-circle') + '"></i> ' +
            (message || '');

        const content = document.querySelector('.admin-content');
        if (content) {
            content.insertBefore(alertDiv, content.firstChild);
        } else {
            document.body.insertBefore(alertDiv, document.body.firstChild);
        }

        setTimeout(function () {
            if (alertDiv && alertDiv.remove) alertDiv.remove();
        }, 3000);
    }

    // Close modal when clicking outside
    document.addEventListener('click', function (e) {
        if (e.target && e.target.classList && e.target.classList.contains('modal')) {
            e.target.classList.remove('active');
            document.body.style.overflow = 'auto';
        }
    });

    // Close modal with Escape key
    document.addEventListener('keydown', function (e) {
        if (e.key === 'Escape') {
            const modals = document.querySelectorAll('.modal.active');
            for (let i = 0; i < modals.length; i++) {
                modals[i].classList.remove('active');
            }
            document.body.style.overflow = 'auto';
        }
    });

    // Expose to global for legacy inline handlers
    window.openModal = openModal;
    window.closeModal = closeModal;
    window.showAlert = showAlert;

})(window, document);

