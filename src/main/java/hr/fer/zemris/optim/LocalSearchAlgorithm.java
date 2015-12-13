package hr.fer.zemris.optim;

public interface LocalSearchAlgorithm<T> extends IOptAlgorithm<T> {
	public void setStartSolution(T solution);
}
