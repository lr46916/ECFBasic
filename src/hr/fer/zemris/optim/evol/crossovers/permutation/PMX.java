package hr.fer.zemris.optim.evol.crossovers.permutation;

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import hr.fer.zemris.optim.algorithms.evol.ga.Crossover;
import hr.fer.zemris.optim.evol.FieldChromosome;
import hr.fer.zemris.optim.evol.PopulationGenerator;
import hr.fer.zemris.optim.rng.IRNG;
import hr.fer.zemris.optim.rng.RNG;

public class PMX<T extends FieldChromosome> implements Crossover<T> {

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

		Map<Integer, Integer> map1 = new HashMap<>(index2 - index1, 1);
		Map<Integer, Integer> map2 = new HashMap<>(index2 - index1, 1);

		@SuppressWarnings("unchecked")
		T[] res = (T[]) Array.newInstance(parentOne.getClass(), 2);

		res[0] = (T) parentOne.newLikeThis();
		res[1] = (T) parentOne.newLikeThis();

		for (int i = index1; i < index2; i++) {
			res[0].field[i] = parentTwo.field[i];
			res[1].field[i] = parentOne.field[i];
			map1.put(res[0].field[i], res[1].field[i]);
			map2.put(res[1].field[i], res[0].field[i]);
		}

		int i = index2;
		while (i != index1) {
			if (map1.containsKey(parentOne.field[i])) {
				res[0].field[i] = map1.get(parentOne.field[i]);
				while (map1.containsKey(res[0].field[i])) {
					res[0].field[i] = map1.get(res[0].field[i]);
				}
			} else {
				res[0].field[i] = parentOne.field[i];
			}
			if (map2.containsKey(parentTwo.field[i])) {
				res[1].field[i] = map2.get(parentTwo.field[i]);
				while (map2.containsKey(res[1].field[i])) {
					res[1].field[i] = map2.get(res[1].field[i]);
				}
			} else {
				res[1].field[i] = parentTwo.field[i];
			}
			i = (i + 1) % len;
		}
//		for (int z = 0; z < res.length; z++) {
//			Set<Integer> bla = new HashSet<>();
//			for (int k = 0; k < 256; k++) {
//				bla.add(res[z].field[k]);
//			}
//			if (bla.size() != 256) {
//				System.out.println(index1 + "," + index2);
//				System.err.println("ERROR IN PMX");
//				System.out.println(Arrays.toString(parentOne.field));
//				System.out.println(Arrays.toString(parentTwo.field));
//				System.out.println(Arrays.toString(res[z].field));
//				System.out.println(bla.size());
//				System.out.println("AHA: " + (c1 + c2 + index2 - index1));
//				System.out
//						.println(z + "....." + c1 + "---" + (index2 - index1));
//				System.out.println(map1.keySet());
//				System.out.println(map1.values());
//				System.out.println(bla);
//				System.exit(-1);
//			}
//		}
		return res;
	}

}
