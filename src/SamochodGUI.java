import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SamochodGUI {
    private KontrolerSamochodu kontroler;
    private JTextArea statusArea;
    private JTextArea statusArea2;

    public SamochodGUI(KontrolerSamochodu kontroler) {
        this.kontroler = kontroler;
        createAndShowGUI();
    }

    private void createAndShowGUI() {
        JFrame frame = new JFrame("Symulator Samochodu");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        // Główny panel
        JPanel rootPanel = new JPanel();
        rootPanel.setLayout(new BorderLayout());

        // Pole statusu
        statusArea = new JTextArea();
        statusArea.setEditable(false);
        statusArea.setText(kontroler.wyswietlStatus());
        JScrollPane scrollPane = new JScrollPane(statusArea);

        statusArea2 = new JTextArea();
        statusArea2.setEditable(false);
        statusArea2.setText(kontroler.wyswietlError());
        JScrollPane scrollPane2 = new JScrollPane(statusArea2);

        // Panel przycisków
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(6, 2));

        JButton wlaczSilnikButton = new JButton("Włącz Silnik");
        wlaczSilnikButton.addActionListener(e -> {
            kontroler.wlaczSilnik();
            updateStatus();
            updateError();
        });

        JButton wylaczSilnikButton = new JButton("Wyłącz Silnik");
        wylaczSilnikButton.addActionListener(e -> {
            kontroler.wylaczSilnik();
            updateStatus();
            updateError();
        });

        JButton zwiekszBiegButton = new JButton("Zwiększ Bieg");
        zwiekszBiegButton.addActionListener(e -> {
            int obecnyBieg = kontroler.pobierzBieg();
            if (obecnyBieg < 5) {
                kontroler.zmienBieg(obecnyBieg + 1);
                updateStatus();
                updateError();
            }
        });

        JButton zmniejszBiegButton = new JButton("Zmniejsz Bieg");
        zmniejszBiegButton.addActionListener(e -> {
            int obecnyBieg = kontroler.pobierzBieg();
            if (obecnyBieg > 0) {
                kontroler.zmienBieg(obecnyBieg - 1);
                updateStatus();
                updateError();
            }
        });

        JButton przyspieszButton = new JButton("Przyspiesz");
        przyspieszButton.addActionListener(e -> {
            kontroler.przyspiesz(10);
            updateStatus();
            updateError();
        });

        JButton hamujButton = new JButton("Hamuj");
        hamujButton.addActionListener(e -> {
            kontroler.hamuj(10);
            updateStatus();
            updateError();
        });

        JButton dolejPaliwoButton = new JButton("Dolej Paliwo");
        dolejPaliwoButton.addActionListener(e -> {
            String iloscPaliwaStr = JOptionPane.showInputDialog("Ile jednostek paliwa dolać?");
            int iloscPaliwa = Integer.parseInt(iloscPaliwaStr);
            kontroler.dolejPaliwo(iloscPaliwa);
            updateStatus();
            updateError();
        });

        JButton dolejOlejButton = new JButton("Dolej Olej");
        dolejOlejButton.addActionListener(e -> {
            kontroler.dolejOlej();
            updateStatus();
            updateError();
        });
        JButton saveButton = new JButton("Zapisz dane");
        saveButton.addActionListener(e -> {
            try {
                kontroler.saveToJSON("samochod.json");
                JOptionPane.showMessageDialog(frame, "Dane zostały zapisane.", "Informacja", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Błąd przy zapisywaniu danych: " + ex.getMessage(), "Błąd", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Przycisk ładowania danych z bazy
        JButton loadButton = new JButton("Wczytaj dane");
        loadButton.addActionListener(e -> {
            try {
                kontroler.loadFromJSON("samochod.json");
                updateStatus();  // Aktualizacja statusu samochodu w GUI
                updateError();   // Aktualizacja komunikatów o błędach
                JOptionPane.showMessageDialog(frame, "Dane zostały wczytane.", "Informacja", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Błąd przy wczytywaniu danych: " + ex.getMessage(), "Błąd", JOptionPane.ERROR_MESSAGE);
            }
        });

        JButton exitButton = new JButton("Zakończ program");
        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.dispose(); // Zamyka okno, ale nie kończy całego procesu, jeśli są inne wątki
                System.exit(0); // Bezpieczne zakończenie programu, zamyka wszystkie wątki
            }
        });

        buttonPanel.add(wlaczSilnikButton);
        buttonPanel.add(wylaczSilnikButton);
        buttonPanel.add(zwiekszBiegButton);
        buttonPanel.add(zmniejszBiegButton);
        buttonPanel.add(przyspieszButton);
        buttonPanel.add(hamujButton);
        buttonPanel.add(dolejPaliwoButton);
        buttonPanel.add(dolejOlejButton);
        buttonPanel.add(saveButton);
        buttonPanel.add(loadButton);
        buttonPanel.add(exitButton);


        // Dodanie komponentów do rootPanel
        rootPanel.add(scrollPane, BorderLayout.NORTH);
        rootPanel.add(scrollPane2, BorderLayout.CENTER);
        rootPanel.add(buttonPanel, BorderLayout.SOUTH);

        frame.add(rootPanel);
        frame.setVisible(true);
    }

    private void updateStatus() {
        statusArea.setText(kontroler.wyswietlStatus());
    }
    private void updateError() {
        statusArea2.setText(kontroler.wyswietlError());

    }
}

