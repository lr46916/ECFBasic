package hr.fer.zemris.optim.evol.algorithms.artifImmune.hypermutation;

import java.util.Set;
import java.util.TreeSet;

import hr.fer.zemris.optim.evol.algorithms.artifImmune.HyperMutation;
import hr.fer.zemris.optim.evol.chromosome.FloatingPointChromosome;
import hr.fer.zemris.optim.rng.IRNG;
import hr.fer.zemris.optim.rng.RNG;

public class FloatingPointHypermutation implements HyperMutation<FloatingPointChromosome> {

	private double t;
	private double mean;
	private double std;
	private double ro;

	public FloatingPointHypermutation(double ro, double mean, double std) {
		this.mean = mean;
		this.std = std;
		this.ro = ro;
	}

	@Override
	public void mutate(FloatingPointChromosome[] targets) {
		t = -(targets.length - 1) / (Math.log(ro));
		for (int i = 0; i < targets.length; ++i) {
			mutateSingle(targets[i], i);
		}
	}

	private void mutateSingle(FloatingPointChromosome target, int rank) {
		int numberOfMutations = (int) (1 + target.data.length * -Math.expm1(-(rank / t))) + 1;

		int len = target.data.length;

		Set<Integer> set = new TreeSet<>();

		IRNG rand = RNG.getRNG();
		/**
		 * We expect: numberOfMutations << len. With that this loop should work
		 * "fast".
		 */
		for (int i = 0; i < numberOfMutations; i++) {
			int num = rand.nextInt(len);
			while (!set.add(num)) {
				num = rand.nextInt(len);
			}
		}

		for (int ind : set) {
			target.data[ind] += rand.nextGaussian() * std + mean;
		}
	}
}
