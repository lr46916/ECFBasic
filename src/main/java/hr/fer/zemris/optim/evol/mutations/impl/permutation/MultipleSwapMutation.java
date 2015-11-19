package hr.fer.zemris.optim.evol.mutations.impl.permutation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import hr.fer.zemris.optim.evol.Mutation;
import hr.fer.zemris.optim.evol.chromosome.PermutationChromosome;
import hr.fer.zemris.optim.rng.IRNG;
import hr.fer.zemris.optim.rng.RNG;

public class MultipleSwapMutation<T extends PermutationChromosome> implements
		Mutation<T> {
	private double poss;
	private int k;

	public MultipleSwapMutation(double poss,int k) {
		super();
		this.poss = poss;
		this.k = k;
	}

	@Override
	public void mutate(T target) {
		if (poss != 1) {
			IRNG rand = RNG.getRNG();
			if (poss < rand.nextDouble()) {
				return;
			}
		}
		int len = target.field.length;
		
		Set<Integer> set = new LinkedHashSet<>(k);
		List<Integer> list = new ArrayList<>(k);
		
		IRNG rand = RNG.getRNG();
		for (int i = 0; i < k; i++){
		int num = rand.nextInt(len);
			while(set.contains(num)){
				num = rand.nextInt(len);
			}
			set.add(num);
			list.add(target.field[num]);
		}
		Collections.shuffle(list);
		
		Iterator<Integer> it = list.iterator();
		for(int ind : set){
			target.field[ind] = it.next();
		}		

		
	}
	
}
