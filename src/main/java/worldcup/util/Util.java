package worldcup.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.Map;

@Service
public class Util {
    @Autowired private ObjectMapper objectMapper;

    public void appendURLParam(StringBuilder url, Map<String, String> params) {
        for (String key : params.keySet()) {
            appendURLParam(url, key, params.get(key));
        }
    }

    public void appendURLParam(StringBuilder url, String param, String value) {
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

    public boolean isURLParamAlreadyPresent(String url, String param, String value) {
        String paramValue = param + "=" + value;
        if (url.contains("&" + paramValue) || url.contains("?" + paramValue)) {
            return true;
        }
        return false;
    }

    public void prepareURLForAppend(StringBuilder url) {
        if (url.indexOf("?") == -1) {
            url.append("?");
        } else if (url.charAt(url.length() - 1) != '&') {
            url.append("&");
        }
    }

    public String readAllFromReader(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    public URL prepareURL(String url){
        try {
            return new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public double roundOffToTwoPlaces(double value){
        DecimalFormat pattern = new DecimalFormat("#.##");
        return Double.parseDouble(pattern.format(value));
    }

    public <T> T getModelFromJson(JSONObject jsonObject, Class<T> modelClass) {
        return getModelFromJson(jsonObject.toString(), modelClass);
    }

    public <T> T getModelFromJson(String jsonString, Class<T> modelClass) {
        try {
            return (T) objectMapper.readValue(jsonString, modelClass);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

}
