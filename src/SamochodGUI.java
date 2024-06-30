import javax.swing.*;
import java.awt.*;


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
        frame.setSize(600, 450);

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
        buttonPanel.setLayout(new GridLayout(4, 2));

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

        buttonPanel.add(wlaczSilnikButton);
        buttonPanel.add(wylaczSilnikButton);
        buttonPanel.add(zwiekszBiegButton);
        buttonPanel.add(zmniejszBiegButton);
        buttonPanel.add(przyspieszButton);
        buttonPanel.add(hamujButton);
        buttonPanel.add(dolejPaliwoButton);
        buttonPanel.add(dolejOlejButton);

        // Dodanie komponentów do rootPanel
        rootPanel.add(scrollPane, BorderLayout.CENTER);
        rootPanel.add(scrollPane2, BorderLayout.EAST);
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

