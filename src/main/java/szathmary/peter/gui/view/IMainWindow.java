package szathmary.peter.gui.view;

import szathmary.peter.gui.observable.IObserver;

public interface IMainWindow extends IObserver {
  void startSimulation();

  void setParameters();

  void stopSimulation();
}
