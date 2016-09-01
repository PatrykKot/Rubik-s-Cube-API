package my.kotpat.rubikSolver.domain;

import java.util.HashMap;

import my.kotpat.rubikSolver.exceptions.UnsupportedLayerDimensionException;

/**
 * The class represents one "layer" of the cube. The "layer" because single
 * object truly represents only one face (9 stickers/fields for 3x3x3 cube).
 * Connection between this face and four neighboring faces are true layer. This
 * connections are included inside the class.
 * 
 * @author Patryk Kotlarz
 *
 */
public class Layer {
	/**
	 * The maximum cube dimension. The structure of this project is designed for
	 * every cube with dimension more than 2x2x2 but the moving system is
	 * designed to turn only external faces. Internal faces (like M, r, u etc.)
	 * are not supported in this version.
	 */
	public static final int MAXIMUM_CUBE_DIMENSION = 3;

	/**
	 * Text name of layer. Additional field for user.
	 */
	private String layerName;

	/**
	 * Dimension of the layer (and the cube).
	 */
	private int dimension;

	/**
	 * Two-dimensional array of layer's fields (2x2 for 2x2x2 cube, 3x3 for
	 * 3x3x3 cube etc.).
	 */
	private Field[][] layerFields;

	/**
	 * The reference for layer which is above the current layer.
	 */
	private Layer up;

	/**
	 * The reference for layer which is below the current layer.
	 */
	private Layer down;

	/**
	 * The layer which is to the right of the current layer.
	 */
	private Layer right;

	/**
	 * The layer which is to the left of the current layer.
	 */
	private Layer left;

	/**
	 * Map represents connections between neighboring layers. See an explanation
	 * in README file. {@link LayerPosition} tells about layer above, below, to
	 * the right and to the left, {@link RowColPosition} tells which row or
	 * column of the 2D field's array of the neighboring layer is connected with
	 * the current layer. With this information "layer" (one face) of the cube
	 * becomes a true layer of the cube.
	 */
	private HashMap<LayerPosition, RowColPosition> layerPositions = new HashMap<Layer.LayerPosition, Layer.RowColPosition>();

	/**
	 * Possible rows or columns positions of neighboring layers which are
	 * connected to the current layer.
	 */
	public enum RowColPosition {
		UP, DOWN, RIGHT, LEFT
	}

	/**
	 * Possible positions of neighboring layers.
	 */
	public enum LayerPosition {
		UP_LAYER, DOWN_LAYER, RIGHT_LAYER, LEFT_LAYER
	}

	/**
	 * Creates layer with specific dimension, default color and name.
	 * 
	 * @param dimension
	 *            layer's dimension
	 * @param defaultFieldColor
	 *            default layer's color
	 * @param layerName
	 *            additional layer's name
	 * @throws UnsupportedLayerDimensionException
	 *             if dimension is lower than 2 or bigger than
	 *             {@link MAXIMUM_CUBE_DIMENSION}
	 */
	public Layer(int dimension, Field.Color defaultFieldColor, String layerName)
			throws UnsupportedLayerDimensionException {
		if (dimension < 2 || dimension > MAXIMUM_CUBE_DIMENSION)
			throw new UnsupportedLayerDimensionException();

		this.dimension = dimension;
		this.layerName = layerName;

		layerFields = new Field[dimension][dimension];
		for (int row = 0; row < dimension; row++) {
			for (int col = 0; col < dimension; col++) {
				layerFields[row][col] = new Field(defaultFieldColor);
			}
		}
	}

	/**
	 * Returns true if all the fields have the same color.
	 * 
	 * @return true if all the fields have the same color.
	 */
	public boolean hasAllFieldsEqual() {
		Field.Color color = layerFields[0][0].getFieldColor();

		for (Field[] fields : layerFields) {
			for (Field field : fields) {
				if (!field.getFieldColor().equals(color))
					return false;
			}
		}

		return true;
	}

	/**
	 * Static method which transposes 2D field's array. Used in the a rotation
	 * of 90 degrees.
	 * 
	 * @param fields
	 *            2D field's array
	 * @return new rotated array
	 */
	private static Field[][] getTransposed(Field[][] fields) {
		int dimension = fields.length;
		Field[][] temporaryLayerFields = new Field[dimension][dimension];
		for (int row = 0; row < dimension; row++) {
			for (int col = 0; col < dimension; col++) {
				temporaryLayerFields[row][col] = fields[col][row];
			}
		}
		return temporaryLayerFields;
	}

	/**
	 * Static method which swaps rows in the 2D field's array. Used in the a
	 * rotation of 90 and 180 degrees.
	 * 
	 * @param fields
	 *            2D field's array
	 */
	private static void swapRows(Field[][] fields) {
		int length = fields.length;
		for (Field[] fieldArray : fields) {
			for (int i = 0; i < length / 2; i++) {
				Field temp = fieldArray[i];
				fieldArray[i] = fieldArray[length - i - 1];
				fieldArray[length - i - 1] = temp;
			}
		}
	}

	/**
	 * Static method which swaps columns in the 2D field's array. Used in the a
	 * rotation of 90 and 180 degrees.
	 * 
	 * @param fields
	 *            2D field's array
	 */
	private static void swapCols(Field[][] fields) {
		int length = fields.length;
		for (int i = 0; i < length / 2; i++) {
			Field[] temp = fields[i];
			fields[i] = fields[length - i - 1];
			fields[length - i - 1] = temp;
		}
	}

	/**
	 * Method rotates layer clockwise, counter-clockwise or doubled. If move is
	 * doubled it delegates to the private method
	 * {@link Layer#turnAroundDoubled()}. If it is a 90 degree rotation, at
	 * first method transposes field's array and then swaps rows (clockwise) or
	 * swaps columns (counter-clockwise) and then rotates neighboring layers by
	 * method {@link Layer#turnAroundNeighbors(boolean)}
	 * 
	 * @param clockwise
	 *            is clockwise
	 * @param doubled
	 *            is doubled
	 */
	public void turnAround(boolean clockwise, boolean doubled) {
		if (doubled) {
			turnAroundDoubled();
			return;
		}

		layerFields = getTransposed(layerFields);

		if (clockwise) {
			swapRows(layerFields);
		} else {
			swapCols(layerFields);
		}

		turnAroundNeighbors(clockwise);
	}

	/**
	 * Method does 180 degrees rotation of layer by swapping rows and columns
	 * and then rotates neighboring layers.
	 */
	private void turnAroundDoubled() {
		swapRows(layerFields);
		swapCols(layerFields);
		turnAroundNeighborsDoubled();
	}

	/**
	 * Method connects neighboring layer's position with its row/column position
	 * to the {@link Layer#layerPositions}.
	 * 
	 * @param layerPosition
	 *            neighboring layer position
	 * @param rowColPosition
	 *            row/column position of neighboring layer
	 */
	public void addLayerPosition(LayerPosition layerPosition, RowColPosition rowColPosition) {
		this.layerPositions.put(layerPosition, rowColPosition);
	}

	/**
	 * Method rotates neighboring layers by swaping their rows or columns.
	 * 
	 * @param clockwise
	 *            is clockwise
	 */
	private void turnAroundNeighbors(boolean clockwise) {

		Field[] upFields = up.getFields(layerPositions.get(LayerPosition.UP_LAYER));
		Field[] downFields = down.getFields(layerPositions.get(LayerPosition.DOWN_LAYER));
		Field[] rightFields = right.getFields(layerPositions.get(LayerPosition.RIGHT_LAYER));
		Field[] leftFields = left.getFields(layerPositions.get(LayerPosition.LEFT_LAYER));

		if (clockwise) {
			up.setFields(layerPositions.get(LayerPosition.UP_LAYER), leftFields);
			left.setFields(layerPositions.get(LayerPosition.LEFT_LAYER), downFields);
			down.setFields(layerPositions.get(LayerPosition.DOWN_LAYER), rightFields);
			right.setFields(layerPositions.get(LayerPosition.RIGHT_LAYER), upFields);
		} else {
			up.setFields(layerPositions.get(LayerPosition.UP_LAYER), rightFields);
			right.setFields(layerPositions.get(LayerPosition.RIGHT_LAYER), downFields);
			down.setFields(layerPositions.get(LayerPosition.DOWN_LAYER), leftFields);
			left.setFields(layerPositions.get(LayerPosition.LEFT_LAYER), upFields);
		}
	}

	/**
	 * The same action as {@link Layer#turnAroundNeighbors(boolean)} but swaps
	 * every opposing pairs of rows and columns.
	 */
	private void turnAroundNeighborsDoubled() {
		Field[] upFields = up.getFields(layerPositions.get(LayerPosition.UP_LAYER));
		Field[] downFields = down.getFields(layerPositions.get(LayerPosition.DOWN_LAYER));
		Field[] rightFields = right.getFields(layerPositions.get(LayerPosition.RIGHT_LAYER));
		Field[] leftFields = left.getFields(layerPositions.get(LayerPosition.LEFT_LAYER));

		up.setFields(layerPositions.get(LayerPosition.UP_LAYER), downFields);
		left.setFields(layerPositions.get(LayerPosition.LEFT_LAYER), rightFields);
		down.setFields(layerPositions.get(LayerPosition.DOWN_LAYER), upFields);
		right.setFields(layerPositions.get(LayerPosition.RIGHT_LAYER), leftFields);
	}

	/**
	 * Returns the specific Field object of Layer.
	 * 
	 * @param row
	 *            number of row
	 * @param col
	 *            number of column
	 * @return {@link Field} object
	 */
	public Field getField(int row, int col) {
		return layerFields[row][col];
	}

	/**
	 * Setts the specific field in Layer object.
	 * 
	 * @param row
	 *            number of row
	 * @param col
	 *            number of column
	 * @param field
	 *            {@link Field} object
	 */
	public void setField(int row, int col, Field field) {
		layerFields[row][col] = field;
	}

	/**
	 * Set a row or column of fields to the specific position in the layer.
	 * 
	 * @param rowColPosition
	 *            row or column position
	 * @param fields
	 *            row or column of fields
	 */
	private void setFields(RowColPosition rowColPosition, Field[] fields) {

		switch (rowColPosition) {
		case DOWN: {
			for (int i = 0; i < dimension; i++) {
				layerFields[dimension - 1][dimension - i - 1] = fields[i];
			}
			break;
		}
		case LEFT: {
			for (int i = 0; i < dimension; i++) {
				layerFields[dimension - i - 1][0] = fields[i];
			}
			break;
		}
		case RIGHT: {
			for (int i = 0; i < dimension; i++) {
				layerFields[i][dimension - 1] = fields[i];
			}
			break;
		}
		case UP: {
			for (int i = 0; i < dimension; i++) {
				layerFields[0][i] = fields[i];
			}
			break;
		}
		}
	}

	/**
	 * Returns row or column of fields from the specific position in the layer.
	 * 
	 * @param rowColPosition
	 *            row or column position
	 * @return row or column of fields
	 */
	private Field[] getFields(RowColPosition rowColPosition) {
		Field[] tempFields = new Field[dimension];

		switch (rowColPosition) {
		case DOWN: {
			for (int i = 0; i < dimension; i++) {
				tempFields[i] = layerFields[dimension - 1][dimension - i - 1];
			}
			break;
		}
		case LEFT: {
			for (int i = 0; i < dimension; i++) {
				tempFields[i] = layerFields[dimension - i - 1][0];
			}
			break;
		}
		case RIGHT: {
			for (int i = 0; i < dimension; i++) {
				tempFields[i] = layerFields[i][dimension - 1];
			}
			break;
		}
		case UP: {
			for (int i = 0; i < dimension; i++) {
				tempFields[i] = layerFields[0][i];
			}
			break;
		}
		}

		return tempFields;
	}

	/**
	 * Returns {@link Layer} which is above the current layer.
	 * 
	 * @return layer above
	 */
	public Layer getUp() {
		return up;
	}

	/**
	 * Sets {@link Layer} which is above the current layer.
	 * 
	 * @param up
	 *            layer above
	 */
	public void setUp(Layer up) {
		this.up = up;
	}

	/**
	 * Returns {@link Layer} which is bellow the current layer.
	 * 
	 * @return layer bellow
	 */
	public Layer getDown() {
		return down;
	}

	/**
	 * Sets {@link Layer} which is bellow the current layer.
	 * 
	 * @param down
	 *            layer bellow
	 */
	public void setDown(Layer down) {
		this.down = down;
	}

	/**
	 * Returns {@link Layer} which is to the right of the current layer.
	 * 
	 * @return layer to the right
	 */
	public Layer getRight() {
		return right;
	}

	/**
	 * Sets {@link Layer} which is to the right of the current layer.
	 * 
	 * @param right
	 *            layer to the right
	 */
	public void setRight(Layer right) {
		this.right = right;
	}

	/**
	 * Returns {@link Layer} which is to the left of the current layer.
	 * 
	 * @return layer to the left
	 */
	public Layer getLeft() {
		return left;
	}

	/**
	 * Sets {@link Layer} which is to the left of the current layer.
	 * 
	 * @param left
	 *            layer to the left
	 */
	public void setLeft(Layer left) {
		this.left = left;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder("Layer " + layerName + " " + dimension + "x" + dimension);
		for (Field[] fields : layerFields) {
			builder.append("\n");
			for (Field field : fields) {
				builder.append(field.getFieldColor().toString());
				builder.append("\t");
			}
		}

		return builder.toString();
	}
}
