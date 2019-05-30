package worldcup.service;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import worldcup.constant.WorldCupConstant;
import worldcup.dao.*;
import worldcup.model.*;
import worldcup.util.WorldCupPropertyUtil;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

@Service
public class ParticipantService {
    private static final int TOTAL_COLUMNS = 21;
    private static final int EMAIL_COLUMN = 1;
    private static final int TOSS_COLUMN = 2;
    private static final int MATCH_WINNER_COLUMN = 3;
    private static final int RUN_RANGE_COLUMN = 4;
    private static final int WICKET_RANGE_COLUMN = 5;
    private static final int POINT_BOOSTER_COLUMN = 6;
    private static final int ROULETTE_COLUMN_START = 7;
    private static final int ROULETTE_COLUMN_END = 15;
    private static final int BATTING_CHOICE_COLUMN_START = 15;
    private static final int BATTING_CHOICE_COLUMN_END = 18;
    private static final int BOWLING_CHOICE_COLUMN_START = 18;
    private static final int BOWLING_CHOICE_COLUMN_END = 21;
    private static final String BOWLING_ROULETTE = "Bowling Roulette";
    public enum ROULETTE_TYPE{
        BATTING, BOWLING
    }

    private static final String INPUT_PREDICTION_FILE_NAME = "input/Match1-ENGvSA.csv";

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

    public void calculatePoints(Long matchId) {
        String line;
        boolean isTitleRow = true;
        ROULETTE_TYPE rouletteType = ROULETTE_TYPE.BATTING;
        String pointBoosterAnswer = WorldCupPropertyUtil.getInstance().getProperty(WorldCupConstant.POINT_BOOSTER_ANSWER_PROPERTY);
        try (BufferedReader br = new BufferedReader(new FileReader(WorldCupConstant.CSV_PATH + INPUT_PREDICTION_FILE_NAME))) {
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
                    if(StringUtils.isNotBlank(pointBoosterAnswer) && pointBoosterAnswer.equalsIgnoreCase(row[POINT_BOOSTER_COLUMN])){
                        predictionPoints.setPointBoosterPoints(
                                Integer.parseInt(WorldCupPropertyUtil.getInstance().getProperty(WorldCupConstant.POINT_BOOSTER_POINTS_PROPERTY)));
                    }
                    String[] roulette = Arrays.copyOfRange(row, ROULETTE_COLUMN_START, ROULETTE_COLUMN_END);
                    predictionService.calculateRoulettePoints(predictionPoints, rouletteType, roulette,match);
                    predictionPointsRepository.save(predictionPoints);

                    String[] battingChoice = Arrays.copyOfRange(row, BATTING_CHOICE_COLUMN_START, BATTING_CHOICE_COLUMN_END);
                    calculateOwnBattingStats(battingChoice[0], participant, match);

                    String[] bowlingChoice = Arrays.copyOfRange(row, BOWLING_CHOICE_COLUMN_START, BOWLING_CHOICE_COLUMN_END);
                    calculateOwnBowlingStats(bowlingChoice[0], participant, match);

                    String[] players = ArrayUtils.addAll(bowlingChoice, battingChoice);
                    calculateTeamStats(players, participant, match);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void calculateTeamStats(String[] choices, Participant participant, Match match) {
        for (String choice : choices) {
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
