package hr.fer.zemris.optim.algorithms.localSearch;

import hr.fer.zemris.optim.LocalSearchAlgorithm;
import hr.fer.zemris.optim.NeighbourhoodGenerator;
import hr.fer.zemris.optim.evol.Chromosome;
import hr.fer.zemris.optim.evol.Evaluator;

public class GreedyAlgorthm<T extends Chromosome> implements
		LocalSearchAlgorithm<T> {

	private T start;
	private NeighbourhoodGenerator<T> ng;
	private Evaluator<T> evaluator;

	public GreedyAlgorthm(T start, NeighbourhoodGenerator<T> ng, Evaluator<T> evaluator) {
		super();
		this.start = start;
		this.ng = ng;
		this.evaluator = evaluator;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T run() {
		T init = (T) start.clone();
		evaluator.evaluate(init);
		T res = searchForBest(init);
		return res;
	}

	private T searchForBest(T seed) {
		for (T it : ng.neighbourhood(seed)) {
			evaluator.evaluate(it);
//			System.out.println(seed + "::::" + it);
			if (it.compareTo(seed) > 0) {
//				return it;
				System.out.println(it);
				T res = searchForBest(it);
				if(res.equals(seed))
					return res;
			}
		}
		return seed;
	}

	@Override
	public void setStartSolution(T solution) {
		start = solution;
	}

}
