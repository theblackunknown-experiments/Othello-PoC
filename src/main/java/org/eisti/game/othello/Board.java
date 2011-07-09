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

import org.eisti.labs.game.AbstractBoard;

/**
 * <p>
 * 1 2 3 4 5 6 7 8
 * _________________
 * 1 | | | | | | | | |
 * _________________
 * 2 | | | | | | | | |
 * _________________
 * 3 | | | | | | | | |
 * _________________
 * 4 | | | |O|X| | | |
 * _________________
 * 5 | | | |X|O| | | |
 * _________________
 * 6 | | | | | | | | |
 * _________________
 * 7 | | | | | | | | |
 * _________________
 * 8 | | | | | | | | |
 * _________________
 * </p>
 * An othello board is a 8x8 grid,
 * initialized with a 2x2 cross with white and black pawn
 *
 * @author MACHIZAUD Andréa
 * @version 6/19/11
 */
public class Board
        extends AbstractBoard<Board>
        implements OthelloProperties {

    public Board() {
        super(
                OTHELLO_DIMENSION.width,
                OTHELLO_DIMENSION.height);
    }

    /**
     * Othello start a cross at the center of the board
     */
    @Override
    protected void initializeBoard() {
        setPawn('D','4',WHITE_PAWN_ID);
        setPawn('D','5',BLACK_PAWN_ID);
        setPawn('E','4',BLACK_PAWN_ID);
        setPawn('E','5',WHITE_PAWN_ID);
    }
}
