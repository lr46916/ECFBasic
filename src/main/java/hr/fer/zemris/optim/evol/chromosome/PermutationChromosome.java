package hr.fer.zemris.optim.evol.chromosome;

import hr.fer.zemris.optim.evol.Chromosome;

public abstract class PermutationChromosome extends Chromosome {
	
	public int[] field;
	
	public PermutationChromosome(int size){
		field = new int[size];
	}
	
	public PermutationChromosome(int[] field){
		this.field = field;
	}
	
}

