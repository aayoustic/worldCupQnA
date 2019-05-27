package worldcup.service;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import worldcup.constant.WorldCupConstant;
import worldcup.cricketData.CricketInfo;
import worldcup.dao.PlayerRepository;
import worldcup.model.Player;

import java.util.logging.Logger;

@Service
public class DailyStatsDataService {
    private static final Logger LOGGER = Logger.getLogger(DailyStatsDataService.class.getName());

    @Autowired private CricketInfo<JSONObject> cricketInfo;
    @Autowired private PlayerRepository playerRepository;


    public void calculatePlayerStats(){
        JSONObject cricInfo = cricketInfo.getMatchSummary();
        LOGGER.info(cricInfo.getJSONObject("data").getString("man-of-the-match"));
    }

    public void loadSquads(String matchId){
        JSONObject squadDetail = cricketInfo.getSquadsDetail(matchId);
        JSONArray squads = squadDetail.getJSONArray(WorldCupConstant.SQUAD_KEY);
        loadPlayers(squads.getJSONObject(0));
        loadPlayers(squads.getJSONObject(1));
    }

    private void loadPlayers(JSONObject squad){
        String team = squad.getString(WorldCupConstant.NAME_KEY);
        JSONArray players = squad.getJSONArray(WorldCupConstant.PLAYERS_KEY);
        for (int i = 0; i < players.length(); i++) {
            JSONObject playerJSON = players.getJSONObject(i);
            Player player = new Player();
            player.setId(playerJSON.getLong(WorldCupConstant.PLAYER_ID_KEY));
            player.setName(playerJSON.getString(WorldCupConstant.NAME_KEY));
            player.setTeam(team);
            playerRepository.save(player);
        }
    }
}
