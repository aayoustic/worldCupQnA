package worldcup.cricketData;

import java.net.URL;

public interface CricketInfo<T> {

    T getMatchSummary(String matchId);

    T getSquadsDetail(String matchId);

    T getPlayerDetail(String playerId);

    T callAPI(URL url);
}
