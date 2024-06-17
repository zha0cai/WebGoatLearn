package org.owasp.webgoat.lessons.missingac.mytest;

import org.owasp.webgoat.lessons.missingac.DisplayUser;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class GenerationHash {
    public static void main(String[] args) {
        // SVtOlaa+ER+w2eoIIVE5/77umvhcsh5V8UyDLUa1Itg=
        // d4T2ahJN4fWP83s9JdLISio7Auh4mWhFT1Q38S6OewM=

        /**
         * 一共在 3 处地方存在密码
         * src/main/resources/lessons/missingac/db/migration/V2021_11_03_1__ac.sql
         * src/main/resources/lessons/jwt/db/migration/V2019_09_25_1__jwt.sql
         * org.owasp.webgoat.lessons.missingac.MissingFunctionACYourHashAdminTest
         */

        String username = "Jerry";
        String password = "doesnotreallymatter";

        String PASSWORD_SALT_SIMPLE = "DeliberatelyInsecure1234";
        String PASSWORD_SALT_ADMIN = "DeliberatelyInsecure1235";

        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            String saltedSimple = password + PASSWORD_SALT_SIMPLE + username;
            String saltedAdmin = password + PASSWORD_SALT_ADMIN + username;

            byte[] simpleHash = messageDigest.digest(saltedSimple.getBytes(StandardCharsets.UTF_8));
            byte[] adminHash = messageDigest.digest(saltedAdmin.getBytes(StandardCharsets.UTF_8));
            System.out.println(Base64.getEncoder().encodeToString(simpleHash));
            System.out.println(Base64.getEncoder().encodeToString(adminHash));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

    }
}
