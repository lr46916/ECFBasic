package hr.fer.zemris.optim.evol.multiobj.algorithms.nsga;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.TreeSet;

import hr.fer.zemris.optim.evol.Chromosome;
import hr.fer.zemris.optim.evol.multiobj.MultiObjective;
import hr.fer.zemris.optim.evol.multiobj.MultiObjective;
import hr.fer.zemris.optim.rng.IRNG;
import hr.fer.zemris.optim.rng.RNG;

public class NSGAUtils {

	public static int[] createArraysOfFrontsByElemIndexes(List<ArrayList<Integer>> sortedPopulation, int totalSize,
			int[] result) {

		int c = 0;
		for (ArrayList<Integer> front : sortedPopulation) {
			for (int index : front) {
				result[index] = c;
			}
			c++;
		}

		return result;
	}

	/**
	 * It is assumed that k is a small number (around at most 10)
	 * 
	 */
	public static <T extends MultiObjective> Integer[] doGroupingKTournamentSelection(int[] elementFronts,
			T[] population, Integer[] result, double[] distance) {

		if (elementFronts.length != population.length)
			throw new IllegalArgumentException("Each element in population must have a front");

		Set<Integer> set = new TreeSet<>();

		IRNG rand = RNG.getRNG();

		// Array sort can't work with costum comparators on native int
		// index in result array
		int c = 0;
		// for this loop it is important that size of wanted selected elements
		// is small (length of results field)
		while (set.size() != result.length) {
			int index = rand.nextInt(population.length);
			if (set.add(index)) {
				result[c++] = index;
			}
		}

		Arrays.sort(result,
				(one, two) -> elementFronts[one] == elementFronts[two] ? Double.compare(distance[two], distance[one])
						: Integer.compare(elementFronts[two], elementFronts[one]));

		return result;
	}

	/**
	 * Calculates distance parameters of all
	 * {@link MultiObjectiveChromosomeNSGA2} in given population. THIS METHOD
	 * MAY CHANGE ORDERING OF ELEMENTS IN EACH FRONT.
	 * 
	 * @param domSortedPop
	 */
	public static <T extends MultiObjective> void calculateDistance(ArrayList<Integer> indexes, T[] population,
			double[] distance) {

		int m = population[0].componentsCount();

		double[] max = new double[m];
		double[] min = new double[m];

		Iterator<Integer> it = indexes.iterator();
		int firstIndex = it.next();
		distance[firstIndex] = 0;

		for (int i = 0; i < m; i++) {
			max[i] = population[firstIndex].getComponent(i);
			min[i] = population[firstIndex].getComponent(i);
		}

		while (it.hasNext()) {
			int index = it.next();
			T elem = population[index];
			distance[index] = 0;
			for (int i = 0; i < m; i++) {
				double elemCompVal = elem.getComponent(i);
				if (elemCompVal > max[i]) {
					max[i] = elemCompVal;
				} else {
					if (elemCompVal < min[i]) {
						min[i] = elemCompVal;
					}
				}
			}
		}

		for (int i = 0; i < m; i++) {
			final int ind = i;
			Collections.sort(indexes, (ind1, ind2) -> Double.compare(population[ind1].getComponent(ind),
					population[ind2].getComponent(ind)));

			int size = indexes.size();

			// population[indexes.get(0)].distance = Double.POSITIVE_INFINITY;
			// population[indexes.get(size - 1)].distance =
			// Double.POSITIVE_INFINITY;
			distance[indexes.get(0)] = Double.POSITIVE_INFINITY;
			distance[indexes.get(size - 1)] = Double.POSITIVE_INFINITY;

			for (int j = 1; j < size - 1; j++) {
				distance[indexes.get(j)] += (Math.abs(population[indexes.get(j - 1)].getComponent(i)
						- population[indexes.get(j + 1)].getComponent(i))) / (max[i] - min[i]);
			}
		}

	}

	/**
	 * Sorts given population in domination fronts. First front (on index 0 of
	 * the list) is most dominant (there is no other element in whole population
	 * that dominates any element in first front).
	 * 
	 * @param population
	 *            population to sort
	 * @return given population sorted in fronts
	 */
	public static <T extends MultiObjective> List<ArrayList<Integer>> sortInDominationFronts(T[] population) {

		@SuppressWarnings("unchecked")
		List<Integer>[] dominated = new List[population.length];
		int[] parents = new int[population.length];

		for (int i = 0; i < population.length; i++) {
			dominated[i] = new ArrayList<Integer>();
		}

		for (int i = 0; i < population.length - 1; i++) {
			T current = population[i];
			for (int j = i + 1; j < population.length; j++) {
				switch (dominates(current, population[j])) {
				case -1:
					dominated[j].add(i);
					parents[i]++;
					break;
				case 1:
					dominated[i].add(j);
					parents[j]++;
					break;
				}
			}
		}

		Queue<Tuple> open = new LinkedList<>();

		for (int i = 0; i < population.length; i++) {
			if (parents[i] == 0) {
				open.add(new Tuple(i, 0));
			}
		}

		int currDepth = -1;
		ArrayList<Integer> layer = new ArrayList<>();
		List<ArrayList<Integer>> result = new ArrayList<>();

		while (!open.isEmpty()) {

			Tuple current = open.poll();

			if (current.depth != currDepth) {
				if (currDepth != -1)
					result.add(layer);
				layer = new ArrayList<>();
				currDepth = current.depth;
			}
			layer.add(current.index);

			for (int next : dominated[current.index]) {
				if (--parents[next] == 0) {
					open.add(new Tuple(next, current.depth + 1));
				}
			}

		}
		if (!layer.isEmpty())
			result.add(layer);
		return result;
	}

	/**
	 * Sorts given population in domination fronts. First front (on index 0 of
	 * the list) is most dominant (there is no other element in whole population
	 * that dominates any element in first front).
	 * 
	 * @param population
	 *            population to sort
	 * @param dominationComparator
	 *            comparator used to compare two Chromosomes (solutions) to
	 *            determine whether one dominates the other. If called for A and
	 *            B it should return 1 if A dominates B, -1 if B dominates A and
	 *            0 otherwise.
	 * @return given population sorted in fronts
	 */
	public static <T extends MultiObjective> List<ArrayList<Integer>> sortInDominationFronts(T[] population,
			Comparator<T> dominationComparator) {

		@SuppressWarnings("unchecked")
		List<Integer>[] dominated = new List[population.length];
		int[] parents = new int[population.length];

		for (int i = 0; i < population.length; i++) {
			dominated[i] = new ArrayList<Integer>();
		}

		for (int i = 0; i < population.length - 1; i++) {
			T current = population[i];
			for (int j = i + 1; j < population.length; j++) {
				switch (dominationComparator.compare(current, population[j])) {
				case -1:
					dominated[j].add(i);
					parents[i]++;
					break;
				case 1:
					dominated[i].add(j);
					parents[j]++;
					break;
				}
			}
		}

		Queue<Tuple> open = new LinkedList<>();

		for (int i = 0; i < population.length; i++) {
			if (parents[i] == 0) {
				open.add(new Tuple(i, 0));
			}
		}

		int currDepth = -1;
		ArrayList<Integer> layer = new ArrayList<>();
		List<ArrayList<Integer>> result = new ArrayList<>();

		while (!open.isEmpty()) {

			Tuple current = open.poll();

			if (current.depth != currDepth) {
				if (currDepth != -1)
					result.add(layer);
				layer = new ArrayList<>();
				currDepth = current.depth;
			}
			layer.add(current.index);

			for (int next : dominated[current.index]) {
				if (--parents[next] == 0) {
					open.add(new Tuple(next, current.depth + 1));
				}
			}

		}
		if (!layer.isEmpty())
			result.add(layer);
		return result;
	}

	/**
	 * This method takes two MultiObjectiveChromosomes and returns their
	 * domination relation. It is assumed that all parts of fitness functions
	 * are to be MAXIMIZED.
	 * 
	 * @param one
	 * @param two
	 * @return 1 if first element dominates second, -1 if second element
	 *         dominates first, 0 if neither of them dominates the other
	 */
	public static int dominates(MultiObjective one, MultiObjective two) {

		int res = 0;

		int m = one.componentsCount();
		
		for (int i = 0; i < m; i++) {
			double compOne = one.getComponent(i);
			double compTwo = two.getComponent(i);
			if (compOne > compTwo) {
				if (res == -1) {
					return 0;
				}
				res = 1;
			} else {
				if (compOne < compTwo) {
					if (res == 1) {
						return 0;
					}
					res = -1;
				}
			}
		}

		return res;
	}

	private static class Tuple {
		int index;
		int depth;

		public Tuple(int index, int depth) {
			super();
			this.index = index;
			this.depth = depth;
		}

	}

	public static void main(String[] args) {

		TestChromosome[] tmp = new TestChromosome[6];

		double[] distances = new double[tmp.length];

		tmp[0] = new TestChromosome(12, 1, 1);
		tmp[1] = new TestChromosome(4, 4, 2);
		tmp[2] = new TestChromosome(1, 6, 3);
		tmp[3] = new TestChromosome(10, 2, 4);
		tmp[4] = new TestChromosome(3, 5, 5);
		tmp[5] = new TestChromosome(9, 3, 6);

		List<ArrayList<Integer>> res = sortInDominationFronts(tmp);
		calculateDistance(res.get(0), tmp, distances);

		// for (int x : doGroupingKTournamentSelection(res, tmp, 3)) {
		// System.out.println(x + 1);
		// }

	}

	private static class TestChromosome extends Chromosome implements MultiObjective {

		public int val;
		private double[] fitness;

		public TestChromosome(double first, double second, int val) {
			this.fitness = new double[2];
			this.fitness[0] = first;
			this.fitness[1] = second;
			this.val = val;
		}

		public String toString() {
			return val + " -> (" + fitness[0] + ", " + fitness[1] + ")";
		}

		@Override
		public Chromosome clone() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Chromosome newLikeThis() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public int componentsCount() {
			return fitness.length;
		}

		@Override
		public double getComponent(int index) {
			return fitness[index];
		}

	}

}
