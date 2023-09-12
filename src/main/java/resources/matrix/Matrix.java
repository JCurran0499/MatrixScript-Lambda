/* John Curran
 * 
 * This class simulates a matrix of numbers. A matrix is simulated 
 * with a two-dimensional array of BigDecimal values. BigDecimal is
 * used in order to ensure precision when it comes to mathematical 
 * calculations. The methods below define the behavior of the matrix. 
 * In this case, each row in a matrix is an array of BigDecimal values 
 * in the two-dimensional array, so each value in the matrix can be
 * retrieved from the array using matrix[row][column]. All methods
 * that return objects will return null on an error, so it is the 
 * responsibility of the user to check for that if they think there
 * might be an error. Methods that do not return values, along with
 * constructors, will throw an exception upon an error.
 *
 * With proper credit, anybody is free to use and distribute this code for
 * their own purposes. */

package resources.matrix;

import resources.matrix.exceptions.MatrixDimensionsException;
import resources.matrix.exceptions.MatrixNullException;
import resources.matrix.exceptions.MatrixOutOfBoundsException;
import resources.matrix.exceptions.MatrixStringException;

import java.lang.reflect.Array;
import java.util.Optional;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Arrays;

public class Matrix {
	
	//represents the matrix, with each array within the array representing a row
	private final BigDecimal[][] matrix;
	
	/* the matrix class contains several constructors for different purposes */
	
	//creates a matrix with the given dimensions, starting with all values at 0
	public Matrix(int rows, int cols) {
		if (rows <= 0 || cols <= 0)
			throw new MatrixDimensionsException();
		
		matrix = new BigDecimal[rows][cols];
		for (int i = 0; i < rows; i++)
			for (int j = 0; j < cols; j++)
				matrix[i][j] = BigDecimal.valueOf(0);
	}

	public Matrix(int[][] m) {
		if (m == null)
			throw new MatrixNullException();
		if (m.length == 0)
			throw new MatrixDimensionsException();

		for (int[] row : m)
			if (row == null)
				throw new MatrixNullException();
			else if (row.length == 0 || row.length != m[0].length)
				throw new MatrixDimensionsException();

		matrix = new BigDecimal[m.length][m[0].length];
		for (int i = 0; i < m.length; i++)
			for (int j = 0; j < m[0].length; j++)
				matrix[i][j] = BigDecimal.valueOf(m[i][j]);
	}
	
	//creates a matrix from the corresponding two-dimensional array
	public Matrix(double[][] m) {
		if (m == null)
			throw new MatrixNullException();
		if (m.length == 0)
			throw new MatrixDimensionsException();

		for (double[] row : m)
			if (row == null)
				throw new MatrixNullException();
			else if (row.length == 0 || row.length != m[0].length)
				throw new MatrixDimensionsException();
		
		matrix = new BigDecimal[m.length][m[0].length];
		for (int i = 0; i < m.length; i++)
			for (int j = 0; j < m[0].length; j++)
				matrix[i][j] = BigDecimal.valueOf(m[i][j]);
	}

	public Matrix(BigDecimal[][] m) {
		if (m == null)
			throw new MatrixNullException();
		if (m.length == 0)
			throw new MatrixDimensionsException();

		for (BigDecimal[] row : m)
			if (row == null)
				throw new MatrixNullException();
			else if (row.length == 0 || row.length != m[0].length)
				throw new MatrixDimensionsException();

		matrix = new BigDecimal[m.length][m[0].length];
		for (int i = 0; i < m.length; i++) {
			for (int j = 0; j < m[0].length; j++) {
				if (m[i][j] == null)
					throw new MatrixNullException();

				matrix[i][j] = (m[i][j]);
			}
		}
	}
	
	//creates a deep copy of the given matrix
	public Matrix(Matrix m) {
		if (m == null)
			throw new MatrixNullException();

		matrix = new BigDecimal[m.matrix.length][];
		for (int r = 0; r < m.matrix.length; r++)
			matrix[r] = Arrays.copyOf(m.matrix[r], m.matrix[r].length);
	}
	
	/* this is the most complex and most useful Matrix constructor. This creates
	 * a matrix from the given String command. The String must be in the format
	 * "row1_val1 row1_val2 row1_val3 ; row2_val1 row2_val2 row2_val3", with
	 * each row separated by a semicolon and each value in each row separated
	 * by at least one space. There can be any number of rows, and each row
	 * may have any number of values as long as each row has the same number of
	 * values */
	
	public Matrix(String s) {
		if (s == null)
			throw new MatrixNullException();

		try {
			String[] rows = s.split(";", -1);
			matrix = new BigDecimal[rows.length][];

			if (rows.length == 0)
				throw new MatrixStringException();

			for (int i = 0; i < rows.length; i++) {
				String row = rows[i];
				String[] ints = row.strip().split("\\s+", -1);
				BigDecimal[] vals = new BigDecimal[ints.length];
				for (int j = 0; j < ints.length; j++)
					vals[j] = BigDecimal.valueOf(Double.parseDouble(ints[j]));

				matrix[i] = vals;
			}

		} catch (Exception e) { throw new MatrixStringException(); }

		for (BigDecimal[] row : matrix)
			if (row.length == 0 || row.length != matrix[0].length)
				throw new MatrixDimensionsException();
	}
	
	/* the following are the methods associated with Matrix class */
	
	
	/* prints the given matrix out in the following for:
	 * Matrix: "1 2 3 ; 4 5 6 ; 7 8 9"
	 * 
	 *    [ 1  2  3 ]
	 *    [ 4  5  6 ]
	 *    [ 7  8  9 ]
	 *    
	 * This method adjusts for the differing widths of the values, and guarantees
	 * that all rows are identical in visual length
	 */
	public String printString() {
		StringBuilder print = new StringBuilder();
		int[] widths = widths(this);
		
		for (int i = 0; i < rows(); i++) {
			print.append("[ ");
			for (int j = 0; j < cols() - 1; j++) {
				print.append(printVal(matrix[i][j].setScale(5,RoundingMode.HALF_UP), widths[j]));
				print.append("  ");
			}
			
			print.append(printVal(matrix[i][cols() - 1].setScale(5,RoundingMode.HALF_UP),
					widths[cols() - 1]));
			print.append(" ]\n");
		}
		print.setLength(print.length() - 1);
		
		return print.toString();
	}
	
	/* returns the matrix in the given String form:
	 * Matrix: 1 2 3 ; 4 5 6 ; 7 8 9 -> "[ 1 2 3 ; 4 5 6 ; 7 8 9 ]"
	 */
	public String toString() {
		StringBuilder m = new StringBuilder("[ ");
		double d;
		
		for (int i = 0; i < rows(); i++) {
			for (int j = 0; j < cols(); j++) {
				d = matrix[i][j].setScale(5,RoundingMode.HALF_UP).doubleValue();
				
				if (d % 1 == 0)
					m.append(((int) d)).append(" ");
				else
					m.append(d).append(" ");
			}
			
			if (i < rows() - 1)
				m.append("; ");
		}
		
		m.append("]");
		return m.toString();
	}
	
	
	//returns the identity matrix, with the given number of rows/columns
	public static Matrix Identity(int r) {
		if (r <= 0)
			return null;
		
		double[][] m = new double[r][r];
		for (int i = 0; i < r; i++)
			m[i][i] = 1;
		
		return new Matrix(m);
	}
	
	//returns the zero vector, using a matrix with one column, with the given
	//number of rows
	public static Matrix ZeroVector(int r) {
		if (r <= 0)
			return null;
		
		return new Matrix(r, 1);
	}


	//returns whether the calling matrix is identical to the argument matrix,
	//to at least the 5th decimal place
	public boolean equals(Matrix m, int scale) {
		if (m == null || m.rows() != rows() || m.cols() != cols())
			return false;

		for (int i = 0; i < rows(); i++)
			for (int j = 0; j < cols(); j++)
				if (matrix[i][j].setScale(scale, RoundingMode.HALF_UP).compareTo(
					m.matrix[i][j].setScale(scale, RoundingMode.HALF_UP)) != 0
				)
					return false;

		return true;
	}

	public boolean equals(Matrix m) {
		return equals(m, 20);
	}

	//returns the number of rows in the matrix
	public int rows() {
		return matrix.length;
	}
	
	//returns the number of columns in the matrix
	public int cols() {
		return matrix[0].length;
	}
	
	//returns the size of matrix (rows * columns)
	public int size() {
		return rows() * cols();
	}
	
	//returns whether the matrix is square
	public boolean isSquare() {
		return rows() == cols();
	}
	
	//returns whether the matrix is symmetrical
	public boolean isSymmetrical() {
		return equals(transpose());
	}
	
	//returns the value within the matrix in the given position (row r, column c).
	//Rows and columns begin at 0
	public double getValue(int r, int c) {
		if (r < 0 || c < 0 || r >= rows() || c >= cols())
			throw new MatrixOutOfBoundsException();
		
		return matrix[r][c].setScale(5,RoundingMode.HALF_UP).doubleValue();
	}

	//returns a single-row matrix that represents the given row in the matrix.
	//the returned row and original matrix are independent, and altering one
	//will not alter the other
	public Matrix getRow(int r) {
		if (r < 0 || r >= rows())
			return null;

		Matrix m = new Matrix(1, cols());
		m.matrix[0] = Arrays.copyOf(matrix[r], matrix[r].length);
		return m;
	}
	
	//returns a single-column matrix that represents the given column in the matrix.
	//the returned column and original matrix are independent, and altering one
	//will not alter the other
	public Matrix getColumn(int c) {
		return Optional.ofNullable(transpose().getRow(c))
			.map(Matrix::transpose)
			.orElse(null);
	}
	
	//alters the matrix by setting the given position to the given value 'd'
	public void setValue(int r, int c, double d) {
		if (r < 0 || c < 0 || r >= rows() || c >= cols())
			throw new MatrixOutOfBoundsException();
		
		matrix[r][c] = BigDecimal.valueOf(d);
	}

	//alters the matrix by setting the given row to the new matrix
	public void setRow(int r, Matrix m) {
		if (m == null)
			throw new MatrixNullException();
		if (r < 0 || r >= rows())
			throw new MatrixOutOfBoundsException();
		if (m.rows() != 1 || m.cols() != cols())
			throw new MatrixDimensionsException();

		matrix[r] = Arrays.copyOf(m.matrix[0], m.matrix[0].length);
	}
	
	//alters the matrix by setting the given column to the new matrix
	public void setColumn(int c, Matrix m) {
		if (m == null)
			throw new MatrixNullException();
		if (c < 0 || c >= cols())
			throw new MatrixOutOfBoundsException();
		if (m.cols() != 1 || m.rows() != rows())
			throw new MatrixDimensionsException();

		for (int i = 0; i < rows(); i++)
			matrix[i][c] = m.matrix[i][0];
	}
	
	//adds together the matrix with the argument matrix
	public Matrix add(Matrix m) {
		if (m == null || m.rows() != rows() || m.cols() != cols())
			return null;
		
		BigDecimal[][] m1 = new BigDecimal[rows()][cols()];
		
		for (int i = 0; i < rows(); i++)
			for (int j = 0; j < cols(); j++)
				m1[i][j] = matrix[i][j].add(m.matrix[i][j]);
		
		return new Matrix(m1);
	}
	
	//subtracts the argument matrix from the calling matrix
	public Matrix subtract(Matrix m) {
		if (m == null || m.rows() != rows() || m.cols() != cols())
			return null;
		
		BigDecimal[][] m1 = new BigDecimal[rows()][cols()];
		
		for (int i = 0; i < rows(); i++)
			for (int j = 0; j < cols(); j++)
				m1[i][j] = matrix[i][j].subtract(m.matrix[i][j]);
		
		return new Matrix(m1);
	}

	//multiplies the matrix by the given value
	public Matrix multiply(double c) {
		BigDecimal[][] m = new BigDecimal[rows()][cols()];

		for (int i = 0; i < rows(); i++)
			for (int j = 0; j < cols(); j++)
				m[i][j] = matrix[i][j].multiply(BigDecimal.valueOf(c), MathContext.DECIMAL128);

		return new Matrix(m);
	}

	//multiplies the calling matrix by the argument matrix
	public Matrix multiply(Matrix m) {
		if (m == null || m.rows() != cols())
			return null;

		BigDecimal[][] m1 = new BigDecimal[rows()][m.cols()];

		for (int i = 0; i < rows(); i++) {
			for (int j = 0; j < m.cols(); j++) {

				BigDecimal sum = BigDecimal.valueOf(0);
				for (int k = 0; k < m.rows(); k++)
					sum = sum.add(matrix[i][k].multiply(m.matrix[k][j], MathContext.DECIMAL128));

				m1[i][j] = sum;
			}
		}

		return new Matrix(m1);
	}
	
	//divides the matrix by the given value
	public Matrix divide(double c) {
		if (c == 0)
			return null;
		
		BigDecimal[][] m = new BigDecimal[rows()][cols()];
		
		for (int i = 0; i < rows(); i++)
			for (int j = 0; j < cols(); j++)
				m[i][j] = matrix[i][j].divide(BigDecimal.valueOf(c), MathContext.DECIMAL128);
		
		return new Matrix(m);
	}
	
	//brings the matrix to the given (non-negative) exponent
	public Matrix toPower(int power) {
		if (!isSquare() || power < 0)
			return null;
		
		Matrix newMatrix = Identity(rows());
		if (newMatrix == null)
			return null;

		for (int i = 0; i < power; i++)
			newMatrix = newMatrix.multiply(this);
		
		return newMatrix;
	}
	
	//returns the transpose of the matrix
	public Matrix transpose() {
		BigDecimal[][] m1 = new BigDecimal[cols()][rows()];
		for(int i = 0; i < rows(); i++)
			for (int j = 0; j < cols(); j++)
				m1[j][i] = matrix[i][j];
		
		return new Matrix(m1);
	}

	@SuppressWarnings("unchecked")
	public <T> T[][] mapToArray(T a, ConvertFunction<T> fun) {
		T[] a1 = (T[]) Array.newInstance(a.getClass(), 0);

		T[][] arr = (T[][]) Array.newInstance(a1.getClass(), rows());
		for (int i = 0; i < rows(); i++)
			arr[i] = (T[]) Array.newInstance(a.getClass(), cols());

		for (int i = 0; i < rows(); i++)
			for (int j = 0; j < cols(); j++)
				arr[i][j] = fun.call(matrix[i][j].doubleValue());

		return arr;
	}
	
	//appends the columns of the argument matrix onto the right end of the calling matrix
	public Matrix augment(Matrix m) {
		if (m == null || m.rows() != rows())
			return null;
		
		BigDecimal[][] m1 = new BigDecimal[rows()][m.cols() + cols()];
		
		for (int i = 0; i < rows(); i++) {
			if (cols() >= 0) System.arraycopy(matrix[i], 0, m1[i], 0, cols());

			for (int j = 0; j < m.cols(); j++)
				m1[i][cols() + j] = m.matrix[i][j];
		}
		
		return new Matrix(m1);
	}

	//appends the columns of the argument matrix onto the bottom of the calling matrix
	public Matrix append(Matrix m) {
		if (m == null || m.cols() != cols())
			return null;

		BigDecimal[][] m1 = new BigDecimal[m.rows() + rows()][cols()];

		for (int i = 0; i < rows(); i++)
			m1[i] = Arrays.copyOf(matrix[i], matrix[i].length);
		for (int i = 0; i < m.rows(); i++)
			m1[rows() + i] = Arrays.copyOf(m.matrix[i], m.matrix[i].length);

		return new Matrix(m1);
	}
	
	//returns the matrix in reduced row echelon form
	public Matrix rref() {
		Matrix m = transpose(); //simpler to work with the transpose
		BigDecimal scale;
		int indexRow = 0, indexCol = 0;
		
		int rows = m.rows(), cols = m.cols();
		while (indexCol < cols && indexRow < rows) {
			if (!zeroFromPoint(m.getRow(indexRow), indexCol, 1)) {
				if (m.getValue(indexRow, indexCol) == 0) {
					int j = indexCol + 1;
					Matrix v;
					
					while (m.getValue(indexRow, j) == 0)
						j++;
					v = m.getColumn(j); //row swap
					m.setColumn(j, m.getColumn(indexCol));
					m.setColumn(indexCol, v);
				}
				
				for (int i = 0; i < cols; i++) //row reduce
					if (i != indexCol && m.getValue(indexRow, i) != 0) {
						scale = (m.matrix[indexRow][i]).divide(m.matrix[indexRow][indexCol], 
								MathContext.DECIMAL128).negate();
						m.setColumn(i, m.getColumn(indexCol).multiply(scale.doubleValue()).add(m.getColumn(i)));
					}
				
				if (m.getValue(indexRow, indexCol) != 1) { //scale the row
					scale = (BigDecimal.valueOf(1)).divide(m.matrix[indexRow][indexCol], MathContext.DECIMAL128);
					m.setColumn(indexCol, m.getColumn(indexCol).multiply(scale.doubleValue()));
				}
				
				indexCol++;
			}
			
			indexRow++;
		}
		
		return m.transpose();
	}
	
	//returns the determinant of the matrix
	public double determinant() {
		if (!isSquare())
			return 0;
		
		BigDecimal determinant = BigDecimal.valueOf(1);
		
		Matrix m = transpose(); //it is simpler to work with columns rather than rows
		BigDecimal scale; 
		int indexRow = 0, indexCol = 0;
		
		//carry out the actions of reducing the matrix into rref form,
		//all while keeping track of the changing determinant
		int rows = m.rows(), columns = m.cols();
		while (indexCol < columns && indexRow < rows) {
			if (!zeroFromPoint(m.getRow(indexRow), indexCol, 1)) {
				if (m.getValue(indexRow, indexCol) == 0) {
					int j  = indexCol + 1;
					Matrix v;
				
					while (m.getValue(indexRow, j) == 0)
						j++;
					v = m.getColumn(j); //row swap
					m.setColumn(j, m.getColumn(0));
					m.setColumn(0, v);
					determinant = determinant.negate();
				}
				
				for (int i = 0; i < columns; i++)  //row reduce
					if (i != indexCol && m.getValue(indexRow, i) != 0) {
						scale = (m.matrix[indexRow][i]).divide(m.matrix[indexRow][indexCol],
								MathContext.DECIMAL128).negate();
						m.setColumn(i, m.getColumn(indexCol).multiply(scale.doubleValue()).add(m.getColumn(i)));
					}
				
				if (m.getValue(indexRow, indexCol) != 1) { //scale the row
					scale = (BigDecimal.valueOf(1)).divide(m.matrix[indexRow][indexCol], MathContext.DECIMAL128);
					m.setColumn(indexCol, m.getColumn(indexCol).multiply(scale.doubleValue()));					
					determinant = determinant.divide(scale, MathContext.DECIMAL128);
				}
				
					indexCol++;
			}
			
			indexRow++;
		}

		if (!m.equals(Identity(rows)))
			return 0;
		
		return determinant.doubleValue();
	}
	
	//returns whether the matrix is invertible
	public boolean invertible() {
		return determinant() != 0;
	}
	
	//returns the inverse of the matrix
	public Matrix inverse() {
		if (!invertible())
			return null;
		
		Matrix m = augment(Identity(cols())).rref();
		
		Matrix newMatrix = m.getColumn(cols());
		for (int i = cols() + 1; i < m.cols(); i++)
			newMatrix = newMatrix.augment(m.getColumn(i));				
		
		return newMatrix;
	}
	
	//returns the rank of the matrix
	public int rank() {
		Matrix r = rref();
		int rows = rows(), cols = cols(), indexRow = 0, indexCol = 0, rank = 0;
		
		while (indexRow < rows && indexCol < cols) {
			if (r.getValue(indexRow, indexCol) == 1) {
				rank++;
				indexRow++;
			}
			
			indexCol++;
		}
		
		return rank;
	}
	
	
	/* the following are several private helper methods used only in this class */
	
	//used in the rref() method, this method returns whether a given one-column
	//matrix has only zero values starting from the given point and moving
	//up or down depending on the given direction
	private boolean zeroFromPoint(Matrix m, int point, int direction) {
		if (direction > 0) {
			for (int i = point; i < m.cols(); i++) 
				if (m.getValue(0, i) != 0)
					return false;			
		}
		else if (direction < 0) {
			for (int i = point - 1; i > 0; i--)
				if (m.getValue(0, i - 1) != 0)
					return false;
		}
		
		return true;
	}
	
	//prints a value, dropping the decimal end if the value is an integer
	private static String printVal(BigDecimal v, int w) {
		StringBuilder val = new StringBuilder();
		double d = v.doubleValue();
		int l;
		
		if (d % 1 == 0) {
			l = Integer.toString((int) d).length();
			val.append(" ".repeat(Math.max(0, w - l)));
			val.append((int) d);
		}
		else {
			l = Double.toString(d).length();
			val.append(" ".repeat(Math.max(0, w - l)));
			val.append(d);
		}

		return val.toString();
	}
	
	//returns the width of each column in the matrix by calculating the maximum 
	//length of each value in String form 
	private static int[] widths(Matrix m) {
		int[] ws = new int[m.cols()];
		
		for (int i = 0; i < m.cols(); i++) {
			int w = 0;
			for (int j = 0; j < m.rows(); j++) {
				
				double d = m.matrix[j][i].setScale(5,RoundingMode.HALF_UP).doubleValue();
				String s;
				if (d % 1 == 0)
					s = (Integer.valueOf((int) d)).toString();
				else
					s = (Double.valueOf(d)).toString();
				
				if (s.length() > w)
					w = s.length();
			}
			ws[i] = w;
		}
		
		return ws;
	}

	public interface ConvertFunction<T> {
		T call(double d);
	}
}