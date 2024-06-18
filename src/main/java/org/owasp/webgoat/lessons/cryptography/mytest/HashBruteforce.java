package org.owasp.webgoat.lessons.cryptography.mytest;

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashBruteforce {
    public static final String[] SECRETS = {"secret", "admin", "password", "123456", "passw0rd"};

    public static void main(String[] args) {
        String md5HashStr = "21232F297A57A5A743894A0E4A801FC3";
        String sha256HashStr = "34de66e5caf2cb69ff2bebdc1f3091ecf6296852446c718e38ebfa60e4aa75d2";

        String md5SecretStr = crackHash(md5HashStr, "MD5");
        System.out.println(md5SecretStr);

        String sha256SecretStr = crackHash(sha256HashStr, "SHA-256");
        System.out.println(sha256SecretStr);
    }

    private static String crackHash(String targetMd5Hash, String algorithm) {
        try {
            // 选择算法
            MessageDigest md = switch (algorithm) {
                case "MD5" -> MessageDigest.getInstance("MD5");
                case "SHA-256" -> MessageDigest.getInstance("SHA-256");
                default -> throw new NoSuchAlgorithmException("不支持的哈希算法: " + algorithm);
            };

            // 爆破 hash
            for (String secret: SECRETS) {
                md.update(secret.getBytes());
                byte[] digest = md.digest();

                String md5Hash = DatatypeConverter.printHexBinary(digest).toUpperCase();
                if (md5Hash.equals(targetMd5Hash)) {
                    return secret;
                }
            }

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        return null;
    }
}
