package my.kotpat.rubikSolver.test;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import my.kotpat.rubikSolver.domain.Scramble;
import my.kotpat.rubikSolver.services.RandomScrambleGenerator;
import my.kotpat.rubikSolver.services.ScrambleGenerator;

@RunWith(BlockJUnit4ClassRunner.class)
public class ScrambleGeneratorClassTest {
	@Test
	public void generatingRandomScrambleTest() {
		ScrambleGenerator generator = new RandomScrambleGenerator();
		Scramble scramble = null;
		for (int i = 0; i < 1000; i++) {
			scramble = generator.getRandomScramble(20);
			Assert.assertTrue("Incorrect random scramble length", scramble.size() == 20);
		}

	}
}
