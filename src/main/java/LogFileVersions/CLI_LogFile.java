package LogFileVersions;

public class CLI_LogFile {


    private static final String MAIN_MENU_OPTION_LOOK_UP_SCORES = "Search Scores";
    private static final String MAIN_MENU_OPTION_ENTER_SCORE = "Enter Score";
    private static final String MAIN_MENU_OPTION_EXIT = "Exit";
    private static final String[] MAIN_MENU_OPTIONS = {MAIN_MENU_OPTION_LOOK_UP_SCORES, MAIN_MENU_OPTION_ENTER_SCORE, MAIN_MENU_OPTION_EXIT};

    private MenuLF menuLF = new MenuLF();
    ScoreboardLogFile scoreboard = new ScoreboardLogFile();

    public CLI_LogFile(MenuLF menuLF) {
        this.menuLF = menuLF;
    }



    public static void main(String[] args) {
        MenuLF menuLF = new MenuLF(System.in, System.out);
        CLI_LogFile cli = new CLI_LogFile(menuLF);
        cli.run();
    }


    public void run() {
        System.out.println();
        System.out.println("Welcome to Rachel and Edwin's Scoreboard App!");

        while (true) {
            System.out.println();

            String choice = (String) menuLF.getChoiceFromOptions(MAIN_MENU_OPTIONS);

            if (choice.equals(MAIN_MENU_OPTION_LOOK_UP_SCORES)) {
                //Search scores
                String game = (String) scoreboard.getChoiceFromOptions(GameLookUpLF.gameOptions, "Games: ");
                String timeframe = (String) scoreboard.getChoiceFromOptions(GameLookUpLF.timeFrameOptions, "Time Frame: ");

                scoreboard.printGames(game, timeframe);
            } else if (choice.equals(MAIN_MENU_OPTION_ENTER_SCORE)) {
                //Get game from user
                scoreboard.enterNewGame(GameLookUpLF.gameOptions);
            }else if (choice.equals(MAIN_MENU_OPTION_EXIT)) {
                break;
            }
        }

    }

}
