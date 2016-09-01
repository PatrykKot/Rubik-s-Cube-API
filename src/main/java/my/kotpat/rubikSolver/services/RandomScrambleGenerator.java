package my.kotpat.rubikSolver.services;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import my.kotpat.rubikSolver.domain.Move;
import my.kotpat.rubikSolver.domain.Scramble;
import my.kotpat.rubikSolver.exceptions.UnsupportedMoveException;

/**
 * The class represents random scramble generator. Has a list of allowed moves (
 * {@link RandomScrambleGenerator#allowedMoves}) and generates random scrambles
 * using {@link Random} class.
 * 
 * @author Patryk Kotlarz
 *
 */
public class RandomScrambleGenerator implements ScrambleGenerator {

	/**
	 * The list of allowed moves used in generating random scramble.
	 */
	private List<Move> allowedMoves = new LinkedList<Move>();

	/**
	 * Helpful {@link Random} object.
	 */
	private Random random;

	/**
	 * The constructor which allocates the memory for {@link Random} object and
	 * sets the {@link RandomScrambleGenerator#allowedMoves}.
	 * 
	 * @param allowedMoves
	 *            list of allowed moves
	 */
	public RandomScrambleGenerator(List<Move> allowedMoves) {
		random = new Random();
		this.allowedMoves = allowedMoves;
	}

	/**
	 * The constructor which allocates the memory for {@link Random} object and
	 * generates the {@link RandomScrambleGenerator#allowedMoves} by checking
	 * all the possibilities which are not throwing the
	 * {@link UnsupportedMoveException} by {@link Move} objects.
	 * 
	 * @see RandomScrambleGenerator#updateAllowedMoves()
	 */
	public RandomScrambleGenerator() {
		random = new Random();
		updateAllowedMoves();
	}

	/**
	 * This method clears the {@link RandomScrambleGenerator#allowedMoves} and
	 * tries to generate all the possible {@link Move} objects (which are not
	 * throwing {@link UnsupportedMoveException}). The method uses {@link Set}
	 * to filter equivalent {@link Move} objects.
	 * 
	 * @see Move#equals(Object)
	 * @see Move#hashCode()
	 */
	private void updateAllowedMoves() {
		allowedMoves.clear();

		List<String> letters = new LinkedList<String>(
				Arrays.asList("AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpRrSsTtUuWwYyZzXxQq".split("")));
		List<String> digits = new LinkedList<String>(Arrays.asList("0123456789".split("")));
		digits.add("");
		List<String> signs = new LinkedList<String>(Arrays.asList("'".split("")));
		signs.add("");

		Set<Move> temporarySet = new HashSet<Move>();

		for (String letter : letters) {
			for (String digit : digits) {
				for (String sign : signs) {
					try {
						Move move = new Move(letter + digit + sign);
						temporarySet.add(move);
					} catch (UnsupportedMoveException e) {
					}
				}
			}
		}

		allowedMoves.addAll(temporarySet);
	}

	/**
	 * Returns random item from {@link RandomScrambleGenerator#allowedMoves}
	 * list.
	 * 
	 * @return random random move
	 */
	public Move getRandomMove() {
		int randomNumber = random.nextInt(allowedMoves.size());
		return new Move(allowedMoves.get(randomNumber));
	}

	/**
	 * Returns random scramble with specific length.
	 * 
	 * @param length
	 *            scramble length
	 */
	public Scramble getRandomScramble(int length) {
		Scramble scramble = new Scramble();

		while (scramble.size() != length) {
			int currentSize = scramble.size();
			for (int i = 0; i < length - currentSize; i++) {
				scramble.add(getRandomMove());
			}
			scramble.simplify();
		}
		return scramble;
	}

}
