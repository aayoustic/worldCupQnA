package worldcup.service;

import org.springframework.stereotype.Service;
import worldcup.constant.WorldCupConstant;
import worldcup.util.Util;
import worldcup.util.WorldCupPropertyUtil;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@Service
public class MatchURLService {

    public URL getMatchSummaryURL(){
        StringBuilder summaryAPIUrl = new StringBuilder(
                WorldCupPropertyUtil.getInstance().getProperty(WorldCupConstant.SUMMARY_API_URL_PROPERTY));
        Map<String, String> params = new HashMap<>();
        String apiKey = WorldCupPropertyUtil.getInstance().getProperty(WorldCupConstant.API_KEY_PROPERTY);
        String matchUniqueKey = WorldCupPropertyUtil.getInstance().getProperty(WorldCupConstant.API_KEY_PROPERTY);
        params.put(WorldCupConstant.API_KEY_PARAM, apiKey);
        params.put(WorldCupConstant.MATCH_UNIQUE_ID_PARAM, matchUniqueKey);
        Util.appendURLParam(summaryAPIUrl, params);
        return prepareURL(summaryAPIUrl.toString());
    }

    public URL getSquadFetchURL(){
        StringBuilder squadAPIUrl = new StringBuilder(
                WorldCupPropertyUtil.getInstance().getProperty(WorldCupConstant.SQUAD_API_URL_PROPERTY));
        Map<String, String> params = new HashMap<>();
        String apiKey = WorldCupPropertyUtil.getInstance().getProperty(WorldCupConstant.API_KEY_PROPERTY);
        String matchUniqueKey = WorldCupPropertyUtil.getInstance().getProperty(WorldCupConstant.API_KEY_PROPERTY);
        params.put(WorldCupConstant.API_KEY_PARAM, apiKey);
        params.put(WorldCupConstant.MATCH_UNIQUE_ID_PARAM, matchUniqueKey);
        Util.appendURLParam(squadAPIUrl, params);
        return prepareURL(squadAPIUrl.toString());
    }

    private URL prepareURL(String url){
        try {
            return new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
