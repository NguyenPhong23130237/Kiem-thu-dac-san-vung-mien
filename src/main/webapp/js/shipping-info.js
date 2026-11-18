document.addEventListener("DOMContentLoaded", function () {
    const provinceSelect = document.getElementById("province");
    const districtSelect = document.getElementById("district");
    const wardSelect = document.getElementById("ward");

// Load tỉnh/thành (nếu JSP chưa render sẵn)
    if (provinceSelect.options.length === 1) {
        fetch("https://provinces.open-api.vn/api/p/")
            .then(res => res.json())
            .then(data => {
                data.forEach(p => {
                    provinceSelect.innerHTML +=
                        `<option value="${p.code}">${p.name}</option>`;
                });
            });
    }

// Khi chọn tỉnh → load huyện
    provinceSelect.addEventListener("change", function () {
        districtSelect.innerHTML = '<option value="">Quận/Huyện *</option>';
        wardSelect.innerHTML = '<option value="">Phường/Xã *</option>';

        if (!this.value) return;

        fetch(`https://provinces.open-api.vn/api/p/${this.value}?depth=2`)
            .then(res => res.json())
            .then(data => {
                data.districts.forEach(d => {
                    districtSelect.innerHTML +=
                        `<option value="${d.code}">${d.name}</option>`;
                });
            });
    });

// Khi chọn huyện → load xã
    districtSelect.addEventListener("change", function () {
        wardSelect.innerHTML = '<option value="">Phường/Xã *</option>';

        if (!this.value) return;

        fetch(`https://provinces.open-api.vn/api/d/${this.value}?depth=2`)
            .then(res => res.json())
            .then(data => {
                data.wards.forEach(w => {
                    wardSelect.innerHTML +=
                        `<option value="${w.code}">${w.name}</option>`;
                });
            });
    });

    document.getElementById("address-form").addEventListener("submit", function () {
        document.getElementById("provinceName").value =
            provinceSelect.options[provinceSelect.selectedIndex].text;

        document.getElementById("districtName").value =
            districtSelect.options[districtSelect.selectedIndex].text;

        document.getElementById("wardName").value =
            wardSelect.options[wardSelect.selectedIndex].text;
    });

    // Modal elements
    const editModal = document.getElementById('editModal');
    const modalClose = document.getElementById('modalClose');
    const btnCancel = document.getElementById('btnCancel');

    document.querySelectorAll('.btn-edit').forEach(btn => {
        btn.addEventListener('click', function () {

            const addressItem = this.closest('.address-item');
            const {id, name, phone, province, district, ward, address} = addressItem.dataset;

            // RESET lỗi mỗi lần mở popup
            const errEl = document.getElementById("editErrorMsg");
            errEl.textContent = "";
            errEl.style.display = "none";

            document.getElementById("editShippingId").value = id;
            document.getElementById('editName').value = name;
            document.getElementById('editPhone').value = phone;
            document.getElementById('editAddress').value = address;

            setEditAddress(province, district, ward);

            editModal.classList.add('active');
            document.body.style.overflow = 'hidden';
        });
    });

    // Đóng modal
    function closeModal() {
        editModal.classList.remove('active');
        document.body.style.overflow = '';

        // Clear lỗi khi đóng
        const errEl = document.getElementById("editErrorMsg");
        errEl.textContent = "";
        errEl.style.display = "none";
    }

    modalClose.addEventListener('click', closeModal);
    btnCancel.addEventListener('click', closeModal);

    // Đóng khi click overlay
    editModal.addEventListener('click', function (e) {
        if (e.target === editModal) {
            closeModal();
        }
    });

    // Đóng bằng phím Escape
    document.addEventListener('keydown', function (e) {
        if (e.key === 'Escape' && editModal.classList.contains('active')) {
            closeModal();
        }
    });

    // Submit form
    document.getElementById('editForm').addEventListener('submit', function (e) {
        document.getElementById("editProvinceName").value =
            editProvince.options[editProvince.selectedIndex].text;

        document.getElementById("editDistrictName").value =
            editDistrict.options[editDistrict.selectedIndex].text;

        document.getElementById("editWardName").value =
            editWard.options[editWard.selectedIndex].text;
    });

    function setEditAddress(provinceName, districtName, wardName) {

        const pSelect = document.getElementById('editProvince');
        const dSelect = document.getElementById('editDistrict');
        const wSelect = document.getElementById('editWard');

        // Load tỉnh
        fetch("https://provinces.open-api.vn/api/p/")
            .then(res => res.json())
            .then(provinces => {
                pSelect.innerHTML = '<option value="">-- Chọn Tỉnh --</option>';

                provinces.forEach(p => {
                    const selected = p.name === provinceName ? 'selected' : '';
                    pSelect.innerHTML +=
                        `<option value="${p.code}" ${selected}>${p.name}</option>`;
                });

                // tìm code tỉnh
                const province = provinces.find(p => p.name === provinceName);
                if (!province) return;

                // Load huyện
                return fetch(`https://provinces.open-api.vn/api/p/${province.code}?depth=2`);
            })
            .then(res => res.json())
            .then(pData => {
                dSelect.innerHTML = '<option value="">-- Chọn Huyện --</option>';

                pData.districts.forEach(d => {
                    const selected = d.name === districtName ? 'selected' : '';
                    dSelect.innerHTML +=
                        `<option value="${d.code}" ${selected}>${d.name}</option>`;
                });

                const district = pData.districts.find(d => d.name === districtName);
                if (!district) return;

                // Load xã
                return fetch(`https://provinces.open-api.vn/api/d/${district.code}?depth=2`);
            })
            .then(res => res.json())
            .then(dData => {
                wSelect.innerHTML = '<option value="">-- Chọn Xã --</option>';

                dData.wards.forEach(w => {
                    const selected = w.name === wardName ? 'selected' : '';
                    wSelect.innerHTML +=
                        `<option value="${w.code}" ${selected}>${w.name}</option>`;
                });
            });
    }

    const editProvince = document.getElementById('editProvince');
    const editDistrict = document.getElementById('editDistrict');
    const editWard = document.getElementById('editWard');

    editProvince.addEventListener('change', function () {
        editDistrict.innerHTML = '<option value="">Quận/Huyện *</option>';
        editWard.innerHTML = '<option value="">Phường/Xã *</option>';

        if (!this.value) return;

        fetch(`https://provinces.open-api.vn/api/p/${this.value}?depth=2`)
            .then(res => res.json())
            .then(data => {
                data.districts.forEach(d => {
                    editDistrict.innerHTML +=
                        `<option value="${d.code}">${d.name}</option>`;
                });
            });
    });

    editDistrict.addEventListener('change', function () {
        editWard.innerHTML = '<option value="">Phường/Xã *</option>';

        if (!this.value) return;

        fetch(`https://provinces.open-api.vn/api/d/${this.value}?depth=2`)
            .then(res => res.json())
            .then(data => {
                data.wards.forEach(w => {
                    editWard.innerHTML +=
                        `<option value="${w.code}">${w.name}</option>`;
                });
            });
    });
    const stateEl = document.getElementById("editState");

    const editId = stateEl?.dataset.editId?.trim();
    const editError = stateEl?.dataset.editError?.trim();

    if (editId && editError) {
        const item = document.querySelector(`.address-item[data-id='${editId}']`);

        if (item) {
            const {id, name, phone, province, district, ward, address} = item.dataset;

            // Đổ dữ liệu
            document.getElementById("editShippingId").value = id;
            document.getElementById("editName").value = name;
            document.getElementById("editPhone").value = phone;
            document.getElementById("editAddress").value = address;

            // Load tỉnh / huyện / xã
            setEditAddress(province, district, ward);

            // Hiện lỗi
            const errEl = document.getElementById("editErrorMsg");
            errEl.textContent = editError;
            errEl.style.display = "block";

            // Mở modal
            editModal.classList.add("active");
            document.body.style.overflow = "hidden";
        }
    }

});