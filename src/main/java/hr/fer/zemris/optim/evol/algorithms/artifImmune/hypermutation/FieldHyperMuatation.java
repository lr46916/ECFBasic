package hr.fer.zemris.optim.evol.algorithms.artifImmune.hypermutation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import hr.fer.zemris.optim.evol.algorithms.artifImmune.HyperMutation;
import hr.fer.zemris.optim.evol.chromosome.PermutationChromosome;
import hr.fer.zemris.optim.rng.IRNG;
import hr.fer.zemris.optim.rng.RNG;

public class FieldHyperMuatation<T extends PermutationChromosome> implements HyperMutation<T> {

	private double t;

	public FieldHyperMuatation(int n, double ro) {
		super();
		t = -(n - 1) / (Math.log(ro));
	}

	private void mutateSingle(T target, int rank) {
		int numberOfMutations = (int) (1 + target.field.length * (-Math.expm1(-(rank / t)))) + 1;

		int len = target.field.length;

		Set<Integer> set = new LinkedHashSet<>();
		List<Integer> list = new ArrayList<>(numberOfMutations);

		IRNG rand = RNG.getRNG();
		for (int i = 0; i < numberOfMutations; i++) {
			int num = rand.nextInt(len);
			while (set.contains(num)) {
				num = rand.nextInt(len);
			}
			set.add(num);
			list.add(target.field[num]);
		}
		Collections.shuffle(list);

		Iterator<Integer> it = list.iterator();
		for (int ind : set) {
			target.field[ind] = it.next();
		}
	}

	@Override
	public void mutate(T[] targets) {
		for (int i = 0; i < targets.length; ++i) {
			mutateSingle(targets[i], i);
		}
	}

}
