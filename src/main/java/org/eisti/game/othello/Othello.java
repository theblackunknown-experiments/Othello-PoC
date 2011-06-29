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

import java.awt.*;

/**
 * @author MACHIZAUD Andréa
 * @version 23/06/11
 */
public interface Othello {

    public static final Dimension OTHELLO_DIMENSION = new Dimension(8, 8);

    public static final Integer NUMBERS_OF_PLAYERS = 2;

    public static final int BLACK = 0x0;
    public static final int WHITE = 0x1;

    public static final Integer NUMBERS_OF_TYPED_PAWN = 1;

    //public static final int NO_PAWN = 0x0;
    public static final int PAWN = 0x1;

    public static final int BLACK_PAWN_ID =  // should be equals to '10' binary
            (PAWN << NUMBERS_OF_TYPED_PAWN ) | BLACK;
    public static final int WHITE_PAWN_ID =  // should be equals to '11' binary
            (PAWN << NUMBERS_OF_TYPED_PAWN ) | WHITE;

}
