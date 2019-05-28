package worldcup.model;

import com.fasterxml.jackson.annotation.JsonAlias;

import javax.persistence.*;

@Entity
public class BattingStats implements Stats {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "PLAYER_ID")
    private Player player;
    @ManyToOne
    @JoinColumn(name = "MATCH_ID")
    private Match match;
    @JsonAlias("R")
    private int runs;
    @JsonAlias("B")
    private int balls;
    @JsonAlias("6s")
    private int sixes;
    @JsonAlias("4s")
    private int fours;
    private double strikeRate;
    private boolean notOut;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    @Override
    public void setPlayer(Player player) {
        this.player = player;
    }

    @Override
    public Match getMatch() {
        return match;
    }

    @Override
    public void setMatch(Match match) {
        this.match = match;
    }

    public int getRuns() {
        return runs;
    }

    public void setRuns(int runs) {
        this.runs = runs;
    }

    public int getBalls() {
        return balls;
    }

    public void setBalls(int balls) {
        this.balls = balls;
    }

    public int getSixes() {
        return sixes;
    }

    public void setSixes(int sixes) {
        this.sixes = sixes;
    }

    public int getFours() {
        return fours;
    }

    public void setFours(int fours) {
        this.fours = fours;
    }

    public double getStrikeRate() {
        return strikeRate;
    }

    public void setStrikeRate(double strikeRate) {
        this.strikeRate = strikeRate;
    }

    public boolean isNotOut() {
        return notOut;
    }

    public void setNotOut(boolean notOut) {
        this.notOut = notOut;
    }
}
