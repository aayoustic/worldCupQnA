package worldcup.model;

import javax.persistence.*;

@Entity
public class PredictionPoints {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "PARTICIPANT_ID")
    private Participant participant;
    @ManyToOne
    @JoinColumn(name = "MATCH_ID")
    private Match match;
    private int tossPoints;
    private int matchWinnerPoints;
    private int runRangePoints;
    private int wicketRangePoints;
    private int pointBoosterPoints;
    private int roulettePoints;

    public PredictionPoints() {
    }

    public PredictionPoints(Participant participant, Match match) {
        this.participant = participant;
        this.match = match;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Participant getParticipant() {
        return participant;
    }

    public void setParticipant(Participant participant) {
        this.participant = participant;
    }

    public Match getMatch() {
        return match;
    }

    public void setMatch(Match match) {
        this.match = match;
    }

    public int getTossPoints() {
        return tossPoints;
    }

    public void setTossPoints(int tossPoints) {
        this.tossPoints = tossPoints;
    }

    public int getMatchWinnerPoints() {
        return matchWinnerPoints;
    }

    public void setMatchWinnerPoints(int matchWinnerPoints) {
        this.matchWinnerPoints = matchWinnerPoints;
    }

    public int getRunRangePoints() {
        return runRangePoints;
    }

    public void setRunRangePoints(int runRangePoints) {
        this.runRangePoints = runRangePoints;
    }

    public int getWicketRangePoints() {
        return wicketRangePoints;
    }

    public void setWicketRangePoints(int wicketRangePoints) {
        this.wicketRangePoints = wicketRangePoints;
    }

    public int getPointBoosterPoints() {
        return pointBoosterPoints;
    }

    public void setPointBoosterPoints(int pointBoosterPoints) {
        this.pointBoosterPoints = pointBoosterPoints;
    }

    public int getRoulettePoints() {
        return roulettePoints;
    }

    public void setRoulettePoints(int roulettePoints) {
        this.roulettePoints = roulettePoints;
    }
}
