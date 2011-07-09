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

import org.eisti.game.othello.tasks.PlayerHasRemainingPlies;
import org.eisti.labs.game.*;
import org.eisti.labs.util.Tuple;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author MACHIZAUD Andréa
 * @version 23/06/11
 */
public class OthelloContext
        extends GameContext<Board, OthelloContext>
        implements OthelloProperties {

    private static Board[] castArray(IBoard[] generalArray) {
        Board[] castedArray = new Board[generalArray.length];
        for (int i = generalArray.length; i-- > 0; )
            castedArray[i] = (Board) generalArray[i];
        return castedArray;
    }

    public OthelloContext(
            Clock elapsedTime,
            IBoard[] history,
            IPlayer[] playersInGame,
            Clock[] playersRemainingTime) {
        super(elapsedTime, castArray(history), playersInGame, playersRemainingTime);
    }

    private OthelloContext() {
        super();
    }

    //TODO Test Case
    @Override
    public GameState getState() {
        int[] pawnCounter = new int[NUMBERS_OF_PLAYERS];
        pawnCounter[BLACK] = pawnCounter[WHITE] = 0;
        Board currentBoard = getBoard();
        Tuple<IPlayer, Clock>[] players = getPlayers();

        ExecutorService remainingMoveChecker = null;
        try {
            remainingMoveChecker = Executors.newFixedThreadPool(2);
            //current context, player one is current player
            OthelloContext playerOneContext = this;//changePerspective(players[BLACK])
            //same context but as rival's perspective
            OthelloContext playerTwoContext = changePerspective(players[WHITE].getFirst());

            //check remaining moves for player 1
            Future<Boolean> remainingMovesPlayerOne = remainingMoveChecker.submit(
                    new PlayerHasRemainingPlies(playerOneContext));
            //check remaining moves for player 2
            Future<Boolean> remainingMovesPlayerTwo = remainingMoveChecker.submit(
                    new PlayerHasRemainingPlies(playerTwoContext));

            //some player has remaining move : game goes on
            if (remainingMovesPlayerOne.get()
                    || remainingMovesPlayerTwo.get()) {
                return GameState.RUNNING;
            }//no one else can play : compute statistics and determine winner
            else {
                //gather statistics
                for (Ply.Coordinate location : currentBoard) {
                    int pawnID = currentBoard.getPawn(location);
                    if (pawnID == BLACK_PAWN_ID) {
                        pawnCounter[BLACK]++;
                    } else if (pawnID == WHITE_PAWN_ID) {
                        pawnCounter[WHITE]++;
                    }
                }

                //decide who won
                IPlayer currentPlayer = getActivePlayer().getFirst();
                if (pawnCounter[BLACK] > pawnCounter[WHITE]) {
                    if (currentPlayer == players[BLACK].getFirst())
                        return GameState.WIN;
                    else
                        return GameState.LOSE;
                } else if (pawnCounter[WHITE] > pawnCounter[BLACK]) {
                    if (currentPlayer == players[WHITE].getFirst())
                        return GameState.WIN;
                    else
                        return GameState.LOSE;
                } else
                    return GameState.DRAW;
            }

        } catch (InterruptedException e) {
            //Computation can be interrupted by bot worker
//            throw new Error("Unexpected error while computing remaining player's moves", e);
            return null;
        } catch (ExecutionException e) {
            throw new Error("Unexpected error while computing remaining player's moves", e);
        } finally {
            if (remainingMoveChecker != null)
                remainingMoveChecker.shutdownNow();
        }
    }

    @Override
    protected OthelloContext buildEmptyContext() {
        return new OthelloContext();
    }
}
