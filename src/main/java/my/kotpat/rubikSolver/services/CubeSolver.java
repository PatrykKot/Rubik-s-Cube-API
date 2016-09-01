package my.kotpat.rubikSolver.services;

import my.kotpat.rubikSolver.domain.Cube;
import my.kotpat.rubikSolver.domain.SolvingMeta;

/**
 * The class which is implementing {@link CubeSolver} should has ability to
 * solve the scrambled cube.
 * 
 * @author Patryk Kotlarz
 */
public interface CubeSolver {
	/**
	 * This method should return a correct scramble to solve the cube.
	 * Additionally could include info about solving (like solving time).
	 * 
	 * @param cube
	 *            cube to solve
	 * @return solving information
	 */
	public SolvingMeta getSolution(Cube cube);
}
