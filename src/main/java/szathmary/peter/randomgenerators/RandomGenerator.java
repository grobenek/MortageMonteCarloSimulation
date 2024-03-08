package szathmary.peter.randomgenerators;

import java.util.Random;

/** Created by petos on 27/02/2024. */
public abstract class RandomGenerator<T extends Number> extends Generator<T> {
  private static final Random seedGenerator = new Random();
  protected final Random randomGenerator;

  public RandomGenerator(long seed) {
    this.randomGenerator = new Random(seed);
  }

  public RandomGenerator() {
    this.randomGenerator = new Random(seedGenerator.nextLong());
  }
}
