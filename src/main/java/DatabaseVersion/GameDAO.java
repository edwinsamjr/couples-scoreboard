package DatabaseVersion;

import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface GameDAO {

    List<Game> getAllGames();
    List<Game> filterGamesByName(List<Game> games, String name);
    List<Game> filterGamesByDate(List<Game> games, LocalDate startDate, LocalDate endDate);


    void getAllGamesAllTime();
    void getAllGamesThisMonth();
    void getAllGamesByDate(LocalDate startDate, LocalDate endDate);
    void getSpecificGameAllTime(String gameName);
    void getSpecificGameThisMonth(String gameName);
    void getSpecificGameByDate(String gameName, LocalDate startDate, LocalDate endDate);

    void getPointsALlTime();
    void getPointsThisMonth();
    void getPointsByDate(LocalDate startDate, LocalDate endDate);

    void addNewGame(Game game);

    void printGameDetails(List<Game> games);
    Map calculateRecord(List<Game> games);
    void printRecord(Map<String, Integer> record);

    Map calculateScore(List<Game> games);
    void printScore(Map<String, Integer> scores);


}
