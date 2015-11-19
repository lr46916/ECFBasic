package hr.fer.zemris.optim.algorithms.evol.gat;

import hr.fer.zemris.optim.LocalSearchAlgorithm;
import hr.fer.zemris.optim.NeighbourhoodGenerator;
import hr.fer.zemris.optim.evol.Evaluator;
import hr.fer.zemris.optim.evol.IOptAlgorithm;

public class TreeAlgorithm<T> implements LocalSearchAlgorithm<T> {

	private int Z;
	private NeighbourhoodGenerator<T> ng;
	private Evaluator<T> eval;
	private ConditionsExaminer<T> examiner;
	private T startSolution;
	private T bestOne;

	public TreeAlgorithm(int z, NeighbourhoodGenerator<T> ng,
			Evaluator<T> eval, ConditionsExaminer<T> examiner, T startSolution) {
		super();
		Z = z;
		this.ng = ng;
		this.eval = eval;
		this.examiner = examiner;
		this.startSolution = startSolution;
		this.bestOne = startSolution;
	}

	@Override
	public T run() {
		Counter c = new Counter(0);
		return treePart(startSolution, c);
	}

	private T treePart(T sb, Counter counter) {
		for (T s : ng.neighbourhood(sb)) {
			eval.evaluate(s);
			if (counter.c++ > Z) {
				return null;
			}
			// if (counter.c % 1000 == 0) {
			// System.out.format("Passed %d units, current unit " + sb
			// + System.getProperty("line.separator"), counter.c);
			// }
			if (examiner.compare(bestOne, s) < 0) {
				return s;
			} else {
				if (examiner.compare(s, sb) > 0) {
					System.out.println("TreeAlg: Best solution update, Z="
							+ counter.c + ": " + bestOne);
					T res = treePart(s, counter);
					if (res != null)
						return res;
				}
			}
		}
		return null;
	}

	private static class Counter {
		public int c;

		public Counter(int c) {
			super();
			this.c = c;
		}

	}

	@Override
	public void setStartSolution(T solution) {
		this.startSolution = solution;
		this.bestOne = startSolution;
	}

}
