package hr.fer.zemris.optim.evol.chromosome;

import hr.fer.zemris.optim.evol.Chromosome;

public abstract class MultiObjectiveChromosome extends Chromosome {
	public double[] fitness;
	
	protected MultiObjectiveChromosome(int size){
		fitness = new double[size];
	}
	
}
