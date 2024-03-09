package szathmary.peter;

import szathmary.peter.gui.controller.IController;
import szathmary.peter.gui.controller.MonteCarloController;
import szathmary.peter.gui.model.IModel;
import szathmary.peter.gui.model.MonteCarloModel;
import szathmary.peter.gui.view.IMainWindow;
import szathmary.peter.gui.view.MainWindow;

public class Main {
  public static void main(String[] args) {
    IModel model = new MonteCarloModel();
    IController controller = new MonteCarloController(model);
    IMainWindow mainWindow = new MainWindow(controller);
  }
}
