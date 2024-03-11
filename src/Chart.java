import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.*;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;

public class Chart extends JFrame {

    private DefaultCategoryDataset datasetA;
    private DefaultCategoryDataset datasetB;
    private DefaultCategoryDataset datasetC;

    private AtomicBoolean isUpdatingStopped;
    private double minYValueA = Double.POSITIVE_INFINITY;
    private double maxYValueA = Double.NEGATIVE_INFINITY;
    private double minYValueB = Double.POSITIVE_INFINITY;
    private double maxYValueB = Double.NEGATIVE_INFINITY;
    private double minYValueC = Double.POSITIVE_INFINITY;
    private double maxYValueC = Double.NEGATIVE_INFINITY;
    private JTextField replicationInput;
    private final double Y_AXIS_MARGIN = 0.1;
    private JButton startButton;
    private JButton startRandomButton;
    private JButton startZerosButton;
    private int numberOfValueA = 0;
    private int numberOfValueB = 0;
    private int numberOfValueC = 0;
    private int tickUnits = 10;
    private ChartPanel panelA;
    private ChartPanel panelB;
    private ChartPanel panelC;
    public Chart(String title) {
        super(title);
        JPanel chartPanel = new JPanel();
        chartPanel.setLayout(new GridLayout(3, 1));
        this.datasetA = new DefaultCategoryDataset();
        JFreeChart chartA = ChartFactory.createLineChart(
                "Dynamic Chart",
                "Počet replikácií",
                "Hodnota",
                datasetA
        );
        panelA = new ChartPanel(chartA);
        chartPanel.add(panelA);


        this.datasetB = new DefaultCategoryDataset();
        JFreeChart chartB = ChartFactory.createLineChart(
                "Dynamic Chart",
                "Počet replikácií",
                "Hodnota",
                datasetB
        );

        panelB = new ChartPanel(chartB);
        chartPanel.add(panelB);

        this.datasetC = new DefaultCategoryDataset();
        JFreeChart chartC = ChartFactory.createLineChart(
                "Dynamic Chart",
                "Počet replikácií",
                "Hodnota",
                datasetC
        );

        panelC = new ChartPanel(chartC);
        chartPanel.add(panelC);


// Add ChartPanel to the CENTER of the content pane
        getContentPane().add(chartPanel, BorderLayout.CENTER);

        JPanel topPanel = new JPanel();
        JLabel replicationLabel = new JLabel("Počet replikácií:");
        replicationInput = new JTextField(12);
        replicationInput.setText("100000000");
        startButton = new JButton("Start");
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Integer.parseInt(replicationInput.getText());
                    startSimulation();
                    disableButtons();
                }
                catch (NumberFormatException i) {
                    //Not an integer
                }
            }
        });
        topPanel.add(replicationLabel);
        topPanel.add(replicationInput);
        topPanel.add(startButton);

        getContentPane().add(topPanel, BorderLayout.NORTH);

        JButton stopButton = new JButton("Stop");
        stopButton.addActionListener(e -> stopUpdating());
        JPanel controlPanel = new JPanel();
        controlPanel.add(stopButton);
        getContentPane().add(controlPanel, BorderLayout.SOUTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
        setVisible(true);
        this.isUpdatingStopped = new AtomicBoolean(false);
    }
    private void disableButtons() {
        startButton.setEnabled(false);
    }

    private void startSimulation() {
        Banka banka1 = new Banka(Integer.parseInt(replicationInput.getText()), Arrays.asList(5, 3, 1, 1), this,'A');
        Banka banka2 = new Banka(Integer.parseInt(replicationInput.getText()), Arrays.asList(3, 3, 3, 1), this, 'B');
        Banka banka3 = new Banka(Integer.parseInt(replicationInput.getText()), Arrays.asList(3, 1, 5, 1), this, 'C');
        Thread producer1 = new Thread(banka1);
        Thread producer2 = new Thread(banka2);
        Thread producer3 = new Thread(banka3);
        Thread simulatcia1 = new Thread(banka1::simuluj);
        Thread simulatcia2 = new Thread(banka2::simuluj);
        Thread simulatcia3 = new Thread(banka3::simuluj);
        producer1.start();
        producer2.start();
        producer3.start();
        simulatcia1.start();
        simulatcia2.start();
        simulatcia3.start();
    }


    public void addData(double value, int replikacia, char typ) {
        if (!isUpdatingStopped.get()) {
            if (typ == 'A') {
                SwingUtilities.invokeLater(() -> {
                    datasetA.addValue(value, "Hodnota", String.valueOf(replikacia));
                    if (value < minYValueA) minYValueA = value;
                    if (value > maxYValueA) maxYValueA = value;
                    adjustAxis('A');
                    panelA.repaint(); // Repaint the chart panel
                });
            } else if (typ == 'B') {
                SwingUtilities.invokeLater(() -> {
                    datasetB.addValue(value, "Hodnota", String.valueOf(replikacia));
                    if (value < minYValueB) minYValueB = value;
                    if (value > maxYValueB) maxYValueB = value;
                    adjustAxis('B');
                    panelB.repaint(); // Repaint the chart panel
                });
            } else if (typ == 'C') {
                SwingUtilities.invokeLater(() -> {
                    datasetC.addValue(value, "Hodnota", String.valueOf(replikacia));
                    if (value < minYValueC) minYValueC = value;
                    if (value > maxYValueC) maxYValueC = value;
                    adjustAxis('C');
                    panelC.repaint(); // Repaint the chart panel
                });
            }
        }

    }



    private void adjustAxis(char type) {
        ChartPanel chartPanel;
        double upperBound;
        double lowerBound;
        if (type == 'A') {
            numberOfValueA++;
            chartPanel = panelA;
            double range = maxYValueA - minYValueA;
            double margin = range * Y_AXIS_MARGIN;
            upperBound = maxYValueA + margin;
            lowerBound = minYValueA - margin;
        } else if (type == 'B') {
            numberOfValueB++;
            chartPanel = panelB;
            double range = maxYValueB - minYValueB;
            double margin = range * Y_AXIS_MARGIN;
            upperBound = maxYValueB + margin;
            lowerBound = minYValueB - margin;
        } else {
            numberOfValueC++;
            chartPanel = panelC;
            double range = maxYValueC - minYValueC;
            double margin = range * Y_AXIS_MARGIN;
            upperBound = maxYValueC + margin;
            lowerBound = minYValueC - margin;
        }
        JFreeChart chart = chartPanel.getChart();
        CategoryPlot plot = (CategoryPlot) chart.getPlot();
        ValueAxis yAxis = plot.getRangeAxis();
        yAxis.setAutoRange(false);
        yAxis.setRange(lowerBound, upperBound);
        if (numberOfValueA % 10 == 0) {
            CategoryAxis xAxis = plot.getDomainAxis();
            xAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45); // Rotate labels for better readability
            tickUnits += 10;
        }

    }

    public void stopUpdating() {
        isUpdatingStopped.set(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Chart("Dynamic Chart"));
    }
}