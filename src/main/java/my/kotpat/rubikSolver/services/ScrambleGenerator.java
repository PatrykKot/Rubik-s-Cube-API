package my.kotpat.rubikSolver.services;

import my.kotpat.rubikSolver.domain.Scramble;

/**
 * The class which is implementing {@link ScrambleGenerator} should has ability
 * to generate random (or not) scramble with specific scramble length.
 * 
 * @author Patryk Kotlarz
 *
 */
public interface ScrambleGenerator {
	/**
	 * The method returns a random scramble with specific {@code length}
	 * 
	 * @param length
	 *            scramble length
	 * @return generated scramble
	 */
	public Scramble getRandomScramble(int length);
}
