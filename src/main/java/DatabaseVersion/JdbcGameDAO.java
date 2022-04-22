package DatabaseVersion;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

public class JdbcGameDAO implements GameDAO {

    private final JdbcTemplate jdbcTemplate;

    public JdbcGameDAO(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public List<Game> getAllGames() {
        //Query database for all games
        String sql = "SELECT date, game, winner, rachel_score, edwin_score FROM gamelog ORDER BY date, game;";
        SqlRowSet result = this.jdbcTemplate.queryForRowSet(sql);

        //Add all games to list called "games"
        List<Game> games = new ArrayList<>();
        while (result.next()) {
            Game game = new Game();
            game.setName(result.getString("game"));
            game.setDate(result.getDate("date").toLocalDate());
            game.setRachelScore(result.getInt("rachel_score"));
            game.setEdwinScore(result.getInt("edwin_score"));

            games.add(game);
        }

        //return list of all games
        return games;
    }
    @Override
    public List<Game> filterGamesByName(List<Game> games, String name){
        List<Game> filteredGames = new ArrayList<>();
        for (Game game : games){
            if (game.getName().equals(name)) {
                filteredGames.add(game);
            }
        }

        return filteredGames;
    }

    @Override
    public List<Game> filterGamesByDate(List<Game> games, LocalDate startDate, LocalDate endDate){
        List<Game> filteredGames = new ArrayList<>();

        for (Game game : games) {
            boolean inTimeRange = game.getDate().isBefore(endDate.plusDays(1)) && game.getDate().isAfter(startDate.minusDays(1));

            if (inTimeRange) {
                filteredGames.add(game);
            }

        }

        return filteredGames;
    }


    @Override
    public void getAllGamesAllTime() {
        //Store all games from DB into list
        List<Game> games = getAllGames();

        //Print all games to user
        printGameDetails(games);

        //Calculate num of wins for each player and store in Map
        Map<String, Integer> record = calculateRecord(games);

        //Print record to user
        printRecord(record);

    }

    @Override
    public void getAllGamesThisMonth() {
        LocalDate firstOfMonth = LocalDate.now().with(TemporalAdjusters.firstDayOfMonth());
        LocalDate lastOfMonth = LocalDate.now().with(TemporalAdjusters.lastDayOfMonth());

        //Store all games from DB into list
        List<Game> games = getAllGames();

        //Filter games by date
        List<Game> filteredGames = filterGamesByDate(games, firstOfMonth, lastOfMonth);

        //Print all games to user
        printGameDetails(filteredGames);

        //Calculate num of wins for each player and store in Map
        Map<String, Integer> record = calculateRecord(filteredGames);

        //Print record to user
        printRecord(record);

    }

    @Override
    public void getAllGamesByDate(LocalDate startDate, LocalDate endDate) {
        //Store all games from DB into list
        List<Game> games = getAllGames();

        //Filter games by date
        List<Game> filteredGames = filterGamesByDate(games, startDate, endDate);

        //Print all games to user
        printGameDetails(filteredGames);

        //Calculate num of wins for each player and store in Map
        Map<String, Integer> record = calculateRecord(filteredGames);

        //Print record to user
        printRecord(record);
    }

    @Override
    public void getSpecificGameAllTime(String gameName) {
        //Store all games from DB into List
        List<Game> games = getAllGames();
        //Filter games by name
        List<Game> filteredGames = filterGamesByName(games, gameName);

        //Print filtered games to user
        printGameDetails(filteredGames);

        //Calculate num of wins for each player and store in Map
        Map<String, Integer> record = calculateRecord(filteredGames);
        Map<String, Integer> scores = calculateScore(filteredGames);

        //Print record to user
        printRecord(record);
        printScore(scores);

    }

    @Override
    public void getSpecificGameThisMonth(String gameName) {
        LocalDate firstOfMonth = LocalDate.now().with(TemporalAdjusters.firstDayOfMonth());
        LocalDate lastOfMonth = LocalDate.now().with(TemporalAdjusters.lastDayOfMonth());

        //Store all games from DB into List
        List<Game> games = getAllGames();
        //Filter games by name and date
        List<Game> specificGames = filterGamesByName(games, gameName);
        List<Game> specificGamesThisMonth = filterGamesByDate(specificGames, firstOfMonth, lastOfMonth);

        //Print filtered games to user
        printGameDetails(specificGamesThisMonth);

        //Calculate num of wins for each player and store in Map
        Map<String, Integer> record = calculateRecord(specificGamesThisMonth);
        Map<String, Integer> scores = calculateScore(specificGamesThisMonth);

        //Print record to user
        printRecord(record);
        printScore(scores);

    }

    @Override
    public void getSpecificGameByDate(String gameName,LocalDate startDate,  LocalDate endDate) {
        //Store all games from DB into List
        List<Game> games = getAllGames();
        //Filter games by name and date
        List<Game> specificGames = filterGamesByName(games, gameName);
        List<Game> specificGamesThisMonth = filterGamesByDate(specificGames, startDate, endDate);

        //Print filtered games to user
        printGameDetails(specificGamesThisMonth);

        //Calculate num of wins for each player and store in Map
        Map<String, Integer> record = calculateRecord(specificGamesThisMonth);
        Map<String, Integer> scores = calculateScore(specificGamesThisMonth);

        //Print record to user
        printRecord(record);
        printScore(scores);
    }

    @Override
    public void getPointsALlTime() {
        //TODO - Write points methods
    }

    @Override
    public void getPointsThisMonth() {
        //TODO - Write points methods
    }

    @Override
    public void getPointsByDate(LocalDate startDate, LocalDate endDate) {
        //TODO - Write points methods
    }

    @Override
    public void addNewGame(Game game) {
        String sql = "INSERT INTO gamelog (date, game, winner, rachel_score, edwin_score)" +
                " VALUES (?, ?, ?, ?, ?);";

        this.jdbcTemplate.update(sql,game.getDate(),game.getName(),
                game.getWinnerName(), game.getRachelScore(), game.getEdwinScore());


    }


    @Override
    public void printGameDetails(List<Game> games){
        System.out.println("   DATE    |   GAME   | WINNER | RACHEL | EDWIN |");
        System.out.println("-------------------------------------------------");
        for (Game game : games) {
            System.out.println(game.toString());
        }
    }

    @Override
    public Map<String, Integer> calculateRecord(List<Game> games) {
        Map<String, Integer> record = new HashMap<>();

        int rachelWins = 0;
        int edwinWins = 0;
        int draws = 0;

        for (Game game : games) {
            if (game.getWinnerName().equals("Rachel")) {
                rachelWins++;
            } else if (game.getWinnerName().equals("Edwin")) {
                edwinWins++;
            } else {
                draws++;
            }

        }

        record.put("Rachel", rachelWins);
        record.put("Edwin", edwinWins);
        record.put("Draw", draws);

        return record;

    }

    @Override
    public Map<String, Integer> calculateScore(List<Game> games) {
        Map<String, Integer> record = new HashMap<>();

        int rachelTotal = 0;
        int edwinTotal = 0;

        for (Game game : games) {
            rachelTotal += game.getRachelScore();
            edwinTotal += game.getEdwinScore();

        }

        record.put("Rachel", rachelTotal);
        record.put("Edwin", edwinTotal);

        return record;

    }


    @Override
    public void printScore(Map<String, Integer> scores) {
        int rachelTotal = scores.get("Rachel");
        int edwinTotal = scores.get("Edwin");

        System.out.println("The total score is");
        System.out.printf("Rachel: " + rachelTotal + " - Edwin: " + edwinTotal);

    }

    @Override
    public void printRecord(Map<String, Integer> record) {
        int numRachelWins = record.get("Rachel");
        int numEdwinWins = record.get("Edwin");
        int numDraws = record.get("Draw");

        boolean rachelMoreWins = numRachelWins > numEdwinWins;
        boolean edwinMoreWins = numEdwinWins > numRachelWins;

        System.out.println();
        if (rachelMoreWins) {
            System.out.printf("Rachel leads %d-%d-%d \n", numRachelWins, numEdwinWins, numDraws);
        } else if (edwinMoreWins) {
            System.out.printf("Edwin leads %d-%d-%d \n", numEdwinWins, numRachelWins, numDraws);
        } else {
            System.out.printf("The record is tied %d-%d-%d \n", numEdwinWins, numRachelWins, numDraws);

        }

    }



}
