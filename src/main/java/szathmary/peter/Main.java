package szathmary.peter;

import szathmary.peter.simulation.core.MonteCarloSimulationCore;
import szathmary.peter.simulation.core.MortgageLoanMonteCarlo;

public class Main {
  public static void main(String[] args) {
    MonteCarloSimulationCore monteCarloSimulationCore =
        new MortgageLoanMonteCarlo(10000000);
    monteCarloSimulationCore.startSimulation();
  }
}
