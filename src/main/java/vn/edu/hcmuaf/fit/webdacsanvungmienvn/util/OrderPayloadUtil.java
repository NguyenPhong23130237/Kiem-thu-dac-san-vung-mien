package vn.edu.hcmuaf.fit.webdacsanvungmienvn.util;

import vn.edu.hcmuaf.fit.webdacsanvungmienvn.model.Order;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.model.OrderItem;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

public class OrderPayloadUtil {
    private static final DateTimeFormatter ORDER_DATE_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final ZoneId ZONE = ZoneId.of("Asia/Ho_Chi_Minh");

    private static String formatOrderDate(Date d) {
        if (d == null) return "";

        if (d instanceof java.sql.Date sqlDate) {
            return sqlDate.toLocalDate().atStartOfDay().format(ORDER_DATE_FMT);
        }

        return d.toInstant().atZone(ZONE).toLocalDateTime().format(ORDER_DATE_FMT);
    }

    public static String buildOrderPayload(Order order) {
        List<OrderItem> items = order.getOrderItems();
        String orderDate = formatOrderDate(order.getDate());
        StringBuilder sb = new StringBuilder();
        sb.append("ORDER_ID=").append(order.getId()).append('\n')
                .append("USER_ID=").append(order.getUserId()).append('\n')
                .append("PAYMENT_METHOD=").append(order.getPaymentMethod()).append('\n')
                .append("FULL_NAME=").append(order.getFullName()).append('\n')
                .append("PHONE_NUMBER=").append(order.getPhoneNumber()).append('\n')
                .append("ADDRESS=").append(order.getAddress()).append('\n')
                .append("ORDER_DATE=").append(orderDate).append('\n')
                .append("NOTE=").append(order.getNote() != null ? order.getNote().trim() : "").append('\n')
                .append("TOTAL=").append((long) order.getTotal()).append('\n')
                .append("ITEMS_COUNT=").append(items.size()).append("\n\n");

        for (int i = 0; i < items.size(); i++) {
            OrderItem item = items.get(i);
            sb.append("ITEM_").append(i + 1).append("_PRODUCT_ID=").append(item.getProductId()).append('\n');
            sb.append("ITEM_").append(i + 1).append("_PRODUCT_NAME=").append(item.getProductName()).append('\n');
            sb.append("ITEM_").append(i + 1).append("_QUANTITY=").append(item.getProductQuantity()).append('\n');
            sb.append("ITEM_").append(i + 1).append("_PRICE=").append((long) item.getProductPrice()).append("\n\n");
        }
        return sb.toString();
    }
}
