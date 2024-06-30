public class Main {
    public static void main(String[] args) {
        Silnik silnik = new Silnik();
        SkrzyniaBiegow skrzyniaBiegow = new SkrzyniaBiegow();
        ZasobySamochodu zasobySamochodu = new ZasobySamochodu(0); // Początkowe wartości: 0 jednostek paliwa, brak oleju
        StanSamochodu stanSamochodu = new StanSamochodu();

        KontrolerSamochodu kontroler = new KontrolerSamochodu(silnik, skrzyniaBiegow, zasobySamochodu, stanSamochodu);
        new SamochodGUI(kontroler);
    }
}
