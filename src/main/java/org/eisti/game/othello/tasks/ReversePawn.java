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
import org.eisti.game.othello.OthelloProperties;
import org.eisti.game.othello.Rules;
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
    private final int playerPawn;

    public ReversePawn(
            Board board,
            GridTraversor direction,
            Ply.Coordinate start,
            IPlayer currentPlayer) {
        super(board, start, direction);

        this.playerPawn = Rules.getPawnID(currentPlayer);
    }

    /**
     * Reverse pawn core
     */
    public void run() {
        LineIterator checker = getIterator(_direction);
        //look if we can reverse
        for (Ply.Coordinate cursor = checker.initialize(_start);
             checker.verify(cursor, null);
             cursor = checker.update(cursor)) {

            //it's ok to reverse
            if (_board.isAt(cursor, playerPawn)) {

                //reversion
                for (Ply.Coordinate reverse = _start;
                     checker.verify(reverse, cursor);
                     reverse = checker.update(reverse)) {
                    _board.setPawn(reverse, playerPawn);
                }
                //reverse completed in this direction
                break;

            }

        }

    }
}
