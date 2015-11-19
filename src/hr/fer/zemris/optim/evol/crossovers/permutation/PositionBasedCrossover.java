package hr.fer.zemris.optim.evol.crossovers.permutation;

import java.lang.reflect.Array;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Random;
import java.util.Set;

import hr.fer.zemris.optim.evol.FieldChromosome;
import hr.fer.zemris.optim.evol.PopulationGenerator;
import hr.fer.zemris.optim.evol.crossovers.Crossover;
import hr.fer.zemris.optim.rng.IRNG;
import hr.fer.zemris.optim.rng.RNG;

public class PositionBasedCrossover<T extends FieldChromosome> implements
		Crossover<T> {

	@Override
	public T[] doCrossover(T parentOne, T parentTwo) {
		IRNG rand = RNG.getRNG();
		int len = parentOne.field.length;
		int numOfPicks = rand.nextInt((len / 2) - 1) + 1;

		Set<Integer> indexes = new LinkedHashSet<>(numOfPicks, 1);

		while (indexes.size() < numOfPicks) {
			indexes.add(rand.nextInt(len));
		}

		if (rand.nextBoolean()) {
			T tmp = parentOne;
			parentOne = parentTwo;
			parentTwo = tmp;
		}

		@SuppressWarnings("unchecked")
		T child = (T) parentOne.newLikeThis();

		Set<Integer> elem = new HashSet<>(numOfPicks, 1);

		for (int x : indexes) {
			child.field[x] = parentOne.field[x];
			elem.add(parentOne.field[x]);
		}

		int j = 0;
		for (int i = 0; i < len; i++) {
			if (indexes.contains(i)) {
				continue;
			}
			while (elem.contains(parentTwo.field[j])) {
				j++;
			}
			child.field[i] = parentTwo.field[j++];
		}
		
		@SuppressWarnings("unchecked")
		T[] res = (T[]) Array.newInstance(child.getClass(), 1);
		res[0] = child;
//		for (int z = 0; z < res.length; z++) {
//			Set<Integer> bla = new HashSet<>();
//			for (int k = 0; k < 256; k++) {
//				bla.add(res[z].field[k]);
//			}
//			if (bla.size() != 256) {
//				System.err.println("ERROR IN POS BASED");
//				System.out.println(indexes);
//				System.out.println(Arrays.toString(parentOne.field));
//				System.out.println(Arrays.toString(parentTwo.field));
//				System.out.println(Arrays.toString(res[z].field));
//				System.out.println(bla.size());
//				System.exit(-1);
//			}
//		}
		return res;
	}

}
