package hr.fer.zemris.optim.evol.multiobj.algorithms.nsga;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import hr.fer.zemris.optim.IMultiObjOptAlgorithm;
import hr.fer.zemris.optim.evol.Chromosome;
import hr.fer.zemris.optim.evol.Crossover;
import hr.fer.zemris.optim.evol.Evaluator;
import hr.fer.zemris.optim.evol.Mutation;
import hr.fer.zemris.optim.evol.multiobj.MultiObjective;
import hr.fer.zemris.optim.evol.populationgenerator.PopulationGenerator;

public class NSGA2<T extends Chromosome & MultiObjective> implements IMultiObjOptAlgorithm<T> {

	private PopulationGenerator<T> populationGenerator;
	private Mutation<T> mutation;
	private Crossover<T> crossover;
	private Evaluator<T> evaluator;
	private int maxNumberOfIterations;
	private int sizeOfPopulation;
	private int k;

	public NSGA2(PopulationGenerator<T> populationGenerator, Mutation<T> mutation, Crossover<T> crossover,
			Evaluator<T> evaluator, int maxNumberOfIterations, int sizeOfPopulation, int k) {
		super();
		this.populationGenerator = populationGenerator;
		this.mutation = mutation;
		this.crossover = crossover;
		this.evaluator = evaluator;
		this.maxNumberOfIterations = maxNumberOfIterations;
		this.sizeOfPopulation = sizeOfPopulation;
		this.k = k;
	}

	@Override
	public List<T> run() {

		List<T> popualtion = runAndReturnFinishingPopualtion();

		@SuppressWarnings("unchecked")
		T[] popArray = (T[]) Array.newInstance(popualtion.get(0).getClass(), popualtion.size());
		
		int offset = 0;
		for(T elem : popualtion)
			popArray[offset++] = elem;
		
		List<ArrayList<Integer>> fronts = NSGAUtils.sortInDominationFronts(popArray);

		List<T> result = new ArrayList<>();

		for (int index : fronts.get(0)) {
			result.add(popArray[index]);
		}

		return result;
	}

	@Override
	public List<T> runAndReturnFinishingPopualtion() {
		T[] population = populationGenerator.generatePopulation(sizeOfPopulation);

		double[] distances = new double[sizeOfPopulation << 1];
		int[] indexToFront = new int[sizeOfPopulation];
		Integer[] selectionRes = new Integer[k];

		for (int i = 0; i < sizeOfPopulation; i++) {
			evaluator.evaluate(population[i]);
		}

		@SuppressWarnings("unchecked")
		T[] union = (T[]) Array.newInstance(population[0].getClass(), population.length << 1);

		for (int i = 0; i < maxNumberOfIterations; i++) {

			for (int j = 0; j < population.length; j++) {
				union[j] = population[j];
			}

			List<ArrayList<Integer>> sortedPop = NSGAUtils.sortInDominationFronts(population);
			
			for (ArrayList<Integer> front : sortedPop) {
				NSGAUtils.calculateDistance(front, population, distances);
			}

			NSGAUtils.createArraysOfFrontsByElemIndexes(sortedPop, population.length, indexToFront);

			int totalSize = population.length;
			while (totalSize < union.length) {
				Integer[] selected = NSGAUtils.doGroupingKTournamentSelection(indexToFront, population, selectionRes,
						distances);

				T[] children = crossover.doCrossover(population[selected[0]], population[selected[1]]);

				for (int l = 0; l < children.length && totalSize < union.length; l++) {
					mutation.mutate(children[l]);

					// could be costly...
					evaluator.evaluate(children[l]);

					union[totalSize++] = children[l];
				}
			}

			List<ArrayList<Integer>> sortedUnion = NSGAUtils.sortInDominationFronts(union);

			int total = 0;
			ArrayList<Integer> lastFront = null;
			for (ArrayList<Integer> front : sortedUnion) {
				if (total + front.size() < sizeOfPopulation) {
					for (int index : front) {
						population[total++] = union[index];
					}
				} else {
					lastFront = front;
					// System.out.println(
					// "popLen: " + population.length + ", total: " + total + ",
					// frontSize: " + front.size());
					break;
				}
			}

			// System.out.println(lastFront.size() + " ..... " +
			// (population.length - total));

			NSGAUtils.calculateDistance(lastFront, union, distances);

			// TODO this can be optimized using k-th (1-st) order statistics if
			// |population.length - total| is small, calculating 1-st order stat
			// |population.length - total| times.
			Collections.sort(lastFront, (a, b) -> Double.compare(distances[a], distances[b]));
			List<Integer> toAdd = lastFront.subList(0, population.length - total);
			for (int index : toAdd) {
				population[total++] = union[index];
			}
		}

		List<T> result = new ArrayList<>();

		for (T elem : population)
			result.add(elem);

		return result;
	}

}
