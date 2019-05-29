package worldcup.model;

import com.fasterxml.jackson.annotation.JsonAlias;

import javax.persistence.*;

@Entity
public class ParticipantFieldingStats {

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
    @JoinColumn(name = "FIELDING_STATS_ID")
    private FieldingStats fieldingStats;

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

    public FieldingStats getFieldingStats() {
        return fieldingStats;
    }

    public void setFieldingStats(FieldingStats fieldingStats) {
        this.fieldingStats = fieldingStats;
    }
}