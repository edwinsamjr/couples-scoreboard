import DatabaseVersion.Game;
import DatabaseVersion.JdbcGameDAO;
import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class JdbcGameDAOTest {
    BasicDataSource dataSource = new BasicDataSource();
    JdbcGameDAO dao = new JdbcGameDAO(dataSource);

    private static final Game GAME_ONE = new Game("Scrabble", LocalDate.parse("2022-01-15"), 244, 306);
    private static final Game GAME_TWO = new Game("Wordle", LocalDate.parse("2022-02-15"), 3, 4);
    private static final Game GAME_THREE = new Game("Wordle", LocalDate.parse("2022-02-16"), 4, 5);
    private static final Game GAME_FOUR = new Game("Wordle", LocalDate.parse("2022-03-17"), 5,5);
    private static final Game GAME_FIVE = new Game ("Wordle", LocalDate.parse("2022-03-18"), 5, 4);

    List<Game> allGames = Arrays.asList(GAME_ONE, GAME_TWO, GAME_THREE, GAME_FOUR, GAME_FIVE);


    @Test
    public void calculates_record() {
        int expectedRachelWins = 2;
        int expectedEdwinWins = 2;
        int expectedDraws = 1;

        Map<String, Integer> record = dao.calculateRecord(allGames);

        int actualRachelWins = record.get("Rachel");
        int actualEdwinWins = record.get("Edwin");
        int actualDraws = record.get("Draw");

        Assert.assertEquals("Incorrect number of Rachel wins", expectedRachelWins, actualRachelWins);
        Assert.assertEquals("Incorrect number of Edwin wins", expectedEdwinWins, actualEdwinWins);
        Assert.assertEquals("Incorrect number of Draws", expectedDraws, actualDraws);

    }

    @Test
    public void filters_games_by_name() {
        int expectedSize = 4;
        List<Game> filteredGames = dao.filterGamesByName(allGames, "Wordle");

        Assert.assertEquals("Incorrect size of filtered list of games", expectedSize, filteredGames.size());
        assertGamesMatch(GAME_TWO, filteredGames.get(0));
        assertGamesMatch(GAME_FIVE, filteredGames.get(3));
    }

    @Test
    public void filters_games_by_date() {
        int expectedSize = 2;
        List<Game> filteredGames = dao.filterGamesByDate(allGames, LocalDate.parse("2022-02-15"), LocalDate.parse("2022-02-16"));

        Assert.assertEquals("Incorrect size of filtered list of games", expectedSize, filteredGames.size());
    }


    private void assertGamesMatch(Game game1, Game game2){
        Assert.assertEquals(game1.getName(), game2.getName());
        Assert.assertEquals(game1.getDate(), game2.getDate());
        Assert.assertEquals(game1.getRachelScore(), game2.getRachelScore());
        Assert.assertEquals(game1.getEdwinScore(), game2.getEdwinScore());
    }


}
