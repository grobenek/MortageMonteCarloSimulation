package szathmary.peter.simulation.core;

public enum Strategy {
  A(0),
  B(1),
  C(2);

  private final int index;

  Strategy(int index) {
    this.index = index;
  }

  public int getIndex() {
    return index;
  }
}
