package hr.fer.zemris.optim.rng.rngprovimpl;

import hr.fer.zemris.optim.rng.IRNG;
import hr.fer.zemris.optim.rng.IRNGProvider;
import hr.fer.zemris.optim.rng.rngimpl.RNGRandomImpl;

public class ThreadLocalRNGProvider implements IRNGProvider {

	private ThreadLocal<IRNG> threadLocal;

	public ThreadLocalRNGProvider() {
		super();
		threadLocal = new ThreadLocal<IRNG>();
	}

	@Override
	public IRNG getRNG() {
		IRNG ret = threadLocal.get();
		if(ret == null){
			ret = new RNGRandomImpl();
		}
		threadLocal.set(ret);
		return ret;
	}

}
