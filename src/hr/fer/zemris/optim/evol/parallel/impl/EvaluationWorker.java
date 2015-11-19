package hr.fer.zemris.optim.evol.parallel.impl;

import java.util.Queue;

import hr.fer.zemris.optim.evol.Chromosome;
import hr.fer.zemris.optim.evol.Evaluator;
import hr.fer.zemris.optim.evol.parallel.Worker;

public class EvaluationWorker<T extends Chromosome>
		extends Worker<T, T> {
	
	private Evaluator<T> evaluator;
	
	public EvaluationWorker(Queue<T> taskQueue, Queue<T> resultQueue,
			Evaluator<T> evaluator) {
		super(taskQueue, resultQueue);
		this.evaluator = evaluator;
	}

	@Override
	public void run() {
		while(true){
			T chromosome;
			while((chromosome = this.taskQueue.poll()) == null);
			
			evaluator.evaluate(chromosome);
			this.resultQueue.add(chromosome);
		}
	}

}
