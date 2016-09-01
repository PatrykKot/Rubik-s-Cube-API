package my.kotpat.rubikSolver.domain;

/**
 * Base class which represents one single sticker of the cube. Field object has
 * its color (red, blue, yellow, white, orange or green). Class allows only
 * setting and getting color.
 * 
 * @author Patryk Kotlarz
 *
 */
public class Field {

	/**
	 * Possible color of the field (sticker).
	 */
	public enum Color {
		RED, BLUE, YELLOW, WHITE, ORANGE, GREEN
	}

	/**
	 * Field's color.
	 */
	private Color fieldColor;

	/**
	 * Constructor of the class (parameterized because field color shouldn't be
	 * null).
	 * 
	 * @param fieldColor
	 *            field's color
	 */
	public Field(Color fieldColor) {
		this.fieldColor = fieldColor;
	}

	/**
	 * Field's color getter.
	 * 
	 * @return color field
	 */
	public Color getFieldColor() {
		return fieldColor;
	}

	/**
	 * Field's color setter.
	 * 
	 * @param fieldColor
	 *            color field
	 */
	public void setFieldColor(Color fieldColor) {
		this.fieldColor = fieldColor;
	}

	@Override
	public String toString() {
		return "Field " + fieldColor.toString();
	}

}
