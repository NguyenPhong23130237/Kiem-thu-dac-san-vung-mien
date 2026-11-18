package vn.edu.hcmuaf.fit.webdacsanvungmienvn.controller.adminKey;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.dao.GenKeyDAO;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.model.GenKey;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "adminKeyServlet", value = "/admin/key")
public class adminKeyServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String search = request.getParameter("search");
        if (search == null) search = "";
        search = search.trim();

        int currentPage = 1;
        String pageStr = request.getParameter("page");
        if (pageStr != null && !pageStr.isBlank()) {
            try {
                currentPage = Integer.parseInt(pageStr);
                if (currentPage < 1) currentPage = 1;
            } catch (NumberFormatException e) {
                currentPage = 1;
            }
        }

        int pageSize = 10;
        int totalRecords = GenKeyDAO.getTotalKeysCount(search);
        int totalPages = (int) Math.ceil((double) totalRecords / pageSize);
        if (totalPages == 0) totalPages = 1;
        if (currentPage > totalPages) currentPage = totalPages;

        List<GenKey> listKeys = GenKeyDAO.getKeysPaginated(search, currentPage, pageSize);

        request.setAttribute("listKeys", listKeys);
        request.setAttribute("cp", currentPage);
        request.setAttribute("tp", totalPages);
        request.setAttribute("search", search);

        request.getRequestDispatcher("/admin-key.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}