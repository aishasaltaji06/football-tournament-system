import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.time.LocalTime;

public class TeamManagementFrame extends JFrame implements ActionListener {

    private Tournament tournament;

    private JTextArea outputArea;

    private JLabel tournamentNameLabel;
    private JLabel teamCountLabel;
    private JLabel latestActionLabel;

    private JButton addManualButton;
    private JButton addFromFileButton;
    private JButton removeButton;
    private JButton searchButton;
    private JButton listButton;
    private JButton backButton;

    public TeamManagementFrame(Tournament tournament) {

        this.tournament = tournament;

        setTitle("Team Management (" + tournament.getTeamCount() + " Teams)");
        setSize(1400, 850);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocation(800, 100);
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

        if (tournament.getTeamCount() == 0) {
            outputArea.setText("\n\n\n        No teams have been added yet.");
        } else {
            listTeams();
        }

        setVisible(true);
    }

    private void createTitlePanel(Container contentPane) {

        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(20, 45, 85));
        titlePanel.setPreferredSize(new Dimension(950, 80));

        JLabel titleLabel = new JLabel("TEAM MANAGEMENT");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        FootballTheme.styleHeaderPanel(titlePanel, titleLabel);

        titlePanel.add(titleLabel);
        contentPane.add(titlePanel, BorderLayout.NORTH);
    }

    private void createStatusPanel(Container contentPane) {

        JPanel statusPanel = new JPanel();
        statusPanel.setLayout(new GridLayout(3, 1, 5, 5));
        statusPanel.setPreferredSize(new Dimension(260, 130));

        statusPanel.setBorder(
                BorderFactory.createTitledBorder("System Status")
        );

        tournamentNameLabel = new JLabel();
        teamCountLabel = new JLabel();
        latestActionLabel = new JLabel();

        Font statusFont = new Font("Arial", Font.BOLD, 13);

        tournamentNameLabel.setFont(statusFont);
        teamCountLabel.setFont(statusFont);
        latestActionLabel.setFont(statusFont);
        FootballTheme.styleLabel(tournamentNameLabel);
        FootballTheme.styleLabel(teamCountLabel);
        FootballTheme.styleLabel(latestActionLabel);

        statusPanel.add(tournamentNameLabel);
        statusPanel.add(teamCountLabel);
        statusPanel.add(latestActionLabel);

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
                BorderFactory.createTitledBorder("Activity & Team List")
        );

        FootballTheme.styleScrollPane(scrollPane);

        contentPane.add(scrollPane, BorderLayout.CENTER);
    }

    private void createButtonPanel(Container contentPane) {

        JPanel buttonPanel = new JPanel();
        FootballTheme.styleButtonPanelPlain(buttonPanel);
        buttonPanel.setLayout(new GridLayout(2, 3, 10, 10));

        buttonPanel.setBorder(
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        );

        addManualButton = new JButton("Add Manual Team");
        addFromFileButton = new JButton("Add Team From File");
        removeButton = new JButton("Remove Team");
        searchButton = new JButton("Search Team");
        listButton = new JButton("List Teams");
        backButton = new JButton("Back");

        JButton[] buttons = {
                addManualButton,
                addFromFileButton,
                removeButton,
                searchButton,
                listButton,
                backButton
        };

        for (int i = 0; i < buttons.length; i++) {

            buttons[i].setFont(new Font("Arial", Font.BOLD, 14));
            buttons[i].setFocusPainted(false);
            FootballTheme.styleButton(buttons[i]);
            buttons[i].addActionListener(this);

            buttonPanel.add(buttons[i]);
        }

        contentPane.add(buttonPanel, BorderLayout.SOUTH);
    }

    public void actionPerformed(ActionEvent event) {

        if (event.getSource() == addManualButton) {
            addManualTeam();
        }

        else if (event.getSource() == addFromFileButton) {
            addExistingTeamFromFile();
        }

        else if (event.getSource() == removeButton) {
            removeTeam();
        }

        else if (event.getSource() == searchButton) {
            searchTeam();
        }

        else if (event.getSource() == listButton) {
            listTeams();
        }

        else if (event.getSource() == backButton) {
            dispose();
        }
    }

    private void updateStatus(String action) {

        tournamentNameLabel.setText(
                "Tournament : " + tournament.getName()
        );

        teamCountLabel.setText(
                "Stored Teams : " + tournament.getTeamCount()
        );

        latestActionLabel.setText(
                "Latest Action : " + action
        );

        setTitle(
                "Team Management (" + tournament.getTeamCount() + " Teams)"
        );
    }

    private void addLog(String message) {

        outputArea.append(
                "[" + LocalTime.now().withNano(0) + "] "
                        + message + "\n"
        );
    }

    private void addManualTeam() {

        String teamCode =
                JOptionPane.showInputDialog(
                        this,
                        "Enter team code:"
                );

        if (teamCode == null || teamCode.trim().equals("")) {
            return;
        }

        String countryName =
                JOptionPane.showInputDialog(
                        this,
                        "Enter country name:"
                );

        if (countryName == null || countryName.trim().equals("")) {
            return;
        }

        Team newTeam = new Team(teamCode, countryName);

        if (tournament.addTeam(newTeam)) {

            addLog("Team added successfully.");

            try {
                tournament.saveTeamToFile(newTeam);
                addLog("Team file saved successfully.");
            }

            catch (IOException e) {
                addLog("Team was added, but file could not be saved.");
                addLog(e.getMessage());
            }

            listTeams();
            updateStatus("Manual Team Added");
        }

        else {
            addLog("Could not add team.");
            updateStatus("Add Failed");
        }
    }

    private void addExistingTeamFromFile() {

        outputArea.setText("");

        outputArea.append("=========== EXISTING TEAM FILES ===========\n\n");

        String printedFiles = capturePrintedOutput("existingFiles");

        if (printedFiles.trim().equals("")) {
            outputArea.append("No file list output was returned.\n\n");
        }

        else {
            outputArea.append(printedFiles + "\n");
        }

        String fileName =
                JOptionPane.showInputDialog(
                        this,
                        "Enter existing team file name, for example RealMadrid.txt:"
                );

        if (fileName == null || fileName.trim().equals("")) {
            return;
        }

        try {
            Team existingTeam =
                    tournament.loadExistingTeamFromFile(fileName);

            if (tournament.addTeam(existingTeam)) {

                addLog("Existing team loaded successfully.");
                addLog("Team members were loaded from file.");

                try {
                    tournament.saveTeamToFile(existingTeam);
                    addLog("Loaded team was also saved in savedTeams folder.");
                }

                catch (IOException e) {
                    addLog("Team was loaded, but file could not be saved.");
                    addLog(e.getMessage());
                }

                listTeams();
                updateStatus("Team Loaded From File");
            }

            else {
                addLog("Could not add team. It may already exist.");
                updateStatus("Load Failed");
            }
        }

        catch (IOException e) {
            addLog("Could not load existing team from file.");
            addLog(e.getMessage());
            updateStatus("File Load Failed");
        }

        catch (NumberFormatException e) {
            addLog("File contains invalid number format.");
            updateStatus("Invalid File Format");
        }
    }

    private void removeTeam() {

        String teamName =
                JOptionPane.showInputDialog(
                        this,
                        "Enter team name to remove:"
                );

        if (teamName == null || teamName.trim().equals("")) {
            return;
        }

        if (tournament.removeTeam(teamName)) {

            addLog("Team removed successfully.");
            listTeams();
            updateStatus("Team Removed");
        }

        else {
            addLog("Team not found.");
            updateStatus("Remove Failed");
        }
    }

    private void searchTeam() {

        String teamName =
                JOptionPane.showInputDialog(
                        this,
                        "Enter team name to search:"
                );

        if (teamName == null || teamName.trim().equals("")) {
            return;
        }

        Team foundTeam = tournament.searchTeam(teamName);

        outputArea.setText("");

        outputArea.append("=========== SEARCH RESULT ===========\n\n");

        if (foundTeam != null) {

            outputArea.append("Team found:\n\n");
            outputArea.append(foundTeam.getSummary() + "\n");

            updateStatus("Team Found");
        }

        else {
            outputArea.append("Team not found.\n");
            updateStatus("Search Failed");
        }
    }

    private void listTeams() {

        outputArea.setText("");

        outputArea.append("=========== TEAMS ===========\n\n");

        if (tournament.getTeamCount() == 0) {

            outputArea.append("No teams found.\n");
        }

        else {

            for (int i = 0; i < tournament.getTeamCount(); i++) {

                Team currentTeam =
                        (Team) tournament
                                .getTeams()
                                .getObject(i);

                outputArea.append(
                        (i + 1) + ". "
                                + currentTeam.getSummary()
                                + "\n"
                );
            }
        }

        updateStatus("Viewing Teams");
    }

    private String capturePrintedOutput(String reportType) {

        java.io.PrintStream originalOutput = System.out;

        java.io.ByteArrayOutputStream byteOutput =
                new java.io.ByteArrayOutputStream();

        java.io.PrintStream temporaryOutput =
                new java.io.PrintStream(byteOutput);

        System.setOut(temporaryOutput);

        if (reportType.equals("existingFiles")) {
            tournament.printExistingTeamFiles();
        }

        System.out.flush();

        System.setOut(originalOutput);

        return byteOutput.toString();
    }
}