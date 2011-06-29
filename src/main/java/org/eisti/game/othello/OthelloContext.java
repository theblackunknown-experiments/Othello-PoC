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

import org.eisti.labs.game.Duration;
import org.eisti.labs.game.GameContext;
import org.eisti.labs.game.IBoard;
import org.eisti.labs.game.IPlayer;
import org.eisti.labs.util.Tuple;

import java.lang.reflect.Array;

/**
 * @author MACHIZAUD Andréa
 * @version 23/06/11
 */
public class OthelloContext
        extends GameContext<Board>
        implements Othello {

    private static Board[] castArray(IBoard[] generalArray){
        Board[] castedArray = new Board[generalArray.length];
        for(int i=generalArray.length;i-->0;)
            castedArray[i] = (Board) generalArray[i];
        return castedArray;
    }

    public OthelloContext(
            Duration elapsedTime,
            IBoard[] history,
            IPlayer[] playersInGame,
            Duration[] playersRemainingTime) {
        super(elapsedTime, castArray(history), playersInGame, playersRemainingTime);
    }

    private OthelloContext() { super(); }

    //FIXME Wrong ending : No one else can play, or no more empty case on board
    @Override
    public GameState getState() {
        int[] pawnCounter = new int[NUMBERS_OF_PLAYERS];
        pawnCounter[BLACK] = pawnCounter[WHITE] = 0;
        Board currentBoard = getBoard();
        Tuple<IPlayer, Duration>[] players = getPlayers();

        //gather statistics
        for (IBoard.ICase area : currentBoard) {
            int pawnID = area.getPawnID();
            if (pawnID == IBoard.ICase.NO_PAWN) {
                return GameState.NOT_YET_FINISH;
            } else if (pawnID == BLACK_PAWN_ID) {
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

    @Override
    protected GameContext buildEmptyContext() {
        return new OthelloContext();
    }

    @Override
    public OthelloContext branchOff(Board board) {
        return (OthelloContext) super.branchOff(board);
    }
}
