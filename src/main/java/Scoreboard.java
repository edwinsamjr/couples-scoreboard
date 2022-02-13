import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;


public class Scoreboard {

    Scanner scanner = new Scanner(System.in);

    Menu menu = new Menu();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("mm/DD/yyyy");

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
        //Receive date from user and store it in variable "date"
        String game = (String) getChoiceFromOptions(options, "Games");
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

        //Congratulate Winner
        String winnerName = getWinnerName(game, rachelScore, edwinScore);
        congratulateWinner(game, winnerName, rachelScore, edwinScore);

        try (PrintWriter logFile = new PrintWriter(new FileWriter("game-log.txt", true))) {
            logFile.printf("%s|%s|%s|%d|%d%n", date, game, winnerName, rachelScore, edwinScore);

        } catch (FileNotFoundException e) {
            System.out.println("Log File not found");
        } catch (IOException e) {
            System.out.println("Log File not found");
        }

    }

    public String getDateFromUser() {

        System.out.println();
        System.out.print("Please enter date of game (M/DD/YYYY) >>> ");
        String userInput = scanner.nextLine();

        return userInput;

    }

    public String[] splitUserInputDate(String userInput) {
        String[] dateNums = userInput.split("/");
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


    public void congratulateWinner(String game, String winnerName, int rachelScore, int edwinScore) {

        boolean isScrabble = game.equals("Scrabble");
        boolean isWordle = game.equals("Wordle");
        boolean isCupPong = game.equals("Cup Pong");
        boolean isWordHunt = game.equals("Word Hunt");
        boolean is8Ball = game.equals("8 Ball");

        if (isScrabble || isWordHunt || isWordle) {

            if (winnerName.equals("Rachel")) {
                System.out.println();
                System.out.printf("Congratulations Rachel! You won %d to %d!", rachelScore, edwinScore);
            } else if (winnerName.equals("Edwin")) {
                System.out.println();
                System.out.printf("Congratulations Edwin! You won %d to %d!", edwinScore, rachelScore);
            } else {
                System.out.printf("Draw! The score was %d to %d!", rachelScore, edwinScore);
            }
        }

        if (isCupPong || is8Ball) {

            if (winnerName.equals("Rachel")) {
                System.out.println();
                System.out.printf("Congratulations Rachel! You won with %d remaining!", edwinScore);
            } else if (winnerName.equals("Edwin")) {
                System.out.println();
                System.out.printf("Congratulations Edwin! You won with %d remaining!", rachelScore);
            } else {
                System.out.printf("Draw! The score was %d to %d!", rachelScore, edwinScore);
            }


        }

    }

    public String getWinnerName(String game, int rachelStore, int edwinScore) {
        String winnerName = "Draw";

        boolean isScrabble = game.equals("Scrabble");
        boolean isWordle = game.equals("Wordle");
        boolean isCupPong = game.equals("Cup Pong");
        boolean isWordHunt = game.equals("Word Hunt");
        boolean is8Ball = game.equals("8 Ball");
        boolean rachelScoreHigher = rachelStore > edwinScore;
        boolean edwinScoreHigher = edwinScore > rachelStore;

        if (isScrabble || isWordHunt) {
            if (rachelScoreHigher) {
                winnerName = "Rachel";
            } else if (edwinScoreHigher) {
                winnerName = "Edwin";
            }
        }

        if (isWordle || isCupPong || is8Ball) {
            if (rachelScoreHigher) {
                winnerName = "Edwin";
            } else if (edwinScoreHigher) {
                winnerName = "Rachel";
            }
        }

        return winnerName;

    }


}
