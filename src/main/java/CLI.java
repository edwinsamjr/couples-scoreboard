import java.awt.font.GlyphMetrics;

public class CLI {


    private static final String MAIN_MENU_OPTION_LOOK_UP_SCORES = "Search Scores";
    private static final String MAIN_MENU_OPTION_ENTER_SCORE = "Enter Score";
    private static final String[] MAIN_MENU_OPTIONS = {MAIN_MENU_OPTION_LOOK_UP_SCORES, MAIN_MENU_OPTION_ENTER_SCORE};

    private Menu menu = new Menu();
    Scoreboard scoreboard = new Scoreboard();
    GameLookUp gameLookUp = new GameLookUp();

    public CLI(Menu menu) {
        this.menu = menu;
    }



    public static void main(String[] args) {
        Menu menu = new Menu(System.in, System.out);
        CLI cli = new CLI(menu);
        cli.run();
    }


    public void run() {
        System.out.println("Welcome to Rachel and Edwin's Scoreboard App!");
        System.out.println();

        while (true) {
            System.out.println();

            String choice = (String) menu.getChoiceFromOptions(MAIN_MENU_OPTIONS);

            if (choice.equals(MAIN_MENU_OPTION_LOOK_UP_SCORES)) {
                //Search scores
                String game = (String) scoreboard.getChoiceFromOptions(GameLookUp.gameOptions, "Games: ");
                String timeframe = (String) scoreboard.getChoiceFromOptions(GameLookUp.timeFrameOptions, "Time Frame: ");

                scoreboard.printGames(game, timeframe);
            } else if (choice.equals(MAIN_MENU_OPTION_ENTER_SCORE)) {
                //Get game from user
                scoreboard.enterNewGame(GameLookUp.gameOptions);


            }
        }

    }

}
