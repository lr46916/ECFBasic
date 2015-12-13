package hr.fer.zemris.optim.evol;

import hr.fer.zemris.optim.IOptAlgorithm;

public interface PopulationAlgorithm<T> extends IOptAlgorithm<T> {
	public T[] runAndReturnFinishingPopulation();
}
