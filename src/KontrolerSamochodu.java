public class KontrolerSamochodu {
    private Silnik silnik;
    private SkrzyniaBiegow skrzyniaBiegow;
    private ZasobySamochodu zasobySamochodu;
    private StanSamochodu stanSamochodu;
    private final int[] LIMITY_PREDKOSCI = {0, 20, 40, 60, 80, 100}; // Maksymalne prędkości dla biegów od 0 do 5
    private String errorOutput = "Wszystko w porzadku.";

    public KontrolerSamochodu(Silnik silnik, SkrzyniaBiegow skrzyniaBiegow, ZasobySamochodu zasobySamochodu, StanSamochodu stanSamochodu) {
        this.silnik = silnik;
        this.skrzyniaBiegow = skrzyniaBiegow;
        this.zasobySamochodu = zasobySamochodu;
        this.stanSamochodu = stanSamochodu;
    }

    public void wlaczSilnik() {
        if (zasobySamochodu.pobierzPaliwo() <= 0) {
            errorOutput = "Brak paliwa! Nie można uruchomić silnika.";
        } else if (!zasobySamochodu.czyJestOlej()) {
            errorOutput = "Brak oleju! Nie można uruchomić silnika.";
        } else {
            silnik.wlacz();
        }
    }

    public void wylaczSilnik() {
        silnik.wylacz();
        stanSamochodu.ustawPredkosc(0);
        stanSamochodu.ustawPoruszanie(false);
        skrzyniaBiegow.ustawBieg(0);
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
}

