package hr.fer.zemris.optim.evol.crossovers;

import java.lang.reflect.Array;

import hr.fer.zemris.optim.evol.Chromosome;
import hr.fer.zemris.optim.evol.Crossover;
import hr.fer.zemris.optim.rng.RNG;

public class NoCrossover<T extends Chromosome> implements Crossover<T> {

	@SuppressWarnings("unchecked")
	@Override
	public T[] doCrossover(T parentOne, T parentTwo) {

		T[] res = (T[]) Array.newInstance(parentOne.getClass(), 1);

		if (RNG.getRNG().nextBoolean()) {
			res[0] = (T) parentOne.clone();
		} else {
			res[0] = (T) parentTwo.clone();
		}

		return res;
	}

}
