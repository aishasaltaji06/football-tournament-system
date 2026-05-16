import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalTime;

public class MatchManagementFrame extends JFrame implements ActionListener {

    private Tournament tournament;

    private JTextArea outputArea;

    private JLabel tournamentNameLabel;
    private JLabel totalMatchesLabel;
    private JLabel playedMatchesLabel;
    private JLabel upcomingMatchesLabel;
    private JLabel latestActionLabel;

    private JButton generateButton;
    private JButton upcomingButton;
    private JButton allMatchesButton;
    private JButton lineupButton;
    private JButton randomButton;
    private JButton manualButton;
    private JButton summaryButton;
    private JButton clearButton;
    private JButton backButton;

    public MatchManagementFrame(Tournament tournament) {

        this.tournament = tournament;

        setTitle("Match Management");
        setSize(1400, 850);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocation(720, 70);
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

        outputArea.setText("\n\n\n        Welcome to Match Management.");

        setVisible(true);
    }

    private void createTitlePanel(Container contentPane) {

        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(20, 45, 85));
        titlePanel.setPreferredSize(new Dimension(1050, 80));

        JLabel titleLabel = new JLabel("MATCH MANAGEMENT");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        FootballTheme.styleHeaderPanel(titlePanel, titleLabel);

        titlePanel.add(titleLabel);
        contentPane.add(titlePanel, BorderLayout.NORTH);
    }

    private void createStatusPanel(Container contentPane) {

        JPanel statusPanel = new JPanel();
        statusPanel.setLayout(new GridLayout(5, 1, 5, 5));
        statusPanel.setPreferredSize(new Dimension(290, 180));

        statusPanel.setBorder(
                BorderFactory.createTitledBorder("Match Status")
        );

        Font labelFont = new Font("Arial", Font.BOLD, 13);

        tournamentNameLabel = new JLabel();
        totalMatchesLabel = new JLabel();
        playedMatchesLabel = new JLabel();
        upcomingMatchesLabel = new JLabel();
        latestActionLabel = new JLabel();

        JLabel[] labels = {
                tournamentNameLabel,
                totalMatchesLabel,
                playedMatchesLabel,
                upcomingMatchesLabel,
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
                BorderFactory.createTitledBorder("Matches & Activity")
        );

        FootballTheme.styleScrollPane(scrollPane);

        contentPane.add(scrollPane, BorderLayout.CENTER);
    }

    private void createButtonPanel(Container contentPane) {

        JPanel buttonPanel = new JPanel();
        FootballTheme.styleButtonPanelPlain(buttonPanel);
        buttonPanel.setLayout(new GridLayout(3, 3, 10, 10));

        buttonPanel.setBorder(
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        );

        generateButton = new JButton("Generate Matches");
        upcomingButton = new JButton("Upcoming Matches");
        allMatchesButton = new JButton("All Matches");
        lineupButton = new JButton("Prepare Lineups");
        randomButton = new JButton("Simulate Match");
        manualButton = new JButton("Play Manual");
        summaryButton = new JButton("Match Summary");
        clearButton = new JButton("Clear Matches");
        backButton = new JButton("Back");

        JButton[] buttons = {
                generateButton,
                upcomingButton,
                allMatchesButton,
                lineupButton,
                randomButton,
                manualButton,
                summaryButton,
                clearButton,
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

        if (event.getSource() == generateButton) {
            generateMatches();
        }

        else if (event.getSource() == upcomingButton) {
            showUpcomingMatches();
        }

        else if (event.getSource() == allMatchesButton) {
            showAllMatches();
        }

        else if (event.getSource() == lineupButton) {
            prepareLineups();
        }

        else if (event.getSource() == randomButton) {
            playRandomMatch();
        }

        else if (event.getSource() == manualButton) {

            Match selectedMatch = askForMatch();

            if (selectedMatch == null) {

                outputArea.setText(
                        "Match not found.\n"
                );

                updateStatus("Match Not Found");

                return;
            }

            new ManualMatchFrame(selectedMatch);

            updateStatus("Manual Match Opened");
        }

        else if (event.getSource() == summaryButton) {
            showMatchSummary();
        }

        else if (event.getSource() == clearButton) {
            clearMatches();
        }

        else if (event.getSource() == backButton) {
            dispose();
        }
    }

    private void updateStatus(String action) {

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

        tournamentNameLabel.setText(
                "Tournament : " + tournament.getName()
        );

        totalMatchesLabel.setText(
                "Total Matches : " + totalMatches
        );

        playedMatchesLabel.setText(
                "Played Matches : " + playedMatches
        );

        upcomingMatchesLabel.setText(
                "Upcoming Matches : " + upcomingMatches
        );

        latestActionLabel.setText(
                "Latest Action : " + action
        );

        setTitle(
                "Match Management (" + totalMatches + " Matches)"
        );
    }

    private void addLog(String message) {

        outputArea.append(
                "[" + LocalTime.now().withNano(0) + "] "
                        + message + "\n"
        );
    }

    private void generateMatches() {

        outputArea.setText("");

        try {

            tournament.generateAllMatchesOnce();

            outputArea.append("Matches generated successfully.\n\n");

            showUpcomingMatches();

            updateStatus("Matches Generated");
        }

        catch (InvalidDateFormatException e) {

            outputArea.append(e.getMessage() + "\n");

            updateStatus("Invalid Date");
        }
    }

    private void showUpcomingMatches() {

        outputArea.setText("");

        outputArea.append("==============================================\n");
        outputArea.append("              UPCOMING MATCHES                \n");
        outputArea.append("==============================================\n\n");

        if (tournament.getMatchCount() == 0) {

            outputArea.append("No matches have been generated yet.\n");

            updateStatus("No Matches");

            return;
        }

        boolean foundUpcoming = false;

        int matchNumber = 1;

        for (int i = 0; i < tournament.getMatchCount(); i++) {

            Match currentMatch = tournament.searchMatchByIndex(i);

            if (currentMatch != null && !currentMatch.isFinished()) {

                foundUpcoming = true;

                outputArea.append("----------------------------------------------\n");
                outputArea.append("MATCH #" + matchNumber + "\n");
                outputArea.append("----------------------------------------------\n");
                outputArea.append("Match Name : " + currentMatch.getMatchName() + "\n");
                outputArea.append("Home Team  : " + currentMatch.getHome().getCountryName() + "\n");
                outputArea.append("Away Team  : " + currentMatch.getAway().getCountryName() + "\n");

                if (currentMatch.getStadium() != null) {
                    outputArea.append("Stadium    : " + currentMatch.getStadium().getName() + "\n");
                } else {
                    outputArea.append("Stadium    : TBD\n");
                }

                if (currentMatch.getDate() != null) {
                    outputArea.append("Date       : " + currentMatch.getDate() + "\n");
                } else {
                    outputArea.append("Date       : TBD\n");
                }

                if (currentMatch.getTime() != null) {
                    outputArea.append("Time       : " + currentMatch.getTime() + "\n");
                } else {
                    outputArea.append("Time       : TBD\n");
                }

                outputArea.append("Status     : Upcoming\n");
                outputArea.append("----------------------------------------------\n\n");

                matchNumber++;
            }
        }

        if (!foundUpcoming) {

            outputArea.append(
                    "No upcoming matches. All matches may already be finished.\n"
            );
        }

        updateStatus("Viewing Upcoming");
    }

    private void showAllMatches() {

        outputArea.setText("");

        outputArea.append("==============================================\n");
        outputArea.append("                 ALL MATCHES                  \n");
        outputArea.append("==============================================\n\n");

        if (tournament.getMatchCount() == 0) {

            outputArea.append("No matches generated yet.\n");

            updateStatus("No Matches");

            return;
        }

        for (int i = 0; i < tournament.getMatchCount(); i++) {

            Match currentMatch =
                    tournament.searchMatchByIndex(i);

            if (currentMatch != null) {

                outputArea.append("----------------------------------------------\n");

                outputArea.append(
                        "Match Name : "
                                + currentMatch.getMatchName()
                                + "\n"
                );

                outputArea.append(
                        "Home Team  : "
                                + currentMatch.getHome().getCountryName()
                                + "\n"
                );

                outputArea.append(
                        "Away Team  : "
                                + currentMatch.getAway().getCountryName()
                                + "\n"
                );

                if (currentMatch.isFinished()) {

                    outputArea.append(
                            "Score      : "
                                    + currentMatch.getHomeGoals()
                                    + " - "
                                    + currentMatch.getAwayGoals()
                                    + "\n"
                    );

                    outputArea.append(
                            "Status     : Finished\n"
                    );

                } else {

                    outputArea.append(
                            "Status     : Upcoming\n"
                    );
                }

                outputArea.append("----------------------------------------------\n\n");
            }
        }

        updateStatus("Viewing All Matches");
    }

    private void prepareLineups() {

        Match selectedMatch = askForMatch();

        if (selectedMatch == null) {

            outputArea.setText("Match not found.\n");

            updateStatus("Match Not Found");

            return;
        }

        new LineupManagementFrame(selectedMatch);

        updateStatus("Lineup Frame Opened");
    }

    private void playRandomMatch() {

        Match selectedMatch = askForMatch();

        if (selectedMatch == null) {

            outputArea.setText("Match not found.\n");

            updateStatus("Match Not Found");

            return;
        }

        if (selectedMatch.isFinished()) {

            outputArea.setText("This match is already finished.\n");

            updateStatus("Already Finished");

            return;
        }

        selectedMatch.getHome().simulateLineup();
        selectedMatch.getAway().simulateLineup();

        selectedMatch.playRandomMatch();

        outputArea.setText("");

        outputArea.append("==============================================\n");
        outputArea.append("              MATCH SIMULATED                  \n");
        outputArea.append("==============================================\n");

        outputArea.append(selectedMatch.getSummary());

        updateStatus("Match Simulated");
    }

    private void showMatchSummary() {

        Match selectedMatch = askForMatch();

        if (selectedMatch == null) {

            outputArea.setText("Match not found.\n");

            updateStatus("Match Not Found");

            return;
        }

        outputArea.setText("");

        outputArea.append("==============================================\n");
        outputArea.append("                MATCH SUMMARY                 \n");
        outputArea.append("==============================================\n");

        outputArea.append(selectedMatch.getSummary());

        updateStatus("Viewing Summary");
    }

    private void clearMatches() {

        int confirmation =
                JOptionPane.showConfirmDialog(
                        this,
                        "Are you sure you want to clear all matches?",
                        "Clear Matches",
                        JOptionPane.YES_NO_OPTION
                );

        if (confirmation != JOptionPane.YES_OPTION) {
            return;
        }

        tournament.clearMatches();

        outputArea.setText("");

        outputArea.append("All matches cleared.\n");

        updateStatus("Matches Cleared");
    }

    private Match askForMatch() {

        String matchName =
                JOptionPane.showInputDialog(
                        this,
                        "Enter match name:"
                );

        if (matchName == null || matchName.trim().equals("")) {
            return null;
        }

        return tournament.searchMatch(matchName);
    }
}