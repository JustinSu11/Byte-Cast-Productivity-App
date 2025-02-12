import javax.swing.*;
import java.awt.*;
import java.util.Calendar;
import java.util.Locale;

public class CalendarApp {
    private JFrame frame;
    private JPanel calendarPanel;
    private JLabel monthLabel;
    private Calendar calendar;
    public CalendarApp() {
        frame = new JFrame("Swing Calendar");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);
        calendar = Calendar.getInstance();
        monthLabel = new JLabel("", SwingConstants.CENTER);

        JButton preButton  = new JButton("<");
        JButton nextButton = new JButton(">");
        preButton.addActionListener(e -> updateMonth(-1));
        nextButton.addActionListener(e -> updateMonth(1));

        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BorderLayout());
        headerPanel.add(preButton, BorderLayout.WEST);
        headerPanel.add(monthLabel, BorderLayout.CENTER);
        headerPanel.add(nextButton, BorderLayout.EAST);

        calendarPanel = new JPanel(new GridLayout(7, 7));
        frame.setLayout(new BorderLayout());
        frame.add(headerPanel, BorderLayout.NORTH);
        frame.add(calendarPanel, BorderLayout.CENTER);

        updateCalendar();
        frame.setVisible(true);
    }
    private void updateMonth(int change) {
        calendar.add(Calendar.MONTH, change);
        updateCalendar();
    }

    private void updateCalendar() {
        calendarPanel.removeAll();
        monthLabel.setText(calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault()) + " " + calendar.get(Calendar.YEAR));

        String[] days = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
        for (String day : days) {
            calendarPanel.add(new JLabel(day, SwingConstants.CENTER));
        }

        Calendar temp = (Calendar) calendar.clone();
        temp.set(Calendar.DAY_OF_MONTH, 1);
        int firstDay = temp.get(Calendar.DAY_OF_WEEK) - 1;
        int maxDays = temp.getActualMaximum(Calendar.DAY_OF_MONTH);

        for (int i = 0; i < firstDay; i++) {
            calendarPanel.add(new JLabel(""));
        }
        for (int day = 1; day <= maxDays; day++) {
            JButton dayButton = new JButton(String.valueOf(day));
            calendarPanel.add(dayButton);
        }
        frame.revalidate();
        frame.repaint();
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(CalendarApp::new);
    }
}
