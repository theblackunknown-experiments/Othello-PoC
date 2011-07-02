package org.eisti.game.othello.tasks;

import org.eisti.game.othello.Board;
import org.eisti.game.othello.Othello;
import org.eisti.game.othello.Referee;
import org.eisti.labs.game.IBoard;
import org.eisti.labs.game.IPlayer;
import org.eisti.labs.game.Ply;

/**
 * Rival's pawn reverse task
 *
 * @author MACHIZAUD Andréa
 * @version 7/2/11
 */
public class ReversePawn
        extends LineTraversor
        implements Runnable {

    /**
     * Current player pawn ID
     */
    private int playerPawn;
    /**
     * Current rival pawn ID
     */
    private int rivalPawn;

    public ReversePawn(
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
     * Reverse pawn core
     */
    public void run() {
        LineIterator checker = getIterator(_direction);
        //look if we can reverse
        for (int rowCursor = _start.getRow(),
                     columnCursor = _start.getColumn();
             checker.verify(rowCursor, columnCursor, null);
             rowCursor = checker.updateRow(rowCursor),
                     columnCursor = checker.updateColumn(columnCursor)) {

            int pawnID = _board.getCase(rowCursor, columnCursor).getPawnID();

            //it's ok to reverse
            if (pawnID == playerPawn) {

                Ply.Coordinate target = Ply.Coordinate.Coordinate(
                        (char)(columnCursor + 'A'),
                        (char)(rowCursor + '1')
                );

                //reversion
                for (int rowReverse = _start.getRow(),
                         columnReverse = _start.getColumn();
                     checker.verify(rowReverse, columnReverse, target);
                     rowReverse = checker.updateRow(rowReverse),
                             columnReverse = checker.updateColumn(columnReverse)) {

                    IBoard.ICase area = _board.getCase(rowReverse, columnReverse);
                    if (area.getPawnID() == rivalPawn)
                        area.setPawnID(playerPawn);

                }

            }

        }

    }
}
