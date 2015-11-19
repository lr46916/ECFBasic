package hr.fer.zemris.optim;

import hr.fer.zemris.optim.evol.IOptAlgorithm;

public interface LocalSearchAlgorithm<T> extends IOptAlgorithm<T> {
	public void setStartSolution(T solution);
}
