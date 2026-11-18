/**
 * admin-news.js
 * Page-specific behavior for admin news pages.
 *
 * Convention:
 * - Keep scripts small and predictable like other admin pages.
 * - Prefer server-side search (GET /admin/news?search=...).
 * - This file supports both list page (admin-news.jsp) and form page (news-form.jsp)
 *   by using DOM guards.
 */

(function (window, document) {
    'use strict';

    function normalizeText(s) {
        return String(s || '').toLowerCase().trim();
    }

    // ---------- List page helpers (admin-news.jsp) ----------

    function enhanceSearchForm() {
        const form = document.querySelector('.table-actions form[action*="/admin/news"]');
        if (!form) return;

        const input = form.querySelector('input[name="search"]');
        if (!input) return;

        // Add a small clear button (non-breaking; only appears when JS loads)
        const clearBtn = document.createElement('button');
        clearBtn.type = 'button';
        clearBtn.className = 'btn btn-warning';
        clearBtn.title = 'Xóa tìm kiếm';
        clearBtn.innerHTML = '<i class="fas fa-times"></i>';
        clearBtn.style.padding = '10px 14px';
        clearBtn.addEventListener('click', function () {
            if (!input.value) return;
            input.value = '';
            form.submit();
        });

        // Insert clear button right after the submit/search button
        const buttons = form.querySelectorAll('button');
        const submitBtn = buttons.length ? buttons[buttons.length - 1] : null;
        if (submitBtn && submitBtn.parentNode === form) {
            submitBtn.insertAdjacentElement('afterend', clearBtn);
        } else {
            form.appendChild(clearBtn);
        }
    }

    // Optional: quick client-side filter when user types (without extra requests).
    // Only activates if the table exists and there is more than 1 row.
    function enableClientSideFilter() {
        const form = document.querySelector('.table-actions form[action*="/admin/news"]');
        const input = form ? form.querySelector('input[name="search"]') : null;
        const table = document.querySelector('.data-table');
        if (!input || !table) return;

        const tbody = table.querySelector('tbody');
        if (!tbody) return;

        const rows = Array.prototype.slice.call(tbody.querySelectorAll('tr'));
        if (rows.length <= 1) return;

        // Create a "no results" row
        const emptyRow = document.createElement('tr');
        emptyRow.innerHTML = '<td colspan="6" style="text-align:center; padding: 16px; color:#666;">Không tìm thấy bài viết phù hợp.</td>';
        emptyRow.style.display = 'none';
        tbody.appendChild(emptyRow);

        input.addEventListener('input', function () {
            const q = normalizeText(input.value);
            let visible = 0;
            for (let i = 0; i < rows.length; i++) {
                const row = rows[i];
                const text = normalizeText(row.textContent);
                const show = (!q || text.indexOf(q) !== -1);
                row.style.display = show ? '' : 'none';
                if (show) visible++;
            }
            emptyRow.style.display = (visible === 0) ? '' : 'none';
        });
    }

    function initListPage() {
        // Guard: only run on list page
        const hasTable = !!document.querySelector('.data-table-container .data-table');
        const hasActions = !!document.querySelector('.data-table-container .table-actions');
        if (!hasTable || !hasActions) return;

        enhanceSearchForm();
        enableClientSideFilter();
    }

    // ---------- Form page helpers (news-form.jsp) ----------

    function initFormPage() {
        const form = document.querySelector('form.news-form');
        if (!form) return;

        // Minimal guardrails: prevent double submit
        let submitting = false;
        form.addEventListener('submit', function (e) {
            if (submitting) {
                if (e && typeof e.preventDefault === 'function') e.preventDefault();
                return false;
            }
            submitting = true;

            const btn = (e && e.submitter) ? e.submitter : form.querySelector('button[type="submit"]');
            if (btn) {
                btn.disabled = true;
                btn.setAttribute('data-disabled-by-js', '1');
            }
        });
    }

    function init() {
        initListPage();
        initFormPage();
    }

    if (document.readyState === 'loading') {
        document.addEventListener('DOMContentLoaded', init);
    } else {
        init();
    }

})(window, document);

