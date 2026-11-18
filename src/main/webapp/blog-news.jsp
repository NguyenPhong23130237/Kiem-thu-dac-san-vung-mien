<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>
<%@ taglib uri="jakarta.tags.functions" prefix="fn" %><!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Tin tức blog</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/global.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/blog-news.css">
</head>
<body>
<%@ include file="header.jsp" %>

<main>
    <section class="section-tin-tuc">
        <div class="news-container">
            <h2 class="blog-title">Tin tức & Blog mới nhất</h2>
            <div class="blog-grid">
                <c:forEach var="news" items="${newsList}">
                    <article class="blog-card">
                        <c:choose>
                            <c:when test="${not empty news.image}">
                                <c:if test="${not empty news.image}">
                                    <c:choose>
                                        <c:when test="${fn:startsWith(news.image, 'http')}">
                                            <img src="${news.image}">
                                        </c:when>
                                        <c:when test="${fn:contains(news.image, 'images/')}">
                                            <img src="${pageContext.request.contextPath}/${news.image}">
                                        </c:when>
                                        <c:otherwise>
                                            <img src="${pageContext.request.contextPath}/images/${news.image}">
                                        </c:otherwise>
                                    </c:choose>
                                </c:if>
                            </c:when>
                            <c:otherwise>
                                <img src="${pageContext.request.contextPath}/images/default-news.jpg"
                                     alt="${news.title}">
                            </c:otherwise>
                        </c:choose>
                        <div class="blog-content">
                            <h3>${news.title}</h3>
                            <p>${fn:substring(news.content, 0, 150)}...</p>
                            <div class="blog-meta">
                                <span><i class="fas fa-user"></i> ${news.author}</span>
                                <span><i class="fas fa-calendar"></i>
                                    <fmt:formatDate value="${news.publishedDate}" pattern="dd/MM/yyyy" />
                                </span>
                            </div>
                            <a href="${pageContext.request.contextPath}/news?id=${news.id}">Đọc thêm</a>
                        </div>
                    </article>
                </c:forEach>
            </div>

            <!-- Phân trang (nếu cần) -->
            <div class="pagination">
                <button disabled>&laquo;</button>
                <button class="active">1</button>
                <button>2</button>
                <button>3</button>
                <button>&raquo;</button>
            </div>
        </div>
    </section>
</main>

<%@ include file="footer.jsp" %>
</body>
</html>