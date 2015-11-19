package hr.fer.zemris.optim.evol.parallel;

public interface TaskExecutor<T,E> {
	public E executeTask(T task);
}
