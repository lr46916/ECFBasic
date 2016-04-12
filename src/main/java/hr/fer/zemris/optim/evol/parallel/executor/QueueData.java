package hr.fer.zemris.optim.evol.parallel.executor;

import java.util.Queue;

public class QueueData<T, E> {
	private int queueId;
	private Queue<T> inputQueue;
	private Queue<E> resultQueue;

	public QueueData(int queueId, Queue<T> inputQueue, Queue<E> resultQueue) {
		super();
		this.queueId = queueId;
		this.inputQueue = inputQueue;
		this.resultQueue = resultQueue;
	}

	public int getQueueId() {
		return queueId;
	}

	public Queue<T> getInputQueue() {
		return inputQueue;
	}

	public Queue<E> getResultQueue() {
		return resultQueue;
	}

}
