package hr.fer.zemris.optim.evol.examples.nsga2;

import java.util.Arrays;

import hr.fer.zemris.optim.evol.Chromosome;
import hr.fer.zemris.optim.evol.Crossover;
import hr.fer.zemris.optim.evol.Evaluator;
import hr.fer.zemris.optim.evol.Mutation;
import hr.fer.zemris.optim.evol.PopulationGenerator;
import hr.fer.zemris.optim.evol.multiobj.MultiObjective;
import hr.fer.zemris.optim.evol.multiobj.algorithms.nsga.NSGA2UniquePopulation;
import hr.fer.zemris.optim.rng.IRNG;
import hr.fer.zemris.optim.rng.RNG;

public class SimpleMultiObjExample {

	public static void main(String[] args) {

		double[] botLimits = new double[] { 0.1, 0 };
		double[] upperLimits = new double[] { 0, 5 };

		// double[] botLimits = new double[5];
		// double[] upperLimits = new double[]{5,5,5,5,5};

		ExampleCrossover crossover = new ExampleCrossover();
		ExampleMutation mutation = new ExampleMutation(0.05, upperLimits, botLimits);
		ExampleEvaluator evaluator = new ExampleEvaluator();
		ExamplePopGenerator populationGenerator = new ExamplePopGenerator(botLimits, upperLimits);

		// NSGA2<ExampleFuncChromosome> nsgaAlg = new
		// NSGA2<>(populationGenerator, mutation, crossover, evaluator, 10000,
		// 100, 3);

		NSGA2UniquePopulation<ExampleFuncChromosome> nsgaAlg = new NSGA2UniquePopulation<ExampleFuncChromosome>(
				populationGenerator, mutation, crossover, evaluator, 10000, 100, 3);

		for (ExampleFuncChromosome a : nsgaAlg.run()) {
			System.out.println(a);
		}

	}

	private static class ExampleFuncChromosome extends Chromosome implements MultiObjective {

		public double[] fitness;
		public double[] x;

		public ExampleFuncChromosome(int size) {
			x = new double[size];
			fitness = new double[size];
		}

		@Override
		public String toString() {
			return Arrays.toString(x) + " -> " + Arrays.toString(fitness);
		}

		@Override
		public Chromosome clone() {
			ExampleFuncChromosome ret = (ExampleFuncChromosome) this.newLikeThis();
			for (int i = 0; i < fitness.length; i++) {
				ret.fitness[i] = fitness[i];
			}
			return ret;
		}

		@Override
		public Chromosome newLikeThis() {
			return new ExampleFuncChromosome(x.length);
		}

		@Override
		public int componentsCount() {
			return fitness.length;
		}

		@Override
		public double getComponent(int index) {
			return fitness[index];
		}

		@Override
		public int hashCode() {
			return Arrays.hashCode(x);
		}

		@Override
		public boolean equals(Object o) {
			return o instanceof ExampleFuncChromosome ? Arrays.equals(x, ((ExampleFuncChromosome) o).x) : false;
		}

	}

	private static class ExampleCrossover implements Crossover<ExampleFuncChromosome> {
		@Override
		public ExampleFuncChromosome[] doCrossover(ExampleFuncChromosome parentOne, ExampleFuncChromosome parentTwo) {
			ExampleFuncChromosome child = (ExampleFuncChromosome) parentOne.newLikeThis();
			for (int i = 0; i < child.x.length; i++) {
				child.x[i] = (parentOne.x[i] + parentTwo.x[i]) / 2;
			}
			return new ExampleFuncChromosome[] { child };
		}

	}

	private static class ExampleMutation implements Mutation<ExampleFuncChromosome> {

		private double mutationPoss;
		private double[] upperLimits;
		private double[] botLimits;

		public ExampleMutation(double mutationPoss, double[] upperLimits, double[] botLimits) {
			super();
			this.mutationPoss = mutationPoss;
			this.upperLimits = upperLimits;
			this.botLimits = botLimits;
		}

		@Override
		public void mutate(ExampleFuncChromosome target) {
			IRNG rand = RNG.getRNG();
			for (int i = 0; i < target.x.length; i++) {
				if (rand.nextFloat() <= mutationPoss) {
					target.x[i] += rand.nextGaussian();
					target.x[i] = Double.max(botLimits[i], target.x[i]);
					target.x[i] = Double.min(upperLimits[i], target.x[i]);
				}
			}
		}
	}

	private static class ExampleEvaluator implements Evaluator<ExampleFuncChromosome> {
		@Override
		public void evaluate(ExampleFuncChromosome target) {
			target.fitness[0] = -target.x[0];
			target.fitness[1] = -(target.x[1] + 1) / target.x[0];
			// for(int i = 0; i < target.x.length; i++) {
			// target.fitness[i] = target.x[i] * target.x[i];
			// }
		}
	}

	private static class ExamplePopGenerator implements PopulationGenerator<ExampleFuncChromosome> {

		private double[] botLimits;
		private double[] upperLimits;

		public ExamplePopGenerator(double[] botLimits, double[] upperLimits) {
			super();
			this.botLimits = botLimits;
			this.upperLimits = upperLimits;
		}

		@Override
		public ExampleFuncChromosome[] generatePopulation(int sizeOfPop) {

			ExampleFuncChromosome[] result = new ExampleFuncChromosome[sizeOfPop];

			IRNG rand = RNG.getRNG();

			for (int i = 0; i < sizeOfPop; i++) {
				result[i] = new ExampleFuncChromosome(botLimits.length);
				for (int j = 0; j < result[i].x.length; j++) {
					result[i].x[j] = rand.nextDouble(botLimits[j], upperLimits[j]);
				}
			}

			return result;
		}
	}
}
