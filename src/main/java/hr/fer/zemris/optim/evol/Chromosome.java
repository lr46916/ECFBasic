package hr.fer.zemris.optim.evol;

public abstract class Chromosome implements Comparable<Chromosome>{
	/**
	 * Chromosome fitness value.
	 */
	public double fitness;
	public abstract Chromosome clone();
	public abstract Chromosome newLikeThis();
	
	@Override
	public int compareTo(Chromosome c){
		return Double.compare(fitness, c.fitness);
	}
}
