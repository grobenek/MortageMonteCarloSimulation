package szathmary.peter.simulation.core;

import java.util.ArrayList;
import java.util.List;
import szathmary.peter.gui.observable.IObserver;
import szathmary.peter.randomgenerators.DeterministicRandomGenerator;
import szathmary.peter.randomgenerators.Generator;
import szathmary.peter.randomgenerators.RandomGenerator;
import szathmary.peter.randomgenerators.continuousgenerators.ContinuousEmpiricRandomGenerator;
import szathmary.peter.randomgenerators.continuousgenerators.ContinuousUniformGenerator;
import szathmary.peter.randomgenerators.discretegenerators.DiscreteUniformRandomGenerator;
import szathmary.peter.randomgenerators.empiricnumbergenerator.EmpiricOption;

/** Created by petos on 05/03/2024. */
public class MortgageLoanMonteCarloStrategyA extends MonteCarloSimulationCore {
  public static final int STARTING_YEAR = 2024;
  public static final int MONTHS_IN_YEAR = 12;
  private static final int YEARS_OF_MORTGAGE_LOAN = 10;
  private static final int TOTAL_LOANED_MONEY = 100_000;
  private final List<IObserver> observerList;
  private final RandomGenerator<Integer> year2024And2025Generator;
  private final RandomGenerator<Double> year2026And2027Generator;
  private final RandomGenerator<Double> year2028And2029Generator;
  private final Generator<Double> year2030And2031Generator;
  private final RandomGenerator<Double> year2032And2033Generator;
  private final int[] fixationPeriods = {5, 3, 1, 1};
  private double totalMoneyPaid;
  private int totalReplicationsDone;
  private int currentYear;
  private double moneyToPayLeft;
  private int yearsToPayLeft;
  private int fixationIndex;

  public MortgageLoanMonteCarloStrategyA(long numberOfReplications) {
    super(numberOfReplications);

    this.observerList = new ArrayList<>();

    this.year2024And2025Generator = new DiscreteUniformRandomGenerator(1, 5);
    this.year2026And2027Generator = new ContinuousUniformGenerator(0.3, 5);
    this.year2028And2029Generator =
        new ContinuousEmpiricRandomGenerator(
            List.of(
                new EmpiricOption<>(0.1, 0.3, 0.1),
                new EmpiricOption<>(0.3, 0.8, 0.35),
                new EmpiricOption<>(0.8, 1.2, 0.2),
                new EmpiricOption<>(1.2, 2.5, 0.15),
                new EmpiricOption<>(2.5, 3.8, 0.15),
                new EmpiricOption<>(3.8, 4.8, 0.05)));
    this.year2030And2031Generator = new DeterministicRandomGenerator<>(1.3);
    this.year2032And2033Generator = new ContinuousUniformGenerator(0.9, 2.2);
  }

  private static double getMonthlyInterestRate(double annualInterestRate) {
    return (annualInterestRate / 100.0) / 12.0;
  }

  @Override
  public void beforeReplications() {
    totalMoneyPaid = 0.0;
    totalReplicationsDone = 0;
  }

  @Override
  public void beforeReplication() {
    currentYear = STARTING_YEAR;
    yearsToPayLeft = YEARS_OF_MORTGAGE_LOAN;
    moneyToPayLeft = TOTAL_LOANED_MONEY;
  }

  @Override
  public void replication() {
    int currentFixation = fixationPeriods[0];
    fixationIndex = 0;
    double annualInterestRate = getAnnualInteresRateByYear();
    double monthlyInterestRate = getMonthlyInterestRate(annualInterestRate);
    double monthlyPayment = getMonthlyPayment(monthlyInterestRate);
    for (int i = 1; i <= YEARS_OF_MORTGAGE_LOAN; i++) {
      if (currentFixation < i) {
        // new fixation is calculated
        moneyToPayLeft = getPrincipalLeft(monthlyInterestRate);
        yearsToPayLeft -= fixationPeriods[fixationIndex];
        annualInterestRate = getAnnualInteresRateByYear();
        monthlyInterestRate = getMonthlyInterestRate(annualInterestRate);
        monthlyPayment = getMonthlyPayment(monthlyInterestRate);
        fixationIndex++;
        currentFixation += fixationPeriods[fixationIndex];
      }

      totalMoneyPaid += (MONTHS_IN_YEAR * monthlyPayment);

      currentYear++;
    }

    totalReplicationsDone++;
  }

  private double getAnnualInteresRateByYear() {
    return switch (currentYear) {
      case 2024, 2025 -> year2024And2025Generator.sample();
      case 2026, 2027 -> year2026And2027Generator.sample();
      case 2028, 2029 -> year2028And2029Generator.sample();
      case 2030, 2031 -> year2030And2031Generator.sample();
      case 2032, 2033 -> year2032And2033Generator.sample();
      default -> throw new IllegalStateException("Illegal year occured!");
    };
  }

  @Override
  public void afterReplication() {
    //    sendNotifications();
  }

  @Override
  public void afterReplications() {
    System.out.println(totalMoneyPaid / totalReplicationsDone);
  }

  private double getMonthlyPayment(double monthlyInterestRate) {
    return ((moneyToPayLeft
            * monthlyInterestRate
            * Math.pow((1.0 + monthlyInterestRate), 12.0 * yearsToPayLeft)))
        / ((Math.pow((1.0 + monthlyInterestRate), 12.0 * yearsToPayLeft)) - 1.0);
  }

  private double getPrincipalLeft(double monthlyInterestRate) {
    if (yearsToPayLeft == 0) {
      return 0;
    }

    return moneyToPayLeft
        * (((Math.pow(1 + monthlyInterestRate, 12.0 * yearsToPayLeft))
                - (Math.pow(1 + monthlyInterestRate, 12.0 * fixationPeriods[fixationIndex])))
            / ((Math.pow(1 + monthlyInterestRate, 12.0 * yearsToPayLeft)) - 1.0));
  }

  @Override
  public void attach(IObserver observer) {
    observerList.add(observer);
  }

  @Override
  public void detach(IObserver observer) {
    observerList.remove(observer);
  }

  @Override
  public void sendNotifications() {
    for (IObserver observer : observerList) {
      observer.update(this);
    }
  }

  @Override
  public double getLastResult() {
    return totalMoneyPaid;
  }
}
