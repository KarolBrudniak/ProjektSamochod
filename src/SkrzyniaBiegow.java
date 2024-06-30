public class SkrzyniaBiegow {
    private int bieg;

    public void ustawBieg(int bieg) {
        if (bieg < 0 || bieg > 5) {
            throw new IllegalArgumentException("Nieprawidłowy bieg: " + bieg);
        }
        this.bieg = bieg;
    }

    public int pobierzBieg() {
        return bieg;
    }
}
