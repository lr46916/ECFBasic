package hr.fer.zemris.optim.algorithms.evol.ga;

import hr.fer.zemris.optim.evol.Chromosome;

import java.lang.reflect.Array;
import java.util.Stack;

public class ChromosomeArrayFactory<T extends Chromosome> implements
		ArrayFactory<T> {

	private int arraySize;
	private Stack<T[]> avaiable;
	private Class<T> prototype;
	
	public ChromosomeArrayFactory(int arraySize, Class<T> prototype) {
		super();
		this.arraySize = arraySize;
		this.avaiable = new Stack<>();
		this.prototype = prototype;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T[] getArray() {
		synchronized (avaiable) {
			if (avaiable.isEmpty()) {
				return 	(T[]) Array.newInstance(prototype, arraySize);
			}
			return avaiable.pop();
		}
	}

	@Override
	public void freeArray(T[] targetArray) {
		synchronized (avaiable) {
			avaiable.push(targetArray);
		}
	}

}
