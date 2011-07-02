package org.eisti.game.othello.tasks;

import org.eisti.game.othello.Board;
import org.eisti.game.othello.Othello;
import org.eisti.game.othello.Referee;
import org.eisti.labs.game.IBoard;
import org.eisti.labs.game.IPlayer;
import org.eisti.labs.game.Ply;

import java.util.concurrent.Callable;

import static org.eisti.labs.game.IBoard.ICase.NO_PAWN;
import static org.eisti.labs.game.Ply.Coordinate.Coordinate;

/**
 * @author MACHIZAUD Andréa
 * @version 7/2/11
 */
public class LegalMoveRegistration
        extends LineTraversor
        implements Callable<Ply> {

    /**
     * Current player pawn ID
     */
    private int playerPawn;
    /**
     * Current rival pawn ID
     */
    private int rivalPawn;

    public LegalMoveRegistration(
            Board board,
            GridTraversor direction,
            Ply.Coordinate start,
            IPlayer currentPlayer) {
        super(board, start, direction);
        this.playerPawn = Referee.getPawnID(currentPlayer);
        this.rivalPawn = playerPawn == Othello.BLACK_PAWN_ID
                ? Othello.WHITE_PAWN_ID
                : Othello.BLACK_PAWN_ID;
    }

    /**
     * Legal move finder core
     * @return
     *      legal move if any or null
     */
    @Override
    public Ply call() {
        boolean rivalPawnEncountered = false;
        LineIterator checker = getIterator(_direction);
        for (int rowCursor = _start.getRow(),
                     columnCursor = _start.getColumn();
             checker.verify(rowCursor, columnCursor, null);
             rowCursor = checker.updateRow(rowCursor),
                     columnCursor = checker.updateColumn(columnCursor)) {

            int pawnID = _board.getCase(rowCursor, columnCursor).getPawnID();

            //stop on a empty case
            if (pawnID == NO_PAWN) {
                //it's a legal move if we already encounter a rival pawn,
                // thus we can reverse a line
                if (rivalPawnEncountered)
                    return new Ply(Coordinate(
                            (char) (columnCursor + 'A'),
                            (char) (rowCursor + '1')
                    ));
                else
                    break;
            } else if (pawnID == playerPawn) // own pawn encountered, no line reverse possible
            {
                break;
            } else if (pawnID == rivalPawn) // rival pawn encountered, try to find where we can put a pawn
            {
                rivalPawnEncountered = true;
            }

        }
        return null;
    }
}
