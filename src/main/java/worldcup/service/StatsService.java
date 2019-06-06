package worldcup.service;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import worldcup.constant.WorldCupConstant;
import worldcup.cricketData.CricketInfo;
import worldcup.dao.*;
import worldcup.model.*;
import worldcup.util.Util;

import java.util.logging.Logger;

@Service
public class StatsService {

    private static Logger LOGGER = Logger.getLogger(StatsService.class.getName());

    @Autowired private CricketInfo<JSONObject> cricketInfo;
    @Autowired private PlayerRepository playerRepository;
    @Autowired private MatchRepository matchRepository;
    @Autowired private Util util;
    @Autowired private BattingStatsRepository battingStatsRepository;
    @Autowired private BowlingStatsRepository bowlingStatsRepository;
    @Autowired private FieldingStatsRepository fieldingStatsRepository;

    public Player loadPlayer(Long playerId){
        LOGGER.info("=======> Loading Player with ID - " + playerId);
        JSONObject playerJSON = cricketInfo.getPlayerDetail(playerId.toString());
        Player player = util.getModelFromJson(playerJSON, Player.class);
        String playerRole = playerJSON.isNull(WorldCupConstant.PLAYER_ROLE_KEY) ?
                null : playerJSON.getString(WorldCupConstant.PLAYER_ROLE_KEY);
        if(playerRole != null) {
            player.setName(player.getName() + " - " + playerRole);
        }
        LOGGER.info("=======> Name is " + player.getName());
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
        double strikeRate = 0;
        if(battingStats.getBalls() > 0) {
            strikeRate = (battingStats.getRuns() * 100) / (double) battingStats.getBalls();
            strikeRate = util.roundOffToTwoPlaces(strikeRate);
        }
        battingStats.setStrikeRate(strikeRate);
        battingStatsRepository.save(battingStats);
    }

    public void loadBowlingStats(JSONObject playerJSON, Match match, Player player){
        BowlingStats bowlingStats = util.getModelFromJson(playerJSON, BowlingStats.class);
        double economyRate = Double.parseDouble(playerJSON.getString(WorldCupConstant.ECONOMY_KEY));
        bowlingStats.setEconomyRate(economyRate);
        setMatchAndPlayer(bowlingStats, match, player);
        bowlingStatsRepository.save(bowlingStats);
    }

    public void loadFieldingStats(JSONObject playerJSON, Match match, Player player){
        FieldingStats fieldingStats = util.getModelFromJson(playerJSON, FieldingStats.class);
        setMatchAndPlayer(fieldingStats, match, player);
        fieldingStatsRepository.save(fieldingStats);
    }

    public Match loadMatch(JSONObject data, String matchId){
        Match match = util.getModelFromJson(data, Match.class);
        match.setId(Long.parseLong(matchId));
        calculateTeamScores(match);
        matchRepository.save(match);
        return match;
    }

    private void calculateTeamScores(Match match) {
        JSONObject cricketScoreText = cricketInfo.getCricketScore(match.getId().toString());
        String entireScore = cricketScoreText.getString(WorldCupConstant.SCORE_KEY);
        String teamOne = cricketScoreText.getString(WorldCupConstant.TEAM_ONE_KEY);
        String teamTwo = cricketScoreText.getString(WorldCupConstant.TEAM_TWO_KEY);
        match.setTeam1(teamOne);
        match.setTeam2(teamTwo);
        String[] scores = entireScore.split(WorldCupConstant.V_DELIMITER);
        String[] scoreOne = scores[0].split(WorldCupConstant.SPACE);
        String[] scoreTwo = scores[1].split(WorldCupConstant.SPACE);
        int teamOneRuns = -1, teamTwoRuns = -1;
        int teamOneWickets = -1, teamTwoWickets = -1;
        for(String string: scoreOne){
            if(string.contains(WorldCupConstant.SLASH)){
                String[] parts = string.split(WorldCupConstant.SLASH);
                teamOneRuns = Integer.parseInt(parts[0]);
                teamOneWickets = Integer.parseInt(parts[1]);
            }
        }
        for (String string : scoreTwo) {
            if(string.contains(WorldCupConstant.SLASH)){
                String[] parts = string.split(WorldCupConstant.SLASH);
                teamTwoRuns = Integer.parseInt(parts[0]);
                teamTwoWickets = Integer.parseInt(parts[1]);
            }
        }
        match.setTeamOneRuns(teamOneRuns);
        match.setTeamTwoRuns(teamTwoRuns);
        match.setTeamOneWickets(teamOneWickets);
        match.setTeamTwoWickets(teamTwoWickets);

    }

    private void setMatchAndPlayer(Stats stats, Match match, Player player){
        stats.setMatch(match);
        stats.setPlayer(player);
    }
}
