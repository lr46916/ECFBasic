package hr.fer.zemris.optim.evol.algorithms.ga.generation.parallel;

import java.lang.reflect.Array;
import java.util.Queue;

import hr.fer.zemris.optim.Prototype;
import hr.fer.zemris.optim.Tuple;
import hr.fer.zemris.optim.evol.Chromosome;
import hr.fer.zemris.optim.evol.Crossover;
import hr.fer.zemris.optim.evol.Evaluator;
import hr.fer.zemris.optim.evol.Mutation;
import hr.fer.zemris.optim.evol.algorithms.ga.GeneticAlgorithm;
import hr.fer.zemris.optim.evol.algorithms.ga.generation.GenerationGAAbs;
import hr.fer.zemris.optim.evol.parallel.Operation;
import hr.fer.zemris.optim.evol.parallel.executor.QueueData;
import hr.fer.zemris.optim.evol.parallel.executor.TaskExecutor;
import hr.fer.zemris.optim.evol.populationgenerator.PopulationGenerator;
import hr.fer.zemris.optim.evol.selection.Selection;

public class GenerationGAParallelEval<T extends Chromosome, E extends Evaluator<T> & Prototype> extends GenerationGAAbs<T> {

	private E evaluator;
	private int numberOfThreads;

	public GenerationGAParallelEval(int sizeOfPop, PopulationGenerator<T> pg, Crossover<T> crossover,
			Mutation<T> mutation, E evaluator, int iterations, Selection maleSelection,
			Selection femaleSelection, int threads) {
		super(sizeOfPop, pg, crossover, mutation, evaluator, iterations, maleSelection, femaleSelection);
		this.evaluator = evaluator;
		this.numberOfThreads = threads;
	}

	private void doEvaluation(T[] population, Tuple<Integer, T>[] inputForEvaluation,
			Queue<Tuple<Integer, T>> inputQueue, Queue<Tuple<Integer, Double>> resultsQueue) {
		for (int i = 0; i < sizeOfPop; i++) {
			inputForEvaluation[i] = new Tuple<Integer, T>(i, population[i]);
			inputQueue.offer(inputForEvaluation[i]);
		}

		Tuple<Integer, Double> res;

		for (int i = 0; i < sizeOfPop; i++) {
			while ((res = resultsQueue.poll()) == null)
				;
			population[res._1].fitness = res._2;
		}
	}

	@Override
	public T[] runAndReturnFinishingPopulation() {
		
		TaskExecutor ta = TaskExecutor.getInstance();

		QueueData<Tuple<Integer, T>, Tuple<Integer, Double>> queueData = ta
				.createTaskQueue(new EvaluateChromosome<>(evaluator), numberOfThreads, new Tuple<>(-1, null));

		Queue<Tuple<Integer, T>> inputQueue = queueData.getInputQueue();
		Queue<Tuple<Integer, Double>> resultsQueue = queueData.getResultQueue();

		T[] population = pg.generatePopulation(sizeOfPop);

		@SuppressWarnings("unchecked")
		Tuple<Integer, T>[] inputForEvaluation = new Tuple[sizeOfPop];

		doEvaluation(population, inputForEvaluation, inputQueue, resultsQueue);

		@SuppressWarnings("unchecked")
		T best = (T) GeneticAlgorithm.findBest(population).clone();

		System.out.println("Starting with: " + best.fitness);

		@SuppressWarnings("unchecked")
		T[] nextGeneration = (T[]) Array.newInstance(population[0].getClass(), sizeOfPop);
		
		for (int i = 0; i < iterations; i++) {
			int nextGenSize = 1;
			nextGeneration[0] = (T) best.clone();
			while (nextGenSize < sizeOfPop) {
				int firstParentIndex = maleSelection.doSelection(population);
				int secondParentIndex = femaleSelection.doSelection(population);

				int c = 0;
				while (firstParentIndex == secondParentIndex) {
					secondParentIndex = femaleSelection.doSelection(population);
					if (c++ > 5) {
						secondParentIndex = (firstParentIndex + 1) % sizeOfPop;
					}
				}

				T[] children = crossover.doCrossover(population[firstParentIndex], population[secondParentIndex]);

				for (int k = 0; k < children.length && nextGenSize < sizeOfPop; k++) {
					mutation.mutate(children[k]);
					nextGeneration[nextGenSize++] = children[k];
				}
			}
			doEvaluation(nextGeneration, inputForEvaluation, inputQueue, resultsQueue);
			T bestNextGen = (T) GeneticAlgorithm.findBest(nextGeneration);
			if (best.compareTo(bestNextGen) < 0) {
				best = (T) bestNextGen.clone();
				System.out.println("Best solution update, generation " + i + ", value:" + best.fitness);
			}
			T[] tmp = population;
			population = nextGeneration;
			nextGeneration = tmp;
		}

		ta.freeTaskQueue(queueData.getQueueId());

		return population;
	}

	private static class EvaluateChromosome<T extends Chromosome, E extends Evaluator<T> & Prototype>
			implements Operation<Tuple<Integer, T>, Tuple<Integer, Double>> {

		private E evaluator;

		public EvaluateChromosome(E evaluator) {
			super();
			this.evaluator = evaluator;
		}

		@Override
		public Tuple<Integer, Double> execute(Tuple<Integer, T> input) {
			evaluator.evaluate(input._2);
			return new Tuple<Integer, Double>(input._1, input._2.fitness);
		}

		@SuppressWarnings("unchecked")
		@Override
		public Operation<Tuple<Integer, T>, Tuple<Integer, Double>> duplicate() {
			return new EvaluateChromosome<>((E)evaluator.duplicate());
		}

	}

}
