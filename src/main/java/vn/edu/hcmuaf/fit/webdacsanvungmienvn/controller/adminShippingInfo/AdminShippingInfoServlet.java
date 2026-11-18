package vn.edu.hcmuaf.fit.webdacsanvungmienvn.controller.adminShippingInfo;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.model.ShippingInfo;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.service.ShippingInfoService;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "AdminShippingInfoServlet", value = "/admin/shipping-info")
public class AdminShippingInfoServlet extends HttpServlet {
    private final ShippingInfoService shippingService = new ShippingInfoService();
    private static final int PAGE_SIZE = 10;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        int page = 1;
        String pageParam = request.getParameter("page");
        if (pageParam != null) {
            try { page = Integer.parseInt(pageParam); } catch (NumberFormatException ignored) {}
        }
        if (page < 1) page = 1;

        String search = request.getParameter("search");
        if (search != null) {
            search = search.trim();
            if (search.isEmpty()) search = null;
        }
        StringBuffer reqUrl = request.getRequestURL();
        String qs = request.getQueryString();
        System.out.println("[AdminShippingInfoServlet] URL=" + reqUrl + (qs != null ? ("?" + qs) : "") + " page=" + page + " search='" + search + "'");
        List<ShippingInfo> shippingInfos;
        int totalCount;
        int totalPages;

        if (search != null && !search.isEmpty()) {
            shippingInfos = shippingService.searchShippingByAdminPaged(search, page, PAGE_SIZE);
            totalCount = shippingService.getSearchCount(search);
        } else {
            shippingInfos = shippingService.getShippingInfosPaged(page, PAGE_SIZE);
            totalCount = shippingService.getShippingInfoCount();
        }

        totalPages = (int) Math.ceil((double) totalCount / PAGE_SIZE);
        if (totalPages < 1) totalPages = 1;

        request.setAttribute("shippingInfos", shippingInfos);
        request.setAttribute("currentPage", page);
        request.setAttribute("search", search);
        request.setAttribute("totalCount", totalCount);
        request.setAttribute("pageSize", PAGE_SIZE);
        request.setAttribute("totalPages", totalPages);

        request.getRequestDispatcher("/admin-shipping-info.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "POST not allowed on admin shipping-info");
    }
}
