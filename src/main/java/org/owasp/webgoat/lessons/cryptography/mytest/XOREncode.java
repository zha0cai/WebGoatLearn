package org.owasp.webgoat.lessons.cryptography.mytest;

import java.util.Base64;

public class XOREncode {
    public static void main(String[] args) {
        // Base64 编码的字符串
        // {xor}Oz4rPj0+LDovPiwsKDAtOw==
        String base64EncodedStr = "Oz4rPj0+LDovPiwsKDAtOw==";
        // Base64 解码
        byte[] base64DecodedBytes = Base64.getDecoder().decode(base64EncodedStr);

        // 进行 xor 猜解
        for (int key=0; key<=0xFF; key++) {
            byte[] xorDecodedBytes = xorDecode(base64DecodedBytes, (byte) key);
            String decodedStr = new String(xorDecodedBytes);

            System.out.printf("Key: 0x%02X -> Decoded String: %s%n", key, decodedStr);
//            if (decodedStr.equals("databasepassword")) {
//                System.out.printf("Key: 0x%02X -> Decoded String: %s%n", key, decodedStr);
//            }
        }

//        String path = "../../etc/passwd";
//        if (!path.contains("./")) {
//            System.out.println("Path does not contain './'");
//        } else {
//            System.out.println("Path contains './'");
//        }
    }

    private static byte[] xorDecode(byte[] dataBytes, byte key) {
        byte[] xorDecodedBytes = new byte[dataBytes.length];
        for (int i=0; i<dataBytes.length; i++) {
            xorDecodedBytes[i] = (byte)(dataBytes[i] ^ key);
        }

        return xorDecodedBytes;
    }
}
