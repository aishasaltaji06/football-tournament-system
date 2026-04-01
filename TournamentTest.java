import java.util.Scanner;

public class TournamentTest {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        System.out.println("=====================================");
        System.out.println("      CREATE NEW TOURNAMENT");
        System.out.println("=====================================");

        System.out.print("Enter tournament name: ");
        String name = input.nextLine();

        System.out.print("Enter maximum number of teams: ");
        int maxTeams = input.nextInt();

        System.out.print("Enter maximum number of matches: ");
        int maxMatches = input.nextInt();

        System.out.print("Enter maximum number of stadiums: ");
        int maxStadiums = input.nextInt();
        input.nextLine();

        System.out.print("Enter start date (DD-MM-YYYY): ");
        String startDate = input.nextLine();

        Tournament tournament =
                new Tournament(name, maxTeams, maxMatches, maxStadiums, startDate);

        int mainChoice;

        do {
            printMainMenu();
            mainChoice = input.nextInt();
            input.nextLine();

            switch (mainChoice) {
                case 1:
                    manageStadiums(input, tournament);
                    break;
                case 2:
                    manageTeams(input, tournament);
                    break;
                case 3:
                    manageMembers(input, tournament);
                    break;
                case 4:
                    manageMatches(input, tournament);
                    break;
                case 5:
                    manageReports(input, tournament);
                    break;
                case 0:
                    System.out.println("Exiting system...");
                    break;
                default:
                    System.out.println("Invalid choice.");
            }

        } while (mainChoice != 0);
    }

    public static void printMainMenu() {
        System.out.println("\n=========== TOURNAMENT SYSTEM ===========");
        System.out.println("1. Manage Stadiums");
        System.out.println("2. Manage Teams");
        System.out.println("3. Manage Members");
        System.out.println("4. Manage Matches");
        System.out.println("5. Reports");
        System.out.println("0. Exit");
        System.out.println("=========================================");
        System.out.print("Enter choice: ");
    }

    public static void manageStadiums(Scanner input, Tournament tournament) {
        int choice;

        do {
            int used = tournament.getStadiumCount();
            int max = tournament.getStadiums().length;
            int remaining = max - used;

            System.out.println("\n+--------------------------------------------+");
            System.out.println("|              STADIUM MANAGEMENT            |");
            System.out.println("+--------------------------------------------+");
            System.out.printf("|  Stadium slots used : %-20s |\n", used + " / " + max);
            System.out.printf("|  Remaining slots    : %-20s |\n", remaining);
            if (used == max) {
                System.out.println("|  All stadium slots are currently filled.   |");
            }
            System.out.println("+--------------------------------------------+");
            System.out.println("|  1) Add Stadium                            |");
            System.out.println("|  2) Remove Stadium                         |");
            System.out.println("|  3) View Stadiums                          |");
            System.out.println("|  0) Back                                   |");
            System.out.println("+--------------------------------------------+");
            System.out.print("Enter choice: ");
            choice = input.nextInt();
            input.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("Enter stadium name: ");
                    String name = input.nextLine();

                    System.out.print("Enter city: ");
                    String city = input.nextLine();

                    System.out.print("Enter capacity: ");
                    int capacity = input.nextInt();
                    input.nextLine();

                    Stadium s = new Stadium(name, city, capacity);
                    if (tournament.addStadium(s)) {
                        System.out.println("Stadium added successfully.");
                    } else {
                        System.out.println("Can't add stadium.");
                    }
                    break;

                case 2:
                    System.out.print("Enter stadium name to remove: ");
                    String stadiumName = input.nextLine();

                    if (tournament.removeStadium(stadiumName)) {
                        System.out.println("Stadium removed successfully.");
                    } else {
                        System.out.println("Stadium not found.");
                    }
                    break;

                case 3:
                    Stadium[] stadiums = tournament.getStadiums();
                    int count = tournament.getStadiumCount();

                    if (count == 0) {
                        System.out.println("No stadiums found.");
                    } else {
                        for (int i = 0; i < count; i++) {
                            System.out.println(stadiums[i]);
                        }
                    }
                    break;

                case 0:
                    break;

                default:
                    System.out.println("Invalid choice.");
            }

        } while (choice != 0);
    }

    public static void manageTeams(Scanner input, Tournament tournament) {
        int choice;

        do {
            int used = tournament.getTeamCount();
            int max = tournament.getTeams().length;
            int remaining = max - used;

            System.out.println("\n+--------------------------------------------+");
            System.out.println("|               TEAM MANAGEMENT              |");
            System.out.println("+--------------------------------------------+");

            System.out.printf("|  Team slots used    : %-20s |\n", used + " / " + max);
            System.out.printf("|  Remaining slots    : %-20s |\n", remaining);

            if (used == 0) {
                System.out.println("|  No teams have been added yet.             |");
            }

            if (used == max) {
                System.out.println("|  All team slots are currently filled.      |");
            }

            System.out.println("+--------------------------------------------+");
            System.out.println("|  1) Add Team                               |");
            System.out.println("|  2) Remove Team                            |");
            System.out.println("|  3) Search Team                            |");
            System.out.println("|  4) List Teams                             |");
            System.out.println("|  0) Back                                   |");
            System.out.println("+--------------------------------------------+");
            System.out.print("Enter choice: ");
            choice = input.nextInt();
            input.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("Enter team code: ");
                    String code = input.nextLine();

                    System.out.print("Enter country name: ");
                    String countryName = input.nextLine();

                    Team team = new Team(code, countryName);
                    if (tournament.addTeam(team)) {
                        System.out.println("Team added successfully.");
                    } else {
                        System.out.println("Could not add team.");
                    }
                    break;

                case 2:
                    System.out.print("Enter team name to remove: ");
                    String removeName = input.nextLine();

                    if (tournament.removeTeam(removeName)) {
                        System.out.println("Team removed successfully.");
                    } else {
                        System.out.println("Team not found.");
                    }
                    break;

                case 3:
                    System.out.print("Enter team name to search: ");
                    String searchName = input.nextLine();

                    Team found = tournament.searchTeam(searchName);
                    if (found != null) {
                        System.out.println("Team found: " + found.getSummary());
                    } else {
                        System.out.println("Team not found.");
                    }
                    break;

                case 4:
                    if (tournament.getTeamCount() == 0) {
                        System.out.println("No teams found.");
                    } else {
                        tournament.printTeamsRecursive();
                    }
                    break;

                case 0:
                    break;

                default:
                    System.out.println("Invalid choice.");
            }

        } while (choice != 0);
    }

    public static void manageMembers(Scanner input, Tournament tournament) {
        Team team = null;

        while (team == null) {
            System.out.print("Enter team name (or 0 to go back): ");
            String teamName = input.nextLine();

            if (teamName.equals("0")) {
                return;
            }

            team = tournament.searchTeam(teamName);

            if (team == null) {
                System.out.println("Team not found. Available teams:");
                if (tournament.getTeamCount() == 0) {
                    System.out.println("No teams available.");
                } else {
                    tournament.printTeamsRecursive();
                }
            }
        }

        int choice;

        do {
            int used = team.getMemberCount();
            int max = team.getMaxMembers();
            int remaining = max - used;

            String coachName = "Not assigned";
            String goalkeeperAssigned = "No";

            for (int i = 0; i < team.getMemberCount(); i++) {
                Person p = team.getMember(i);

                if (p instanceof Coach) {
                    coachName = p.getFullName();
                }

                if (p instanceof Goalkeeper) {
                    goalkeeperAssigned = "Yes";
                }
            }

            System.out.println("\n+--------------------------------------------+");
            System.out.println("|             MEMBER MANAGEMENT              |");
            System.out.println("+--------------------------------------------+");
            System.out.printf("|  Team name            : %-18s|\n", team.getCountryName());
            System.out.printf("|  Members used         : %-18s|\n", used + " / " + max);
            System.out.printf("|  Remaining slots      : %-18s|\n", remaining);
            System.out.printf("|  Coach assigned       : %-18s|\n", coachName);
            System.out.printf("|  Goalkeeper assigned  : %-18s|\n", goalkeeperAssigned);

            if (used == 0) {
                System.out.println("|  This team has no members yet.             |");
            }

            if (used == max) {
                System.out.println("|  The team roster is currently full.        |");
            }

            System.out.println("+--------------------------------------------+");
            System.out.println("|  1) Add Player                             |");
            System.out.println("|  2) Add Goalkeeper                         |");
            System.out.println("|  3) Add Coach                              |");
            System.out.println("|  4) Remove Member                          |");
            System.out.println("|  5) Show Team Members                      |");
            System.out.println("|  0) Back                                   |");
            System.out.println("+--------------------------------------------+");
            System.out.print("Enter choice: ");

            choice = input.nextInt();
            input.nextLine();

            switch (choice) {
                case 1: {
                    System.out.print("Enter player full name: ");
                    String fullName = input.nextLine();

                    System.out.print("Enter national ID: ");
                    String nationalId = input.nextLine();

                    System.out.print("Enter age: ");
                    int age = input.nextInt();

                    System.out.print("Enter salary: ");
                    double salary = input.nextDouble();

                    System.out.print("Enter contract years: ");
                    int contractYears = input.nextInt();

                    System.out.print("Enter shirt number: ");
                    int shirtNumber = input.nextInt();
                    input.nextLine();

                    System.out.print("Enter position (DEF/MID/ATT): ");
                    String position = input.nextLine();

                    Player player = new Player(fullName, nationalId, age, salary,
                            contractYears, shirtNumber, position);

                    if (team.addMember(player)) {
                        System.out.println("Player added successfully.");
                    } else {
                        System.out.println("Could not add player.");
                    }
                    break;
                }

                case 2: {
                    System.out.print("Enter goalkeeper full name: ");
                    String fullName = input.nextLine();

                    System.out.print("Enter national ID: ");
                    String nationalId = input.nextLine();

                    System.out.print("Enter age: ");
                    int age = input.nextInt();

                    System.out.print("Enter salary: ");
                    double salary = input.nextDouble();

                    System.out.print("Enter contract years: ");
                    int contractYears = input.nextInt();

                    System.out.print("Enter shirt number: ");
                    int shirtNumber = input.nextInt();

                    System.out.print("Enter saves: ");
                    int saves = input.nextInt();
                    input.nextLine();

                    Goalkeeper goalkeeper = new Goalkeeper(fullName, nationalId, age,
                            salary, contractYears, shirtNumber, "GK", saves);

                    if (team.addMember(goalkeeper)) {
                        System.out.println("Goalkeeper added successfully.");
                    } else {
                        System.out.println("Could not add goalkeeper.");
                    }
                    break;
                }

                case 3: {
                    System.out.print("Enter coach full name: ");
                    String fullName = input.nextLine();

                    System.out.print("Enter national ID: ");
                    String nationalId = input.nextLine();

                    System.out.print("Enter age: ");
                    int age = input.nextInt();

                    System.out.print("Enter salary: ");
                    double salary = input.nextDouble();

                    System.out.print("Enter contract years: ");
                    int contractYears = input.nextInt();

                    System.out.print("Enter license level: ");
                    int licenseLevel = input.nextInt();
                    input.nextLine();

                    Coach coach = new Coach(fullName, nationalId, age, salary,
                            contractYears, licenseLevel);

                    if (team.addMember(coach)) {
                        System.out.println("Coach added successfully.");
                    } else {
                        System.out.println("Could not add coach.");
                    }
                    break;
                }

                case 4: {
                    System.out.print("Enter member national ID to remove: ");
                    String memberId = input.nextLine();

                    if (team.removeMember(memberId)) {
                        System.out.println("Member removed successfully.");
                    } else {
                        System.out.println("Member not found.");
                    }
                    break;
                }

                case 5:
                    team.printMembersWithRoles();
                    break;

                case 0:
                    break;

                default:
                    System.out.println("Invalid choice.");
            }

        } while (choice != 0);
    }

    public static void manageMatches(Scanner input, Tournament tournament) {
        int choice;

        do {
            int used = tournament.getMatchCount();
            int max = tournament.getMatches().length;
            int remaining = max - used;

            int played = 0;
            int upcoming = 0;

            for (int i = 0; i < tournament.getMatchCount(); i++) {
                Match m = tournament.searchMatchByIndex(i);

                if (m != null) {
                    if (m.isFinished()) {
                        played++;
                    } else {
                        upcoming++;
                    }
                }
            }

            System.out.println("\n+--------------------------------------------+");
            System.out.println("|              MATCH MANAGEMENT              |");
            System.out.println("+--------------------------------------------+");
            System.out.printf("|  Match slots used   : %-20s |\n", used + " / " + max);
            System.out.printf("|  Remaining slots    : %-20s |\n", remaining);
            System.out.printf("|  Matches played     : %-20s |\n", played);
            System.out.printf("|  Upcoming matches   : %-20s |\n", upcoming);

            if (used == 0) {
                System.out.println("|  No matches have been generated yet.       |");
            }

            if (used == max) {
                System.out.println("|  All match slots are currently filled.     |");
            }

            System.out.println("+--------------------------------------------+");
            System.out.println("|  1) Generate Matches                       |");
            System.out.println("|  2) Show Upcoming Matches                  |");
            System.out.println("|  3) Prepare Match Lineups                  |");
            System.out.println("|  4) Play Match Randomly                    |");
            System.out.println("|  5) Play Match Manually                    |");
            System.out.println("|  6) View Match Summary                     |");
            System.out.println("|  7) Clear Matches                          |");
            System.out.println("|  0) Back                                   |");
            System.out.println("+--------------------------------------------+");
            System.out.print("Enter choice: ");

            choice = input.nextInt();
            input.nextLine();

            switch (choice) {
                case 1:
                    tournament.generateAllMatchesOnce();
                    break;

                case 2:
                    tournament.printUpcomingMatches();
                    break;

                case 3: {
                    Match match = askForMatch(input, tournament);
                    if (match == null) {
                        break;
                    }
                    prepareMatchLineups(input, match);
                    break;
                }

                case 4: {
                    Match match = askForMatch(input, tournament);
                    if (match == null) {
                        break;
                    }

                    if (match.isFinished()) {
                        System.out.println("This match is already finished.");
                    } else {
                        match.playRandomMatch();
                        System.out.println("Match played randomly.");
                    }
                    break;
                }

                case 5: {
                    Match match = askForMatch(input, tournament);
                    if (match == null) {
                        break;
                    }

                    playMatchManually(input, match);
                    break;
                }

                case 6: {
                    Match match = askForMatch(input, tournament);
                    if (match == null) {
                        break;
                    }

                    System.out.println(match.getSummary());
                    break;
                }

                case 7:
                    tournament.clearMatches();
                    break;

                case 0:
                    break;

                default:
                    System.out.println("Invalid choice.");
            }

        } while (choice != 0);
    }

    public static void playMatchManually(Scanner input, Match match) {
        int manualChoice;

        do {
            System.out.println("\n+--------------------------------------------+");
            System.out.println("|           PLAY MATCH MANUALLY              |");
            System.out.println("+--------------------------------------------+");
            System.out.printf("|  Match            : %-22s|\n", match.getMatchName());
            System.out.printf("|  Home team        : %-22s|\n", match.getHome().getCountryName());
            System.out.printf("|  Away team        : %-22s|\n", match.getAway().getCountryName());
            System.out.printf("|  Current score    : %-22s|\n", match.getHomeGoals() + " - " + match.getAwayGoals());
            System.out.println("+--------------------------------------------+");
            System.out.println("|  1) Record Home Goal                       |");
            System.out.println("|  2) Record Away Goal                       |");
            System.out.println("|  3) Give Yellow Card                       |");
            System.out.println("|  4) Give Red Card                          |");
            System.out.println("|  5) Add Injury                             |");
            System.out.println("|  6) Make Substitution                      |");
            System.out.println("|  7) Finish Match                           |");
            System.out.println("|  0) Back                                   |");
            System.out.println("+--------------------------------------------+");
            System.out.print("Enter choice: ");

            manualChoice = input.nextInt();
            input.nextLine();

            switch (manualChoice) {
                case 1: {
                    Team home = match.getHome();

                    System.out.println("\nHome team players:");
                    home.printMembersWithRoles();

                    System.out.print("Enter home player national ID: ");
                    String id = input.nextLine();

                    Person p = home.searchMember(id);

                    if (p instanceof Player) {
                        Player scorer = (Player) p;
                        scorer.addGoal();
                        match.setHomeGoals(match.getHomeGoals() + 1);
                        System.out.println("Home goal recorded for " + scorer.getFullName() + ".");
                    } else {
                        System.out.println("Player not found.");
                    }
                    break;
                }

                case 2: {
                    Team away = match.getAway();

                    System.out.println("\nAway team players:");
                    away.printMembersWithRoles();

                    System.out.print("Enter away player national ID: ");
                    String id = input.nextLine();

                    Person p = away.searchMember(id);

                    if (p instanceof Player) {
                        Player scorer = (Player) p;
                        scorer.addGoal();
                        match.setAwayGoals(match.getAwayGoals() + 1);
                        System.out.println("Away goal recorded for " + scorer.getFullName() + ".");
                    } else {
                        System.out.println("Player not found.");
                    }
                    break;
                }

                case 3: {
                    System.out.print("Give yellow card to which team? (home/away): ");
                    String side = input.nextLine();

                    Team chosenTeam = null;

                    if (side.equalsIgnoreCase("home")) {
                        chosenTeam = match.getHome();
                    } else if (side.equalsIgnoreCase("away")) {
                        chosenTeam = match.getAway();
                    } else {
                        System.out.println("Invalid team choice.");
                        break;
                    }

                    chosenTeam.printMembersWithRoles();

                    System.out.print("Enter player national ID: ");
                    String id = input.nextLine();

                    Person p = chosenTeam.searchMember(id);

                    if (p instanceof Player) {
                        Player player = (Player) p;
                        boolean hadRedBefore = player.hasRedCard();

                        player.addYellowCard();

                        if (chosenTeam == match.getHome()) {
                            match.setHomeYellowCards(match.getHomeYellowCards() + 1);
                            if (!hadRedBefore && player.hasRedCard()) {
                                match.setHomeRedCards(match.getHomeRedCards() + 1);
                            }
                        } else {
                            match.setAwayYellowCards(match.getAwayYellowCards() + 1);
                            if (!hadRedBefore && player.hasRedCard()) {
                                match.setAwayRedCards(match.getAwayRedCards() + 1);
                            }
                        }

                        System.out.println("Yellow card given successfully.");
                    } else {
                        System.out.println("Player not found.");
                    }
                    break;
                }

                case 4: {
                    System.out.print("Give red card to which team? (home/away): ");
                    String side = input.nextLine();

                    Team chosenTeam = null;

                    if (side.equalsIgnoreCase("home")) {
                        chosenTeam = match.getHome();
                    } else if (side.equalsIgnoreCase("away")) {
                        chosenTeam = match.getAway();
                    } else {
                        System.out.println("Invalid team choice.");
                        break;
                    }

                    chosenTeam.printMembersWithRoles();

                    System.out.print("Enter player national ID: ");
                    String id = input.nextLine();

                    Person p = chosenTeam.searchMember(id);

                    if (p instanceof Player) {
                        Player player = (Player) p;
                        if (player.hasRedCard()) {
                            System.out.println("This player already has a red card.");
                        } else {
                            player.giveRedCard();

                            if (chosenTeam == match.getHome()) {
                                match.setHomeRedCards(match.getHomeRedCards() + 1);
                            } else {
                                match.setAwayRedCards(match.getAwayRedCards() + 1);
                            }

                            System.out.println("Red card given successfully.");
                        }
                    } else {
                        System.out.println("Player not found.");
                    }
                    break;
                }

                case 5: {
                    System.out.print("Add injury to which team? (home/away): ");
                    String side = input.nextLine();

                    Team chosenTeam = null;

                    if (side.equalsIgnoreCase("home")) {
                        chosenTeam = match.getHome();
                    } else if (side.equalsIgnoreCase("away")) {
                        chosenTeam = match.getAway();
                    } else {
                        System.out.println("Invalid team choice.");
                        break;
                    }

                    chosenTeam.printMembersWithRoles();

                    System.out.print("Enter player national ID: ");
                    String id = input.nextLine();

                    Person p = chosenTeam.searchMember(id);

                    if (p instanceof Player) {
                        Player player = (Player) p;
                        player.setInjured(true);
                        System.out.println("Injury recorded successfully.");
                    } else {
                        System.out.println("Player not found.");
                    }
                    break;
                }

                case 6: {
                    System.out.print("Make substitution for which team? (home/away): ");
                    String side = input.nextLine();

                    Team chosenTeam = null;
                    boolean isHome = false;

                    if (side.equalsIgnoreCase("home")) {
                        chosenTeam = match.getHome();
                        isHome = true;
                    } else if (side.equalsIgnoreCase("away")) {
                        chosenTeam = match.getAway();
                    } else {
                        System.out.println("Invalid team choice.");
                        break;
                    }

                    if (chosenTeam.getStartingCount() == 0 || chosenTeam.getBenchCount() == 0) {
                        System.out.println("This team must have a starting XI and bench first.");
                        break;
                    }

                    System.out.println("\nStarting XI players:");
                    for (int i = 0; i < chosenTeam.getStartingCount(); i++) {
                        Player p = chosenTeam.getStartingPlayer(i);
                        if (p != null) {
                            System.out.println("- " + p.getFullName() + " (" + p.getNationalId() + ")");
                        }
                    }

                    System.out.println("\nBench players:");
                    for (int i = 0; i < chosenTeam.getBenchCount(); i++) {
                        Player p = chosenTeam.getBenchPlayer(i);
                        if (p != null) {
                            System.out.println("- " + p.getFullName() + " (" + p.getNationalId() + ")");
                        }
                    }

                    System.out.print("Enter outgoing player national ID: ");
                    String outId = input.nextLine();

                    System.out.print("Enter incoming player national ID: ");
                    String inId = input.nextLine();

                    Person outPerson = chosenTeam.searchMember(outId);
                    Person inPerson = chosenTeam.searchMember(inId);

                    if (outPerson instanceof Player && inPerson instanceof Player) {
                        boolean done = chosenTeam.substitutePlayer((Player) outPerson, (Player) inPerson);

                        if (done) {
                            if (isHome) {
                                match.setHomeSubstitutions(match.getHomeSubstitutions() + 1);
                            } else {
                                match.setAwaySubstitutions(match.getAwaySubstitutions() + 1);
                            }
                            System.out.println("Substitution completed successfully.");
                        } else {
                            System.out.println("Could not complete substitution.");
                            System.out.println("Make sure outgoing player is in starting XI and incoming player is on bench.");
                        }
                    } else {
                        System.out.println("One or both players were not found.");
                    }
                    break;
                }

                case 7: {
                    if (match.isFinished()) {
                        System.out.println("This match is already finished.");
                        break;
                    }

                    int homeGoals = match.getHomeGoals();
                    int awayGoals = match.getAwayGoals();

                    Team home = match.getHome();
                    Team away = match.getAway();

                    home.updateStats(homeGoals, awayGoals);
                    away.updateStats(awayGoals, homeGoals);

                    match.setFinished(true);

                    System.out.println("Match finished successfully.");
                    System.out.println(home.getCountryName() + " " + homeGoals +
                                       " - " + awayGoals + " " +
                                       away.getCountryName());
                    break;
                }

                case 0:
                    break;

                default:
                    System.out.println("Invalid choice.");
            }

        } while (manualChoice != 0);
    }

    public static void prepareMatchLineups(Scanner input, Match match) {
        int choice;
        Team home = match.getHome();
        Team away = match.getAway();

        do {
            System.out.println("\n+--------------------------------------------+");
            System.out.println("|            PREPARE MATCH LINEUPS           |");
            System.out.println("+--------------------------------------------+");
            System.out.printf("|  Match        : %-26s|\n", match.getMatchName());
            System.out.printf("|  Home Team    : %-26s|\n", home.getCountryName());
            System.out.printf("|  Away Team    : %-26s|\n", away.getCountryName());
            System.out.println("+--------------------------------------------+");
            System.out.printf("|  Home XI      : %-26s|\n", home.getStartingCount() + " / 11");
            System.out.printf("|  Away XI      : %-26s|\n", away.getStartingCount() + " / 11");
            System.out.printf("|  Home Bench   : %-26s|\n", home.getBenchCount() + " / 15");
            System.out.printf("|  Away Bench   : %-26s|\n", away.getBenchCount() + " / 15");
            System.out.println("+--------------------------------------------+");
            System.out.println("|  1) Add Home Player to Starting XI         |");
            System.out.println("|  2) Add Home Player to Bench               |");
            System.out.println("|  3) Validate Home Lineup                   |");
            System.out.println("|  4) Add Away Player to Starting XI         |");
            System.out.println("|  5) Add Away Player to Bench               |");
            System.out.println("|  6) Validate Away Lineup                   |");
            System.out.println("|  7) Validate Both Lineups                  |");
            System.out.println("|  8) Auto Build Both Lineups                |");
            System.out.println("|  0) Back                                   |");
            System.out.println("+--------------------------------------------+");
            System.out.print("Enter choice: ");

            choice = input.nextInt();
            input.nextLine();

            switch (choice) {
                case 1: {
                    System.out.print("Enter home player ID: ");
                    String id = input.nextLine();
                    Person p = home.searchMember(id);

                    if (p instanceof Player) {
                        if (home.addToStartingXI((Player) p)) {
                            System.out.println("Player added to home starting XI.");
                        } else {
                            System.out.println("Could not add player.");
                        }
                    } else {
                        System.out.println("Player not found.");
                    }
                    break;
                }

                case 2: {
                    System.out.print("Enter home player ID: ");
                    String id = input.nextLine();
                    Person p = home.searchMember(id);

                    if (p instanceof Player) {
                        if (home.addToBench((Player) p)) {
                            System.out.println("Player added to home bench.");
                        } else {
                            System.out.println("Could not add player.");
                        }
                    } else {
                        System.out.println("Player not found.");
                    }
                    break;
                }

                case 3:
                    if (home.hasValidStartingXI()) {
                        System.out.println("Home lineup is valid.");
                    } else {
                        System.out.println("Home lineup is NOT valid.");
                    }
                    break;

                case 4: {
                    System.out.print("Enter away player ID: ");
                    String id = input.nextLine();
                    Person p = away.searchMember(id);

                    if (p instanceof Player) {
                        if (away.addToStartingXI((Player) p)) {
                            System.out.println("Player added to away starting XI.");
                        } else {
                            System.out.println("Could not add player.");
                        }
                    } else {
                        System.out.println("Player not found.");
                    }
                    break;
                }

                case 5: {
                    System.out.print("Enter away player ID: ");
                    String id = input.nextLine();
                    Person p = away.searchMember(id);

                    if (p instanceof Player) {
                        if (away.addToBench((Player) p)) {
                            System.out.println("Player added to away bench.");
                        } else {
                            System.out.println("Could not add player.");
                        }
                    } else {
                        System.out.println("Player not found.");
                    }
                    break;
                }

                case 6:
                    if (away.hasValidStartingXI()) {
                        System.out.println("Away lineup is valid.");
                    } else {
                        System.out.println("Away lineup is NOT valid.");
                    }
                    break;

                case 7:
                    if (home.hasValidStartingXI() && away.hasValidStartingXI()) {
                        System.out.println("Both lineups are valid.");
                    } else {
                        System.out.println("One or both lineups are invalid.");
                    }
                    break;

                case 8:
                    home.simulateLineup();
                    away.simulateLineup();
                    System.out.println("Both lineups were built automatically.");
                    break;

                case 0:
                    break;

                default:
                    System.out.println("Invalid choice.");
            }

        } while (choice != 0);
    }

    public static void manageReports(Scanner input, Tournament tournament) {
        int choice;

        do {
            System.out.println("\n+--------------------------------------------+");
            System.out.println("|                 REPORTS                    |");
            System.out.println("+--------------------------------------------+");
            System.out.println("|  1) Tournament Standings                   |");
            System.out.println("|  2) Tournament Winner                      |");
            System.out.println("|  3) Apply Goal Bonuses                     |");
            System.out.println("|  4) Top Scorers                            |");
            System.out.println("|  0) Back                                   |");
            System.out.println("+--------------------------------------------+");
            System.out.print("Enter choice: ");
            choice = input.nextInt();
            input.nextLine();

            switch (choice) {
                case 1:
                    tournament.printStandings();
                    break;

                case 2:
                    Team winner = tournament.getTournamentWinner();
                    if (winner != null) {
                        System.out.println("Tournament Winner: " + winner.getCountryName());
                    }
                    break;

                case 3:
                    tournament.applyGoalBonuses();
                    System.out.println("Goal bonuses applied.");
                    break;

                case 4:
                    tournament.printTopScorers();
                break;
                case 0:
                    break;

                default:
                    System.out.println("Invalid choice.");
            }

        } while (choice != 0);
    }

    public static Match askForMatch(Scanner input, Tournament tournament) {
        System.out.print("Enter match name: ");
        String matchName = input.nextLine();

        Match match = tournament.searchMatch(matchName);
        if (match == null) {
            System.out.println("Match not found.");
        }

        return match;
    }
}    