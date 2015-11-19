package hr.fer.zemris.optim.evol.mutations.impl.permutation;

import java.util.Arrays;

import hr.fer.zemris.optim.evol.Mutation;
import hr.fer.zemris.optim.evol.chromosome.FieldChromosome;
import hr.fer.zemris.optim.rng.IRNG;
import hr.fer.zemris.optim.rng.RNG;

public class InversionMutation<T extends FieldChromosome> implements
		Mutation<T> {

	private double poss;

	public InversionMutation(double poss) {
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

		if (index2 < index1) {
			int tmp = index1;
			index1 = index2;
			index2 = tmp;
		}

		int size = index2 - index1;
		int[] tmp = new int[size];

		for (int i = 1; i <= size; i++) {
			tmp[i - 1] = target.field[index2 - i];
		}

		for (int i = index1; i < len - size; i++) {
			target.field[i] = target.field[i + size];
		}

		int index = rand.nextInt(len - size);

		for (int i = len - 1; i >= index + size; i--) {
			target.field[i] = target.field[i - size];
		}

		for(int i = 0; i < size; i++){
			target.field[index+i] = tmp[i];
		}

	}

}
