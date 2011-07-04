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

import static org.eisti.game.othello.tasks.LineTraversor.GridTraversor.*;
import static org.eisti.labs.game.IBoard.ICase.NO_PAWN;
import static org.eisti.labs.game.Ply.Coordinate.Coordinate;
import static org.eisti.labs.util.Validation.require;

/**
 * @author MACHIZAUD Andréa
 * @version 6/20/11
 */
public class Rules
        extends AbstractRules<Board, OthelloContext>
        implements OthelloProperties {

    //Thread pool as many as possible direction checking
    public static final ExecutorService LINE_CHECKER =
            Executors.newFixedThreadPool(values().length);

    @Override
    public int getNumberOfPlayer() {
        return NUMBERS_OF_PLAYERS;
    }

    @Override
    public int getNumberOfTypedPawns() {
        return NUMBERS_OF_TYPED_PAWN;
    }

    @Override
    public Set<Ply> getLegalMoves(OthelloContext context) {
        return legalMoves(context);
    }

    @Override
    public OthelloContext doPly(OthelloContext previousContext, Ply ply) {
        //same game if a player pass
        if (ply.isPass())
            return previousContext.branchOff(previousContext.getBoard());

        Board oldBoard = previousContext.getBoard();
        IPlayer activePlayer = previousContext.getActivePlayer().getFirst();

        require(oldBoard.getPawnID(ply.getDestination()) == NO_PAWN,
                "Already a pawn at given ply position : " + ply);

        Board subGame = oldBoard.clone();
        Ply.Coordinate newPawnPosition = ply.getDestination();
        int playerPawn = getPawnID(activePlayer);

        //put the pawn at correct place
        subGame.getCase(
                newPawnPosition.getRow(),
                newPawnPosition.getColumn())
                .setPawnID(playerPawn);

        ReversePawn[] taskList =
                new ReversePawn[values().length];

        if (newPawnPosition.getRow() > 0)
            taskList[NORTH.ordinal()] = new ReversePawn(
                    subGame,
                    NORTH,
                    Coordinate(
                            (char) (newPawnPosition.getColumn() + 'A'),
                            (char) (newPawnPosition.getRow() - 1 + '1')
                    ),
                    activePlayer
            );
        if (newPawnPosition.getRow() > 0
                && newPawnPosition.getColumn() < OTHELLO_DIMENSION.width - 1)
            taskList[NORTH_EAST.ordinal()] = new ReversePawn(
                    subGame,
                    NORTH_EAST,
                    Coordinate(
                            (char) (newPawnPosition.getColumn() + 1 + 'A'),
                            (char) (newPawnPosition.getRow() - 1 + '1')
                    ),
                    activePlayer
            );
        if (newPawnPosition.getColumn() < OTHELLO_DIMENSION.width - 1)
            taskList[EAST.ordinal()] = new ReversePawn(
                    subGame,
                    EAST,
                    Coordinate(
                            (char) (newPawnPosition.getColumn() + 1 + 'A'),
                            (char) (newPawnPosition.getRow() + '1')
                    ),
                    activePlayer
            );
        if (newPawnPosition.getRow() < OTHELLO_DIMENSION.height - 1
                && newPawnPosition.getColumn() < OTHELLO_DIMENSION.width - 1)
            taskList[SOUTH_EAST.ordinal()] = new ReversePawn(
                    subGame,
                    SOUTH_EAST,
                    Coordinate(
                            (char) (newPawnPosition.getColumn() + 1 + 'A'),
                            (char) (newPawnPosition.getRow() + 1 + '1')
                    ),
                    activePlayer
            );
        if (newPawnPosition.getRow() < OTHELLO_DIMENSION.height - 1)
            taskList[SOUTH.ordinal()] = new ReversePawn(
                    subGame,
                    SOUTH,
                    Coordinate(
                            (char) (newPawnPosition.getColumn() + 'A'),
                            (char) (newPawnPosition.getRow() + 1 + '1')
                    ),
                    activePlayer
            );
        if (newPawnPosition.getRow() < OTHELLO_DIMENSION.height - 1
                && newPawnPosition.getColumn() > 0)
            taskList[SOUTH_WEST.ordinal()] = new ReversePawn(
                    subGame,
                    SOUTH_WEST,
                    Coordinate(
                            (char) (newPawnPosition.getColumn() - 1 + 'A'),
                            (char) (newPawnPosition.getRow() + 1 + '1')
                    ),
                    activePlayer
            );
        if (newPawnPosition.getColumn() > 0)
            taskList[WEST.ordinal()] = new ReversePawn(
                    subGame,
                    WEST,
                    Coordinate(
                            (char) (newPawnPosition.getColumn() - 1 + 'A'),
                            (char) (newPawnPosition.getRow() + '1')
                    ),
                    activePlayer
            );
        if (newPawnPosition.getRow() > 0
                && newPawnPosition.getColumn() > 0)
            taskList[NORTH_WEST.ordinal()] = new ReversePawn(
                    subGame,
                    NORTH_WEST,
                    Coordinate(
                            (char) (newPawnPosition.getColumn() - 1 + 'A'),
                            (char) (newPawnPosition.getRow() - 1 + '1')
                    ),
                    activePlayer
            );

        try {
            for (ReversePawn task : taskList)
                if (task != null)
                    LINE_CHECKER.submit(task);
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

        for (IBoard.ICase area : currentBoard)
            if (area.getPawnID() == playerPawn) {

                Ply.Coordinate start = area.getPosition();

                //check in every direction where we can put our pawn
                if (start.getRow() > 0)
                    taskList.add(new LegalMoveRegistration(
                            currentBoard,
                            NORTH,
                            Coordinate(
                                    (char) (start.getColumn() + 'A'),
                                    (char) (start.getRow() - 1 + '1')
                            ),
                            activePlayer
                    ));
                if (start.getRow() > 0
                        && start.getColumn() < OTHELLO_DIMENSION.width - 1)
                    taskList.add(new LegalMoveRegistration(
                            currentBoard,
                            NORTH_EAST,
                            Coordinate(
                                    (char) (start.getColumn() + 1 + 'A'),
                                    (char) (start.getRow() - 1 + '1')
                            ),
                            activePlayer
                    ));
                if (start.getColumn() < OTHELLO_DIMENSION.width - 1)
                    taskList.add(new LegalMoveRegistration(
                            currentBoard,
                            EAST,
                            Coordinate(
                                    (char) (start.getColumn() + 1 + 'A'),
                                    (char) (start.getRow() + '1')
                            ),
                            activePlayer
                    ));
                if (start.getRow() < OTHELLO_DIMENSION.height - 1
                        && start.getColumn() < OTHELLO_DIMENSION.width - 1)
                    taskList.add(new LegalMoveRegistration(
                            currentBoard,
                            SOUTH_EAST,
                            Coordinate(
                                    (char) (start.getColumn() + 1 + 'A'),
                                    (char) (start.getRow() + 1 + '1')
                            ),
                            activePlayer
                    ));
                if (start.getRow() < OTHELLO_DIMENSION.height - 1)
                    taskList.add(new LegalMoveRegistration(
                            currentBoard,
                            SOUTH,
                            Coordinate(
                                    (char) (start.getColumn() + 'A'),
                                    (char) (start.getRow() + 1 + '1')
                            ),
                            activePlayer
                    ));
                if (start.getRow() < OTHELLO_DIMENSION.height - 1
                        && start.getColumn() > 0)
                    taskList.add(new LegalMoveRegistration(
                            currentBoard,
                            SOUTH_WEST,
                            Coordinate(
                                    (char) (start.getColumn() - 1 + 'A'),
                                    (char) (start.getRow() + 1 + '1')
                            ),
                            activePlayer
                    ));
                if (start.getColumn() > 0)
                    taskList.add(new LegalMoveRegistration(
                            currentBoard,
                            WEST,
                            Coordinate(
                                    (char) (start.getColumn() - 1 + 'A'),
                                    (char) (start.getRow() + '1')
                            ),
                            activePlayer
                    ));
                if (start.getRow() > 0
                        && start.getColumn() > 0)
                    taskList.add(new LegalMoveRegistration(
                            currentBoard,
                            NORTH_WEST,
                            Coordinate(
                                    (char) (start.getColumn() - 1 + 'A'),
                                    (char) (start.getRow() - 1 + '1')
                            ),
                            activePlayer
                    ));

                try {
                    for (Future<Ply> computation : LINE_CHECKER.invokeAll(taskList)) {
                        Ply result = computation.get();
                        if (result != null)
                            legalPlys.add(result);
                    }
                } catch (InterruptedException e) {
                    //Computation can be interrupted by bot worker
//                    throw new Error("Unexpected error while computing legal moves", e);
                } catch (ExecutionException e) {
                    throw new Error("Unexpected error while computing legal moves", e);
                }

            }

        return legalPlys;
    }
}
