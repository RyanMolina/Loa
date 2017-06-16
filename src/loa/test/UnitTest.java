package loa.test;

import loa.core.Board;
import loa.core.Move;
import loa.core.Piece;
import org.junit.Test;

import static loa.core.Piece.BP;
import static loa.core.Piece.EMP;
import static loa.core.Piece.WP;
import static org.junit.Assert.assertEquals;

/**
 * Created by ryang on 14/06/2017.
 */
public class UnitTest {
	@Test
	public void testIsLegal() {
		Board board = new Board();
		//move B8 - H8
		Move jumper = Move.create(1, 0, 7, 0, board);
		assertEquals(true, board.isLegal(jumper));

		//D8 - B8
		Move tooShort = Move.create(3, 0, 1, 0, board);
		assertEquals(false, board.isLegal(tooShort));

		//A6 - A-5
		Move blocked = Move.create(0, 2, 0, 3, board);
		assertEquals(false, board.isLegal(blocked));

		//D1 - D4
		Move tooLong = Move.create(3, 7, 3, 4, board);
		assertEquals(false, board.isLegal(tooLong));

		//D1 - D3
		Move legal = Move.create(3, 7, 3, 5, board);
		assertEquals(true, board.isLegal(legal));

		//E1 - G3
		Move diagonalLegal = Move.create(4, 7, 6, 5, board);
		assertEquals(true, board.isLegal(diagonalLegal));

		//F1 -H3
		Move replace = Move.create(5, 7, 7, 5, board);
		assertEquals(true, board.isLegal(replace));
	}

	@Test
	public void testPiecesContiguous() {

		System.out.println("piecesContiguous");

		Piece[][] c = {

				{EMP, EMP, EMP, EMP, EMP, EMP, EMP, EMP},
				{EMP, EMP, EMP, EMP, EMP, BP, BP, EMP},
				{EMP, EMP, EMP, EMP, WP, BP, EMP, BP},
				{EMP, EMP, EMP, WP, BP, EMP, BP, EMP},
				{EMP, EMP, EMP, BP, WP, WP, BP, BP},
				{EMP, EMP, EMP, WP, EMP, BP, EMP, EMP},
				{EMP, EMP, EMP, EMP, BP, EMP, EMP, EMP},
				{EMP, EMP, EMP, EMP, EMP, EMP, EMP, EMP}
		};

		Piece piece = Piece.BP;
		Board instance = new Board(c, piece);
		boolean result = instance.piecesContiguous(piece);
		assertEquals(true, result);
	}
}


