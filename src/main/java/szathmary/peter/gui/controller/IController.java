package szathmary.peter.gui.controller;

import szathmary.peter.gui.observable.IObserver;
import szathmary.peter.gui.observable.IReplicationObservable;

public interface IController extends IObserver, IReplicationObservable {
  void startSimulation();

  void setParameters(
      long numberOfReplications,
      long sampleSize,
      long numberOfFirstReplicationsToCut,
      long principalLoan);

  void cancelSimulation();
}
