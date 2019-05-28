package worldcup.service;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import worldcup.constant.WorldCupConstant;
import worldcup.cricketData.CricketInfo;
import worldcup.dao.*;
import worldcup.model.*;
import worldcup.util.Util;

@Service
public class StatsService {

    @Autowired private CricketInfo<JSONObject> cricketInfo;
    @Autowired private PlayerRepository playerRepository;
    @Autowired private MatchRepository matchRepository;
    @Autowired private Util util;
    @Autowired private BattingStatsRepository battingStatsRepository;
    @Autowired private BowlingStatsRepository bowlingStatsRepository;
    @Autowired private FieldingStatsRepository fieldingStatsRepository;

    public Player loadPlayer(Long playerId){
        JSONObject playerJSON = cricketInfo.getPlayerDetail(playerId.toString());
        Player player = util.getModelFromJson(playerJSON, Player.class);
        playerRepository.save(player);
        return player;
    }

    public void loadBattingStats(JSONObject playerJSON, Match match, Player player){
        BattingStats battingStats = util.getModelFromJson(playerJSON, BattingStats.class);
        setMatchAndPlayer(battingStats, match, player);
        if(playerJSON.getString(WorldCupConstant.DISMISSAL_KEY).equalsIgnoreCase(WorldCupConstant.NOT_OUT)){
            battingStats.setNotOut(true);
        } else {
            battingStats.setNotOut(false);
        }
        double strikeRate = (battingStats.getRuns() * 100) / (double) battingStats.getBalls();
        battingStats.setStrikeRate(util.roundOffToTwoPlaces(strikeRate));
        battingStatsRepository.save(battingStats);
    }

    public void loadBowlingStats(JSONObject playerJSON, Match match, Player player){
        BowlingStats bowlingStats = util.getModelFromJson(playerJSON, BowlingStats.class);
        setMatchAndPlayer(bowlingStats, match, player);
        bowlingStatsRepository.save(bowlingStats);
    }

    public void loadFieldingStats(JSONObject playerJSON, Match match, Player player){
        FieldingStats fieldingStats = util.getModelFromJson(playerJSON, FieldingStats.class);
        setMatchAndPlayer(fieldingStats, match, player);
        fieldingStatsRepository.save(fieldingStats);
    }

    public Match loadMatch(JSONObject data, String matchId){
        String team1 = data.getJSONArray(WorldCupConstant.TEAM_KEY).getJSONObject(0).getString(WorldCupConstant.NAME_KEY);
        String team2 = data.getJSONArray(WorldCupConstant.TEAM_KEY).getJSONObject(1).getString(WorldCupConstant.NAME_KEY);
        Match match = util.getModelFromJson(data, Match.class);
        match.setId(Long.parseLong(matchId));
        match.setTeam1(team1);
        match.setTeam2(team2);
        matchRepository.save(match);
        return match;
    }

    private void setMatchAndPlayer(Stats stats, Match match, Player player){
        stats.setMatch(match);
        stats.setPlayer(player);
    }
}
