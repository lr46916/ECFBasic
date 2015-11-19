package hr.fer.zemris.optim.evol;

public abstract class Chromosome implements Comparable<Chromosome>{
	public double fitness;
	public abstract Chromosome clone();
	public abstract Chromosome newLikeThis();
}
