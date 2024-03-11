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
    private int zakladSuma;
    private char typ;
    private GeneratorPodlaRoku generator;
    Chart chart;
    public Banka(int pocetReplikacii, int value, List<Integer> fixacie, Chart chart, char a) {
        super(pocetReplikacii);
        this.fixacie = fixacie;
        this.chart = chart;
        this.typ = a;
        this.zakladSuma = value;
    }

    @Override
    void beforeReps() {

        this.generator = new GeneratorPodlaRoku(this.aktualnyRok);
        this.celkovaSuma = 0.0;
        this.pocetReplikacii = 0;
    }

    @Override
    void beforeRep() {
        this.HU = this.zakladSuma;
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
            //ziskanie pocet splacanych rokov
            this.m = this.fixacie.get(poradieVZozname);
            double S = this.HU * (Math.pow(1+ipm, 12*this.n)-Math.pow(1+ipm, 12*this.m))/(Math.pow(1+ipm, 12*this.n)-1);
            //aktualizacia rokov do splatenia
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
        int number = getNumberOfReplications() / 25;
        if (pocetReplikacii % number == 0 || pocetReplikacii == getNumberOfReplications() ) {
        run();
        }
    }
    @Override
    void afterReps() {
        System.out.println("Stratégia " + typ + ": " +this.celkovaSuma/super.getNumberOfReplications());
    }

    @Override
    public void run() {
        //posielanie dat do grafu
        double data = this.celkovaSuma/pocetReplikacii;
        chart.addData(data, pocetReplikacii, typ);
    }
}

