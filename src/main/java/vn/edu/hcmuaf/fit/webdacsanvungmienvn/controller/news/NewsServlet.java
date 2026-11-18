package vn.edu.hcmuaf.fit.webdacsanvungmienvn.controller.news;

import vn.edu.hcmuaf.fit.webdacsanvungmienvn.dao.NewsDAO;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.model.News;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "NewsServlet", value = {"/news", "/blog-news"})
public class NewsServlet extends HttpServlet {
    private NewsDAO newsDAO;

    @Override
    public void init() throws ServletException {
        newsDAO = new NewsDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getServletPath();

        if ("/blog-news".equals(path)) {
            List<News> newsList = newsDAO.getLatestNews(9);
            request.setAttribute("newsList", newsList);
            request.getRequestDispatcher("/blog-news.jsp").forward(request, response);
        } else if ("/news".equals(path)) {
            String id = request.getParameter("id");
            if (id != null) {
                News news = newsDAO.getNewsById(Integer.parseInt(id));
                request.setAttribute("news", news);
                request.getRequestDispatcher("/blog-news-detail.jsp").forward(request, response);
            } else {
                response.sendRedirect(request.getContextPath() + "/blog-news");
            }
        }
    }
}