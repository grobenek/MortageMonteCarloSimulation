package szathmary.peter;

import javax.swing.*;
import szathmary.peter.simulation.core.MonteCarloSimulationCore;
import szathmary.peter.simulation.core.MortgageLoanMonteCarloStrategyA;

public class Main {
  public static void main(String[] args) {
    MonteCarloSimulationCore monteCarloSimulationCore =
        new MortgageLoanMonteCarloStrategyA(10000000);
    monteCarloSimulationCore.startSimulation();
  }
}
