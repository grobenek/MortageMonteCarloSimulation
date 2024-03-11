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
public class MortgageLoanMonteCarlo extends MonteCarloSimulationCore {
  public static final int STARTING_YEAR = 2024;
  public static final int MONTHS_IN_YEAR = 12;
  private static final int YEARS_OF_MORTGAGE_LOAN = 10;
  private final long totalLoanedMoney;
  private final int[] fixationPeriodsStrategyA = {5, 3, 1, 1};
  private final int[] fixationPeriodsStrategyB = {3, 3, 3, 1};
  private final int[] fixationPeriodsStrategyC = {3, 1, 5, 1};
  private final RandomGenerator<Integer> year2024And2025Generator;
  private final RandomGenerator<Double> year2026And2027Generator;
  private final RandomGenerator<Double> year2028And2029Generator;
  private final Generator<Double> year2030And2031Generator;
  private final RandomGenerator<Double> year2032And2033Generator;
  private final List<IObserver> observerList;
  private double totalMoneyPaidStrategyA;
  private double totalMoneyPaidStrategyB;
  private double totalMoneyPaidStrategyC;
  private int totalReplicationsDone;
  private int currentYearStrategyA;
  private int currentYearStrategyB;
  private int currentYearStrategyC;
  private double moneyToPayLeftStrategyA;
  private double moneyToPayLeftStrategyB;
  private double moneyToPayLeftStrategyC;
  private int yearsToPayLeftStrategyA;
  private int yearsToPayLeftStrategyB;
  private int yearsToPayLeftStrategyC;
  private int fixationIndexStrategyA;
  private int fixationIndexStrategyB;
  private int fixationIndexStrategyC;

  public MortgageLoanMonteCarlo(long numberOfReplications, long loanPrincipal) {
    super(numberOfReplications);

    this.totalLoanedMoney = loanPrincipal;

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
    totalMoneyPaidStrategyA = 0.0;
    totalMoneyPaidStrategyB = 0.0;
    totalMoneyPaidStrategyC = 0.0;

    totalReplicationsDone = 0;
  }

  @Override
  public void beforeReplication() {
    currentYearStrategyA = STARTING_YEAR;
    currentYearStrategyB = STARTING_YEAR;
    currentYearStrategyC = STARTING_YEAR;

    yearsToPayLeftStrategyA = YEARS_OF_MORTGAGE_LOAN;
    yearsToPayLeftStrategyB = YEARS_OF_MORTGAGE_LOAN;
    yearsToPayLeftStrategyC = YEARS_OF_MORTGAGE_LOAN;

    moneyToPayLeftStrategyA = totalLoanedMoney;
    moneyToPayLeftStrategyB = totalLoanedMoney;
    moneyToPayLeftStrategyC = totalLoanedMoney;

    fixationIndexStrategyA = 0;
    fixationIndexStrategyB = 0;
    fixationIndexStrategyC = 0;
  }

  @Override
  public void replication() {
    replicationStrategyA();
    replicationStrategyB();
    replicationStrategyC();

    totalReplicationsDone++;
  }

  private void replicationStrategyA() {
    int currentFixation = fixationPeriodsStrategyA[0]; // TODO spravit zastavenie simulacie
    fixationIndexStrategyA = 0;
    double annualInterestRate = getAnnualInteresRateByYear(currentYearStrategyA);
    double monthlyInterestRate = getMonthlyInterestRate(annualInterestRate);
    double monthlyPayment =
        getMonthlyPayment(monthlyInterestRate, moneyToPayLeftStrategyA, yearsToPayLeftStrategyA);
    for (int i = 1; i <= YEARS_OF_MORTGAGE_LOAN; i++) {
      if (currentFixation < i) {
        // new fixation is calculated
        moneyToPayLeftStrategyA =
            getPrincipalLeft(
                monthlyInterestRate,
                yearsToPayLeftStrategyA,
                moneyToPayLeftStrategyA,
                fixationPeriodsStrategyA,
                fixationIndexStrategyA);
        yearsToPayLeftStrategyA -= fixationPeriodsStrategyA[fixationIndexStrategyA];
        annualInterestRate = getAnnualInteresRateByYear(currentYearStrategyA);
        monthlyInterestRate = getMonthlyInterestRate(annualInterestRate);
        monthlyPayment =
            getMonthlyPayment(
                monthlyInterestRate, moneyToPayLeftStrategyA, yearsToPayLeftStrategyA);
        fixationIndexStrategyA++;
        currentFixation += fixationPeriodsStrategyA[fixationIndexStrategyA];
      }

      totalMoneyPaidStrategyA += (MONTHS_IN_YEAR * monthlyPayment);

      currentYearStrategyA++;
    }
  }

  private void replicationStrategyB() {
    int currentFixation = fixationPeriodsStrategyB[0];
    double annualInterestRate = getAnnualInteresRateByYear(currentYearStrategyB);
    double monthlyInterestRate = getMonthlyInterestRate(annualInterestRate);
    double monthlyPayment =
        getMonthlyPayment(monthlyInterestRate, moneyToPayLeftStrategyB, yearsToPayLeftStrategyB);
    for (int i = 1; i <= YEARS_OF_MORTGAGE_LOAN; i++) {
      if (currentFixation < i) {
        // new fixation is calculated
        moneyToPayLeftStrategyB =
            getPrincipalLeft(
                monthlyInterestRate,
                yearsToPayLeftStrategyB,
                moneyToPayLeftStrategyB,
                fixationPeriodsStrategyB,
                fixationIndexStrategyB);
        yearsToPayLeftStrategyB -= fixationPeriodsStrategyB[fixationIndexStrategyB];
        annualInterestRate = getAnnualInteresRateByYear(currentYearStrategyB);
        monthlyInterestRate = getMonthlyInterestRate(annualInterestRate);
        monthlyPayment =
            getMonthlyPayment(
                monthlyInterestRate, moneyToPayLeftStrategyB, yearsToPayLeftStrategyB);
        fixationIndexStrategyB++;
        currentFixation += fixationPeriodsStrategyB[fixationIndexStrategyB];
      }

      totalMoneyPaidStrategyB += (MONTHS_IN_YEAR * monthlyPayment);

      currentYearStrategyB++;
    }
  }

  private void replicationStrategyC() {
    int currentFixation = fixationPeriodsStrategyC[0];
    double annualInterestRate = getAnnualInteresRateByYear(currentYearStrategyC);
    double monthlyInterestRate = getMonthlyInterestRate(annualInterestRate);
    double monthlyPayment =
        getMonthlyPayment(monthlyInterestRate, moneyToPayLeftStrategyC, yearsToPayLeftStrategyC);
    for (int i = 1; i <= YEARS_OF_MORTGAGE_LOAN; i++) {
      if (currentFixation < i) {
        // new fixation is calculated
        moneyToPayLeftStrategyC =
            getPrincipalLeft(
                monthlyInterestRate,
                yearsToPayLeftStrategyC,
                moneyToPayLeftStrategyC,
                fixationPeriodsStrategyC,
                fixationIndexStrategyC);
        yearsToPayLeftStrategyC -= fixationPeriodsStrategyC[fixationIndexStrategyC];
        annualInterestRate = getAnnualInteresRateByYear(currentYearStrategyC);
        monthlyInterestRate = getMonthlyInterestRate(annualInterestRate);
        monthlyPayment =
            getMonthlyPayment(
                monthlyInterestRate, moneyToPayLeftStrategyC, yearsToPayLeftStrategyC);
        fixationIndexStrategyC++;
        currentFixation += fixationPeriodsStrategyC[fixationIndexStrategyC];
      }

      totalMoneyPaidStrategyC += (MONTHS_IN_YEAR * monthlyPayment);

      currentYearStrategyC++;
    }
  }

  private double getAnnualInteresRateByYear(int currentYearStrategy) {
    return switch (currentYearStrategy) {
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
    sendNotifications();
  }

  @Override
  public void afterReplications() {
    System.out.printf(
        "Strategy A: %f\nStrategyB: %f\nStrategy C: %f%n",
        (totalMoneyPaidStrategyA / totalReplicationsDone),
        (totalMoneyPaidStrategyB / totalReplicationsDone),
        (totalMoneyPaidStrategyC / totalReplicationsDone));
  }

  private double getMonthlyPayment(
      double monthlyInterestRate, double moneyToPayLeftStrategy, int yearsToPayLeftStrategy) {
    return ((moneyToPayLeftStrategy
            * monthlyInterestRate
            * Math.pow((1.0 + monthlyInterestRate), MONTHS_IN_YEAR * yearsToPayLeftStrategy)))
        / ((Math.pow((1.0 + monthlyInterestRate), MONTHS_IN_YEAR * yearsToPayLeftStrategy)) - 1.0);
  }

  private double getPrincipalLeft(
      double monthlyInterestRate,
      int yearsToPayLeftStrategy,
      double moneyToPayLeftStrategy,
      int[] fixationPeriodsStrategy,
      int fixationIndexStrategy) {
    if (yearsToPayLeftStrategy == 0) {
      return 0;
    }

    return moneyToPayLeftStrategy
        * (((Math.pow(1 + monthlyInterestRate, MONTHS_IN_YEAR * yearsToPayLeftStrategy))
                - (Math.pow(
                    1 + monthlyInterestRate,
                    MONTHS_IN_YEAR * fixationPeriodsStrategy[fixationIndexStrategy])))
            / ((Math.pow(1 + monthlyInterestRate, MONTHS_IN_YEAR * yearsToPayLeftStrategy)) - 1.0));
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
  public double[] getLastResults() {
    return new double[] {
      totalMoneyPaidStrategyA / totalReplicationsDone,
      totalMoneyPaidStrategyB / totalReplicationsDone,
      totalMoneyPaidStrategyC / totalReplicationsDone
    };
  }
}
