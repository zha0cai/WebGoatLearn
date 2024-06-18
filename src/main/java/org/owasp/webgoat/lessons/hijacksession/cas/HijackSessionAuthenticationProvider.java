/*
 * This file is part of WebGoat, an Open Web Application Security Project utility. For details, please see http://www.owasp.org/
 *
 * Copyright (c) 2002 - 2021 Bruce Mayhew
 *
 * This program is free software; you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation; either version 2 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program; if
 * not, write to the Free Software Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA.
 *
 * Getting Source
 * ==============
 *
 * Source for this application is maintained at https://github.com/WebGoat/WebGoat, a repository for free software projects.
 */

package org.owasp.webgoat.lessons.hijacksession.cas;

import java.time.Instant;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.DoublePredicate;
import java.util.function.Supplier;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.ApplicationScope;

/**
 * @author Angel Olle Blazquez
 */

// weak id value and mechanism

@ApplicationScope
@Component
public class HijackSessionAuthenticationProvider implements AuthenticationProvider<Authentication> {

  private Queue<String> sessions = new LinkedList<>();
  private static long id = new Random().nextLong() & Long.MAX_VALUE;
  protected static final int MAX_SESSIONS = 50;

  private static final DoublePredicate PROBABILITY_DOUBLE_PREDICATE = pr -> pr < 0.75;

  /**
   * 一个静态常量 Supplier<Authentication> 对象，用于提供 Authentication 实例 使用 lambda 表达式定义 Supplier，返回一个构建好的
   * Authentication 对象 通过 Instant.now() 获取当前时间对象，再通过 toEpochMilli() 方法将 Instant 转换为自 Unix
   * 纪元（1970-01-01T00:00:00Z）以来的毫秒数 即生成一个 id-时间戳 的 sessionid
   */
  private static final Supplier<String> GENERATE_SESSION_ID =
      () -> ++id + "-" + Instant.now().toEpochMilli();

  public static final Supplier<Authentication> AUTHENTICATION_SUPPLIER =
      () -> Authentication.builder().id(GENERATE_SESSION_ID.get()).build();

  @Override
  public Authentication authenticate(Authentication authentication) {
    if (authentication == null) {
      return AUTHENTICATION_SUPPLIER.get();
    }
    // 判断 id 是否为空，不为空判断是否存在 session 中，如果存在则表示已授权
    if (StringUtils.isNotEmpty(authentication.getId())
        && sessions.contains(authentication.getId())) {
      authentication.setAuthenticated(true);
      return authentication;
    }

    // 如果 id 为空，创建 SessionID
    if (StringUtils.isEmpty(authentication.getId())) {
      authentication.setId(GENERATE_SESSION_ID.get());
    }

    /**
     * 创建自动登录 SessionID 通过 PROBABILITY_DOUBLE_PREDICATE.test 判断该 sessionid 是否存入 session 中
     * 用来模拟实际网络系统中存在已经登录的 session 这样就可以通过 session 的规律爆破 session 登录其他用户【漏洞点】
     */
    authorizedUserAutoLogin();

    return authentication;
  }

  protected void authorizedUserAutoLogin() {
    /** 生成一个 0.0 到 1.0 之间的随机双精度浮点数，并测试它是否符合概率条件 这里需要生成 < 0.75
     * 如果符合条件，就生成一个 sessionid 添加到 session 中
     * 注意，这里和返回到 setCookie 的不是同一个 sessionId
     * */
    if (!PROBABILITY_DOUBLE_PREDICATE.test(ThreadLocalRandom.current().nextDouble())) {
      Authentication authentication = AUTHENTICATION_SUPPLIER.get();
      authentication.setAuthenticated(true);
      addSession(authentication.getId());
    }
  }

  protected boolean addSession(String sessionId) {
    if (sessions.size() >= MAX_SESSIONS) {
      sessions.remove();
    }
    return sessions.add(sessionId);
  }

  protected int getSessionsSize() {
    return sessions.size();
  }
}
