<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %><!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>${news.title}</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/global.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/blog-news-detail.css">
</head>
<body>
<%@ include file="header.jsp" %>

<main>
    <section>
        <div class="container">
            <c:if test="${not empty news}">
                <article class="news-detail">
                    <h1>${news.title}</h1>
                    <div class="news-meta">
                        Tác giả: ${news.author} -
                        Ngày đăng: <fmt:formatDate value="${news.publishedDate}" pattern="dd/MM/yyyy" />
                    </div>

                    <c:choose>
                        <c:when test="${not empty news.image}">
                            <img src="${pageContext.request.contextPath}/images/${news.image}"
                                 alt="${news.title}">
                        </c:when>
                        <c:otherwise>
                            <img src="${pageContext.request.contextPath}/images/default-news.jpg"
                                 alt="${news.title}">
                        </c:otherwise>
                    </c:choose>

                    <div class="news-content">
                        ${news.content}
                    </div>

                    <div class="news-navigation">
                        <a href="${pageContext.request.contextPath}/blog-news">
                            <i class="fas fa-arrow-left"></i> Quay lại danh sách
                        </a>
                        <a href="${pageContext.request.contextPath}/">
                            <i class="fas fa-home"></i> Về trang chủ
                        </a>
                    </div>
                </article>
            </c:if>

            <c:if test="${empty news}">
                <div class="not-found">
                    <h2>Bài viết không tồn tại</h2>
                    <p>Bài viết bạn đang tìm kiếm có thể đã bị xóa hoặc không tồn tại.</p>
                    <a href="${pageContext.request.contextPath}/blog-news" class="btn">Quay lại danh sách tin tức</a>
                </div>
            </c:if>
        </div>
    </section>
</main>

<%@ include file="footer.jsp" %>
</body>
</html>