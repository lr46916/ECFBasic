package hr.fer.zemris.optim.evol.algorithms.artifImmune;

import hr.fer.zemris.optim.IOptAlgorithm;
import hr.fer.zemris.optim.Pool;
import hr.fer.zemris.optim.evol.Chromosome;
import hr.fer.zemris.optim.evol.Evaluator;
import hr.fer.zemris.optim.evol.parallel.impl.EvaluationWorker;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public class ClonAlg<T extends Chromosome> implements IOptAlgorithm<T> {

	private HyperMutation<T> mutate;
	private Factory<T> fact;
	private Pool<T> pool;
	private Replicator<T> replicator;
	private double b;
	private Evaluator<T> eval;
	private int sizeOfPop;
	private int iterations;
	private int d;

	public ClonAlg(HyperMutation<T> mutate, Factory<T> fact, Pool<T> pool,
			Replicator<T> replicator, double b, Evaluator<T> eval,
			int sizeOfPop, int iterations, int d) {
		super();
		this.mutate = mutate;
		this.fact = fact;
		this.pool = pool;
		this.replicator = replicator;
		this.b = b;
		this.eval = eval;
		this.sizeOfPop = sizeOfPop;
		this.iterations = iterations;
		this.d = d;
	}

	@Override
	public T run() {
		Queue<T> toEvaluate = new LinkedBlockingQueue<>();
		Queue<T> results = new LinkedBlockingQueue<>();
		int numOfThreads = Runtime.getRuntime().availableProcessors();
		ExecutorService ec = Executors.newFixedThreadPool(numOfThreads);
		for (int i = 0; i < numOfThreads; i++)
			ec.submit(new EvaluationWorker<T>(toEvaluate, results, eval));
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
			Arrays.sort(population, (x,y) -> -x.compareTo(y));
//			long start = System.nanoTime();
			clonePop(population, clones, sizeOfClonePop);
//			System.out.println((System.nanoTime()-start) * 0.0000000001);
//			System.out.println(sizeOfClonePop);
			for (int j = 0; j < sizeOfClonePop; j++) {
				toEvaluate.add(clones[j]);
			}

			for (int j = 0; j < sizeOfClonePop; j++) {
				T res;
				while ((res = results.poll()) == null)
					;
				clones[j] = res;
			}
			Arrays.sort(clones,(x,y) -> -x.compareTo(y));
//			System.out.println(Arrays.toString(clones));
//			System.exit(-1);
			for (int j = 1; j < sizeOfPop - d; j++) {
				pool.free(population[j]);
				population[j] = clones[j];
			}
			for (int j = sizeOfPop - d; j < sizeOfPop; j++) {
				pool.free(population[j]);
				pool.free(clones[j]);
				population[j] = fact.createElement();
				eval.evaluate(population[j]);
			}
			for (int j = sizeOfPop; j < sizeOfClonePop; j++) {
				pool.free(clones[j]);
			}
			if (population[0].compareTo(best) > 0) {
				best = population[0].clone();
				System.out.println("Best sol update, gen " + (i + 1) + ": " + best);
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
			for (int j = 0; j < numOfClones; j++) {
				T clone = pool.getElement();
				replicator.replicateAToB(population[i], clone);
				clones[offset + j] = clone;
				mutate.mutate(clone, i);
			}
			offset += numOfClones;
		}
	}

}
