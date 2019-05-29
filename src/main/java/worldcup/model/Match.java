package worldcup.model;

import com.fasterxml.jackson.annotation.JsonAlias;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "`MATCH`")
public class Match {

    @Id
    private Long id;
    private String team1;
    private String team2;
    @JsonAlias("toss_winner_team")
    private String tossWinner;
    @JsonAlias("winner_team")
    private String matchWinner;
    private int teamOneRuns;
    private int teamTwoRuns;
    private int teamOneWickets;
    private int teamTwoWickets;

    protected Match(){}

    public Match(Long id, String team1, String team2) {
        this.id = id;
        this.team1 = team1;
        this.team2 = team2;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTeam1() {
        return team1;
    }

    public void setTeam1(String team1) {
        this.team1 = team1;
    }

    public String getTeam2() {
        return team2;
    }

    public void setTeam2(String team2) {
        this.team2 = team2;
    }

    public String getTossWinner() {
        return tossWinner;
    }

    public void setTossWinner(String tossWinner) {
        this.tossWinner = tossWinner;
    }

    public String getMatchWinner() {
        return matchWinner;
    }

    public void setMatchWinner(String matchWinner) {
        this.matchWinner = matchWinner;
    }

    public int getTeamOneRuns() {
        return teamOneRuns;
    }

    public void setTeamOneRuns(int teamOneRuns) {
        this.teamOneRuns = teamOneRuns;
    }

    public int getTeamTwoRuns() {
        return teamTwoRuns;
    }

    public void setTeamTwoRuns(int teamTwoRuns) {
        this.teamTwoRuns = teamTwoRuns;
    }

    public int getTeamOneWickets() {
        return teamOneWickets;
    }

    public void setTeamOneWickets(int teamOneWickets) {
        this.teamOneWickets = teamOneWickets;
    }

    public int getTeamTwoWickets() {
        return teamTwoWickets;
    }

    public void setTeamTwoWickets(int teamTwoWickets) {
        this.teamTwoWickets = teamTwoWickets;
    }
}
