package DatabaseVersion;

import javax.sql.DataSource;


import org.apache.commons.dbcp2.BasicDataSource;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.Scanner;

public class CLI_DB {

    private static final String USERNAME = System.getenv("USERNAME");
    private static final String PASSWORD = System.getenv("PASSWORD");

    private static final String MAIN_MENU_OPTION_ENTER_SCORE = "Enter Score";
    private static final String MAIN_MENU_OPTION_SEARCH_POINTS = "Search Points";
    private static final String MAIN_MENU_OPTION_LOOK_UP_SCORES = "Search Scores";
    private static final String MAIN_MENU_OPTION_EXIT = "Exit";
    private static final String[] MAIN_MENU_OPTIONS = {MAIN_MENU_OPTION_LOOK_UP_SCORES, MAIN_MENU_OPTION_SEARCH_POINTS, MAIN_MENU_OPTION_ENTER_SCORE, MAIN_MENU_OPTION_EXIT};
    private static final String SEARCH_ALL_TIME = "All Time";
    private static final String SEARCH_THIS_MONTH = "This Month";
    private static final String SEARCH_BY_DATE = "Enter Starting/Ending Dates";

    private Menu menu;
    private final GameDAO gameDAO;
    Scanner scanner = new Scanner(System.in);
    Game newGame = new Game();


    public static void main(String[] args) {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setUrl("jdbc:postgresql://localhost:5432/ScoreboardDB");
        dataSource.setUsername(USERNAME);
        dataSource.setPassword(PASSWORD);

        CLI_DB cli = new CLI_DB(dataSource);
        cli.run();
    }

    public CLI_DB(DataSource dataSource) {
        this.menu = new Menu(System.in, System.out);;
        gameDAO = new JdbcGameDAO(dataSource);
    }


    public void run() {
        System.out.println();
        System.out.println("Welcome to Rachel and Edwin's Scoreboard App!");

        while (true) {
            System.out.println();

            String choice = (String) menu.getChoiceFromOptions(MAIN_MENU_OPTIONS);

            if (choice.equals(MAIN_MENU_OPTION_LOOK_UP_SCORES)) {
                //Search scores
//                String game = (String) getChoiceFromOptions(GameLookUp.gameOptionsOverall, "Games: ");
//                String timeframe = (String) getChoiceFromOptions(GameLookUp.timeFrameOptions, "Time Frame: ");
//
//                printGames(game, timeframe);

                searchGames(GameLookUp.gameOptionsOverall);
            } else if (choice.equals(MAIN_MENU_OPTION_ENTER_SCORE)) {
                //Get game from user
                enterNewGame(GameLookUp.gameOptions);
            } else if (choice.equals(MAIN_MENU_OPTION_SEARCH_POINTS)) {
              //Get points based on time frame

            } else if (choice.equals(MAIN_MENU_OPTION_EXIT)) {
                break;
            }
        }

    }




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

    public void printGames(String gameName, String timeframe) {
        //TODO - Create printGames method

        if (gameName.equals("Overall") && timeframe.equals(SEARCH_ALL_TIME)) {
            gameDAO.getAllGamesAllTime();
        } else if (gameName.equals("Overall") && timeframe.equals(SEARCH_THIS_MONTH)) {
            gameDAO.getAllGamesThisMonth();
        } else if (gameName.equals("Overall") && timeframe.equals(SEARCH_BY_DATE)){
            LocalDate startDate = getStartDateFromUser();
            LocalDate endDate = getEndDateFromUser();

            gameDAO.getAllGamesByDate(startDate, endDate);
        } else if (timeframe.equals(SEARCH_ALL_TIME)) {
            gameDAO.getSpecificGameAllTime(gameName);
        } else if (timeframe.equals(SEARCH_THIS_MONTH)) {
            gameDAO.getSpecificGameThisMonth(gameName);
        } else if (timeframe.equals(SEARCH_BY_DATE)) {

            LocalDate startDate = getStartDateFromUser();
            LocalDate endDate = getEndDateFromUser();

            gameDAO.getSpecificGameByDate(gameName, startDate, endDate);
        }
    }

    public void printScoreOverTimeframe(String game, String timeframe) {
        //TODO - Create printScoreOverTimeFrame method
    }

    public void searchGames(Object[] options) {
        //Receive game choice from user
        String gameName = (String) getChoiceFromOptions(options, "Games");

        //Receive time frame from user
        String timeFrame = (String) getChoiceFromOptions(GameLookUp.timeFrameOptions, "Time Frame:");

        //Print requested games to user
        printGames(gameName, timeFrame);
    }

    public void enterNewGame(Object[] options) {
        //Receive game choice from user
        String gameName = (String) getChoiceFromOptions(options, "Games");

        //Receive game date from user and store it in variable "date"
        LocalDate date = getGameDateFromUser();

        //Receive Rachel score from user and store it in variable "rachelScore"
        int rachelScore = getRachelScoreFromUser(gameName);

        //Receive Edwin score from user and store it in variable "rachelScore"
        int edwinScore = getEdwinScoreFromUser(gameName);

        //Store game details into instance of Game
        newGame.setName(gameName);
        newGame.setDate(date);
        newGame.setRachelScore(rachelScore);
        newGame.setEdwinScore(edwinScore);

        //Add game into DB
        gameDAO.addNewGame(newGame);

        //Congratulate Winner
        newGame.congratulateWinner();

    }

    public LocalDate getGameDateFromUser() {
        boolean isValidDate = false;
        LocalDate date = null;

        while (isValidDate == false) {
            try {
                String dateString = receiveUserInputDate();
                String[] userInput = splitUserInputDate(dateString);
                int[] dateInts = convertStringDateToInts(userInput);
                date = getDateFromInts(dateInts);
                isValidDate = true;


            } catch (DateTimeException | NumberFormatException |ArrayIndexOutOfBoundsException e) {
                System.out.println("Invalid Date");
            }
        }

        return date;

    }

    public String receiveUserInputDate() {

            System.out.println();
            System.out.print("Please enter date of game (MM-DD-YYYY) >>> ");
            String userInput = scanner.nextLine();

            return userInput;

    }


    public String[] splitUserInputDate(String userInput) throws NumberFormatException {
        String[] dateNums = userInput.split("-");
        boolean isFourDigitYear = dateNums[2].length() == 4;
        if (!isFourDigitYear) {
            throw new NumberFormatException();
        }

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

    public int getRachelScoreFromUser(String name) {
        boolean isValidScore = false;
        int score = 0;

        while (!isValidScore) {
            try {
                String rachelScoreString = receiveRachelScoreFromUser();
                score = convertScoreToInt(rachelScoreString);
                validateScore(name, score);
                isValidScore = true;
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid Score");
            }
        }
        return score;
    }

    public String receiveRachelScoreFromUser() {
        System.out.println();
        System.out.print("Please enter Rachel's score >>> ");
        String userInput = scanner.nextLine();

        return userInput;
    }

    public int convertScoreToInt(String userInput) {
        Integer scoreInt = Integer.parseInt(userInput);
        return scoreInt;
    }

    /**
     *
     * @param name
     * @param score
     * @return
     * @throws IllegalArgumentException
     */
    public int validateScore(String name, int score) {
        boolean isScrabble = name.equals("Scrabble");
        boolean isWordle = name.equals("Wordle");
        boolean isQuordle = name.equals("Quordle");
        boolean isCupPong = name.equals("Cup Pong");
        boolean isWordHunt = name.equals("Word Hunt");
        boolean is8Ball = name.equals("8 Ball");

        boolean isValidWordleScore = (score > 0 && score < 7) || score == 9;
        boolean isValidScrabbleScore = score > 0;
        boolean isValidQuordleScore = (score > 10 && score < 31) || score == 35;
        boolean isValidCupPongScore = score > -1 && score < 11;
        boolean isValidWordHuntScore = score > 0;
        boolean isValid8BallScore = score > -1 && score < 8;


        if (isScrabble && isValidScrabbleScore){

        } else if (isWordle && isValidWordleScore) {

        } else if (isQuordle && isValidQuordleScore) {

        } else if (isCupPong && isValidCupPongScore) {

        } else if (isWordHunt && isValidWordHuntScore) {

        } else if (is8Ball && isValid8BallScore) {

        } else{
            throw new IllegalArgumentException();
        }

        return score;
    }

    public int getEdwinScoreFromUser(String name) {
        boolean isValidScore = false;
        int score = 0;

        while (isValidScore == false) {
            try {
                String edwinScoreString = receiveEdwinScoreFromUser();
                score = convertScoreToInt(edwinScoreString);
                validateScore(name, score);
                isValidScore = true;
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid Score");
            }
        }
        return score;
    }

    public String receiveEdwinScoreFromUser() {
        System.out.println();
        System.out.print("Please enter Edwin's score >>> ");
        String userInput = scanner.nextLine();

        return userInput;
    }

    public LocalDate getStartDateFromUser() {
        boolean isValidDate = false;
        LocalDate date = null;

        while (isValidDate == false) {
            try {
                String dateString = receiveSearchStartDateFromUser();
                String[] userInput = splitUserInputDate(dateString);
                int[] dateInts = convertStringDateToInts(userInput);
                date = getDateFromInts(dateInts);
                isValidDate = true;


            } catch (DateTimeException | NumberFormatException |ArrayIndexOutOfBoundsException e) {
                System.out.println("Invalid Date");
            }
        }

        return date;

    }

    public String receiveSearchStartDateFromUser() {
        System.out.println();
        System.out.print("Please enter start date (MM-DD-YYYY) >>> ");
        String userInput = scanner.nextLine();

        return userInput;
    }

    public LocalDate getEndDateFromUser() {
        boolean isValidDate = false;
        LocalDate date = null;

        while (isValidDate == false) {
            try {
                String dateString = receiveSearchEndDateFromUser();
                String[] userInput = splitUserInputDate(dateString);
                int[] dateInts = convertStringDateToInts(userInput);
                date = getDateFromInts(dateInts);
                isValidDate = true;


            } catch (DateTimeException | NumberFormatException |ArrayIndexOutOfBoundsException e) {
                System.out.println("Invalid Date");
            }
        }

        return date;

    }

    public String receiveSearchEndDateFromUser() {
        System.out.print("Please enter end date (MM-DD-YYYY) >>> ");
        String userInput = scanner.nextLine();

        return userInput;
    }








}
