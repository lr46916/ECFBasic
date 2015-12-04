package hr.fer.zemris.optim.evol.algorithms.gat;

import java.lang.reflect.Array;
import java.util.Arrays;

import hr.fer.zemris.optim.NeighbourhoodGenerator;
import hr.fer.zemris.optim.evol.Evaluator;
import hr.fer.zemris.optim.evol.IOptAlgorithm;
import hr.fer.zemris.optim.evol.populationgenerator.PopulationGenerator;

public class GaT<T> implements IOptAlgorithm<T> {
	private int M;
	private int C;
	private int I;
	private int Z;
	private PopulationGenerator<T> pg;
	private NeighbourhoodGenerator<T> ng;
	private Evaluator<T> eval;
	private ConditionsExaminer<T> examiner;
	private T bestOne;

	public GaT(int m, int c, int i, int z, PopulationGenerator<T> pg,
			NeighbourhoodGenerator<T> ng, Evaluator<T> eval,
			ConditionsExaminer<T> examiner) {
		super();
		M = m;
		C = c;
		I = i;
		Z = z;
		this.pg = pg;
		this.ng = ng;
		this.eval = eval;
		this.examiner = examiner;
	}

	@Override
	public T run() {
		int it = 0;
		int CN = 0;
		Counter counter = new Counter(0);
		T[] population = pg.generatePopulation(M * C);
		@SuppressWarnings("unchecked")
		T[] nextGen = (T[]) Array.newInstance(population[0].getClass(), M * C);
		bestOne = population[0];
		while (it < I && counter.c < Z) {
			for (T sb : population) {
				eval.evaluate(sb);
				if (examiner.compare(bestOne, sb) < 0) {
					bestOne = sb;
					if (examiner.over(bestOne)) {
						return sb;
					} else {
						if (examiner.treePartCondtitionsMet(bestOne)) {
							System.out
									.println("Entering tree part of the algotihm with "
											+ sb);
							return treePart(sb, counter);
						}
					}
				}
				counter.c++;
			}
			Arrays.sort(population, (x, y) -> -examiner.compare(x, y));
			for (int i = 0; i < M; i++) {
				int j = 0;
				for (T s : ng.neighbourhoodPart(population[i], C)) {
					nextGen[j + i * M] = s;
					j++;
				}
			}
			T[] hlp = population;
			population = nextGen;
			nextGen = hlp;
			if (it % 10 == 0)
				System.out.println("Iteration " + it + ", " + counter.c + ", best result: "
						+ population[0]);
			it++;
		}

		return null;
	}

	private T treePart(T sb, Counter counter) {
		for (T s : ng.neighbourhood(sb)) {
			eval.evaluate(s);
			if (counter.c++ > Z) {
				return null;
			}
//			if (counter.c % 1000 == 0) {
//				System.out.format("Passed %d units, current unit " + sb
//						+ System.getProperty("line.separator"), counter.c);
//			}
			if (examiner.over(s)) {
				return s;
			} else {
				if (examiner.compare(s, sb) > 0) {
					System.out.format("Found better one, %d, " + s + "\n",counter.c);
//					 CN = (int) s.features[0];
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

}
