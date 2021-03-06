package worldcup.model;

import com.fasterxml.jackson.annotation.JsonAlias;

import javax.persistence.*;

@Entity
public class BowlingStats implements Stats {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "PLAYER_ID")
    private Player player;
    @ManyToOne
    @JoinColumn(name = "MATCH_ID")
    private Match match;
    @JsonAlias("O")
    private String overs;
    @JsonAlias("M")
    private String maidens;
    @JsonAlias("R")
    private String runs;
    @JsonAlias("W")
    private String wickets;
    private double economyRate;
    @JsonAlias("0s")
    private int dots;
    private boolean fourWicketHaul;
    private boolean fiveWicketHaul;

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

    public String getOvers() {
        return overs;
    }

    public void setOvers(String overs) {
        this.overs = overs;
    }

    public String getMaidens() {
        return maidens;
    }

    public void setMaidens(String maidens) {
        this.maidens = maidens;
    }

    public String getRuns() {
        return runs;
    }

    public void setRuns(String runs) {
        this.runs = runs;
    }

    public String getWickets() {
        return wickets;
    }

    public void setWickets(String wickets) {
        this.wickets = wickets;
        if(Integer.parseInt(wickets) == 4){
            setFourWicketHaul(true);
        } else if(Integer.parseInt(wickets) >= 5){
            setFiveWicketHaul(true);
        }
    }

    public double getEconomyRate() {
        return economyRate;
    }

    public void setEconomyRate(double economyRate) {
        this.economyRate = economyRate;
    }

    public int getDots() {
        return dots;
    }

    public void setDots(int dots) {
        this.dots = dots;
    }

    public boolean isFourWicketHaul() {
        return fourWicketHaul;
    }

    public void setFourWicketHaul(boolean fourWicketHaul) {
        this.fourWicketHaul = fourWicketHaul;
    }

    public boolean isFiveWicketHaul() {
        return fiveWicketHaul;
    }

    public void setFiveWicketHaul(boolean fiveWicketHaul) {
        this.fiveWicketHaul = fiveWicketHaul;
    }
}
