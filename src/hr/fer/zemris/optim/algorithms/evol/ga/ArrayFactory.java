package hr.fer.zemris.optim.algorithms.evol.ga;

public interface ArrayFactory<T> {
	public T[] getArray();
	public void freeArray(T[] targetArray);
}
