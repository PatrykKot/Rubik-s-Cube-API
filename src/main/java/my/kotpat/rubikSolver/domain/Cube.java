package my.kotpat.rubikSolver.domain;

import java.util.ArrayList;
import java.util.List;

import my.kotpat.rubikSolver.domain.Move.MoveLayer;
import my.kotpat.rubikSolver.exceptions.UnsupportedLayerDimensionException;

/**
 * This class represents the cube with specific {@link Cube#dimension}. Allows
 * rotating layers, scrambling, and checking information about fields (like
 * {@link Cube#isSolved()}.
 * 
 * @author Patryk Kotlarz
 *
 */
public class Cube {
	/**
	 * Dimension of the cube.
	 */
	private int dimension;

	/**
	 * References to all of the layers.
	 */
	private List<Layer> layerList = new ArrayList<Layer>();

	/**
	 * The upper layer.
	 */
	private Layer up;

	/**
	 * The lower layer.
	 */
	private Layer down;

	/**
	 * The right layer.
	 */
	private Layer right;

	/**
	 * The left layer.
	 */
	private Layer left;

	/**
	 * The front layer.
	 */
	private Layer front;

	/**
	 * The back layer.
	 */
	private Layer back;

	/**
	 * Constructor creates cube with the specific {@link dimension}. This
	 * constructor allocates memory for all layers (with specific color and
	 * name) and sets connections between layers to allow correct rotations.
	 * 
	 * @param dimension
	 *            dimension of cube
	 * @throws UnsupportedLayerDimensionException
	 *             if dimension is less than 2 or more than 3 (in currect
	 *             version of project)
	 */
	public Cube(int dimension) throws UnsupportedLayerDimensionException {

		up = new Layer(dimension, Field.Color.WHITE, "U");
		down = new Layer(dimension, Field.Color.YELLOW, "D");
		right = new Layer(dimension, Field.Color.RED, "R");
		left = new Layer(dimension, Field.Color.ORANGE, "L");
		front = new Layer(dimension, Field.Color.GREEN, "F");
		back = new Layer(dimension, Field.Color.BLUE, "B");
		this.dimension = dimension;

		up.setUp(back);
		up.setDown(front);
		up.setRight(right);
		up.setLeft(left);

		up.addLayerPosition(Layer.LayerPosition.UP_LAYER, Layer.RowColPosition.DOWN);
		up.addLayerPosition(Layer.LayerPosition.DOWN_LAYER, Layer.RowColPosition.UP);
		up.addLayerPosition(Layer.LayerPosition.RIGHT_LAYER, Layer.RowColPosition.LEFT);
		up.addLayerPosition(Layer.LayerPosition.LEFT_LAYER, Layer.RowColPosition.RIGHT);

		down.setUp(front);
		down.setDown(back);
		down.setRight(right);
		down.setLeft(left);

		down.addLayerPosition(Layer.LayerPosition.UP_LAYER, Layer.RowColPosition.DOWN);
		down.addLayerPosition(Layer.LayerPosition.DOWN_LAYER, Layer.RowColPosition.UP);
		down.addLayerPosition(Layer.LayerPosition.RIGHT_LAYER, Layer.RowColPosition.RIGHT);
		down.addLayerPosition(Layer.LayerPosition.LEFT_LAYER, Layer.RowColPosition.LEFT);

		right.setUp(back);
		right.setDown(front);
		right.setRight(down);
		right.setLeft(up);

		right.addLayerPosition(Layer.LayerPosition.UP_LAYER, Layer.RowColPosition.RIGHT);
		right.addLayerPosition(Layer.LayerPosition.DOWN_LAYER, Layer.RowColPosition.RIGHT);
		right.addLayerPosition(Layer.LayerPosition.RIGHT_LAYER, Layer.RowColPosition.RIGHT);
		right.addLayerPosition(Layer.LayerPosition.LEFT_LAYER, Layer.RowColPosition.RIGHT);

		left.setUp(back);
		left.setDown(front);
		left.setRight(up);
		left.setLeft(down);

		left.addLayerPosition(Layer.LayerPosition.UP_LAYER, Layer.RowColPosition.LEFT);
		left.addLayerPosition(Layer.LayerPosition.DOWN_LAYER, Layer.RowColPosition.LEFT);
		left.addLayerPosition(Layer.LayerPosition.RIGHT_LAYER, Layer.RowColPosition.LEFT);
		left.addLayerPosition(Layer.LayerPosition.LEFT_LAYER, Layer.RowColPosition.LEFT);

		front.setUp(up);
		front.setDown(down);
		front.setRight(right);
		front.setLeft(left);

		front.addLayerPosition(Layer.LayerPosition.UP_LAYER, Layer.RowColPosition.DOWN);
		front.addLayerPosition(Layer.LayerPosition.DOWN_LAYER, Layer.RowColPosition.UP);
		front.addLayerPosition(Layer.LayerPosition.RIGHT_LAYER, Layer.RowColPosition.DOWN);
		front.addLayerPosition(Layer.LayerPosition.LEFT_LAYER, Layer.RowColPosition.DOWN);

		back.setUp(down);
		back.setDown(up);
		back.setRight(right);
		back.setLeft(left);

		back.addLayerPosition(Layer.LayerPosition.UP_LAYER, Layer.RowColPosition.DOWN);
		back.addLayerPosition(Layer.LayerPosition.DOWN_LAYER, Layer.RowColPosition.UP);
		back.addLayerPosition(Layer.LayerPosition.RIGHT_LAYER, Layer.RowColPosition.UP);
		back.addLayerPosition(Layer.LayerPosition.LEFT_LAYER, Layer.RowColPosition.UP);

		layerList.add(back);
		layerList.add(left);
		layerList.add(up);
		layerList.add(right);
		layerList.add(front);
		layerList.add(down);
	}

	/**
	 * Method rotate a layer specified by {@link Move} object.
	 * 
	 * @param move
	 *            {@link Move} object
	 */
	public void move(Move move) {
		boolean doubled = move.isDoubled();
		boolean clockwise = move.isClockwise();
		MoveLayer moveLayer = move.getMoveLayer();

		switch (moveLayer) {
		case BACK: {
			back.turnAround(clockwise, doubled);
			break;
		}
		case FRONT: {
			front.turnAround(clockwise, doubled);
			break;
		}
		case RIGHT: {
			right.turnAround(clockwise, doubled);
			break;
		}
		case LEFT: {
			left.turnAround(clockwise, doubled);
			break;
		}
		case UP: {
			up.turnAround(clockwise, doubled);
			break;
		}
		case DOWN: {
			down.turnAround(clockwise, doubled);
			break;
		}
		}
	}

	/**
	 * Method do all the rotations specified by {@link Scramble} object.
	 * 
	 * @param scramble
	 *            {@link Scramble} object
	 */
	public void scramble(Scramble scramble) {
		for (Move move : scramble) {
			move(move);
		}
	}

	/**
	 * Returns {@code true} if cube is solved.
	 * 
	 * @return true if cube is solved
	 */
	public boolean isSolved() {
		for (Layer layer : layerList) {
			if (!layer.hasAllFieldsEqual())
				return false;
		}
		return true;
	}

	/**
	 * Returns dimension of the cube.
	 * 
	 * @return dimension of the cube
	 */
	public int getDimension() {
		return dimension;
	}

	/**
	 * Returns the upper layer.
	 * 
	 * @return the upper layer
	 */
	public Layer getUp() {
		return up;
	}

	/**
	 * Returns the lower layer.
	 * 
	 * @return the lower layer
	 */
	public Layer getDown() {
		return down;
	}

	/**
	 * Returns the right layer.
	 * 
	 * @return the right layer
	 */
	public Layer getRight() {
		return right;
	}

	/**
	 * Returns the left layer.
	 * 
	 * @return the left layer
	 */
	public Layer getLeft() {
		return left;
	}

	/**
	 * Returns the front layer.
	 * 
	 * @return the front layer
	 */
	public Layer getFront() {
		return front;
	}

	/**
	 * Returns the back layer.
	 * 
	 * @return the back layer
	 */
	public Layer getBack() {
		return back;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder("Cube " + dimension + "x" + dimension + "x" + dimension);

		for (Layer layer : layerList) {
			builder.append("\n");
			builder.append(layer.toString());
		}

		return builder.toString();
	}
}
