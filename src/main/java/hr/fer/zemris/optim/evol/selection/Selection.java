package hr.fer.zemris.optim.evol.selection;

import hr.fer.zemris.optim.evol.Chromosome;

public interface Selection {
	public int doSelection(Chromosome[] population);
}
