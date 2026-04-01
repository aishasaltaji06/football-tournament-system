import java.util.Random;

interface Summarizable {
    String getSummary();
}

public class Tournament {

    private String name;
    private Team[] teams;
    private int teamCount;
    private Match[] matches;
    private int matchCount = 0;
    private Stadium[] stadiums;
    private int stadiumCount = 0;
    private String startDate;
    private int GOAL_BONUS_THRESHOLD = 30;
    private int GOAL_BONUS_AMOUNT = 1000;

    public Tournament(String name, int maxTeams, int maxMatches, int maxStadiums, String startDate) {
        this.name = name;
        this.teams = new Team[maxTeams];
        this.teamCount = 0;
        this.matches = new Match[maxMatches];
        this.matchCount = 0;
        this.stadiums = new Stadium[maxStadiums];
        this.stadiumCount = 0;
        this.startDate = startDate;
    }

    public boolean addStadium(Stadium s) {
        if (stadiumCount < stadiums.length) {
            stadiums[stadiumCount++] = s;
            return true;
        } else {
            return false;
        }
    }

    public boolean removeStadium(String stadiumName) {
        for (int i = 0; i < stadiumCount; i++) {
            if (stadiums[i].getName().equals(stadiumName)) {
                for (int j = i; j < stadiumCount - 1; j++) {
                    stadiums[j] = stadiums[j + 1];
                }
                stadiums[--stadiumCount] = null;
                return true;
            }
        }
        return false;
    }

    public boolean addTeam(Team t) {
        if (teamCount < teams.length) {
            teams[teamCount++] = t;
            return true;
        } else {
            return false;
        }
    }

    public boolean removeTeam(String teamName) {
        for (int i = 0; i < teamCount; i++) {
            if (teams[i].getCountryName().equals(teamName)) {
                for (int j = i; j < teamCount - 1; j++) {
                    teams[j] = teams[j + 1];
                }
                teams[--teamCount] = null;
                return true;
            }
        }
        return false;
    }

    public Team searchTeam(String teamName) {
        for (int i = 0; i < teamCount; i++) {
            if (teams[i].getCountryName().equals(teamName)) {
                return teams[i];
            }
        }
        return null;
    }

    public boolean addMatch(Match m) {
        if (m == null) {
            return false;
        }

        if (m.getTournament() != this) {
            return false;
        }

        if (matchCount < matches.length) {
            matches[matchCount++] = m;
            return true;
        } else {
            return false;
        }
    }

    public boolean removeMatch(String matchId) {
        for (int i = 0; i < matchCount; i++) {
            if (matches[i].getMatchName().equals(matchId)) {
                for (int j = i; j < matchCount - 1; j++) {
                    matches[j] = matches[j + 1];
                }
                matches[--matchCount] = null;
                return true;
            }
        }
        return false;
    }

    public Match searchMatch(String matchId) {
        for (int i = 0; i < matchCount; i++) {
            if (matches[i].getMatchName().equals(matchId)) {
                return matches[i];
            }
        }
        return null;
    }

    public Match searchMatchByIndex(int index) {
        if (index >= 0 && index < matchCount) {
            return matches[index];
        }
        return null;
    }

    public void printTeamsRecursive() {
        printTeamsRecursive(0);
    }

    private void printTeamsRecursive(int index) {
        if (index < teamCount) {
            System.out.println(teams[index].getCountryName());
            printTeamsRecursive(index + 1);
        }
    }

    private void sortTeamsByStandings() {
        for (int i = 0; i < teamCount - 1; i++) {
            int bestIndex = i;

            for (int j = i + 1; j < teamCount; j++) {
                Team current = teams[j];
                Team best = teams[bestIndex];

                if (current == null) continue;
                if (best == null) {
                    bestIndex = j;
                    continue;
                }

                int currentPoints = current.getPoints();
                int bestPoints = best.getPoints();

                if (currentPoints > bestPoints) {
                    bestIndex = j;
                } else if (currentPoints == bestPoints) {
                    int currentGD = current.getGoalDifference();
                    int bestGD = best.getGoalDifference();

                    if (currentGD > bestGD) {
                        bestIndex = j;
                    } else if (currentGD == bestGD) {
                        int currentGF = current.getGoalsFor();
                        int bestGF = best.getGoalsFor();

                        if (currentGF > bestGF) {
                            bestIndex = j;
                        } else if (currentGF == bestGF) {
                            String currentName = current.getCountryName().toLowerCase();
                            String bestName = best.getCountryName().toLowerCase();
                            if (currentName.compareTo(bestName) < 0) {
                                bestIndex = j;
                            }
                        }
                    }
                }
            }

            if (bestIndex != i) {
                Team temp = teams[i];
                teams[i] = teams[bestIndex];
                teams[bestIndex] = temp;
            }
        }
    }

    public void printStandings() {
        sortTeamsByStandings();

        System.out.println("===============================================================");
        System.out.println("\t\t" + name.toUpperCase());
        System.out.println("\t\tTOURNAMENT STANDINGS");
        System.out.println("===============================================================");

        System.out.printf("| %-3s | %-15s | %-2s | %-2s | %-2s | %-2s | %-3s | %-3s | %-3s | %-3s |\n",
                "Pos", "Country", "MP", "W", "D", "L", "GF", "GA", "GD", "Pts");

        System.out.println("---------------------------------------------------------------");

        for (int i = 0; i < teamCount; i++) {
            Team t = teams[i];
            if (t == null) continue;

            System.out.printf("| %-3d | %-15s | %-2d | %-2d | %-2d | %-2d | %-3d | %-3d | %+3d | %-3d |\n",
                    (i + 1),
                    t.getCountryName(),
                    t.getPlayed(),
                    t.getWins(),
                    t.getDraws(),
                    t.getLosses(),
                    t.getGoalsFor(),
                    t.getGoalsAgainst(),
                    t.getGoalDifference(),
                    t.getPoints());
        }

        System.out.println("===============================================================");
    }

    public void applyGoalBonuses() {
        for (int i = 0; i < teamCount; i++) {
            Team team = teams[i];
            if (team == null) continue;

            for (int j = 0; j < team.getMemberCount(); j++) {
                Person person = team.getMember(j);

                if (person instanceof Player) {
                    Player player = (Player) person;

                    if (!player.isGoalBonusApplied() &&
                        player.getGoals() > GOAL_BONUS_THRESHOLD) {

                        double newSalary = player.getSalary() + GOAL_BONUS_AMOUNT;
                        player.setSalary(newSalary);
                        player.setGoalBonusApplied(true);
                    }
                }
            }
        }
    }

    public void generateAllMatchesOnce() {
        int matchIndex = 0;

        if (matchCount > 0) {
            System.out.println("Matches already generated. Clear first if you want to regenerate.");
            return;
        }

        if (teamCount < 2) {
            System.out.println("Not enough teams to generate matches.");
            return;
        }

        if (startDate == null || startDate.length() != 10 ||
            startDate.charAt(2) != '-' || startDate.charAt(5) != '-') {
            System.out.println("Invalid start date. Use format DD-MM-YYYY.");
            return;
        }
        for (int i = 0; i < startDate.length(); i++) {
            if (i == 2 || i == 5) continue;

            char c = startDate.charAt(i);

            if (!Character.isDigit(c)) {
                System.out.println("Date must contain only numbers.");
                return;
            }
        }

        int day = Integer.parseInt(startDate.substring(0, 2));
        int month = Integer.parseInt(startDate.substring(3, 5));
        int year = Integer.parseInt(startDate.substring(6, 10));


        int[] daysInMonth = {31,28,31,30,31,30,31,31,30,31,30,31};

        for (int i = 0; i < teamCount; i++) {
            for (int j = i + 1; j < teamCount; j++) {
                Team home = teams[i];
                Team away = teams[j];

                Stadium stadium = null;
                if (stadiumCount > 0) {
                    stadium = stadiums[matchIndex % stadiumCount];
                }

                String matchDate = "" + (day < 10 ? "0" : "") + day + "-" + (month < 10 ? "0" : "") + month + "-" + year;


                Match m = new Match(this, home, away, stadium, matchDate, "8:00 PM");

                if (!addMatch(m)) {
                    System.out.println("Matches array is full. Could not add more matches.");
                    return;
                }

                day += 2;
                int currentMonthDays = daysInMonth[month - 1];

                if (day > currentMonthDays) {
                    day -= currentMonthDays;
                    month++;
                    if (month > 12) {
                        month = 1;
                        year++;
                    }
                }

                matchIndex++;
            }
        }
    }

    public void clearMatches() {
        for (int i = 0; i < matchCount; i++) {
            matches[i] = null;
        }
        matchCount = 0;
        System.out.println("All matches cleared.");
    }

    public void printUpcomingMatches() {
        System.out.println("===============================================");
        System.out.println("\tUpcoming Matches in " + name);
        System.out.println("===============================================");

        boolean found = false;

        for (int i = 0; i < matchCount; i++) {
            Match m = matches[i];
            if (m == null) continue;

            if (!m.isFinished()) {
                found = true;

                System.out.println("-----------------------------------------------");
                System.out.println("Match Name : " + m.getMatchName());
                System.out.println("Home Team  : " + m.getHome().getCountryName());
                System.out.println("Away Team  : " + m.getAway().getCountryName());

                if (m.getStadium() != null) {
                    System.out.println("Stadium    : " + m.getStadium().getName());
                } else {
                    System.out.println("Stadium    : TBD");
                }

                if (m.getDate() != null) {
                    System.out.println("Date       : " + m.getDate());
                } else {
                    System.out.println("Date       : TBD");
                }

                if (m.getTime() != null) {
                    System.out.println("Time       : " + m.getTime());
                } else {
                    System.out.println("Time       : TBD");
                }
            }
        }

        if (!found) {
            System.out.println("No upcoming matches.");
        }

        System.out.println("===============================================");
    }

    public Team getTournamentWinner() {
        if (matchCount == 0) {
            System.out.println("No matches have been played yet.");
            return null;
        }

        for (int i = 0; i < matchCount; i++) {
            if (matches[i] != null && !matches[i].isFinished()) {
                System.out.println("Not all matches have been played yet.");
                return null;
            }
        }
        

        sortTeamsByStandings();

        if (teamCount == 0) {
            return null;
        }
        return teams[0];
    }
    public void printTopScorers() {
        int totalPlayers = 0;

        for (int i = 0; i < teamCount; i++) {
            for (int j = 0; j < teams[i].getMemberCount(); j++) {
                if (teams[i].getMember(j) instanceof Player) {
                    totalPlayers++;
                }
            }
        }

        if (totalPlayers == 0) {
            System.out.println("No players found.");
            return;
        }

        Player[] allPlayers = new Player[totalPlayers];
        Team[] playerTeams = new Team[totalPlayers];
        int index = 0;

        
        for (int i = 0; i < teamCount; i++) {
            for (int j = 0; j < teams[i].getMemberCount(); j++) {
                Person p = teams[i].getMember(j);
                if (p instanceof Player) {
                    allPlayers[index] = (Player) p;
                    playerTeams[index] = teams[i];
                    index++;
                }
            }
        }

        
        for (int i = 0; i < index - 1; i++) {
            int maxIndex = i;
            for (int j = i + 1; j < index; j++) {
                if (allPlayers[j].getGoals() > allPlayers[maxIndex].getGoals()) {
                    maxIndex = j;
                }
            }
            if (maxIndex != i) {
                Player tempPlayer = allPlayers[i];
                allPlayers[i] = allPlayers[maxIndex];
                allPlayers[maxIndex] = tempPlayer;

                Team tempTeam = playerTeams[i];
                playerTeams[i] = playerTeams[maxIndex];
                playerTeams[maxIndex] = tempTeam;
            }
        }

        System.out.println("==========================================================");
        System.out.println("\t\tTOP SCORERS");
        System.out.println("==========================================================");
        System.out.printf("| %4s | %-20s | %-5s | %-6s | %-5s |\n",
                "Rank", "Player", "Goals", "Team", "Pos");
        System.out.println("----------------------------------------------------------");

        int rank = 1;
        boolean found = false;

        for (int i = 0; i < index; i++) {
            Player p = allPlayers[i];
            if (p.getGoals() > 0) {
                System.out.printf("| %4d | %-20s | %5d | %-6s | %-5s |\n",
                        rank++,
                        p.getFullName(),
                        p.getGoals(),
                        playerTeams[i].getCode(),
                        p.getPosition());
                found = true;
            }
        }

        if (!found) {
            System.out.println("|            No players with goals found.                |");
        }

        System.out.println("==========================================================");
    }



    public String getName() {
        return name;
    }

    public Team[] getTeams() {
        return teams;
    }

    public int getTeamCount() {
        return teamCount;
    }

    public Match[] getMatches() {
        return matches;
    }

    public int getMatchCount() {
        return matchCount;
    }

    public Stadium[] getStadiums() {
        return stadiums;
    }

    public int getStadiumCount() {
        return stadiumCount;
    }

    public String getStartDate() {
        return startDate;
    }

    public int getGoalBonusThreshold() {
        return GOAL_BONUS_THRESHOLD;
    }

    public int getGoalBonusAmount() {
        return GOAL_BONUS_AMOUNT;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public void setGoalBonusAmount(int amount) {
        GOAL_BONUS_AMOUNT = amount;
    }

    public void setGoalBonusThreshold(int threshold) {
        GOAL_BONUS_THRESHOLD = threshold;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTeams(Team[] teams) {
        this.teams = teams;
    }

    public void setMatches(Match[] matches) {
        this.matches = matches;
    }

    public void setStadiums(Stadium[] stadiums) {
        this.stadiums = stadiums;
    }
}

class Match implements Summarizable {

    private Tournament tournament;
    private String matchName;
    private Team home;
    private Team away;
    private Stadium stadium;
    private int homeGoals;
    private int awayGoals;
    private String date;
    private String time;
    private boolean isFinished;
    private int homeYellowCards;
    private int awayYellowCards;
    private int homeRedCards;
    private int awayRedCards;
    private int homeSubstitutions;
    private int awaySubstitutions;
    private Random rand = new Random();

    public Match(Tournament tournament, Team home, Team away, Stadium stadium, String date, String time) {
        this.tournament = tournament;
        this.home = home;
        this.away = away;
        this.stadium = stadium;
        this.date = date;
        this.time = time;
        this.isFinished = false;
        this.homeGoals = 0;
        this.awayGoals = 0;
        this.homeYellowCards = 0;
        this.awayYellowCards = 0;
        this.homeRedCards = 0;
        this.awayRedCards = 0;
        this.homeSubstitutions = 0;
        this.awaySubstitutions = 0;
        this.matchName = generateMatchName();
    }

    public String getMatchName() {
        return matchName;
    }

    public Team getHome() {
        return home;
    }

    public Team getAway() {
        return away;
    }

    public Stadium getStadium() {
        return stadium;
    }

    public int getHomeGoals() {
        return homeGoals;
    }

    public int getAwayGoals() {
        return awayGoals;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public int getHomeYellowCards() {
        return homeYellowCards;
    }

    public int getAwayYellowCards() {
        return awayYellowCards;
    }

    public int getHomeRedCards() {
        return homeRedCards;
    }

    public int getAwayRedCards() {
        return awayRedCards;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public void setFinished(boolean finished) {
        this.isFinished = finished;
    }

    public Tournament getTournament() {
        return tournament;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setHomeGoals(int homeGoals) {
        this.homeGoals = homeGoals;
    }

    public void setAwayGoals(int awayGoals) {
        this.awayGoals = awayGoals;
    }

    public void setHomeYellowCards(int homeYellowCards) {
        this.homeYellowCards = homeYellowCards;
    }

    public void setAwayYellowCards(int awayYellowCards) {
        this.awayYellowCards = awayYellowCards;
    }

    public void setHomeRedCards(int homeRedCards) {
        this.homeRedCards = homeRedCards;
    }

    public void setAwayRedCards(int awayRedCards) {
        this.awayRedCards = awayRedCards;
    }

    public int getHomeSubstitutions() {
        return homeSubstitutions;
    }

    public int getAwaySubstitutions() {
        return awaySubstitutions;
    }

    public void setHomeSubstitutions(int homeSubstitutions) {
        this.homeSubstitutions = homeSubstitutions;
    }

    public void setAwaySubstitutions(int awaySubstitutions) {
        this.awaySubstitutions = awaySubstitutions;
    }

    private String generateMatchName() {
        String homeCode = home.getCode().toUpperCase();
        String awayCode = away.getCode().toUpperCase();
        return homeCode + "_" + awayCode;
    }

    private void resetStartingPlayersDiscipline(Team team) {
        for (int i = 0; i < team.getStartingCount(); i++) {
            Player p = team.getStartingPlayer(i);
            if (p != null) {
                p.resetMatchDiscipline();
            }
        }
    }

    public void playRandomMatch() {
        if (isFinished) {
            return;
        }

        if (!home.hasValidStartingXI()) {
            home.simulateLineup();
        }

        if (!away.hasValidStartingXI()) {
            away.simulateLineup();
        }

        if (!home.hasValidStartingXI() || !away.hasValidStartingXI()) {
            System.out.println("Teams do not have valid starting XI.");
            return;
        }

        resetStartingPlayersDiscipline(home);
        resetStartingPlayersDiscipline(away);

        int MAX_GOALS = 6;
        homeGoals = rand.nextInt(MAX_GOALS + 1);
        awayGoals = rand.nextInt(MAX_GOALS + 1);

        for (int i = 0; i < homeGoals; i++) {
            Player scorer = getRandomWeightedPlayer(home);
            if (scorer != null) {
                scorer.addGoal();
            }
        }

        for (int i = 0; i < awayGoals; i++) {
            Player scorer = getRandomWeightedPlayer(away);
            if (scorer != null) {
                scorer.addGoal();
            }
        }

        simulateYellowCards();
        simulateRedCards();
        simulateInjuries();

        home.updateStats(homeGoals, awayGoals);
        away.updateStats(awayGoals, homeGoals);

        isFinished = true;

        home.reduceSuspensionsForNonPlayingPlayers();
        away.reduceSuspensionsForNonPlayingPlayers();

        home.clearLineup();
        away.clearLineup();
    }

    private Player getRandomWeightedPlayer(Team team) {
        int totalWeight = 0;

        for (int i = 0; i < team.getStartingCount(); i++) {
            Player player = team.getStartingPlayer(i);

            if (player != null && player.isAvailable()) {
                int w = getPositionWeight(player);
                if (w > 0) {
                    totalWeight += w;
                }
            }
        }

        if (totalWeight <= 0) {
            return null;
        }

        int r = rand.nextInt(totalWeight);

        for (int i = 0; i < team.getStartingCount(); i++) {
            Player player = team.getStartingPlayer(i);

            if (player != null && player.isAvailable()) {
                int w = getPositionWeight(player);
                if (w <= 0) continue;

                if (r < w) {
                    return player;
                }
                r -= w;
            }
        }

        return null;
    }

    private int getPositionWeight(Player player) {
        String pos = player.getPosition();
        if (pos == null) {
            return 1;
        }

        String p = pos.toLowerCase();

        if (p.equals("att")) {
            return 5;
        }

        if (p.equals("mid")) {
            return 3;
        }

        if (p.equals("def")) {
            return 2;
        }

        if (p.equals("gk")) {
            return 0;
        }

        return 2;
    }

    public Team getWinner() {
        if (!isFinished) {
            return null;
        } else if (homeGoals > awayGoals) {
            return home;
        } else if (awayGoals > homeGoals) {
            return away;
        } else {
            return null;
        }
    }

    private void simulateYellowCards() {
        for (int i = 0; i < 3; i++) {
            int num = rand.nextInt(100) + 1;

            if (num % 4 == 0) {
                Team chosenTeam = rand.nextBoolean() ? home : away;
                Player p = chosenTeam.getRandomStartingPlayer();

                if (p != null && !p.hasRedCard() && !p.isInjured()) {
                    boolean hadRedBefore = p.hasRedCard();
                    p.addYellowCard();

                    if (chosenTeam == home) {
                        homeYellowCards++;
                        if (!hadRedBefore && p.hasRedCard()) {
                            homeRedCards++;
                        }
                    } else {
                        awayYellowCards++;
                        if (!hadRedBefore && p.hasRedCard()) {
                            awayRedCards++;
                        }
                    }
                }
            }
        }
    }

    public String getSummary() {
        String status;

        if (!isFinished) {
            status = "Not Played Yet";
        } else if (homeGoals > awayGoals) {
            status = "Winner: " + home.getCountryName();
        } else if (awayGoals > homeGoals) {
            status = "Winner: " + away.getCountryName();
        } else {
            status = "Result: Draw";
        }

        String summary =
            "\n====================================\n" +
            "Match: " + matchName + "\n" +
            "------------------------------------\n" +
            "Home Team      : " + home.getCountryName() + "\n" +
            "Away Team      : " + away.getCountryName() + "\n" +
            "Date           : " + date + "\n" +
            "Time           : " + time + "\n" +
            "Score          : " + homeGoals + " - " + awayGoals + "\n" +
            "Home Yellow    : " + homeYellowCards + "\n" +
            "Away Yellow    : " + awayYellowCards + "\n" +
            "Home Red       : " + homeRedCards + "\n" +
            "Away Red       : " + awayRedCards + "\n" +
            "Home Subs      : " + homeSubstitutions + "\n" +
            "Away Subs      : " + awaySubstitutions + "\n" +
            "Status         : " + status + "\n" +
            "====================================\n";

        return summary;
    }

    private void simulateRedCards() {
        for (int i = 0; i < 2; i++) {
            int num = rand.nextInt(100) + 1;

            if (num % 7 == 0) {
                Team chosenTeam = rand.nextBoolean() ? home : away;
                Player p = chosenTeam.getRandomStartingPlayer();

                if (p != null && !p.hasRedCard() && !p.isInjured()) {
                    p.giveRedCard();

                    if (chosenTeam == home) {
                        homeRedCards++;
                    } else {
                        awayRedCards++;
                    }
                }
            }
        }
    }

    private void simulateInjuries() {
        int num = rand.nextInt(100) + 1;

        if (num % 9 == 0) {
            Team chosenTeam = rand.nextBoolean() ? home : away;
            Player injuredPlayer = chosenTeam.getRandomStartingPlayer();

            if (injuredPlayer != null && !injuredPlayer.isInjured() && !injuredPlayer.hasRedCard()) {
                injuredPlayer.setInjured(true);

                if (chosenTeam == home) {
                    if (homeSubstitutions < 3) {
                        Player benchPlayer = chosenTeam.getRandomBenchPlayer();

                        if (benchPlayer != null) {
                            boolean done = chosenTeam.substitutePlayer(injuredPlayer, benchPlayer);
                            if (done) {
                                homeSubstitutions++;
                            }
                        }
                    }
                } else {
                    if (awaySubstitutions < 3) {
                        Player benchPlayer = chosenTeam.getRandomBenchPlayer();

                        if (benchPlayer != null) {
                            boolean done = chosenTeam.substitutePlayer(injuredPlayer, benchPlayer);
                            if (done) {
                                awaySubstitutions++;
                            }
                        }
                    }
                }
            }
        }
    }
}

class Stadium {

    private String name;
    private String city;
    private int capacity;

    public Stadium(String name, String city, int capacity) {
        this.name = name;
        this.city = city;
        this.capacity = capacity;
    }

    public String getName() {
        return name;
    }

    public String getCity() {
        return city;
    }

    public int getCapacity() {
        return capacity;
    }

    public String toString() {
        return "Stadium: " + name + " " + city + " " + capacity;
    }
}

class Team implements Summarizable {

    private String code;
    private String countryName;
    private Person[] members;
    private int memberCount;
    private int played;
    private int wins;
    private int losses;
    private int goalsFor;
    private int goalsAgainst;
    private static final int maxMembers = 26;
    private Player[] startingXI;
    private int startingCount;
    private Player[] bench;
    private int benchCount;

    public Team(String code, String countryName) {
        this.code = code;
        this.countryName = countryName;
        this.members = new Person[maxMembers];
        this.memberCount = 0;
        this.played = 0;
        this.wins = 0;
        this.losses = 0;
        this.goalsFor = 0;
        this.goalsAgainst = 0;
        this.startingXI = new Player[11];
        this.startingCount = 0;
        this.bench = new Player[15];
        this.benchCount = 0;
    }

    public Person searchMember(String id) {
        for (int i = 0; i < members.length; i++) {
            if (members[i] != null && members[i].getNationalId().equals(id)) {
                return members[i];
            }
        }
        return null;
    }

    public boolean addMember(Person person) {
        for (int i = 0; i < members.length; i++) {
            if (members[i] != null &&
                members[i].getNationalId().equals(person.getNationalId())) {
                System.out.println("Person already in team");
                return false;
            }
        }

        if (person instanceof Coach) {
            for (int j = 0; j < memberCount; j++) {
                if (members[j] instanceof Coach) {
                    System.out.println("This team already has a coach.");
                    return false;
                }
            }
        }

        for (int i = 0; i < members.length; i++) {
            if (members[i] == null) {
                members[i] = person;
                memberCount++;
                return true;
            }
        }

        System.out.println("Team is full");
        return false;
    }

    public boolean removeMember(String id) {
        for (int i = 0; i < members.length; i++) {
            if (members[i] != null &&
                members[i].getNationalId().equals(id)) {
                members[i] = null;
                for (int j = i; j < memberCount - 1; j++) {
                    members[j] = members[j + 1];
                }
                members[--memberCount] = null;
                return true;
            }
        }
        return false;
    }

    public Person getMember(int index) {
        if (index >= 0 && index < memberCount) {
            return members[index];
        }
        return null;
    }

    public int getMemberCount() {
        return memberCount;
    }

    public String getCountryName() {
        return countryName;
    }

    public void updateStats(int goalsFor, int goalsAgainst) {
        this.goalsFor += goalsFor;
        this.goalsAgainst += goalsAgainst;
        played++;

        if (goalsFor > goalsAgainst) {
            wins++;
        } else if (goalsFor < goalsAgainst) {
            losses++;
        }
    }

    public void printMembersWithRoles() {
        System.out.println("Members of team " + countryName + ":");
        for (int i = 0; i < memberCount; i++) {
            Person p = members[i];
            System.out.println("- " + p.getRole() + ": " + p.getFullName() + " (" + p.getNationalId() + ")");
        }
    }

    public int getGoalDifference() {
        return goalsFor - goalsAgainst;
    }

    public int getPoints() {
        return wins * 3 + getDraws();
    }

    public int getPlayed() {
        return played;
    }

    public int getWins() {
        return wins;
    }

    public int getLosses() {
        return losses;
    }

    public int getGoalsFor() {
        return goalsFor;
    }

    public int getGoalsAgainst() {
        return goalsAgainst;
    }

    public int getDraws() {
        return played - wins - losses;
    }

    public String getCode() {
        return code;
    }

    public int getMaxMembers() {
        return members.length;
    }

    public boolean addToStartingXI(Player p) {
        if (p == null || startingCount >= 11 || !p.isAvailable() || !containsPlayer(p)) {
            return false;
        }

        for (int i = 0; i < startingCount; i++) {
            if (startingXI[i] == p) {
                return false;
            }
        }

        for (int i = 0; i < benchCount; i++) {
            if (bench[i] == p) {
                return false;
            }
        }

        startingXI[startingCount++] = p;
        p.setInStartingXI(true);
        return true;
    }

    public boolean addToBench(Player p) {
        if (p == null || benchCount >= bench.length || !p.isAvailable() || !containsPlayer(p)) {
            return false;
        }

        for (int i = 0; i < benchCount; i++) {
            if (bench[i] == p) {
                return false;
            }
        }

        for (int i = 0; i < startingCount; i++) {
            if (startingXI[i] == p) {
                return false;
            }
        }

        bench[benchCount++] = p;
        return true;
    }

    public Player getStartingPlayer(int index) {
        if (index >= 0 && index < startingCount) {
            return startingXI[index];
        }
        return null;
    }

    public Player getBenchPlayer(int index) {
        if (index >= 0 && index < benchCount) {
            return bench[index];
        }
        return null;
    }

    public int getStartingCount() {
        return startingCount;
    }

    public int getBenchCount() {
        return benchCount;
    }

    public boolean hasValidStartingXI() {
        if (startingCount != 11) {
            return false;
        }

        int gk = 0;
        int def = 0;
        int mid = 0;
        int att = 0;

        for (int i = 0; i < startingCount; i++) {
            Player p = startingXI[i];
            if (p == null) continue;

            String pos = p.getPosition();

            if (pos.equalsIgnoreCase("GK")) {
                gk++;
            } else if (pos.equalsIgnoreCase("DEF")) {
                def++;
            } else if (pos.equalsIgnoreCase("MID")) {
                mid++;
            } else if (pos.equalsIgnoreCase("ATT")) {
                att++;
            }
        }

        return gk == 1 && def == 4 && mid == 3 && att == 3;
    }

    public Player getRandomStartingPlayer() {
        Random rand = new Random();

        if (startingCount == 0) {
            return null;
        }

        for (int tries = 0; tries < 20; tries++) {
            int index = rand.nextInt(startingCount);
            Player p = startingXI[index];

            if (p != null && p.isAvailable()) {
                return p;
            }
        }

        return null;
    }

    public boolean substitutePlayer(Player out, Player in) {
        if (out == null || in == null) {
            return false;
        }

        if (!in.isAvailable()) {
            return false;
        }

        int outIndex = -1;
        int inIndex = -1;

        for (int i = 0; i < startingCount; i++) {
            if (startingXI[i] == out) {
                outIndex = i;
                break;
            }
        }

        for (int i = 0; i < benchCount; i++) {
            if (bench[i] == in) {
                inIndex = i;
                break;
            }
        }

        if (outIndex == -1 || inIndex == -1) {
            return false;
        }

        startingXI[outIndex] = in;
        bench[inIndex] = out;

        out.setInStartingXI(false);
        in.setInStartingXI(true);

        return true;
    }

    public void reduceSuspensionsForNonPlayingPlayers() {
        for (int i = 0; i < memberCount; i++) {
            if (members[i] instanceof Player) {
                Player p = (Player) members[i];

                if (p.getSuspensionMatches() > 0 && !p.isInStartingXI()) {
                    p.serveSuspension();
                }
            }
        }
    }

    public void clearLineup() {
        for (int i = 0; i < startingCount; i++) {
            if (startingXI[i] != null) {
                startingXI[i].setInStartingXI(false);
                startingXI[i] = null;
            }
        }
        startingCount = 0;

        for (int i = 0; i < benchCount; i++) {
            bench[i] = null;
        }
        benchCount = 0;
    }

    public boolean containsPlayer(Player p) {
        for (int i = 0; i < memberCount; i++) {
            if (members[i] == p) {
                return true;
            }
        }
        return false;
    }

    public Player getRandomBenchPlayer() {
        Random rand = new Random();

        if (benchCount == 0) {
            return null;
        }

        for (int tries = 0; tries < 20; tries++) {
            int index = rand.nextInt(benchCount);
            Player p = bench[index];

            if (p != null && p.isAvailable()) {
                return p;
            }
        }

        return null;
    }

    public void simulateLineup() {
        clearLineup();

        int gk = 0;
        int def = 0;
        int mid = 0;
        int att = 0;

        for (int i = 0; i < memberCount; i++) {
            if (!(members[i] instanceof Player)) continue;

            Player p = (Player) members[i];

            if (!p.isAvailable()) continue;

            String pos = p.getPosition();

            if (pos.equalsIgnoreCase("GK") && gk < 1) {
                addToStartingXI(p);
                gk++;
            } else if (pos.equalsIgnoreCase("DEF") && def < 4) {
                addToStartingXI(p);
                def++;
            } else if (pos.equalsIgnoreCase("MID") && mid < 3) {
                addToStartingXI(p);
                mid++;
            } else if (pos.equalsIgnoreCase("ATT") && att < 3) {
                addToStartingXI(p);
                att++;
            }

            if (gk == 1 && def == 4 && mid == 3 && att == 3) {
                break;
            }
        }

        for (int i = 0; i < memberCount; i++) {
            if (!(members[i] instanceof Player)) continue;

            Player p = (Player) members[i];

            if (!p.isAvailable()) continue;

            boolean alreadyStarting = false;

            for (int j = 0; j < startingCount; j++) {
                if (startingXI[j] == p) {
                    alreadyStarting = true;
                    break;
                }
            }

            if (!alreadyStarting) {
                addToBench(p);
            }
        }
    }

    public String getSummary() {
        return "Team: " + code + " " + countryName;
    }
}

abstract class Person {

    protected String fullName;
    protected String nationalId;
    protected int age;

    public Person(String fullName, String nationalId, int age) {
        this.fullName = fullName;
        this.nationalId = nationalId;
        this.age = age;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getNationalId() {
        return nationalId;
    }

    public void setNationalId(String nationalId) {
        this.nationalId = nationalId;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public abstract String getRole();
}

class Member extends Person {

    protected double salary;
    protected int contractYearsLeft;

    public Member(String fullName, String nationalId, int age, double salary, int contractYearsLeft) {
        super(fullName, nationalId, age);
        this.salary = salary;
        this.contractYearsLeft = contractYearsLeft;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public int getContractYearsLeft() {
        return contractYearsLeft;
    }

    public void setContractYearsLeft(int contractYearsLeft) {
        this.contractYearsLeft = contractYearsLeft;
    }

    public String getRole() {
        return "Member";
    }
}

class Player extends Member {

    protected int shirtNumber;
    protected int goals;
    protected boolean goalBonusApplied;
    protected String position;
    protected int yellowCards;
    protected boolean redCard;
    protected int suspensionMatches;
    protected boolean injured;
    protected boolean inStartingXI;
    protected int currentMatchYellowCards;

    public Player(String fullName, String nationalId, int age, double salary, int contractYears,
                  int shirtNum, String position) {
        super(fullName, nationalId, age, salary, contractYears);
        this.goals = 0;
        this.goalBonusApplied = false;
        this.position = position;
        this.shirtNumber = shirtNum;
        this.yellowCards = 0;
        this.redCard = false;
        this.injured = false;
        this.inStartingXI = false;
        this.currentMatchYellowCards = 0;
        this.suspensionMatches = 0;
    }

    public int getGoals() {
        return goals;
    }

    public void addGoal() {
        goals++;
    }

    public boolean isGoalBonusApplied() {
        return goalBonusApplied;
    }

    public void setGoalBonusApplied(boolean applied) {
        goalBonusApplied = applied;
    }

    public String getPosition() {
        return position;
    }

    public String getRole() {
        return "Player";
    }

    public int getShirtNumber() {
        return shirtNumber;
    }

    public void setShirtNumber(int shirtNumber) {
        this.shirtNumber = shirtNumber;
    }

    public void addYellowCard() {
        yellowCards++;
        currentMatchYellowCards++;

        if (currentMatchYellowCards >= 2) {
            redCard = true;
            suspensionMatches = 1;
        }
    }

    public int getYellowCards() {
        return yellowCards;
    }

    public int getSuspensionMatches() {
        return suspensionMatches;
    }

    public void serveSuspension() {
        if (suspensionMatches > 0) {
            suspensionMatches--;
        }
    }

    public boolean hasRedCard() {
        return redCard;
    }

    public void giveRedCard() {
        redCard = true;
        suspensionMatches = 1;
    }

    public boolean isInjured() {
        return injured;
    }

    public void setInjured(boolean injured) {
        this.injured = injured;
    }

    public boolean isInStartingXI() {
        return inStartingXI;
    }

    public void setInStartingXI(boolean inStartingXI) {
        this.inStartingXI = inStartingXI;
    }

    public boolean isAvailable() {
        return !injured && !redCard && suspensionMatches == 0;
    }

    public void resetMatchDiscipline() {
        currentMatchYellowCards = 0;
        redCard = false;
    }
}

class Coach extends Member {

    private int liscenseLevel;

    public Coach(String fullName, String nationalId, int age, double salary, int contractYearsLeft, int liscenseLevel) {
        super(fullName, nationalId, age, salary, contractYearsLeft);
        this.liscenseLevel = liscenseLevel;
    }

    public String getRole() {
        return "Coach";
    }

    public int getLiscenseLevel() {
        return liscenseLevel;
    }

    public void setLiscenseLevel(int liscenseLevel) {
        this.liscenseLevel = liscenseLevel;
    }
}

class Goalkeeper extends Player {

    private int saves;

    public Goalkeeper(String fullName, String nationalId, int age, double salary,
                      int contractYearsLeft, int shirtNum, String position, int saves) {
        super(fullName, nationalId, age, salary, contractYearsLeft, shirtNum, position);
        this.saves = saves;
    }

    public int getSaves() {
        return saves;
    }

    public void setSaves(int saves) {
        this.saves = saves;
    }

    public String getRole() {
        return "Goalkeeper";
    }
}