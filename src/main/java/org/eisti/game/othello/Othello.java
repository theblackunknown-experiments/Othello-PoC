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
