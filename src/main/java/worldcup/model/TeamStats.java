package worldcup.model;

import javax.persistence.*;

@Entity
public class TeamStats {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "PLAYER_ID")
    private Player player;
    @ManyToOne
    @JoinColumn(name = "MATCH_ID")
    private Match match;
    @ManyToOne
    @JoinColumn(name = "PARTICIPANT_ID")
    private Participant participant;

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

    public Match getMatch() {
        return match;
    }

    public void setMatch(Match match) {
        this.match = match;
    }

    public Participant getParticipant() {
        return participant;
    }

    public void setParticipant(Participant participant) {
        this.participant = participant;
    }
}
