package vn.edu.hcmuaf.fit.webdacsanvungmienvn.service;

import vn.edu.hcmuaf.fit.webdacsanvungmienvn.dao.ShippingInfoDAO;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.model.ShippingInfo;

import java.util.List;

public class ShippingInfoService {
    private final ShippingInfoDAO shippingInfoDAO = new ShippingInfoDAO();

    public void addShippingInfo(ShippingInfo shippingInfo) {
        String receiverName = shippingInfo.getReceiverName();
        String receiverPhoneNumber = shippingInfo.getReceiverPhoneNumber();
        String province = shippingInfo.getProvince();
        String district = shippingInfo.getDistrict();
        String ward = shippingInfo.getWard();
        String detailedAddress = shippingInfo.getDetailedAddress();
        if (receiverName == null || receiverName.trim().isEmpty()) {
            throw new IllegalArgumentException("Tên người nhận không được để trống");
        }
        if (receiverPhoneNumber == null || receiverPhoneNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Số điện thoại người nhận không được để trống");
        }
        if (!receiverPhoneNumber.matches("^0\\d{9}$")) {
            throw new IllegalArgumentException("Số điện thoại không hợp lệ");
        }
        if (province == null || province.trim().isEmpty()) {
            throw new IllegalArgumentException("Tỉnh/Thành phố không được để trống");
        }
        if (district == null || district.trim().isEmpty()) {
            throw new IllegalArgumentException("Quận/Huyện không được để trống");
        }
        if (ward == null || ward.trim().isEmpty()) {
            throw new IllegalArgumentException("Phường/Xã không được để trống");
        }
        if (detailedAddress == null || detailedAddress.trim().isEmpty()) {
            throw new IllegalArgumentException("Địa chỉ chi tiết không được để trống");
        }
        shippingInfoDAO.addShippingInfo(shippingInfo);
    }

    public void deleteShippingInfo(int id) {
        shippingInfoDAO.deleteShippingInfo(id);
    }

    public List<ShippingInfo> getShippingInfosByUserId(int userId) {
        return shippingInfoDAO.getShippingInfosByUserId(userId);
    }

    public ShippingInfo getShippingInfoById(int id) {
        return shippingInfoDAO.getShippingInfoById(id);
    }

    public void updateShippingInfo(ShippingInfo shippingInfo) {
        String receiverName = shippingInfo.getReceiverName();
        String receiverPhoneNumber = shippingInfo.getReceiverPhoneNumber();
        String province = shippingInfo.getProvince();
        String district = shippingInfo.getDistrict();
        String ward = shippingInfo.getWard();
        String detailedAddress = shippingInfo.getDetailedAddress();
        if (receiverName == null || receiverName.trim().isEmpty()) {
            throw new IllegalArgumentException("Tên người nhận không được để trống");
        }
        if (receiverPhoneNumber == null || receiverPhoneNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Số điện thoại người nhận không được để trống");
        }
        if (!receiverPhoneNumber.matches("^0\\d{9}$")) {
            throw new IllegalArgumentException("Số điện thoại không hợp lệ");
        }
        if (province == null || province.trim().isEmpty()) {
            throw new IllegalArgumentException("Tỉnh/Thành phố không được để trống");
        }
        if (district == null || district.trim().isEmpty()) {
            throw new IllegalArgumentException("Quận/Huyện không được để trống");
        }
        if (ward == null || ward.trim().isEmpty()) {
            throw new IllegalArgumentException("Phường/Xã không được để trống");
        }
        if (detailedAddress == null || detailedAddress.trim().isEmpty()) {
            throw new IllegalArgumentException("Địa chỉ chi tiết không được để trống");
        }
        shippingInfoDAO.updateShippingInfo(shippingInfo);
    }

    public int getShippingInfoCount() {
        return shippingInfoDAO.getShippingInfoCount();
    }

    public List<ShippingInfo> getShippingInfosPaged(int page, int pageSize) {
        return shippingInfoDAO.getShippingInfosPaged(page, pageSize);
    }

    public List<ShippingInfo> searchShippingByAdminPaged(String keyword, int page, int pageSize) {
        return shippingInfoDAO.searchShippingByAdminPaged(keyword, page, pageSize);
    }

    public int getSearchCount(String keyword) {
        return shippingInfoDAO.getSearchCount(keyword);
    }

}
