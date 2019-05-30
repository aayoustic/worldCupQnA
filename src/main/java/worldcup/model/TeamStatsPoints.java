package worldcup.model;

import javax.persistence.*;

@Entity
public class TeamStatsPoints {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "PARTICIPANT_ID")
    private Participant participant;
    @ManyToOne
    @JoinColumn(name = "MATCH_ID")
    private Match match;
    private Long runsPoint;
    private Long foursPoint;
    private Long sixesPoint;
    private Long fiftiesPoint;
    private Long centuriesPoint;
    private Long wicketPoint;
    private Long maidensPoint;
    private Long fiveWicketHaulPoint;
    private Long catchesPoint;
    private Long stumpingsPoint;

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

    public Long getRunsPoint() {
        return runsPoint;
    }

    public void setRunsPoint(Long runsPoint) {
        this.runsPoint = runsPoint;
    }

    public Long getFoursPoint() {
        return foursPoint;
    }

    public void setFoursPoint(Long foursPoint) {
        this.foursPoint = foursPoint;
    }

    public Long getSixesPoint() {
        return sixesPoint;
    }

    public void setSixesPoint(Long sixesPoint) {
        this.sixesPoint = sixesPoint;
    }

    public Long getFiftiesPoint() {
        return fiftiesPoint;
    }

    public void setFiftiesPoint(Long fiftiesPoint) {
        this.fiftiesPoint = fiftiesPoint;
    }

    public Long getCenturiesPoint() {
        return centuriesPoint;
    }

    public void setCenturiesPoint(Long centuriesPoint) {
        this.centuriesPoint = centuriesPoint;
    }

    public Long getWicketPoint() {
        return wicketPoint;
    }

    public void setWicketPoint(Long wicketPoint) {
        this.wicketPoint = wicketPoint;
    }

    public Long getMaidensPoint() {
        return maidensPoint;
    }

    public void setMaidensPoint(Long maidensPoint) {
        this.maidensPoint = maidensPoint;
    }

    public Long getFiveWicketHaulPoint() {
        return fiveWicketHaulPoint;
    }

    public void setFiveWicketHaulPoint(Long fiveWicketHaulPoint) {
        this.fiveWicketHaulPoint = fiveWicketHaulPoint;
    }

    public Long getCatchesPoint() {
        return catchesPoint;
    }

    public void setCatchesPoint(Long catchesPoint) {
        this.catchesPoint = catchesPoint;
    }

    public Long getStumpingsPoint() {
        return stumpingsPoint;
    }

    public void setStumpingsPoint(Long stumpingsPoint) {
        this.stumpingsPoint = stumpingsPoint;
    }
}