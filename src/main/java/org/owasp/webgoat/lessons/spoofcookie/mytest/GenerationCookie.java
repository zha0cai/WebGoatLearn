package org.owasp.webgoat.lessons.spoofcookie.mytest;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.crypto.codec.Hex;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class GenerationCookie {

    // 生成一个包含指定长度的随机字母字符串
    private static final String SALT = RandomStringUtils.randomAlphabetic(10);

    public static String encode(final String value) {
        if (value == null) {
            return null;
        }

        // cookie 处理流程
        String encoded = value.toLowerCase() + SALT;
        encoded = revert(encoded);
        encoded = hexEncode(encoded);
        return base64Encode(encoded);
    }

    public static String decode(final String encodedValue) {
        if (encodedValue == null) {
            return null;
        }

        String decoded = base64Decode(encodedValue);
        decoded = hexDecode(decoded);
        decoded = revert(decoded);

        return decoded = decoded.substring(0, decoded.length()-SALT.length());
    }

    private static String revert(final String value) {
        return new StringBuilder(value).reverse().toString();
    }

    private static String hexEncode(final String value) {
        char[] encoded = Hex.encode(value.getBytes(StandardCharsets.UTF_8));
        return new String(encoded);
    }

    private static String hexDecode(final String value) {
        byte[] decoded = Hex.decode(value);
        return new String(decoded);
    }

    private static String base64Encode(final String value) {
        return Base64.getEncoder().encodeToString(value.getBytes());
    }

    private static String base64Decode(final String value) {
        byte[] decoded = Base64.getDecoder().decode(value.getBytes());
        return new String(decoded);
    }

    public static void main(String[] args) {
        String username = "Tom";
        System.out.println(encode(username));

        String lowerCasedUsername = decode("NDQ2NTZmNWE2ODZmNGE1MjRmNDI2ZDZmNzQ=");
        System.out.println(lowerCasedUsername);
    }

}
