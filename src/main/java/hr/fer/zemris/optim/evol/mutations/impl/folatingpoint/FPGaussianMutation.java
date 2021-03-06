package hr.fer.zemris.optim.evol.mutations.impl.folatingpoint;

import hr.fer.zemris.optim.evol.Mutation;
import hr.fer.zemris.optim.evol.chromosome.FloatingPointChromosome;
import hr.fer.zemris.optim.rng.IRNG;
import hr.fer.zemris.optim.rng.RNG;

public class FPGaussianMutation implements Mutation<FloatingPointChromosome> {

	private double mean;
	private double std;
	private double chanceOfMut;

	public FPGaussianMutation(double mean, double std, double chanceOfMut) {
		super();
		this.mean = mean;
		this.std = std;
		this.chanceOfMut = chanceOfMut;
	}

	@Override
	public void mutate(FloatingPointChromosome target) {
		IRNG rand = RNG.getRNG();

		for (int i = 0; i < target.data.length; i++) {
			if (rand.nextFloat() <= chanceOfMut)
				target.data[i] += rand.nextGaussian() * std + mean;
		}

	}

}
