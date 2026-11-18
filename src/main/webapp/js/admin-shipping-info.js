/**
 * admin-shipping-info.js
 * Page-specific behavior for `admin-shipping-info.jsp`.
 */

(function (window, document) {
    'use strict';

    function showShippingView(btn) {
        const el = btn;
        if (!el) return;

        function setText(id, v) {
            const node = document.getElementById(id);
            if (node) node.innerText = (v == null ? '' : String(v));
        }

        setText('view-id', el.getAttribute('data-id'));
        setText('view-user-id', el.getAttribute('data-user-id'));
        setText('view-receiver-name', el.getAttribute('data-receiver-name'));
        setText('view-receiver-phone', el.getAttribute('data-receiver-phone-number'));
        setText('view-province', el.getAttribute('data-province'));
        setText('view-district', el.getAttribute('data-district'));
        setText('view-ward', el.getAttribute('data-ward'));
        setText('view-detailed-address', el.getAttribute('data-detailed-address'));

        const full = (el.getAttribute('data-detailed-address') || '')
            + ', ' + (el.getAttribute('data-ward') || '')
            + ', ' + (el.getAttribute('data-district') || '')
            + ', ' + (el.getAttribute('data-province') || '');
        setText('view-full-address', full);

        window.openModal('viewModal');
    }

    window.showShippingView = showShippingView;

})(window, document);

