import java.util.Random;
import java.util.Scanner;
import java.io.*;
import javax.swing.*;


interface Summarizable {
    String getSummary();
}

class InvalidDateFormatException extends Exception {
    public InvalidDateFormatException(String message) {
        super(message);
    }
}

class InvalidCapacityException extends RuntimeException {
    public InvalidCapacityException(String message) {
        super(message);
    }
}

class Node {
    private Object data;
    private Node next;

    public Node(Object data) {
        this.data = data;
        this.next = null;
    }

    public Node(Object data, Node next) {
        this.data = data;
        this.next = next;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Node getNext() {
        return next;
    }

    public void setNext(Node next) {
        this.next = next;
    }
}

class ObjectLinkedList {
    private Node head;
    private Node tail;
    private int size;

    public ObjectLinkedList() {
        head = null;
        tail = null;
        size = 0;
    }

    public boolean isEmpty() {
        return head == null;
    }

    public int size() {
        return size;
    }

    public void insertAtFront(Object obj) {
        Node newNode = new Node(obj);

        if (isEmpty()) {
            head = tail = newNode;
        } else {
            newNode.setNext(head);
            head = newNode;
        }

        size++;
    }

    public void insertAtBack(Object obj) {
        Node newNode = new Node(obj);

        if (isEmpty()) {
            head = tail = newNode;
        } else {
            tail.setNext(newNode);
            tail = newNode;
        }

        size++;
    }

    public Object getObject(int index) {
        if (index < 0 || index >= size) {
            return null;
        }

        Node current = head;
        int currentIndex = 0;

        while (current != null) {
            if (currentIndex == index) {
                return current.getData();
            }

            current = current.getNext();
            currentIndex++;
        }

        return null;
    }

    public boolean setObject(int index, Object newData) {
        if (index < 0 || index >= size) {
            return false;
        }

        Node current = head;
        int currentIndex = 0;

        while (current != null) {
            if (currentIndex == index) {
                current.setData(newData);
                return true;
            }

            current = current.getNext();
            currentIndex++;
        }

        return false;
    }

    public Object removeFromFront() {
        if (isEmpty()) {
            return null;
        }

        Object removedData = head.getData();

        if (head == tail) {
            head = tail = null;
        } else {
            head = head.getNext();
        }

        size--;
        return removedData;
    }

    public Object removeFromBack() {
        if (isEmpty()) {
            return null;
        }

        Object removedData = tail.getData();

        if (head == tail) {
            head = tail = null;
            size--;
            return removedData;
        }

        Node current = head;

        while (current.getNext() != tail) {
            current = current.getNext();
        }

        current.setNext(null);
        tail = current;
        size--;

        return removedData;
    }

    public boolean removeObject(Object target) {
        if (isEmpty()) {
            return false;
        }

        if (head.getData() == target) {
            removeFromFront();
            return true;
        }

        Node previous = head;
        Node current = head.getNext();

        while (current != null) {
            if (current.getData() == target) {
                previous.setNext(current.getNext());

                if (current == tail) {
                    tail = previous;
                }

                size--;
                return true;
            }

            previous = current;
            current = current.getNext();
        }

        return false;
    }

    public void clear() {
        head = null;
        tail = null;
        size = 0;
    }
}

public class Tournament implements Serializable{

    private String name;
    private String startDate;

    private ObjectLinkedList teams;
    private ObjectLinkedList matches;
    private ObjectLinkedList stadiums;

    private int goalBonusThreshold;
    private int goalBonusAmount;

    public Tournament(String name, String startDate) {
        this.name = name;
        this.startDate = startDate;

        teams = new ObjectLinkedList();
        matches = new ObjectLinkedList();
        stadiums = new ObjectLinkedList();

        goalBonusThreshold = 30;
        goalBonusAmount = 1000;
    }

    // Stadium Management

    public boolean addStadium(Stadium stadium) {
        if (stadium == null) {
            return false;
        }

        for (int i = 0; i < stadiums.size(); i++) {
            Stadium currentStadium = (Stadium) stadiums.getObject(i);

            if (currentStadium.getName().equalsIgnoreCase(stadium.getName())) {
                return false;
            }
        }

        stadiums.insertAtBack(stadium);
        return true;
    }

    public boolean removeStadium(String stadiumName) {
        for (int i = 0; i < stadiums.size(); i++) {
            Stadium currentStadium = (Stadium) stadiums.getObject(i);

            if (currentStadium.getName().equals(stadiumName)) {
                return stadiums.removeObject(currentStadium);
            }
        }

        return false;
    }

    // Team Management

    public boolean addTeam(Team team) {
        if (team == null) {
            return false;
        }

        for (int i = 0; i < teams.size(); i++) {
            Team currentTeam = (Team) teams.getObject(i);

            if (currentTeam.getCountryName().equalsIgnoreCase(team.getCountryName())) {
                return false;
            }
        }

        teams.insertAtBack(team);
        return true;
    }

    public boolean removeTeam(String teamName) {
        for (int i = 0; i < teams.size(); i++) {
            Team currentTeam = (Team) teams.getObject(i);

            if (currentTeam.getCountryName().equals(teamName)) {
                return teams.removeObject(currentTeam);
            }
        }

        return false;
    }

    public Team searchTeam(String teamName) {
        for (int i = 0; i < teams.size(); i++) {
            Team currentTeam = (Team) teams.getObject(i);

            if (currentTeam.getCountryName().equals(teamName)) {
                return currentTeam;
            }
        }

        return null;
    }

    public void printTeamsRecursive() {
        printTeamsRecursive(0);
    }

    private void printTeamsRecursive(int index) {
        if (index < teams.size()) {
            Team currentTeam = (Team) teams.getObject(index);
            System.out.println(currentTeam.getCountryName());
            printTeamsRecursive(index + 1);
        }
    }

    // Match Management

    public boolean addMatch(Match match) {
        if (match == null) {
            return false;
        }

        if (match.getTournament() != this) {
            return false;
        }

        matches.insertAtBack(match);
        return true;
    }

    public boolean removeMatch(String matchName) {
        for (int i = 0; i < matches.size(); i++) {
            Match currentMatch = (Match) matches.getObject(i);

            if (currentMatch.getMatchName().equals(matchName)) {
                return matches.removeObject(currentMatch);
            }
        }

        return false;
    }

    public Match searchMatch(String matchName) {
        for (int i = 0; i < matches.size(); i++) {
            Match currentMatch = (Match) matches.getObject(i);

            if (currentMatch.getMatchName().equals(matchName)) {
                return currentMatch;
            }
        }

        return null;
    }

    public Match searchMatchByIndex(int index) {
        if (index >= 0 && index < matches.size()) {
            return (Match) matches.getObject(index);
        }

        return null;
    }

    public void generateAllMatchesOnce() throws InvalidDateFormatException {
        int stadiumRotationIndex = 0;

        if (matches.size() > 0) {
            System.out.println("Matches already generated. Clear first if you want to regenerate.");
            return;
        }

        if (teams.size() < 2) {
            System.out.println("Not enough teams to generate matches.");
            return;
        }

        validateStartDate(startDate);

        int day = Integer.parseInt(startDate.substring(0, 2));
        int month = Integer.parseInt(startDate.substring(3, 5));
        int year = Integer.parseInt(startDate.substring(6, 10));

        int[] daysInMonth = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

        for (int i = 0; i < teams.size(); i++) {
            for (int j = i + 1; j < teams.size(); j++) {
                Team homeTeam = (Team) teams.getObject(i);
                Team awayTeam = (Team) teams.getObject(j);

                Stadium selectedStadium = null;

                if (stadiums.size() > 0) {
                    selectedStadium = (Stadium) stadiums.getObject(stadiumRotationIndex % stadiums.size());
                }

                String matchDate = formatDate(day, month, year);
                Match newMatch = new Match(this, homeTeam, awayTeam, selectedStadium, matchDate, "8:00 PM");

                addMatch(newMatch);

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

                stadiumRotationIndex++;
            }
        }
    }

    public void clearMatches() {
        matches.clear();
        System.out.println("All matches cleared.");
    }

    public void printUpcomingMatches() {
        System.out.println("===============================================");
        System.out.println("\tUpcoming Matches in " + name);
        System.out.println("===============================================");

        boolean foundUpcomingMatch = false;

        for (int i = 0; i < matches.size(); i++) {
            Match currentMatch = (Match) matches.getObject(i);

            if (!currentMatch.isFinished()) {
                foundUpcomingMatch = true;

                System.out.println("-----------------------------------------------");
                System.out.println("Match Name : " + currentMatch.getMatchName());
                System.out.println("Home Team  : " + currentMatch.getHome().getCountryName());
                System.out.println("Away Team  : " + currentMatch.getAway().getCountryName());

                if (currentMatch.getStadium() != null) {
                    System.out.println("Stadium    : " + currentMatch.getStadium().getName());
                } else {
                    System.out.println("Stadium    : TBD");
                }

                if (currentMatch.getDate() != null) {
                    System.out.println("Date       : " + currentMatch.getDate());
                } else {
                    System.out.println("Date       : TBD");
                }

                if (currentMatch.getTime() != null) {
                    System.out.println("Time       : " + currentMatch.getTime());
                } else {
                    System.out.println("Time       : TBD");
                }
            }
        }

        if (!foundUpcomingMatch) {
            System.out.println("No upcoming matches.");
        }

        System.out.println("===============================================");
    }

    // Reports

    public void printStandings() {
        sortTeamsByStandings();

        System.out.println("===============================================================");
        System.out.println("\t\t" + name.toUpperCase());
        System.out.println("\t\tTOURNAMENT STANDINGS");
        System.out.println("===============================================================");

        System.out.printf("| %-3s | %-15s | %-2s | %-2s | %-2s | %-2s | %-3s | %-3s | %-3s | %-3s |%n",
                "Pos", "Country", "MP", "W", "D", "L", "GF", "GA", "GD", "Pts");

        System.out.println("---------------------------------------------------------------");

        for (int i = 0; i < teams.size(); i++) {
            Team currentTeam = (Team) teams.getObject(i);

            System.out.printf("| %-3d | %-15s | %-2d | %-2d | %-2d | %-2d | %-3d | %-3d | %+3d | %-3d |%n",
                    i + 1,
                    currentTeam.getCountryName(),
                    currentTeam.getPlayed(),
                    currentTeam.getWins(),
                    currentTeam.getDraws(),
                    currentTeam.getLosses(),
                    currentTeam.getGoalsFor(),
                    currentTeam.getGoalsAgainst(),
                    currentTeam.getGoalDifference(),
                    currentTeam.getPoints());
        }

        System.out.println("===============================================================");
    }

    public Team getTournamentWinner() {
        if (matches.size() == 0) {
            System.out.println("No matches have been played yet.");
            return null;
        }

        for (int i = 0; i < matches.size(); i++) {
            Match currentMatch = (Match) matches.getObject(i);

            if (!currentMatch.isFinished()) {
                System.out.println("Not all matches have been played yet.");
                return null;
            }
        }

        sortTeamsByStandings();

        if (teams.size() == 0) {
            return null;
        }

        return (Team) teams.getObject(0);
    }

    public void applyGoalBonuses() {
        for (int i = 0; i < teams.size(); i++) {
            Team currentTeam = (Team) teams.getObject(i);

            for (int j = 0; j < currentTeam.getMemberCount(); j++) {
                Person currentPerson = currentTeam.getMember(j);

                if (currentPerson instanceof Player) {
                    Player currentPlayer = (Player) currentPerson;

                    if (!currentPlayer.isGoalBonusApplied() &&
                            currentPlayer.getGoals() > goalBonusThreshold) {

                        double updatedSalary = currentPlayer.getSalary() + goalBonusAmount;
                        currentPlayer.setSalary(updatedSalary);
                        currentPlayer.setGoalBonusApplied(true);
                    }
                }
            }
        }
    }

    public void printTopScorers() {
        ObjectLinkedList scorerPlayers = new ObjectLinkedList();
        ObjectLinkedList scorerTeams = new ObjectLinkedList();

        for (int i = 0; i < teams.size(); i++) {
            Team currentTeam = (Team) teams.getObject(i);

            for (int j = 0; j < currentTeam.getMemberCount(); j++) {
                Person currentPerson = currentTeam.getMember(j);

                if (currentPerson instanceof Player) {
                    scorerPlayers.insertAtBack(currentPerson);
                    scorerTeams.insertAtBack(currentTeam);
                }
            }
        }

        if (scorerPlayers.size() == 0) {
            System.out.println("No players found.");
            return;
        }

        for (int i = 0; i < scorerPlayers.size() - 1; i++) {
            int bestIndex = i;

            for (int j = i + 1; j < scorerPlayers.size(); j++) {
                Player currentPlayer = (Player) scorerPlayers.getObject(j);
                Player bestPlayer = (Player) scorerPlayers.getObject(bestIndex);

                if (currentPlayer.getGoals() > bestPlayer.getGoals()) {
                    bestIndex = j;
                }
            }

            if (bestIndex != i) {
                Object tempPlayer = scorerPlayers.getObject(i);
                Object tempTeam = scorerTeams.getObject(i);

                scorerPlayers.setObject(i, scorerPlayers.getObject(bestIndex));
                scorerTeams.setObject(i, scorerTeams.getObject(bestIndex));

                scorerPlayers.setObject(bestIndex, tempPlayer);
                scorerTeams.setObject(bestIndex, tempTeam);
            }
        }

        System.out.println("==========================================================");
        System.out.println("\t\tTOP SCORERS");
        System.out.println("==========================================================");
        System.out.printf("| %4s | %-20s | %-5s | %-6s | %-5s |%n",
                "Rank", "Player", "Goals", "Team", "Pos");
        System.out.println("----------------------------------------------------------");

        int rank = 1;
        boolean foundScorer = false;

        for (int i = 0; i < scorerPlayers.size(); i++) {
            Player currentPlayer = (Player) scorerPlayers.getObject(i);
            Team currentTeam = (Team) scorerTeams.getObject(i);

            if (currentPlayer.getGoals() > 0) {
                System.out.printf("| %4d | %-20s | %5d | %-6s | %-5s |%n",
                        rank++,
                        currentPlayer.getFullName(),
                        currentPlayer.getGoals(),
                        currentTeam.getCode(),
                        currentPlayer.getPosition());

                foundScorer = true;
            }
        }

        if (!foundScorer) {
            System.out.println("|            No players with goals found.                |");
        }

        System.out.println("==========================================================");
    }

    // Helper Methods

    private void sortTeamsByStandings() {
        for (int i = 0; i < teams.size() - 1; i++) {
            int bestIndex = i;

            for (int j = i + 1; j < teams.size(); j++) {
                Team currentTeam = (Team) teams.getObject(j);
                Team bestTeam = (Team) teams.getObject(bestIndex);

                int currentPoints = currentTeam.getPoints();
                int bestPoints = bestTeam.getPoints();

                if (currentPoints > bestPoints) {
                    bestIndex = j;
                } else if (currentPoints == bestPoints) {
                    int currentGoalDifference = currentTeam.getGoalDifference();
                    int bestGoalDifference = bestTeam.getGoalDifference();

                    if (currentGoalDifference > bestGoalDifference) {
                        bestIndex = j;
                    } else if (currentGoalDifference == bestGoalDifference) {
                        int currentGoalsFor = currentTeam.getGoalsFor();
                        int bestGoalsFor = bestTeam.getGoalsFor();

                        if (currentGoalsFor > bestGoalsFor) {
                            bestIndex = j;
                        } else if (currentGoalsFor == bestGoalsFor) {
                            String currentCountryName = currentTeam.getCountryName().toLowerCase();
                            String bestCountryName = bestTeam.getCountryName().toLowerCase();

                            if (currentCountryName.compareTo(bestCountryName) < 0) {
                                bestIndex = j;
                            }
                        }
                    }
                }
            }

            if (bestIndex != i) {
                Object temporaryTeam = teams.getObject(i);
                teams.setObject(i, teams.getObject(bestIndex));
                teams.setObject(bestIndex, temporaryTeam);
            }
        }
    }

    private void validateStartDate(String date) throws InvalidDateFormatException {
        if (date == null || date.length() != 10) {
            throw new InvalidDateFormatException("Invalid date format. Expected format: DD-MM-YYYY");
        }

        if (date.charAt(2) != '-' || date.charAt(5) != '-') {
            throw new InvalidDateFormatException("Invalid date format. Use dashes. Expected format: DD-MM-YYYY");
        }

        for (int i = 0; i < date.length(); i++) {
            if (i == 2 || i == 5) {
                continue;
            }

            if (!Character.isDigit(date.charAt(i))) {
                throw new InvalidDateFormatException("Invalid date format. Only digits allowed in date and month. Expected format: DD-MM-YYYY");
            }
        }

        int day = Integer.parseInt(date.substring(0, 2));
        int month = Integer.parseInt(date.substring(3, 5));
        int year = Integer.parseInt(date.substring(6, 10));

        if (month < 1 || month > 12) {
            throw new InvalidDateFormatException("Invalid month. Month must be between 1 and 12.");
        }

        int[] daysInMonth = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

        if (day < 1 || day > daysInMonth[month - 1]) {
            throw new InvalidDateFormatException("Invalid day for the given month.");
        }

        if (year < 2026) {
            throw new InvalidDateFormatException("Invalid year. Year must be at least 2026.");
        }
    }

    private String formatDate(int day, int month, int year) {
        String formattedDay = day < 10 ? "0" + day : String.valueOf(day);
        String formattedMonth = month < 10 ? "0" + month : String.valueOf(month);
        return formattedDay + "-" + formattedMonth + "-" + year;
    }

    // File Handling Methods

    public Team loadExistingTeamFromFile(String fileName) throws IOException {
        File inFile = new File("existingTeams", fileName);
        Scanner fileInput = new Scanner(inFile);

        Team loadedTeam = null;

        while (fileInput.hasNext()) {
            String line = fileInput.nextLine();

            if (!line.equals("")) {
                String[] mainParts = line.split("=");

                if (mainParts.length != 2) {
                    fileInput.close();
                    throw new IOException("Invalid file format.");
                }

                String recordType = mainParts[0];
                String[] data = mainParts[1].split(",");

                if (recordType.equals("TEAM")) {
                    if (data.length != 2) {
                        fileInput.close();
                        throw new IOException("Invalid team data in file.");
                    }

                    String teamCode = data[0];
                    String teamName = data[1];

                    loadedTeam = new Team(teamCode, teamName);
                } else if (recordType.equals("COACH")) {
                    if (loadedTeam == null) {
                        fileInput.close();
                        throw new IOException("Team data must come before member data.");
                    }

                    if (data.length != 6) {
                        fileInput.close();
                        throw new IOException("Invalid coach data in file.");
                    }

                    String fullName = data[0];
                    String nationalId = data[1];
                    int age = Integer.parseInt(data[2]);
                    double salary = Double.parseDouble(data[3]);
                    int contractYearsLeft = Integer.parseInt(data[4]);
                    int licenseLevel = Integer.parseInt(data[5]);

                    Coach coach = new Coach(fullName, nationalId, age, salary,
                            contractYearsLeft, licenseLevel);

                    loadedTeam.addMember(coach);
                } else if (recordType.equals("GOALKEEPER")) {
                    if (loadedTeam == null) {
                        fileInput.close();
                        throw new IOException("Team data must come before member data.");
                    }

                    if (data.length != 8) {
                        fileInput.close();
                        throw new IOException("Invalid goalkeeper data in file.");
                    }

                    String fullName = data[0];
                    String nationalId = data[1];
                    int age = Integer.parseInt(data[2]);
                    double salary = Double.parseDouble(data[3]);
                    int contractYearsLeft = Integer.parseInt(data[4]);
                    int shirtNumber = Integer.parseInt(data[5]);
                    String position = data[6];
                    int saves = Integer.parseInt(data[7]);

                    Goalkeeper goalkeeper = new Goalkeeper(fullName, nationalId, age,
                            salary, contractYearsLeft, shirtNumber, position, saves);

                    loadedTeam.addMember(goalkeeper);
                } else if (recordType.equals("PLAYER")) {
                    if (loadedTeam == null) {
                        fileInput.close();
                        throw new IOException("Team data must come before member data.");
                    }

                    if (data.length != 7) {
                        fileInput.close();
                        throw new IOException("Invalid player data in file.");
                    }

                    String fullName = data[0];
                    String nationalId = data[1];
                    int age = Integer.parseInt(data[2]);
                    double salary = Double.parseDouble(data[3]);
                    int contractYearsLeft = Integer.parseInt(data[4]);
                    int shirtNumber = Integer.parseInt(data[5]);
                    String position = data[6];

                    Player player = new Player(fullName, nationalId, age,
                            salary, contractYearsLeft, shirtNumber, position);

                    loadedTeam.addMember(player);
                } else {
                    fileInput.close();
                    throw new IOException("Unknown record type in file.");
                }
            }
        }

        fileInput.close();

        if (loadedTeam == null) {
            throw new IOException("No team data found in file.");
        }

        return loadedTeam;
    }

    public void saveTeamToFile(Team team) throws IOException {
        if (team == null) {
            throw new IOException("Cannot save a null team.");
        }

        File outFile = new File("savedTeams", team.getCode() + ".txt");
        FileOutputStream fileOutputStream = new FileOutputStream(outFile);
        PrintWriter printWriter = new PrintWriter(fileOutputStream);

        printWriter.println("TEAM=" + team.getCode() + "," + team.getCountryName());

        for (int i = 0; i < team.getMemberCount(); i++) {
            Person currentPerson = team.getMember(i);

            if (currentPerson instanceof Goalkeeper) {
                Goalkeeper currentGoalkeeper = (Goalkeeper) currentPerson;

                printWriter.println("GOALKEEPER=" +
                        currentGoalkeeper.getFullName() + "," +
                        currentGoalkeeper.getNationalId() + "," +
                        currentGoalkeeper.getAge() + "," +
                        currentGoalkeeper.getSalary() + "," +
                        currentGoalkeeper.getContractYearsLeft() + "," +
                        currentGoalkeeper.getShirtNumber() + "," +
                        currentGoalkeeper.getPosition() + "," +
                        currentGoalkeeper.getSaves());
            } else if (currentPerson instanceof Player) {
                Player currentPlayer = (Player) currentPerson;

                printWriter.println("PLAYER=" +
                        currentPlayer.getFullName() + "," +
                        currentPlayer.getNationalId() + "," +
                        currentPlayer.getAge() + "," +
                        currentPlayer.getSalary() + "," +
                        currentPlayer.getContractYearsLeft() + "," +
                        currentPlayer.getShirtNumber() + "," +
                        currentPlayer.getPosition());
            } else if (currentPerson instanceof Coach) {
                Coach currentCoach = (Coach) currentPerson;

                printWriter.println("COACH=" +
                        currentCoach.getFullName() + "," +
                        currentCoach.getNationalId() + "," +
                        currentCoach.getAge() + "," +
                        currentCoach.getSalary() + "," +
                        currentCoach.getContractYearsLeft() + "," +
                        currentCoach.getLicenseLevel());
            }
        }

        printWriter.close();
    }



    public void printExistingTeamFiles() {
        File folder = new File("existingTeams");

        if (!folder.exists() || !folder.isDirectory()) {
            System.out.println("existingTeams folder was not found.");
            return;
        }

        String[] fileNames = folder.list();

        if (fileNames == null || fileNames.length == 0) {
            System.out.println("No existing team files found.");
            return;
        }

        System.out.println("\nAvailable existing team files:");

        for (int i = 0; i < fileNames.length; i++) {
            if (fileNames[i].endsWith(".txt")) {
                System.out.println("- " + fileNames[i]);
            }
        }
    }

    

    public void saveResultsToFile() throws IOException {
        File resultsDir = new File("results");

        if (!resultsDir.exists()) {
            resultsDir.mkdirs();
        }

        File outFile = new File(resultsDir, name.replaceAll("\\s+", "_") + "_results.txt");
        PrintWriter pw = new PrintWriter(new FileOutputStream(outFile));

        pw.println("===============================================================");
        pw.println("\t\t" + name.toUpperCase());
        pw.println("\t\tTOURNAMENT STANDINGS");
        pw.println("===============================================================");

        sortTeamsByStandings();

        pw.printf("| %-3s | %-15s | %-2s | %-2s | %-2s | %-2s | %-3s | %-3s | %-3s | %-3s |%n",
                "Pos", "Country", "MP", "W", "D", "L", "GF", "GA", "GD", "Pts");
        pw.println("---------------------------------------------------------------");

        for (int i = 0; i < teams.size(); i++) {
            Team t = (Team) teams.getObject(i);
            pw.printf("| %-3d | %-15s | %-2d | %-2d | %-2d | %-2d | %-3d | %-3d | %+3d | %-3d |%n",
                    i + 1, t.getCountryName(), t.getPlayed(), t.getWins(), t.getDraws(),
                    t.getLosses(), t.getGoalsFor(), t.getGoalsAgainst(),
                    t.getGoalDifference(), t.getPoints());
        }

        pw.println("===============================================================");
        pw.println();

        pw.println("==========================================================");
        pw.println("\t\tTOP SCORERS");
        pw.println("==========================================================");
        pw.printf("| %4s | %-20s | %-5s | %-6s | %-5s |%n",
                "Rank", "Player", "Goals", "Team", "Pos");
        pw.println("----------------------------------------------------------");

        ObjectLinkedList scorerPlayers = new ObjectLinkedList();
        ObjectLinkedList scorerTeams = new ObjectLinkedList();

        for (int i = 0; i < teams.size(); i++) {
            Team t = (Team) teams.getObject(i);
            for (int j = 0; j < t.getMemberCount(); j++) {
                Person p = t.getMember(j);
                if (p instanceof Player) {
                    scorerPlayers.insertAtBack(p);
                    scorerTeams.insertAtBack(t);
                }
            }
        }

        for (int i = 0; i < scorerPlayers.size() - 1; i++) {
            int bestIndex = i;
            for (int j = i + 1; j < scorerPlayers.size(); j++) {
                if (((Player) scorerPlayers.getObject(j)).getGoals() >
                        ((Player) scorerPlayers.getObject(bestIndex)).getGoals()) {
                    bestIndex = j;
                }
            }
            if (bestIndex != i) {
                Object tp = scorerPlayers.getObject(i);
                Object tt = scorerTeams.getObject(i);
                scorerPlayers.setObject(i, scorerPlayers.getObject(bestIndex));
                scorerTeams.setObject(i, scorerTeams.getObject(bestIndex));
                scorerPlayers.setObject(bestIndex, tp);
                scorerTeams.setObject(bestIndex, tt);
            }
        }

        int rank = 1;
        boolean foundScorer = false;

        for (int i = 0; i < scorerPlayers.size(); i++) {
            Player p = (Player) scorerPlayers.getObject(i);
            Team t = (Team) scorerTeams.getObject(i);
            if (p.getGoals() > 0) {
                pw.printf("| %4d | %-20s | %5d | %-6s | %-5s |%n",
                        rank++, p.getFullName(), p.getGoals(), t.getCode(), p.getPosition());
                foundScorer = true;
            }
        }

        if (!foundScorer) {
            pw.println("|            No players with goals found.                |");
        }

        pw.println("==========================================================");
        pw.println();

        Team winner = getTournamentWinner();

        if (winner != null) {
            pw.println("==========================================================");
            pw.println("  TOURNAMENT WINNER: " + winner.getCountryName().toUpperCase());
            pw.println("==========================================================");
        }

        pw.close();
    }

    // Getters and Setters

    public String getName() {
        return name;
    }

    public ObjectLinkedList getTeams() {
        return teams;
    }

    public int getTeamCount() {
        return teams.size();
    }

    public ObjectLinkedList getMatches() {
        return matches;
    }

    public int getMatchCount() {
        return matches.size();
    }

    public ObjectLinkedList getStadiums() {
        return stadiums;
    }

    public int getStadiumCount() {
        return stadiums.size();
    }

    public String getStartDate() {
        return startDate;
    }

    public int getGoalBonusThreshold() {
        return goalBonusThreshold;
    }

    public int getGoalBonusAmount() {
        return goalBonusAmount;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public void setGoalBonusAmount(int goalBonusAmount) {
        this.goalBonusAmount = goalBonusAmount;
    }

    public void setGoalBonusThreshold(int goalBonusThreshold) {
        this.goalBonusThreshold = goalBonusThreshold;
    }

    public void setName(String name) {
        this.name = name;
    }
}

class Match implements Summarizable,Serializable {

    private Tournament tournament;
    private String matchName;

    private Team home;
    private Team away;
    private Stadium stadium;

    private int homeGoals;
    private int awayGoals;

    private String date;
    private String time;

    private boolean finished;

    private int homeYellowCards;
    private int awayYellowCards;
    private int homeRedCards;
    private int awayRedCards;
    private int homeSubstitutions;
    private int awaySubstitutions;

    private Random randomGenerator = new Random();

    public Match(Tournament tournament, Team home, Team away, Stadium stadium, String date, String time) {
        this.tournament = tournament;
        this.home = home;
        this.away = away;
        this.stadium = stadium;
        this.date = date;
        this.time = time;

        finished = false;

        homeGoals = 0;
        awayGoals = 0;

        homeYellowCards = 0;
        awayYellowCards = 0;
        homeRedCards = 0;
        awayRedCards = 0;

        homeSubstitutions = 0;
        awaySubstitutions = 0;

        matchName = generateMatchName();
    }

  public void playRandomMatch() {
    if (finished) {
        return;
    }

    
    if (!home.hasValidStartingXI() || !away.hasValidStartingXI()) {
        System.out.println("Teams do not have valid starting XI.");
        return;
    }

    resetStartingPlayersDiscipline(home);
    resetStartingPlayersDiscipline(away);

    homeGoals = 0;
    awayGoals = 0;

    int homeAttack = 0, awayAttack = 0;
    int homeMidfield = 0, awayMidfield = 0;
    int homeDefense = 0, awayDefense = 0;
    int homeGoalkeeper = 0, awayGoalkeeper = 0;

    int homeChemistry = 0, awayChemistry = 0;

    for (int i = 0; i < home.getStartingCount(); i++) {
        Player p = home.getStartingPlayer(i);

        if (p == null) {
            continue;
        }

        String position = p.getPosition();

        if (position.equalsIgnoreCase("ATT")) {
            homeAttack += 15 + (p.getGoals() * 2);
            homeMidfield += 5;
            homeDefense += 1;
        } else if (position.equalsIgnoreCase("MID")) {
            homeAttack += 7;
            homeMidfield += 15 + p.getGoals();
            homeDefense += 5;
        } else if (position.equalsIgnoreCase("DEF")) {
            homeAttack += 2;
            homeMidfield += 3;
            homeDefense += 16;
        } else if (position.equalsIgnoreCase("GK")) {
            homeDefense += 8;

            if (p instanceof Goalkeeper) {
                Goalkeeper gk = (Goalkeeper) p;
                homeGoalkeeper += gk.getSaves();
            } else {
                homeGoalkeeper += 20;
            }
        }

        homeDefense -= p.getYellowCards();

        if (p.hasRedCard()) {
            homeAttack -= 5;
            homeMidfield -= 7;
            homeDefense -= 10;
            homeChemistry -= 3;
        }

        if (p.isInjured()) {
            homeChemistry -= 2;

            if (position.equalsIgnoreCase("ATT")) {
                homeAttack -= 5;
            } else if (position.equalsIgnoreCase("MID")) {
                homeMidfield -= 5;
            } else if (position.equalsIgnoreCase("DEF")) {
                homeDefense -= 5;
            } else if (position.equalsIgnoreCase("GK")) {
                homeGoalkeeper -= 5;
            }
        }
    }

    for (int i = 0; i < away.getStartingCount(); i++) {
        Player p = away.getStartingPlayer(i);

        if (p == null) {
            continue;
        }

        String position = p.getPosition();

        if (position.equalsIgnoreCase("ATT")) {
            awayAttack += 15 + (p.getGoals() * 2);
            awayMidfield += 5;
            awayDefense += 1;
        } else if (position.equalsIgnoreCase("MID")) {
            awayAttack += 7;
            awayMidfield += 15 + p.getGoals();
            awayDefense += 5;
        } else if (position.equalsIgnoreCase("DEF")) {
            awayAttack += 2;
            awayMidfield += 3;
            awayDefense += 16;
        } else if (position.equalsIgnoreCase("GK")) {
            awayDefense += 8;

            if (p instanceof Goalkeeper) {
                Goalkeeper gk = (Goalkeeper) p;
                awayGoalkeeper += gk.getSaves();
            } else {
                awayGoalkeeper += 20;
            }
        }

        awayDefense -= p.getYellowCards();

        if (p.hasRedCard()) {
            awayAttack -= 5;
            awayMidfield -= 7;
            awayDefense -= 10;
            awayChemistry -= 3;
        }

        if (p.isInjured()) {
            awayChemistry -= 2;

            if (position.equalsIgnoreCase("ATT")) {
                awayAttack -= 5;
            } else if (position.equalsIgnoreCase("MID")) {
                awayMidfield -= 5;
            } else if (position.equalsIgnoreCase("DEF")) {
                awayDefense -= 5;
            } else if (position.equalsIgnoreCase("GK")) {
                awayGoalkeeper -= 5;
            }
        }
    }

    if (home.hasValidStartingXI()) {
        homeChemistry += 10;
    }

    if (away.hasValidStartingXI()) {
        awayChemistry += 10;
    }

    homeAttack += homeChemistry / 2;
    homeMidfield += homeChemistry;

    awayAttack += awayChemistry / 2;
    awayMidfield += awayChemistry;

    // Home advantage
    homeAttack += 5;
    homeMidfield += 5;

    // Stadium crowd bonus
    if (stadium != null && stadium.getCapacity() > 50000) {
        homeAttack += 3;
        homeMidfield += 3;
    }

    if (homeAttack < 1) homeAttack = 1;
    if (awayAttack < 1) awayAttack = 1;
    if (homeMidfield < 1) homeMidfield = 1;
    if (awayMidfield < 1) awayMidfield = 1;
    if (homeDefense < 1) homeDefense = 1;
    if (awayDefense < 1) awayDefense = 1;
    if (homeGoalkeeper < 1) homeGoalkeeper = 1;
    if (awayGoalkeeper < 1) awayGoalkeeper = 1;

    int totalMidfield = homeMidfield + awayMidfield;

    int homePossession = (homeMidfield * 100) / totalMidfield;
    int awayPossession = 100 - homePossession;

    int homeAttacks =
        (homePossession / 50)
        + (homeAttack / 50)
        + randomGenerator.nextInt(2);

    int awayAttacks =
        (awayPossession / 50)
        + (awayAttack / 50)
        + randomGenerator.nextInt(2);

    for (int minute = 1; minute <= 90; minute++) {

        if (minute == 75) {
            homeDefense -= 3;
            awayDefense -= 3;
            homeMidfield -= 2;
            awayMidfield -= 2;

            if (homeDefense < 1) homeDefense = 1;
            if (awayDefense < 1) awayDefense = 1;
            if (homeMidfield < 1) homeMidfield = 1;
            if (awayMidfield < 1) awayMidfield = 1;
        }

        if (randomGenerator.nextInt(100) < homeAttacks) {
            int attackChance = homeAttack + (homeMidfield / 2) + randomGenerator.nextInt(15);
            int defenseChance = awayDefense + randomGenerator.nextInt(15);

            if (attackChance > defenseChance) {
                Player scorer = getRandomWeightedPlayer(home);

                if (scorer != null) {
                    int shotPower = homeAttack + (scorer.getGoals() / 2) + randomGenerator.nextInt(10);
                    int savePower = awayGoalkeeper + randomGenerator.nextInt(10);

                    if (shotPower > savePower) {
                        homeGoals++;
                        scorer.addGoal();

                        homeDefense += 4;
                        awayAttack += 5;
                        awayDefense -= 2;

                        if (awayDefense < 1) {
                            awayDefense = 1;
                        }
                    }
                }
            }
        }

        if (randomGenerator.nextInt(100) < awayAttacks) {
            int attackChance = awayAttack + (awayMidfield / 2) + randomGenerator.nextInt(15);
            int defenseChance = homeDefense + randomGenerator.nextInt(15);

            if (attackChance > defenseChance) {
                Player scorer = getRandomWeightedPlayer(away);

                if (scorer != null) {
                    int shotPower = awayAttack + (scorer.getGoals() / 2) + randomGenerator.nextInt(10);
                    int savePower = homeGoalkeeper + randomGenerator.nextInt(10);

                    if (shotPower > savePower) {
                        awayGoals++;
                        scorer.addGoal();

                        awayDefense += 4;
                        homeAttack += 5;
                        homeDefense -= 2;

                        if (homeDefense < 1) {
                            homeDefense = 1;
                        }
                    }
                }
            }
        }
    }

    simulateYellowCards();
    simulateRedCards();
    simulateInjuries();

    home.updateStats(homeGoals, awayGoals);
    away.updateStats(awayGoals, homeGoals);

    finished = true;

    home.reduceSuspensionsForNonPlayingPlayers();
    away.reduceSuspensionsForNonPlayingPlayers();

    home.clearLineup();
    away.clearLineup();
}

    public Team getWinner() {
        if (!finished) {
            return null;
        }

        if (homeGoals > awayGoals) {
            return home;
        }

        if (awayGoals > homeGoals) {
            return away;
        }

        return null;
    }

    public String getSummary() {
        String matchStatus;

        if (!finished) {
            matchStatus = "Not Played Yet";
        } else if (homeGoals > awayGoals) {
            matchStatus = "Winner: " + home.getCountryName();
        } else if (awayGoals > homeGoals) {
            matchStatus = "Winner: " + away.getCountryName();
        } else {
            matchStatus = "Result: Draw";
        }

        return "\n====================================\n" +
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
                "Status         : " + matchStatus + "\n" +
                "====================================\n";
    }

    private String generateMatchName() {
        String homeCode = home.getCode().toUpperCase();
        String awayCode = away.getCode().toUpperCase();
        return homeCode + "_" + awayCode;
    }

    private void resetStartingPlayersDiscipline(Team team) {
        for (int i = 0; i < team.getStartingCount(); i++) {
            Player currentPlayer = team.getStartingPlayer(i);

            if (currentPlayer != null) {
                currentPlayer.resetMatchDiscipline();
            }
        }
    }

    private Player getRandomWeightedPlayer(Team team) {
        int totalWeight = 0;

        for (int i = 0; i < team.getStartingCount(); i++) {
            Player currentPlayer = team.getStartingPlayer(i);

            if (currentPlayer != null && currentPlayer.isAvailable()) {
                int positionWeight = getPositionWeight(currentPlayer);

                if (positionWeight > 0) {
                    totalWeight += positionWeight;
                }
            }
        }

        if (totalWeight <= 0) {
            return null;
        }

        int randomValue = randomGenerator.nextInt(totalWeight);

        for (int i = 0; i < team.getStartingCount(); i++) {
            Player currentPlayer = team.getStartingPlayer(i);

            if (currentPlayer != null && currentPlayer.isAvailable()) {
                int positionWeight = getPositionWeight(currentPlayer);

                if (positionWeight <= 0) {
                    continue;
                }

                if (randomValue < positionWeight) {
                    return currentPlayer;
                }

                randomValue -= positionWeight;
            }
        }

        return null;
    }

    private int getPositionWeight(Player player) {
        String position = player.getPosition();

        if (position == null) {
            return 1;
        }

        String lowerCasePosition = position.toLowerCase();

        if (lowerCasePosition.equals("att")) {
            return 5;
        }

        if (lowerCasePosition.equals("mid")) {
            return 3;
        }

        if (lowerCasePosition.equals("def")) {
            return 2;
        }

        if (lowerCasePosition.equals("gk")) {
            return 0;
        }

        return 2;
    }

    private void simulateYellowCards() {
        for (int i = 0; i < 3; i++) {
            int randomNumber = randomGenerator.nextInt(100) + 1;

            if (randomNumber % 4 == 0) {
                Team selectedTeam = randomGenerator.nextBoolean() ? home : away;
                Player selectedPlayer = selectedTeam.getRandomStartingPlayer();

                if (selectedPlayer != null && !selectedPlayer.hasRedCard() && !selectedPlayer.isInjured()) {
                    boolean alreadyHadRedCard = selectedPlayer.hasRedCard();
                    selectedPlayer.addYellowCard();

                    if (selectedTeam == home) {
                        homeYellowCards++;

                        if (!alreadyHadRedCard && selectedPlayer.hasRedCard()) {
                            homeRedCards++;
                        }
                    } else {
                        awayYellowCards++;

                        if (!alreadyHadRedCard && selectedPlayer.hasRedCard()) {
                            awayRedCards++;
                        }
                    }
                }
            }
        }
    }

    private void simulateRedCards() {
        for (int i = 0; i < 2; i++) {
            int randomNumber = randomGenerator.nextInt(100) + 1;

            if (randomNumber % 7 == 0) {
                Team selectedTeam = randomGenerator.nextBoolean() ? home : away;
                Player selectedPlayer = selectedTeam.getRandomStartingPlayer();

                if (selectedPlayer != null && !selectedPlayer.hasRedCard() && !selectedPlayer.isInjured()) {
                    selectedPlayer.giveRedCard();

                    if (selectedTeam == home) {
                        homeRedCards++;
                    } else {
                        awayRedCards++;
                    }
                }
            }
        }
    }

    private void simulateInjuries() {
        int randomNumber = randomGenerator.nextInt(100) + 1;

        if (randomNumber % 5 == 0) {
            Team selectedTeam = randomGenerator.nextBoolean() ? home : away;
            Player injuredPlayer = selectedTeam.getRandomStartingPlayer();

            if (injuredPlayer != null && !injuredPlayer.isInjured() && !injuredPlayer.hasRedCard()) {
                injuredPlayer.setInjured(true);

                if (selectedTeam == home) {
                    if (homeSubstitutions < 3) {
                        Player benchPlayer = selectedTeam.getRandomBenchPlayer();

                        if (benchPlayer != null) {
                            boolean substitutionCompleted = selectedTeam.substitutePlayer(injuredPlayer, benchPlayer);

                            if (substitutionCompleted) {
                                homeSubstitutions++;
                            }
                        }
                    }
                } else {
                    if (awaySubstitutions < 3) {
                        Player benchPlayer = selectedTeam.getRandomBenchPlayer();

                        if (benchPlayer != null) {
                            boolean substitutionCompleted = selectedTeam.substitutePlayer(injuredPlayer, benchPlayer);

                            if (substitutionCompleted) {
                                awaySubstitutions++;
                            }
                        }
                    }
                }
            }
        }
    }

    public Tournament getTournament() {
        return tournament;
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

    public boolean isFinished() {
        return finished;
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

    public int getHomeSubstitutions() {
        return homeSubstitutions;
    }

    public int getAwaySubstitutions() {
        return awaySubstitutions;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
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

    public void setHomeSubstitutions(int homeSubstitutions) {
        this.homeSubstitutions = homeSubstitutions;
    }

    public void setAwaySubstitutions(int awaySubstitutions) {
        this.awaySubstitutions = awaySubstitutions;
    }
}

class Stadium implements Serializable{

    private String name;
    private String city;
    private int capacity;

    public Stadium(String name, String city, int capacity) {
        if (capacity <= 0) {
            throw new InvalidCapacityException("Capacity must be greater than 0");
        }

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

class Team implements Summarizable,Serializable {

    private static final int MAX_MEMBERS = 26;
    private static final int MAX_STARTING_XI = 11;
    private static final int MAX_BENCH = 15;

    private String code;
    private String countryName;

    private ObjectLinkedList members;
    private ObjectLinkedList startingXI;
    private ObjectLinkedList bench;

    private int played;
    private int wins;
    private int losses;
    private int goalsFor;
    private int goalsAgainst;

    public Team(String code, String countryName) {
        this.code = code;
        this.countryName = countryName;

        members = new ObjectLinkedList();
        startingXI = new ObjectLinkedList();
        bench = new ObjectLinkedList();

        played = 0;
        wins = 0;
        losses = 0;
        goalsFor = 0;
        goalsAgainst = 0;
    }

    public Person searchMember(String nationalId) {
        for (int i = 0; i < members.size(); i++) {
            Person currentMember = (Person) members.getObject(i);

            if (currentMember.getNationalId().equals(nationalId)) {
                return currentMember;
            }
        }

        return null;
    }

    public boolean addMember(Person person) {
        if (person == null) {
            return false;
        }

        for (int i = 0; i < members.size(); i++) {
            Person currentMember = (Person) members.getObject(i);

            if (currentMember.getNationalId().equals(person.getNationalId())) {
                System.out.println("Person already in team");
                return false;
            }
        }

        if (person instanceof Coach) {
            for (int i = 0; i < members.size(); i++) {
                Person currentMember = (Person) members.getObject(i);

                if (currentMember instanceof Coach) {
                    System.out.println("This team already has a coach.");
                    return false;
                }
            }
        }

        if (members.size() >= MAX_MEMBERS) {
            System.out.println("Team is full");
            return false;
        }

        members.insertAtBack(person);
        return true;
    }

    public boolean removeMember(String nationalId) {
        for (int i = 0; i < members.size(); i++) {
            Person currentMember = (Person) members.getObject(i);

            if (currentMember.getNationalId().equals(nationalId)) {
                return members.removeObject(currentMember);
            }
        }

        return false;
    }

    public void printMembersWithRoles() {
        System.out.println("Members of team " + countryName + ":");

        for (int i = 0; i < members.size(); i++) {
            Person currentMember = (Person) members.getObject(i);
            System.out.println("- " + currentMember.getRole() + ": " +
                    currentMember.getFullName() + " (" + currentMember.getNationalId() + ")");
        }
    }

    public void updateStats(int scoredGoals, int concededGoals) {
        goalsFor += scoredGoals;
        goalsAgainst += concededGoals;
        played++;

        if (scoredGoals > concededGoals) {
            wins++;
        } else if (scoredGoals < concededGoals) {
            losses++;
        }
    }

    public int getGoalDifference() {
        return goalsFor - goalsAgainst;
    }

    public int getPoints() {
        return wins * 3 + getDraws();
    }

    public int getDraws() {
        return played - wins - losses;
    }

    public boolean addToStartingXI(Player player) {
        if (player == null || startingXI.size() >= MAX_STARTING_XI || !player.isAvailable() || !containsPlayer(player)) {
            return false;
        }

        for (int i = 0; i < startingXI.size(); i++) {
            Player currentPlayer = (Player) startingXI.getObject(i);

            if (currentPlayer == player) {
                return false;
            }
        }

        for (int i = 0; i < bench.size(); i++) {
            Player currentPlayer = (Player) bench.getObject(i);

            if (currentPlayer == player) {
                return false;
            }
        }

        startingXI.insertAtBack(player);
        player.setInStartingXI(true);
        return true;
    }

    public boolean addToBench(Player player) {
        if (player == null || bench.size() >= MAX_BENCH || !player.isAvailable() || !containsPlayer(player)) {
            return false;
        }

        for (int i = 0; i < bench.size(); i++) {
            Player currentPlayer = (Player) bench.getObject(i);

            if (currentPlayer == player) {
                return false;
            }
        }

        for (int i = 0; i < startingXI.size(); i++) {
            Player currentPlayer = (Player) startingXI.getObject(i);

            if (currentPlayer == player) {
                return false;
            }
        }

        bench.insertAtBack(player);
        return true;
    }

    public boolean hasValidStartingXI() {
        if (startingXI.size() != 11) {
            return false;
        }

        int goalkeeperCount = 0;
        int defenderCount = 0;
        int midfielderCount = 0;
        int attackerCount = 0;

        for (int i = 0; i < startingXI.size(); i++) {
            Player currentPlayer = (Player) startingXI.getObject(i);
            String position = currentPlayer.getPosition();

            if (position.equalsIgnoreCase("GK")) {
                goalkeeperCount++;
            } else if (position.equalsIgnoreCase("DEF")) {
                defenderCount++;
            } else if (position.equalsIgnoreCase("MID")) {
                midfielderCount++;
            } else if (position.equalsIgnoreCase("ATT")) {
                attackerCount++;
            }
        }

        return goalkeeperCount == 1 && defenderCount == 4 && midfielderCount == 3 && attackerCount == 3;
    }

    public boolean substitutePlayer(Player outgoingPlayer, Player incomingPlayer) {
        if (outgoingPlayer == null || incomingPlayer == null) {
            return false;
        }

        if (!incomingPlayer.isAvailable()) {
            return false;
        }

        int outgoingPlayerIndex = -1;
        int incomingPlayerIndex = -1;

        for (int i = 0; i < startingXI.size(); i++) {
            Player currentPlayer = (Player) startingXI.getObject(i);

            if (currentPlayer == outgoingPlayer) {
                outgoingPlayerIndex = i;
                break;
            }
        }

        for (int i = 0; i < bench.size(); i++) {
            Player currentPlayer = (Player) bench.getObject(i);

            if (currentPlayer == incomingPlayer) {
                incomingPlayerIndex = i;
                break;
            }
        }

        if (outgoingPlayerIndex == -1 || incomingPlayerIndex == -1) {
            return false;
        }

        startingXI.setObject(outgoingPlayerIndex, incomingPlayer);
        bench.setObject(incomingPlayerIndex, outgoingPlayer);

        outgoingPlayer.setInStartingXI(false);
        incomingPlayer.setInStartingXI(true);

        return true;
    }

    public void reduceSuspensionsForNonPlayingPlayers() {
        for (int i = 0; i < members.size(); i++) {
            Person currentPerson = (Person) members.getObject(i);

            if (currentPerson instanceof Player) {
                Player currentPlayer = (Player) currentPerson;

                if (currentPlayer.getSuspensionMatches() > 0 && !currentPlayer.isInStartingXI()) {
                    currentPlayer.serveSuspension();
                }
            }
        }
    }

    public void clearLineup() {
        for (int i = 0; i < startingXI.size(); i++) {
            Player currentPlayer = (Player) startingXI.getObject(i);

            if (currentPlayer != null) {
                currentPlayer.setInStartingXI(false);
            }
        }

        startingXI.clear();
        bench.clear();
    }

    public void simulateLineup() {
        clearLineup();

        int goalkeeperCount = 0;
        int defenderCount = 0;
        int midfielderCount = 0;
        int attackerCount = 0;

        for (int i = 0; i < members.size(); i++) {
            Person currentPerson = (Person) members.getObject(i);

            if (!(currentPerson instanceof Player)) {
                continue;
            }

            Player currentPlayer = (Player) currentPerson;

            if (!currentPlayer.isAvailable()) {
                continue;
            }

            String position = currentPlayer.getPosition();

            if (position.equalsIgnoreCase("GK") && goalkeeperCount < 1) {
                addToStartingXI(currentPlayer);
                goalkeeperCount++;
            } else if (position.equalsIgnoreCase("DEF") && defenderCount < 4) {
                addToStartingXI(currentPlayer);
                defenderCount++;
            } else if (position.equalsIgnoreCase("MID") && midfielderCount < 3) {
                addToStartingXI(currentPlayer);
                midfielderCount++;
            } else if (position.equalsIgnoreCase("ATT") && attackerCount < 3) {
                addToStartingXI(currentPlayer);
                attackerCount++;
            }

            if (goalkeeperCount == 1 && defenderCount == 4 && midfielderCount == 3 && attackerCount == 3) {
                break;
            }
        }

        for (int i = 0; i < members.size(); i++) {
            Person currentPerson = (Person) members.getObject(i);

            if (!(currentPerson instanceof Player)) {
                continue;
            }

            Player currentPlayer = (Player) currentPerson;

            if (!currentPlayer.isAvailable()) {
                continue;
            }

            boolean alreadyInStartingXI = false;

            for (int j = 0; j < startingXI.size(); j++) {
                Player startingPlayer = (Player) startingXI.getObject(j);

                if (startingPlayer == currentPlayer) {
                    alreadyInStartingXI = true;
                    break;
                }
            }

            if (!alreadyInStartingXI) {
                addToBench(currentPlayer);
            }
        }
    }

    public boolean containsPlayer(Player player) {
        for (int i = 0; i < members.size(); i++) {
            Person currentPerson = (Person) members.getObject(i);

            if (currentPerson == player) {
                return true;
            }
        }

        return false;
    }

    public Player getRandomStartingPlayer() {
        Random randomGenerator = new Random();

        if (startingXI.size() == 0) {
            return null;
        }

        for (int attempt = 0; attempt < 20; attempt++) {
            int randomIndex = randomGenerator.nextInt(startingXI.size());
            Player selectedPlayer = (Player) startingXI.getObject(randomIndex);

            if (selectedPlayer != null && selectedPlayer.isAvailable()) {
                return selectedPlayer;
            }
        }

        return null;
    }

    public Player getRandomBenchPlayer() {
        Random randomGenerator = new Random();

        if (bench.size() == 0) {
            return null;
        }

        for (int attempt = 0; attempt < 20; attempt++) {
            int randomIndex = randomGenerator.nextInt(bench.size());
            Player selectedPlayer = (Player) bench.getObject(randomIndex);

            if (selectedPlayer != null && selectedPlayer.isAvailable()) {
                return selectedPlayer;
            }
        }

        return null;
    }

    public String getSummary() {
        return "Team: " + code + " " + countryName;
    }

    public String getCode() {
        return code;
    }

    public String getCountryName() {
        return countryName;
    }

    public Person getMember(int index) {
        if (index >= 0 && index < members.size()) {
            return (Person) members.getObject(index);
        }

        return null;
    }

    public int getMemberCount() {
        return members.size();
    }

    public int getMaxMembers() {
        return MAX_MEMBERS;
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

    public Player getStartingPlayer(int index) {
        if (index >= 0 && index < startingXI.size()) {
            return (Player) startingXI.getObject(index);
        }

        return null;
    }

    public Player getBenchPlayer(int index) {
        if (index >= 0 && index < bench.size()) {
            return (Player) bench.getObject(index);
        }

        return null;
    }

    public int getStartingCount() {
        return startingXI.size();
    }

    public int getBenchCount() {
        return bench.size();
    }
}

abstract class Person implements Serializable {

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

class Member extends Person implements Serializable{

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

class Player extends Member implements Serializable {

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

    public Player(String fullName, String nationalId, int age, double salary, int contractYearsLeft,
                  int shirtNumber, String position) {
        super(fullName, nationalId, age, salary, contractYearsLeft);

        this.shirtNumber = shirtNumber;
        this.position = position;

        goals = 0;
        goalBonusApplied = false;
        yellowCards = 0;
        redCard = false;
        suspensionMatches = 0;
        injured = false;
        inStartingXI = false;
        currentMatchYellowCards = 0;
    }

    public void addGoal() {
        goals++;
    }

    public void addYellowCard() {
        yellowCards++;
        currentMatchYellowCards++;

        if (currentMatchYellowCards >= 2) {
            redCard = true;
            suspensionMatches = 1;
        }
    }

    public void giveRedCard() {
        redCard = true;
        suspensionMatches = 1;
    }

    public void serveSuspension() {
        if (suspensionMatches > 0) {
            suspensionMatches--;
        }
    }

    public boolean isAvailable() {
        return !injured && !redCard && suspensionMatches == 0;
    }

    public void resetMatchDiscipline() {
        currentMatchYellowCards = 0;
        redCard = false;
    }

    public String getRole() {
        return "Player";
    }

    public int getGoals() {
        return goals;
    }

    public boolean isGoalBonusApplied() {
        return goalBonusApplied;
    }

    public void setGoalBonusApplied(boolean goalBonusApplied) {
        this.goalBonusApplied = goalBonusApplied;
    }

    public String getPosition() {
        return position;
    }

    public int getShirtNumber() {
        return shirtNumber;
    }

    public void setShirtNumber(int shirtNumber) {
        this.shirtNumber = shirtNumber;
    }

    public int getYellowCards() {
        return yellowCards;
    }

    public boolean hasRedCard() {
        return redCard;
    }

    public int getSuspensionMatches() {
        return suspensionMatches;
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
}

class Coach extends Member implements Serializable {

    private int licenseLevel;

    public Coach(String fullName, String nationalId, int age, double salary,
                 int contractYearsLeft, int licenseLevel) {
        super(fullName, nationalId, age, salary, contractYearsLeft);
        this.licenseLevel = licenseLevel;
    }

    public int getLicenseLevel() {
        return licenseLevel;
    }

    public void setLicenseLevel(int licenseLevel) {
        this.licenseLevel = licenseLevel;
    }

    public String getRole() {
        return "Coach";
    }
}

class Goalkeeper extends Player implements Serializable{

    private int saves;

    public Goalkeeper(String fullName, String nationalId, int age, double salary,
                      int contractYearsLeft, int shirtNumber, String position, int saves) {
        super(fullName, nationalId, age, salary, contractYearsLeft, shirtNumber, position);
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