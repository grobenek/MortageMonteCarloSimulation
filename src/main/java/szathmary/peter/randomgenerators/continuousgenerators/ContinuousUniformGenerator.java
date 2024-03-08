package szathmary.peter.randomgenerators.continuousgenerators;

import szathmary.peter.randomgenerators.RandomGenerator;

/** Created by petos on 27/02/2024. */
public class ContinuousUniformGenerator extends RandomGenerator<Double> {
  /** Minimal generated value (inclusive) */
  private final double min;

  /** Maximal generated value (exclusive) */
  private final double max;

  public ContinuousUniformGenerator(long seed, double min, double max) {
    super(seed);
    this.min = min;
    this.max = max;
  }

  public ContinuousUniformGenerator(double min, double max) {
    this.min = min;
    this.max = max;
  }

  @Override
  public Double sample() {
    return randomGenerator.nextDouble(min, max);
  }
}
