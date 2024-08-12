package org.owasp.webgoat.lessons.jwt.mytest;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.time.Instant;
import java.util.Date;

public class CreateJwtTest {

    // 生成 JWT Token
    public String generateToken(String secretKey, String subject, long expirationMillis) {
        // 当前时间
        Date now = new Date();

        //Date test = Date.from(Instant.now().plusSeconds(60));
        //System.out.println(test); // Mon Jul 15 14:53:51 CST 2024

        // 计算过期时间
        Date expiration = new Date(now.getTime() + expirationMillis);
        System.out.println(expiration); // Mon Jul 15 15:52:51 CST 2024

        // 获取时间戳
        long seconds = expiration.getTime() / 1000;
        System.out.println("Seconds time: " + seconds); // Seconds time: 1721030281

        // 将时间戳转换回标准时间
        Date dateFromTimestamp = new Date(seconds * 1000);
        System.out.println("Standard time: " + dateFromTimestamp); // Standard time: Mon Jul 15 15:58:01 CST 2024

        // 生成 JWT Token
        String token = Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();

        return token;
    }

    public static void main(String[] args) {
        String secretKey = "your_secret_key";
        String subject = "user123";
        long expirationMillis = 3600000; // 1 hour in milliseconds

        CreateJwtTest createJwtTest = new CreateJwtTest();
        String token = createJwtTest.generateToken(secretKey, subject, expirationMillis);

        System.out.println("Generated JWT Token: " + token);
    }
}
