package org.example.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;


import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

@WebServlet(value = "/time")
public class TimeServlet extends HttpServlet {
    private TemplateEngine engine;
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String timezone = req.getParameter("timezone");
        Cookie[] cookies = req.getCookies();
        if (timezone != null && !timezone.isEmpty()) {
            Cookie tzCookie = new Cookie("timezone", timezone);
            tzCookie.setMaxAge(60 * 60 * 24 * 30);
            resp.addCookie(tzCookie);
        }else {
            timezone = "UTS";
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals("timezone")) {
                        timezone = cookie.getValue();
                        break;
                    }
                }
            }
        }
        Date date = new Date();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        df.setTimeZone(TimeZone.getTimeZone(timezone));

        resp.setContentType("text/html");

        Context context = new Context();
        context.setVariable("message", df.format(date) + " " + timezone);

        engine.process("timeZone", context, resp.getWriter());
        resp.getWriter().close();

    }
    @Override
    public void init() {
        engine = new TemplateEngine();

        ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
        resolver.setPrefix("templates/");
        resolver.setSuffix(".html");
        resolver.setTemplateMode("HTML5");
        resolver.setOrder(engine.getTemplateResolvers().size());
        resolver.setCacheable(false);
        engine.addTemplateResolver(resolver);
    }


}


