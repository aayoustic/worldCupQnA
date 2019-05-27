package worldcup.util;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

public class Util {
    public static void appendURLParam(StringBuilder url, Map<String, String> params) {
        for (String key : params.keySet()) {
            appendURLParam(url, key, params.get(key));
        }
    }

    public static void appendURLParam(StringBuilder url, String param, String value) {
        if (!isURLParamAlreadyPresent(url.toString(), param, value)) {
            prepareURLForAppend(url);
            url.append(param);
            url.append("=");
            if (!StringUtils.isEmpty(value)) {
                try {
                    url.append(URLEncoder.encode(value, "UTF-8"));
                } catch (UnsupportedEncodingException ex) {
                }
            }
        }
    }

    public static boolean isURLParamAlreadyPresent(String url, String param, String value) {
        String paramValue = param + "=" + value;
        if (url.contains("&" + paramValue) || url.contains("?" + paramValue)) {
            return true;
        }
        return false;
    }

    public static void prepareURLForAppend(StringBuilder url) {
        if (url.indexOf("?") == -1) {
            url.append("?");
        } else if (url.charAt(url.length() - 1) != '&') {
            url.append("&");
        }
    }

    public static String readAllFromReader(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    public static URL prepareURL(String url){
        try {
            return new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
