package hr.fer.zemris.optim.algorithms.localSearch;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

import hr.fer.zemris.optim.IOptAlgorithm;
import hr.fer.zemris.optim.evol.Chromosome;
import hr.fer.zemris.optim.evol.Evaluator;
import hr.fer.zemris.optim.evol.Mutation;
import hr.fer.zemris.optim.evol.algorithms.artifImmune.Factory;
import hr.fer.zemris.optim.evol.mutations.impl.permutation.MultipleSwapMutation;

public class MyLocalAlgorithm<T extends Chromosome> implements IOptAlgorithm<T> {

	private Collection<Mutation<T>> mutations;
	private Factory<T> factory;
	private Evaluator<T> eval;
	private int it;

	public MyLocalAlgorithm(Collection<Mutation<T>> mutations,
			Factory<T> factory, Evaluator<T> eval, int it) {
		super();
		this.mutations = mutations;
		this.factory = factory;
		this.eval = eval;
		this.it = it;
	}

	@Override
	public T run() {
		Comparator<T> comp = (x, y) -> -x.compareTo(y);
		PriorityQueue<T> heap = new PriorityQueue<>(comp);
		T solution = factory.createElement();
		@SuppressWarnings("unchecked")
		T best = (T) solution.clone();
		eval.evaluate(solution);

		System.out.println("Starts with: " + solution);

		for (int i = 0; i < it; i++) {
			for (Mutation<T> mut : mutations) {
				@SuppressWarnings("unchecked")
				T target = (T) solution.clone();
				mut.mutate(target);
				eval.evaluate(target);
				// System.out.println(target.fitness + " " +
				// solution.equals(target));
				heap.add(target);
			}
			solution = heap.remove();
			if (best.compareTo(solution) < 0) {
				best = solution;
				System.out.println("Best solution update, itaration " + i + ":"
						+ best);
			}
			heap.clear();
		}

		System.out.println("Done??");
		return solution;
	}

}
