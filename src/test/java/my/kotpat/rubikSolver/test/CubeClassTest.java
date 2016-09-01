package my.kotpat.rubikSolver.test;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import my.kotpat.rubikSolver.domain.Cube;
import my.kotpat.rubikSolver.domain.Scramble;
import my.kotpat.rubikSolver.domain.SolvingMeta;
import my.kotpat.rubikSolver.exceptions.UnsupportedLayerDimensionException;
import my.kotpat.rubikSolver.services.CubeSolver;
import my.kotpat.rubikSolver.services.RandomCubeSolver;
import my.kotpat.rubikSolver.services.RandomScrambleGenerator;

@RunWith(BlockJUnit4ClassRunner.class)
public class CubeClassTest {
	@Test
	public void inversingScrambleTest() throws UnsupportedLayerDimensionException {
		Cube cube = new Cube(3);
		Scramble scramble = (new RandomScrambleGenerator()).getRandomScramble(10000);
		cube.scramble(scramble);
		Assert.assertFalse("Cube is solved after scrambling", cube.isSolved());

		scramble.reverse();
		cube.scramble(scramble);
		Assert.assertTrue("Cube is not solved after reversing scramble", cube.isSolved());
	}

	@Test
	public void twoByTwoRandomSolvingTest() throws UnsupportedLayerDimensionException {
		Cube cube = new Cube(2);
		Scramble scramble = (new RandomScrambleGenerator()).getRandomScramble(10000);
		cube.scramble(scramble);
		CubeSolver solver = new RandomCubeSolver();
		SolvingMeta solution = solver.getSolution(cube);
		cube.scramble(solution.getSolvingScramble());
	}
}
