package hr.fer.zemris.optim.evol.algorithms.multiobj.nsga;

public class SharingFunction {
	private double sigma;
	private double alpha;

	public SharingFunction(double sigma, double alpha) {
		super();
		this.sigma = sigma;
		this.alpha = alpha;
	}

	public double calculate(double d) {
		if (d >= sigma)
			return 0;
		return 1 - Math.pow(d / sigma, alpha);
	}

}
