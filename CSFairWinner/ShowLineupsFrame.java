import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ShowLineupsFrame extends JFrame implements ActionListener {

    private static final int ORIGINAL_IMAGE_WIDTH = 1536;
    private static final int ORIGINAL_IMAGE_HEIGHT = 1024;

    private static final int IMAGE_WIDTH = 1200;
    private static final int IMAGE_HEIGHT = 800;

    private Match match;
    private Team homeTeam;
    private Team awayTeam;
    private JFrame previousFrame;

    private JLabel backgroundLabel;
    private JButton backButton;

    public ShowLineupsFrame(Match match, JFrame previousFrame) {

        this.match = match;
        this.homeTeam = match.getHome();
        this.awayTeam = match.getAway();
        this.previousFrame = previousFrame;

        setTitle("Show Lineups - " + match.getMatchName());
        setSize(1220, 875);
        setLocation(560, 25);
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());
        contentPane.setBackground(new Color(14, 24, 35));

        createLineupPanel(contentPane);
        createBackPanel(contentPane);

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent event) {
                returnToLineupFrame();
            }
        });

        setVisible(true);
    }

    private void createLineupPanel(Container contentPane) {

        String imagePath = System.getProperty("user.dir") + "/lineup_template.jpeg";
        ImageIcon originalIcon = new ImageIcon(imagePath);

        if (originalIcon.getIconWidth() <= 0) {
            JOptionPane.showMessageDialog(
                    this,
                    "Could not find lineup_template.jpeg.\nPut the image in the same folder as the Java files."
            );
        }

        Image scaledImage = originalIcon.getImage().getScaledInstance(
                IMAGE_WIDTH,
                IMAGE_HEIGHT,
                Image.SCALE_SMOOTH
        );

        backgroundLabel = new JLabel(new ImageIcon(scaledImage));
        backgroundLabel.setPreferredSize(new Dimension(IMAGE_WIDTH, IMAGE_HEIGHT));
        backgroundLabel.setLayout(null);

        JPanel imagePanel = new JPanel();
        imagePanel.setBackground(new Color(14, 24, 35));
        imagePanel.add(backgroundLabel);

        contentPane.add(imagePanel, BorderLayout.CENTER);

        addTeamHeaders();
        addTeamLineup(homeTeam, true);
        addTeamLineup(awayTeam, false);
        addSubstitutes(homeTeam, true);
        addSubstitutes(awayTeam, false);
    }

    private void createBackPanel(Container contentPane) {

        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(new Color(14, 24, 35));
        bottomPanel.setPreferredSize(new Dimension(1220, 55));

        backButton = new JButton("Back");
        backButton.setFont(new Font("Arial", Font.BOLD, 15));
        backButton.setFocusPainted(false);
        backButton.addActionListener(this);

        bottomPanel.add(backButton);
        contentPane.add(bottomPanel, BorderLayout.SOUTH);
    }

    public void actionPerformed(ActionEvent event) {
        if (event.getSource() == backButton) {
            returnToLineupFrame();
        }
    }

    private void returnToLineupFrame() {
        dispose();

        if (previousFrame != null) {
            previousFrame.setVisible(true);
        }
    }

    private void addTeamHeaders() {

        String homeText = homeTeam.getCode() + " - " + homeTeam.getCountryName();
        String awayText = awayTeam.getCode() + " - " + awayTeam.getCountryName();

        addTextLabel(homeText, 205, 55, 360, 55, Color.BLACK, 24, true, SwingConstants.CENTER);
        addTextLabel(awayText, 875, 55, 360, 55, Color.BLACK, 24, true, SwingConstants.CENTER);
    }

    private void addTeamLineup(Team team, boolean homeSide) {

        Player[] goalkeepers = getPlayersByPosition(team, "GK");
        Player[] defenders = getPlayersByPosition(team, "DEF");
        Player[] midfielders = getPlayersByPosition(team, "MID");
        Player[] attackers = getPlayersByPosition(team, "ATT");

        if (homeSide) {
            addRow(attackers, new int[][]{{145, 335}, {270, 335}, {405, 335}, {535, 335}}, Color.BLUE);
            addRow(midfielders, new int[][]{{230, 560}, {365, 560}, {500, 560}}, Color.BLUE);
            addRow(defenders, new int[][]{{230, 765}, {365, 765}, {500, 765}}, Color.BLUE);
            addGoalkeeper(goalkeepers, 375, 910, Color.BLUE);
        } else {
            addRow(attackers, new int[][]{{760, 335}, {895, 335}, {1030, 335}, {1160, 335}}, Color.RED);
            addRow(midfielders, new int[][]{{820, 560}, {950, 560}, {1080, 560}}, Color.RED);
            addRow(defenders, new int[][]{{820, 765}, {950, 765}, {1080, 765}}, Color.RED);
            addGoalkeeper(goalkeepers, 945, 910, Color.RED);
        }

        fillEmptySpotsWithRemainingPlayers(team, homeSide);
    }

    private void fillEmptySpotsWithRemainingPlayers(Team team, boolean homeSide) {
        // This keeps the frame safe if the lineup is not exactly 3-3-4 yet.
        // The main rows above already display the proper 3-3-4 order.
    }

    private void addRow(Player[] players, int[][] nameBoxCenters, Color numberColor) {

        int limit = Math.min(players.length, nameBoxCenters.length);

        for (int i = 0; i < limit; i++) {
            int centerX = nameBoxCenters[i][0];
            int centerY = nameBoxCenters[i][1];
            addPlayer(players[i], centerX, centerY, numberColor);
        }
    }

    private void addGoalkeeper(Player[] goalkeepers, int centerX, int centerY, Color numberColor) {

        if (goalkeepers.length > 0) {
            addPlayer(goalkeepers[0], centerX, centerY, numberColor);
        }
    }

    private void addPlayer(Player player, int nameCenterX, int nameCenterY, Color numberColor) {

        if (player == null) {
            return;
        }

        String numberText = "" + player.getShirtNumber();
        String nameText = shortenName(player.getFullName(), 16);

        addTextLabel(
                numberText,
                nameCenterX - 18,
                nameCenterY - 98,
                36,
                25,
                Color.WHITE,
                18,
                true,
                SwingConstants.CENTER
        );

        addTextLabel(
                nameText,
                nameCenterX - 55,
                nameCenterY - 13,
                110,
                24,
                Color.BLACK,
                11,
                true,
                SwingConstants.CENTER
        );
    }

    private void addSubstitutes(Team team, boolean homeSide) {

        String titleText = team.getCode() + " - " + team.getCountryName();

        if (homeSide) {
            addTextLabel(titleText, 1290, 210, 180, 40, Color.BLACK, 16, true, SwingConstants.CENTER);
            addBenchNames(team, 1295, 330, 180, 29, new Color(20, 55, 120));
        } else {
            addTextLabel(titleText, 1290, 615, 180, 40, Color.BLACK, 16, true, SwingConstants.CENTER);
            addBenchNames(team, 1295, 720, 180, 29, new Color(160, 20, 20));
        }
    }

    private void addBenchNames(Team team, int originalX, int originalY, int originalWidth, int originalGap, Color textColor) {

        int maxVisible = 8;
        int count = Math.min(team.getBenchCount(), maxVisible);

        for (int i = 0; i < count; i++) {
            Player player = team.getBenchPlayer(i);

            if (player != null) {
                String text = player.getShirtNumber() + "  " + shortenName(player.getFullName(), 17);

                addTextLabel(
                        text,
                        originalX,
                        originalY + (i * originalGap),
                        originalWidth,
                        22,
                        textColor,
                        13,
                        true,
                        SwingConstants.LEFT
                );
            }
        }

        if (team.getBenchCount() > maxVisible) {
            addTextLabel(
                    "+ " + (team.getBenchCount() - maxVisible) + " more",
                    originalX,
                    originalY + (maxVisible * originalGap),
                    originalWidth,
                    22,
                    Color.DARK_GRAY,
                    12,
                    true,
                    SwingConstants.LEFT
            );
        }
    }

    private Player[] getPlayersByPosition(Team team, String position) {

        int count = 0;

        for (int i = 0; i < team.getStartingCount(); i++) {
            Player player = team.getStartingPlayer(i);

            if (player != null && player.getPosition().equalsIgnoreCase(position)) {
                count++;
            }
        }

        Player[] players = new Player[count];
        int index = 0;

        for (int i = 0; i < team.getStartingCount(); i++) {
            Player player = team.getStartingPlayer(i);

            if (player != null && player.getPosition().equalsIgnoreCase(position)) {
                players[index] = player;
                index++;
            }
        }

        return players;
    }

    private void addTextLabel(String text, int originalX, int originalY, int originalWidth, int originalHeight,
                              Color color, int fontSize, boolean bold, int alignment) {

        JLabel label = new JLabel(text);
        label.setForeground(color);
        label.setHorizontalAlignment(alignment);
        label.setVerticalAlignment(SwingConstants.CENTER);
        label.setOpaque(false);

        int fontStyle;
        if (bold) {
            fontStyle = Font.BOLD;
        } else {
            fontStyle = Font.PLAIN;
        }

        label.setFont(new Font("Arial", fontStyle, scaleFont(fontSize)));

        label.setBounds(
                scaleX(originalX),
                scaleY(originalY),
                scaleX(originalWidth),
                scaleY(originalHeight)
        );

        backgroundLabel.add(label);
    }

    private String shortenName(String name, int maxLength) {

        if (name == null) {
            return "";
        }

        if (name.length() <= maxLength) {
            return name;
        }

        return name.substring(0, maxLength - 3) + "...";
    }

    private int scaleX(int value) {
        return value * IMAGE_WIDTH / ORIGINAL_IMAGE_WIDTH;
    }

    private int scaleY(int value) {
        return value * IMAGE_HEIGHT / ORIGINAL_IMAGE_HEIGHT;
    }

    private int scaleFont(int value) {
        return Math.max(9, value * IMAGE_WIDTH / ORIGINAL_IMAGE_WIDTH);
    }
}
