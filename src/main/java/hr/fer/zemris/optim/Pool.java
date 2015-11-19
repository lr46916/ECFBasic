package hr.fer.zemris.optim;

public interface Pool<T> {
	public void free(T element);
	public T getElement();
}
