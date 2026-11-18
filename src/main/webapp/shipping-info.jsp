<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Thông tin giao hàng</title>
    <link rel="stylesheet" href="css/global.css">
    <link rel="stylesheet" href="css/shipping-info.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
</head>
<body>
<%@ include file="header.jsp" %>

<main>
    <c:if test="${isSelectMode}">
        <div class="back-nav-wrapper">
            <div class="back-nav">
                <a href="checkout" class="back-link">
                    <i class="fa fa-arrow-left"></i>
                    <span>Quay lại</span>
                </a>
            </div>
        </div>
    </c:if>

    <div class="page-header">
        <h1>📦 Thông tin giao hàng</h1>
        <p>Quản lý thông tin giao hàng của bạn tại đây — chỉnh sửa hoặc cập nhật địa chỉ nhanh chóng và tiện lợi.</p>
    </div>

    <div class="address-layout">
        <section class="address-header">
            <form id="address-form" class="address-form" action="add-shipping-info" method="post">
                <c:if test="${isSelectMode}">
                    <input type="hidden" name="mode" value="select">
                </c:if>
                <input type="hidden" name="provinceName" id="provinceName">
                <input type="hidden" name="districtName" id="districtName">
                <input type="hidden" name="wardName" id="wardName">

                <h2 class="form-title">Thêm thông tin giao hàng</h2>
                <div class="row">
                    <div class="form-group">
                        <label><i class="fa fa-user"></i></label>
                        <input type="text" id="name" name="receiverName" placeholder="Tên người nhận" required>
                    </div>
                    <div class="form-group">
                        <label><i class="fa fa-phone"></i></label>
                        <input type="text" id="phone" name="receiverPhoneNumber" placeholder="Số điện thoại người nhận"
                               required>
                    </div>
                </div>

                <div class="row-selects">
                    <select id="province" name="province" required>
                        <option value="">Tỉnh/Thành *</option>
                    </select>
                    <select id="district" name="district" required>
                        <option value="">Quận/Huyện *</option>
                    </select>
                    <select id="ward" name="ward" required>
                        <option value="">Phường/Xã *</option>
                    </select>
                </div>

                <div class="form-group">
                    <label><i class="fa fa-map-marker"></i></label>
                    <input type="text" id="address" name="detailedAddress" placeholder="Địa chỉ cụ thể" required>
                </div>

                <c:if test="${not empty error}">
                    <p style="color:red">${error}</p>
                </c:if>

                <div class="actions">
                    <button type="submit">Thêm</button>
                </div>
            </form>
        </section>
        <section class="address-list">
            <p>Danh sách địa chỉ giao hàng</p>
            <div class="address-list-container">
                <c:choose>
                    <c:when test="${not empty shippingInfos}">
                        <c:forEach items="${shippingInfos}" var="si">
                            <div class="address-item <c:if test='${isSelectMode && si.id == sessionScope.cart.shippingInfo.id}'>selected</c:if>"
                                 data-id="${si.id}"
                                 data-name="${si.receiverName}"
                                 data-phone="${si.receiverPhoneNumber}"
                                 data-province="${si.province}"
                                 data-district="${si.district}"
                                 data-ward="${si.ward}"
                                 data-address="${si.detailedAddress}">
                                <div class="address-info">
                                    <strong class="receiver-name">${si.receiverName}</strong>
                                    <div class="phone">${si.receiverPhoneNumber}</div>
                                    <div class="address-text">
                                            ${si.ward}, ${si.district}, ${si.province}<br>
                                            ${si.detailedAddress}
                                    </div>
                                </div>

                                <div class="address-actions">
                                    <button type="button" class="btn-edit" title="Chỉnh sửa">
                                        <i class="fa-solid fa-pencil"></i>
                                    </button>

                                    <c:if test="${isSelectMode && si.id != sessionScope.cart.shippingInfo.id}">
                                        <form action="choose-shipping-info" method="post">
                                            <input type="hidden" name="shippingInfoId" value="${si.id}">
                                            <button type="submit" class="btn-choose">Chọn</button>
                                        </form>
                                    </c:if>

                                    <c:if test="${!isSelectMode || si.id != sessionScope.cart.shippingInfo.id}">
                                        <form action="delete-shipping-info"
                                              method="post"
                                              class="delete-form">
                                            <c:if test="${isSelectMode}">
                                                <input type="hidden" name="mode" value="select">
                                            </c:if>
                                            <input type="hidden" name="shippingInfoId" value="${si.id}">
                                            <button type="submit" class="btn-delete">
                                                Xóa
                                            </button>
                                        </form>
                                    </c:if>
                                </div>
                            </div>


                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <p style="text-align: center; font-weight: bold">Chưa có địa chỉ giao hàng nào.</p>
                    </c:otherwise>
                </c:choose>
            </div>
        </section>
    </div>

</main>

<%@ include file="footer.jsp" %>

<!-- Popup chỉnh sửa thông tin giao hàng -->
<div id="editState"
     data-edit-id="${editId}"
     data-edit-error="${editError}">
    <div class="modal-overlay" id="editModal">
        <div class="modal-container">
            <div class="modal-header">
                <h3><i class="fa fa-edit"></i> Chỉnh sửa thông tin giao hàng</h3>
                <button type="button" class="modal-close" id="modalClose">
                    <i class="fa fa-times"></i>
                </button>
            </div>
            <form class="modal-body" id="editForm" action="update-shipping-info" method="post">
                <c:if test="${isSelectMode}">
                    <input type="hidden" name="mode" value="select">
                </c:if>
                <input type="hidden" id="editShippingId" name="shippingInfoId">
                <input type="hidden" name="provinceName" id="editProvinceName">
                <input type="hidden" name="districtName" id="editDistrictName">
                <input type="hidden" name="wardName" id="editWardName">
                <div class="form-row">
                    <div class="form-group-modal">
                        <label for="editName">
                            <i class="fa fa-user"></i> Tên người nhận
                        </label>
                        <input type="text" id="editName" name="receiverName" placeholder="Nhập tên người nhận" required>
                    </div>
                    <div class="form-group-modal">
                        <label for="editPhone">
                            <i class="fa fa-phone"></i> Số điện thoại
                        </label>
                        <input type="tel" id="editPhone" name="receiverPhoneNumber" placeholder="Nhập số điện thoại"
                               required>
                    </div>
                </div>

                <div class="form-row three-cols">
                    <div class="form-group-modal">
                        <label for="editProvince">
                            <i class="fa fa-city"></i> Tỉnh/Thành phố
                        </label>
                        <select id="editProvince" name="province" required>
                            <option value="">Tỉnh/Thành *</option>
                        </select>
                    </div>
                    <div class="form-group-modal">
                        <label for="editDistrict">
                            <i class="fa fa-building"></i> Quận/Huyện
                        </label>
                        <select id="editDistrict" name="district" required>
                            <option value="">Quận/Huyện *</option>
                        </select>
                    </div>
                    <div class="form-group-modal">
                        <label for="editWard">
                            <i class="fa fa-map-pin"></i> Phường/Xã
                        </label>
                        <select id="editWard" name="ward" required>
                            <option value="">Phường/Xã *</option>
                        </select>
                    </div>
                </div>

                <div class="form-group-modal">
                    <label for="editAddress">
                        <i class="fa fa-map-marker-alt"></i> Địa chỉ cụ thể
                    </label>
                    <input type="text" id="editAddress" name="detailedAddress"
                           placeholder="Số nhà, tên đường, tòa nhà..."
                           required>
                </div>

                <p id="editErrorMsg" style="color:red; display:none;"></p>

                <div class="form-actions-modal">
                    <button type="button" class="btn-cancel" id="btnCancel">
                        <i class="fa fa-times"></i> Hủy
                    </button>
                    <button type="submit" class="btn-save">
                        <i class="fa fa-save"></i> Lưu thay đổi
                    </button>
                </div>
            </form>
        </div>
    </div>
</div>

<script src="js/shipping-info.js"></script>
</body>
</html>