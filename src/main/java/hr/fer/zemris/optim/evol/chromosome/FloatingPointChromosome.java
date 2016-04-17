package hr.fer.zemris.optim.evol.chromosome;

import java.util.Arrays;

import hr.fer.zemris.optim.evol.Chromosome;

public class FloatingPointChromosome extends Chromosome {

	public double[] data;

	public FloatingPointChromosome(double[] data) {
		super();
		this.data = data;
	}

	public FloatingPointChromosome(int size) {
		super();
		this.data = new double[size];
	}

	@Override
	public Chromosome clone() {
		FloatingPointChromosome res = new FloatingPointChromosome(Arrays.copyOf(data, data.length));
		res.fitness = this.fitness;
		return res;
	}

	@Override
	public Chromosome newLikeThis() {
		return new FloatingPointChromosome(new double[data.length]);
	}

	@Override
	public String toString() {
		return Arrays.toString(data) + " -> " + fitness;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof FloatingPointChromosome) {
			return Arrays.equals(data, ((FloatingPointChromosome) o).data);
		} else
			return false;
	}

	private Integer hc = null;

	@Override
	public int hashCode() {
		if (hc == null) {
			hc = Arrays.hashCode(data);
		}
		return hc;
	}

}
