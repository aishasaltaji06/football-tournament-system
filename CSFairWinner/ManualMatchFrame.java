import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalTime;

public class ManualMatchFrame extends JFrame implements ActionListener {

    private Match match;

    private JTextArea outputArea;

    private JLabel matchLabel;
    private JLabel scoreLabel;
    private JLabel statusLabel;
    private JLabel latestActionLabel;

    private JButton homeGoalButton;
    private JButton awayGoalButton;

    private JButton yellowCardButton;
    private JButton redCardButton;

    private JButton injuryButton;
    private JButton substitutionButton;

    private JButton finishButton;
    private JButton summaryButton;
    private JButton backButton;

    public ManualMatchFrame(Match match) {

        this.match = match;

        setTitle("Manual Match - " + match.getMatchName());

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

        topPanel.setBackground(new Color(25, 45, 80));

        topPanel.setPreferredSize(new Dimension(1100, 80));

        JLabel titleLabel =
                new JLabel("MANUAL MATCH MANAGEMENT");

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

        statusPanel.setPreferredSize(new Dimension(320, 250));

        statusPanel.setLayout(
                new GridLayout(4, 1, 5, 5)
        );

        statusPanel.setBorder(
                BorderFactory.createTitledBorder("Match Status")
        );

        Font labelFont =
                new Font("Arial", Font.BOLD, 14);

        matchLabel = new JLabel();

        scoreLabel = new JLabel();

        statusLabel = new JLabel();

        latestActionLabel = new JLabel();

        JLabel[] labels = {
                matchLabel,
                scoreLabel,
                statusLabel,
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
                new Font("Courier", Font.PLAIN, 13)
        );

        outputArea.setMargin(
                new Insets(10, 10, 10, 10)
        );
        FootballTheme.styleOutputArea(outputArea);

        JScrollPane scrollPane =
                new JScrollPane(outputArea);

        scrollPane.setBorder(
                BorderFactory.createTitledBorder("Live Match Events")
        );

        FootballTheme.styleScrollPane(scrollPane);

        contentPane.add(scrollPane, BorderLayout.CENTER);
    }

    private void createButtonPanel(Container contentPane) {

        JPanel buttonPanel = new JPanel();
        FootballTheme.styleButtonPanelPlain(buttonPanel);

        buttonPanel.setLayout(
                new GridLayout(3, 3, 10, 10)
        );

        buttonPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        15,
                        15,
                        15,
                        15
                )
        );

        homeGoalButton =
                new JButton("Home Goal");

        awayGoalButton =
                new JButton("Away Goal");

        yellowCardButton =
                new JButton("Yellow Card");

        redCardButton =
                new JButton("Red Card");

        injuryButton =
                new JButton("Add Injury");

        substitutionButton =
                new JButton("Substitution");

        finishButton =
                new JButton("Finish Match");

        summaryButton =
                new JButton("Show Summary");

        backButton =
                new JButton("Back");

        JButton[] buttons = {
                homeGoalButton,
                awayGoalButton,
                yellowCardButton,
                redCardButton,
                injuryButton,
                substitutionButton,
                finishButton,
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

        if (match.isFinished()
                && event.getSource() != summaryButton
                && event.getSource() != backButton) {

            JOptionPane.showMessageDialog(
                    this,
                    "This match is already finished."
            );

            return;
        }

        if (event.getSource() == homeGoalButton) {

            addGoal(match.getHome(), true);
        }

        else if (event.getSource() == awayGoalButton) {

            addGoal(match.getAway(), false);
        }

        else if (event.getSource() == yellowCardButton) {

            giveYellowCard();
        }

        else if (event.getSource() == redCardButton) {

            giveRedCard();
        }

        else if (event.getSource() == injuryButton) {

            addInjury();
        }

        else if (event.getSource() == substitutionButton) {

            makeSubstitution();
        }

        else if (event.getSource() == finishButton) {

            finishMatch();
        }

        else if (event.getSource() == summaryButton) {

            outputArea.setText(
                    match.getSummary()
            );

            updateStatus("Viewing Summary");
        }

        else if (event.getSource() == backButton) {

            dispose();
        }
    }

    private void addGoal(Team team, boolean homeTeam) {

        Player selectedPlayer =
                choosePlayer(team);

        if (selectedPlayer == null) {
            return;
        }

        selectedPlayer.addGoal();

        if (homeTeam) {

            match.setHomeGoals(
                    match.getHomeGoals() + 1
            );

        } else {

            match.setAwayGoals(
                    match.getAwayGoals() + 1
            );
        }

        addLog(
                "GOAL - "
                        + selectedPlayer.getFullName()
                        + " scored for "
                        + team.getCountryName()
        );

        updateStatus("Goal Recorded");
    }

    private void giveYellowCard() {

        Team selectedTeam =
                chooseTeam();

        if (selectedTeam == null) {
            return;
        }

        Player selectedPlayer =
                choosePlayer(selectedTeam);

        if (selectedPlayer == null) {
            return;
        }

        boolean alreadyHadRed =
                selectedPlayer.hasRedCard();

        selectedPlayer.addYellowCard();

        if (selectedTeam == match.getHome()) {

            match.setHomeYellowCards(
                    match.getHomeYellowCards() + 1
            );

            if (!alreadyHadRed
                    && selectedPlayer.hasRedCard()) {

                match.setHomeRedCards(
                        match.getHomeRedCards() + 1
                );
            }

        } else {

            match.setAwayYellowCards(
                    match.getAwayYellowCards() + 1
            );

            if (!alreadyHadRed
                    && selectedPlayer.hasRedCard()) {

                match.setAwayRedCards(
                        match.getAwayRedCards() + 1
                );
            }
        }

        addLog(
                "YELLOW CARD - "
                        + selectedPlayer.getFullName()
        );

        updateStatus("Yellow Card");
    }

    private void giveRedCard() {

        Team selectedTeam =
                chooseTeam();

        if (selectedTeam == null) {
            return;
        }

        Player selectedPlayer =
                choosePlayer(selectedTeam);

        if (selectedPlayer == null) {
            return;
        }

        if (selectedPlayer.hasRedCard()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Player already has red card."
            );

            return;
        }

        selectedPlayer.giveRedCard();

        if (selectedTeam == match.getHome()) {

            match.setHomeRedCards(
                    match.getHomeRedCards() + 1
            );

        } else {

            match.setAwayRedCards(
                    match.getAwayRedCards() + 1
            );
        }

        addLog(
                "RED CARD - "
                        + selectedPlayer.getFullName()
        );

        updateStatus("Red Card");
    }

    private void addInjury() {

        Team selectedTeam =
                chooseTeam();

        if (selectedTeam == null) {
            return;
        }

        Player selectedPlayer =
                choosePlayer(selectedTeam);

        if (selectedPlayer == null) {
            return;
        }

        selectedPlayer.setInjured(true);

        addLog(
                "INJURY - "
                        + selectedPlayer.getFullName()
        );

        updateStatus("Injury Added");
    }

    private void makeSubstitution() {

        Team selectedTeam =
                chooseTeam();

        if (selectedTeam == null) {
            return;
        }

        Player outgoingPlayer =
                chooseStartingPlayer(selectedTeam);

        if (outgoingPlayer == null) {
            return;
        }

        Player incomingPlayer =
                chooseBenchPlayer(selectedTeam);

        if (incomingPlayer == null) {
            return;
        }

        boolean success =
                selectedTeam.substitutePlayer(
                        outgoingPlayer,
                        incomingPlayer
                );

        if (!success) {

            JOptionPane.showMessageDialog(
                    this,
                    "Could not complete substitution."
            );

            return;
        }

        if (selectedTeam == match.getHome()) {

            match.setHomeSubstitutions(
                    match.getHomeSubstitutions() + 1
            );

        } else {

            match.setAwaySubstitutions(
                    match.getAwaySubstitutions() + 1
            );
        }

        addLog(
                "SUBSTITUTION - "
                        + outgoingPlayer.getFullName()
                        + " OUT | "
                        + incomingPlayer.getFullName()
                        + " IN"
        );

        updateStatus("Substitution");
    }

    private void finishMatch() {

        Team homeTeam =
                match.getHome();

        Team awayTeam =
                match.getAway();

        homeTeam.updateStats(
                match.getHomeGoals(),
                match.getAwayGoals()
        );

        awayTeam.updateStats(
                match.getAwayGoals(),
                match.getHomeGoals()
        );

        match.setFinished(true);

        addLog(
                "MATCH FINISHED - "
                        + homeTeam.getCountryName()
                        + " "
                        + match.getHomeGoals()
                        + " - "
                        + match.getAwayGoals()
                        + " "
                        + awayTeam.getCountryName()
        );

        updateStatus("Match Finished");
    }

    private Team chooseTeam() {

        String[] options = {
                match.getHome().getCountryName(),
                match.getAway().getCountryName()
        };

        String selectedOption =
                (String) JOptionPane.showInputDialog(
                        this,
                        "Choose Team:",
                        "Select Team",
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        options,
                        options[0]
                );

        if (selectedOption == null) {
            return null;
        }

        if (selectedOption.equals(
                match.getHome().getCountryName()
        )) {

            return match.getHome();
        }

        return match.getAway();
    }

    private Player choosePlayer(Team team) {

        int playerCount = 0;

        for (int i = 0;
             i < team.getMemberCount();
             i++) {

            Person currentMember =
                    team.getMember(i);

            if (currentMember instanceof Player) {
                playerCount++;
            }
        }

        if (playerCount == 0) {
            return null;
        }

        Player[] players =
                new Player[playerCount];

        String[] options =
                new String[playerCount];

        int index = 0;

        for (int i = 0;
             i < team.getMemberCount();
             i++) {

            Person currentMember =
                    team.getMember(i);

            if (currentMember instanceof Player) {

                Player currentPlayer =
                        (Player) currentMember;

                players[index] =
                        currentPlayer;

                options[index] =
                        currentPlayer.getFullName()
                                + " | "
                                + currentPlayer.getPosition();

                index++;
            }
        }

        String selectedOption =
                (String) JOptionPane.showInputDialog(
                        this,
                        "Choose Player:",
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

    private Player chooseStartingPlayer(Team team) {

        if (team.getStartingCount() == 0) {
            return null;
        }

        Player[] players =
                new Player[team.getStartingCount()];

        String[] options =
                new String[team.getStartingCount()];

        for (int i = 0;
             i < team.getStartingCount();
             i++) {

            Player currentPlayer =
                    team.getStartingPlayer(i);

            players[i] = currentPlayer;

            options[i] =
                    currentPlayer.getFullName()
                            + " | "
                            + currentPlayer.getPosition();
        }

        String selectedOption =
                (String) JOptionPane.showInputDialog(
                        this,
                        "Outgoing Player:",
                        "Starting XI",
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

    private Player chooseBenchPlayer(Team team) {

        if (team.getBenchCount() == 0) {
            return null;
        }

        Player[] players =
                new Player[team.getBenchCount()];

        String[] options =
                new String[team.getBenchCount()];

        for (int i = 0;
             i < team.getBenchCount();
             i++) {

            Player currentPlayer =
                    team.getBenchPlayer(i);

            players[i] = currentPlayer;

            options[i] =
                    currentPlayer.getFullName()
                            + " | "
                            + currentPlayer.getPosition();
        }

        String selectedOption =
                (String) JOptionPane.showInputDialog(
                        this,
                        "Incoming Player:",
                        "Bench",
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

    private void addLog(String message) {

        outputArea.append(
                "[" +
                        LocalTime.now().withNano(0)
                        + "] "
                        + message
                        + "\n"
        );
    }

    private void updateStatus(String latestAction) {

        matchLabel.setText(
                "Match : "
                        + match.getMatchName()
        );

        scoreLabel.setText(
                "Score : "
                        + match.getHomeGoals()
                        + " - "
                        + match.getAwayGoals()
        );

        statusLabel.setText(
                "Status : "
                        + (match.isFinished()
                        ? "Finished"
                        : "In Progress")
        );

        latestActionLabel.setText(
                "Latest Action : "
                        + latestAction
        );
    }
}