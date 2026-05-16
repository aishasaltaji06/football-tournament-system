import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.time.LocalTime;

public class MemberManagementFrame extends JFrame implements ActionListener {

    private Tournament tournament;

    private Team selectedTeam;

    private JTextArea outputArea;

    private JLabel teamNameLabel;
    private JLabel memberCountLabel;
    private JLabel remainingSlotsLabel;
    private JLabel coachLabel;
    private JLabel goalkeeperLabel;
    private JLabel latestActionLabel;

    private JButton addPlayerButton;
    private JButton addGoalkeeperButton;
    private JButton addCoachButton;
    private JButton removeMemberButton;
    private JButton showMembersButton;
    private JButton changeTeamButton;
    private JButton backButton;

    public MemberManagementFrame(Tournament tournament) {

        this.tournament = tournament;

        chooseTeam();

        if (selectedTeam == null) {

            dispose();

            return;
        }

        setTitle(
                "Member Management - "
                        + selectedTeam.getCountryName()
        );

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

        showMembers();

        setVisible(true);
    }

    private void chooseTeam() {

        while (selectedTeam == null) {

            String teamName =
                    JOptionPane.showInputDialog(
                            this,
                            "Enter team name:"
                    );

            if (teamName == null) {

                return;
            }

            selectedTeam =
                    tournament.searchTeam(teamName);

            if (selectedTeam == null) {

                JOptionPane.showMessageDialog(
                        this,
                        "Team not found."
                );
            }
        }
    }

    private void createTitlePanel(Container contentPane) {

        JPanel titlePanel = new JPanel();

        titlePanel.setBackground(
                new Color(20, 45, 85)
        );

        titlePanel.setPreferredSize(
                new Dimension(1050, 80)
        );

        JLabel titleLabel =
                new JLabel(
                        "MEMBER MANAGEMENT"
                );

        titleLabel.setFont(
                new Font("Arial", Font.BOLD, 24)
        );

        titleLabel.setForeground(Color.WHITE);
        FootballTheme.styleHeaderPanel(titlePanel, titleLabel);

        titlePanel.add(titleLabel);

        contentPane.add(
                titlePanel,
                BorderLayout.NORTH
        );
    }

    private void createStatusPanel(Container contentPane) {

        JPanel statusPanel = new JPanel();

        statusPanel.setLayout(
                new GridLayout(6, 1, 5, 5)
        );

        statusPanel.setPreferredSize(
                new Dimension(320, 200)
        );

        statusPanel.setBorder(
                BorderFactory.createTitledBorder(
                        "Team Status"
                )
        );

        Font labelFont =
                new Font("Arial", Font.BOLD, 13);

        teamNameLabel = new JLabel();
        memberCountLabel = new JLabel();
        remainingSlotsLabel = new JLabel();
        coachLabel = new JLabel();
        goalkeeperLabel = new JLabel();
        latestActionLabel = new JLabel();

        JLabel[] labels = {
                teamNameLabel,
                memberCountLabel,
                remainingSlotsLabel,
                coachLabel,
                goalkeeperLabel,
                latestActionLabel
        };

        for (int i = 0; i < labels.length; i++) {

            labels[i].setFont(labelFont);
            FootballTheme.styleLabel(labels[i]);

            statusPanel.add(labels[i]);
        }

        contentPane.add(
                statusPanel,
                BorderLayout.WEST
        );
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
                BorderFactory.createTitledBorder(
                        "Members & Activity"
                )
        );

        contentPane.add(
                scrollPane,
                BorderLayout.CENTER
        );
    }

    private void createButtonPanel(Container contentPane) {

        JPanel buttonPanel = new JPanel();
        FootballTheme.styleButtonPanelPlain(buttonPanel);

        buttonPanel.setLayout(
                new GridLayout(2, 4, 10, 10)
        );

        buttonPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        15,
                        15,
                        15,
                        15
                )
        );

        addPlayerButton =
                new JButton("Add Player");

        addGoalkeeperButton =
                new JButton("Add Goalkeeper");

        addCoachButton =
                new JButton("Add Coach");

        removeMemberButton =
                new JButton("Remove Member");

        showMembersButton =
                new JButton("Show Members");

        changeTeamButton =
                new JButton("Change Team");

        backButton =
                new JButton("Back");

        JButton[] buttons = {
                addPlayerButton,
                addGoalkeeperButton,
                addCoachButton,
                removeMemberButton,
                showMembersButton,
                changeTeamButton,
                backButton
        };

        for (int i = 0; i < buttons.length; i++) {

            buttons[i].setFont(
                    new Font("Arial", Font.BOLD, 14)
            );

            buttons[i].setFocusPainted(false);
            FootballTheme.styleButton(buttons[i]);

            buttons[i].addActionListener(this);

            buttonPanel.add(buttons[i]);
        }

        contentPane.add(
                buttonPanel,
                BorderLayout.SOUTH
        );
    }

    public void actionPerformed(ActionEvent event) {

        if (event.getSource() == addPlayerButton) {

            addPlayer();
        }

        else if (event.getSource() == addGoalkeeperButton) {

            addGoalkeeper();
        }

        else if (event.getSource() == addCoachButton) {

            addCoach();
        }

        else if (event.getSource() == removeMemberButton) {

            removeMember();
        }

        else if (event.getSource() == showMembersButton) {

            showMembers();
        }

        else if (event.getSource() == changeTeamButton) {

            selectedTeam = null;

            chooseTeam();

            if (selectedTeam != null) {

                showMembers();

                updateStatus("Team Changed");
            }
        }

        else if (event.getSource() == backButton) {

            dispose();
        }
    }

    private void updateStatus(String action) {

        int usedMembers =
                selectedTeam.getMemberCount();

        int maxMembers =
                selectedTeam.getMaxMembers();

        int remainingSlots =
                maxMembers - usedMembers;

        String coachName =
                "Not assigned";

        String goalkeeperAssigned =
                "No";

        for (int i = 0;
             i < selectedTeam.getMemberCount();
             i++) {

            Person currentMember =
                    selectedTeam.getMember(i);

            if (currentMember instanceof Coach) {

                coachName =
                        currentMember.getFullName();
            }

            if (currentMember instanceof Goalkeeper) {

                goalkeeperAssigned = "Yes";
            }
        }

        teamNameLabel.setText(
                "Team : "
                        + selectedTeam.getCountryName()
        );

        memberCountLabel.setText(
                "Members : "
                        + usedMembers
                        + " / "
                        + maxMembers
        );

        remainingSlotsLabel.setText(
                "Remaining Slots : "
                        + remainingSlots
        );

        coachLabel.setText(
                "Coach : "
                        + coachName
        );

        goalkeeperLabel.setText(
                "Goalkeeper Assigned : "
                        + goalkeeperAssigned
        );

        latestActionLabel.setText(
                "Latest Action : "
                        + action
        );

        setTitle(
                "Member Management - "
                        + selectedTeam.getCountryName()
        );
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

    private void addPlayer() {

        try {

            String fullName =
                    JOptionPane.showInputDialog(
                            this,
                            "Enter player full name:"
                    );

            if (fullName == null) {
                return;
            }

            String nationalId =
                    JOptionPane.showInputDialog(
                            this,
                            "Enter national ID:"
                    );

            int age =
                    Integer.parseInt(
                            JOptionPane.showInputDialog(
                                    this,
                                    "Enter age:"
                            )
                    );

            double salary =
                    Double.parseDouble(
                            JOptionPane.showInputDialog(
                                    this,
                                    "Enter salary:"
                            )
                    );

            int contractYearsLeft =
                    Integer.parseInt(
                            JOptionPane.showInputDialog(
                                    this,
                                    "Enter contract years:"
                            )
                    );

            int shirtNumber =
                    Integer.parseInt(
                            JOptionPane.showInputDialog(
                                    this,
                                    "Enter shirt number:"
                            )
                    );

            String playerPosition =
                    JOptionPane.showInputDialog(
                            this,
                            "Enter position (DEF/MID/ATT):"
                    );

            Player newPlayer =
                    new Player(
                            fullName,
                            nationalId,
                            age,
                            salary,
                            contractYearsLeft,
                            shirtNumber,
                            playerPosition
                    );

            if (selectedTeam.addMember(newPlayer)) {

                addLog(
                        "Player added successfully."
                );

                try {

                    tournament.saveTeamToFile(
                            selectedTeam
                    );

                    addLog(
                            "Team file updated successfully."
                    );
                }

                catch (IOException e) {

                    addLog(
                            "Player was added, but file could not be updated."
                    );

                    addLog(e.getMessage());
                }

                showMembers();

                updateStatus(
                        "Player Added"
                );
            }

            else {

                addLog(
                        "Could not add player."
                );

                updateStatus(
                        "Add Failed"
                );
            }
        }

        catch (NumberFormatException e) {

            addLog(
                    "Invalid numeric input."
            );
        }
    }

    private void addGoalkeeper() {

        try {

            String fullName =
                    JOptionPane.showInputDialog(
                            this,
                            "Enter goalkeeper full name:"
                    );

            if (fullName == null) {
                return;
            }

            String nationalId =
                    JOptionPane.showInputDialog(
                            this,
                            "Enter national ID:"
                    );

            int age =
                    Integer.parseInt(
                            JOptionPane.showInputDialog(
                                    this,
                                    "Enter age:"
                            )
                    );

            double salary =
                    Double.parseDouble(
                            JOptionPane.showInputDialog(
                                    this,
                                    "Enter salary:"
                            )
                    );

            int contractYearsLeft =
                    Integer.parseInt(
                            JOptionPane.showInputDialog(
                                    this,
                                    "Enter contract years:"
                            )
                    );

            int shirtNumber =
                    Integer.parseInt(
                            JOptionPane.showInputDialog(
                                    this,
                                    "Enter shirt number:"
                            )
                    );

            int saves =
                    Integer.parseInt(
                            JOptionPane.showInputDialog(
                                    this,
                                    "Enter saves:"
                            )
                    );

            Goalkeeper newGoalkeeper =
                    new Goalkeeper(
                            fullName,
                            nationalId,
                            age,
                            salary,
                            contractYearsLeft,
                            shirtNumber,
                            "GK",
                            saves
                    );

            if (selectedTeam.addMember(newGoalkeeper)) {

                addLog(
                        "Goalkeeper added successfully."
                );

                try {

                    tournament.saveTeamToFile(
                            selectedTeam
                    );

                    addLog(
                            "Team file updated successfully."
                    );
                }

                catch (IOException e) {

                    addLog(
                            "Goalkeeper was added, but file could not be updated."
                    );

                    addLog(e.getMessage());
                }

                showMembers();

                updateStatus(
                        "Goalkeeper Added"
                );
            }

            else {

                addLog(
                        "Could not add goalkeeper."
                );

                updateStatus(
                        "Add Failed"
                );
            }
        }

        catch (NumberFormatException e) {

            addLog(
                    "Invalid numeric input."
            );
        }
    }

    private void addCoach() {

        try {

            String fullName =
                    JOptionPane.showInputDialog(
                            this,
                            "Enter coach full name:"
                    );

            if (fullName == null) {
                return;
            }

            String nationalId =
                    JOptionPane.showInputDialog(
                            this,
                            "Enter national ID:"
                    );

            int age =
                    Integer.parseInt(
                            JOptionPane.showInputDialog(
                                    this,
                                    "Enter age:"
                            )
                    );

            double salary =
                    Double.parseDouble(
                            JOptionPane.showInputDialog(
                                    this,
                                    "Enter salary:"
                            )
                    );

            int contractYearsLeft =
                    Integer.parseInt(
                            JOptionPane.showInputDialog(
                                    this,
                                    "Enter contract years:"
                            )
                    );

            int licenseLevel =
                    Integer.parseInt(
                            JOptionPane.showInputDialog(
                                    this,
                                    "Enter license level:"
                            )
                    );

            Coach newCoach =
                    new Coach(
                            fullName,
                            nationalId,
                            age,
                            salary,
                            contractYearsLeft,
                            licenseLevel
                    );

            if (selectedTeam.addMember(newCoach)) {

                addLog(
                        "Coach added successfully."
                );

                try {

                    tournament.saveTeamToFile(
                            selectedTeam
                    );

                    addLog(
                            "Team file updated successfully."
                    );
                }

                catch (IOException e) {

                    addLog(
                            "Coach was added, but file could not be updated."
                    );

                    addLog(e.getMessage());
                }

                showMembers();

                updateStatus(
                        "Coach Added"
                );
            }

            else {

                addLog(
                        "Could not add coach."
                );

                updateStatus(
                        "Add Failed"
                );
            }
        }

        catch (NumberFormatException e) {

            addLog(
                    "Invalid numeric input."
            );
        }
    }

    private void removeMember() {

        String nationalId =
                JOptionPane.showInputDialog(
                        this,
                        "Enter member national ID to remove:"
                );

        if (nationalId == null) {
            return;
        }

        if (selectedTeam.removeMember(nationalId)) {

            addLog(
                    "Member removed successfully."
            );

            try {

                tournament.saveTeamToFile(
                        selectedTeam
                );

                addLog(
                        "Team file updated successfully."
                );
            }

            catch (IOException e) {

                addLog(
                        "Member removed, but file update failed."
                );

                addLog(e.getMessage());
            }

            showMembers();

            updateStatus(
                    "Member Removed"
            );
        }

        else {

            addLog(
                    "Member not found."
            );

            updateStatus(
                    "Remove Failed"
            );
        }
    }

    private void showMembers() {

        outputArea.setText("");

        outputArea.append(
                "=========== MEMBERS OF "
                        + selectedTeam
                        .getCountryName()
                        .toUpperCase()
                        + " ===========\n\n"
        );

        if (selectedTeam.getMemberCount() == 0) {

            outputArea.append(
                    "No members found.\n"
            );
        }

        else {

            for (int i = 0;
                 i < selectedTeam.getMemberCount();
                 i++) {

                Person currentMember =
                        selectedTeam.getMember(i);

                outputArea.append(
                        (i + 1)
                                + ". "
                                + currentMember
                                .getRole()
                                + " - "
                                + currentMember
                                .getFullName()
                                + "\n"
                );
            }
        }

        updateStatus(
                "Viewing Members"
        );
    }
}