package worldcup.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import worldcup.model.TeamStats;

import java.util.List;

public interface TeamStatsRepository extends CrudRepository<TeamStats, Long> {
    @Query(value = "SELECT p.ID, SUM(ba.RUNS) AS Runs, SUM(ba.FOURS) AS Fours, SUM(ba.SIXES) AS Sixes," +
            " SUM(ba.FIFTY) AS Fifties, SUM(ba.CENTURY) AS Century," +
            " SUM(bo.WICKETS) AS Wickets, SUM(bo.MAIDENS) AS Maidens, SUM(bo.FIVE_WICKET_HAUL) AS FiveWicketHaul," +
            " SUM(fi.CATCHES) AS Catches, SUM(fi.STUMPINGS) AS Stumpings" +
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
