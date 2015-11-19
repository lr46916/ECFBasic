package hr.fer.zemris.optim.evol.mutations.impl.permutation;

import hr.fer.zemris.optim.evol.Chromosome;
import hr.fer.zemris.optim.evol.Mutation;
import hr.fer.zemris.optim.rng.RNG;

public class MultipleChoiceMutation<T extends Chromosome> implements Mutation<T> {

	private Mutation<T>[] mutations;
	private double[] poss;

	public MultipleChoiceMutation(Mutation<T>[] mutations, double[] poss) {
		super();
		if (mutations.length != poss.length) {
			throw new IllegalArgumentException(
					"Each mutation needs to have coresponding value of choosing possibility.");
		}
		this.mutations = mutations;
		this.poss = poss;
	}

	@Override
	public void mutate(T target) {
		double randomNum = RNG.getRNG().nextDouble();
		double sum = 0;
		for (int i = 0; i < mutations.length; i++) {
			sum += poss[i];
			if (sum >= randomNum) {
				mutations[i].mutate(target);
				return;
			}
		}
	}

}
