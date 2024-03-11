package szathmary.peter.gui.model;

import java.util.ArrayList;
import java.util.List;
import szathmary.peter.gui.observable.IObservable;
import szathmary.peter.gui.observable.IObserver;
import szathmary.peter.gui.observable.IReplicationObservable;
import szathmary.peter.simulation.core.MonteCarloSimulationCore;
import szathmary.peter.simulation.core.MortgageLoanMonteCarlo;

/** Created by petos on 28/02/2024. */
public class MonteCarloModel implements IModel {
  private final List<IObserver> observers;
  private MonteCarloSimulationCore monteCarloSimulation;
  private double[] lastResults;
  private long currentReplication;
  private long sampleSize;
  private long numberOfReplicationsToCut;

  public MonteCarloModel() {
    this.observers = new ArrayList<>();
    this.currentReplication = 0;
  }

  @Override
  public void startSimulation() {
    monteCarloSimulation.startSimulation();
  }

  @Override
  public void setParameters(
      MortgageLoanMonteCarloConfiguration mortgageLoanMonteCarloConfiguration) {
    monteCarloSimulation =
        new MortgageLoanMonteCarlo(
            mortgageLoanMonteCarloConfiguration.numberOfReplications(),
            mortgageLoanMonteCarloConfiguration.principalLoan());

    monteCarloSimulation.attach(this);

    sampleSize = mortgageLoanMonteCarloConfiguration.sampleSize();
    numberOfReplicationsToCut = mortgageLoanMonteCarloConfiguration.numberOfReplicationsToCut();
  }

  @Override
  public void stopSimulation() {
    monteCarloSimulation.setRunning(false);
  }

  @Override
  public void attach(IObserver observer) {
    observers.add(observer);
  }

  @Override
  public void detach(IObserver observer) {
    observers.remove(observer);
  }

  @Override
  public void sendNotifications() {
    for (IObserver observer : observers) {
      observer.update(this);
    }
  }

  @Override
  public void update(IObservable observable) {
    if (!(observable instanceof IReplicationObservable replicationObservable)) {
      return;
    }

    lastResults = replicationObservable.getLastResults();
    currentReplication++;

    if (currentReplication <= numberOfReplicationsToCut) {
      return;
    }

    if (currentReplication % sampleSize == 0) {
      sendNotifications();
    }
  }

  @Override
  public double[] getLastResults() {
    return lastResults;
  }
}
