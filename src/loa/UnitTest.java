package loa;

import loa.core.Board;
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
