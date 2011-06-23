package org.eisti.game.othello;

import org.junit.Test;

import java.awt.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author MACHIZAUD Andréa
 * @version 6/21/11
 */
public class BoardTest
    implements Othello {

    @Test
    public void wellInitialized() {
        Board othelloBoard = new Board();
        Dimension othelloBoardDimension = new Dimension(8, 8);

        assertEquals("Wrong board dimension",
                othelloBoard.getDimension(),
                othelloBoardDimension);

        assertTrue("Badly initialized : no white pawn at (4,4)",
                WHITE_PAWN_ID == othelloBoard.getCase(3, 3).getPawnID());
        assertTrue("Badly initialized : no white pawn at (5,5)",
                WHITE_PAWN_ID == othelloBoard.getCase(4, 4).getPawnID());
        assertTrue("Badly initialized : no black pawn at (4,4)",
                BLACK_PAWN_ID == othelloBoard.getCase(3, 4).getPawnID());
        assertTrue("Badly initialized : no black pawn at (4,4)",
                BLACK_PAWN_ID == othelloBoard.getCase(4, 3).getPawnID());
    }
}
