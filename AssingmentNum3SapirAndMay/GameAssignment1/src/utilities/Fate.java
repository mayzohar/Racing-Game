package utilities;

import java.util.Random;

/**
 * The Fate class provides methods to generate random outcomes for a system's malfunctions or failures.
 * @author   Sapir Ovadya, id: 318258274
 * 			May Zohar, id : 315199810
 * @version 1.0
 */
public class Fate {

	private static Random rand = new Random();

	public static boolean breakDown() {
		//return rand.nextBoolean();
		return rand.nextInt(10) > 8;
	}

	public static boolean generateFixable() {
		return rand.nextInt(10) > 1;
	}

	public static Mishap generateMishap() {
		return new Mishap(generateFixable(), generateTurns(), generateReduction());
	}

	private static float generateReduction() {
		return rand.nextFloat();
	}

	private static int generateTurns() {
		return rand.nextInt(5) + 1;
	}

	public static void setSeed(int seed) {
		rand.setSeed(seed);
	}

}
