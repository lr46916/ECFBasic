package hr.fer.zemris.optim.algorithms.evol.artifImmune;

import java.lang.reflect.Array;
import java.util.Arrays;

import hr.fer.zemris.optim.NeighbourhoodGenerator;
import hr.fer.zemris.optim.Pool;
import hr.fer.zemris.optim.evol.Chromosome;
import hr.fer.zemris.optim.evol.Evaluator;
import hr.fer.zemris.optim.evol.IOptAlgorithm;

public class ClonAlgNeighbourhood<T extends Chromosome> implements
		IOptAlgorithm<T> {

	private NeighbourhoodGenerator<T> ng;
	private Factory<T> fact;
	private Pool<T> pool;
	private double b;
	private Evaluator<T> eval;
	private int sizeOfPop;
	private int iterations;
	private int d;

	public ClonAlgNeighbourhood(NeighbourhoodGenerator<T> ng, Factory<T> fact,
			Pool<T> pool, double b,
			Evaluator<T> eval, int sizeOfPop, int iterations, int d) {
		super();
		this.ng = ng;
		this.fact = fact;
		this.pool = pool;
		this.b = b;
		this.eval = eval;
		this.sizeOfPop = sizeOfPop;
		this.iterations = iterations;
		this.d = d;
	}

	@Override
	public T run() {
		T[] population = fact.generatePopulation(sizeOfPop);
		int sizeOfClonePop = 0;
		Chromosome best = population[0];
		for (int i = 0; i < sizeOfPop; i++) {
			eval.evaluate(population[i]);
			if (population[i].compareTo(best) > 0)
				best = population[i].clone();
			sizeOfClonePop += ((b * sizeOfPop) / (i + 1));
		}
		System.out.println(sizeOfClonePop);
		@SuppressWarnings("unchecked")
		T[] clones = (T[]) Array.newInstance(population[0].getClass(),
				sizeOfClonePop);
		System.out.println("Starts with: " + best);
		for (int i = 0; i < iterations; i++) {
			Arrays.sort(population, (x, y) -> x.compareTo(y));
			if (population[sizeOfPop - 1].compareTo(best) > 0) {
				best = population[sizeOfPop-1].clone();
				System.out.println("Best sol update, gen " + (i + 1) + ": "
						+ best);
			}
			// long start = System.nanoTime();
			clonePop(population, clones, sizeOfClonePop);
			// System.out.println((System.nanoTime()-start) * 0.0000000001);
			// System.out.println(sizeOfClonePop);
			
			for (int j = 0; j < sizeOfClonePop; j++) {
				eval.evaluate(clones[j]);
			}

			Arrays.sort(clones, (x, y) -> -x.compareTo(y));
			// System.out.println(Arrays.toString(clones));
			// System.exit(-1);
			for (int j = 2; j <= sizeOfPop - d; j++) {
				pool.free(population[sizeOfPop - j]);
				population[sizeOfPop - j] = clones[j];
			}
			for (int j = 0; j < d; j++) {
				pool.free(population[j]);
				population[j] = fact.createElement();
				eval.evaluate(population[j]);
			}
			for (int j = sizeOfPop - d; j < sizeOfClonePop; j++) {
				pool.free(clones[j]);
			}

		}
		@SuppressWarnings("unchecked")
		T returnValue = (T) best;
		return returnValue;
	}

	private void clonePop(T[] population, T[] clones, int sizeOfClonePop) {
		int offset = 0;
		for (int i = 0; i < sizeOfPop; i++) {
			int numOfClones = (int) ((b * sizeOfPop) / (i + 1));
			int j = 0;
			for (T neighbour : ng.neighbourhoodPart(population[i], numOfClones)) {
				clones[offset + j] = neighbour;
				j++;
			}
			offset += numOfClones;
		}
	}

}
