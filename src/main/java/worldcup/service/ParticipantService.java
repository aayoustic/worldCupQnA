package worldcup.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import worldcup.constant.WorldCupConstant;
import worldcup.dao.*;
import worldcup.model.*;
import worldcup.util.Util;
import worldcup.util.WorldCupPropertyUtil;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

@Service
public class ParticipantService {
    private static final int CHANGE_COLUMN = 2;
    private static final int TOTAL_COLUMNS = 22 - CHANGE_COLUMN;

    private static final int EMAIL_COLUMN = 1;
    private static final int TOSS_COLUMN = 2;
    private static final int MATCH_WINNER_COLUMN = 3;
    private static final int RUN_RANGE_COLUMN = 4;
    private static final int WICKET_RANGE_COLUMN = 5;
    private static final int POINT_BOOSTER_COLUMN = 6;
    private static final int ROULETTE_COLUMN_START = 7;
    private static final int ROULETTE_COLUMN_END = 15 - CHANGE_COLUMN;
    private static final int BATTING_CHOICE_COLUMN = 15 - CHANGE_COLUMN;
    private static final int BOWLING_CHOICE_COLUMN = 16 - CHANGE_COLUMN;
    private static final int PLAYER_CHOICE_COLUMN_START = 15 - CHANGE_COLUMN;
    private static final int PLAYER_CHOICE_COLUMN_END = 21 - CHANGE_COLUMN;
    private static final int BONUS_COLUMN_START = 21 - CHANGE_COLUMN;
    private static final String BOWLING_ROULETTE = "Bowling Roulette";
    public enum ROULETTE_TYPE{
        BATTING, BOWLING
    }

    @Autowired
    private ParticipantRepository participantRepository;
    @Autowired
    private MatchRepository matchRepository;
    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private BattingStatsRepository battingStatsRepository;
    @Autowired
    private BowlingStatsRepository bowlingStatsRepository;
    @Autowired
    private FieldingStatsRepository fieldingStatsRepository;
    @Autowired
    private ParticipantBattingStatsRepository participantBattingStatsRepository;
    @Autowired
    private ParticipantBowlingStatsRepository participantBowlingStatsRepository;
    @Autowired
    private ParticipantFieldingStatsRepository participantFieldingStatsRepository;
    @Autowired
    private PredictionPointsRepository predictionPointsRepository;
    @Autowired
    private TeamStatsRepository teamStatsRepository;
    @Autowired
    private TeamStatsPointRepository teamStatsPointRepository;

    @Autowired
    private PredictionService predictionService;
    @Autowired
    private Util util;

    public void calculatePoints(Long matchId) {
        String line;
        boolean isTitleRow = true;
        ROULETTE_TYPE rouletteType = ROULETTE_TYPE.BATTING;
        String pointBoosterAnswer = WorldCupPropertyUtil.getInstance().getProperty(WorldCupConstant.POINT_BOOSTER_ANSWER_PROPERTY);
        try (BufferedReader br = new BufferedReader(new FileReader(WorldCupPropertyUtil.getInstance().getProperty(WorldCupConstant.CSV_INPUT_PROPERTY)))) {
            while ((line = br.readLine()) != null) {
                String[] row = line.split(WorldCupConstant.COMMA_DELIMITER);
                if (isTitleRow) {
                    String[] columns = line.split(WorldCupConstant.COMMA_DELIMITER);
                    if (columns.length != TOTAL_COLUMNS) {
                        throw new RuntimeException("Incorrect no of columns - " + columns.length);
                    }
                    if(row[ROULETTE_COLUMN_START].contains(BOWLING_ROULETTE)){
                        rouletteType = ROULETTE_TYPE.BOWLING;
                    }
                    isTitleRow = false;
                    continue;
                }
                Optional<Participant> participantOptional = participantRepository.findByEmail(row[EMAIL_COLUMN]);
                Optional<Match> matchOptional = matchRepository.findById(matchId);
                if (participantOptional.isPresent() && matchOptional.isPresent()) {
                    Participant participant = participantOptional.get();
                    Match match = matchOptional.get();
                    PredictionPoints predictionPoints = new PredictionPoints(participantOptional.get(), match);
                    if (row[TOSS_COLUMN].equalsIgnoreCase(match.getTossWinner())) {
                        predictionPoints.setTossPoints(
                                Integer.parseInt(WorldCupPropertyUtil.getInstance().getProperty(WorldCupConstant.TOSS_POINTS_PROPERTY)));
                    }
                    if (row[MATCH_WINNER_COLUMN].equalsIgnoreCase(match.getMatchWinner())) {
                        predictionPoints.setMatchWinnerPoints(
                                Integer.parseInt(WorldCupPropertyUtil.getInstance().getProperty(WorldCupConstant.MATCH_WINNER_POINTS_PROPERTY)));
                    }
                    if (predictionService.isRunRangeCorrect(row[RUN_RANGE_COLUMN], match)) {
                        predictionPoints.setRunRangePoints(
                                Integer.parseInt(WorldCupPropertyUtil.getInstance().getProperty(WorldCupConstant.RUN_RANGE_POINTS_PROPERTY)));
                    }
                    if (predictionService.isWicketRangeCorrect(row[WICKET_RANGE_COLUMN], match)) {
                        predictionPoints.setWicketRangePoints(
                                Integer.parseInt(WorldCupPropertyUtil.getInstance().getProperty(WorldCupConstant.WICKET_RANGE_POINTS_PROPERTY)));
                    }
                    if(StringUtils.isNotBlank(pointBoosterAnswer) && pointBoosterAnswer.equals(row[POINT_BOOSTER_COLUMN])){
                        predictionPoints.setPointBoosterPoints(
                                Integer.parseInt(WorldCupPropertyUtil.getInstance().getProperty(WorldCupConstant.POINT_BOOSTER_POINTS_PROPERTY)));
                    }
                    String[] roulette = Arrays.copyOfRange(row, ROULETTE_COLUMN_START, ROULETTE_COLUMN_END);
                    predictionPoints.setRoulettePoints(predictionService.calculateRoulettePoints(rouletteType, roulette,match));

                    calculateOwnBattingStats(row[BATTING_CHOICE_COLUMN], participant, match);
                    calculateOwnBowlingStats(row[BOWLING_CHOICE_COLUMN], participant, match);

                    String[] players = Arrays.copyOfRange(row, PLAYER_CHOICE_COLUMN_START, PLAYER_CHOICE_COLUMN_END);

                    calculateTeamStats(players, participant, match);

                    int totalBonusPoints = 0;
                    for(int i=BONUS_COLUMN_START,bonusQuestion=1; i<row.length;i++,bonusQuestion++){
                        String bonusAnswerProperty = util.getBonusQuestionProperty(bonusQuestion, Util.BONUS_PROPERTY.answer);
                        String bonusPrediction = row[i];
                        if(StringUtils.isNotBlank(bonusPrediction) && bonusPrediction.equals(WorldCupPropertyUtil.getInstance().getProperty(bonusAnswerProperty))){
                            String bonusPointProperty = util.getBonusQuestionProperty(bonusQuestion, Util.BONUS_PROPERTY.point);
                            totalBonusPoints += Integer.parseInt(WorldCupPropertyUtil.getInstance().getProperty(bonusPointProperty));
                        }
                    }
                    predictionPoints.setBonusPoints(totalBonusPoints);
                    predictionPointsRepository.save(predictionPoints);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void calculateTeamStats(String[] choices, Participant participant, Match match) {
        Set<String> uniqueChoices = new HashSet<>(Arrays.asList(choices)); // remove redundant choices
        for (String choice : uniqueChoices) {
            Optional<Player> player = playerRepository.findByName(choice);
            if(player.isPresent()) {
                TeamStats teamStats = new TeamStats();
                teamStats.setMatch(match);
                teamStats.setParticipant(participant);
                teamStats.setPlayer(player.get());
                teamStatsRepository.save(teamStats);
            } else {
                throw new RuntimeException("==========> Player not found for name - " + choice);
            }
        }
    }

    private void calculateOwnBattingStats(String choice, Participant participant, Match match){
        Optional<Player> playerOptional1 = playerRepository.findByName(choice);
        if(playerOptional1.isPresent()) {
            Player meAsPlayer = playerOptional1.get();
            Optional<BattingStats> battingStats = battingStatsRepository.findByMatchAndPlayer(match, meAsPlayer);
            if(battingStats.isPresent()){
                ParticipantBattingStats participantBattingStats = new ParticipantBattingStats();
                participantBattingStats.setParticipant(participant);
                participantBattingStats.setMatch(match);
                participantBattingStats.setBattingStats(battingStats.get());
                participantBattingStatsRepository.save(participantBattingStats);
            }
            addFieldingStats(participant, match, meAsPlayer);
        } else {
            throw new RuntimeException("==========> Player not found for name - " + choice);
        }
    }

    private void calculateOwnBowlingStats(String choice, Participant participant, Match match){
        Optional<Player> playerOptional1 = playerRepository.findByName(choice);
        if(playerOptional1.isPresent()) {
            Player meAsPlayer = playerOptional1.get();
            Optional<BowlingStats> bowlingStats = bowlingStatsRepository.findByMatchAndPlayer(match, meAsPlayer);
            if(bowlingStats.isPresent()){
                ParticipantBowlingStats participantBowlingStats = new ParticipantBowlingStats();
                participantBowlingStats.setParticipant(participant);
                participantBowlingStats.setMatch(match);
                participantBowlingStats.setBowlingStats(bowlingStats.get());
                participantBowlingStatsRepository.save(participantBowlingStats);
            }
            addFieldingStats(participant, match, meAsPlayer);
        } else {
            throw new RuntimeException("==========> Player not found for name - " + choice);
        }
    }

    private void addFieldingStats(Participant participant, Match match, Player player){
        Optional<FieldingStats> fieldingStats = fieldingStatsRepository.findByMatchAndPlayer(match, player);
        if(fieldingStats.isPresent()){
            ParticipantFieldingStats participantFieldingStats = new ParticipantFieldingStats();
            participantFieldingStats.setParticipant(participant);
            participantFieldingStats.setMatch(match);
            participantFieldingStats.setFieldingStats(fieldingStats.get());
            participantFieldingStatsRepository.save(participantFieldingStats);
        }
    }

    public void calculateTeamPoints(Long matchId) {
        List<Object[]> teamStats = teamStatsRepository.findTeamStats(matchId);
        for (Object[] teamStat : teamStats) {
            int columnCount = 0;
            Long participantId = Long.parseLong(teamStat[columnCount++].toString());
            Optional<Participant> participant = participantRepository.findById(participantId);
            Optional<Match> match = matchRepository.findById(matchId);
            if(participant.isPresent() && match.isPresent()){
                TeamStatsPoints teamStatsPoints = new TeamStatsPoints();
                teamStatsPoints.setParticipant(participant.get());
                teamStatsPoints.setMatch(match.get());
                teamStatsPoints.setRunsPoint(Long.parseLong(teamStat[columnCount++].toString()));
                teamStatsPoints.setFoursPoint(Long.parseLong(teamStat[columnCount++].toString()) * 2);
                teamStatsPoints.setSixesPoint(Long.parseLong(teamStat[columnCount++].toString()) * 3);
                teamStatsPoints.setFiftiesPoint(Long.parseLong(teamStat[columnCount++].toString()) * 5);
                teamStatsPoints.setCenturiesPoint(Long.parseLong(teamStat[columnCount++].toString()) * 10);
                teamStatsPoints.setWicketPoint(Long.parseLong(teamStat[columnCount++].toString()) * 24);
                teamStatsPoints.setMaidensPoint(Long.parseLong(teamStat[columnCount++].toString()) * 3);
                teamStatsPoints.setFourWicketHaulPoint(Long.parseLong(teamStat[columnCount++].toString()) * 5);
                teamStatsPoints.setFiveWicketHaulPoint(Long.parseLong(teamStat[columnCount++].toString()) * 10);
                teamStatsPoints.setCatchesPoint(Long.parseLong(teamStat[columnCount++].toString()) * 4);
                teamStatsPoints.setStumpingsPoint(Long.parseLong(teamStat[columnCount].toString()) * 4);
                teamStatsPointRepository.save(teamStatsPoints);
            } else{
                throw new RuntimeException("Match " + matchId + " or Participant " + participantId + " doesn't exist.");
            }
        }
    }
}
