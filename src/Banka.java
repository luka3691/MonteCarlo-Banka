import Rozdelenia.GeneratorPodlaRoku;

import java.util.List;

public class Banka extends SimJadro implements Runnable {
    private final int aktualnyRok = 2024;
    private List<Integer> fixacie;
    private double HU;
    private int n;
    private int m;
    private double celkovaSuma;
    private int pocetReplikacii;
    private char typ;
    private GeneratorPodlaRoku generator;
    Chart chart;
    public Banka(int pocetReplikacii, List<Integer> fixacie, Chart chart, char a) {
        super(pocetReplikacii);
        this.fixacie = fixacie;
        this.chart = chart;
        this.typ = a;
    }

    @Override
    void beforeReps() {

        this.generator = new GeneratorPodlaRoku(this.aktualnyRok);
        this.celkovaSuma = 0.0;
        this.pocetReplikacii = 0;
    }

    @Override
    void beforeRep() {
        this.HU = 100000.0;
        this.m = 0;

        this.n = 10;
    }

    @Override
    public void doRep() {
        //pre každú fixaciu
        int roky = 0;
        for (int poradieVZozname = 0; poradieVZozname < this.fixacie.size(); poradieVZozname++) {
            //vygenerovanie uroku podla aktualneho roku
            double ipa = this.generator.getUrok(this.aktualnyRok + roky)/100.0;
            double ipm = ipa/12.0;
            //vypocitanie mesacnej splatky
            double M = (this.HU * ipm * Math.pow(1+ipm, 12*this.n)) / (Math.pow(1+ipm, 12*this.n) - 1);
            //vypocitanie zaplatenej sumy podla poctu rokov a mesacnej splatky
            int rokyNaPosunutie = this.fixacie.get(poradieVZozname);
            this.celkovaSuma += M * 12.0 * rokyNaPosunutie ;
            this.m = this.fixacie.get(poradieVZozname);
            double S = this.HU * (Math.pow(1+ipm, 12*this.n)-Math.pow(1+ipm, 12*this.m))/(Math.pow(1+ipm, 12*this.n)-1);
            this.n = this.n - this.fixacie.get(poradieVZozname);

            //urcenie zostatku istiny ako sumu potrebnu na splacanie
            this.HU = S;
            //prepocitanie rokov splacania
roky += this.m;
        }
    }
    @Override
    void afterRep() {
        pocetReplikacii++;
    }
    @Override
    void afterReps() {
        System.out.println(this.celkovaSuma/super.getNumberOfReplications());
    }

    @Override
    public void run() {
        try {
            Thread.sleep(2000); // Add data every 2 seconds
            while (true) {
                double data = this.celkovaSuma/pocetReplikacii;
                chart.addData(data, pocetReplikacii, typ);

                    Thread.sleep(2000);

            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }
}
