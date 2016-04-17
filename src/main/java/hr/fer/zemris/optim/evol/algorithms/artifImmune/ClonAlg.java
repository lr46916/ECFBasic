package hr.fer.zemris.optim.evol.algorithms.artifImmune;

import hr.fer.zemris.optim.IOptAlgorithm;
import hr.fer.zemris.optim.Pool;
import hr.fer.zemris.optim.evol.Chromosome;
import hr.fer.zemris.optim.evol.Evaluator;
import hr.fer.zemris.optim.evol.Factory;
import hr.fer.zemris.optim.evol.PopulationAlgorithm;

import java.lang.reflect.Array;
import java.util.Arrays;

public class ClonAlg<T extends Chromosome> implements PopulationAlgorithm<T> {

	private HyperMutation<T> mutate;
	private Factory<T> fact;
	private Replicator<T> replicator;
	private double b;
	private Evaluator<T> eval;
	private int sizeOfPop;
	private int iterations;
	private int d;

	private int[] numberOfClonesPerSolution;

	/**
	 * 
	 * @param mutate
	 * @param fact
	 * @param pool
	 * @param replicator
	 * @param b
	 *            Parameter defining size of clone population.
	 * @param eval
	 * @param sizeOfPop
	 * @param iterations
	 * @param d
	 *            Number of fleshly (randomly) generated solutions each
	 *            iteration.
	 */
	public ClonAlg(HyperMutation<T> mutate, Factory<T> fact, Replicator<T> replicator, double b, Evaluator<T> eval,
			int sizeOfPop, int iterations, int d) {
		super();
		this.mutate = mutate;
		this.fact = fact;
		this.replicator = replicator;
		this.b = b;
		this.eval = eval;
		this.sizeOfPop = sizeOfPop;
		this.iterations = iterations;
		this.d = d;

		this.numberOfClonesPerSolution = new int[sizeOfPop];

	}

	@Override
	public T run() {
		T[] population = runAndReturnFinishingPopulation();
		T res = population[0];
		for (int i = 1; i < population.length; ++i) {
			if (res.compareTo(population[i]) < 0) {
				res = population[i];
			}
		}
		return res;
	}

	@Override
	public T[] runAndReturnFinishingPopulation() {
		T[] population = fact.generatePopulation(sizeOfPop);
		int sizeOfClonePop = 0;
		Chromosome best = population[0];
		for (int i = 0; i < sizeOfPop; i++) {
			eval.evaluate(population[i]);
			if (population[i].compareTo(best) > 0)
				best = population[i].clone();
			sizeOfClonePop += ((b * sizeOfPop) / (i + 1));
			numberOfClonesPerSolution[i] = (int) ((b * sizeOfPop) / (i + 1));
		}
		System.out.println(sizeOfClonePop);
		@SuppressWarnings("unchecked")
		T[] clones = (T[]) Array.newInstance(population[0].getClass(), sizeOfClonePop);
		System.out.println("Starts with: " + best.fitness);
		for (int i = 0; i < iterations; i++) {
			Arrays.sort(population, (x, y) -> -x.compareTo(y));
			clonePop(population, clones, sizeOfClonePop);

			mutate.mutate(clones);
			eval.evaluate(clones);

			Arrays.sort(clones, (x, y) -> -x.compareTo(y));

			for (int j = 0; j < sizeOfPop - d; j++) {
				fact.free(population[j]);
				population[j] = clones[j];
			}
			for (int j = sizeOfPop - d; j < sizeOfPop; j++) {
				fact.free(population[j]);
				fact.free(clones[j]);
				population[j] = fact.generateElement();
				eval.evaluate(population[j]);
			}
			for (int j = sizeOfPop; j < sizeOfClonePop; j++) {
				fact.free(clones[j]);
			}
			if (population[0].compareTo(best) > 0) {
				best = population[0].clone();
				System.out.println("Best sol update, gen " + (i + 1) + ": " + best.fitness);
			}
		}

		return population;
	}

	private void clonePop(T[] population, T[] clones, int sizeOfClonePop) {
		int offset = 0;
		for (int i = 0; i < sizeOfPop; i++) {
			int numOfClones = numberOfClonesPerSolution[i];
			for (int j = 0; j < numOfClones; j++) {
				T clone = fact.getElement();
				replicator.replicateAToB(population[i], clone);
				clones[offset + j] = clone;
			}
			offset += numOfClones;
		}
	}

}
