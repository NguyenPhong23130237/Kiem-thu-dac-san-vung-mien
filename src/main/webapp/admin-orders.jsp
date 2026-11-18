<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
    if (request.getAttribute("orders") == null && !request.getRequestURI().endsWith("/admin/orders")) {
        String qs = request.getQueryString();
        response.sendRedirect(request.getContextPath() + "/admin/orders" + (qs != null ? ("?" + qs) : ""));
        return;
    }
%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Quản lý Đơn hàng - Admin</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/css/admin.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body>
<!-- Sidebar -->
<aside class="admin-sidebar">
    <div class="logo">
        <h2><i class="fas fa-store"></i> DacSanVungMien</h2>
        <p>Quản trị hệ thống</p>
    </div>
    <nav>
        <ul>
            <li><a href="<%=request.getContextPath()%>/admin"><i class="fas fa-home"></i> <span>Dashboard</span></a></li>

            <li class="menu-section">Quản lý sản phẩm</li>
            <li><a href="<%=request.getContextPath()%>/admin/products"><i class="fas fa-box"></i> <span>Sản phẩm</span></a></li>
            <li><a href="<%=request.getContextPath()%>/admin/categories"><i class="fas fa-tags"></i> <span>Danh mục</span></a></li>
            <li><a href="<%=request.getContextPath()%>/admin/brands"><i class="fas fa-trademark"></i> <span>Thương hiệu</span></a></li>
            <li><a href="<%=request.getContextPath()%>/admin/banners"><i class="fas fa-image"></i> <span>Banner</span></a></li>

            <li class="menu-section">Khuyến mãi</li>
            <li><a href="<%=request.getContextPath()%>/admin/product-discounts"><i class="fas fa-percent"></i> <span>KM Sản phẩm</span></a></li>
            <li><a href="<%=request.getContextPath()%>/admin/category-discounts"><i class="fas fa-tag"></i> <span>KM Danh mục</span></a></li>

            <li class="menu-section">Đơn hàng</li>
            <li><a href="<%=request.getContextPath()%>/admin/orders" class="active"><i class="fas fa-shopping-cart"></i> <span>Đơn hàng</span></a></li>

            <li class="menu-section">Người dùng</li>
            <li><a href="<%=request.getContextPath()%>/admin/users"><i class="fas fa-users"></i> <span>Người dùng</span></a></li>
            <li><a href="<%=request.getContextPath()%>/admin/shipping-info"><i class="fas fa-map-marker-alt"></i> <span>Địa chỉ giao</span></a></li>
            <li><a href="<%=request.getContextPath()%>/admin/key"> <i class="fas fa-key"></i> <span>Khóa người dùng</span></a></li>

            <li class="menu-section">Nội dung</li>
            <li><a href="<%=request.getContextPath()%>/admin-news.jsp"><i class="fas fa-newspaper"></i> <span>Tin tức</span></a></li>

            <li class="menu-section">Hệ thống</li>
            <li><a href="<%=request.getContextPath()%>/home"><i class="fas fa-external-link-alt"></i> <span>Xem Website</span></a></li>
            <li><a href="<%=request.getContextPath()%>/logout"><i class="fas fa-sign-out-alt"></i><span>Đăng xuất</span></a></li>
        </ul>
    </nav>
</aside>

<!-- Main Content -->
<main class="admin-main">
    <header class="admin-header">
        <h1>Quản lý Đơn hàng</h1>
        <div class="user-info">
            <span>Xin chào, Admin</span>
        </div>
    </header>

    <div class="admin-content">
        <div class="data-table-container">
            <div class="table-header">
                <h2><i class="fas fa-users"></i> Danh sách đơn hàng</h2>
                <div class="table-actions">
                    <form action="${pageContext.request.contextPath}/admin/orders" method="get" style="display:flex; gap:8px;">
                        <input type="text"
                               name="search"
                               class="search-box"
                               placeholder="Tìm kiếm danh mục..."
                               value="${param.search}">
                        <button type="submit" class="btn btn-primary">
                            <i class="fas fa-search"></i>
                        </button>
                    </form>
                    <button type="button" class="btn btn-primary" onclick="openModal('addModal')">
                        <i class="fas fa-plus"></i> Thêm mới
                    </button>
                </div>
            </div>
            <table class="data-table">
                <thead>
                <tr>
                    <th>STT</th>
                    <th>Mã đơn</th>
                    <th>Khách hàng</th>
                    <th>SĐT</th>
                    <th>Địa chỉ</th>
                    <th>Ngày đặt</th>
                    <th>Thanh toán</th>
                    <th>Tổng tiền</th>
                    <th>Trạng thái</th>
                    <th>Chữ ký</th>
                    <th>Thao tác</th>
                </tr>
                </thead>
                <tbody>
                <c:set var="cp" value="${currentPage != null ? currentPage : 1}" />
                <c:set var="ps" value="${pageSize != null ? pageSize : 10}" />
                <c:choose>
                    <c:when test="${not empty orders}">
                        <c:forEach var="order" items="${orders}" varStatus="loop">
                            <tr>
                                <td><c:out value="${(cp - 1) * ps + loop.index + 1}"/></td>
                                <td><c:out value="${order.id}"/></td>
                                <td><c:out value="${order.fullName}"/></td>
                                <td><c:out value="${order.phoneNumber}"/></td>
                                <td><c:out value="${order.address}"/></td>
                                <td><fmt:formatDate value="${order.date}" pattern="dd/MM/yyyy"/></td>
                                <td>
                                    <c:choose>
                                        <c:when test="${order.paymentMethod == 'COD'}">
                                            <span class="badge badge-info">COD</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="badge badge-success">
                                                <c:out value="${order.paymentMethod}"/>
                                            </span>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td>
                                    <fmt:formatNumber value="${order.total}" type="currency" currencyCode="VND"/>
                                </td>
                                <td>
                                    <c:choose>
                                        <c:when test="${empty order.status || fn:trim(order.status) == ''}">
                                            <span class="badge badge-secondary">Chưa cập nhật</span>
                                        </c:when>
                                        <c:when test="${fn:trim(order.status) == 'processing' || fn:trim(order.status) == 'Đang xử lý'}">
                                            <span class="badge badge-warning">Đang xử lý</span>
                                        </c:when>
                                        <c:when test="${fn:trim(order.status) == 'completed' || fn:trim(order.status) == 'Đã giao'}">
                                            <span class="badge badge-success">Đã giao</span>
                                        </c:when>
                                        <c:when test="${fn:trim(order.status) == 'shipping' || fn:trim(order.status) == 'Đang giao'}">
                                            <span class="badge badge-info">Đang giao</span>
                                        </c:when>
                                        <c:when test="${fn:trim(order.status) == 'cancelled' || fn:trim(order.status) == 'Đã hủy'}">
                                            <span class="badge badge-danger">Đã hủy</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="badge badge-secondary"><c:out value="${order.status}"/></span>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td class="sign">
                                    <c:set var="sigSt" value="${signatureStatusMap[order.id]}"/>
                                    <c:choose>
                                        <c:when test="${sigSt == 'VALID'}">
                                            <span class="badge badge-success">Đã xác thực</span>
                                        </c:when>
                                        <c:when test="${sigSt == 'INVALID'}">
                                            <span class="badge badge-danger">Không hợp lệ</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="badge badge-secondary">Chưa xác thực</span>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td class="actions">
                                    <button type="button" class="btn btn-info btn-sm btn-view-order" title="Xem chi tiết"
                                            data-order-id="${order.id}">
                                        <i class="fas fa-eye"></i>
                                    </button>
                                    <c:choose>
                                        <c:when test="${sigSt == 'VALID'}">
                                            <button type="button" class="btn btn-warning btn-sm btn-edit-order" title="Sửa trạng thái"
                                                    data-order-id="${order.id}"
                                                    data-status="${fn:escapeXml(order.status)}">
                                                <i class="fas fa-edit"></i>
                                            </button>
                                        </c:when>
                                        <c:otherwise>
                                            <button type="button" class="btn btn-secondary btn-sm" disabled
                                                    title="${sigSt == 'INVALID' ? 'Chữ ký không hợp lệ — không thể sửa' : 'Đơn hàng chưa được xác thực — không thể sửa'}"
                                                    style="opacity:0.45; cursor:not-allowed;">
                                                <i class="fas fa-lock"></i>
                                            </button>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                            </tr>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <tr>
                            <td colspan="10" style="text-align:center;">Không có đơn hàng nào.</td>
                        </tr>
                    </c:otherwise>
                </c:choose>
                </tbody>
            </table>
            <c:if test="${not empty orders}">
                <c:forEach var="order" items="${orders}">
                    <div id="order-template-${order.id}" style="display:none;" data-sig-status="${signatureStatusMap[order.id]}" data-order-status="${fn:escapeXml(order.status)}">
                        <table style="width:100%; border-collapse: collapse;">
                            <tr><td><strong>STT:</strong></td><td>${order.id}</td></tr>
                            <tr><td><strong>Mã đơn:</strong></td><td>#DH${order.id}</td></tr>
                            <tr><td><strong>Khách hàng:</strong></td><td><c:out value="${order.fullName}"/></td></tr>
                            <tr><td><strong>SĐT:</strong></td><td><c:out value="${order.phoneNumber}"/></td></tr>
                            <tr><td><strong>Địa chỉ:</strong></td><td><c:out value="${order.address}"/></td></tr>
                            <tr><td><strong>Ngày đặt:</strong></td><td><fmt:formatDate value="${order.date}" pattern="dd/MM/yyyy"/></td></tr>
                            <tr><td><strong>Thanh toán:</strong></td><td><c:out value="${order.paymentMethod}"/></td></tr>
                            <tr><td><strong>Tổng tiền:</strong></td><td><fmt:formatNumber value="${order.total}" type="currency" currencyCode="VND"/></td></tr>
                            <tr><td><strong>Trạng thái:</strong></td><td>
                                <c:choose>
                                    <c:when test="${empty order.status}">Chưa cập nhật</c:when>
                                    <c:otherwise><c:out value="${order.status}"/></c:otherwise>
                                </c:choose>
                            </td></tr>
                            <tr>
                                <td><strong>Tình trạng chữ ký:</strong></td>
                                <td>
                                    <c:set var="sigStTpl" value="${signatureStatusMap[order.id]}"/>
                                    <c:choose>
                                        <c:when test="${sigStTpl == 'VALID'}">
                                            <span class="badge badge-success">Đã xác thực</span>
                                        </c:when>
                                        <c:when test="${sigStTpl == 'INVALID'}">
                                            <span class="badge badge-danger">Không hợp lệ</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="badge badge-secondary">Chưa xác thực</span>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                            </tr>

                            <tr>
                                <td><strong>Ngày ký:</strong></td>
                                <td>
                                    <c:choose>
                                        <c:when test="${not empty order.signed_at}">
                                            <fmt:formatDate value="${order.signed_at}" pattern="dd/MM/yyyy"/>
                                        </c:when>
                                        <c:otherwise>—</c:otherwise>
                                    </c:choose>
                                </td>
                            </tr>

                            <tr>
                                <td><strong>Thuật toán:</strong></td>
                                <td>SHA256withDSA</td>
                            </tr>

                            <tr>
                                <td><strong>Chữ ký:</strong></td>
                                <td>
                                    <c:choose>
                                        <c:when test="${not empty order.signature}">
                                            <div style="display:flex; gap:8px; align-items:center; flex-wrap:nowrap;">
                                                <span class="shortSignature" data-full="<c:out value='${order.signature}'/>"><c:out value="${fn:substring(order.signature, 0, 40)}"/>...</span>
                                                <button type="button" class="btn btn-secondary btn-sm copy-signature">Copy</button>
                                                <button type="button" class="btn btn-info btn-sm view-full-signature">Xem đầy đủ</button>
                                            </div>
                                        </c:when>
                                        <c:otherwise>—</c:otherwise>
                                    </c:choose>
                                </td>
                            </tr>

                            <tr>
                                <td><strong>Public key:</strong></td>
                                <td>
                                    <c:choose>
                                        <c:when test="${sigStTpl == 'VALID' || sigStTpl == 'INVALID'}">
                                            <div style="display:flex; gap:8px; align-items:center; flex-wrap:nowrap;">
                                                <span class="shortPublicKey" data-full="<c:out value='${order.publicKey}'/>"><c:out value="${fn:substring(order.publicKey, 0, 30)}"/>...</span>
                                                <button type="button" class="btn btn-secondary btn-sm copy-public-key">Copy</button>
                                                <button type="button" class="btn btn-info btn-sm view-full-public-key">Xem đầy đủ</button>
                                            </div>
                                        </c:when>
                                        <c:otherwise>—</c:otherwise>
                                    </c:choose>
                                </td>
                            </tr>
                        </table>
                        <h4 style="margin-top:10px;">Sản phẩm</h4>
                        <table class="data-table" style="width:100%;">
                            <thead>
                                <tr>
                                    <th>Sản phẩm</th>
                                    <th>Đơn giá</th>
                                    <th>Số lượng</th>
                                    <th>Thành tiền</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="item" items="${order.orderItems}">
                                    <tr>
                                        <td><c:out value="${item.productName}"/></td>
                                        <td><fmt:formatNumber value="${item.productPrice}" type="currency" currencyCode="VND"/></td>
                                        <td><c:out value="${item.productQuantity}"/></td>
                                        <td><fmt:formatNumber value="${item.productPrice * item.productQuantity}" type="currency" currencyCode="VND"/></td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </c:forEach>
            </c:if>

            <div class="pagination">
                <c:set var="ctx" value="${pageContext.request.contextPath}"/>
                <c:set var="cp" value="${currentPage}"/>
                <c:set var="tp" value="${totalPages}"/>
                <c:set var="searchParam" value="${param.search != null && param.search != '' ? '&search=' + param.search : ''}" />

                <c:if test="${cp > 1}">
                    <a class="btn" href="${ctx}/admin/orders?page=${cp-1}${searchParam}"><i class="fas fa-chevron-left"></i></a>
                </c:if>
                <c:if test="${cp <= 1}">
                    <button disabled><i class="fas fa-chevron-left"></i></button>
                </c:if>

                <c:forEach var="i" begin="1" end="${tp}">
                    <c:choose>
                        <c:when test="${i == cp}">
                            <button class="active" disabled><c:out value="${i}"/></button>
                        </c:when>
                        <c:otherwise>
                            <a class="btn" href="${ctx}/admin/orders?page=${i}${searchParam}"><c:out value="${i}"/></a>
                        </c:otherwise>
                    </c:choose>
                </c:forEach>

                <c:if test="${cp < tp}">
                    <a class="btn" href="${ctx}/admin/orders?page=${cp+1}${searchParam}"><i class="fas fa-chevron-right"></i></a>
                </c:if>
                <c:if test="${cp >= tp}">
                    <button disabled><i class="fas fa-chevron-right"></i></button>
                </c:if>
            </div>
        </div>
    </div>
</main>

<!-- Detail Modal -->
<div class="modal" id="detailModal">
    <div class="modal-content" style="max-width: 600px;">
        <div class="modal-header">
            <h3><i class="fas fa-eye"></i> Chi tiết đơn hàng</h3>
            <button class="modal-close" onclick="closeModal('detailModal')">&times;</button>
        </div>
        <div class="modal-body">
            <table style="width: 100%; border-collapse: collapse;">
                <tr>
                    <td style="padding: 10px; border-bottom: 1px solid #eee; width: 40%;"><strong>ID:</strong></td>
                    <td style="padding: 10px; border-bottom: 1px solid #eee;">&nbsp;</td>
                </tr>
                <tr>
                    <td style="padding: 10px; border-bottom: 1px solid #eee;"><strong>Họ tên khách hàng:</strong></td>
                    <td style="padding: 10px; border-bottom: 1px solid #eee;">&nbsp;</td>
                </tr>
                <tr>
                    <td style="padding: 10px; border-bottom: 1px solid #eee;"><strong>Số điện thoại:</strong></td>
                    <td style="padding: 10px; border-bottom: 1px solid #eee;">&nbsp;</td>
                </tr>
                <tr>
                    <td style="padding: 10px; border-bottom: 1px solid #eee;"><strong>Địa chỉ:</strong></td>
                    <td style="padding: 10px; border-bottom: 1px solid #eee;">&nbsp;</td>
                </tr>
                <tr>
                    <td style="padding: 10px; border-bottom: 1px solid #eee;"><strong>Ngày đặt:</strong></td>
                    <td style="padding: 10px; border-bottom: 1px solid #eee;">&nbsp;</td>
                </tr>
                <tr>
                    <td style="padding: 10px; border-bottom: 1px solid #eee;"><strong>Thanh toán bằng:</strong></td>
                    <td style="padding: 10px; border-bottom: 1px solid #eee;">&nbsp;</td>
                </tr>
                <tr>
                    <td style="padding: 10px; border-bottom: 1px solid #eee;"><strong>Tổng tiền:</strong></td>
                    <td style="padding: 10px; border-bottom: 1px solid #eee;">&nbsp;</td>
                </tr>
                <tr>
                    <td style="padding: 10px; border-bottom: 1px solid #eee;"><strong>Trạng thái:</strong></td>
                    <td style="padding: 10px; border-bottom: 1px solid #eee;"><span class="badge badge-info">User</span></td>
                </tr>
                <tr>
                    <td style="padding: 10px; border-bottom: 1px solid #eee;"><strong>Tình trạng chữ ký:</strong></td>
                    <td style="padding: 10px; border-bottom: 1px solid #eee;">Chưa xác thực</td>
                </tr>
                <tr>
                    <td style="padding: 10px; border-bottom: 1px solid #eee;"><strong>Ngày ký:</strong></td>
                    <td style="padding: 10px; border-bottom: 1px solid #eee;">xx/xx/xxxx</td>
                </tr>
                <tr>
                    <td style="padding: 10px; border-bottom: 1px solid #eee;"><strong>Thuật toán:</strong></td>
                    <td style="padding: 10px; border-bottom: 1px solid #eee;">SHA256withDSA</td>
                </tr>
                <tr>
                    <td style="padding: 10px; border-bottom: 1px solid #eee;"><strong>Chữ ký:</strong></td>
                    <td style="padding: 10px; border-bottom: 1px solid #eee;">
                        <div style="display:flex; gap:8px; align-items:center; flex-wrap:wrap;">
                            <span id="shortSignature">MIIBuwYJKoZIhvcNAQcCoIIBrDCCAagCAQEx...</span>
                            <button>Copy</button>
                            <button>Xem đầy đủ</button>
                        </div>
                    </td>
                </tr>
                <tr>
                    <td style="padding: 10px; border-bottom: 1px solid #eee;"><strong>Public key:</strong></td>
                    <td style="padding: 10px; border-bottom: 1px solid #eee;">
                        <div style="display:flex; gap:8px; align-items:center; flex-wrap:wrap;">
                            <span id="shortPublickey">MIIBuwYJKoZIhvcNAQcCoIIBrDCCAagCAQEx...</span>
                            <button>Copy</button>
                            <button>Xem đầy đủ</button>
                        </div>
                    </td>
                </tr>
            </table>
        </div>
        <div class="modal-footer">
            <button class="btn" onclick="closeModal('detailModal')">Đóng</button>
            <button class="btn btn-warning" onclick="closeModal('detailModal'); openModal('orderEditModal');">
                <i class="fas fa-edit"></i> Chỉnh sửa
            </button>
        </div>
    </div>
</div>

<!-- Edit Modal -->
<div class="modal" id="orderEditModal">
    <div class="modal-content">
        <div class="modal-header">
            <h3><i class="fas fa-edit"></i> Sửa status đơn hàng</h3>
            <button class="modal-close" onclick="closeModal('orderEditModal')">&times;</button>
        </div>
        <div class="modal-body">
            <form id="editOrderForm" action="${pageContext.request.contextPath}/UpdateOrderAdminServlet" method="post">
                <input type="hidden" name="orderId" id="edit_id" value="<c:out value='${editOrder.id}'/>">
                <div class="form-group">
                    <label>Cập nhật trạng thái *</label>
                    <select name="status" required>
                        <option value="Đang xử lý" selected>Đang xử lý</option>
                        <option value="Đang giao">Đang giao</option>
                        <option value="Đã giao">Đã giao</option>
                        <option value="Đã hủy">Đã hủy</option>
                    </select>
                </div>
            </form>
        </div>
        <div class="modal-footer">
            <button class="btn" onclick="closeModal('orderEditModal')">Hủy</button>
            <button type="submit" class="btn btn-primary" form="editOrderForm">
                <i class="fas fa-save"></i> Cập nhật
            </button>
        </div>
    </div>
</div>

<!-- Add Modal -->
<div class="modal" id="addModal">
    <div class="modal-content" style="max-width: 780px;">
        <div class="modal-header">
            <h3><i class="fas fa-plus-circle"></i> Thêm đơn hàng mới</h3>
            <button class="modal-close" onclick="closeModal('addModal')">&times;</button>
        </div>
        <div class="modal-body">
            <form id="addOrderForm" action="${pageContext.request.contextPath}/admin/orders" method="post">
                <input type="hidden" name="action" value="add" />

                <div class="form-row">
                    <div class="form-group" style="flex: 1;">
                        <label>Khách hàng (User ID) <span class="required">*</span></label>
                        <input type="number" name="userId" min="1" required placeholder="Ví dụ: 1" />
                        <small style="display:block; color:#666; margin-top:6px;">Nhập ID user trong hệ thống.</small>
                    </div>
                    <div class="form-group" style="flex: 1;">
                        <label>Phương thức thanh toán <span class="required">*</span></label>
                        <select name="paymentMethod" required>
                            <option value="COD" selected>COD</option>
                            <option value="VNPAY">VNPAY</option>
                            <option value="MOMO">MOMO</option>
                        </select>
                    </div>
                </div>

                <div class="form-row">
                    <div class="form-group" style="flex: 1;">
                        <label>Họ tên người nhận <span class="required">*</span></label>
                        <input type="text" name="receiverName" required placeholder="Nhập họ tên" />
                    </div>
                    <div class="form-group" style="flex: 1;">
                        <label>SĐT người nhận <span class="required">*</span></label>
                        <input type="text" name="receiverPhone" required placeholder="Ví dụ: 0912345678" />
                    </div>
                </div>

                <div class="form-row">
                    <div class="form-group" style="flex: 1;">
                        <label>Tỉnh/Thành <span class="required">*</span></label>
                        <input type="text" name="province" required placeholder="Ví dụ: TP.HCM" />
                    </div>
                    <div class="form-group" style="flex: 1;">
                        <label>Quận/Huyện <span class="required">*</span></label>
                        <input type="text" name="district" required placeholder="Ví dụ: Quận 1" />
                    </div>
                </div>

                <div class="form-row">
                    <div class="form-group" style="flex: 1;">
                        <label>Phường/Xã <span class="required">*</span></label>
                        <input type="text" name="ward" required placeholder="Ví dụ: Bến Nghé" />
                    </div>
                    <div class="form-group" style="flex: 1;">
                        <label>Địa chỉ cụ thể <span class="required">*</span></label>
                        <input type="text" name="detailedAddress" required placeholder="Số nhà, tên đường..." />
                    </div>
                </div>

                <div class="form-group">
                    <label>Ghi chú</label>
                    <textarea name="note" rows="3" placeholder="Ghi chú cho đơn hàng..."></textarea>
                </div>

                <div class="form-group">
                    <label>Sản phẩm <span class="required">*</span></label>
                    <div id="addOrderItems" style="display:flex; flex-direction:column; gap:10px;"></div>
                    <button type="button" class="btn" onclick="addOrderItemRow()" style="margin-top:10px;">
                        <i class="fas fa-plus"></i> Thêm sản phẩm
                    </button>
                    <small style="display:block; color:#666; margin-top:6px;">Mỗi dòng: Product ID và số lượng.</small>
                </div>

                <div class="modal-footer">
                    <button type="button" class="btn" onclick="closeModal('addModal')">Hủy</button>
                    <button type="submit" class="btn btn-primary">
                        <i class="fas fa-save"></i> Tạo đơn hàng
                    </button>
                </div>
            </form>
        </div>
    </div>
</div>
<script>
    window.APP_CTX = '<%= request.getContextPath() %>';
</script>
<script src="<%=request.getContextPath()%>/js/admin-core.js"></script>
<script src="<%=request.getContextPath()%>/js/admin-orders.js"></script>
</body>
</html>

