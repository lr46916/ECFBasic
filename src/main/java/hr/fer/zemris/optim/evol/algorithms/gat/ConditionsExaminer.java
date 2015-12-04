package hr.fer.zemris.optim.evol.algorithms.gat;

public interface ConditionsExaminer<T> {
	public boolean treePartCondtitionsMet(T target);
	public boolean over(T target);
	public int compare(T one, T two);
}
