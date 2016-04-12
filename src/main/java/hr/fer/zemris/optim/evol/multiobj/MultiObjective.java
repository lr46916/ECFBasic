package hr.fer.zemris.optim.evol.multiobj;

public interface MultiObjective {
	public int componentsCount();
	public double getComponent(int index);
}
