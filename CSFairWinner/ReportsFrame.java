import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class ReportsFrame extends JFrame implements ActionListener {

    private Tournament tournament;

    private JTextArea outputArea;

    private JLabel tournamentLabel;
    private JLabel teamsLabel;
    private JLabel matchesLabel;
    private JLabel leaderLabel;
    private JLabel latestActionLabel;

    private JButton standingsButton;
    private JButton winnerButton;
    private JButton scorersButton;
    private JButton bonusesButton;
    private JButton summaryButton;
    private JButton backButton;

    public ReportsFrame(Tournament tournament) {

        this.tournament = tournament;

        setTitle("Tournament Reports");

        setSize(1400, 850);

        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocation(650, 50);

        setResizable(true);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        Container contentPane = getContentPane();

        contentPane.setLayout(new BorderLayout());
        FootballTheme.styleFrame(contentPane);

        createTopPanel(contentPane);

        createStatusPanel(contentPane);

        createOutputPanel(contentPane);

        createButtonPanel(contentPane);

        updateStatus("System Ready");

        setVisible(true);
    }

    private void createTopPanel(Container contentPane) {

        JPanel topPanel = new JPanel();

        topPanel.setBackground(new Color(35, 55, 90));

        topPanel.setPreferredSize(
                new Dimension(1100, 80)
        );

        JLabel titleLabel =
                new JLabel("TOURNAMENT REPORTS");

        titleLabel.setFont(
                new Font("Arial", Font.BOLD, 24)
        );

        titleLabel.setForeground(Color.WHITE);
        FootballTheme.styleHeaderPanel(topPanel, titleLabel);

        topPanel.add(titleLabel);

        contentPane.add(topPanel, BorderLayout.NORTH);
    }

    private void createStatusPanel(Container contentPane) {

        JPanel statusPanel = new JPanel();

        statusPanel.setPreferredSize(
                new Dimension(320, 250)
        );

        statusPanel.setLayout(
                new GridLayout(5, 1, 5, 5)
        );

        statusPanel.setBorder(
                BorderFactory.createTitledBorder(
                        "Tournament Status"
                )
        );

        Font labelFont =
                new Font("Arial", Font.BOLD, 14);

        tournamentLabel = new JLabel();

        teamsLabel = new JLabel();

        matchesLabel = new JLabel();

        leaderLabel = new JLabel();

        latestActionLabel = new JLabel();

        JLabel[] labels = {
                tournamentLabel,
                teamsLabel,
                matchesLabel,
                leaderLabel,
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

        outputArea.setFont(
                new Font("Monospaced", Font.PLAIN, 13)
        );

        outputArea.setMargin(
                new Insets(10, 10, 10, 10)
        );
        FootballTheme.styleOutputArea(outputArea);

        JScrollPane scrollPane =
                new JScrollPane(outputArea);

        scrollPane.setBorder(
                BorderFactory.createTitledBorder(
                        "Tournament Reports"
                )
        );

        FootballTheme.styleScrollPane(scrollPane);

        contentPane.add(scrollPane, BorderLayout.CENTER);
    }

    private void createButtonPanel(Container contentPane) {

        JPanel buttonPanel = new JPanel();
        FootballTheme.styleButtonPanelPlain(buttonPanel);

        buttonPanel.setLayout(
                new GridLayout(2, 3, 10, 10)
        );

        buttonPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        15,
                        15,
                        15,
                        15
                )
        );

        standingsButton =
                new JButton("Standings");

        winnerButton =
                new JButton("Tournament Winner");

        scorersButton =
                new JButton("Top Scorers");

        bonusesButton =
                new JButton("Apply Bonuses");

        summaryButton =
                new JButton("Tournament Summary");

        backButton =
                new JButton("Back");

        JButton[] buttons = {
                standingsButton,
                winnerButton,
                scorersButton,
                bonusesButton,
                summaryButton,
                backButton
        };

        for (int i = 0; i < buttons.length; i++) {

            buttons[i].setFont(
                    new Font("Arial", Font.BOLD, 13)
            );

            buttons[i].setFocusPainted(false);
            FootballTheme.styleButton(buttons[i]);

            buttons[i].addActionListener(this);

            buttonPanel.add(buttons[i]);
        }

        contentPane.add(buttonPanel, BorderLayout.SOUTH);
    }

    public void actionPerformed(ActionEvent event) {

        if (event.getSource() == standingsButton) {

            showStandings();
        }

        else if (event.getSource() == winnerButton) {

            showWinner();
        }

        else if (event.getSource() == scorersButton) {

            showTopScorers();
        }

        else if (event.getSource() == bonusesButton) {

            applyBonuses();
        }

        else if (event.getSource() == summaryButton) {

            showTournamentSummary();
        }

        else if (event.getSource() == backButton) {

            dispose();
        }
    }

    private void showStandings() {

        outputArea.setText(
                captureConsoleOutput(
                        new Runnable() {

                            public void run() {

                                tournament.printStandings();
                            }
                        }
                )
        );

        updateStatus("Viewing Standings");
    }

    private void showWinner() {

        Team winner =
                tournament.getTournamentWinner();

        outputArea.setText("");

        if (winner == null) {

            outputArea.append(
                    "Tournament winner cannot be determined yet.\n"
            );

        } else {

            outputArea.append(
                    "========================================\n"
            );

            outputArea.append(
                    "         TOURNAMENT WINNER\n"
            );

            outputArea.append(
                    "========================================\n\n"
            );

            outputArea.append(
                    "Winner : "
                            + winner.getCountryName()
                            + "\n"
            );

            outputArea.append(
                    "Code   : "
                            + winner.getCode()
                            + "\n"
            );

            outputArea.append(
                    "Points : "
                            + winner.getPoints()
                            + "\n"
            );
        }

        updateStatus("Viewing Winner");
    }

    private void showTopScorers() {

        outputArea.setText(
                captureConsoleOutput(
                        new Runnable() {

                            public void run() {

                                tournament.printTopScorers();
                            }
                        }
                )
        );

        updateStatus("Viewing Top Scorers");
    }

    private void applyBonuses() {

        tournament.applyGoalBonuses();

        outputArea.setText("");

        outputArea.append(
                "Goal bonuses applied successfully.\n"
        );

        outputArea.append(
                "\nThreshold : "
                        + tournament.getGoalBonusThreshold()
        );

        outputArea.append(
                "\nBonus Amount : "
                        + tournament.getGoalBonusAmount()
        );

        updateStatus("Bonuses Applied");
    }

    private void showTournamentSummary() {

        outputArea.setText("");

        int finishedMatches = 0;

        for (int i = 0;
             i < tournament.getMatchCount();
             i++) {

            Match currentMatch =
                    tournament.searchMatchByIndex(i);

            if (currentMatch != null
                    && currentMatch.isFinished()) {

                finishedMatches++;
            }
        }

        outputArea.append(
                "========================================\n"
        );

        outputArea.append(
                "         TOURNAMENT SUMMARY\n"
        );

        outputArea.append(
                "========================================\n\n"
        );

        outputArea.append(
                "Tournament Name : "
                        + tournament.getName()
                        + "\n"
        );

        outputArea.append(
                "Start Date      : "
                        + tournament.getStartDate()
                        + "\n"
        );

        outputArea.append(
                "Teams            : "
                        + tournament.getTeamCount()
                        + "\n"
        );

        outputArea.append(
                "Matches           : "
                        + tournament.getMatchCount()
                        + "\n"
        );

        outputArea.append(
                "Finished Matches  : "
                        + finishedMatches
                        + "\n"
        );

        outputArea.append(
                "Stadiums          : "
                        + tournament.getStadiumCount()
                        + "\n"
        );

        updateStatus("Viewing Summary");
    }

    private String captureConsoleOutput(Runnable runnable) {

        PrintStream originalOutput =
                System.out;

        ByteArrayOutputStream byteOutput =
                new ByteArrayOutputStream();

        PrintStream temporaryOutput =
                new PrintStream(byteOutput);

        System.setOut(temporaryOutput);

        runnable.run();

        System.out.flush();

        System.setOut(originalOutput);

        return byteOutput.toString();
    }

    private void updateStatus(String latestAction) {

        tournamentLabel.setText(
                "Tournament : "
                        + tournament.getName()
        );

        teamsLabel.setText(
                "Teams : "
                        + tournament.getTeamCount()
        );

        matchesLabel.setText(
                "Matches : "
                        + tournament.getMatchCount()
        );

        Team leader =
                tournament.getTournamentWinner();

        if (leader == null
                && tournament.getTeamCount() > 0) {

            leader =
                    (Team) tournament.getTeams()
                            .getObject(0);
        }

        if (leader != null) {

            leaderLabel.setText(
                    "Leader : "
                            + leader.getCountryName()
            );

        } else {

            leaderLabel.setText(
                    "Leader : N/A"
            );
        }

        latestActionLabel.setText(
                "Latest Action : "
                        + latestAction
        );
    }
}