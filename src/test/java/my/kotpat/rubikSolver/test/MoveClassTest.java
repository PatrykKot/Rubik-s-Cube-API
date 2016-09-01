package my.kotpat.rubikSolver.test;

import java.util.HashSet;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import my.kotpat.rubikSolver.domain.Move;
import my.kotpat.rubikSolver.exceptions.DifferentMoveLayersException;
import my.kotpat.rubikSolver.exceptions.FullAngleMoveException;
import my.kotpat.rubikSolver.exceptions.UnsupportedMoveException;

@RunWith(BlockJUnit4ClassRunner.class)
public class MoveClassTest {
	@Test
	public void creatingMovesTest() {
		boolean tryFlag = false;
		Move move = null;
		try {
			move = new Move("  2F2   ");
		} catch (UnsupportedMoveException e) {
			tryFlag = true;
		}
		Assert.assertFalse("F2 move not created", tryFlag);
		Assert.assertNotNull("Move F2 is null", move);
		Assert.assertTrue("F2 move is not correct",
				(move.getMoveLayer() == Move.MoveLayer.FRONT) && (move.isDoubled()));

		tryFlag = false;
		try {
			move = new Move(" ;/RLRF ;/? ");
		} catch (UnsupportedMoveException e) {
			tryFlag = true;
		}
		Assert.assertFalse("R move not created", tryFlag);
		Assert.assertNotNull("Move R is null", move);
		Assert.assertTrue("R move is not correct",
				(move.getMoveLayer() == Move.MoveLayer.RIGHT) && (!move.isDoubled()) && (move.isClockwise()));

		tryFlag = false;
		try {
			move = new Move(" ;/U'LRF ;/? ");
		} catch (UnsupportedMoveException e) {
			tryFlag = true;
		}
		Assert.assertFalse("U move not created", tryFlag);
		Assert.assertNotNull("Move U is null", move);
		Assert.assertTrue("U move is not correct",
				(move.getMoveLayer() == Move.MoveLayer.UP) && (!move.isDoubled()) && (!move.isClockwise()));

		tryFlag = false;
		try {
			move = new Move(" ;/K ;/? ");
		} catch (UnsupportedMoveException e) {
			tryFlag = true;
		}
		Assert.assertTrue("K move created", tryFlag);
	}

	@Test
	public void movesEqualTest() throws UnsupportedMoveException {
		Move move1 = new Move("R2'");
		Move move2 = new Move("R2");
		Assert.assertTrue("R2' is not equal to R2", move1.equals(move2));

		move1 = new Move("R2'");
		move2 = new Move("R");
		Assert.assertFalse("R2' is not equal to R", move1.equals(move2));

		move1 = new Move("L ");
		move2 = new Move(" L");

		Set<Move> tempSet = new HashSet<Move>();
		tempSet.add(move1);
		tempSet.add(move2);
		Assert.assertTrue("Two L moves are not equal in Set object", tempSet.size() == 1);
	}

	@Test
	public void movesConnecting() throws UnsupportedMoveException {
		Move moveClock = new Move("R");
		Move moveCounterClock = new Move("R'");
		Move moveDoubled = new Move("R2");
		Move moveAnother = new Move("L");

		boolean tryFlagDifferentMoveLayers = false;
		boolean tryFlagFullAngleMove = false;
		Move totalMove = null;
		try {
			totalMove = Move.connect(moveClock, moveClock);
		} catch (DifferentMoveLayersException e) {
			tryFlagDifferentMoveLayers = true;
		} catch (FullAngleMoveException e) {
			tryFlagFullAngleMove = true;
		}
		Assert.assertFalse("Clockwise and clockwise == differentMoveLayers", tryFlagDifferentMoveLayers);
		Assert.assertFalse("Clockwise and clockwise == fullAngleMove", tryFlagFullAngleMove);
		Assert.assertTrue("Clockwise and clockwise != doubled", totalMove.isDoubled());

		tryFlagDifferentMoveLayers = false;
		tryFlagFullAngleMove = false;
		totalMove = null;
		try {
			totalMove = Move.connect(moveClock, moveCounterClock);
		} catch (DifferentMoveLayersException e) {
			tryFlagDifferentMoveLayers = true;
		} catch (FullAngleMoveException e) {
			tryFlagFullAngleMove = true;
		}
		Assert.assertFalse("Clockwise and counter-clockwise == differentMoveLayers", tryFlagDifferentMoveLayers);
		Assert.assertTrue("Clockwise and counter-clockwise != fullAngleMove", tryFlagFullAngleMove);

		tryFlagDifferentMoveLayers = false;
		tryFlagFullAngleMove = false;
		totalMove = null;
		try {
			totalMove = Move.connect(moveClock, moveDoubled);
		} catch (DifferentMoveLayersException e) {
			tryFlagDifferentMoveLayers = true;
		} catch (FullAngleMoveException e) {
			tryFlagFullAngleMove = true;
		}
		Assert.assertFalse("Clockwise and doubled == differentMoveLayers", tryFlagDifferentMoveLayers);
		Assert.assertFalse("Clockwise and doubled == fullAngleMove", tryFlagFullAngleMove);
		Assert.assertFalse("Clockwise and doubled != counter-clockwise", totalMove.isClockwise());

		tryFlagDifferentMoveLayers = false;
		tryFlagFullAngleMove = false;
		totalMove = null;
		try {
			totalMove = Move.connect(moveCounterClock, moveDoubled);
		} catch (DifferentMoveLayersException e) {
			tryFlagDifferentMoveLayers = true;
		} catch (FullAngleMoveException e) {
			tryFlagFullAngleMove = true;
		}
		Assert.assertFalse("Counter-clockwise and doubled == differentMoveLayers", tryFlagDifferentMoveLayers);
		Assert.assertFalse("Counter-clockwise and doubled == fullAngleMove", tryFlagFullAngleMove);
		Assert.assertTrue("Counter-clockwise and doubled != clockwise", totalMove.isClockwise());

		tryFlagDifferentMoveLayers = false;
		tryFlagFullAngleMove = false;
		totalMove = null;
		try {
			totalMove = Move.connect(moveDoubled, moveDoubled);
		} catch (DifferentMoveLayersException e) {
			tryFlagDifferentMoveLayers = true;
		} catch (FullAngleMoveException e) {
			tryFlagFullAngleMove = true;
		}
		Assert.assertFalse("Doubled and doubled == differentMoveLayers", tryFlagDifferentMoveLayers);
		Assert.assertTrue("Doubled and doubled != fullAngleMove", tryFlagFullAngleMove);

		tryFlagDifferentMoveLayers = false;
		tryFlagFullAngleMove = false;
		totalMove = null;
		try {
			totalMove = Move.connect(moveDoubled, moveAnother);
		} catch (DifferentMoveLayersException e) {
			tryFlagDifferentMoveLayers = true;
		} catch (FullAngleMoveException e) {
			tryFlagFullAngleMove = true;
		}
		Assert.assertTrue("R and L != differentMoveLayers", tryFlagDifferentMoveLayers);
		Assert.assertFalse("R and L == fullAngleMove", tryFlagFullAngleMove);
	}
}
