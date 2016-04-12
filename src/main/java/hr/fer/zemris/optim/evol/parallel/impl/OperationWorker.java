package hr.fer.zemris.optim.evol.parallel.impl;

import java.util.Queue;

import hr.fer.zemris.optim.evol.parallel.Operation;

public class OperationWorker<T, E> implements Runnable {

	private Operation<T, E> operation;
	private Queue<T> inputs;
	private Queue<E> outputs;
	private T poisonPill;

	public OperationWorker(Operation<T, E> operation, Queue<T> inputs, Queue<E> outputs, T poisonPill) {
		super();
		this.operation = operation;
		this.inputs = inputs;
		this.outputs = outputs;
		this.poisonPill = poisonPill;
	}

	@Override
	public void run() {

		while (true) {
			T input;
			while ((input = this.inputs.poll()) == null)
				;
			if (input == poisonPill){
				break;
			}
			this.outputs.add(operation.execute(input));
		}

		System.err.println("Bye");

	}

}
