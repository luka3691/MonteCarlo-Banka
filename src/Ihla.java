import java.util.Random;

public class Ihla extends SimJadro{
    double pocetPretati = 0;
    int D = 500;
    int l = 10;
    double y;
    double alfa;
    double a;
    Random rand;
    public Ihla(int numberOfReplications) {
        super(numberOfReplications);
    }

    @Override
    void beforeReps() {
        rand = new Random(System.currentTimeMillis());
        System.out.println("Začínam simuláciu");

    }
    @Override
    void beforeRep() {

    }
    @Override
    void doRep() {
        y = rand.nextDouble() * (D);
        alfa = rand.nextDouble() * (Math.PI);
        a = l * Math.sin(Math.abs(alfa));
    }
    @Override
    void afterRep() {
        if (a + y >= D) {
            pocetPretati++;
        }

    }
    @Override
    void afterReps() {
        System.out.println("Posledna replikacia je " + pocetPretati);
        System.out.println("Pravdepodobnosť je " + pocetPretati/super.getNumberOfReplications());
        System.out.println("Pi je " + 2 * l / (D * pocetPretati));
    }
}
