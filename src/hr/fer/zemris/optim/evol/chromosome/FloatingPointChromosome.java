package hr.fer.zemris.optim.evol.chromosome;

import java.util.Arrays;

import hr.fer.zemris.optim.evol.Chromosome;

public class FloatingPointChromosome extends Chromosome {

	public double[] data;

	public FloatingPointChromosome(double[] data) {
		super();
		this.data = data;
	}

	@Override
	public Chromosome clone() {
		return new FloatingPointChromosome(Arrays.copyOf(data, data.length));
	}

	@Override
	public Chromosome newLikeThis() {
		return new FloatingPointChromosome(new double[data.length]);
	}

}
