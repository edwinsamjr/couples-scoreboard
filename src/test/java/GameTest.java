import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GameTest {

    private static final Game GAME_ONE = new Game("Scrabble", LocalDate.parse("2022-01-15"), 244, 306);
    private static final Game GAME_TWO = new Game("Wordle", LocalDate.parse("2022-02-15"), 3, 4);
    private static final Game GAME_THREE = new Game("Wordle", LocalDate.parse("2022-02-16"), 4, 5);
    private static final Game GAME_FOUR = new Game("Wordle", LocalDate.parse("2022-02-17"), 5,5);
    private static final Game GAME_FIVE = new Game ("Wordle", LocalDate.parse("2022-02-18"), 5, 4);

    List<Game> allGames = Arrays.asList(GAME_ONE, GAME_TWO, GAME_THREE, GAME_FOUR, GAME_FIVE);
    List<Game> januaryGames = Arrays.asList(GAME_ONE);
    List<Game> februaryGames = Arrays.asList(GAME_TWO, GAME_THREE, GAME_FOUR);


    @Test
    public void gets_correct_winner_name() {
        Assert.assertEquals("Incorrect winner name for low-score winner", "Rachel", GAME_TWO.getWinnerName());
        Assert.assertEquals("Incorrect winner name for high-score winner", "Edwin", GAME_ONE.getWinnerName());
        Assert.assertEquals("Incorrect winner name for tie game", "Draw", GAME_FOUR.getWinnerName());
    }



    private void assertGamesMatch(Game game1, Game game2){
        Assert.assertEquals(game1.getName(), game2.getName());
        Assert.assertEquals(game1.getDate(), game2.getDate());
        Assert.assertEquals(game1.getRachelScore(), game2.getRachelScore());
        Assert.assertEquals(game1.getEdwinScore(), game2.getEdwinScore());
    }

}
