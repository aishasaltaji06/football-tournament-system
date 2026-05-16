import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalTime;

public class StadiumManagementFrame extends JFrame implements ActionListener {

    private Tournament tournament;

    private JTextArea outputArea;

    private JLabel tournamentNameLabel;
    private JLabel stadiumCountLabel;
    private JLabel latestActionLabel;

    private JButton addButton;
    private JButton removeButton;
    private JButton viewButton;
    private JButton backButton;

    public StadiumManagementFrame(Tournament tournament) {

        this.tournament = tournament;

        setTitle(
                "Stadium Management ("
                        + tournament.getStadiumCount()
                        + " Stadiums)"
        );

        setSize(1400, 850);

        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocation(820, 120);

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

        if (tournament.getStadiumCount() == 0) {

            outputArea.setText(
                    "\n\n\n        No stadiums have been added yet."
            );
        }

        else {

            viewStadiums();
        }

        setVisible(true);
    }

    private void createTitlePanel(Container contentPane) {

        JPanel titlePanel = new JPanel();

        titlePanel.setBackground(
                new Color(20, 45, 85)
        );

        titlePanel.setPreferredSize(
                new Dimension(900, 80)
        );

        JLabel titleLabel =
                new JLabel("STADIUM MANAGEMENT");

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
                new GridLayout(3, 1, 5, 5)
        );

        statusPanel.setPreferredSize(
                new Dimension(250, 120)
        );

        statusPanel.setBorder(
                BorderFactory.createTitledBorder(
                        "System Status"
                )
        );

        tournamentNameLabel = new JLabel();

        stadiumCountLabel = new JLabel();

        latestActionLabel = new JLabel();

        Font statusFont =
                new Font("Arial", Font.BOLD, 13);

        tournamentNameLabel.setFont(statusFont);

        stadiumCountLabel.setFont(statusFont);

        latestActionLabel.setFont(statusFont);
        FootballTheme.styleLabel(tournamentNameLabel);
        FootballTheme.styleLabel(stadiumCountLabel);
        FootballTheme.styleLabel(latestActionLabel);

        statusPanel.add(tournamentNameLabel);

        statusPanel.add(stadiumCountLabel);

        statusPanel.add(latestActionLabel);

        FootballTheme.styleSimplePanel(statusPanel);

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
                        "Activity & Stadium List"
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
                new GridLayout(1, 4, 10, 10)
        );

        buttonPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        15,
                        15,
                        15,
                        15
                )
        );

        addButton = new JButton("Add Stadium");

        removeButton = new JButton("Remove Stadium");

        viewButton = new JButton("View Stadiums");

        backButton = new JButton("Back");

        JButton[] buttons = {
                addButton,
                removeButton,
                viewButton,
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

        if (event.getSource() == addButton) {

            addStadium();
        }

        else if (event.getSource() == removeButton) {

            removeStadium();
        }

        else if (event.getSource() == viewButton) {

            viewStadiums();
        }

        else if (event.getSource() == backButton) {

            dispose();
        }
    }

    private void updateStatus(String action) {

        tournamentNameLabel.setText(
                "Tournament : "
                        + tournament.getName()
        );

        stadiumCountLabel.setText(
                "Stored Stadiums : "
                        + tournament.getStadiumCount()
        );

        latestActionLabel.setText(
                "Latest Action : " + action
        );

        setTitle(
                "Stadium Management ("
                        + tournament.getStadiumCount()
                        + " Stadiums)"
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

    private void addStadium() {

        String stadiumName =
                JOptionPane.showInputDialog(
                        this,
                        "Enter stadium name:"
                );

        if (stadiumName == null ||
                stadiumName.trim().equals("")) {

            return;
        }

        String cityName =
                JOptionPane.showInputDialog(
                        this,
                        "Enter city:"
                );

        if (cityName == null ||
                cityName.trim().equals("")) {

            return;
        }

        int stadiumCapacity;

        try {

            stadiumCapacity =
                    Integer.parseInt(

                            JOptionPane.showInputDialog(
                                    this,
                                    "Enter capacity:"
                            )
                    );
        }

        catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Invalid capacity."
            );

            return;
        }

        try {

            Stadium newStadium =
                    new Stadium(
                            stadiumName,
                            cityName,
                            stadiumCapacity
                    );

            if (tournament.addStadium(newStadium)) {

                addLog(
                        "Stadium added successfully."
                );

                viewStadiums();

                updateStatus(
                        "Stadium Added"
                );
            }

            else {

                addLog(
                        "Could not add stadium."
                );

                updateStatus(
                        "Add Failed"
                );
            }
        }

        catch (InvalidCapacityException e) {

            addLog(
                    e.getMessage()
            );

            updateStatus(
                    "Invalid Capacity"
            );
        }
    }

    private void removeStadium() {

        String stadiumName =
                JOptionPane.showInputDialog(
                        this,
                        "Enter stadium name to remove:"
                );

        if (stadiumName == null) {

            return;
        }

        if (tournament.removeStadium(stadiumName)) {

            addLog(
                    "Stadium removed successfully."
            );

            viewStadiums();

            updateStatus(
                    "Stadium Removed"
            );
        }

        else {

            addLog(
                    "Stadium not found."
            );

            updateStatus(
                    "Remove Failed"
            );
        }
    }

    private void viewStadiums() {

        outputArea.setText("");

        outputArea.append(
                "=========== STADIUMS ===========\n\n"
        );

        if (tournament.getStadiumCount() == 0) {

            outputArea.append(
                    "No stadiums found.\n"
            );
        }

        else {

            for (int i = 0;
                 i < tournament.getStadiumCount();
                 i++) {

                Stadium currentStadium =
                        (Stadium)
                                tournament
                                        .getStadiums()
                                        .getObject(i);

                outputArea.append(
                        currentStadium.toString()
                                + "\n\n"
                );
            }
        }

        updateStatus(
                "Viewing Stadiums"
        );
    }
}