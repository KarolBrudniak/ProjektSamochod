public class StanSamochodu {
    private int predkosc;
    private boolean poruszanie;

    public int pobierzPredkosc() {
        return predkosc;
    }

    public void ustawPredkosc(int predkosc) {
        this.predkosc = predkosc;
        ustawPoruszanie(predkosc > 0);
    }

    public boolean czyPoruszaSie() {
        return poruszanie;
    }

    public void ustawPoruszanie(boolean poruszanie) {
        this.poruszanie = poruszanie;
    }
}
