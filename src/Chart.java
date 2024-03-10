import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.atomic.AtomicBoolean;

public class Chart extends JFrame {

    private DefaultCategoryDataset dataset;

    private AtomicBoolean isUpdatingStopped;
    private double minYValue = Double.POSITIVE_INFINITY;
    private double maxYValue = Double.NEGATIVE_INFINITY;

    private final double Y_AXIS_MARGIN = 0.1;
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

        // Add ChartPanel to the CENTER of the content pane
        getContentPane().add(chartPanel, BorderLayout.CENTER);




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

    }

    public void stopUpdating() {
        isUpdatingStopped.set(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Chart("Dynamic Chart"));
    }
}