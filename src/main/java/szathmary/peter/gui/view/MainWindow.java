package szathmary.peter.gui.view;

import javax.swing.*;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import szathmary.peter.gui.controller.IController;
import szathmary.peter.gui.observable.IObservable;
import szathmary.peter.gui.observable.IReplicationObservable;
import szathmary.peter.simulation.core.Strategy;

/** Created by petos on 28/02/2024. */
public class MainWindow extends JFrame implements IMainWindow {
  private final IController controller;
  private SwingWorker<Void, Void> simulationWorker;
  private JFreeChart chartStrategyA;
  private JFreeChart chartStrategyB;
  private JFreeChart chartStrategyC;
  private XYSeriesCollection datasetStrategyA;
  private XYSeriesCollection datasetStrategyB;
  private XYSeriesCollection datasetStrategyC;
  private JTextField numberOfReplicationsTextField;
  private JTextField sampleSizeTextField;
  private JButton startButton;
  private JButton stopButton;
  private ChartPanel chartPanelStrategyA;
  private ChartPanel chartPanelStrategyB;
  private ChartPanel chartPanelStrategyC;
  private JPanel mainPanel;
  private JTextPane resultTextPaneStrategyA;
  private JTextPane resultTextPaneStrategyB;
  private JTextPane resultTextPaneStrategyC;
  private JTextField cutFirstXValuesTextField;

  public MainWindow(IController controller) {
    this.controller = controller;
    this.controller.attach(this);

    setContentPane(mainPanel);
    setTitle("Mortgage loan Monte carlo");
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    setSize(1600, 1000);
    setLocationRelativeTo(null);
    setVisible(true);

    startButton.addActionListener(e -> startSimulation());

    stopButton.addActionListener(e -> stopSimulation());
  }

  public void createUIComponents() {
    datasetStrategyA = new XYSeriesCollection();
    XYSeries newSeriesStrategyA = new XYSeries("replications");
    datasetStrategyA.addSeries(newSeriesStrategyA);

    chartStrategyA =
        ChartFactory.createXYLineChart(
            "Strategy A",
            "Replication",
            "Total money paid",
            datasetStrategyA,
            PlotOrientation.VERTICAL,
            false,
            false,
            false);

    NumberAxis rangeAxisStrategyA = (NumberAxis) chartStrategyA.getXYPlot().getRangeAxis();
    rangeAxisStrategyA.setAutoRangeIncludesZero(false);

    chartPanelStrategyA = new ChartPanel(chartStrategyA);
    chartPanelStrategyA.setMouseZoomable(true);

    datasetStrategyB = new XYSeriesCollection();
    XYSeries newSeriesStrategyB = new XYSeries("replications");
    datasetStrategyB.addSeries(newSeriesStrategyB);

    chartStrategyB =
        ChartFactory.createXYLineChart(
            "Strategy B",
            "Replication",
            "Total money paid",
            datasetStrategyB,
            PlotOrientation.VERTICAL,
            false,
            false,
            false);

    NumberAxis rangeAxisStrategyB = (NumberAxis) chartStrategyB.getXYPlot().getRangeAxis();
    rangeAxisStrategyB.setAutoRangeIncludesZero(false);

    chartPanelStrategyB = new ChartPanel(chartStrategyB);
    chartPanelStrategyB.setMouseZoomable(true);

    datasetStrategyC = new XYSeriesCollection();
    XYSeries newSeriesStrategyC = new XYSeries("replications");
    datasetStrategyC.addSeries(newSeriesStrategyC);

    chartStrategyC =
        ChartFactory.createXYLineChart(
            "Strategy C",
            "Replication",
            "Total money paid",
            datasetStrategyC,
            PlotOrientation.VERTICAL,
            false,
            false,
            false);

    NumberAxis rangeAxisStrategyC = (NumberAxis) chartStrategyC.getXYPlot().getRangeAxis();
    rangeAxisStrategyC.setAutoRangeIncludesZero(false);

    chartPanelStrategyC = new ChartPanel(chartStrategyC);
    chartPanelStrategyC.setMouseZoomable(true);
  }

  @Override
  public void startSimulation() {
    setParameters();
    resetChart();

    simulationWorker =
        new SwingWorker<>() {
          @Override
          protected Void doInBackground() {
            controller.startSimulation();
            return null;
          }
        };

    simulationWorker.execute();
  }

  private void resetChart() {
    datasetStrategyA.getSeries(0).clear();
    datasetStrategyB.getSeries(0).clear();
    datasetStrategyC.getSeries(0).clear();
  }

  @Override
  public void setParameters() {
    controller.setParameters(
        Long.parseLong(numberOfReplicationsTextField.getText()),
        Integer.parseInt(sampleSizeTextField.getText()),
        Long.parseLong(cutFirstXValuesTextField.getText()));
  }

  @Override
  public void stopSimulation() {
    if (simulationWorker == null) {
      return;
    }

    simulationWorker.cancel(true);
  }

  @Override
  public void update(IObservable observable) {
    if (!(observable instanceof IReplicationObservable replicationObservable)) {
      return;
    }

    if (simulationWorker != null && !simulationWorker.isDone()) {
      double[] lastResult = replicationObservable.getLastResults();

      SwingUtilities.invokeLater(
          () -> {
            updateChart(
                lastResult[Strategy.A.getIndex()],
                datasetStrategyA,
                chartStrategyA,
                resultTextPaneStrategyA);

            updateChart(
                lastResult[Strategy.B.getIndex()],
                datasetStrategyB,
                chartStrategyB,
                resultTextPaneStrategyB);

            updateChart(
                lastResult[Strategy.C.getIndex()],
                datasetStrategyC,
                chartStrategyC,
                resultTextPaneStrategyC);
          });
    }
  }

  private void updateChart(
      double lastResult, XYSeriesCollection dataset, JFreeChart chart, JTextPane resultTextPane) {
    XYSeries series = dataset.getSeries(0);

    int sampleSize = Integer.parseInt(sampleSizeTextField.getText());

    series.add(
        series.getItemCount() == 0
            ? series.getItemCount() + sampleSize
            : series.getMaxX() + sampleSize,
        lastResult);

    chart.fireChartChanged();
    resultTextPane.setText("Result: " + lastResult);
  }
}
