package szathmary.peter.gui.controller;

import java.util.ArrayList;
import java.util.List;
import szathmary.peter.gui.model.MortgageLoanMonteCarloConfiguration;
import szathmary.peter.gui.model.IModel;
import szathmary.peter.gui.observable.IObservable;
import szathmary.peter.gui.observable.IObserver;
import szathmary.peter.gui.observable.IReplicationObservable;

/** Created by petos on 28/02/2024. */
public class MonteCarloController implements IController {
  private final IModel model;
  private final List<IObserver> observers;
  private double[] lastResults;

  public MonteCarloController(IModel model) {
    this.model = model;
    this.model.attach(this);
    this.observers = new ArrayList<>();
  }

  @Override
  public void startSimulation() {
    model.startSimulation();
  }

  @Override
  public void setParameters(long numberOfReplications, int sampleSize, long numberOfFirstReplicationsToCut) {
    MortgageLoanMonteCarloConfiguration mortgageLoanMonteCarloConfiguration =
        new MortgageLoanMonteCarloConfiguration(numberOfReplications, sampleSize, numberOfFirstReplicationsToCut);

    model.setParameters(mortgageLoanMonteCarloConfiguration);
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
    sendNotifications();
  }

  @Override
  public double[] getLastResults() {
    return lastResults;
  }
}
