package my.kotpat.rubikSolver.services;

import my.kotpat.rubikSolver.domain.Cube;
import my.kotpat.rubikSolver.domain.Move;
import my.kotpat.rubikSolver.domain.Scramble;
import my.kotpat.rubikSolver.domain.SolvingMeta;

/**
 * Implementation of {@link CubeSolver}. It is a simplest, the slowest and the
 * least effective solver because it finds a solution by generating random
 * scrambles with specified length until the cube will not be solved. It works
 * not bad for 2x2x2 cube (sometimes it finds solution after 3 seconds,
 * sometimes after 2-5 minutes) but it is useless for 3x3x3 cube (more than 43
 * trillion combinations).
 * 
 * @author Patryk Kotlarz
 *
 */
public class RandomCubeSolver implements CubeSolver {

	/**
	 * Maximum scramble length (default 100)
	 */
	int maxScrambleLength = 100;

	public RandomCubeSolver() {
	}

	/**
	 * Constructor with {@link RandomCubeSolver#maxScrambleLength} setter.
	 * 
	 * @param maxScrambleLength
	 *            maximum scramble length
	 */
	public RandomCubeSolver(int maxScrambleLength) {
		this.maxScrambleLength = maxScrambleLength;
	}

	public SolvingMeta getSolution(Cube cube) {
		ScrambleGenerator generator = new RandomScrambleGenerator();
		Scramble randomScramble = null;
		boolean solved = false;
		int movesInScramble = 0;
		long analyzedMoves = 0;
		long analyzedScrambles = 0;

		long startingTime = System.nanoTime();

		// Until not solved
		while (!solved) {
			// Generating random scramble
			randomScramble = generator.getRandomScramble(maxScrambleLength);
			movesInScramble = 0;
			analyzedScrambles++;

			// Rotating layers
			for (Move move : randomScramble) {

				// Checking the cube
				if (cube.isSolved()) {
					solved = true;

					// Deleting unnecessary moves if cube is solved
					int toDelete = randomScramble.size() - movesInScramble;
					for (int i = 0; i < toDelete; i++) {
						randomScramble.deleteLast();
					}
					break;
				}

				cube.move(move);
				movesInScramble++;
				analyzedMoves++;
			}

			// Reversing the scramble
			randomScramble.reverse();
			// Reversing cube to the initial state
			cube.scramble(randomScramble);
		}
		long finishingTime = System.nanoTime();
		double timeMs = (double) (finishingTime - startingTime) / 1000000;

		// Reversing the scramble to get the "solution scramble"
		randomScramble.reverse();

		SolvingMeta meta = new SolvingMeta();
		meta.setAnalyzedMoves(analyzedMoves);
		meta.setAnalyzedScrambles(analyzedScrambles);
		meta.setSolvingScramble(randomScramble);
		meta.setTimeMs(timeMs);

		return meta;
	}

	public int getMaxScrambleLength() {
		return maxScrambleLength;
	}

	public void setMaxScrambleLength(int maxScrambleLength) {
		this.maxScrambleLength = maxScrambleLength;
	}
}
