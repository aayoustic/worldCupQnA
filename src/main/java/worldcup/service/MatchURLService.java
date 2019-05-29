package worldcup.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import worldcup.constant.WorldCupConstant;
import worldcup.util.Util;
import worldcup.util.WorldCupPropertyUtil;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@Service
public class MatchURLService {

    @Autowired private Util util;

    public URL getMatchSummaryURL(String matchId){
        StringBuilder summaryAPIUrl = new StringBuilder(
                WorldCupPropertyUtil.getInstance().getProperty(WorldCupConstant.SUMMARY_API_URL_PROPERTY));
        Map<String, String> params = new HashMap<>();
        String apiKey = WorldCupPropertyUtil.getInstance().getProperty(WorldCupConstant.API_KEY_PROPERTY);
        params.put(WorldCupConstant.API_KEY_PARAM, apiKey);
        params.put(WorldCupConstant.MATCH_UNIQUE_ID_PARAM, matchId);
        util.appendURLParam(summaryAPIUrl, params);
        return util.prepareURL(summaryAPIUrl.toString());
    }

    public URL getSquadFetchURL(String matchId){
        StringBuilder squadAPIUrl = new StringBuilder(
                WorldCupPropertyUtil.getInstance().getProperty(WorldCupConstant.SQUAD_API_URL_PROPERTY));
        Map<String, String> params = new HashMap<>();
        String apiKey = WorldCupPropertyUtil.getInstance().getProperty(WorldCupConstant.API_KEY_PROPERTY);
        params.put(WorldCupConstant.API_KEY_PARAM, apiKey);
        params.put(WorldCupConstant.MATCH_UNIQUE_ID_PARAM, matchId);
        util.appendURLParam(squadAPIUrl, params);
        return util.prepareURL(squadAPIUrl.toString());
    }

    public URL getPlayerDetailsURL(String playerId){
        StringBuilder squadAPIUrl = new StringBuilder(
                WorldCupPropertyUtil.getInstance().getProperty(WorldCupConstant.PLAYER_DETAILS_URL_PROPERTY));
        Map<String, String> params = new HashMap<>();
        String apiKey = WorldCupPropertyUtil.getInstance().getProperty(WorldCupConstant.API_KEY_PROPERTY);
        params.put(WorldCupConstant.API_KEY_PARAM, apiKey);
        params.put(WorldCupConstant.PLAYER_ID_PARAM, playerId);
        util.appendURLParam(squadAPIUrl, params);
        return util.prepareURL(squadAPIUrl.toString());
    }

    public URL getCricketScoreURL(String matchId){
        StringBuilder cricketScoreUrl = new StringBuilder(
                WorldCupPropertyUtil.getInstance().getProperty(WorldCupConstant.CRICKET_SCORE_URL_PROPERTY));
        Map<String, String> params = new HashMap<>();
        String apiKey = WorldCupPropertyUtil.getInstance().getProperty(WorldCupConstant.API_KEY_PROPERTY);
        params.put(WorldCupConstant.API_KEY_PARAM, apiKey);
        params.put(WorldCupConstant.MATCH_UNIQUE_ID_PARAM, matchId);
        util.appendURLParam(cricketScoreUrl, params);
        return util.prepareURL(cricketScoreUrl.toString());
    }
}
