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

    private DefaultCategoryDataset dataset;

    private AtomicBoolean isUpdatingStopped;
    private double minYValue = Double.POSITIVE_INFINITY;
    private double maxYValue = Double.NEGATIVE_INFINITY;
    private JTextField replicationInput;
    private final double Y_AXIS_MARGIN = 0.1;
    private JButton startButton;
    private JButton startRandomButton;
    private JButton startZerosButton;
    private int numberOfValue = 0;
    private int tickUnits = 10;

    public Chart(String title) {
        super(title);

        this.dataset = new DefaultCategoryDataset();
        JFreeChart chart = ChartFactory.createLineChart(
                "Dynamic Chart",
                "Počet replikácií",
                "Hodnota",
                dataset
        );

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(800, 600));

        // Set the layout of the content pane
        getContentPane().setLayout(new BorderLayout());

        // Add ChartPanel to the CENTER of the content pane
        getContentPane().add(chartPanel, BorderLayout.CENTER);
        JPanel topPanel = new JPanel();
        JLabel replicationLabel = new JLabel("Počet replikácií:");
        replicationInput = new JTextField(12);
        replicationInput.setText("100000000");
        startButton = new JButton("A");
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Integer.parseInt(replicationInput.getText());
                    startSimulation('A');
                    disableButtons();
                }
                catch (NumberFormatException i) {
                    //Not an integer
                }
            }
        });
        startRandomButton = new JButton("B");
        startRandomButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Integer.parseInt(replicationInput.getText());
                    startSimulation('B');
                    disableButtons();
                }
                catch (NumberFormatException i) {
                    //Not an integer
                }
            }
        });
        startZerosButton = new JButton("C");
        startZerosButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Integer.parseInt(replicationInput.getText());
                    startSimulation('C');
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
        topPanel.add(startRandomButton);
        topPanel.add(startZerosButton);
        add(topPanel, BorderLayout.NORTH);




        JButton stopButton = new JButton("Stop");
        stopButton.addActionListener(e -> stopUpdating());

        JPanel controlPanel = new JPanel();

        controlPanel.add(stopButton);
        add(controlPanel, BorderLayout.SOUTH);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
        setVisible(true);
        this.isUpdatingStopped = new AtomicBoolean(false);
    }
    private void disableButtons() {
        startButton.setEnabled(false);
        startRandomButton.setEnabled(false);
        startZerosButton.setEnabled(false);
    }

    private void startSimulation(char pismenko) {
        Banka banka;
        if (pismenko == 'A') {
            banka = new Banka(Integer.parseInt(replicationInput.getText()), Arrays.asList(5, 3, 1, 1), this);
        } else if (pismenko == 'B') {
            banka = new Banka(Integer.parseInt(replicationInput.getText()), Arrays.asList(3, 3, 3, 1), this);
        } else {
            banka = new Banka(Integer.parseInt(replicationInput.getText()), Arrays.asList(3, 1, 5, 1), this);
        }
        Thread producerThread = new Thread(banka);
        Thread simulatcia = new Thread(banka::simuluj);
        producerThread.start();
        simulatcia.start();
    }


    public void addData(double value, int replikacia) {
        if (!isUpdatingStopped.get()) {

            SwingUtilities.invokeLater(() -> {
                dataset.addValue(value, "Hodnota", String.valueOf(replikacia));
                if (value < minYValue) minYValue = value;
                if (value > maxYValue) maxYValue = value;
                adjustAxis();
                ((ChartPanel) getContentPane().getComponent(0)).repaint(); // Repaint the chart panel
            });
        }

    }



    private void adjustAxis() {
        numberOfValue++;
        ChartPanel chartPanel = (ChartPanel) getContentPane().getComponent(0);
        JFreeChart chart = chartPanel.getChart();
        double range = maxYValue - minYValue;
        double margin = range * Y_AXIS_MARGIN;
        double upperBound = maxYValue + margin;
        double lowerBound = minYValue - margin;

        CategoryPlot plot = (CategoryPlot) chart.getPlot();
        ValueAxis yAxis = plot.getRangeAxis();
        yAxis.setAutoRange(false);
        yAxis.setRange(lowerBound, upperBound);
        if (numberOfValue % 10 == 0) {
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