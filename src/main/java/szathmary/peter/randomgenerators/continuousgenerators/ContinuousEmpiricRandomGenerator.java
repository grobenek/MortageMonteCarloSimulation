package szathmary.peter.randomgenerators.continuousgenerators;

import java.util.List;
import szathmary.peter.randomgenerators.RandomGenerator;
import szathmary.peter.randomgenerators.empiricnumbergenerator.EmpiricNumberGenerator;
import szathmary.peter.randomgenerators.empiricnumbergenerator.EmpiricOption;

/** Created by petos on 27/02/2024. */
public class ContinuousEmpiricRandomGenerator extends EmpiricNumberGenerator<Double> {

  public ContinuousEmpiricRandomGenerator(long seed, List<EmpiricOption<Double>> parameters) {
    super(seed, parameters);
  }

  public ContinuousEmpiricRandomGenerator(List<EmpiricOption<Double>> parameters) {
    super(parameters);
  }

  @Override
  protected void initializeParameterGenerators() {
    parameters.forEach(
        parameter ->
            parametersGenerators.add(
                new ContinuousUniformGenerator(parameter.min(), parameter.max())));
  }

  @Override
  public Double sample() {
    double generatedProbabilityOfOption = randomGenerator.nextDouble();
    double comulativeProbability = 0;
    int selectedParameterIndex = -1;

    for (int i = 0; i < parameters.size(); i++) {
      EmpiricOption<Double> parameter = parameters.get(i);
      comulativeProbability += parameter.probability();

      if (generatedProbabilityOfOption <= comulativeProbability) {
        selectedParameterIndex = i;
        break;
      }
    }

    RandomGenerator<Double> generatorOfParameter = parametersGenerators.get(selectedParameterIndex);
    return generatorOfParameter.sample();
  }
}
