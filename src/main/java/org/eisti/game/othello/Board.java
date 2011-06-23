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
        extends AbstractBoard
        implements Othello {

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
        getCase(3, 3).setPawnID(WHITE_PAWN_ID);
        getCase(3, 4).setPawnID(BLACK_PAWN_ID);
        getCase(4, 3).setPawnID(BLACK_PAWN_ID);
        getCase(4, 4).setPawnID(WHITE_PAWN_ID);
    }

    @Override
    public Board clone() {
        return (Board) super.clone();
    }
}
