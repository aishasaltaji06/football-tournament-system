import javax.swing.*;
import java.awt.*;

public class TournamentGUITest {

    public static void main(String[] args) {

        UIManager.put("OptionPane.background", FootballTheme.NIGHT);
        UIManager.put("Panel.background", FootballTheme.NIGHT);
        UIManager.put("OptionPane.messageForeground", FootballTheme.TEXT);

        String tournamentName;

        ImageIcon originalIcon =
        new ImageIcon(
                System.getProperty("user.dir") + "/trophy.png"
        );

Image scaledImage =
        originalIcon.getImage().getScaledInstance(
                90,
                110,
                Image.SCALE_SMOOTH
        );

ImageIcon trophyIcon =
        new ImageIcon(scaledImage);
        do {

            tournamentName =
                    (String) JOptionPane.showInputDialog(
                            null,
                            "Enter tournament name:",
                            "🏆 Create Tournament",
                            JOptionPane.PLAIN_MESSAGE,
                            trophyIcon,
                            null,
                            ""
                    );

            if (tournamentName == null) {

                System.exit(0);
            }

        } while (tournamentName.trim().equals(""));

        String startDate;

        while (true) {

            startDate =
                    (String) JOptionPane.showInputDialog(
                            null,
                            "Enter start date (DD-MM-YYYY):",
                            "🏆 Create Tournament",
                            JOptionPane.PLAIN_MESSAGE,
                            trophyIcon,
                            null,
                            ""
                    );

            if (startDate == null) {

                System.exit(0);
            }

            if (isValidDateFormat(startDate)) {

                int year = Integer.parseInt(startDate.substring(6, 10));

                if (year >= 2026) {
                    break;
                }

                JOptionPane.showMessageDialog(
                        null,
                        "Year must be 2026 or later.",
                        "Invalid Year",
                        JOptionPane.ERROR_MESSAGE
                );

                continue;
            }

            JOptionPane.showMessageDialog(
                    null,
                    "Invalid date format.\nUse DD-MM-YYYY.",
                    "Invalid Date",
                    JOptionPane.ERROR_MESSAGE
            );
        }

        Tournament tournament =
                new Tournament(
                        tournamentName,
                        startDate
                );

        SwingUtilities.invokeLater(new Runnable() {

            public void run() {

                new TournamentMainFrame(tournament);
            }
        });
    }

    private static boolean isValidDateFormat(String date) {

        if (!date.matches("\\d{2}-\\d{2}-\\d{4}")) {

            return false;
        }

        String[] parts = date.split("-");

        int day =
                Integer.parseInt(parts[0]);

        int month =
                Integer.parseInt(parts[1]);

        int year =
                Integer.parseInt(parts[2]);

        if (month < 1 || month > 12) {

            return false;
        }

        if (day < 1 || day > 31) {

            return false;
        }

        if (year < 2026) {

            return false;
        }

        return true;
    }
}