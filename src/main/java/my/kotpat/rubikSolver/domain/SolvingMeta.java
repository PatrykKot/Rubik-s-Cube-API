package my.kotpat.rubikSolver.domain;

/**
 * Represents meta data about cube solving. Includes solving time in ms, finish
 * {@link Scramble}, number of analyzed scrambles and analyzed moves.
 * 
 * @author Patryk Kotlarz
 */
public class SolvingMeta {
	public double timeMs;
	public Scramble solvingScramble;
	public long analyzedScrambles;
	public long analyzedMoves;

	public SolvingMeta() {
	}

	public double getTimeMs() {
		return timeMs;
	}

	public void setTimeMs(double timeMs) {
		this.timeMs = timeMs;
	}

	public Scramble getSolvingScramble() {
		return solvingScramble;
	}

	public void setSolvingScramble(Scramble solvingScramble) {
		this.solvingScramble = solvingScramble;
	}

	public long getAnalyzedScrambles() {
		return analyzedScrambles;
	}

	public void setAnalyzedScrambles(long analyzedScrambles) {
		this.analyzedScrambles = analyzedScrambles;
	}

	public long getAnalyzedMoves() {
		return analyzedMoves;
	}

	public void setAnalyzedMoves(long analyzedMoves) {
		this.analyzedMoves = analyzedMoves;
	}

}