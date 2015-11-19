package hr.fer.zemris.optim.algorithms.evol.ga.generation;

import hr.fer.zemris.optim.algorithms.evol.ga.GeneticAlgorithm;
import hr.fer.zemris.optim.evol.Chromosome;
import hr.fer.zemris.optim.evol.Evaluator;
import hr.fer.zemris.optim.evol.Mutation;
import hr.fer.zemris.optim.evol.PopulationGenerator;
import hr.fer.zemris.optim.evol.Selection;
import hr.fer.zemris.optim.evol.crossovers.Crossover;

public abstract class GenerationGAAbs<T extends Chromosome> extends GeneticAlgorithm<T> {

	protected Selection maleSelection;
	protected Selection femaleSelection;

	public GenerationGAAbs(int sizeOfPop, PopulationGenerator<T> pg,
			Crossover<T> crossover, Mutation<T> mutation,
			Evaluator<T> evaluator, int iterations, Selection maleSelection,
			Selection femaleSelection) {
		super(sizeOfPop, pg, crossover, mutation, evaluator, iterations);
		this.maleSelection = maleSelection;
		this.femaleSelection = femaleSelection;
	}


}
