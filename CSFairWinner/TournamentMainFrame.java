import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class TournamentMainFrame extends JFrame implements ActionListener {

    private Tournament tournament;

    private JButton stadiumButton;
    private JButton teamButton;
    private JButton memberButton;
    private JButton matchButton;
    private JButton reportButton;
    private JButton exitButton;

    public TournamentMainFrame(Tournament tournament) {

        this.tournament = tournament;

        setTitle("Football Tournament Control Room");
        setSize(1400, 850);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocation(720, 80);
        setResizable(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());
        FootballTheme.styleFrame(contentPane);

        createTitlePanel(contentPane);
        createMenuPanel(contentPane);

        setVisible(true);
    }

    private void createTitlePanel(Container contentPane) {
        JPanel header = FootballTheme.createHeader(
                "FOOTBALL TOURNAMENT SYSTEM",
                " • manage teams, stadiums, matches, lineups and reports",
                900,
                105
        );
        contentPane.add(header, BorderLayout.NORTH);
    }

    private void createMenuPanel(Container contentPane) {

        JPanel mainPanel = new JPanel(new BorderLayout(18, 18));
        mainPanel.setBackground(FootballTheme.NIGHT);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(24, 28, 28, 28));

        JPanel heroPanel = new JPanel(new BorderLayout());
        heroPanel.setBackground(FootballTheme.CARD);
        heroPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(FootballTheme.LINE, 1, true),
                BorderFactory.createEmptyBorder(24, 28, 24, 28)
        ));

        JLabel welcome = new JLabel("<html><div style='font-size:24px;'>Welcome, Tournament Director</div>"
                + "<div style='font-size:12px;color:#bed2cd;'>Open the match center for the best live demo at the fair.</div></html>");
        welcome.setForeground(FootballTheme.TEXT);

        JLabel score = new JLabel("⚽ LIVE DEMO READY", SwingConstants.CENTER);
        score.setOpaque(true);
        score.setBackground(FootballTheme.GOLD);
        score.setForeground(FootballTheme.NIGHT);
        score.setFont(new Font("Arial", Font.BOLD, 18));
        score.setBorder(BorderFactory.createEmptyBorder(12, 18, 12, 18));

        heroPanel.add(welcome, BorderLayout.WEST);
        heroPanel.add(score, BorderLayout.EAST);

        JPanel cardPanel = new JPanel(new GridLayout(2, 3, 16, 16));
        cardPanel.setBackground(FootballTheme.NIGHT);

        stadiumButton = createMenuButton("🏟  Stadiums", "Add, remove and view venues");
        teamButton = createMenuButton("🛡  Teams", "Build the tournament clubs");
        memberButton = createMenuButton("👥  Members", "Players, goalkeepers and coaches");
        matchButton = createMenuButton("🔥  Match Center", "Generate, simulate and control games");
        reportButton = createMenuButton("📊  Reports", "Standings, winner and top scorers");
        exitButton = createMenuButton("🚪  Exit", "Close the system");

        JButton[] buttons = {stadiumButton, teamButton, memberButton, matchButton, reportButton, exitButton};

        for (int i = 0; i < buttons.length; i++) {
            buttons[i].addActionListener(this);
            cardPanel.add(buttons[i]);
        }

        mainPanel.add(heroPanel, BorderLayout.NORTH);
        mainPanel.add(cardPanel, BorderLayout.CENTER);
        contentPane.add(mainPanel, BorderLayout.CENTER);
    }

    private JButton createMenuButton(String title, String subtitle) {
        JButton button = new JButton("<html><center><div style='font-size:18px;'>" + title + "</div>"
                + "<div style='font-size:10px;color:#d6efe6;'>" + subtitle + "</div></center></html>");
        FootballTheme.styleButton(button);
        return button;
    }

    public void actionPerformed(ActionEvent event) {
        if (event.getSource() == stadiumButton) {
            new StadiumManagementFrame(tournament);
        }
        else if (event.getSource() == teamButton) {
            new TeamManagementFrame(tournament);
        }
        else if (event.getSource() == memberButton) {
            new MemberManagementFrame(tournament);
        }
        else if (event.getSource() == matchButton) {
            new MatchManagementFrame(tournament);
        }
        else if (event.getSource() == reportButton) {
            new ReportsFrame(tournament);
        }
        else if (event.getSource() == exitButton) {
            dispose();
        }
    }
}
