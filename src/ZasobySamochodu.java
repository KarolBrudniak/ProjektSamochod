public class ZasobySamochodu {
    private int iloscPaliwa;
    private boolean jestOlej;

    public ZasobySamochodu(int iloscPaliwa) {
        this.iloscPaliwa = iloscPaliwa;
        this.jestOlej = false; // Na początku brak oleju
    }

    // Metody do zarządzania paliwem
    public int pobierzPaliwo() {
        return iloscPaliwa;
    }

    public void dolejPaliwo(int ilosc) {
        iloscPaliwa += ilosc;
    }

    public void zuzyjPaliwo(int ilosc) {
        iloscPaliwa -= ilosc;
        if (iloscPaliwa < 0) {
            iloscPaliwa = 0;
        }
    }

    // Metody do zarządzania olejem
    public boolean czyJestOlej() {
        return jestOlej;
    }

    public void ustawOlej(boolean stan) {
        this.jestOlej = stan;
    }
}
