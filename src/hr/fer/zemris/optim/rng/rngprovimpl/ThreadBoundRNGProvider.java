package hr.fer.zemris.optim.rng.rngprovimpl;

import hr.fer.zemris.optim.rng.IRNG;
import hr.fer.zemris.optim.rng.IRNGProvider;

public class ThreadBoundRNGProvider implements IRNGProvider {

	@Override
	public IRNG getRNG() {
		IRNGProvider prov = (IRNGProvider) Thread.currentThread();
		return prov.getRNG();
	}

}
