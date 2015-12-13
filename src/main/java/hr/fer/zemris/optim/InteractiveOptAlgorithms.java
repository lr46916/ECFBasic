package hr.fer.zemris.optim;

public interface InteractiveOptAlgorithms<T> extends IOptAlgorithm<T> {
	public void offer(T solution);
	public T getCurrentBest();
	public T[] getNumberOfBest(int k);
}
