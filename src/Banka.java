import Rozdelenia.GeneratorPodlaRoku;
import Rozdelenia.SpojiteEmpiricke;

import java.util.Arrays;
import java.util.List;

public class Banka extends SimJadro {
    private final int aktualnyRok = 2024;
    private List<Integer> fixacie;
    private double HU;
    private int n;
    private int m;
    private double M;
    private double S;

    private double ipa;
    private double splatenaSuma;
    private double celkovaSuma;

    private GeneratorPodlaRoku generator;

    public Banka(int pocetReplikacii, List<Integer> fixacie) {
        super(pocetReplikacii);
        this.fixacie = fixacie;


    }



    @Override
    void beforeReps() {

        this.generator = new GeneratorPodlaRoku(this.aktualnyRok);
        this.celkovaSuma = 0.0;
    }



    @Override
    void beforeRep() {
        this.HU = 100000.0;
        this.m = 0;
        this.splatenaSuma = 0.0;
        this.n = 10;
        this.S = 0.0;
    }

    @Override
    public void doRep() {

        for (int poradieVZozname = 0; poradieVZozname < this.fixacie.size(); poradieVZozname++) {
            //vygenerovanie uroku podla aktualneho roku
            this.ipa = this.generator.getUrok(this.aktualnyRok + this.m)/100.0;
            double ipm = this.ipa/12.0;
            //vypocitanie mesacnej splatky
            this.M = (this.HU * ipm * Math.pow(1+ipm, 12*this.n)) / (Math.pow(1+ipm, 12*this.n) - 1);
            //vypocitanie zaplatenej sumy podla poctu rokov a mesacnej splatky
            int rokyNaPosunutie = this.fixacie.get(poradieVZozname);
            this.splatenaSuma += this.M * 12.0 * rokyNaPosunutie ;
            this.m = this.fixacie.get(poradieVZozname);

            //vypocitanie zostatku istiny
            this.S = this.HU * (Math.pow(1+ipm, 12*this.n)-Math.pow(1+ipm, 12*this.m))/(Math.pow(1+ipm, 12*this.n)-1);
            this.n = this.n - this.fixacie.get(poradieVZozname);

            //urcenie zostatku istiny ako sumu potrebnu na splacanie
            this.HU = this.S;
            //prepocitanie rokov splacania

        }
    }
    @Override
    void afterRep() {
    this.celkovaSuma += this.splatenaSuma;
    }
    @Override
    void afterReps() {
        System.out.println(this.celkovaSuma/super.getNumberOfReplications());
    }


}
