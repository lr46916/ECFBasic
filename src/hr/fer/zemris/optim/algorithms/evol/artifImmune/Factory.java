package hr.fer.zemris.optim.algorithms.evol.artifImmune;

import hr.fer.zemris.optim.evol.PopulationGenerator;

public interface Factory<T> extends PopulationGenerator<T> {
	public T createElement();
}
