package hr.fer.zemris.optim.evol.populationgenerator.impl;

import hr.fer.zemris.optim.evol.chromosome.FloatingPointChromosome;
import hr.fer.zemris.optim.evol.populationgenerator.PopulationGenerator;
import hr.fer.zemris.optim.rng.IRNG;
import hr.fer.zemris.optim.rng.RNG;

public class FloatingPointChromosomePG implements PopulationGenerator<FloatingPointChromosome> {

	private int solutionSize;

	public FloatingPointChromosomePG(int solutionSize) {
		super();
		this.solutionSize = solutionSize;
	}

	@Override
	public FloatingPointChromosome[] generatePopulation(int sizeOfPop) {

		FloatingPointChromosome[] result = new FloatingPointChromosome[sizeOfPop];

		IRNG rand = RNG.getRNG();

		for (int i = 0; i < sizeOfPop; i++) {

			double[] data = new double[solutionSize];
			
			for(int j = 0; j < solutionSize; j++) {
				data[j] = rand.nextGaussian() + 100;
			}

			result[i] = new FloatingPointChromosome(data);
		}

		return result;
	}

}
