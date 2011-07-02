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
