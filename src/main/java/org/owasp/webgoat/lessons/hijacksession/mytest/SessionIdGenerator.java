package org.owasp.webgoat.lessons.hijacksession.mytest;

import java.time.Instant;
import java.util.Random;
import java.util.function.Supplier;

public class SessionIdGenerator {
  // private static int id = 1000;
  private static long id = new Random().nextLong() & Long.MAX_VALUE;
  /**
   * 用于生成 SessionID 的方法
   *
   * <p>() -> ++id + "-" + Instant.now().toEpochMilli()： 这是一个 lambda 表达式，用于实现 Supplier 接口的 get 方法。
   * Instant.now().toEpochMilli() 返回当前时间的纪元毫秒数（自 1970-01-01T00:00:00Z 开始的毫秒数）。
   */
  private static final Supplier<String> GENERATE_SESSION_ID =
      () -> ++id + "-" + Instant.now().toEpochMilli();

  public static String generateSessionId() {
    return GENERATE_SESSION_ID.get();
  }

  public static void main(String[] args) throws InterruptedException {
    for (int i = 0; i < 20; i++) {
      System.out.println(generateSessionId());
      Thread.sleep(1);
    }
  }
}
