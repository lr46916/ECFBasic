package hr.fer.zemris.optim.evol.algorithms.artifImmune;

import java.lang.reflect.Array;
import java.util.Arrays;

import hr.fer.zemris.optim.IOptAlgorithm;
import hr.fer.zemris.optim.NeighbourhoodGenerator;
import hr.fer.zemris.optim.Pool;
import hr.fer.zemris.optim.evol.Chromosome;
import hr.fer.zemris.optim.evol.Evaluator;
import hr.fer.zemris.optim.evol.Factory;

public class ClonAlgNeighbourhood<T extends Chromosome> implements
		IOptAlgorithm<T> {

	private NeighbourhoodGenerator<T> ng;
	private Factory<T> fact;
	private double b;
	private Evaluator<T> eval;
	private int sizeOfPop;
	private int iterations;
	private int d;

	public ClonAlgNeighbourhood(NeighbourhoodGenerator<T> ng, Factory<T> fact, double b,
			Evaluator<T> eval, int sizeOfPop, int iterations, int d) {
		super();
		this.ng = ng;
		this.fact = fact;
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
			clonePop(population, clones, sizeOfClonePop);
			
			for (int j = 0; j < sizeOfClonePop; j++) {
				eval.evaluate(clones[j]);
			}

			Arrays.sort(clones, (x, y) -> -x.compareTo(y));
			for (int j = 2; j <= sizeOfPop - d; j++) {
				fact.free(population[sizeOfPop - j]);
				population[sizeOfPop - j] = clones[j];
			}
			for (int j = 0; j < d; j++) {
				fact.free(population[j]);
				population[j] = fact.generateElement();
				eval.evaluate(population[j]);
			}
			for (int j = sizeOfPop - d; j < sizeOfClonePop; j++) {
				fact.free(clones[j]);
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
