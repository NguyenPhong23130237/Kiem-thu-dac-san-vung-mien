<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Quản lý khóa người dùng - Admin</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/css/admin.css">

    <link rel="stylesheet" href="<%=request.getContextPath()%>/css/admin-key.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

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
            <li><a href="<%=request.getContextPath()%>/admin/orders"><i class="fas fa-shopping-cart"></i> <span>Đơn hàng</span></a></li>

            <li class="menu-section">Người dùng</li>
            <li><a href="<%=request.getContextPath()%>/admin/users"><i class="fas fa-users"></i> <span>Người dùng</span></a></li>
            <li><a href="<%=request.getContextPath()%>/admin/shipping-info"><i class="fas fa-map-marker-alt"></i> <span>Địa chỉ giao</span></a></li>
            <li><a href="<%=request.getContextPath()%>/admin/key" class="active"> <i class="fas fa-key"></i> <span>Khóa người dùng</span></a></li>

            <li class="menu-section">Nội dung</li>
            <li><a href="<%=request.getContextPath()%>/admin-news.jsp"><i class="fas fa-newspaper"></i> <span>Tin tức</span></a></li>

            <li class="menu-section">Hệ thống</li>
            <li><a href="<%=request.getContextPath()%>/home"><i class="fas fa-external-link-alt"></i> <span>Xem Website</span></a></li>
            <li><a href="<%=request.getContextPath()%>/logout"><i class="fas fa-sign-out-alt"></i><span>Đăng xuất</span></a></li>
        </ul>
    </nav>
</aside>

<main class="admin-main-keys">
    <div class="admin-content">

        <div class="page-header-flex">
            <h2><i class="fas fa-key"></i> Danh Sách Khóa Người Dùng</h2>
        </div>

        <div class="keys-table-container">

            <div class="table-actions">
                <form method="get" action="${ctx}/admin/key" class="search-form-flex">
                    <input type="text" name="search" class="search-input-custom" placeholder="Tìm theo ID User, Thuật toán..." value="<c:out value='${search}'/>">

                    <button type="submit" class="btn-search-submit">
                        <i class="fas fa-search"></i> Tìm kiếm
                    </button>

                    <c:if test="${not empty search}">
                        <a href="${ctx}/admin/key" class="btn-search-clear">Xóa lọc</a>
                    </c:if>
                </form>
            </div>

            <table class="key-data-table">
                <thead>
                <tr>
                    <th>ID Khóa</th>
                    <th>ID Người dùng</th>
                    <th>Thuật toán</th>
                    <th>Mã Public Key (Rút gọn)</th>
                    <th>Ngày tạo</th>
                    <th>Trạng thái</th>
                    <th style="text-align: center;">Hành động</th>
                </tr>
                </thead>
                <tbody>
                <c:choose>
                    <c:when test="${not empty listKeys}">
                        <c:forEach var="k" items="${listKeys}">
                            <tr>
                                <td>${k.id}</td>
                                <td style="font-weight: bold;">User #${k.userId}</td>
                                <td>${k.algorithm}</td>
                                <td>
                                    <div class="pubkey-trunc"><c:out value="${k.publicKey}"/></div>
                                </td>
                                <td>
                                    <fmt:formatDate value="${k.createdAt}" pattern="dd/MM/yyyy HH:mm:ss"/>
                                </td>
                                <td>
                                    <c:choose>
                                        <c:when test="${k.status == 'ACTIVE'}">
                                            <span class="badge badge-active">Đang hoạt động</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="badge badge-revoked">${k.status}</span>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td style="text-align: center;">
                                    <button type="button" class="btn-view-detail"
                                            onclick="showKeyDetail('${k.id}', '${k.userId}', '${k.algorithm}', `<c:out value='${k.publicKey}'/>`, '${k.createdAt}', '${k.revokedAt != null ? k.revokedAt : 'Chưa thu hồi'}', '${k.status}')">
                                        <i class="fas fa-eye"></i> Xem chi tiết
                                    </button>
                                </td>
                            </tr>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <tr>
                            <td colspan="7" style="text-align: center; padding: 30px; color: #999;">Không tìm thấy khóa người dùng nào phù hợp.</td>
                        </tr>
                    </c:otherwise>
                </c:choose>
                </tbody>
            </table>

            <div class="pagination-flex">
                <c:set var="searchParam" value="${not empty search ? '&search='.concat(search) : ''}" />

                <c:if test="${cp > 1}">
                    <a class="btn" href="${ctx}/admin/key?page=${cp-1}${searchParam}"><i class="fas fa-chevron-left"></i></a>
                </c:if>

                <c:forEach var="i" begin="1" end="${tp}">
                    <c:choose>
                        <c:when test="${i == cp}">
                            <button class="active" disabled>${i}</button>
                        </c:when>
                        <c:otherwise>
                            <a class="btn" href="${ctx}/admin/key?page=${i}${searchParam}">${i}</a>
                        </c:otherwise>
                    </c:choose>
                </c:forEach>

                <c:if test="${cp < tp}">
                    <a class="btn" href="${ctx}/admin/key?page=${cp+1}${searchParam}"><i class="fas fa-chevron-right"></i></a>
                </c:if>
            </div>
        </div>
    </div>
</main>

<div id="keyDetailModal" class="key-modal-overlay">
    <div class="key-modal-content">
        <div class="key-modal-header">
            <h3><i class="fas fa-info-circle text-primary"></i> Chi Tiết Khóa Người Dùng</h3>
            <span class="close-modal" onclick="closeDetailModal()">&times;</span>
        </div>
        <div class="key-modal-body">
            <p><strong>Mã định danh Khóa:</strong> <span id="detailId"></span></p>
            <p><strong>Thuộc về User ID:</strong> <span id="detailUserId"></span></p>
            <p><strong>Thuật toán mã hóa:</strong> <span id="detailAlgo"></span></p>
            <p><strong>Trạng thái hoạt động:</strong> <span id="detailStatus"></span></p>
            <p><strong>Thời gian khởi tạo:</strong> <span id="detailCreatedAt"></span></p>
            <p><strong>Thời gian thu hồi (Revoked):</strong> <span id="detailRevokedAt"></span></p>
            <div>
                <p style="margin-bottom: 5px;"><strong>Nội dung Public Key đầy đủ:</strong></p>
                <textarea id="detailPublicKey" readonly onclick="this.select()"></textarea>
            </div>
        </div>
        <div class="key-modal-footer">
            <button type="button" class="btn-modal-close" onclick="closeDetailModal()">Đóng lại</button>
        </div>
    </div>
</div>

<script src="<%=request.getContextPath()%>/js/admin-core.js"></script>
<script src="<%=request.getContextPath()%>/js/admin-key.js"></script>
</body>
</html>