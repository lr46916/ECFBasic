package hr.fer.zemris.optim.evol.mutations.impl.permutation;

import hr.fer.zemris.optim.evol.Mutation;
import hr.fer.zemris.optim.evol.chromosome.FieldChromosome;
import hr.fer.zemris.optim.rng.IRNG;
import hr.fer.zemris.optim.rng.RNG;

public class InsertionMutation<T extends FieldChromosome> implements
		Mutation<T> {

	private double poss;

	public InsertionMutation(double poss) {
		super();
		this.poss = poss;
	}

	@Override
	public void mutate(T target) {
		IRNG rand = RNG.getRNG();
		if (poss < rand.nextDouble()) {
			return;
		}
		int len = target.field.length;

		int index1 = rand.nextInt(len);
		int index2 = rand.nextInt(len);

		if (index1 == index2) {
			index2 = (index2 + 1) % len;
		}

		int value = target.field[index1];

		for (int i = index1; i < len-1; i++) {
			target.field[i] = target.field[i + 1];
		}

		for (int i = len - 1; i > index2; i--) {
			target.field[i] = target.field[i - 1];
		}

		target.field[index2] = value;
		
	}


}
