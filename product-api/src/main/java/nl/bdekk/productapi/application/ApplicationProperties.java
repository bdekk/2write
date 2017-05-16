package nl.bdekk.productapi.application;

import java.util.function.Function;

public class ApplicationProperties {

    private static final String BASE_URL = "AUTH_BASE_URL";
    private static final String DEFAULT_BASE_URL = "http://localhost:8080";

    private static final String CONTEXT_PATH = "AUTH_CONTEXT_PATH";
    private static final String DEFAULT_CONTEXT_PATH = "/rest";

    private static final String HTTP_PORT = "AUTH_HTTP_PORT";
    private static final int DEFAULT_HTTP_PORT = 8080;

    public static String getBaseURL() {
        return getString(BASE_URL, DEFAULT_BASE_URL);
    }

    public static String getContextPath() {
        return getString(CONTEXT_PATH, DEFAULT_CONTEXT_PATH);
    }

    public static int getHTTPPort() {
        return getInt(HTTP_PORT, DEFAULT_HTTP_PORT);
    }

    private static String getString(String name, String defaultValue) {
        String result = get(name, defaultValue, Object::toString);
        return result;
    }

    private static int getInt(String name, int defaultValue) {
        int result = get(name, defaultValue, Integer::valueOf);
        return result;
    }

    private static <T> T get(String name, T defaultValue, Function<String, T> converter) {
        String string = System.getenv(name);
        T result;
        if (string != null && !string.isEmpty()) {
            result = converter.apply(string);
        } else {
            result = defaultValue;
        }
        return result;
    }
}
