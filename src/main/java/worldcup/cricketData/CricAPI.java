package worldcup.cricketData;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import worldcup.service.MatchURLService;
import worldcup.util.Util;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.logging.Logger;

@Service
public class CricAPI implements CricketInfo {

    @Autowired private Util util;
    private static final Logger LOGGER = Logger.getLogger(CricAPI.class.getName());

    @Autowired
    private MatchURLService matchURLService;

    @Override
    public JSONObject getMatchSummary(String matchId){
        URL summaryUrl = matchURLService.getMatchSummaryURL(matchId);
        return callAPI(summaryUrl);
    }

    @Override
    public JSONObject getSquadsDetail(String matchId) {
        URL squadFetchUrl = matchURLService.getSquadFetchURL(matchId);
        return callAPI(squadFetchUrl);
    }


    @Override
    public JSONObject getPlayerDetail(String playerId) {
        URL playerDetailUrl = matchURLService.getPlayerDetailsURL(playerId);
        return callAPI(playerDetailUrl);
    }

    @Override
    public JSONObject getCricketScore(String matchId) {
        URL cricketScoreUrl = matchURLService.getCricketScoreURL(matchId);
        return callAPI(cricketScoreUrl);
    }

    @Override
    public JSONObject callAPI(URL url){
        try {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            int responseCode = connection.getResponseCode();
            LOGGER.info("Response Message - " + connection.getResponseMessage());
            if (responseCode == HttpURLConnection.HTTP_OK) {
                InputStream is = connection.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
                String jsonString = util.readAllFromReader(br);
                return new JSONObject(jsonString);
            } else {
                LOGGER.severe("Response Code - " + connection.getResponseCode());
                throw new RuntimeException();
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
