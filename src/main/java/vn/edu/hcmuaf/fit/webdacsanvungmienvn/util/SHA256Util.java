package vn.edu.hcmuaf.fit.webdacsanvungmienvn.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.HexFormat;

public class SHA256Util {
    public static String checksum(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] messageDigest = md.digest(input.getBytes(StandardCharsets.UTF_8));

            return HexFormat.of().formatHex(messageDigest);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
