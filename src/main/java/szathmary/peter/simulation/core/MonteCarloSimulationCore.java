package szathmary.peter.simulation.core;

import szathmary.peter.gui.observable.IReplicationObservable;

/** Created by petos on 28/02/2024. */
public abstract class MonteCarloSimulationCore implements IReplicationObservable {
  private final long numberOfReplications;

  public MonteCarloSimulationCore(long numberOfReplications) {
    this.numberOfReplications = numberOfReplications;
  }

  public void startSimulation() {
    beforeReplications();
    for (int i = 0; i < numberOfReplications; i++) {
      beforeReplication();
      replication();
      afterReplication();
    }
    afterReplications();
  }

  public abstract void afterReplications();

  public abstract void afterReplication();

  public abstract void replication();

  public abstract void beforeReplication();

  public abstract void beforeReplications();
}
