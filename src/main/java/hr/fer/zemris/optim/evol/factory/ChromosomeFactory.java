package hr.fer.zemris.optim.evol.factory;

import java.util.LinkedList;
import java.util.Queue;

import hr.fer.zemris.optim.evol.Chromosome;
import hr.fer.zemris.optim.evol.Factory;

public abstract class ChromosomeFactory<T extends Chromosome> implements Factory<T> {

	private static int DEFALUT_SIZE = 1024;

	private int maxSize;
	protected T prototype;
	protected Queue<T> freed;

	public ChromosomeFactory(int capacity, T prototype) {
		this.maxSize = capacity;
		freed = new LinkedList<>();
		this.prototype = prototype;
	}
	
	public ChromosomeFactory(T prototype) {
		this(DEFALUT_SIZE, prototype);
	}

	@Override
	public void free(T element) {
		if (freed.size() < maxSize)
			freed.add(element);
	}

	@SuppressWarnings("unchecked")
	@Override
	public T getElement() {
		if(freed.isEmpty()) {
			return (T) prototype.newLikeThis();
		} else {
			return freed.poll();
		}
	}
}
