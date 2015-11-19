package hr.fer.zemris.optim.pool;

import java.util.LinkedList;
import java.util.Queue;

import hr.fer.zemris.optim.Pool;

public class PoolImpl<T> implements Pool<T> {

	private ElementAllocator<T> elemAllo;
	private Queue<T> stack;

	public PoolImpl(ElementAllocator<T> elemAllo) {
		super();
		this.elemAllo = elemAllo;
		this.stack = new LinkedList<>();
	}

	@Override
	public void free(T element) {
		stack.add(element);
	}

	@Override
	public T getElement() {
		if (!stack.isEmpty()) {
			return stack.poll();
		}
		return elemAllo.getElement();
	}

}
