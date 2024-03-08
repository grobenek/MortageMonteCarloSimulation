package szathmary.peter.randomgenerators.discretegenerators;

import szathmary.peter.randomgenerators.RandomGenerator;

/** Created by petos on 27/02/2024. */
public class DiscreteUniformRandomGenerator extends RandomGenerator<Integer> {

  private final int min;
  private final int max;

  public DiscreteUniformRandomGenerator(long seed, int min, int max) {
    super(seed);

    this.min = min;
    this.max = max;
  }

  public DiscreteUniformRandomGenerator(int min, int max) {
    this.min = min;
    this.max = max;
  }

  @Override
  public Integer sample() {
    return randomGenerator.nextInt(min, max);
  }
}
