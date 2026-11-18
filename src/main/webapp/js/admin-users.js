/**
 * admin-users.js
 * Page-specific behavior for `admin-users.jsp`.
 *
 * Requires: admin-core.js (openModal/closeModal/showAlert)
 */

(function (window, document) {
    'use strict';

    function getModalElement(id) {
        if (!id) return null;
        return document.getElementById(id);
    }

    function showUserViewButton(btn) {
        try {
            if (!btn) return;

            let id = btn.getAttribute('data-user-id') || '';
            let fullName = btn.getAttribute('data-full-name') || '';
            let email = btn.getAttribute('data-email') || '';
            let phone = btn.getAttribute('data-phone-number') || '';
            let gender = btn.getAttribute('data-gender') || '';
            let dob = btn.getAttribute('data-date-of-birth') || '';
            let role = btn.getAttribute('data-role') || '';

            if (!id || !fullName) {
                const row = btn.closest('tr');
                if (row) {
                    const tds = row.querySelectorAll('td');
                    if ((!id) && tds.length > 1) id = (tds[1] && tds[1].textContent ? tds[1].textContent.trim() : id);
                    if ((!fullName) && tds.length > 2) fullName = (tds[2] && tds[2].textContent ? tds[2].textContent.trim() : fullName);
                    if ((!email) && tds.length > 3) email = (tds[3] && tds[3].textContent ? tds[3].textContent.trim() : email);
                    if ((!phone) && tds.length > 4) phone = (tds[4] && tds[4].textContent ? tds[4].textContent.trim() : phone);
                    if ((!gender) && tds.length > 5) gender = (tds[5] && tds[5].textContent ? tds[5].textContent.trim() : gender);
                    if ((!dob) && tds.length > 6) dob = (tds[6] && tds[6].textContent ? tds[6].textContent.trim() : dob);
                    if ((!role) && tds.length > 7) role = (tds[7] && tds[7].textContent ? tds[7].textContent.trim() : role);
                }
            }

            const modal = getModalElement('userViewModal');
            if (!modal) {
                console.warn('[admin-users] userViewModal not found');
                return;
            }

            const rows = modal.querySelectorAll('table tr');
            if (rows.length >= 7) {
                rows[0].children[1].textContent = id || '';
                rows[1].children[1].textContent = fullName || '';
                rows[2].children[1].textContent = email || '';
                rows[3].children[1].textContent = phone || '';
                rows[4].children[1].textContent = gender || '';
                rows[5].children[1].textContent = (dob && dob !== '-') ? (dob.indexOf('-') > -1 ? dob.split('-').reverse().join('/') : dob) : '';
                rows[6].children[1].innerHTML = (role === 'admin')
                    ? '<span class="badge badge-danger">Admin</span>'
                    : '<span class="badge badge-info">' + (role || '') + '</span>';
            }

            // Prefill edit modal fields as well
            try {
                const eid = document.getElementById('edit_id');
                if (eid) eid.value = id || '';
                const efull = document.getElementById('edit_full_name');
                if (efull) efull.value = fullName || '';
                const eemail = document.getElementById('edit_email');
                if (eemail) eemail.value = email || '';
                const ephone = document.getElementById('edit_phone_number');
                if (ephone) ephone.value = phone || '';

                const edob = document.getElementById('edit_date_of_birth');
                if (edob) {
                    if (dob) {
                        if (dob.indexOf('-') > -1) {
                            edob.value = dob;
                        } else if (dob.indexOf('/') > -1) {
                            const parts = dob.split('/');
                            if (parts.length === 3) edob.value = parts[2] + '-' + parts[1] + '-' + parts[0];
                            else edob.value = '';
                        } else {
                            edob.value = dob;
                        }
                    } else {
                        edob.value = '';
                    }
                }

                const egender = document.getElementById('edit_gender');
                if (egender) egender.value = gender || 'Không xác định';

                const erole = document.getElementById('edit_role');
                if (erole) erole.value = role || 'user';
            } catch (err) {
                console.warn('[admin-users] Failed to prefill edit form from view modal', err);
            }

            window.openModal('userViewModal');
        } catch (err) {
            console.error('[admin-users] showUserViewButton error', err);
        }
    }

    function showUserEditButton(btn) {
        try {
            if (!btn) return;
            const id = btn.getAttribute('data-user-id') || '';
            const full = btn.getAttribute('data-full-name') || '';
            const email = btn.getAttribute('data-email') || '';
            const phone = btn.getAttribute('data-phone-number') || '';
            const gender = btn.getAttribute('data-gender') || '';
            const role = btn.getAttribute('data-role') || '';
            const dob = btn.getAttribute('data-date-of-birth') || '';

            const eidEl = document.getElementById('edit_id');
            if (eidEl) eidEl.value = id;
            const efull = document.getElementById('edit_full_name');
            if (efull) efull.value = full;
            const eemail = document.getElementById('edit_email');
            if (eemail) eemail.value = email;
            const ephone = document.getElementById('edit_phone_number');
            if (ephone) ephone.value = phone;

            const egender = document.getElementById('edit_gender');
            if (egender) egender.value = gender;
            const erole = document.getElementById('edit_role');
            if (erole) erole.value = role;

            const edob = document.getElementById('edit_date_of_birth');
            if (edob) {
                if (dob) {
                    if (dob.indexOf('-') > -1) {
                        edob.value = dob;
                    } else if (dob.indexOf('/') > -1) {
                        const parts = dob.split('/');
                        if (parts.length === 3) edob.value = parts[2] + '-' + parts[1] + '-' + parts[0];
                        else edob.value = '';
                    } else {
                        edob.value = dob;
                    }
                } else {
                    edob.value = '';
                }
            }

            const epass = document.getElementById('edit_password');
            if (epass) epass.value = '';

            window.openModal('userEditModal');
        } catch (err) {
            console.error('[admin-users] showUserEditButton error', err);
        }
    }

    // Bind events (support both old and new markup)
    document.addEventListener('DOMContentLoaded', function () {
        try {
            const viewBtns = document.querySelectorAll('.btn-view-user');
            for (let i = 0; i < viewBtns.length; i++) {
                const btn = viewBtns[i];
                if (btn.getAttribute('data-listener-bound')) continue;
                btn.addEventListener('click', function (e) {
                    e.preventDefault();
                    showUserViewButton(btn);
                });
                btn.setAttribute('data-listener-bound', '1');
            }

            const editBtns = document.querySelectorAll('.btn-edit-user');
            for (let j = 0; j < editBtns.length; j++) {
                const btn2 = editBtns[j];
                if (btn2.getAttribute('data-listener-bound')) continue;
                btn2.addEventListener('click', function (e) {
                    e.preventDefault();
                    showUserEditButton(btn2);
                });
                btn2.setAttribute('data-listener-bound', '1');
            }
        } catch (err) {
            console.error('[admin-users] Failed to bind view/edit listeners', err);
        }
    });

    // Capture-phase fallback as last resort
    document.addEventListener('click', function (e) {
        try {
            const btnView = e.target && e.target.closest ? e.target.closest('.btn-view-user') : null;
            if (btnView) {
                showUserViewButton(btnView);
                return;
            }
            const btnEdit = e.target && e.target.closest ? e.target.closest('.btn-edit-user') : null;
            if (btnEdit) {
                showUserEditButton(btnEdit);
            }
        } catch (err) {
            console.error('[admin-users] capture click listener error', err);
        }
    }, true);

    // Expose globally (in case JSP has inline onclick)
    window.showUserViewButton = showUserViewButton;
    window.showUserEditButton = showUserEditButton;

})(window, document);

