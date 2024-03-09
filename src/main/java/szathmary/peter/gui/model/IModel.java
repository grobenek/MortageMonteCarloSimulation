package szathmary.peter.gui.model;

import szathmary.peter.gui.observable.IObserver;
import szathmary.peter.gui.observable.IReplicationObservable;

public interface IModel extends IReplicationObservable, IObserver {
  void startSimulation();

  void setParameters(MortgageLoanMonteCarloConfiguration mortgageLoanMonteCarloConfiguration);
}
