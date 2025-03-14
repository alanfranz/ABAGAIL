/**
 * @author kmanda1
 */
package eu.franzoni.abagail.opt.ga;

import java.util.ArrayList;
import java.util.List;

public class NQueensBoardGame {

	/**
	 * X---> increases left to right with zero based index Y increases top to
	 * bottom with zero based index | | V
	 */
	int[][] board;

	int size;

	public NQueensBoardGame(int n) {

		size = n;
		board = new int[size][size];
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				board[i][j] = 0;
			}
		}
	}

	public void addQueenAt(BoardLocation l) {

		if (!(queenExistsAt(l)))
			board[l.getXCoOrdinate()][l.getYCoOrdinate()] = 1;
	}

	public void removeQueenFrom(BoardLocation l) {

		if (board[l.getXCoOrdinate()][l.getYCoOrdinate()] == 1) {
			board[l.getXCoOrdinate()][l.getYCoOrdinate()] = 0;
		}
	}

	private boolean queenExistsAt(int x, int y) {
        if(x >= size && y >= size) return false;
		return (board[x][y] == 1);
	}

	public boolean queenExistsAt(BoardLocation l) {

		return (queenExistsAt(l.getXCoOrdinate(), l.getYCoOrdinate()));
	}

	public void moveQueen(BoardLocation from, BoardLocation to) {

		if ((queenExistsAt(from)) && (!(queenExistsAt(to)))) {
			removeQueenFrom(from);
			addQueenAt(to);
		}
	}

	public void clear() {

		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				board[i][j] = 0;
			}
		}
	}

	public void setBoard(List<BoardLocation> al) {

		clear();

		for (int i = 0; i < al.size(); i++) {
			addQueenAt(al.get(i));
		}
	}

	public int getNumberOfQueensOnBoard() {

		int count = 0;
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				if (board[i][j] == 1) {
					count++;
				}
			}
		}
		return count;
	}

	public List<BoardLocation> getQueenPositions() {

		ArrayList<BoardLocation> result = new ArrayList<BoardLocation>();
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				if (queenExistsAt(i, j)) {
					result.add(new BoardLocation(i, j));
				}
			}
		}
		return result;

	}

	private boolean isSquareHorizontallyAttacked(int x, int y) {

		return numberOfHorizontalAttacksOn(x, y) > 0;
	}

	private boolean isSquareVerticallyAttacked(int x, int y) {
		return numberOfVerticalAttacksOn(x, y) > 0;
	}

	private boolean isSquareDiagonallyAttacked(int x, int y) {
		return numberOfDiagonalAttacksOn(x, y) > 0;
	}

	public boolean isSquareUnderAttack(BoardLocation l) {

		int x = l.getXCoOrdinate();
		int y = l.getYCoOrdinate();
		return (isSquareHorizontallyAttacked(x, y)
				|| isSquareVerticallyAttacked(x, y) || isSquareDiagonallyAttacked(
					x, y));
	}

	public int getSize() {

		return size;
	}

	public void print() {

		System.out.println(getBoardPic());

	}

	public String getBoardPic() {

		StringBuffer buffer = new StringBuffer();
		for (int row = 0; (row < size); row++) { // row
			for (int col = 0; (col < size); col++) { // col
				if (queenExistsAt(col, row)) {
					buffer.append(" Q ");
				} else {
					buffer.append(" - ");
				}
			}
			buffer.append("\n");
		}
		return buffer.toString();
	}

	public int getNumberOfAttacksOn(BoardLocation l) {

		int x = l.getXCoOrdinate();
		int y = l.getYCoOrdinate();
		return numberOfHorizontalAttacksOn(x, y)
				+ numberOfVerticalAttacksOn(x, y)
				+ numberOfDiagonalAttacksOn(x, y);
	}

	private int numberOfHorizontalAttacksOn(int x, int y) {

		int retVal = 0;
		for (int i = 0; i < size; i++) {
			if ((queenExistsAt(i, y))) {
				if (i != x)
					retVal++;
			}
		}
		return retVal;
	}

	private int numberOfVerticalAttacksOn(int x, int y) {

		int retVal = 0;
		for (int j = 0; j < size; j++) {
			if ((queenExistsAt(x, j))) {
				if (j != y)
					retVal++;
			}
		}
		return retVal;
	}

	private int numberOfDiagonalAttacksOn(int x, int y) {

		int retVal = 0;

		int i;
		int j;
		// forward up diagonal
		for (i = (x + 1), j = (y - 1); (i < size && (j > -1)); i++, j--) {
			if (queenExistsAt(i, j)) {
				retVal++;
			}
		}
		// forward down diagonal
		for (i = (x + 1), j = (y + 1); ((i < size) && (j < size)); i++, j++) {
			if (queenExistsAt(i, j)) {
				retVal++;
			}
		}
		// backward up diagonal
		for (i = (x - 1), j = (y - 1); ((i > -1) && (j > -1)); i--, j--) {
			if (queenExistsAt(i, j)) {
				retVal++;
			}
		}

		// backward down diagonal
		for (i = (x - 1), j = (y + 1); ((i > -1) && (j < size)); i--, j++) {
			if (queenExistsAt(i, j)) {
				retVal++;
			}
		}

		return retVal;
	}

	@Override
	public int hashCode() {
		List<BoardLocation> locs = getQueenPositions();

		int result = 17;
		for (BoardLocation loc : locs) {
			result = 37 * loc.hashCode();
		}
		return result;
	}

	@Override
	public boolean equals(Object o) {

		if (this == o) {
			return true;
		}
		if ((o == null) || (this.getClass() != o.getClass())) {
			return false;
		}
		NQueensBoardGame aBoard = (NQueensBoardGame) o;
		boolean retVal = true;
		List<BoardLocation> locs = getQueenPositions();

		for (BoardLocation loc : locs) {
			if (!(aBoard.queenExistsAt(loc))) {
				retVal = false;
			}
		}
		return retVal;
	}

	@Override
	public String toString() {
		StringBuffer buf = new StringBuffer();
		for (int row = 0; row < size; row++) { // rows
			for (int col = 0; col < size; col++) { // columns
				if (queenExistsAt(col, row)) {
					buf.append('Q');
				} else {
					buf.append('-');
				}
			}
			buf.append("\n");
		}
		return buf.toString();
	}

}
