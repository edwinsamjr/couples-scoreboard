package CLI_Tests;

import DatabaseVersion.CLI_DB;
import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.Assert;
import org.junit.Test;

public class ScoreValidationTests {
    BasicDataSource dataSource = new BasicDataSource();
    CLI_DB cli = new CLI_DB(dataSource);

    @Test
    public void returns_score_given_valid_score() {
        int scrabbleScore = cli.validateScore("Scrabble", 200);
        int wordleScore = cli.validateScore("Wordle", 3);
        int _8BallScore = cli.validateScore("8 Ball", 2);

        Assert.assertEquals("Did not return score given valid Scrabble score", 200, scrabbleScore);
        Assert.assertEquals("Did not return score given valid Wordle score", 3, wordleScore);
        Assert.assertEquals("Did not return score given valid 8 Ball score", 2, _8BallScore);
    }

    @Test (expected = IllegalArgumentException.class)
    public void throws_exception_given_invalid_scrabble_score_zero(){
        int fakeScore = cli.validateScore("Scrabble", 0);

    }

    @Test (expected = IllegalArgumentException.class)
    public void throws_exception_given_invalid_wordle_score_zero(){
        int fakeScore = cli.validateScore("Wordle", 0);

    }

    @Test (expected = IllegalArgumentException.class)
    public void throws_exception_given_invalid_wordle_score_seven(){
        int fakeScore = cli.validateScore("Wordle", 7);

    }

    @Test (expected = IllegalArgumentException.class)
    public void throws_exception_given_invalid_quordle_score_nine(){
        int fakeScore = cli.validateScore("Quordle", 9);

    }

    @Test (expected = IllegalArgumentException.class)
    public void throws_exception_given_invalid_quordle_score_thirty_one(){
        int fakeScore = cli.validateScore("Quordle", 31);

    }

    @Test (expected = IllegalArgumentException.class)
    public void throws_exception_given_invalid_cup_pong_score_negative_one(){
        int fakeScore = cli.validateScore("Cup Pong", -1);

    }

    @Test (expected = IllegalArgumentException.class)
    public void throws_exception_given_invalid_cup_pong_score_eleven(){
        int fakeScore = cli.validateScore("Cup Pong", 11);

    }

    @Test (expected = IllegalArgumentException.class)
    public void throws_exception_given_invalid_word_hunt_score_negative_one(){
        int fakeScore = cli.validateScore("Word Hunt", -1);

    }

    @Test (expected = IllegalArgumentException.class)
    public void throws_exception_given_invalid_8Ball_score_negative_one(){
        int fakeScore = cli.validateScore("8 Ball", -1);

    }

    @Test (expected = IllegalArgumentException.class)
    public void throws_exception_given_invalid_8Ball_score_8(){
        int fakeScore = cli.validateScore("8 Ball", 8);

    }

}
