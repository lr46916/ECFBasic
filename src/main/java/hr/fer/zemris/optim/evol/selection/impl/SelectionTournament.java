package hr.fer.zemris.optim.evol.selection.impl;

import hr.fer.zemris.optim.evol.Chromosome;
import hr.fer.zemris.optim.evol.selection.Selection;

public interface SelectionTournament extends Selection {
	/**
	 * It is designed to be called after executing
	 * {@link #doSelection(hr.fer.zemris.optim.evol.Chromosome[]) doSelection}
	 * 
	 * @return selection results acquired after running
	 *         {@link #doSelection(hr.fer.zemris.optim.evol.Chromosome[])
	 *         doSelection} method. Results are sorted descending starting with
	 *         the winner of the tournament.
	 */
	public int[] doTournamentSelection(Chromosome[] population);
}
