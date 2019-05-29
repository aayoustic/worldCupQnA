package worldcup.model;

import javax.persistence.*;

@Entity
public class ParticipantBowlingStats {

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
    @JoinColumn(name = "BOWLING_STATS_ID")
    private BowlingStats bowlingStats;

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

    public BowlingStats getBowlingStats() {
        return bowlingStats;
    }

    public void setBowlingStats(BowlingStats bowlingStats) {
        this.bowlingStats = bowlingStats;
    }
}
