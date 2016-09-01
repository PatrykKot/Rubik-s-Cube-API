package my.kotpat.rubikSolver.exceptions;

import my.kotpat.rubikSolver.domain.Cube;
import my.kotpat.rubikSolver.domain.Layer;

/**
 * @see Layer#Layer(int, my.kotpat.rubikSolver.domain.Field.Color, String)
 * @see Cube#Cube(int)
 * @author Patryk Kotlarz
 *
 */
public class UnsupportedLayerDimensionException extends Exception {

	private static final long serialVersionUID = -303685890008368139L;

}
