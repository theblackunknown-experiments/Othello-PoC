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

import java.util.concurrent.Callable;

import static org.eisti.labs.game.IBoard.NO_PAWN;

/**
 * @author MACHIZAUD Andréa
 * @version 7/2/11
 */
public final class LegalMoveRegistration
        extends LineTraversor
        implements Callable<Ply> {

    /**
     * Current player pawn ID
     */
    private final int playerPawn;
    /**
     * Current rival pawn ID
     */
    private final int rivalPawn;

    public LegalMoveRegistration(
            Board board,
            GridTraversor direction,
            Ply.Coordinate start,
            IPlayer currentPlayer) {
        super(board, start, direction);
        this.playerPawn = Rules.getPawnID(currentPlayer);
        this.rivalPawn = playerPawn == OthelloProperties.BLACK_PAWN_ID
                ? OthelloProperties.WHITE_PAWN_ID
                : OthelloProperties.BLACK_PAWN_ID;
    }

    /**
     * Legal move finder core
     *
     * @return legal move if any or null
     */
    @Override
    public final Ply call() {
        boolean rivalPawnEncountered = false;
        LineIterator checker = getIterator(_direction);
        Ply.Coordinate cursor = null;
        try {
            for (cursor = checker.initialize(_start);
                 checker.verify(cursor, null);
                 cursor = checker.update(cursor)) {

                int pawnID = _board.getPawn(cursor);

                //stop on a empty case
                if (pawnID == NO_PAWN) {
                    //it's a legal move if we already encounter a rival pawn,
                    // thus we can reverse a line
                    if (rivalPawnEncountered)
                        return new Ply(cursor);
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
        } catch (ArrayIndexOutOfBoundsException ex) {
            throw new RuntimeException("Fail with given start : " + _start + ", and current : " + cursor, ex);
        }
        return null;
    }
}
