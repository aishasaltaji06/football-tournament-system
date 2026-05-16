import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalTime;

public class LineupManagementFrame extends JFrame implements ActionListener {

    private Match match;
    private Team homeTeam;
    private Team awayTeam;

    private JTextArea outputArea;

    private JLabel matchNameLabel;
    private JLabel homeTeamLabel;
    private JLabel awayTeamLabel;
    private JLabel homeXILabel;
    private JLabel awayXILabel;
    private JLabel homeBenchLabel;
    private JLabel awayBenchLabel;
    private JLabel latestActionLabel;

    private JButton addHomeStarterButton;
    private JButton addHomeBenchButton;
    private JButton validateHomeButton;
    private JButton addAwayStarterButton;
    private JButton addAwayBenchButton;
    private JButton validateAwayButton;
    private JButton validateBothButton;
    private JButton autoBuildButton;
    private JButton showLineupsButton;
    private JButton clearLineupsButton;
    private JButton backButton;

    public LineupManagementFrame(Match match) {

        this.match = match;
        this.homeTeam = match.getHome();
        this.awayTeam = match.getAway();

        setTitle("Lineup Management - " + match.getMatchName());
        setSize(1400, 850);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocation(680, 50);
        setResizable(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());
        FootballTheme.styleFrame(contentPane);

        createTitlePanel(contentPane);
        createStatusPanel(contentPane);
        createOutputPanel(contentPane);
        createButtonPanel(contentPane);

        updateStatus("System Ready");
        showLineups();

        setVisible(true);
    }

    private void createTitlePanel(Container contentPane) {

        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(20, 45, 85));
        titlePanel.setPreferredSize(new Dimension(1100, 80));

        JLabel titleLabel = new JLabel("LINEUP MANAGEMENT");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        FootballTheme.styleHeaderPanel(titlePanel, titleLabel);

        titlePanel.add(titleLabel);
        contentPane.add(titlePanel, BorderLayout.NORTH);
    }

    private void createStatusPanel(Container contentPane) {

        JPanel statusPanel = new JPanel();
        statusPanel.setLayout(new GridLayout(8, 1, 5, 5));
        statusPanel.setPreferredSize(new Dimension(310, 250));

        statusPanel.setBorder(
                BorderFactory.createTitledBorder("Lineup Status")
        );

        Font labelFont = new Font("Arial", Font.BOLD, 13);

        matchNameLabel = new JLabel();
        homeTeamLabel = new JLabel();
        awayTeamLabel = new JLabel();
        homeXILabel = new JLabel();
        awayXILabel = new JLabel();
        homeBenchLabel = new JLabel();
        awayBenchLabel = new JLabel();
        latestActionLabel = new JLabel();

        JLabel[] labels = {
                matchNameLabel,
                homeTeamLabel,
                awayTeamLabel,
                homeXILabel,
                awayXILabel,
                homeBenchLabel,
                awayBenchLabel,
                latestActionLabel
        };

        for (int i = 0; i < labels.length; i++) {
            labels[i].setFont(labelFont);
            FootballTheme.styleLabel(labels[i]);
            statusPanel.add(labels[i]);
        }

        FootballTheme.styleSimplePanel(statusPanel);

        contentPane.add(statusPanel, BorderLayout.WEST);
    }

    private void createOutputPanel(Container contentPane) {

        outputArea = new JTextArea();
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Courier", Font.PLAIN, 13));
        outputArea.setMargin(new Insets(10, 10, 10, 10));
        FootballTheme.styleOutputArea(outputArea);

        JScrollPane scrollPane = new JScrollPane(outputArea);

        scrollPane.setBorder(
                BorderFactory.createTitledBorder("Lineups & Activity")
        );

        FootballTheme.styleScrollPane(scrollPane);

        contentPane.add(scrollPane, BorderLayout.CENTER);
    }

    private void createButtonPanel(Container contentPane) {

        JPanel buttonPanel = new JPanel();
        FootballTheme.styleButtonPanelPlain(buttonPanel);
        buttonPanel.setLayout(new GridLayout(3, 4, 10, 10));

        buttonPanel.setBorder(
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        );

        addHomeStarterButton = new JButton("Add Home Starter");
        addHomeBenchButton = new JButton("Add Home Bench");
        validateHomeButton = new JButton("Validate Home");

        addAwayStarterButton = new JButton("Add Away Starter");
        addAwayBenchButton = new JButton("Add Away Bench");
        validateAwayButton = new JButton("Validate Away");

        validateBothButton = new JButton("Validate Both");
        autoBuildButton = new JButton("Auto Build");
        showLineupsButton = new JButton("Show Lineups");
        clearLineupsButton = new JButton("Clear Lineups");
        backButton = new JButton("Back");

        JButton[] buttons = {
                addHomeStarterButton,
                addHomeBenchButton,
                validateHomeButton,
                addAwayStarterButton,
                addAwayBenchButton,
                validateAwayButton,
                validateBothButton,
                autoBuildButton,
                showLineupsButton,
                clearLineupsButton,
                backButton
        };

        for (int i = 0; i < buttons.length; i++) {
            buttons[i].setFont(new Font("Arial", Font.BOLD, 13));
            buttons[i].setFocusPainted(false);
            FootballTheme.styleButton(buttons[i]);
            buttons[i].addActionListener(this);
            buttonPanel.add(buttons[i]);
        }

        contentPane.add(buttonPanel, BorderLayout.SOUTH);
    }

    public void actionPerformed(ActionEvent event) {

        if (event.getSource() == addHomeStarterButton) {
            addPlayerToStartingXI(homeTeam, "Home");
        }

        else if (event.getSource() == addHomeBenchButton) {
            addPlayerToBench(homeTeam, "Home");
        }

        else if (event.getSource() == validateHomeButton) {
            validateTeam(homeTeam, "Home");
        }

        else if (event.getSource() == addAwayStarterButton) {
            addPlayerToStartingXI(awayTeam, "Away");
        }

        else if (event.getSource() == addAwayBenchButton) {
            addPlayerToBench(awayTeam, "Away");
        }

        else if (event.getSource() == validateAwayButton) {
            validateTeam(awayTeam, "Away");
        }

        else if (event.getSource() == validateBothButton) {
            validateBoth();
        }

        else if (event.getSource() == autoBuildButton) {
            autoBuildBoth();
        }

        else if (event.getSource() == showLineupsButton) {
            showLineups();
        }

        else if (event.getSource() == clearLineupsButton) {
            clearLineups();
        }

        else if (event.getSource() == backButton) {
            dispose();
        }
    }

    private void updateStatus(String action) {

        matchNameLabel.setText("Match : " + match.getMatchName());
        homeTeamLabel.setText("Home : " + homeTeam.getCountryName());
        awayTeamLabel.setText("Away : " + awayTeam.getCountryName());

        homeXILabel.setText("Home XI : " + homeTeam.getStartingCount() + " / 11");
        awayXILabel.setText("Away XI : " + awayTeam.getStartingCount() + " / 11");

        homeBenchLabel.setText("Home Bench : " + homeTeam.getBenchCount() + " / 15");
        awayBenchLabel.setText("Away Bench : " + awayTeam.getBenchCount() + " / 15");

        latestActionLabel.setText("Latest Action : " + action);
    }

    private void addLog(String message) {

        outputArea.append(
                "[" + LocalTime.now().withNano(0) + "] "
                        + message + "\n"
        );
    }

    private void addPlayerToStartingXI(Team team, String side) {

        Player selectedPlayer = choosePlayer(team);

        if (selectedPlayer == null) {
            return;
        }

        if (team.addToStartingXI(selectedPlayer)) {
            addLog(selectedPlayer.getFullName() + " added to " + side + " Starting XI.");
            updateStatus(side + " Starter Added");
        } else {
            addLog("Could not add " + selectedPlayer.getFullName() + " to " + side + " Starting XI.");
            addLog("Player may be unavailable, already selected, or lineup may be full.");
            updateStatus("Add Failed");
        }

        showLineups();
    }

    private void addPlayerToBench(Team team, String side) {

        Player selectedPlayer = choosePlayer(team);

        if (selectedPlayer == null) {
            return;
        }

        if (team.addToBench(selectedPlayer)) {
            addLog(selectedPlayer.getFullName() + " added to " + side + " Bench.");
            updateStatus(side + " Bench Added");
        } else {
            addLog("Could not add " + selectedPlayer.getFullName() + " to " + side + " Bench.");
            addLog("Player may be unavailable, already selected, or bench may be full.");
            updateStatus("Add Failed");
        }

        showLineups();
    }

    private Player choosePlayer(Team team) {

        int playerCount = 0;

        for (int i = 0; i < team.getMemberCount(); i++) {
            Person currentMember = team.getMember(i);

            if (currentMember instanceof Player) {
                playerCount++;
            }
        }

        if (playerCount == 0) {
            JOptionPane.showMessageDialog(
                    this,
                    "This team has no players."
            );
            return null;
        }

        Player[] players = new Player[playerCount];
        String[] options = new String[playerCount];

        int index = 0;

        for (int i = 0; i < team.getMemberCount(); i++) {
            Person currentMember = team.getMember(i);

            if (currentMember instanceof Player) {
                Player currentPlayer = (Player) currentMember;

                players[index] = currentPlayer;

                options[index] =
                        currentPlayer.getFullName()
                                + " | "
                                + currentPlayer.getPosition()
                                + " | ID: "
                                + currentPlayer.getNationalId();

                index++;
            }
        }

        String selectedOption =
                (String) JOptionPane.showInputDialog(
                        this,
                        "Choose player:",
                        "Select Player",
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        options,
                        options[0]
                );

        if (selectedOption == null) {
            return null;
        }

        for (int i = 0; i < options.length; i++) {
            if (selectedOption.equals(options[i])) {
                return players[i];
            }
        }

        return null;
    }

    private void validateTeam(Team team, String side) {

        outputArea.setText("");

        outputArea.append("==============================================\n");
        outputArea.append("              " + side.toUpperCase() + " LINEUP VALIDATION\n");
        outputArea.append("==============================================\n\n");

        outputArea.append("Team : " + team.getCountryName() + "\n");
        outputArea.append("Starting XI : " + team.getStartingCount() + " / 11\n");
        outputArea.append("Bench       : " + team.getBenchCount() + " / 15\n\n");

        printStartingXI(team);
        outputArea.append("\n");

        if (team.hasValidStartingXI()) {
            outputArea.append(side + " lineup is valid.\n");
            updateStatus(side + " Valid");
        } else {
            outputArea.append(side + " lineup is NOT valid.\n");
            outputArea.append("Required formation: 1 GK, 4 DEF, 3 MID, 3 ATT.\n");
            updateStatus(side + " Invalid");
        }
    }

    private void validateBoth() {

        outputArea.setText("");

        outputArea.append("==============================================\n");
        outputArea.append("              BOTH LINEUPS VALIDATION\n");
        outputArea.append("==============================================\n\n");

        outputArea.append("Home Team : " + homeTeam.getCountryName() + "\n");
        outputArea.append("Home XI   : " + homeTeam.getStartingCount() + " / 11\n");

        if (homeTeam.hasValidStartingXI()) {
            outputArea.append("Home lineup is valid.\n\n");
        } else {
            outputArea.append("Home lineup is NOT valid.\n\n");
        }

        outputArea.append("Away Team : " + awayTeam.getCountryName() + "\n");
        outputArea.append("Away XI   : " + awayTeam.getStartingCount() + " / 11\n");

        if (awayTeam.hasValidStartingXI()) {
            outputArea.append("Away lineup is valid.\n\n");
        } else {
            outputArea.append("Away lineup is NOT valid.\n\n");
        }

        if (homeTeam.hasValidStartingXI() && awayTeam.hasValidStartingXI()) {
            outputArea.append("Both lineups are valid.\n");
            updateStatus("Both Valid");
        } else {
            outputArea.append("One or both lineups are invalid.\n");
            outputArea.append("Required formation: 1 GK, 4 DEF, 3 MID, 3 ATT.\n");
            updateStatus("Validation Failed");
        }
    }

    private void autoBuildBoth() {

        homeTeam.simulateLineup();
        awayTeam.simulateLineup();

        outputArea.setText("");

        outputArea.append("==============================================\n");
        outputArea.append("              AUTO BUILD LINEUPS\n");
        outputArea.append("==============================================\n\n");

        outputArea.append("Both lineups were built automatically.\n\n");

        outputArea.append("Home Team : " + homeTeam.getCountryName() + "\n");
        outputArea.append("Home XI   : " + homeTeam.getStartingCount() + " / 11\n");
        outputArea.append("Home Bench: " + homeTeam.getBenchCount() + " / 15\n\n");

        outputArea.append("Away Team : " + awayTeam.getCountryName() + "\n");
        outputArea.append("Away XI   : " + awayTeam.getStartingCount() + " / 11\n");
        outputArea.append("Away Bench: " + awayTeam.getBenchCount() + " / 15\n\n");

        if (homeTeam.hasValidStartingXI() && awayTeam.hasValidStartingXI()) {
            outputArea.append("Both lineups are valid.\n");
        } else {
            outputArea.append("One or both lineups are invalid.\n");
            outputArea.append("Required formation: 1 GK, 4 DEF, 3 MID, 3 ATT.\n");
        }

        outputArea.append("\n");
        printBothLineups();

        updateStatus("Auto Built");
    }

    private void showLineups() {

        outputArea.setText("");

        outputArea.append("==============================================\n");
        outputArea.append("                  CURRENT LINEUPS\n");
        outputArea.append("==============================================\n\n");

        outputArea.append("Match : " + match.getMatchName() + "\n\n");

        outputArea.append("HOME TEAM: " + homeTeam.getCountryName() + "\n");
        outputArea.append("----------------------------------------------\n");
        outputArea.append("Starting XI: " + homeTeam.getStartingCount() + " / 11\n");
        outputArea.append("Bench      : " + homeTeam.getBenchCount() + " / 15\n\n");

        printStartingXI(homeTeam);
        outputArea.append("\n");
        printBench(homeTeam);

        outputArea.append("\n\n");

        outputArea.append("AWAY TEAM: " + awayTeam.getCountryName() + "\n");
        outputArea.append("----------------------------------------------\n");
        outputArea.append("Starting XI: " + awayTeam.getStartingCount() + " / 11\n");
        outputArea.append("Bench      : " + awayTeam.getBenchCount() + " / 15\n\n");

        printStartingXI(awayTeam);
        outputArea.append("\n");
        printBench(awayTeam);

        updateStatus("Viewing Lineups");
    }

    private void printBothLineups() {

        outputArea.append("==============================================\n");
        outputArea.append("HOME STARTING XI\n");
        outputArea.append("==============================================\n");
        printStartingXI(homeTeam);

        outputArea.append("\n==============================================\n");
        outputArea.append("AWAY STARTING XI\n");
        outputArea.append("==============================================\n");
        printStartingXI(awayTeam);
    }

    private void printStartingXI(Team team) {

        outputArea.append("Starting XI:\n");

        if (team.getStartingCount() == 0) {
            outputArea.append("No starting players selected.\n");
            return;
        }

        for (int i = 0; i < team.getStartingCount(); i++) {

            Player player = team.getStartingPlayer(i);

            if (player != null) {
                outputArea.append(
                        "- "
                                + player.getPosition()
                                + " | "
                                + player.getFullName()
                                + " | ID: "
                                + player.getNationalId()
                                + "\n"
                );
            }
        }
    }

    private void printBench(Team team) {

        outputArea.append("Bench:\n");

        if (team.getBenchCount() == 0) {
            outputArea.append("No bench players selected.\n");
            return;
        }

        for (int i = 0; i < team.getBenchCount(); i++) {

            Player player = team.getBenchPlayer(i);

            if (player != null) {
                outputArea.append(
                        "- "
                                + player.getPosition()
                                + " | "
                                + player.getFullName()
                                + " | ID: "
                                + player.getNationalId()
                                + "\n"
                );
            }
        }
    }

    private void clearLineups() {

        int confirmation =
                JOptionPane.showConfirmDialog(
                        this,
                        "Clear both teams' lineups for this match?",
                        "Clear Lineups",
                        JOptionPane.YES_NO_OPTION
                );

        if (confirmation != JOptionPane.YES_OPTION) {
            return;
        }

        homeTeam.clearLineup();
        awayTeam.clearLineup();

        addLog("Both lineups were cleared.");
        updateStatus("Lineups Cleared");
        showLineups();
    }
}