import Rozdelenia.SpojiteEmpiricke;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        Chart chart = new Chart("Dynamic Chart");
        Banka banka = new Banka(100000000, Arrays.asList(5, 3, 1, 1), chart);



        Thread producerThread = new Thread(banka);
        Thread simulatcia = new Thread(banka::simuluj);
        producerThread.start();
        simulatcia.start();
/*
        List<Double> pravdepodobnosti = Arrays.asList( 0.1, 0.35, 0.2, 0.15, 0.15, 0.05);
        List<Double[]> hranice = Arrays.asList(new Double[]{0.1, 0.3}, new Double[]{0.3, 0.8}, new Double[]{0.8, 1.2}, new Double[]{1.2, 2.5}, new Double[]{2.5, 3.8}, new Double[]{3.8, 4.8});

        SpojiteEmpiricke rozdelenie = new SpojiteEmpiricke(hranice, pravdepodobnosti);
        try {
            FileWriter myWriter = new FileWriter("filename.txt");
            for (int i = 0; i < 100000; i++) {
                myWriter.write(String.valueOf(rozdelenie.sample()) + '\n');

            }
            myWriter.close();

        } catch (IOException e) {
            System.out.println("Error.");
            e.printStackTrace();
        }

*/
/*
        JFrame frame = new JFrame("Stop");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        JButton stopButton = new JButton("Stop");
        frame.getContentPane().add(stopButton);
        Ihla ihla = new Ihla(10000000);

        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ihla.stopSimulaciu();
            }
        });
        frame.setVisible(true);
        Thread simulacia = new Thread(ihla::simuluj);
        simulacia.start();
*/
    }
}