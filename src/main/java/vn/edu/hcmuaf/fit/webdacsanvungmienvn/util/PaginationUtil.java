package vn.edu.hcmuaf.fit.webdacsanvungmienvn.util;

public class PaginationUtil {
    public static int calculateTotalPages(int totalItems, int itemsPerPage) {
        return (int) Math.ceil((double) totalItems / itemsPerPage);
    }
}
