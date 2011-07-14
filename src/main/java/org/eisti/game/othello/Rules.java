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

import org.eisti.game.othello.tasks.LegalMoveRegistration;
import org.eisti.game.othello.tasks.LineTraversor;
import org.eisti.game.othello.tasks.ReversePawn;
import org.eisti.labs.game.AbstractRules;
import org.eisti.labs.game.IBoard;
import org.eisti.labs.game.IPlayer;
import org.eisti.labs.game.Ply;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.*;

import static org.eisti.game.othello.tasks.LineTraversor.GridTraversor.values;
import static org.eisti.labs.util.Validation.require;

/**
 * @author MACHIZAUD Andréa
 * @version 6/20/11
 */
public final class Rules
        extends AbstractRules<Board, OthelloContext>
        implements OthelloProperties {

    //Thread pool as many as possible direction checking
    public static final ExecutorService LINE_CHECKER =
            Executors.newFixedThreadPool(values().length);

    @Override
    public final int getNumberOfPlayer() {
        return NUMBERS_OF_PLAYERS;
    }

    @Override
    public final int getNumberOfTypedPawns() {
        return NUMBERS_OF_TYPED_PAWN;
    }

    @Override
    public final Set<Ply> getLegalMoves(OthelloContext context) {
        return legalMoves(context);
    }

    @Override
    public final OthelloContext doPly(OthelloContext previousContext, Ply ply) {
        //same game if a player pass
        if (ply.isPass())
            return previousContext.branchOff(previousContext.getBoard());

        Board oldBoard = previousContext.getBoard();
        IPlayer activePlayer = previousContext.getActivePlayer().getFirst();

        require(oldBoard.isAt(ply.getDestination(), IBoard.NO_PAWN),
                "Already a pawn at given ply position : " + ply);

        Board subGame = oldBoard.clone();
        Ply.Coordinate newPawnPosition = ply.getDestination();
        int playerPawn = getPawnID(activePlayer);

        //put the pawn at correct place
        subGame.setPawn(newPawnPosition, playerPawn);

        try {
            //reverse pawn line in parallel
            for (LineTraversor.GridTraversor direction : LineTraversor.GridTraversor.values())
                LINE_CHECKER.submit(new ReversePawn(
                        subGame,
                        direction,
                        newPawnPosition,
                        activePlayer
                ));
            //wait for computations to end
            LINE_CHECKER.awaitTermination(0, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new Error("Unexpected error while computing reverse pawns", e);
        }

        return previousContext.branchOff(subGame);
    }


    /*=========================================================================
                       OTHELLO PART
    =========================================================================*/

    // specific to othello, because there is only one pawn's type

    public static int getPawnID(IPlayer player) {
        return player.getIdentifier() == OthelloProperties.WHITE
                ? WHITE_PAWN_ID
                : BLACK_PAWN_ID;
    }

    public static Set<Ply> legalMoves(OthelloContext context) {
        IPlayer activePlayer = context.getActivePlayer().getFirst();
        Board currentBoard = context.getBoard();
        Set<Ply> legalPlys = new HashSet<Ply>();

        int playerPawn = getPawnID(activePlayer);

        //task container for parallel ops
        Collection<LegalMoveRegistration> taskList =
                new ArrayList<LegalMoveRegistration>(values().length);

        for (Ply.Coordinate location : currentBoard)
            if (currentBoard.isAt(location, playerPawn)) {

                try {
                    //lookup in every direction, in parallel
                    for (LineTraversor.GridTraversor direction : LineTraversor.GridTraversor.values())
                        taskList.add(new LegalMoveRegistration(
                                currentBoard,
                                direction,
                                location,
                                activePlayer
                        ));
                    //fetch computation result and aggregated them in the set
                    for (Future<Ply> computation : LINE_CHECKER.invokeAll(taskList)) {
                        Ply result = computation.get();
                        if (result != null)
                            legalPlys.add(result);
                    }
                } catch (InterruptedException e) {
                    //Computation can be interrupted by bot worker
                } catch (ExecutionException e) {
                    throw new Error("Unexpected error while computing legal moves", e);
                }

            }

        return legalPlys;
    }
}
