package hr.fer.zemris.optim.evol;

public interface Evaluator<T> {
	/**
	 * Evaluates given target.
	 * @param target Target to evalaute
	 */
	public void evaluate(T target);
	/**
	 * Evaluates an one or more targets.
	 * @param targets Targets to evaluate
	 */
	public default void evaluate(T[] targets) {
		for(int i = 0; i < targets.length; ++i) {
			this.evaluate(targets[i]);
		}
	}
}
