package hr.fer.zemris.optim.evol;

public abstract class MultiObjectiveChromosome extends Chromosome {
	public double[] fitness;
	
	protected MultiObjectiveChromosome(int size){
		fitness = new double[size];
	}
	
}
