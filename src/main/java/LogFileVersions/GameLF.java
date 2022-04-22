package LogFileVersions;

import java.time.LocalDate;

public class GameLF {

    String name;
    int rachelScore;
    int edwinScore;
    LocalDate date;

    public GameLF() {
    }

    ;

    public GameLF(String name, LocalDate date, int rachelScore, int edwinScore) {
        this.name = name;
        this.date = date;
        this.rachelScore = rachelScore;
        this.edwinScore = edwinScore;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRachelScore() {
        return rachelScore;
    }

    public void setRachelScore(int rachelScore) {
        this.rachelScore = rachelScore;
    }

    public int getEdwinScore() {
        return edwinScore;
    }

    public void setEdwinScore(int edwinScore) {
        this.edwinScore = edwinScore;
    }

    public String getWinnerName() {
        String winnerName = "Draw";

        boolean isScrabble = this.name.equals("Scrabble");
        boolean isWordle = this.name.equals("Wordle");
        boolean isCupPong = this.name.equals("Cup Pong");
        boolean isWordHunt = this.name.equals("Word Hunt");
        boolean is8Ball = this.name.equals("8 Ball");
        boolean rachelScoreHigher = rachelScore > edwinScore;
        boolean edwinScoreHigher = edwinScore > rachelScore;

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

    public void congratulateWinner() {

        boolean isScrabble = name.equals("Scrabble");
        boolean isWordle = name.equals("Wordle");
        boolean isCupPong = name.equals("Cup Pong");
        boolean isWordHunt = name.equals("Word Hunt");
        boolean is8Ball = name.equals("8 Ball");

        if (isScrabble || isWordHunt || isWordle) {

            if (getWinnerName().equals("Rachel")) {
                System.out.println();
                System.out.printf("Congratulations Rachel! You won %d to %d!", rachelScore, edwinScore);
            } else if (getWinnerName().equals("Edwin")) {
                System.out.println();
                System.out.printf("Congratulations Edwin! You won %d to %d!", edwinScore, rachelScore);
            } else {
                System.out.printf("Draw! The score was %d to %d!", rachelScore, edwinScore);
            }
        }

        if (isCupPong || is8Ball) {

            if (getWinnerName().equals("Rachel")) {
                System.out.println();
                System.out.printf("Congratulations Rachel! You won with %d remaining!", edwinScore);
            } else if (getWinnerName().equals("Edwin")) {
                System.out.println();
                System.out.printf("Congratulations Edwin! You won with %d remaining!", rachelScore);
            } else {
                System.out.printf("Draw! The score was %d to %d!", rachelScore, edwinScore);
            }


        }

    }
}


