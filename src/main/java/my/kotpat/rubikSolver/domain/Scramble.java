package my.kotpat.rubikSolver.domain;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import my.kotpat.rubikSolver.exceptions.DifferentMoveLayersException;
import my.kotpat.rubikSolver.exceptions.FullAngleMoveException;
import my.kotpat.rubikSolver.exceptions.UnsupportedMoveException;

/**
 * 
 * This class represents scramble (sequence of moves). It has list of moves
 * inside, allows adding new moves, deleting moves, reversing scramble and
 * simplifying scramble. Implements {@code Iterable<Move>} interface for greater
 * comfort in use.
 * 
 * @author Patryk Kotlarz
 *
 */
public class Scramble implements Iterable<Move> {

	/**
	 * List of moves.
	 */
	private List<Move> movesList;

	/**
	 * Default constructor. It allocates memory for {@link movesList}.
	 */
	public Scramble() {
		movesList = new LinkedList<Move>();
	}

	/**
	 * Parameterized constructor which adds all list of moves to the scramble.
	 * 
	 * @param movesList
	 *            list of moves
	 */
	public Scramble(List<Move> movesList) {
		this();
		this.movesList.addAll(movesList);
	}

	/**
	 * Constructor which converts text description of the scramble (e.g. R2 U2
	 * L' L R") to the specific Scramble object. Uses regex "[R,L,U,D,F,B]2?'?"
	 * to filter the unused information.
	 * 
	 * @param scramble
	 *            text description of the scramble
	 */
	public Scramble(String scramble) {
		this();
		Pattern pattern = Pattern.compile("[r,R,l,L,u,U,d,D,f,F,b,B]2?'?");
		Matcher matcher = pattern.matcher(scramble);
		while (matcher.find()) {
			try {
				movesList.add(new Move(matcher.group()));
			} catch (UnsupportedMoveException e) {
			}
		}
	}

	/**
	 * Adds new move to the sequence.
	 * 
	 * @param move
	 *            new move
	 */
	public void add(Move move) {
		movesList.add(move);
	}

	/**
	 * Deletes last move.
	 */
	public void deleteLast() {
		movesList.remove(movesList.size() - 1);
	}

	/**
	 * Reversing the scramble (e.g. R2 U' L {@code ->} L' U R2). At first method
	 * is reversing the list of moves and then changes in every move: clockwise
	 * != clockwise
	 */
	public void reverse() {
		Collections.reverse(movesList);

		for (Move move : movesList) {
			move.setClockwise(!move.isClockwise());
		}
	}

	/**
	 * 
	 * Returns the number of moves.
	 * 
	 * @return number of moves in scramble
	 */
	public int size() {
		return movesList.size();
	}

	/**
	 * Simplifies the scramble by trying to connect
	 * {@link Move#connect(Move, Move)} every neighboring moves (e.g.
	 * "R2 R' L2 F" {@code ->} "R L2 F").
	 */
	public void simplify() {
		for (int i = 0; i < movesList.size(); i++) {
			Move move = movesList.get(i);
			for (int j = i + 1; j < movesList.size(); j++) {
				try {
					move = Move.connect(move, movesList.get(j));
					movesList.remove(j);
					movesList.set(i, move);
					j--;
				} catch (DifferentMoveLayersException e) {
					// Break if next move has different moving layer
					break;
				} catch (FullAngleMoveException e) {
					// Delete both moves if there is no move
					movesList.remove(j);
					movesList.remove(i);
					i--;
					break;
				}
			}
		}
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder("Scramble");
		for (Move move : movesList) {
			builder.append(' ');
			builder.append(move.getMoveString());
		}
		return builder.toString();
	}

	public Iterator<Move> iterator() {
		return new Iterator<Move>() {
			int counter = 0;

			public Move next() {
				return movesList.get(counter++);
			}

			public boolean hasNext() {
				return counter < movesList.size();
			}
		};
	}
}
