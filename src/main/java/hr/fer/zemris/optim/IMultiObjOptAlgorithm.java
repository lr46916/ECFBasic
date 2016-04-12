package hr.fer.zemris.optim;

import java.util.List;

public interface IMultiObjOptAlgorithm<T> {
	public List<T> run();
	public List<T> runAndReturnFinishingPopualtion();
}
