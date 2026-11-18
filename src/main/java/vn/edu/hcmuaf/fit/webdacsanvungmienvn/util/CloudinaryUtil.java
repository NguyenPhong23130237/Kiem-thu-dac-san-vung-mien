package vn.edu.hcmuaf.fit.webdacsanvungmienvn.util;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import jakarta.servlet.http.Part;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public class CloudinaryUtil {
    private static final Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
            "cloud_name", "dxbuueljs",
            "api_key", "689397762254569",
            "api_secret", "xzvc7Ae9W9_Mkc3HeKNL4ox_l5o"
    ));

    public static String saveUpload(Part part, String folder) throws IOException {
        try {
            InputStream inputStream = part.getInputStream();
            byte[] fileBytes = inputStream.readAllBytes();
            Map uploadResult = cloudinary.uploader().upload(fileBytes,
                    ObjectUtils.asMap("folder", folder,
                            "resource_type", "image"));
            return (String) uploadResult.get("secure_url");
        } catch (Exception e) {
            return null;
        }
    }
}
