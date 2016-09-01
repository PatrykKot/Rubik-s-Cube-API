package my.kotpat.rubikSolver.domain;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import my.kotpat.rubikSolver.exceptions.DifferentMoveLayersException;
import my.kotpat.rubikSolver.exceptions.FullAngleMoveException;
import my.kotpat.rubikSolver.exceptions.UnsupportedMoveException;

/**
 * Representation of the single move of the cube's layer standardized with the
 * WCA (World Cube Association) notation. Allowed moves: R, L, U, D, F, B
 * clockwise (e.g. R, U) or counter-clockwise (e.g. R', U') or "doubled" moves
 * (e.g. R2, U2).
 * 
 * @author Patryk Kotlarz
 *
 */
public class Move {

	/**
	 * Equals <code>true</code> if the move is clockwise.
	 */
	private boolean clockwise = false;

	/**
	 * Equals <code>true</code> if the move is "doubled".
	 */
	private boolean doubled = false;

	/**
	 * Allowed moves (layer without specific information about direction).
	 */
	public enum MoveLayer {
		RIGHT, LEFT, UP, DOWN, FRONT, BACK
	}

	/**
	 * Layer to move.
	 */
	private MoveLayer moveLayer;

	/**
	 * Standard copying constructor.
	 * 
	 * @param move
	 *            Move object to copy
	 */
	public Move(Move move) {
		this.clockwise = move.clockwise;
		this.doubled = move.doubled;
		this.moveLayer = move.moveLayer;
	}

	/**
	 * Constructor which converts text description about turning the layer
	 * ("R2", "U'") to the specific Move object. Uses regex "[R,L,U,D,F,B]2?'?"
	 * to filter the unused information.
	 * 
	 * @param moveString
	 *            text description of move
	 * @throws UnsupportedMoveException
	 *             if a <code>moveString</code> does not match to regex
	 */
	public Move(String moveString) throws UnsupportedMoveException {
		String moveStringTemp = null;
		Pattern pattern = Pattern.compile("[R,L,U,D,F,B]2?'?");
		Matcher matcher = pattern.matcher(moveString);
		if (matcher.find()) {
			moveStringTemp = matcher.group();
		} else
			throw new UnsupportedMoveException();

		if (moveStringTemp.contains("2")) {
			doubled = true;
			moveStringTemp = moveStringTemp.replace("'", "");
		}

		if (!moveStringTemp.contains("'")) {
			clockwise = true;
		}

		char c = moveStringTemp.charAt(0);

		switch (c) {
		case 'R': {
			moveLayer = MoveLayer.RIGHT;
			break;
		}
		case 'L': {
			moveLayer = MoveLayer.LEFT;
			break;
		}
		case 'U': {
			moveLayer = MoveLayer.UP;
			break;
		}
		case 'D': {
			moveLayer = MoveLayer.DOWN;
			break;
		}
		case 'F': {
			moveLayer = MoveLayer.FRONT;
			break;
		}
		case 'B': {
			moveLayer = MoveLayer.BACK;
			break;
		}
		default: {
			throw new UnsupportedMoveException();
		}
		}
	}

	/**
	 * MoveLayer getter.
	 * 
	 * @return layer to move
	 */
	public MoveLayer getMoveLayer() {
		return moveLayer;
	}

	/**
	 * 
	 * MoveLayer setter.
	 * 
	 * @param moveLayer
	 *            layer to move
	 */
	public void setMoveLayer(MoveLayer moveLayer) {
		this.moveLayer = moveLayer;
	}

	/**
	 * Returns <code>true</code> if the move is clockwise.
	 * 
	 * @return true if the move is clockwise
	 */
	public boolean isClockwise() {
		return clockwise;
	}

	/**
	 * Setter of {@link clockwise} parameter.
	 * 
	 * @param clockwise
	 *            true if move should be clockwise
	 */
	public void setClockwise(boolean clockwise) {
		this.clockwise = clockwise;
	}

	/**
	 * Returns <code>true</code> if the move is "doubled".
	 * 
	 * @return true of the move is "doubled"
	 */
	public boolean isDoubled() {
		return doubled;
	}

	/**
	 * Setter of {@link doubled} parameter.
	 * 
	 * @param doubled
	 *            true if move should be "doubled"
	 */
	public void setDoubled(boolean doubled) {
		this.doubled = doubled;
	}

	/**
	 * Return the short form standardized with the WCA notation.
	 * 
	 * @return short text form of move
	 */
	public String getMoveString() {
		String moveString = "" + moveLayer.toString().charAt(0);
		if (!doubled) {
			if (!clockwise) {
				moveString += "'";
			}
		} else {
			moveString += "2";
		}
		return moveString;
	}

	/**
	 * Static function which connects two moves into one (or not if there is no
	 * move e.h. U + U' = no move). Method checks all the possibilities.
	 * 
	 * @param move1
	 *            first move
	 * @param move2
	 *            second move
	 * @return total move
	 * @throws DifferentMoveLayersException
	 *             if layers are different
	 * @throws FullAngleMoveException
	 *             if there is no move
	 */
	public static Move connect(Move move1, Move move2) throws DifferentMoveLayersException, FullAngleMoveException {
		if (move1.getMoveLayer() != move2.getMoveLayer())
			throw new DifferentMoveLayersException();
		Move move = new Move(move1);
		move.setDoubled(false);

		if (move1.doubled) {
			if (move2.doubled) {
				throw new FullAngleMoveException();
			} else {
				if (move2.clockwise) {
					move.setClockwise(false);
				} else {
					move.setClockwise(true);
				}
			}
		} else {
			if (move1.clockwise) {
				if (move2.doubled) {
					move.setClockwise(false);
				} else {
					if (move2.clockwise) {
						move.setDoubled(true);
					} else {
						throw new FullAngleMoveException();
					}
				}
			} else {
				if (move2.doubled) {
					move.setClockwise(true);
				} else {
					if (move2.clockwise) {
						throw new FullAngleMoveException();
					} else {
						move.setDoubled(true);
					}
				}
			}
		}

		return move;
	}

	@Override
	public String toString() {
		return "Move " + moveLayer.toString().charAt(0) + "\t[clock = " + clockwise + "\t, doubled = " + doubled + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (clockwise ? 1231 : 1237);
		result = prime * result + (doubled ? 1231 : 1237);
		result = prime * result + ((moveLayer == null) ? 0 : moveLayer.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Move other = (Move) obj;
		if (clockwise != other.clockwise)
			return false;
		if (doubled != other.doubled)
			return false;
		if (moveLayer != other.moveLayer)
			return false;
		return true;
	}

}
