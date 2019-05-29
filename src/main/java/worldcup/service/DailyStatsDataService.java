package worldcup.service;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import worldcup.constant.WorldCupConstant;
import worldcup.cricketData.CricketInfo;
import worldcup.dao.*;
import worldcup.model.Match;
import worldcup.model.Player;
import worldcup.util.Util;

import java.util.Optional;

@Service
public class DailyStatsDataService {

    @Autowired private StatsService statsService;
    @Autowired private Util util;
    @Autowired private CricketInfo<JSONObject> cricketInfo;

    @Autowired private PlayerRepository playerRepository;
    @Autowired private MatchRepository matchRepository;
    @Autowired private BattingStatsRepository battingStatsRepository;
    @Autowired private BowlingStatsRepository bowlingStatsRepository;
    @Autowired private FieldingStatsRepository fieldingStatsRepository;


    public void addDailyStats(String matchId){
        JSONObject matchSummary = cricketInfo.getMatchSummary(matchId);
        JSONObject data = matchSummary.getJSONObject(WorldCupConstant.DATA_KEY);
        Match match = statsService.loadMatch(data, matchId);
        JSONArray battingData = data.getJSONArray(WorldCupConstant.BATTING_KEY);
        JSONArray bowlingData = data.getJSONArray(WorldCupConstant.BOWLING_KEY);
        JSONArray fieldingData = data.getJSONArray(WorldCupConstant.FIELDING_KEY);
        calculateAndUpdatePlayerStats(battingData, match);
        calculateAndUpdatePlayerStats(bowlingData, match);
        calculateAndUpdatePlayerStats(fieldingData, match);
    }

    public void loadSquads(String matchId){
        JSONObject squadDetail = cricketInfo.getSquadsDetail(matchId);
        JSONArray squads = squadDetail.getJSONArray(WorldCupConstant.SQUAD_KEY);
        loadPlayers(squads.getJSONObject(0));
        loadPlayers(squads.getJSONObject(1));
    }

    private void loadPlayers(JSONObject squad){
        JSONArray players = squad.getJSONArray(WorldCupConstant.PLAYERS_KEY);
        for (int i = 0; i < players.length(); i++) {
            Long playerId = players.getJSONObject(i).getLong(WorldCupConstant.PLAYER_ID_KEY);
            statsService.loadPlayer(playerId);
        }
    }

    private void calculateAndUpdatePlayerStats(JSONArray data, Match match){
        JSONObject squad1 = data.getJSONObject(0);
        JSONObject squad2 = data.getJSONObject(1);
        JSONArray scores1 = squad1.getJSONArray(WorldCupConstant.SCORES_KEY);
        JSONArray scores2 = squad2.getJSONArray(WorldCupConstant.SCORES_KEY);
        calculatePlayerStats(scores1, match);
        calculatePlayerStats(scores2, match);
    }

    private void calculatePlayerStats(JSONArray data, Match match){
        for(int i=0;i<data.length();i++){
            JSONObject playerJSON = data.getJSONObject(i);
            Object pid = playerJSON.get(WorldCupConstant.PLAYER_ID_KEY);
            Long playerId = Long.parseLong(pid.toString());
            if(playerId > 0) {
                Optional<Player> playerOptional = playerRepository.findById(playerId);
                Player player = playerOptional.orElseGet(() -> statsService.loadPlayer(playerId));
                updatePlayerStats(playerJSON, player, match);
            }
        }
    }

    private void updatePlayerStats(JSONObject playerJSON, Player player, Match match) {
        if(!playerJSON.isNull(WorldCupConstant.BATSMAN_KEY)) {
            statsService.loadBattingStats(playerJSON, match, player); // is a batsman
        }else if(!playerJSON.isNull(WorldCupConstant.BOWLER_KEY)) {
            statsService.loadBowlingStats(playerJSON, match, player); // is a bowler
        } else{
            statsService.loadFieldingStats(playerJSON, match, player); // is a fielder
        }
    }
}
