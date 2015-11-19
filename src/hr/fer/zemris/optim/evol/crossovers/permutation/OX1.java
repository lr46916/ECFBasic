package hr.fer.zemris.optim.evol.crossovers.permutation;

import java.lang.reflect.Array;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import hr.fer.zemris.optim.algorithms.evol.ga.Crossover;
import hr.fer.zemris.optim.evol.FieldChromosome;
import hr.fer.zemris.optim.evol.PopulationGenerator;
import hr.fer.zemris.optim.rng.IRNG;
import hr.fer.zemris.optim.rng.RNG;

public class OX1<T extends FieldChromosome> implements Crossover<T> {

	@Override
	public T[] doCrossover(T parentOne, T parentTwo) {
		IRNG rand = RNG.getRNG();
		int len = parentOne.field.length;

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

		@SuppressWarnings("unchecked")
		T[] res = (T[]) Array.newInstance(parentOne.getClass(), 2);

		res[0] = (T) parentOne.newLikeThis();

		res[1] = (T) parentOne.newLikeThis();

		Set<Integer> set1 = new HashSet<>(index2 - index1, 1);
		Set<Integer> set2 = new HashSet<>(index2 - index1, 1);

		for (int i = index1; i < index2; i++) {
			res[0].field[i] = parentOne.field[i];
			set1.add(parentOne.field[i]);
			res[1].field[i] = parentTwo.field[i];
			set2.add(parentTwo.field[i]);
		}
		int total = len;
		int j1 = index2;
		int j2 = index2;
		int i = 0;
		while (total > i) {
			int current = (index2 + i) % len;
			if (!set1.contains(parentTwo.field[current])) {
				res[0].field[j1++] = parentTwo.field[current];
				if (j1 >= len)
					j1 %= len;
			}
			if (!set2.contains(parentOne.field[current])) {
				res[1].field[j2++] = parentOne.field[current];
				if (j2 >= len)
					j2 %= len;
			}
			i++;
		}

//		for (int z = 0; z < res.length; z++) {
//			Set<Integer> bla = new HashSet<>();
//			for (int k = 0; k < 256; k++) {
//				bla.add(res[z].field[k]);
//			}
//			if (bla.size() != 256) {
//				System.err.println("ERROR IN MUTATION");
//				System.exit(-1);
//			}
//		}
		return res;
	}

}
