<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
    if (request.getAttribute("shippingInfos") == null && !request.getRequestURI().endsWith("/admin/shipping-info")) {
        String qs = request.getQueryString();
        response.sendRedirect(request.getContextPath() + "/admin/shipping-info" + (qs != null ? ("?" + qs) : ""));
        return;
    }
%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Quản lý Địa chỉ giao hàng - Admin</title>
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
                <li><a href="<%=request.getContextPath()%>/admin/orders"><i class="fas fa-shopping-cart"></i> <span>Đơn hàng</span></a></li>

                <li class="menu-section">Người dùng</li>
                <li><a href="<%=request.getContextPath()%>/admin/users"><i class="fas fa-users"></i> <span>Người dùng</span></a></li>
                <li><a href="<%=request.getContextPath()%>/admin/shipping-info" class="active"><i class="fas fa-map-marker-alt"></i> <span>Địa chỉ giao</span></a></li>
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
            <h1>Quản lý Địa chỉ giao hàng</h1>
            <div class="user-info">
                <span>Xin chào, Admin</span>
            </div>
        </header>

        <div class="admin-content">
            <div class="data-table-container">
                <div class="table-header">
                    <h2><i class="fas fa-map-marker-alt"></i> Danh sách địa chỉ giao hàng</h2>
                    <div class="table-actions">
                        <c:choose>
                            <c:when test="${not empty param.search}">
                                <c:set var="searchVal" value="${param.search}" />
                            </c:when>
                            <c:otherwise>
                                <c:set var="searchVal" value="${search}" />
                            </c:otherwise>
                        </c:choose>
                        <form action="${pageContext.request.contextPath}/admin/shipping-info" method="get" style="display:flex; gap:8px;">
                            <input type="text"
                                   name="search"
                                   class="search-box"
                                   aria-label="Tìm kiếm"
                                   placeholder="Tìm kiếm..."
                                   value="${fn:escapeXml(searchVal)}">
                            <button type="submit" class="btn btn-primary"><i class="fas fa-search"></i></button>
                        </form>
                    </div>
                </div>
                <table class="data-table">
                    <thead>
                        <tr>
                            <th>STT</th>
                            <th>ID</th>
                            <th>Người dùng (user_id)</th>
                            <th>Người nhận</th>
                            <th>SĐT</th>
                            <th>Tỉnh/TP</th>
                            <th>Quận/Huyện</th>
                            <th>Phường/Xã</th>
                            <th>Địa chỉ chi tiết</th>
                            <th>Thao tác</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:set var="cp" value="${currentPage != null ? currentPage : 1}" />
                        <c:set var="ps" value="10" />
                        <c:choose>
                            <c:when test="${not empty shippingInfos}">
                                <c:forEach var="s" items="${shippingInfos}" varStatus="loop">
                                    <tr>
                                        <td><c:out value="${(cp - 1) * ps + loop.index + 1}"/></td>
                                        <td><c:out value="${s.id}"/></td>
                                        <td><c:out value="${s.userId}"/></td>
                                        <td><c:out value="${s.receiverName}"/></td>
                                        <td><c:out value="${s.receiverPhoneNumber}"/></td>
                                        <td><c:out value="${s.province}"/></td>
                                        <td><c:out value="${s.district}"/></td>
                                        <td><c:out value="${s.ward}"/></td>
                                        <td><c:out value="${s.detailedAddress}"/></td>
                                        <td class="actions">
                                            <button type="button" class="btn btn-info btn-sm"
                                                    title="Xem"
                                                    data-id="${s.id}"
                                                    data-user-id="${s.userId}"
                                                    data-receiver-name="${fn:escapeXml(s.receiverName)}"
                                                    data-receiver-phone-number="${fn:escapeXml(s.receiverPhoneNumber)}"
                                                    data-province="${fn:escapeXml(s.province)}"
                                                    data-district="${fn:escapeXml(s.district)}"
                                                    data-ward="${fn:escapeXml(s.ward)}"
                                                    data-detailed-address="${fn:escapeXml(s.detailedAddress)}"
                                                    onclick="showShippingView(this)">
                                                <i class="fas fa-eye"></i>
                                            </button>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <tr>
                                    <td colspan="10" style="text-align:center;">Không có địa chỉ giao hàng.</td>
                                </tr>
                            </c:otherwise>
                        </c:choose>
                    </tbody>
                </table>
                <div class="pagination">
                    <c:set var="cp" value="${currentPage}"/>
                    <c:set var="tp" value="${totalPages}"/>

                    <c:choose>
                        <c:when test="${cp > 1}">
                            <a class="btn" href="${pageContext.request.contextPath}/admin/shipping-info?page=${cp-1}<c:if test='${not empty searchVal}'>&amp;search=${fn:escapeXml(searchVal)}</c:if>"><i class="fas fa-chevron-left"></i></a>
                        </c:when>
                        <c:otherwise>
                            <button disabled><i class="fas fa-chevron-left"></i></button>
                        </c:otherwise>
                    </c:choose>

                    <c:forEach var="i" begin="1" end="${tp}">
                        <c:choose>
                            <c:when test="${i == cp}">
                                <button class="active" disabled><c:out value="${i}"/></button>
                            </c:when>
                            <c:otherwise>
                                <a class="btn" href="${pageContext.request.contextPath}/admin/shipping-info?page=${i}<c:if test='${not empty searchVal}'>&amp;search=${fn:escapeXml(searchVal)}</c:if>"><c:out value="${i}"/></a>
                            </c:otherwise>
                        </c:choose>
                    </c:forEach>

                    <c:choose>
                        <c:when test="${cp < tp}">
                            <a class="btn" href="${pageContext.request.contextPath}/admin/shipping-info?page=${cp+1}<c:if test='${not empty searchVal}'>&amp;search=${fn:escapeXml(searchVal)}</c:if>"><i class="fas fa-chevron-right"></i></a>
                        </c:when>
                        <c:otherwise>
                            <button disabled><i class="fas fa-chevron-right"></i></button>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>
    </main>

    <!-- View Modal -->
    <div class="modal" id="viewModal">
        <div class="modal-content" style="max-width: 600px;">
            <div class="modal-header">
                <h3><i class="fas fa-eye"></i> Chi tiết địa chỉ giao hàng</h3>
                <button class="modal-close" onclick="closeModal('viewModal')">&times;</button>
            </div>
            <div class="modal-body">
                <table style="width: 100%; border-collapse: collapse;">
                    <tr>
                        <td style="padding: 12px; border-bottom: 1px solid #eee; width: 40%; background: #f8f9fa;"><strong>ID:</strong></td>
                        <td id="view-id" style="padding: 12px; border-bottom: 1px solid #eee;"></td>
                    </tr>
                    <tr>
                        <td style="padding: 12px; border-bottom: 1px solid #eee; background: #f8f9fa;"><strong>Người dùng (id_user):</strong></td>
                        <td id="view-user-id" style="padding: 12px; border-bottom: 1px solid #eee;"></td>
                    </tr>
                    <tr>
                        <td style="padding: 12px; border-bottom: 1px solid #eee; background: #f8f9fa;"><strong>Người nhận (receiver_name):</strong></td>
                        <td id="view-receiver-name" style="padding: 12px; border-bottom: 1px solid #eee;"></td>
                    </tr>
                    <tr>
                        <td style="padding: 12px; border-bottom: 1px solid #eee; background: #f8f9fa;"><strong>SĐT (receiver_phone_number):</strong></td>
                        <td id="view-receiver-phone" style="padding: 12px; border-bottom: 1px solid #eee;"></td>
                    </tr>
                    <tr>
                        <td style="padding: 12px; border-bottom: 1px solid #eee; background: #f8f9fa;"><strong>Tỉnh/TP (province):</strong></td>
                        <td id="view-province" style="padding: 12px; border-bottom: 1px solid #eee;"></td>
                    </tr>
                    <tr>
                        <td style="padding: 12px; border-bottom: 1px solid #eee; background: #f8f9fa;"><strong>Quận/Huyện (district):</strong></td>
                        <td id="view-district" style="padding: 12px; border-bottom: 1px solid #eee;"></td>
                    </tr>
                    <tr>
                        <td style="padding: 12px; border-bottom: 1px solid #eee; background: #f8f9fa;"><strong>Phường/Xã (ward):</strong></td>
                        <td id="view-ward" style="padding: 12px; border-bottom: 1px solid #eee;"></td>
                    </tr>
                    <tr>
                        <td style="padding: 12px; border-bottom: 1px solid #eee; background: #f8f9fa;"><strong>Địa chỉ chi tiết (detailed_address):</strong></td>
                        <td id="view-detailed-address" style="padding: 12px; border-bottom: 1px solid #eee;"></td>
                    </tr>
                </table>
                <div style="margin-top: 20px; padding: 15px; background: #e8f5e9; border-radius: 8px;">
                    <h4 style="color: #2e7d32; margin-bottom: 10px;"><i class="fas fa-map-marked-alt"></i> Địa chỉ đầy đủ</h4>
                    <p id="view-full-address" style="color: #333;"></p>
                </div>
            </div>
            <div class="modal-footer">
                <button class="btn" onclick="closeModal('viewModal')">Đóng</button>
            </div>
        </div>
    </div>
    <script>
        window.APP_CTX = '<%= request.getContextPath() %>';
    </script>
    <script src="<%=request.getContextPath()%>/js/admin-core.js"></script>
    <script src="<%=request.getContextPath()%>/js/admin-shipping-info.js"></script>

</body>
</html>

