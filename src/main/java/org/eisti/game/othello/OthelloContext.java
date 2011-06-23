package org.eisti.game.othello;

import org.eisti.labs.game.Duration;
import org.eisti.labs.game.GameContext;
import org.eisti.labs.game.IBoard;
import org.eisti.labs.game.IPlayer;
import org.eisti.labs.util.Tuple;

/**
 * @author MACHIZAUD Andréa
 * @version 23/06/11
 */
public class OthelloContext
        extends GameContext<Board>
        implements Othello {

    public OthelloContext(
            Duration elapsedTime,
            Board[] history,
            IPlayer[] playersInGame,
            Duration[] playersRemainingTime) {
        super(elapsedTime, history, playersInGame, playersRemainingTime);
    }


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
}
