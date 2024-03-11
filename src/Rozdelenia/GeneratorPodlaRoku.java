package Rozdelenia;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class GeneratorPodlaRoku {
    private List<IRozdelenie> pravdepodobnosti;
    private List<int[]> hranice ;
    private int zaciatocnyRok;
    private Random random;
    public GeneratorPodlaRoku(int zaciatocnyRok) {
        random = new Random(1);
        List<Double> pravdepodobnostiEmpiricke = Arrays.asList( 0.1, 0.35, 0.2, 0.15, 0.15, 0.05);
        List<Double[]> hraniceEmpiricke = Arrays.asList(new Double[]{0.1, 0.3}, new Double[]{0.3, 0.8}, new Double[]{0.8, 1.2}, new Double[]{1.2, 2.5}, new Double[]{2.5, 3.8}, new Double[]{3.8, 4.8});
        this.pravdepodobnosti = Arrays.asList( new DiskretneRovnomerne(1, 4, random), new SpojiteRovnomerne(0.3, 5.0, random), new SpojiteEmpiricke(hraniceEmpiricke, pravdepodobnostiEmpiricke, random), new Deterministicke(1.3), new SpojiteRovnomerne(0.9, 2.2, random));
        this.hranice = Arrays.asList(new int[]{2024, 2025}, new int[]{2026, 2027}, new int[]{2028, 2029}, new int[]{2030, 2031}, new int[]{2032, 2033});
        this.zaciatocnyRok = zaciatocnyRok;
    }

    public double getUrok(int rok) {
        int poradie = 0;
        //kontrolovanie, do ktorých hraníc sa daný rok zmestí a následne vygeneruje úrok z relevantného rozdelenia
        for (int[] hranica : this.hranice) {
            if (hranica[0] <= rok && rok <= hranica[1]) {
                return this.pravdepodobnosti.get(poradie).sample();
            }
            poradie++;
        }
            return 0.0;
    }

}
