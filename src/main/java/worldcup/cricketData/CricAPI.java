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
    private static final Logger LOGGER = Logger.getLogger(CricAPI.class.getName());

    @Autowired
    MatchURLService matchURLService;

    @Override
    public JSONObject getMatchSummary(){
        URL summaryUrl = matchURLService.getMatchSummaryURL();
        LOGGER.info("summaryUrl === " + summaryUrl);
        return callCricAPI(summaryUrl);
    }

    @Override
    public Object getSquad() {
        URL squadFetchUrl = matchURLService.getSquadFetchURL();
        LOGGER.info("squadFetchUrl === " + squadFetchUrl);
        return callCricAPI(squadFetchUrl);
    }

    private JSONObject callCricAPI(URL url){
        try {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            int responseCode = connection.getResponseCode();
            LOGGER.info("Response Message - " + connection.getResponseMessage());
            if (responseCode == HttpURLConnection.HTTP_OK) {
                InputStream is = connection.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
                String jsonString = Util.readAllFromReader(br);
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
