package hr.fer.zemris.optim.algorithms.evol.artifImmune.hypermutation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import hr.fer.zemris.optim.algorithms.evol.artifImmune.HyperMutation;
import hr.fer.zemris.optim.evol.FieldChromosome;
import hr.fer.zemris.optim.rng.IRNG;
import hr.fer.zemris.optim.rng.RNG;

public class FieldHyperMuatation<T extends FieldChromosome> implements  HyperMutation<T> {

//	private Mutation<FieldChromosome> mutation = new SwapMutation<>(1);
	private double t;

	public FieldHyperMuatation(int n, double ro) {
		super();
		t = -(n - 1) / (Math.log(ro));
	}

	@Override
	public void mutate(FieldChromosome target, int rank) {
		int numberOfMutations = (int) (1 + target.field.length
				* (1 - Math.pow(Math.E, -(rank / t)))) + 1;
//		for (int i = 0; i < numberOfMutations; i++) {
//			mutation.mutate(target);
//		}
		
		int len = target.field.length;
		
		Set<Integer> set = new LinkedHashSet<>(numberOfMutations);
		List<Integer> list = new ArrayList<>(numberOfMutations);
		
		IRNG rand = RNG.getRNG();
		for(int i = 0; i < numberOfMutations; i++){
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
