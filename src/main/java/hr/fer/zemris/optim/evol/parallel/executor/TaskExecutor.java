package hr.fer.zemris.optim.evol.parallel.executor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import hr.fer.zemris.optim.evol.parallel.Operation;
import hr.fer.zemris.optim.evol.parallel.impl.OperationWorker;

public class TaskExecutor {

	private static TaskExecutor instance;

	private AtomicInteger idGen;
	private Map<Integer, WorkerDataContainer<?>> taskQueues; // swap
																// with
																// array?

	private TaskExecutor() {
		// TODO add configuration file to load these values
		idGen = new AtomicInteger(0);
		taskQueues = new HashMap<>();
	}

	/**
	 * Poison pill has to be provided because java does NOT allow null to be
	 * inserted in ArrayBlockingQueue which is used as concrete Queue
	 * implementation in workers. Poison pill is literally any instance of given
	 * class T that will not be inserted in the Queue before termination time.
	 * If you do so, you will cause an Invalid state and unexpected behaviour or
	 * deadlock.
	 * 
	 * @param operation
	 * @param workers
	 * @param poisonPill
	 * @return
	 */
	public <T, E> QueueData<T, E> createTaskQueue(Operation<T, E> operation, int workers, T poisonPill) {
		int id = idGen.getAndIncrement();

		Queue<T> inputs = new LinkedBlockingQueue<>();
		Queue<E> outputs = new LinkedBlockingQueue<>();

		List<OperationWorker<T, ?>> workerList = new ArrayList<>(workers);

		for (int i = 0; i < workers; i++) {
			workerList.add(new OperationWorker<T, E>(operation.duplicate(), inputs, outputs, poisonPill));
		}

		taskQueues.put(id, new WorkerDataContainer<T>(inputs, poisonPill, workerList));
		return new QueueData<>(id, inputs, outputs);
	}

	public <T, E> QueueData<T, E> createTaskQueue(Operation<T, E> operation, int workers) {
		return createTaskQueue(operation, workers, null);
	}

	public void freeTaskQueue(int id) {
		taskQueues.get(id).killWorkers();

		taskQueues.remove(id);
	}

	private class WorkerDataContainer<T> {

		private Queue<T> inputs;
		private T poisonPill;
		private int workers;
		private ExecutorService execServ;

		public WorkerDataContainer(Queue<T> inputs, T poisonPill, List<OperationWorker<T, ?>> workers) {
			super();
			this.inputs = inputs;
			this.poisonPill = poisonPill;
			execServ = Executors.newFixedThreadPool(workers.size());
			this.workers = workers.size();
			for (OperationWorker<T, ?> worker : workers) {
				execServ.submit(worker);
			}
		}
		
		public ExecutorService getExecutorService() {
			return execServ;
		}

		public void killWorkers() {
			for (int i = 0; i < workers; i++) {
				try {
					inputs.add(poisonPill);
				} catch (Exception e) {
					e.printStackTrace();
					System.err.println("Invalid input given to worker input queue!");
					killAllAndTerminate();
					System.exit(-1);
				}
			}
			execServ.shutdown();
			try {
				System.err.println(execServ.awaitTermination(4, TimeUnit.SECONDS));
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
	
	private void killAllAndTerminate() {
		
		for(int key : taskQueues.keySet()) {
			WorkerDataContainer<?> wd = taskQueues.get(key);
			wd.killWorkers();
			wd.getExecutorService().shutdown();
			try {
				wd.getExecutorService().awaitTermination(4, TimeUnit.SECONDS);
			} catch (InterruptedException e) {
				wd.getExecutorService().shutdownNow();
			}
			if(!wd.execServ.isTerminated()) {
				System.err.println("Failed to stop all tasks in queue... ");
			}
		}
		System.exit(-1);
	}

	public static TaskExecutor getInstance() {
		if (instance == null) {
			instance = new TaskExecutor();
		}
		return instance;
	}

}
