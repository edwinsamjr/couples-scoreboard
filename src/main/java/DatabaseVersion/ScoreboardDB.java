package DatabaseVersion;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.Scanner;


public class ScoreboardDB {

    private final JdbcTemplate jdbcTemplate = new JdbcTemplate(getDataSource());

    Scanner scanner = new Scanner(System.in);
    Game newGame = new Game();


    public void displayLookUpOptions(Object[] options) {
        for (int i = 0; i < options.length; i++) {
            int optionNum = i + 1;
            System.out.println("[" + optionNum + "] " + options[i]);
        }
        System.out.print(System.lineSeparator() + "Please choose an option >>> ");
        System.out.flush();
    }

    private Object getChoiceFromUserInput(Object[] options) {
        Object choice = null;
        String userInput = scanner.nextLine();
        try {
            int selectedOption = Integer.valueOf(userInput);
            if (selectedOption > 0 && selectedOption <= options.length) {
                choice = options[selectedOption - 1];
            }
        } catch (NumberFormatException e) {
            // eat the exception, an error message will be displayed below since choice will be null
        }
        if (choice == null) {
            System.out.println(System.lineSeparator() + "*** " + userInput + " is not a valid option ***" + System.lineSeparator());
        }
        return choice;
    }

    public Object getChoiceFromOptions(Object[] options, String search) {
        System.out.println();
        System.out.println(search);
        Object choice = null;
        while (choice == null) {
            displayLookUpOptions(options);
            choice = getChoiceFromUserInput(options);
        }
        return choice;
    }

    public void printGames(String game, String timeframe) {
        //TODO - Create printGames method
    }

    public void printScoreOverTimeframe(String game, String timeframe) {
        //TODO - Create printScoreOverTimeFrame method
    }


    public void enterNewGame(Object[] options) {
        //Receive game choice from user
        String gameName = (String) getChoiceFromOptions(options, "Games");

        //Receive date from user and convert to LocalDate
        String dateString = getDateFromUser();
        String[] userInput = splitUserInputDate(dateString);
        int[] dateInts = convertStringDateToInts(userInput);
        LocalDate date = getDateFromInts(dateInts);

        //Receive Rachel score from user and store it in variable "rachelScore"
        String rachelScoreString = getRachelScoreFromUser();
        int rachelScore = convertRachelScoreToInt(rachelScoreString);

        //Receive Edwin score from user and store it in variable "rachelScore"
        String edwinScoreString = getEdwinScoreFromUser();
        int edwinScore = convertEdwinScoreToInt(edwinScoreString);

        //Store game details into instance of Game
        newGame.setName(gameName);
        newGame.setDate(date);
        newGame.setRachelScore(rachelScore);
        newGame.setEdwinScore(edwinScore);

        //Add game into DB
        addNewGame(newGame);

        //Congratulate Winner
        newGame.congratulateWinner();

        try (PrintWriter logFile = new PrintWriter(new FileWriter("game-log.txt", true))) {
            logFile.printf("%s|%s|%s|%d|%d%n", newGame.getDate(), newGame.getName(),
                    newGame.getWinnerName(), newGame.getRachelScore(), newGame.getEdwinScore());

        } catch (FileNotFoundException e) {
            System.out.println("Log File not found");
        } catch (IOException e) {
            System.out.println("Log File not found");
        }

    }

    public String getDateFromUser() {

        System.out.println();
        System.out.print("Please enter date of game (MM-DD-YYYY) >>> ");
        String userInput = scanner.nextLine();

        return userInput;

    }

    public String[] splitUserInputDate(String userInput) {
        String[] dateNums = userInput.split("-");
        return dateNums;
    }

    public int[] convertStringDateToInts(String[] dateNums) {
        int[] dateInts = new int[3];
        for (int i = 0; i < dateNums.length; i++) {
            dateInts[i] = Integer.parseInt(dateNums[i]);
        }

        return dateInts;
    }

    public LocalDate getDateFromInts(int[] dateInts) {
        LocalDate date = LocalDate.of(dateInts[2], dateInts[0], dateInts[1]);
        return date;
    }


    public String getRachelScoreFromUser() {
        System.out.print("Please enter Rachel's score >>> ");
        String userInput = scanner.nextLine();

        return userInput;
    }

    public int convertRachelScoreToInt(String userInput) {
        Integer scoreInt = Integer.parseInt(userInput);
        return scoreInt;
    }


    public String getEdwinScoreFromUser() {
        System.out.print("Please enter Edwin's score >>> ");
        String userInput = scanner.nextLine();

        return userInput;
    }

    public int convertEdwinScoreToInt(String userInput) {
        Integer scoreInt = Integer.parseInt(userInput);
        return scoreInt;
    }

    private void addNewGame(Game game) {
        String sql = "INSERT INTO gamelog (date, game, winner, rachel_score, edwin_score)" +
                     " VALUES (?, ?, ?, ?, ?);";

        this.jdbcTemplate.queryForRowSet(sql,game.getDate(),game.getName(),
                game.getWinnerName(), game.getRachelScore(), game.getEdwinScore());


    }

    protected BasicDataSource getDataSource () {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setUrl("jdbc:postgresql://localhost:5432/ScoreboardDB");
        dataSource.setUsername("postgres");
        dataSource.setPassword("postgres1");

        return dataSource;
    }

}
