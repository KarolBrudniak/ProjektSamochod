import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

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

    public void saveGuiStatusToFile(String filename) {
        String status = wyswietlStatus();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write(status);
        } catch (IOException e) {
            System.err.println("Error writing GUI status to file: " + e.getMessage());
        }
    }

    public void loadAndApplyGuiStatus(String filename) {
        try {
            String data = new String(Files.readAllBytes(Paths.get(filename)));
            applyStatusToObjects(data); // Aktualizacja obiektów na podstawie wczytanych danych
        } catch (IOException e) {
            System.err.println("Error reading GUI status from file: " + e.getMessage());
        }
    }

    private void applyStatusToObjects(String data) {
        String[] lines = data.split("\n");
        for (String line : lines) {
            String[] parts = line.split(": ");
            if (parts.length < 2) continue; // Nieprawidłowy format linii
            String key = parts[0].trim();
            String value = parts[1].trim();

            switch (key) {
                case "Szybkość":
                    stanSamochodu.ustawPredkosc(Integer.parseInt(value.replace(" km/h", "")));
                    break;
                case "Włączony bieg":
                    skrzyniaBiegow.ustawBieg(Integer.parseInt(value));
                    break;
                case "Obroty silnika":
                    silnik.ustawObroty(Integer.parseInt(value.replace(" RPM", "")));
                    break;
                case "Paliwo":
                    zasobySamochodu.dolejPaliwo(Integer.parseInt(value));
                    break;
                case "Olej":
                    zasobySamochodu.ustawOlej(value.equals("Jest"));
                    break;
                case "Silnik":
                    if (value.equals("Włączony")) {
                        silnik.wlacz();
                    } else {
                        silnik.wylacz();
                    }
                    break;
                case "Samochód porusza się":
                    stanSamochodu.ustawPoruszanie(value.equals("tak"));
                    break;
                default:
                    // Nieznany klucz - ignoruj
                    break;
            }
        }
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
        return errorOutput;
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
}
