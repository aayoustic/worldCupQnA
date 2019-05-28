package worldcup.model;

import com.fasterxml.jackson.annotation.JsonAlias;

import javax.persistence.*;

@Entity
public class FieldingStats implements Stats{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "PLAYER_ID")
    private Player player;
    @ManyToOne
    @JoinColumn(name = "MATCH_ID")
    private Match match;
    @JsonAlias("catch")
    private int catches;
    @JsonAlias("stumped")
    private int stumpings;

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

    public int getCatches() {
        return catches;
    }

    public void setCatches(int catches) {
        this.catches = catches;
    }

    public int getStumpings() {
        return stumpings;
    }

    public void setStumpings(int stumpings) {
        this.stumpings = stumpings;
    }
}
