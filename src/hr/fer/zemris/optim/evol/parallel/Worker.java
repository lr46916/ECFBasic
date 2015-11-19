package hr.fer.zemris.optim.evol.parallel;

import java.util.Queue;

public abstract class Worker<T, E> implements Runnable {

	protected Queue<T> taskQueue;
	protected Queue<E> resultQueue;

	protected Worker(Queue<T> taskQueue, Queue<E> resultQueue) {
		super();
		this.taskQueue = taskQueue;
		this.resultQueue = resultQueue;
	}
}
