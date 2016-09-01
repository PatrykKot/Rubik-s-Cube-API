package my.kotpat.rubikSolver.test;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import my.kotpat.rubikSolver.domain.Scramble;

@RunWith(BlockJUnit4ClassRunner.class)
public class ScrambleClassTest {
	@Test
	public void creatingScrambleTest() {
		Scramble scramble = new Scramble(" F22RkuUzD2d22d");
		Assert.assertTrue("Incorrect scramble length", scramble.size() == 4);
	}

	@Test
	public void simplyfyingTest() {
		Scramble scramble = new Scramble("F'FFU2UFUU'U2R'R'R2RFR'RD'D2D2'k");
		scramble.simplify();
		Assert.assertTrue("Simplyfying doesn't work correctly", scramble.size() == 7);
	}
}
