import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

public class FootballTheme {

    public static final Color NIGHT = new Color(6, 18, 34);
    public static final Color DEEP_GREEN = new Color(7, 70, 45);
    public static final Color GRASS = new Color(13, 110, 63);
    public static final Color PANEL = new Color(14, 31, 51);
    public static final Color CARD = new Color(20, 43, 68);
    public static final Color GOLD = new Color(247, 193, 67);
    public static final Color LINE = new Color(52, 211, 153);
    public static final Color TEXT = new Color(240, 250, 245);
    public static final Color MUTED = new Color(190, 210, 205);
    public static final Color DANGER = new Color(223, 70, 70);

    private static final Font TITLE_FONT = new Font("Arial", Font.BOLD, 26);
    private static final Font SUBTITLE_FONT = new Font("Arial", Font.BOLD, 13);
    private static final Font LABEL_FONT = new Font("Arial", Font.BOLD, 13);
    private static final Font BUTTON_FONT = new Font("Arial", Font.BOLD, 14);
    private static final Font OUTPUT_FONT = new Font("Monospaced", Font.PLAIN, 13);

    public static void styleFrame(Container contentPane) {
        contentPane.setBackground(NIGHT);
        applyGlobalDialogTheme();
    }

    public static void applyGlobalDialogTheme() {
        UIManager.put("OptionPane.background", PANEL);
        UIManager.put("Panel.background", PANEL);
        UIManager.put("OptionPane.messageForeground", TEXT);
        UIManager.put("Button.select", DEEP_GREEN);
        UIManager.put("Button.background", new Color(230,230,230));
        UIManager.put("Button.foreground", Color.BLACK);
        UIManager.put("OptionPane.buttonAreaBorder", BorderFactory.createEmptyBorder());
        UIManager.put("Label.foreground", TEXT);
    }


    public static void styleHeaderPanel(JPanel panel, JLabel label) {
        panel.setBackground(DEEP_GREEN);
        panel.setBorder(new EmptyBorder(16, 18, 16, 18));
        label.setText("⚽  " + label.getText());
        label.setFont(TITLE_FONT);
        label.setForeground(TEXT);
    }

    public static void styleSimplePanel(JPanel panel) {
        panel.setBackground(CARD);
        panel.setForeground(TEXT);
    }

    public static void styleButtonPanelPlain(JPanel panel) {
        panel.setBackground(NIGHT);
    }

    public static void styleScrollPane(JScrollPane pane) {
        pane.getViewport().setBackground(new Color(3, 12, 24));
    }

    public static JPanel createHeader(String title, String subtitle, int width, int height) {
        JPanel header = new HeaderPanel();
        header.setOpaque(false);
        header.setPreferredSize(new Dimension(width, height));
        header.setLayout(new BorderLayout());
        header.setBorder(new EmptyBorder(12, 22, 12, 22));

        JLabel titleLabel = new JLabel("⚽  " + title);
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(TEXT);

        JLabel subtitleLabel = new JLabel(subtitle);
        subtitleLabel.setFont(SUBTITLE_FONT);
        subtitleLabel.setForeground(MUTED);

        JPanel textPanel = new JPanel(new GridLayout(2, 1));
        textPanel.setOpaque(false);
        textPanel.add(titleLabel);
        textPanel.add(subtitleLabel);

        JLabel badge = new JLabel("MATCHDAY", SwingConstants.CENTER);
        badge.setFont(new Font("Arial", Font.BOLD, 12));
        badge.setForeground(NIGHT);
        badge.setOpaque(true);
        badge.setBackground(GOLD);
        badge.setBorder(new EmptyBorder(7, 14, 7, 14));

        header.add(textPanel, BorderLayout.WEST);
        header.add(badge, BorderLayout.EAST);
        return header;
    }

    public static void styleStatusPanel(JPanel panel, String title) {
        panel.setBackground(CARD);
        panel.setForeground(TEXT);
        panel.setBorder(new CompoundBorder(
                new EmptyBorder(14, 14, 14, 8),
                new TitledBorder(new LineBorder(LINE, 1, true), title,
                        TitledBorder.LEFT, TitledBorder.TOP,
                        new Font("Arial", Font.BOLD, 13), GOLD)
        ));
    }

    public static void styleButtonPanel(JPanel panel) {
        panel.setBackground(NIGHT);
        panel.setBorder(new EmptyBorder(16, 16, 16, 16));
    }

    public static void styleLabel(JLabel label) {
        label.setForeground(TEXT);
        label.setFont(LABEL_FONT);
        label.setBorder(new EmptyBorder(4, 8, 4, 8));
    }

    public static void styleOutputArea(JTextArea area) {
        area.setBackground(new Color(3, 12, 24));
        area.setForeground(new Color(225, 245, 235));
        area.setCaretColor(GOLD);
        area.setSelectedTextColor(NIGHT);
        area.setSelectionColor(GOLD);
        area.setFont(OUTPUT_FONT);
        area.setMargin(new Insets(14, 14, 14, 14));
        area.setLineWrap(false);
    }

    public static void styleScrollPane(JScrollPane pane, String title) {
        pane.getViewport().setBackground(new Color(3, 12, 24));
        pane.setBorder(new CompoundBorder(
                new EmptyBorder(12, 12, 12, 12),
                new TitledBorder(new LineBorder(LINE, 1, true), title,
                        TitledBorder.LEFT, TitledBorder.TOP,
                        new Font("Arial", Font.BOLD, 13), GOLD)
        ));
    }

    public static void styleButton(JButton button) {
        button.setFont(BUTTON_FONT);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setBackground(GRASS);
        button.setForeground(Color.WHITE);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(new EmptyBorder(10, 14, 10, 14));

        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(GOLD);
                button.setForeground(NIGHT);
            }

            public void mouseExited(MouseEvent e) {
                button.setBackground(GRASS);
                button.setForeground(Color.WHITE);
            }
        });
    }

    public static void styleDangerButton(JButton button) {
        styleButton(button);
        button.setBackground(DANGER);
    }

    public static String fairWelcome(String title) {
        return "\n" +
                "        ╔══════════════════════════════════════════════╗\n" +
                "        ║              " + title + "              ║\n" +
                "        ╚══════════════════════════════════════════════╝\n\n" +
                "        Welcome to the CS Fair football tournament system.\n" +
                "        Choose an action below to manage the tournament.\n";
    }

    private static class HeaderPanel extends JPanel {
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            GradientPaint gradient = new GradientPaint(0, 0, DEEP_GREEN, getWidth(), getHeight(), NIGHT);
            g2.setPaint(gradient);
            g2.fillRect(0, 0, getWidth(), getHeight());

            g2.setColor(new Color(255, 255, 255, 32));
            for (int x = 0; x < getWidth(); x += 90) {
                g2.drawLine(x, 0, x + 55, getHeight());
            }

            g2.setColor(new Color(247, 193, 67, 80));
            g2.drawOval(getWidth() - 165, -40, 160, 160);
            g2.dispose();
            super.paintComponent(g);
        }
    }
}




