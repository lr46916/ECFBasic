package hr.fer.zemris.optim;

import hr.fer.zemris.optim.evol.IOptAlgorithm;

public interface InteractiveOptAlgorithms<T> extends IOptAlgorithm<T> {
	public void offer(T solution);
	public T getCurrentBest();
}
