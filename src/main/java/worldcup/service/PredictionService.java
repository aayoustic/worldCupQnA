package worldcup.service;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import worldcup.constant.WorldCupConstant;
import worldcup.cricketData.CricketInfo;
import worldcup.dao.BattingStatsRepository;
import worldcup.dao.BowlingStatsRepository;
import worldcup.model.BattingStats;
import worldcup.model.BowlingStats;
import worldcup.model.Match;
import worldcup.model.PredictionPoints;

import java.util.Optional;

@Service
public class PredictionService {

    @Autowired private CricketInfo<JSONObject> cricketInfo;
    @Autowired private BattingStatsRepository battingStatsRepository;
    @Autowired private BowlingStatsRepository bowlingStatsRepository;

    public void calculateRoulettePoints(PredictionPoints predictionPoints,
                                        ParticipantService.ROULETTE_TYPE rouletteType,
                                        String[] predictions,
                                        Match match) {
        int correctCount = 0;
        Optional<BattingStats> battingStats;
        Optional<BowlingStats> bowlingStats;
        for (String prediction : predictions) {
            String[] bounds;
            int upperBound = 0, lowerBound = 0;
            if (prediction.contains(WorldCupConstant.HYPHEN)) {
                bounds = prediction.split(WorldCupConstant.HYPHEN);
                lowerBound = Integer.parseInt(bounds[0]);
                upperBound = Integer.parseInt(bounds[1]);
            } else if (prediction.contains(WorldCupConstant.GREATER_THAN)) {
                bounds = prediction.split(WorldCupConstant.GREATER_THAN);
                lowerBound = Integer.parseInt(bounds[1]);
                upperBound = 10000;
            }
            if(ParticipantService.ROULETTE_TYPE.BATTING == rouletteType) {
                if(battingStatsRepository.
                        existsByMatchAndRunsGreaterThanEqualAndRunsLessThanEqual(match, lowerBound, upperBound)){
                    correctCount++;
                }
            } else if(ParticipantService.ROULETTE_TYPE.BOWLING == rouletteType) {
                if(bowlingStatsRepository.
                        existsByMatchAndRunsGreaterThanEqualAndRunsLessThanEqual(match, lowerBound, upperBound)){
                    correctCount++;
                }
            }
        }
        predictionPoints.setRoulettePoints(calculateRoulettePoints(correctCount));
    }

    public boolean isRunRangeCorrect(String prediction, Match match){
        int totalScore = match.getTeamOneRuns() + match.getTeamTwoRuns();
        int lowerBound = 0, upperBound = 0;
        String[] predictedScore;
        if (prediction.contains(WorldCupConstant.HYPHEN)) {
            predictedScore = prediction.split(WorldCupConstant.HYPHEN);
            lowerBound = Integer.parseInt(predictedScore[0]);
            upperBound = Integer.parseInt(predictedScore[1]);
        } else if (prediction.contains(WorldCupConstant.LESS_THAN)) {
            predictedScore = prediction.split(WorldCupConstant.LESS_THAN);
            upperBound = Integer.parseInt(predictedScore[1]);
        } else if (prediction.contains(WorldCupConstant.GREATER_THAN)){
            predictedScore = prediction.split(WorldCupConstant.GREATER_THAN);
            lowerBound = Integer.parseInt(predictedScore[1]);
            upperBound = 1000;
        }
        return lowerBound <= totalScore && totalScore <= upperBound;
    }

    public boolean isWicketRangeCorrect(String prediction, Match match) {
        int totalWickets = match.getTeamOneWickets() + match.getTeamTwoWickets();
        int lowerBound = 0, upperBound = 0;
        String[] predictedWickets;
        if (prediction.contains(WorldCupConstant.HYPHEN)) {
            predictedWickets = prediction.split(WorldCupConstant.HYPHEN);
            lowerBound = Integer.parseInt(predictedWickets[0]);
            upperBound = Integer.parseInt(predictedWickets[1]);
        } else if (prediction.contains(WorldCupConstant.LESS_THAN)) {
            predictedWickets = prediction.split(WorldCupConstant.LESS_THAN);
            upperBound = Integer.parseInt(predictedWickets[1]);
        } else if (prediction.contains(WorldCupConstant.GREATER_THAN)){
            predictedWickets = prediction.split(WorldCupConstant.GREATER_THAN);
            lowerBound = Integer.parseInt(predictedWickets[1]);
            upperBound = 20;
        }
        return lowerBound <= totalWickets && totalWickets <= upperBound;
    }

    private int calculateRoulettePoints(int correctAnswers){
        int points = 0;
        if(correctAnswers <= 1){
            points = -5;
        } else if(correctAnswers >= 3 && correctAnswers <= 5){
            points = 5;
        } else if(correctAnswers >= 6 && correctAnswers <= 7){
            points = 10;
        } else if(correctAnswers == 8){
            points = 20;
        }
        return points;
    }
}