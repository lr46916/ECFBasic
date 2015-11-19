package hr.fer.zemris.optim.evol.crossovers.permutation;

import java.lang.reflect.Array;

import hr.fer.zemris.optim.evol.Chromosome;
import hr.fer.zemris.optim.evol.crossovers.Crossover;
import hr.fer.zemris.optim.rng.IRNG;
import hr.fer.zemris.optim.rng.RNG;

public class MultipleChoiceCrossover<T extends Chromosome> implements
		Crossover<T> {

	private Crossover<T>[] crossovers;
	private double[] poss;
	private double crossoverPoss;

	public MultipleChoiceCrossover(Crossover<T>[] crossovers, double[] poss) {
		super();
		if (crossovers.length != poss.length) {
			throw new IllegalArgumentException();
		}
		this.crossovers = crossovers;
		this.poss = poss;
		this.crossoverPoss = -1;
	}

	public MultipleChoiceCrossover(Crossover<T>[] crossovers, double[] poss,
			double crossoverPoss) {
		super();
		if (crossovers.length != poss.length) {
			throw new IllegalArgumentException();
		}
		this.crossovers = crossovers;
		this.poss = poss;
		this.crossoverPoss = crossoverPoss;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T[] doCrossover(T parentOne, T parentTwo) {
		IRNG rand = RNG.getRNG();
		if (crossoverPoss != -1) {
			if (crossoverPoss < rand.nextFloat()) {
				T[] ret = (T[]) Array.newInstance(parentOne.getClass(), 2);
				ret[0] = (T) parentOne.clone();
				ret[1] = (T) parentTwo.clone();
				return ret;
			}
		}
		double randomNum = rand.nextDouble();
		double sum = 0;
		for (int i = 0; i < crossovers.length; i++) {
			sum += poss[i];
			if (sum >= randomNum) {
				return crossovers[i].doCrossover(parentOne, parentTwo);
			}
		}
		return null;
	}

}
