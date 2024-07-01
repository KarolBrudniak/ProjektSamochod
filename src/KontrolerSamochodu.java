
import java.io.*;
import java.util.*;

public class KontrolerSamochodu {
    private Silnik silnik;
    private SkrzyniaBiegow skrzyniaBiegow;
    private ZasobySamochodu zasobySamochodu;
    private StanSamochodu stanSamochodu;
    private final int[] LIMITY_PREDKOSCI = {0, 20, 40, 60, 80, 100}; // Maksymalne prędkości dla biegów od 0 do 5
    private String errorOutput = "";

    public KontrolerSamochodu(Silnik silnik, SkrzyniaBiegow skrzyniaBiegow, ZasobySamochodu zasobySamochodu, StanSamochodu stanSamochodu) {
        this.silnik = silnik;
        this.skrzyniaBiegow = skrzyniaBiegow;
        this.zasobySamochodu = zasobySamochodu;
        this.stanSamochodu = stanSamochodu;
    }

    public void wlaczSilnik() {
        if (zasobySamochodu.pobierzPaliwo() <= 0 && !zasobySamochodu.czyJestOlej()) {
            errorOutput = "Brak paliwa oraz oleju! Nie można uruchomić silnika.";
        }
        if (zasobySamochodu.pobierzPaliwo() <= 0) {
            errorOutput = "Brak paliwa! Nie można uruchomić silnika.";
        } else if (!zasobySamochodu.czyJestOlej()) {
            errorOutput = "Brak oleju! Nie można uruchomić silnika.";
        } else {
            silnik.wlacz();
            errorOutput = "Silnik wlaczony";
        }
    }

    public void wylaczSilnik() {
        silnik.wylacz();
        stanSamochodu.ustawPredkosc(0);
        stanSamochodu.ustawPoruszanie(false);
        skrzyniaBiegow.ustawBieg(0);
        errorOutput = "Silnik wylaczony";
    }

    public void zmienBieg(int bieg) {
        if (silnik.czyDziala()) {
            skrzyniaBiegow.ustawBieg(bieg);
        } else {
            errorOutput = "Silnik nie jest uruchomiony. Nie można zmieniać biegów.";
        }
    }

    public int pobierzBieg() {
        return skrzyniaBiegow.pobierzBieg();
    }

    public void przyspiesz(int przyrost) {
        errorOutput = "";
        if (silnik.czyDziala() && skrzyniaBiegow.pobierzBieg() > 0) {
            int nowaPredkosc = stanSamochodu.pobierzPredkosc() + przyrost;
            int maksymalnaPredkosc = LIMITY_PREDKOSCI[skrzyniaBiegow.pobierzBieg()];
            if (nowaPredkosc > maksymalnaPredkosc) {
                nowaPredkosc = maksymalnaPredkosc;
                errorOutput = ("Nie można przyspieszyć powyżej maksymalnej prędkości dla biegu " + skrzyniaBiegow.pobierzBieg() + " (" + maksymalnaPredkosc + " km/h)");

            }
            stanSamochodu.ustawPredkosc(nowaPredkosc);
            int noweObroty = 1000 + (nowaPredkosc * 50);
            silnik.ustawObroty(noweObroty);
            zuzyjPaliwoZaleznieOdPredkosci(nowaPredkosc);
            if (zasobySamochodu.pobierzPaliwo() <= 0) {
                errorOutput = "Brak paliwa! Samochód zatrzymuje się.";
                wylaczSilnik();
            }
        } else {
            errorOutput = "Samochód nie może przyśpieszać bez uruchomionego silnika i ustawionego biegu.";
        }
    }

    private void zuzyjPaliwoZaleznieOdPredkosci(int predkosc) {
        int zuzycie = 1;
        if (predkosc > 20 && predkosc < 40) {
            zuzycie = 2;
        } else if (predkosc >= 40 && predkosc < 60) {
            zuzycie = 3;
        } else if (predkosc >= 60 && predkosc < 80) {
            zuzycie = 4;
        } else if (predkosc >= 80) {
            zuzycie = 5;
        }
        zasobySamochodu.zuzyjPaliwo(zuzycie);
    }

    public void hamuj(int zmniejszenie) {
        errorOutput = "";
        if (stanSamochodu.pobierzPredkosc() > 0) {
            int nowaPredkosc = stanSamochodu.pobierzPredkosc() - zmniejszenie;
            if (nowaPredkosc < 0) {
                nowaPredkosc = 0;
            }
            stanSamochodu.ustawPredkosc(nowaPredkosc);
            int noweObroty = 1000 + (nowaPredkosc * 50);
            silnik.ustawObroty(noweObroty);
        } else {
            errorOutput = "Samochód już stoi.";
        }
    }

    public String wyswietlStatus() {
        String status = "Szybkość: " + stanSamochodu.pobierzPredkosc() + " km/h\n";
        status += "Włączony bieg: " + skrzyniaBiegow.pobierzBieg() + "\n";
        status += "Obroty silnika: " + silnik.pobierzObroty() + " RPM\n";
        status += "Paliwo: " + zasobySamochodu.pobierzPaliwo() + "\n";
        status += "Olej: " + (zasobySamochodu.czyJestOlej() ? "Jest" : "Brak") + "\n";
        status += "Silnik: " + (silnik.czyDziala() ? "Włączony" : "Wyłączony") + "\n";

        // Sprawdzenie czy samochód się porusza i odpowiednie wyświetlenie
        String czyPoruszaSieTekst = stanSamochodu.czyPoruszaSie() ? "tak" : "nie";
        status += "Samochód porusza się: " + czyPoruszaSieTekst + "\n";

        return status;
    }

    public String wyswietlError() {
        String error = errorOutput;

        return error;
    }
    public void dolejPaliwo(int ilosc) {
        zasobySamochodu.dolejPaliwo(ilosc);
        System.out.println("Dolewano " + ilosc + " jednostek paliwa. Aktualna ilość paliwa: " + zasobySamochodu.pobierzPaliwo());
        errorOutput = ("Dolewano " + ilosc + " jednostek paliwa. Aktualna ilość paliwa: " + zasobySamochodu.pobierzPaliwo());
    }

    public void dolejOlej() {
        zasobySamochodu.ustawOlej(true);
        System.out.println("Dolewano olej. Aktualny stan: " + (zasobySamochodu.czyJestOlej() ? "Jest olej" : "Brak oleju"));
        errorOutput = ("Dolewano olej. Aktualny stan: " + (zasobySamochodu.czyJestOlej() ? "Jest olej" : "Brak oleju"));
    }
    public void saveToJSON(String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("{\n");
            writer.write("\"silnik\": {\n");
            writer.write("\"wlaczony\": " + silnik.czyDziala() + ",\n");
            writer.write("\"obroty\": " + silnik.pobierzObroty() + "\n");
            writer.write("},\n");
            writer.write("\"skrzyniaBiegow\": {\n");
            writer.write("\"bieg\": " + skrzyniaBiegow.pobierzBieg() + "\n");
            writer.write("},\n");
            writer.write("\"zasobySamochodu\": {\n");
            writer.write("\"paliwo\": " + zasobySamochodu.pobierzPaliwo() + ",\n");
            writer.write("\"olej\": " + zasobySamochodu.czyJestOlej() + "\n");
            writer.write("},\n");
            writer.write("\"stanSamochodu\": {\n");
            writer.write("\"predkosc\": " + stanSamochodu.pobierzPredkosc() + ",\n");
            writer.write("\"poruszaSie\": " + stanSamochodu.czyPoruszaSie() + "\n");
            writer.write("}\n");
            writer.write("}\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Deserializacja z JSON
    public void loadFromJSON(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                String value = line.substring(line.indexOf(":") + 1).trim().replace(",", "").replace("\"", "").replace("}", "");
                if (line.startsWith("\"wlaczony\":")) {
                    if (Boolean.parseBoolean(value)) {
                        silnik.wlacz();
                    } else {
                        silnik.wylacz();
                    }
                } else if (line.startsWith("\"obroty\":")) {
                    silnik.ustawObroty(Integer.parseInt(value));
                } else if (line.startsWith("\"bieg\":")) {
                    skrzyniaBiegow.ustawBieg(Integer.parseInt(value));
                } else if (line.startsWith("\"paliwo\":")) {
                    zasobySamochodu.dolejPaliwo(Integer.parseInt(value));  // Zakładając istnienie metody ustawPaliwo
                } else if (line.startsWith("\"olej\":")) {
                    zasobySamochodu.ustawOlej(Boolean.parseBoolean(value));
                } else if (line.startsWith("\"predkosc\":")) {
                    stanSamochodu.ustawPredkosc(Integer.parseInt(value));
                } else if (line.startsWith("\"poruszaSie\":")) {
                    stanSamochodu.ustawPoruszanie(Boolean.parseBoolean(value));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();  // Rozważ użycie loggera lub informowanie użytkownika GUI
        }
    }
}

