package org.example.controller;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Set;
import java.util.TimeZone;


@WebFilter(value = "/time/*")
public class TimezoneValidateFilter extends HttpFilter {
    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        String timezone = req.getParameter("timezone");

        if (isValidTimeZone(timezone) ) {
            chain.doFilter(req, res);
        }else {
            res.setStatus(400);
            res.setContentType("text/html");
            res.getWriter().write("<h1>Invalid timezone: " + timezone + "</h1>");
            res.getWriter().close();
        }

    }
    public boolean isValidTimeZone(String timeZoneStr) {
        if (timeZoneStr == null || timeZoneStr.isEmpty()) {
            return true;
        }
        Set<String> availableZoneIds = Set.of(TimeZone.getAvailableIDs());
        return availableZoneIds.contains(timeZoneStr);
    }

}
