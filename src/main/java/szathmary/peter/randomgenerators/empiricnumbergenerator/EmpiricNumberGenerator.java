package szathmary.peter.randomgenerators.empiricnumbergenerator;

import java.util.ArrayList;
import java.util.List;
import szathmary.peter.randomgenerators.RandomGenerator;

/** Created by petos on 27/02/2024. */
public abstract class EmpiricNumberGenerator<T extends Number> extends RandomGenerator<T> {
  protected final List<EmpiricOption<T>> parameters;
  protected final List<RandomGenerator<T>> parametersGenerators;

  public EmpiricNumberGenerator(long seed, List<EmpiricOption<T>> parameters) {
    super(seed);

    checkParametersProbabilities(parameters);

    this.parameters = parameters;
    this.parametersGenerators = new ArrayList<>(parameters.size());

    initializeParameterGenerators();
  }

  public EmpiricNumberGenerator(List<EmpiricOption<T>> parameters) {
    checkParametersProbabilities(parameters);

    this.parameters = parameters;
    this.parametersGenerators = new ArrayList<>(parameters.size());

    initializeParameterGenerators();
  }

  private static <T extends Number> void checkParametersProbabilities(
      List<EmpiricOption<T>> parameters) {
    double probabilitiesSum = parameters.stream().mapToDouble(EmpiricOption::probability).sum();
    if (probabilitiesSum != 1) {
      throw new IllegalArgumentException(
          String.format(
              "Empiric options %s do not add up to probability 1! Sum of probabilities: %f",
              parameters, probabilitiesSum));
    }
  }

  protected abstract void initializeParameterGenerators();
}
