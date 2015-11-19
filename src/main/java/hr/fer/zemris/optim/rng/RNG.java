package hr.fer.zemris.optim.rng;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class RNG {
	private static IRNGProvider rngProvider;
	static {
		// Stvorite primjerak razreda Properties;
		// Nad Classloaderom razreda RNG tražite InputStream prema resursu
		// rng-config.properties
		// recite stvorenom objektu razreda Properties da se učita podatcima iz
		// tog streama.
		// Dohvatite ime razreda pridruženo ključu "rng-provider"; zatražite
		// Classloader razreda
		// RNG da učita razred takvog imena i nad dobivenim razredom pozovite
		// metodu newInstance()
		// kako biste dobili jedan primjerak tog razreda; castajte ga u
		// IRNGProvider i zapamtite.
		Properties prop = new Properties();
		InputStream stream = RNG.class.getClassLoader().getResourceAsStream(
				"rng-config.properties");
		try {
			prop.load(stream);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String name = prop.getProperty("rng-provider");
		try {
			rngProvider = (IRNGProvider) RNG.class.getClassLoader()
					.loadClass(name).newInstance();
		} catch (InstantiationException | IllegalAccessException
				| ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static IRNG getRNG() {
		return rngProvider.getRNG();
	}
}
