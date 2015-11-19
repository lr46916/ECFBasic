package hr.fer.zemris.optim.evol.crossovers.impl.floatingpoint;

import hr.fer.zemris.optim.evol.Crossover;
import hr.fer.zemris.optim.evol.chromosome.FloatingPointChromosome;
import hr.fer.zemris.optim.rng.RNG;

public class FPSimpleCrossover implements Crossover<FloatingPointChromosome> {

	@Override
	public FloatingPointChromosome[] doCrossover(FloatingPointChromosome parentOne, FloatingPointChromosome parentTwo) {
		int bp = RNG.getRNG().nextInt(parentOne.data.length);
		
		FloatingPointChromosome[] result = new FloatingPointChromosome[2];
		result[0] = (FloatingPointChromosome) parentOne.newLikeThis();
		result[1] = (FloatingPointChromosome) parentTwo.newLikeThis();
		
		for(int i = 0; i < parentOne.data.length; i++) {
			if(i < bp) {
				result[0].data[i] = parentOne.data[i];
				result[1].data[i] = parentTwo.data[i];
			} else {
				result[1].data[i] = parentOne.data[i];
				result[0].data[i] = parentTwo.data[i];
			}
		}
		
		return result;
	}

}
