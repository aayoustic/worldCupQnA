package worldcup.model;

public interface Stats {

    Long getId();

    void setId(Long id);

    Player getPlayer();

    void setPlayer(Player player);

    Match getMatch();

    void setMatch(Match match);
}
