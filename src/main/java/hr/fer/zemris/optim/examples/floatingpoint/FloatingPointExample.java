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

	public static void main(String[] args) {

		PopulationGenerator<FloatingPointChromosome> pg = new FloatingPointChromosomePG(2);

		SelectionTournament selection = new KTournamentSelection(3);

		Crossover<FloatingPointChromosome> crossover = new FPSimpleCrossover();

		Mutation<FloatingPointChromosome> mutation = new FPGaussianMutation(0, 1);

		Evaluator<FloatingPointChromosome> evaluator = (chromosome) -> {
			chromosome.fitness = chromosome.data[0] * chromosome.data[0] + chromosome.data[1] * chromosome.data[1];
			chromosome.fitness = -chromosome.fitness;
		};

		GeneticAlgorithm<FloatingPointChromosome> ga = new EliminationGA<>(50, pg, crossover, mutation, evaluator,
				1000000, selection);
		
		System.out.println(ga.run());

	}

}
