// src/main/java/com/codeTeam_3/web/LangUtil.java
package com.codeTeam_3.web;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Set;

public class LangUtil {

    // Danh sách ngôn ngữ
    private static final Set<String> SUPPORTED = Set.of("vi", "en", "ja", "fr");

    private static final String DEFAULT_LANG = "vi";

    public static boolean isSupported(String lang) {
        return lang != null && SUPPORTED.contains(lang);
    }

    public static String resolveLang(HttpServletRequest req) {
        // 1) Query param
        String q = req.getParameter("lang");
        if (isSupported(q)) {
            req.getSession().setAttribute("lang", q);
            return q;
        }

        // 2) Session
        Object s = req.getSession().getAttribute("lang");
        if (s instanceof String str && isSupported(str)) {
            return str;
        }

        // 3) Cookie
        if (req.getCookies() != null) {
            for (Cookie c : req.getCookies()) {
                if ("lang".equals(c.getName())) {
                    String v = c.getValue();
                    if (isSupported(v)) {
                        req.getSession().setAttribute("lang", v);
                        return v;
                    }
                }
            }
        }

        // 4) Default
        req.getSession().setAttribute("lang", DEFAULT_LANG);
        return DEFAULT_LANG;
    }
}
