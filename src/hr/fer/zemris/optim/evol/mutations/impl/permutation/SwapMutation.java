package hr.fer.zemris.optim.evol.mutations.impl.permutation;

import hr.fer.zemris.optim.evol.Mutation;
import hr.fer.zemris.optim.evol.chromosome.FieldChromosome;
import hr.fer.zemris.optim.rng.IRNG;
import hr.fer.zemris.optim.rng.RNG;

public class SwapMutation<T extends FieldChromosome> implements Mutation<T> {

	private double poss;

	public SwapMutation(double poss) {
		super();
		this.poss = poss;
	}

	@Override
	public void mutate(T target) {
		IRNG rand = RNG.getRNG();
		if (poss != 1)
			if (poss < rand.nextDouble()) {
				return;
			}
		int len = target.field.length;

		int index1 = rand.nextInt(len);
		int index2 = rand.nextInt(len);

		if (index1 == index2) {
			index2 = (index2 + 1) % len;
		}

		int tmp = target.field[index1];
		target.field[index1] = target.field[index2];
		target.field[index2] = tmp;
	}

}
