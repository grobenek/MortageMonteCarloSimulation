package szathmary.peter.randomgenerators;

/** Created by petos on 27/02/2024. */
public class DeterministicRandomGenerator<T extends Number> extends Generator<T> {
  private final T value;

  public DeterministicRandomGenerator(T value) {
    this.value = value;
  }

  @Override
  public T sample() {
    return value;
  }
}
