package hr.fer.zemris.optim.algorithms.localSearch;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

import hr.fer.zemris.optim.IOptAlgorithm;
import hr.fer.zemris.optim.evol.Chromosome;
import hr.fer.zemris.optim.evol.Evaluator;
import hr.fer.zemris.optim.evol.Factory;
import hr.fer.zemris.optim.evol.Mutation;
import hr.fer.zemris.optim.evol.mutations.impl.permutation.MultipleSwapMutation;

public class MyLocalAlgorithm<T extends Chromosome> implements IOptAlgorithm<T> {

	private Collection<Mutation<T>> mutations;
	private Factory<T> factory;
	private Evaluator<T> eval;
	private int it;

	public MyLocalAlgorithm(Collection<Mutation<T>> mutations, Factory<T> factory, Evaluator<T> eval, int it) {
		super();
		this.mutations = mutations;
		this.factory = factory;
		this.eval = eval;
		this.it = it;
	}

	@Override
	public T run() {
		T solution = factory.generateElement();
		eval.evaluate(solution);

		@SuppressWarnings("unchecked")
		T best = (T) solution.clone();
		
		System.out.println("Starts with: " + solution);

		for (int i = 0; i < it; i++) {
			for (Mutation<T> mut : mutations) {
				@SuppressWarnings("unchecked")
				T target = (T) solution.clone();
				mut.mutate(target);
				eval.evaluate(target);
				if (target.compareTo(solution) > 0)
					solution = target;
			}
			if (solution.compareTo(best) > 0) {
				best = solution;
				System.out.println("Best solution update, itaration " + i + ":" + best);
			}
		}

		System.out.println("Done??");
		return best;
	}

}
