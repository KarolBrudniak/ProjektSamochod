import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class SamochodGUI {
    private KontrolerSamochodu kontroler;
    private JTextArea statusArea;
    private JTextArea errorArea;

    public SamochodGUI(KontrolerSamochodu kontroler) {
        this.kontroler = kontroler;
        createAndShowGUI();
    }

    private void createAndShowGUI() {
        JFrame frame = new JFrame("Symulator Samochodu");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        // Główny panel
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));

        // Panel statusu
        statusArea = new JTextArea(10, 20);  // Zwiększamy liczbę wierszy w JTextArea
        statusArea.setEditable(false);
        statusArea.setText(kontroler.wyswietlStatus());
        JScrollPane scrollPaneStatus = new JScrollPane(statusArea);
        scrollPaneStatus.setBorder(BorderFactory.createTitledBorder("Status Samochodu"));

        // Panel błędów
        errorArea = new JTextArea(5, 20);
        errorArea.setEditable(false);
        errorArea.setText(kontroler.wyswietlError());
        JScrollPane scrollPaneError = new JScrollPane(errorArea);
        scrollPaneError.setBorder(BorderFactory.createTitledBorder("Komunikaty Błędów"));

        // Panel przycisków
        JPanel buttonPanel = new JPanel(new GridLayout(0, 2, 5, 5)); // GridLayout z dwoma kolumnami
        addButtons(buttonPanel, frame);

        // Dodanie paneli do głównego panelu
        mainPanel.add(scrollPaneStatus, BorderLayout.CENTER);  // Status w centralnym miejscu
        mainPanel.add(scrollPaneError, BorderLayout.SOUTH);    // Błędy na dole
        mainPanel.add(buttonPanel, BorderLayout.NORTH);        // Przyciski na górze

        frame.add(mainPanel);
        frame.setVisible(true);
    }

    private void addButtons(JPanel panel, JFrame frame) {
        JButton wlaczSilnikButton = new JButton("Włącz Silnik");
        JButton wylaczSilnikButton = new JButton("Wyłącz Silnik");
        JButton zwiekszBiegButton = new JButton("Zwiększ Bieg");
        JButton zmniejszBiegButton = new JButton("Zmniejsz Bieg");
        JButton przyspieszButton = new JButton("Przyspiesz");
        JButton hamujButton = new JButton("Hamuj");
        JButton dolejPaliwoButton = new JButton("Dolej Paliwo");
        JButton dolejOlejButton = new JButton("Dolej Olej");
        JButton saveButton = new JButton("Zapisz status");
        JButton loadButton = new JButton("Wczytaj status");
        JButton exitButton = new JButton("Zakończ program");

        panel.add(wlaczSilnikButton);
        panel.add(wylaczSilnikButton);
        panel.add(zwiekszBiegButton);
        panel.add(zmniejszBiegButton);
        panel.add(przyspieszButton);
        panel.add(hamujButton);
        panel.add(dolejPaliwoButton);
        panel.add(dolejOlejButton);
        panel.add(saveButton);
        panel.add(loadButton);
        panel.add(exitButton);

        // Dodanie ActionListenerów do przycisków
        wlaczSilnikButton.addActionListener(e -> executeButtonAction("Włącz Silnik", frame));
        wylaczSilnikButton.addActionListener(e -> executeButtonAction("Wyłącz Silnik", frame));
        zwiekszBiegButton.addActionListener(e -> executeButtonAction("Zwiększ Bieg", frame));
        zmniejszBiegButton.addActionListener(e -> executeButtonAction("Zmniejsz Bieg", frame));
        przyspieszButton.addActionListener(e -> executeButtonAction("Przyspiesz", frame));
        hamujButton.addActionListener(e -> executeButtonAction("Hamuj", frame));
        dolejPaliwoButton.addActionListener(e -> executeButtonAction("Dolej Paliwo", frame));
        dolejOlejButton.addActionListener(e -> executeButtonAction("Dolej Olej", frame));
        saveButton.addActionListener(e -> executeButtonAction("Zapisz status", frame));
        loadButton.addActionListener(e -> {
            kontroler.loadAndApplyGuiStatus("status.txt");
            updateStatus();  // Aktualizacja statusu GUI
            updateError();   // Aktualizacja komunikatów o błędach
            JOptionPane.showMessageDialog(frame, "Dane zostały wczytane.", "Wczytany Status", JOptionPane.INFORMATION_MESSAGE);
        });
        exitButton.addActionListener(e -> executeButtonAction("Zakończ program", frame));
    }

    private void executeButtonAction(String action, JFrame frame) {
        try {
            switch (action) {
                case "Włącz Silnik": kontroler.wlaczSilnik(); break;
                case "Wyłącz Silnik": kontroler.wylaczSilnik(); break;
                case "Zwiększ Bieg": kontroler.zmienBieg(kontroler.pobierzBieg() + 1); break;
                case "Zmniejsz Bieg": kontroler.zmienBieg(kontroler.pobierzBieg() - 1); break;
                case "Przyspiesz": kontroler.przyspiesz(10); break;
                case "Hamuj": kontroler.hamuj(10); break;
                case "Dolej Paliwo": {
                    String iloscPaliwaStr = JOptionPane.showInputDialog(frame, "Ile jednostek paliwa dolać?");
                    kontroler.dolejPaliwo(Integer.parseInt(iloscPaliwaStr));
                    break;
                }
                case "Dolej Olej": kontroler.dolejOlej(); break;
                case "Zapisz status": kontroler.saveGuiStatusToFile("status.txt"); break;
                case "Zakończ program": System.exit(0); break;
            }
            updateStatus();
            updateError();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, "Wystąpił błąd: " + ex.getMessage(), "Błąd", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateStatus() {
        statusArea.setText(kontroler.wyswietlStatus());
    }

    private void updateError() {
        errorArea.setText(kontroler.wyswietlError());
    }
}
