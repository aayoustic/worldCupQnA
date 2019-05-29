package worldcup.model;

import javax.persistence.*;

@Entity
public class ParticipantBattingStats {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "PARTICIPANT_ID")
    private Participant participant;
    @ManyToOne
    @JoinColumn(name = "MATCH_ID")
    private Match match;
    @ManyToOne
    @JoinColumn(name = "BATTING_STATS_ID")
    private BattingStats battingStats;


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

    public BattingStats getBattingStats() {
        return battingStats;
    }

    public void setBattingStats(BattingStats battingStats) {
        this.battingStats = battingStats;
    }
}
