package com.art9.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;


public final class JsonUtil {

    private static final Gson GSON = new GsonBuilder().create();

    private JsonUtil() {
    }

    public static void write(HttpServletResponse response, Object payload) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(GSON.toJson(payload));
    }

    public static String toJson(Object payload) {
        return GSON.toJson(payload);
    }
}
