package worldcup.model;

import javax.persistence.*;
import java.util.Date;

@Entity
public class DailyStats {

    @Id
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PLAYER_ID")
    private Player player;
    private int run;
    private int ball;
    private int six;
    private int four;
    private double strikeRate;
    private int wicket;
    private int concededRun;
    private int maiden;
    private int over;
    private double economyRate;
    private Date tlm;
    private Date whenCreated;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public int getRun() {
        return run;
    }

    public void setRun(int run) {
        this.run = run;
    }

    public int getBall() {
        return ball;
    }

    public void setBall(int ball) {
        this.ball = ball;
    }

    public int getSix() {
        return six;
    }

    public void setSix(int six) {
        this.six = six;
    }

    public int getFour() {
        return four;
    }

    public void setFour(int four) {
        this.four = four;
    }

    public double getStrikeRate() {
        return strikeRate;
    }

    public void setStrikeRate(double strikeRate) {
        this.strikeRate = strikeRate;
    }

    public int getWicket() {
        return wicket;
    }

    public void setWicket(int wicket) {
        this.wicket = wicket;
    }

    public int getConcededRun() {
        return concededRun;
    }

    public void setConcededRun(int concededRun) {
        this.concededRun = concededRun;
    }

    public int getMaiden() {
        return maiden;
    }

    public void setMaiden(int maiden) {
        this.maiden = maiden;
    }

    public int getOver() {
        return over;
    }

    public void setOver(int over) {
        this.over = over;
    }

    public double getEconomyRate() {
        return economyRate;
    }

    public void setEconomyRate(double economyRate) {
        this.economyRate = economyRate;
    }

    public Date getTlm() {
        return tlm;
    }

    public void setTlm(Date tlm) {
        this.tlm = tlm;
    }

    public Date getWhenCreated() {
        return whenCreated;
    }

    public void setWhenCreated(Date whenCreated) {
        this.whenCreated = whenCreated;
    }
}
