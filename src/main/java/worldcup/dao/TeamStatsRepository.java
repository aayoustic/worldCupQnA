package worldcup.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import worldcup.model.TeamStats;

import java.util.List;

public interface TeamStatsRepository extends CrudRepository<TeamStats, Long> {
    @Query(value = "SELECT p.ID, " +
            " COALESCE(SUM(ba.RUNS),0) AS Runs, " +
            " COALESCE(SUM(ba.FOURS),0) AS Fours, " +
            " COALESCE(SUM(ba.SIXES),0) AS Sixes," +
            " COALESCE(SUM(ba.FIFTY),0) AS Fifties, " +
            " COALESCE(SUM(ba.CENTURY),0) AS Century," +
            " COALESCE(SUM(bo.WICKETS),0) AS Wickets, " +
            " COALESCE(SUM(bo.MAIDENS),0) AS Maidens, " +
            " COALESCE(SUM(bo.FOUR_WICKET_HAUL),0) AS FourWicketHaul," +
            " COALESCE(SUM(bo.FIVE_WICKET_HAUL),0) AS FiveWicketHaul," +
            " COALESCE(SUM(fi.CATCHES),0) AS Catches, " +
            " COALESCE(SUM(fi.STUMPINGS),0) AS Stumpings" +
            " FROM participant p" +
            " LEFT JOIN team_stats ts ON p.ID = ts.PARTICIPANT_ID" +
            " LEFT JOIN player pl ON pl.ID = ts.PLAYER_ID" +
            " LEFT JOIN batting_stats ba ON ba.PLAYER_ID = ts.PLAYER_ID AND ba.MATCH_ID = ts.MATCH_ID" +
            " LEFT JOIN bowling_stats bo ON bo.PLAYER_ID = ts.PLAYER_ID AND bo.MATCH_ID = ts.MATCH_ID" +
            " LEFT JOIN fielding_stats fi ON fi.PLAYER_ID = ts.PLAYER_ID AND fi.MATCH_ID = ts.MATCH_ID" +
            " WHERE ts.MATCH_ID = ?1" +
            " GROUP BY p.ID" +
            " ORDER BY p.ID ASC", nativeQuery = true)
    List<Object[]> findTeamStats(Long matchId);
}
