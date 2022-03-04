package CLI_Tests;

import DatabaseVersion.CLI_DB;
import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.Assert;
import org.junit.Test;

import java.sql.Array;
import java.time.LocalDate;

public class CLI_Test {
    BasicDataSource dataSource = new BasicDataSource();
    CLI_DB cli = new CLI_DB(dataSource);

    @Test
    public void splits_user_input_date_into_three_ints() {
        String userInput = "03-02-2022";
        String[] expectedArray = new String[] {"03", "02", "2022"};
        String[] actualArray = cli.splitUserInputDate(userInput);

        Assert.assertArrayEquals("Input date not split correctly", expectedArray, actualArray);
    }

    @Test (expected =NumberFormatException.class)
    public void throws_exception_if_not_four_digit_year() {
        String userInput = "03-02-22";
        String[] actualArray = cli.splitUserInputDate(userInput);
    }

    @Test
    public void converts_strings_from_date_into_ints() {
        String[] inputStrings = new String[] {"03", "02", "2022"};
        int[] expectedArray = new int[] {3, 2, 2022};
        int[] actualArray = cli.convertStringDateToInts(inputStrings);

        Assert.assertArrayEquals(expectedArray, actualArray);
    }

    @Test
    public void gets_date_from_int_array() {
        int[] dateInts = new int[] {3, 2, 2022};
        LocalDate expectedDate = LocalDate.parse("2022-03-02");
        LocalDate actualDate = cli.getDateFromInts(dateInts);

        assertDatesMatch(expectedDate, actualDate);
    }








    private void assertDatesMatch(LocalDate date1, LocalDate date2) {
        Assert.assertEquals(date1.getDayOfMonth(), date2.getDayOfMonth());
        Assert.assertEquals(date1.getMonthValue(), date2.getMonthValue());
        Assert.assertEquals(date1.getYear(), date2.getYear());
    }


}
