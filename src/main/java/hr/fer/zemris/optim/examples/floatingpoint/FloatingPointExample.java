package hr.fer.zemris.optim.examples.floatingpoint;

import hr.fer.zemris.optim.algorithms.evol.ga.GeneticAlgorithm;
import hr.fer.zemris.optim.algorithms.evol.ga.tournament.EliminationGA;
import hr.fer.zemris.optim.evol.Crossover;
import hr.fer.zemris.optim.evol.Evaluator;
import hr.fer.zemris.optim.evol.Mutation;
import hr.fer.zemris.optim.evol.chromosome.FloatingPointChromosome;
import hr.fer.zemris.optim.evol.crossovers.impl.floatingpoint.FPSimpleCrossover;
import hr.fer.zemris.optim.evol.mutations.impl.folatingpoint.FPGaussianMutation;
import hr.fer.zemris.optim.evol.populationgenerator.PopulationGenerator;
import hr.fer.zemris.optim.evol.populationgenerator.impl.FloatingPointChromosomePG;
import hr.fer.zemris.optim.evol.selection.impl.KTournamentSelection;
import hr.fer.zemris.optim.evol.selection.impl.SelectionTournament;

public class FloatingPointExample {

	
	/**
	 * Example of finding a minimum of a function f(x1,...,x5) = x1^2 + x2^2 + ... + x5^2 
	 * Of course minimum value is 0.
	 * using genetic alghorithm
	 */
	public static void main(String[] args) {

		PopulationGenerator<FloatingPointChromosome> pg = new FloatingPointChromosomePG(5);

		SelectionTournament selection = new KTournamentSelection(3);

		Crossover<FloatingPointChromosome> crossover = new FPSimpleCrossover();

		Mutation<FloatingPointChromosome> mutation = new FPGaussianMutation(0, 1, 0.02);

		Evaluator<FloatingPointChromosome> evaluator = (chromosome) -> {
			chromosome.fitness = 0;
			for(int i = 0; i < chromosome.data.length; i++) {
				chromosome.fitness -= chromosome.data[i] * chromosome.data[i];
			}
		};

		GeneticAlgorithm<FloatingPointChromosome> ga = new EliminationGA<>(50, pg, crossover, mutation, evaluator,
				10000, selection);
		
		FloatingPointChromosome res = ga.run();
		
		System.out.println("Result: " + res);
		
	}

}
