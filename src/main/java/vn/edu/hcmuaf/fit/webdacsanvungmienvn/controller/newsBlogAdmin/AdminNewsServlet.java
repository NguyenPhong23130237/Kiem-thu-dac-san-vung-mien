package vn.edu.hcmuaf.fit.webdacsanvungmienvn.controller.newsBlogAdmin;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.dao.NewsDAO;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.model.News;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@WebServlet("/admin/news/*")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024,
        maxFileSize = 1024 * 1024 * 5,
        maxRequestSize = 1024 * 1024 * 10
)
public class AdminNewsServlet extends HttpServlet {
    private NewsDAO newsDAO;

    @Override
    public void init() throws ServletException {
        newsDAO = new NewsDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        String action = request.getParameter("action");

        if (pathInfo != null && pathInfo.length() > 1) {
            action = pathInfo.substring(1);
        }
        if (action == null) {
            action = "list";
        }
        System.out.println("DEBUG: Action = " + action + ", PathInfo = " + pathInfo);

        try {
            switch (action) {
                case "new":
                    showNewForm(request, response);
                    break;
                case "edit":
                    showEditForm(request, response);
                    break;
                case "delete":
                    deleteNews(request, response);
                    break;
                case "view":
                    viewNews(request, response);
                    break;
                case "list":
                    listNews(request, response);
                    break;
                default:

                    response.sendRedirect(request.getContextPath() + "/admin/news/list");
                    break;
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        try {
            if ("add".equals(action)) {
                addNews(request, response);
            } else if ("update".equals(action)) {
                updateNews(request, response);
            } else if ("delete".equals(action)) {
                deleteNews(request, response);
            } else {
                response.sendRedirect(request.getContextPath() + "/admin/news/list");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServletException(e);
        }
    }

    private void listNews(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("=== DEBUG: listNews called ===");
        List<News> newsList = newsDAO.getAllNews();
        System.out.println("Number of news: " + newsList.size());

        request.setAttribute("newsList", newsList);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/admin-news.jsp");
        dispatcher.forward(request, response);
    }


    private void showNewForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("/news-form.jsp");
        dispatcher.forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        News existingNews = newsDAO.getNewsById(id);
        request.setAttribute("news", existingNews);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/news-form.jsp");
        dispatcher.forward(request, response);
    }

    private void viewNews(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        News news = newsDAO.getNewsById(id);
        request.setAttribute("news", news);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/news-detail.jsp");
        dispatcher.forward(request, response);
    }

    private void addNews(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        System.out.println("=== DEBUG addNews START ===");
        System.out.println("Title: " + request.getParameter("title"));
        System.out.println("Author: " + request.getParameter("author"));
        System.out.println("Date: " + request.getParameter("published_date"));
        System.out.println("Content length: " + (request.getParameter("content") != null ?
                request.getParameter("content").length() : "null"));

        String title = request.getParameter("title");
        String author = request.getParameter("author");
        String publishedDateStr = request.getParameter("published_date");
        String content = request.getParameter("content");

        Part filePart = request.getPart("image");
        String fileName = null;

        if (filePart != null && filePart.getSize() > 0) {
            fileName = getFileName(filePart);
            System.out.println("Uploading file: " + fileName);

            String uploadPath = getServletContext().getRealPath("") + File.separator + "images" + File.separator;
            System.out.println("Upload path: " + uploadPath);

            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                System.out.println("Creating directory: " + uploadPath);
                uploadDir.mkdirs();
            }

            String fullPath = uploadPath + fileName;
            System.out.println("Full path: " + fullPath);
            filePart.write(fullPath);

            String webPath = request.getContextPath() + "/images/" + fileName;
            System.out.println("Web accessible path: " + webPath);
        } else {
            System.out.println("No file uploaded");
            fileName = "default-news.jpg";
        }

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date publishedDate = sdf.parse(publishedDateStr);

            News newNews = new News();
            newNews.setTitle(title);
            newNews.setAuthor(author);
            newNews.setPublishedDate(publishedDate);
            newNews.setContent(content);
            newNews.setImage(fileName);

            boolean success = newsDAO.addNews(newNews);
            System.out.println("Add news success: " + success);

            System.out.println("=== DEBUG: After adding, total news: " +
                    newsDAO.getAllNews().size());

            response.sendRedirect(request.getContextPath() + "/admin/news?action=list");

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("ERROR in addNews: " + e.getMessage());
            request.setAttribute("error", "Lỗi khi thêm tin tức: " + e.getMessage());
            showNewForm(request, response);
        }
    }


    private void updateNews(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        String title = request.getParameter("title");
        String author = request.getParameter("author");
        String publishedDateStr = request.getParameter("published_date");
        String content = request.getParameter("content");

        try {
            News existingNews = newsDAO.getNewsById(id);
            existingNews.setTitle(title);
            existingNews.setAuthor(author);
            existingNews.setContent(content);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date publishedDate = sdf.parse(publishedDateStr);
            existingNews.setPublishedDate(publishedDate);

            Part filePart = request.getPart("image");
            if (filePart != null && filePart.getSize() > 0) {
                String fileName = getFileName(filePart);
                String uploadPath = getServletContext().getRealPath("") + "images" + java.io.File.separator;
                File uploadDir = new File(uploadPath);
                if (!uploadDir.exists()) uploadDir.mkdirs();
                filePart.write(uploadPath + fileName);
                existingNews.setImage(fileName);
            }

            newsDAO.updateNews(existingNews);
            response.sendRedirect(request.getContextPath() + "/admin/news?action=list");
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Lỗi khi cập nhật tin tức");
            showEditForm(request, response);
        }
    }

    private void deleteNews(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        newsDAO.deleteNews(id);
        response.sendRedirect(request.getContextPath() + "/admin/news?action=list");
    }
    private String getFileName(Part part) {
        String contentDisp = part.getHeader("content-disposition");
        if (contentDisp == null) return "";

        String[] items = contentDisp.split(";");
        for (String s : items) {
            if (s.trim().startsWith("filename")) {
                String fileName = s.substring(s.indexOf("=") + 2, s.length() - 1);
                return fileName.substring(fileName.lastIndexOf("\\") + 1);
            }
        }
        return "";
    }

}


