package hr.fer.zemris.optim.evol.chromosome;

import hr.fer.zemris.optim.evol.Chromosome;

public abstract class FieldChromosome extends Chromosome {
	
	public int[] field;
	
	public FieldChromosome(int size){
		field = new int[size];
	}
	
	public FieldChromosome(int[] field){
		this.field = field;
	}
	
}

