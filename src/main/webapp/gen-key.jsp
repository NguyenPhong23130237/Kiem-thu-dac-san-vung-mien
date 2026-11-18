<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Quản lý khóa</title>
    <link rel="stylesheet" href="css/global.css">
    <link rel="stylesheet" href="<%=request.getContextPath()%>/css/genKey.css?v=2">
</head>
<body>
<%@ include file="header.jsp" %>
<div id="generateKeyModal" class="modal">
    <div class="modal-content">
        <div class="modal-header">
            <h3>Tạo khóa mới</h3>
            <button class="btn btn-secondary" id="closeGenerateKeyBtn">
                Đóng
            </button>
        </div>
        <form action="<%=request.getContextPath()%>/generateKey" method="POST">
        <div class="form-group">
                <label>Thuật Toán:</label>
                <span class="badge-active" style="padding: 4px 10px;">DSA</span>
                <input type="hidden" name="algorithm" value="DSA">
            </div>
            <div class="modal-actions">
                <button type="submit" class="btn btn-success">
                    Tạo khóa
                </button>
            </div>
        </form>
    </div>
</div>
<c:if test="${not empty sessionScope.newPublicKey && not empty sessionScope.newPrivateKey}">
    <div id="resultKeyModal" class="modal">
        <div class="modal-content large-modal">
            <div class="modal-header">
                <h3>Khóa đã được tạo</h3>
                <a>PRIVATE SẼ KHÔNG ĐƯỢC GIỮ LẠI. LƯU Ý CẦN TẢI XUỐNG</a>
                <button class="btn btn-secondary" id="closeResultModalBtn">
                    Đóng
                </button>
            </div>
            <div class="key-container">
                <div class="key-modal-box">
                    <h4>Public Key</h4>
                    <textarea readonly class="modal-key-textarea">${sessionScope.newPublicKey}</textarea>
                    <div class="btn-group">
                        <a href="<%=request.getContextPath()%>/downloadKey?type=public&id=new"
                           class="btn btn-success">
                            Tải Public Key
                        </a>
                    </div>
                </div>
                <div class="key-modal-box">
                    <h4 class="private-title">
                        Private Key
                    </h4>
                    <textarea readonly class="modal-key-textarea">${sessionScope.newPrivateKey}</textarea>
                    <div class="btn-group">
                        <a href="<%=request.getContextPath()%>/downloadKey?type=private&id=new" class="btn btn-danger">
                            Tải Private Key
                        </a>
                    </div>
                </div>
            </div>
        </div>
    </div>

</c:if>
<main>
    <div id="title">QUẢN LÝ KHÓA</div>

    <c:if test="${not empty error}">
        <p style="color:red; text-align:center;">${error}</p>
        <c:remove var="error" scope="session"/>
    </c:if>

    <c:if test="${not empty message}">
        <p class="msg-success">${message}</p>
        <c:remove var="message" scope="session"/>
    </c:if>

    <div class="layout-container">
        <div class="left-panel">
            <h3>Khóa đang hoạt động</h3>
            <hr>
            <c:choose>
                <c:when test="${not empty currentKey}">
                    <div class="key-info">
                        <p><strong>Trạng thái khóa:</strong>
                            <span class="${currentKey.status == 'ACTIVE' ? 'badge-active' : 'badge-inactive'}">
                                    ${currentKey.status == 'ACTIVE' ? 'Active' : 'Không Active'}
                            </span>
                        </p>
                        <p><strong>Key ID:</strong> ${currentKey.id}</p>
                        <p><strong>Algorithm:</strong> ${currentKey.algorithm}</p>
                        <p><strong>Created at:</strong> ${currentKey.createdAt}</p>
                        <p><strong>Khóa công cộng (Public Key):</strong></p>
                        <div class="key-box" id="publicKeyBox">
                                ${currentKey.publicKey}
                        </div>

                        <div class="btn-group">
                            <a href="<%=request.getContextPath()%>/downloadKey?type=public&id=${currentKey.id}"
                               class="btn btn-success">
                                Tải Public Key
                            </a>

                            <form id="lossKeyForm" action="<%=request.getContextPath()%>/reportLostKey" method="POST" onsubmit="return confirmLostKey();" style="margin:0;">
                                <input type="hidden" name="keyId" value="${currentKey.id}">
                                <button type="submit" class="btn btn-danger">
                                    BÁO MẤT KEY
                                </button>
                            </form>
                        </div>
                    </div>
                </c:when>

                <c:otherwise>
                    <div class="key-info">
                        <p><strong>Trạng thái khóa:</strong> <span class="badge-inactive">Chưa có khóa</span></p>
                        <p>Bạn hiện tại chưa có khóa nào đang hoạt động hoặc hệ thống đang trống.</p>

                        <div class="form-generate">
                            <button type="button" class="btn btn-success" id="openGenerateKeyBtn">
                                Tạo khóa
                            </button>
                            <form action="<%=request.getContextPath()%>/UploadKeyServlet" method="POST" enctype="multipart/form-data" id="directUploadForm">
                                <button type="button" class="btn-direct-upload" id="triggerUploadBtn">
                                    <i class="fas fa-cloud-upload-alt"></i> Tải lên khóa từ thiết bị (.pem)
                                </button>
                            </form>
                        </div>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </div>

    <div class="bottom-panel">
        <h3>Lịch sử tạo khóa</h3>
        <table>
            <thead>
            <tr>
                <th>Key ID</th>
                <th>Thuật toán (Algorithm)</th>
                <th>Ngày tạo (Created At)</th>
                <th>Trạng thái</th>
                <th>Public Key (Trích xuất)</th>
                <th>Ngày mất khóa(Revoked At)</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="key" items="${keyList}">
                <tr>
                    <td>${key.id}</td>
                    <td>${key.algorithm}</td>
                    <td>${key.createdAt}</td>
                    <td>
                                <span class="${key.status == 'ACTIVE' ? 'badge-active' : 'badge-inactive'}">
                                        ${key.status == 'ACTIVE' ? 'ACTIVE' : 'REVOKED'}
                                </span>
                    </td>
                    <td>
                                <span title="${key.publicKey}">
                                    ${key.publicKey.substring(0, 20)}...
                                </span>
                    </td>
                    <td>${key.revokedAt}</td>
                </tr>
            </c:forEach>
            <c:if test="${empty keyList}">
                <tr>
                    <td colspan="6" class="text-center">Chưa có dữ liệu khóa nào trong lịch sử.</td>
                </tr>
            </c:if>
            </tbody>
        </table>
    </div>
</main>
<script src="${pageContext.request.contextPath}/js/admin-core.js"></script>
<script src="${pageContext.request.contextPath}/js/genKey.js"></script>
</body>
</html>
