package hr.fer.zemris.optim.evol;

import java.lang.reflect.Array;

import hr.fer.zemris.optim.Pool;

public interface Factory<T> extends Pool<T>, PopulationGenerator<T> {
	public T generateElement();

	@Override
	default T[] generatePopulation(int sizeOfPop) {
		T firstElement = generateElement();
		
		@SuppressWarnings("unchecked")
		T[] result = (T[]) Array.newInstance(firstElement.getClass(), sizeOfPop);

		result[0] = firstElement;
		
		for (int i = 1; i < sizeOfPop; ++i) {
			result[i] = generateElement();
		}

		return result;
	}
}
