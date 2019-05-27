package worldcup.service;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import worldcup.cricketData.CricketInfo;

import java.util.logging.Logger;

@Service
public class DailyStatsDataService {
    private static final Logger LOGGER = Logger.getLogger(DailyStatsDataService.class.getName());

    @Autowired
    private CricketInfo<JSONObject> cricketInfo;

    public void calculatePlayerStats(){
        JSONObject cricInfo = cricketInfo.getMatchSummary();
        LOGGER.info(cricInfo.getJSONObject("data").getString("man-of-the-match"));
    }

    public void loadSquads(){

    }
}
