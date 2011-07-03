/*
 * #%L
 * Othello Game Project
 * %%
 * Copyright (C) 2011 MACHIZAUD Andréa
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */
package org.eisti.game.othello;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author MACHIZAUD Andréa
 * @version 6/21/11
 */
public class BoardTest
    implements OthelloProperties {

    Board othelloBoard;

    @Before
    public void generateNewBoard() {
        othelloBoard = new Board();
    }

    @Test
    public void wellInitialized() {
        assertEquals("Wrong board dimension",
                othelloBoard.getDimension(),
                OTHELLO_DIMENSION);

        assertTrue("Badly initialized : no white pawn at (4,4)",
                WHITE_PAWN_ID == othelloBoard.getCase(3, 3).getPawnID());
        assertTrue("Badly initialized : no white pawn at (5,5)",
                WHITE_PAWN_ID == othelloBoard.getCase(4, 4).getPawnID());
        assertTrue("Badly initialized : no black pawn at (4,4)",
                BLACK_PAWN_ID == othelloBoard.getCase(3, 4).getPawnID());
        assertTrue("Badly initialized : no black pawn at (4,4)",
                BLACK_PAWN_ID == othelloBoard.getCase(4, 3).getPawnID());
    }

    @Ignore(value = "only purpose is to see the board display")
    public void stringRepresentation() {
        System.out.println(othelloBoard.toString());
    }
}
