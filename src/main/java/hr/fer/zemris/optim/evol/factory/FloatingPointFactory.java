package hr.fer.zemris.optim.evol.factory;

import hr.fer.zemris.optim.evol.chromosome.FloatingPointChromosome;
import hr.fer.zemris.optim.rng.IRNG;
import hr.fer.zemris.optim.rng.RNG;

public class FloatingPointFactory<T extends FloatingPointChromosome> extends ChromosomeFactory<T> {
	
	private double min;
	private double max;
	
	public FloatingPointFactory(int capacity, T prototype, double min, double max) {
		super(capacity, prototype);
		this.max = max;
		this.min = min;
	}
	
	public FloatingPointFactory(T prototype, double min, double max) {
		super(prototype);
		this.min = min;
		this.max = max;
	}

	@Override
	public T generateElement() {
		T result = getElement();
		
		IRNG rand = RNG.getRNG();
		
		for(int i = 0; i < result.data.length; ++i) {
			result.data[i] = rand.nextDouble(min, max);
		}
		
		return result;
	}

}
