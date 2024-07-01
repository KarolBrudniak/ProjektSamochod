public class Silnik {
    private boolean wlaczony;
    private int obroty;

    public void wlacz() {
        wlaczony = true;
        obroty = 1000; // Początkowe obroty po włączeniu silnika

    }

    public void wylacz() {
        wlaczony = false;
        obroty = 0; // Obroty są 0 po wyłączeniu silnika
    }

    public boolean czyDziala() {
        return wlaczony;
    }

    public int pobierzObroty() {
        return obroty;
    }

    public void ustawObroty(int obroty) {
        this.obroty = obroty;
    }
}
