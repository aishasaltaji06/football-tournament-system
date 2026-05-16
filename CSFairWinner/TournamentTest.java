import java.util.Scanner;
import java.util.InputMismatchException;
import java.io.IOException;

public class TournamentTest {

    static Scanner input = new Scanner(System.in);

    public static int readInt() {
        while (true) {
            try {
                int number = input.nextInt();
                input.nextLine();
                return number;
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter numbers only.");
                input.nextLine();
            }
        }
    }

    public static double readDouble() {
        while (true) {
            try {
                double value = input.nextDouble();
                input.nextLine();
                return value;
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid number.");
                input.nextLine();
            }
        }
    }

    public static void main(String[] args) {

        System.out.println("=====================================");
        System.out.println("      CREATE NEW TOURNAMENT");
        System.out.println("=====================================");

        System.out.println("Enter tournament name:");
        String tournamentName = input.nextLine();

        System.out.println("Enter start date (DD-MM-YYYY):");
        String startDate = input.nextLine();

        Tournament tournament = new Tournament(tournamentName, startDate);

        int mainMenuChoice;

        do {
            printMainMenu();
            mainMenuChoice = readInt();

            switch (mainMenuChoice) {
                case 1:
                    manageStadiums(tournament);
                    break;
                case 2:
                    manageTeams(tournament);
                    break;
                case 3:
                    manageMembers(tournament);
                    break;
                case 4:
                    manageMatches(tournament);
                    break;
                case 5:
                    manageReports(tournament);
                    break;
                case 0:
                    System.out.println("Exiting system...");
                    break;
                default:
                    System.out.println("Invalid choice.");
            }

        } while (mainMenuChoice != 0);
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
        System.out.println("Enter choice:");
    }

    public static void manageStadiums(Tournament tournament) {
        int stadiumMenuChoice;

        do {
            int stadiumCount = tournament.getStadiumCount();

            System.out.println("\n+--------------------------------------------+");
            System.out.println("|              STADIUM MANAGEMENT            |");
            System.out.println("+--------------------------------------------+");
            System.out.printf("|  Stored stadiums : %-24s|%n", stadiumCount);

            if (stadiumCount == 0) {
                System.out.println("|  No stadiums have been added yet.          |");
            }

            System.out.println("+--------------------------------------------+");
            System.out.println("|  1) Add Stadium                            |");
            System.out.println("|  2) Remove Stadium                         |");
            System.out.println("|  3) View Stadiums                          |");
            System.out.println("|  0) Back                                   |");
            System.out.println("+--------------------------------------------+");
            System.out.println("Enter choice:");

            stadiumMenuChoice = readInt();

            switch (stadiumMenuChoice) {
                case 1:
                    System.out.println("Enter stadium name:");
                    String stadiumName = input.nextLine();

                    System.out.println("Enter city:");
                    String cityName = input.nextLine();

                    System.out.println("Enter capacity:");
                    int stadiumCapacity = readInt();

                    try {
                        Stadium newStadium = new Stadium(stadiumName, cityName, stadiumCapacity);

                        if (tournament.addStadium(newStadium)) {
                            System.out.println("Stadium added successfully.");
                        } else {
                            System.out.println("Can't add stadium.");
                        }
                    } catch (InvalidCapacityException e) {
                        System.out.println(e.getMessage());
                    }
                    break;

                case 2:
                    System.out.println("Enter stadium name to remove:");
                    String stadiumToRemove = input.nextLine();

                    if (tournament.removeStadium(stadiumToRemove)) {
                        System.out.println("Stadium removed successfully.");
                    } else {
                        System.out.println("Stadium not found.");
                    }
                    break;

                case 3:
                    if (tournament.getStadiumCount() == 0) {
                        System.out.println("No stadiums found.");
                    } else {
                        for (int i = 0; i < tournament.getStadiumCount(); i++) {
                            Stadium currentStadium = (Stadium) tournament.getStadiums().getObject(i);
                            System.out.println(currentStadium);
                        }
                    }
                    break;

                case 0:
                    break;

                default:
                    System.out.println("Invalid choice.");
            }

        } while (stadiumMenuChoice != 0);
    }

    public static void manageTeams(Tournament tournament) {
        int teamMenuChoice;

        do {
            int totalTeams = tournament.getTeamCount();

            System.out.println("\n+--------------------------------------------+");
            System.out.println("|               TEAM MANAGEMENT              |");
            System.out.println("+--------------------------------------------+");
            System.out.printf("|  Teams stored       : %-20s |%n", totalTeams);

            if (totalTeams == 0) {
                System.out.println("|  No teams have been added yet.             |");
            }

            System.out.println("+--------------------------------------------+");
            System.out.println("|  1) Add Manual Team                        |");
            System.out.println("|  2) Add Existing Team From File            |");
            System.out.println("|  3) Remove Team                            |");
            System.out.println("|  4) Search Team                            |");
            System.out.println("|  5) List Teams                             |");
            System.out.println("|  0) Back                                   |");
            System.out.println("+--------------------------------------------+");
            System.out.println("Enter choice:");

            teamMenuChoice = readInt();

            switch (teamMenuChoice) {
                case 1:
                    System.out.println("Enter team code:");
                    String teamCode = input.nextLine();

                    System.out.println("Enter country name:");
                    String countryName = input.nextLine();

                    Team newTeam = new Team(teamCode, countryName);

                    if (tournament.addTeam(newTeam)) {
                        System.out.println("Team added successfully.");

                        try {
                            tournament.saveTeamToFile(newTeam);
                            System.out.println("Team file saved successfully.");
                        } catch (IOException e) {
                            System.out.println("Team was added, but file could not be saved.");
                            System.out.println(e.getMessage());
                        }
                    } else {
                        System.out.println("Could not add team.");
                    }
                    break;

                case 2:

                    tournament.printExistingTeamFiles();
                    
                    System.out.println("Enter existing team file name, for example RealMadrid.txt:");
                    String fileName = input.nextLine();

                    try {
                        Team existingTeam = tournament.loadExistingTeamFromFile(fileName);

                        if (tournament.addTeam(existingTeam)) {
                            System.out.println("Existing team loaded successfully.");
                            System.out.println("Team members were loaded from file.");

                            try {
                                tournament.saveTeamToFile(existingTeam);
                                System.out.println("Loaded team was also saved in savedTeams folder.");
                            } catch (IOException e) {
                                System.out.println("Team was loaded, but file could not be saved.");
                                System.out.println(e.getMessage());
                            }
                        } else {
                            System.out.println("Could not add team. It may already exist.");
                        }
                    } catch (IOException e) {
                        System.out.println("Could not load existing team from file.");
                        System.out.println(e.getMessage());
                    } catch (NumberFormatException e) {
                        System.out.println("File contains invalid number format.");
                    }
                    break;

                case 3:
                    System.out.println("Enter team name to remove:");
                    String teamToRemove = input.nextLine();

                    if (tournament.removeTeam(teamToRemove)) {
                        System.out.println("Team removed successfully.");
                    } else {
                        System.out.println("Team not found.");
                    }
                    break;

                case 4:
                    System.out.println("Enter team name to search:");
                    String teamToSearch = input.nextLine();

                    Team foundTeam = tournament.searchTeam(teamToSearch);

                    if (foundTeam != null) {
                        System.out.println("Team found: " + foundTeam.getSummary());
                    } else {
                        System.out.println("Team not found.");
                    }
                    break;

                case 5:
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

        } while (teamMenuChoice != 0);
    }

    public static void manageMembers(Tournament tournament) {
        Team selectedTeam = null;

        while (selectedTeam == null) {
            System.out.println("Enter team name (or 0 to go back):");
            String teamName = input.nextLine();

            if (teamName.equals("0")) {
                return;
            }

            selectedTeam = tournament.searchTeam(teamName);

            if (selectedTeam == null) {
                System.out.println("Team not found. Available teams:");

                if (tournament.getTeamCount() == 0) {
                    System.out.println("No teams available.");
                } else {
                    tournament.printTeamsRecursive();
                }
            }
        }

        int memberMenuChoice;

        do {
            int usedMembers = selectedTeam.getMemberCount();
            int maxMembers = selectedTeam.getMaxMembers();
            int remainingSlots = maxMembers - usedMembers;

            String assignedCoachName = "Not assigned";
            String hasGoalkeeper = "No";

            for (int i = 0; i < selectedTeam.getMemberCount(); i++) {
                Person currentMember = selectedTeam.getMember(i);

                if (currentMember instanceof Coach) {
                    assignedCoachName = currentMember.getFullName();
                }

                if (currentMember instanceof Goalkeeper) {
                    hasGoalkeeper = "Yes";
                }
            }

            System.out.println("\n+--------------------------------------------+");
            System.out.println("|             MEMBER MANAGEMENT              |");
            System.out.println("+--------------------------------------------+");
            System.out.printf("|  Team name            : %-18s|%n", selectedTeam.getCountryName());
            System.out.printf("|  Members used         : %-18s|%n", usedMembers + " / " + maxMembers);
            System.out.printf("|  Remaining slots      : %-18s|%n", remainingSlots);
            System.out.printf("|  Coach assigned       : %-18s|%n", assignedCoachName);
            System.out.printf("|  Goalkeeper assigned  : %-18s|%n", hasGoalkeeper);

            if (usedMembers == 0) {
                System.out.println("|  This team has no members yet.             |");
            }

            if (usedMembers == maxMembers) {
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
            System.out.println("Enter choice:");

            memberMenuChoice = readInt();

            switch (memberMenuChoice) {
                case 1: {
                    System.out.println("Enter player full name:");
                    String fullName = input.nextLine();

                    System.out.println("Enter national ID:");
                    String nationalId = input.nextLine();

                    System.out.println("Enter age:");
                    int age = readInt();

                    System.out.println("Enter salary:");
                    double salary = readDouble();

                    System.out.println("Enter contract years:");
                    int contractYearsLeft = readInt();

                    System.out.println("Enter shirt number:");
                    int shirtNumber = readInt();

                    System.out.println("Enter position (DEF/MID/ATT):");
                    String playerPosition = input.nextLine();

                    Player newPlayer = new Player(fullName, nationalId, age, salary,
                            contractYearsLeft, shirtNumber, playerPosition);

                    if (selectedTeam.addMember(newPlayer)) {
                        System.out.println("Player added successfully.");

                        try {
                            tournament.saveTeamToFile(selectedTeam);
                            System.out.println("Team file updated successfully.");
                        } catch (IOException e) {
                            System.out.println("Player was added, but team file could not be updated.");
                            System.out.println(e.getMessage());
                        }
                    } else {
                        System.out.println("Could not add player.");
                    }
                    break;
                }

                case 2: {
                    System.out.println("Enter goalkeeper full name:");
                    String fullName = input.nextLine();

                    System.out.println("Enter national ID:");
                    String nationalId = input.nextLine();

                    System.out.println("Enter age:");
                    int age = readInt();

                    System.out.println("Enter salary:");
                    double salary = readDouble();

                    System.out.println("Enter contract years:");
                    int contractYearsLeft = readInt();

                    System.out.println("Enter shirt number:");
                    int shirtNumber = readInt();

                    System.out.println("Enter saves:");
                    int saves = readInt();

                    Goalkeeper newGoalkeeper = new Goalkeeper(fullName, nationalId, age,
                            salary, contractYearsLeft, shirtNumber, "GK", saves);

                    if (selectedTeam.addMember(newGoalkeeper)) {
                        System.out.println("Goalkeeper added successfully.");

                        try {
                            tournament.saveTeamToFile(selectedTeam);
                            System.out.println("Team file updated successfully.");
                        } catch (IOException e) {
                            System.out.println("Goalkeeper was added, but team file could not be updated.");
                            System.out.println(e.getMessage());
                        }
                    } else {
                        System.out.println("Could not add goalkeeper.");
                    }
                    break;
                }

                case 3: {
                    System.out.println("Enter coach full name:");
                    String fullName = input.nextLine();

                    System.out.println("Enter national ID:");
                    String nationalId = input.nextLine();

                    System.out.println("Enter age:");
                    int age = readInt();

                    System.out.println("Enter salary:");
                    double salary = readDouble();

                    System.out.println("Enter contract years:");
                    int contractYearsLeft = readInt();

                    System.out.println("Enter license level:");
                    int licenseLevel = readInt();

                    Coach newCoach = new Coach(fullName, nationalId, age, salary,
                            contractYearsLeft, licenseLevel);

                    if (selectedTeam.addMember(newCoach)) {
                        System.out.println("Coach added successfully.");

                        try {
                            tournament.saveTeamToFile(selectedTeam);
                            System.out.println("Team file updated successfully.");
                        } catch (IOException e) {
                            System.out.println("Coach was added, but team file could not be updated.");
                            System.out.println(e.getMessage());
                        }
                    } else {
                        System.out.println("Could not add coach.");
                    }
                    break;
                }

                case 4: {
                    System.out.println("Enter member national ID to remove:");
                    String memberNationalId = input.nextLine();

                    if (selectedTeam.removeMember(memberNationalId)) {
                        System.out.println("Member removed successfully.");

                        try {
                            tournament.saveTeamToFile(selectedTeam);
                            System.out.println("Team file updated successfully.");
                        } catch (IOException e) {
                            System.out.println("Member was removed, but team file could not be updated.");
                            System.out.println(e.getMessage());
                        }
                    } else {
                        System.out.println("Member not found.");
                    }
                    break;
                }

                case 5:
                    selectedTeam.printMembersWithRoles();
                    break;

                case 0:
                    break;

                default:
                    System.out.println("Invalid choice.");
            }

        } while (memberMenuChoice != 0);
    }

    public static void manageMatches(Tournament tournament) {
        int matchMenuChoice;

        do {
            int totalMatches = tournament.getMatchCount();

            int playedMatches = 0;
            int upcomingMatches = 0;

            for (int i = 0; i < tournament.getMatchCount(); i++) {
                Match currentMatch = tournament.searchMatchByIndex(i);

                if (currentMatch != null) {
                    if (currentMatch.isFinished()) {
                        playedMatches++;
                    } else {
                        upcomingMatches++;
                    }
                }
            }

            System.out.println("\n+--------------------------------------------+");
            System.out.println("|              MATCH MANAGEMENT              |");
            System.out.println("+--------------------------------------------+");
            System.out.printf("|  Total matches      : %-20s |%n", totalMatches);
            System.out.printf("|  Matches played     : %-20s |%n", playedMatches);
            System.out.printf("|  Upcoming matches   : %-20s |%n", upcomingMatches);

            if (totalMatches == 0) {
                System.out.println("|  No matches have been generated yet.       |");
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
            System.out.println("Enter choice:");

            matchMenuChoice = readInt();

            switch (matchMenuChoice) {
                case 1:
                    try {
                        tournament.generateAllMatchesOnce();
                    } catch (InvalidDateFormatException e) {
                        System.out.println(e.getMessage());
                    }
                    break;

                case 2:
                    tournament.printUpcomingMatches();
                    break;

                case 3: {
                    Match selectedMatch = askForMatch(tournament);

                    if (selectedMatch == null) {
                        break;
                    }

                    prepareMatchLineups(selectedMatch);
                    break;
                }

                case 4: {
                    Match selectedMatch = askForMatch(tournament);

                    if (selectedMatch == null) {
                        break;
                    }

                    if (selectedMatch.isFinished()) {
                        System.out.println("This match is already finished.");
                    } else {
                        selectedMatch.getHome().simulateLineup();
                        selectedMatch.getAway().simulateLineup();
                        selectedMatch.playRandomMatch();
                        System.out.println("Match played randomly.");
                    }
                    break;
                }

                case 5: {
                    Match selectedMatch = askForMatch(tournament);

                    if (selectedMatch == null) {
                        break;
                    }

                    playMatchManually(selectedMatch);
                    break;
                }

                case 6: {
                    Match selectedMatch = askForMatch(tournament);

                    if (selectedMatch == null) {
                        break;
                    }

                    System.out.println(selectedMatch.getSummary());
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

        } while (matchMenuChoice != 0);
    }

    public static void playMatchManually(Match match) {
        int manualMatchChoice;

        do {
            System.out.println("\n+--------------------------------------------+");
            System.out.println("|           PLAY MATCH MANUALLY              |");
            System.out.println("+--------------------------------------------+");
            System.out.printf("|  Match            : %-22s|%n", match.getMatchName());
            System.out.printf("|  Home team        : %-22s|%n", match.getHome().getCountryName());
            System.out.printf("|  Away team        : %-22s|%n", match.getAway().getCountryName());
            System.out.printf("|  Current score    : %-22s|%n", match.getHomeGoals() + " - " + match.getAwayGoals());
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
            System.out.println("Enter choice:");

            manualMatchChoice = readInt();

            if (match.isFinished() && manualMatchChoice != 0) {
                System.out.println("This match is already finished.");
                continue;
            }

            switch (manualMatchChoice) {
                case 1: {
                    Team homeTeam = match.getHome();

                    System.out.println("\nHome team players:");
                    homeTeam.printMembersWithRoles();

                    System.out.println("Enter home player national ID:");
                    String playerNationalId = input.nextLine();

                    Person selectedPerson = homeTeam.searchMember(playerNationalId);

                    if (selectedPerson instanceof Player) {
                        Player goalScorer = (Player) selectedPerson;
                        goalScorer.addGoal();
                        match.setHomeGoals(match.getHomeGoals() + 1);
                        System.out.println("Home goal recorded for " + goalScorer.getFullName() + ".");
                    } else {
                        System.out.println("Player not found.");
                    }
                    break;
                }

                case 2: {
                    Team awayTeam = match.getAway();

                    System.out.println("\nAway team players:");
                    awayTeam.printMembersWithRoles();

                    System.out.println("Enter away player national ID:");
                    String playerNationalId = input.nextLine();

                    Person selectedPerson = awayTeam.searchMember(playerNationalId);

                    if (selectedPerson instanceof Player) {
                        Player goalScorer = (Player) selectedPerson;
                        goalScorer.addGoal();
                        match.setAwayGoals(match.getAwayGoals() + 1);
                        System.out.println("Away goal recorded for " + goalScorer.getFullName() + ".");
                    } else {
                        System.out.println("Player not found.");
                    }
                    break;
                }

                case 3: {
                    System.out.println("Give yellow card to which team? (home/away):");
                    String selectedSide = input.nextLine();

                    Team selectedTeam = null;

                    if (selectedSide.equalsIgnoreCase("home")) {
                        selectedTeam = match.getHome();
                    } else if (selectedSide.equalsIgnoreCase("away")) {
                        selectedTeam = match.getAway();
                    } else {
                        System.out.println("Invalid team choice.");
                        break;
                    }

                    selectedTeam.printMembersWithRoles();

                    System.out.println("Enter player national ID:");
                    String playerNationalId = input.nextLine();

                    Person selectedPerson = selectedTeam.searchMember(playerNationalId);

                    if (selectedPerson instanceof Player) {
                        Player selectedPlayer = (Player) selectedPerson;
                        boolean alreadyHadRedCard = selectedPlayer.hasRedCard();

                        selectedPlayer.addYellowCard();

                        if (selectedTeam == match.getHome()) {
                            match.setHomeYellowCards(match.getHomeYellowCards() + 1);

                            if (!alreadyHadRedCard && selectedPlayer.hasRedCard()) {
                                match.setHomeRedCards(match.getHomeRedCards() + 1);
                            }
                        } else {
                            match.setAwayYellowCards(match.getAwayYellowCards() + 1);

                            if (!alreadyHadRedCard && selectedPlayer.hasRedCard()) {
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
                    System.out.println("Give red card to which team? (home/away):");
                    String selectedSide = input.nextLine();

                    Team selectedTeam = null;

                    if (selectedSide.equalsIgnoreCase("home")) {
                        selectedTeam = match.getHome();
                    } else if (selectedSide.equalsIgnoreCase("away")) {
                        selectedTeam = match.getAway();
                    } else {
                        System.out.println("Invalid team choice.");
                        break;
                    }

                    selectedTeam.printMembersWithRoles();

                    System.out.println("Enter player national ID:");
                    String playerNationalId = input.nextLine();

                    Person selectedPerson = selectedTeam.searchMember(playerNationalId);

                    if (selectedPerson instanceof Player) {
                        Player selectedPlayer = (Player) selectedPerson;

                        if (selectedPlayer.hasRedCard()) {
                            System.out.println("This player already has a red card.");
                        } else {
                            selectedPlayer.giveRedCard();

                            if (selectedTeam == match.getHome()) {
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
                    System.out.println("Add injury to which team? (home/away):");
                    String selectedSide = input.nextLine();

                    Team selectedTeam = null;

                    if (selectedSide.equalsIgnoreCase("home")) {
                        selectedTeam = match.getHome();
                    } else if (selectedSide.equalsIgnoreCase("away")) {
                        selectedTeam = match.getAway();
                    } else {
                        System.out.println("Invalid team choice.");
                        break;
                    }

                    selectedTeam.printMembersWithRoles();

                    System.out.println("Enter player national ID:");
                    String playerNationalId = input.nextLine();

                    Person selectedPerson = selectedTeam.searchMember(playerNationalId);

                    if (selectedPerson instanceof Player) {
                        Player injuredPlayer = (Player) selectedPerson;
                        injuredPlayer.setInjured(true);
                        System.out.println("Injury recorded successfully.");
                    } else {
                        System.out.println("Player not found.");
                    }
                    break;
                }

                case 6: {
                    System.out.println("Make substitution for which team? (home/away):");
                    String selectedSide = input.nextLine();

                    Team selectedTeam = null;
                    boolean homeTeamSelected = false;

                    if (selectedSide.equalsIgnoreCase("home")) {
                        selectedTeam = match.getHome();
                        homeTeamSelected = true;
                    } else if (selectedSide.equalsIgnoreCase("away")) {
                        selectedTeam = match.getAway();
                    } else {
                        System.out.println("Invalid team choice.");
                        break;
                    }

                    if (selectedTeam.getStartingCount() == 0 || selectedTeam.getBenchCount() == 0) {
                        System.out.println("This team must have a starting XI and bench first.");
                        break;
                    }

                    System.out.println("\nStarting XI players:");
                    for (int i = 0; i < selectedTeam.getStartingCount(); i++) {
                        Player startingPlayer = selectedTeam.getStartingPlayer(i);

                        if (startingPlayer != null) {
                            System.out.println("- " + startingPlayer.getFullName() + " (" + startingPlayer.getNationalId() + ")");
                        }
                    }

                    System.out.println("\nBench players:");
                    for (int i = 0; i < selectedTeam.getBenchCount(); i++) {
                        Player benchPlayer = selectedTeam.getBenchPlayer(i);

                        if (benchPlayer != null) {
                            System.out.println("- " + benchPlayer.getFullName() + " (" + benchPlayer.getNationalId() + ")");
                        }
                    }

                    System.out.println("Enter outgoing player national ID:");
                    String outgoingPlayerId = input.nextLine();

                    System.out.println("Enter incoming player national ID:");
                    String incomingPlayerId = input.nextLine();

                    Person outgoingPerson = selectedTeam.searchMember(outgoingPlayerId);
                    Person incomingPerson = selectedTeam.searchMember(incomingPlayerId);

                    if (outgoingPerson instanceof Player && incomingPerson instanceof Player) {
                        boolean substitutionCompleted =
                                selectedTeam.substitutePlayer((Player) outgoingPerson, (Player) incomingPerson);

                        if (substitutionCompleted) {
                            if (homeTeamSelected) {
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

                    int finalHomeGoals = match.getHomeGoals();
                    int finalAwayGoals = match.getAwayGoals();

                    Team homeTeam = match.getHome();
                    Team awayTeam = match.getAway();

                    homeTeam.updateStats(finalHomeGoals, finalAwayGoals);
                    awayTeam.updateStats(finalAwayGoals, finalHomeGoals);

                    match.setFinished(true);

                    System.out.println("Match finished successfully.");
                    System.out.println(homeTeam.getCountryName() + " " + finalHomeGoals +
                            " - " + finalAwayGoals + " " + awayTeam.getCountryName());
                    break;
                }

                case 0:
                    break;

                default:
                    System.out.println("Invalid choice.");
            }

        } while (manualMatchChoice != 0);
    }

    public static void prepareMatchLineups(Match match) {
        int lineupMenuChoice;
        Team homeTeam = match.getHome();
        Team awayTeam = match.getAway();

        do {
            System.out.println("\n+--------------------------------------------+");
            System.out.println("|            PREPARE MATCH LINEUPS           |");
            System.out.println("+--------------------------------------------+");
            System.out.printf("|  Match        : %-26s|%n", match.getMatchName());
            System.out.printf("|  Home Team    : %-26s|%n", homeTeam.getCountryName());
            System.out.printf("|  Away Team    : %-26s|%n", awayTeam.getCountryName());
            System.out.println("+--------------------------------------------+");
            System.out.printf("|  Home XI      : %-26s|%n", homeTeam.getStartingCount() + " / 11");
            System.out.printf("|  Away XI      : %-26s|%n", awayTeam.getStartingCount() + " / 11");
            System.out.printf("|  Home Bench   : %-26s|%n", homeTeam.getBenchCount() + " / 15");
            System.out.printf("|  Away Bench   : %-26s|%n", awayTeam.getBenchCount() + " / 15");
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
            System.out.println("Enter choice:");

            lineupMenuChoice = readInt();

            switch (lineupMenuChoice) {
                case 1: {
                    System.out.println("Enter home player ID:");
                    String playerNationalId = input.nextLine();

                    Person selectedPerson = homeTeam.searchMember(playerNationalId);

                    if (selectedPerson instanceof Player) {
                        if (homeTeam.addToStartingXI((Player) selectedPerson)) {
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
                    System.out.println("Enter home player ID:");
                    String playerNationalId = input.nextLine();

                    Person selectedPerson = homeTeam.searchMember(playerNationalId);

                    if (selectedPerson instanceof Player) {
                        if (homeTeam.addToBench((Player) selectedPerson)) {
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
                    if (homeTeam.hasValidStartingXI()) {
                        System.out.println("Home lineup is valid.");
                    } else {
                        System.out.println("Home lineup is NOT valid.");
                    }
                    break;

                case 4: {
                    System.out.println("Enter away player ID:");
                    String playerNationalId = input.nextLine();

                    Person selectedPerson = awayTeam.searchMember(playerNationalId);

                    if (selectedPerson instanceof Player) {
                        if (awayTeam.addToStartingXI((Player) selectedPerson)) {
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
                    System.out.println("Enter away player ID:");
                    String playerNationalId = input.nextLine();

                    Person selectedPerson = awayTeam.searchMember(playerNationalId);

                    if (selectedPerson instanceof Player) {
                        if (awayTeam.addToBench((Player) selectedPerson)) {
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
                    if (awayTeam.hasValidStartingXI()) {
                        System.out.println("Away lineup is valid.");
                    } else {
                        System.out.println("Away lineup is NOT valid.");
                    }
                    break;

                case 7:
                    if (homeTeam.hasValidStartingXI() && awayTeam.hasValidStartingXI()) {
                        System.out.println("Both lineups are valid.");
                    } else {
                        System.out.println("One or both lineups are invalid.");
                    }
                    break;

                case 8:
                    homeTeam.simulateLineup();
                    awayTeam.simulateLineup();
                    System.out.println("Both lineups were built automatically.");
                    break;

                case 0:
                    break;

                default:
                    System.out.println("Invalid choice.");
            }

        } while (lineupMenuChoice != 0);
    }

    public static void manageReports(Tournament tournament) {
        int reportMenuChoice;

        do {
            System.out.println("\n+--------------------------------------------+");
            System.out.println("|                 REPORTS                    |");
            System.out.println("+--------------------------------------------+");
            System.out.println("|  1) Tournament Standings                   |");
            System.out.println("|  2) Tournament Winner                      |");
            System.out.println("|  3) Apply Goal Bonuses                     |");
            System.out.println("|  4) Top Scorers                            |");
            System.out.println("|  5) Save Results to File                   |");
            System.out.println("|  0) Back                                   |");
            System.out.println("+--------------------------------------------+");
            System.out.println("Enter choice:");

            reportMenuChoice = readInt();

            switch (reportMenuChoice) {
                case 1:
                    tournament.printStandings();
                    break;

                case 2:
                    Team winningTeam = tournament.getTournamentWinner();

                    if (winningTeam != null) {
                        System.out.println("Tournament Winner: " + winningTeam.getCountryName());
                    }
                    break;

                case 3:
                    tournament.applyGoalBonuses();
                    System.out.println("Goal bonuses applied.");
                    break;

                case 4:
                    tournament.printTopScorers();
                    break;

                case 5:
                    try {
                        tournament.saveResultsToFile();
                        System.out.println("Results saved to results/ folder.");
                    } catch (IOException e) {
                        System.out.println("Could not save results: " + e.getMessage());
                    }
                    break;

                case 0:
                    break;

                default:
                    System.out.println("Invalid choice.");
            }

        } while (reportMenuChoice != 0);
    }

    public static Match askForMatch(Tournament tournament) {
        System.out.println("Enter match name:");
        String matchName = input.nextLine();

        Match selectedMatch = tournament.searchMatch(matchName);

        if (selectedMatch == null) {
            System.out.println("Match not found.");
        }

        return selectedMatch;
    }
}