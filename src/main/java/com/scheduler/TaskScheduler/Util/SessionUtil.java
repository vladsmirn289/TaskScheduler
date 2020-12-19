package com.scheduler.TaskScheduler.Util;

import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.http.HttpServletRequest;

public class SessionUtil {
    public static void setAuthOnNull(HttpServletRequest request) {
        SecurityContextHolder.getContext().setAuthentication(null);
        request.getSession().removeAttribute("SPRING_SECURITY_CONTEXT");
    }
}
