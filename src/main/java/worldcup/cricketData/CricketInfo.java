package worldcup.cricketData;

public interface CricketInfo<T> {

    T getMatchSummary();

    T getSquadsDetail(String matchID);
}
