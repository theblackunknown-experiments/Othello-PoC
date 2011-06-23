package org.eisti.game.othello;

import org.eisti.labs.game.*;
import org.eisti.labs.util.Tuple;

import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import static org.eisti.labs.util.Validation.require;

/**
 * @author MACHIZAUD Andréa
 * @version 6/20/11
 */
public class Referee
        extends AbstractReferee<Board, OthelloContext>
        implements Othello {

    //TODO Clock Tasks
    private Duration elapsedTime;

    public Duration getElapsedTime() {
        return elapsedTime;
    }

    //TODO Clock Tasks
    private Duration[] playersGrantedTime;

    public Duration[] getGrantedTime() {
        return playersGrantedTime;
    }

    //TODO Handle updating, keep in mind add at head
    private Queue<Board> history = new LinkedList<Board>();

    public Board[] getHistory() {
        Board[] historyArray = new Board[history.size()];
        history.toArray(historyArray);
        return historyArray;
    }

    Referee updateHistory(Board recentBoard) {
        history.add(recentBoard);
        return this;
    }

    @Override
    protected IPlayer[] registerPlayers(Tuple<Integer, Class<IPlayer>>... players)
        //forwaded error cause constructor to fail if any problems
            throws
            NoSuchMethodException,
            InvocationTargetException,
            IllegalAccessException,
            InstantiationException {
        return new IPlayer[]{
                //black player
                players[BLACK].getSecond()
                        .getConstructor(int.class)
                        .newInstance(players[BLACK].getFirst()),
                //white player
                players[WHITE].getSecond()
                        .getConstructor(int.class)
                        .newInstance(players[WHITE].getFirst())
        };
    }

    @Override
    public List<Ply> getLegalMoves(OthelloContext gameContext) {
        IPlayer activePlayer = getActivePlayer();
        IPlayer[] allPlayers = getPlayers();
        Board currentBoard = gameContext.getBoard();
        List<Ply> legalPlys = new LinkedList<Ply>();

        for (IBoard.ICase area : currentBoard)
            if (area.getPawnID() == WHITE_PAWN_ID
                    && activePlayer == allPlayers[BLACK]) { // BLACK PLAYER
                for (IBoard.ICase emptyCaseAround : currentBoard.getFreeCaseAround(area))
                    legalPlys.add(new Ply(emptyCaseAround.getPosition()));
            } else if (area.getPawnID() == BLACK_PAWN_ID
                    && activePlayer == allPlayers[WHITE]) {// WHITE PLAYER
                for (IBoard.ICase emptyCaseAround : currentBoard.getFreeCaseAround(area))
                    legalPlys.add(new Ply(emptyCaseAround.getPosition()));
            }
        // else the case is empty so we don't care

        return legalPlys;
    }

    @Override
    public Board generateSubGame(Board iBoard, Ply ply) {
        require(iBoard.getPawnID(ply.getDestination()) == IBoard.ICase.NO_PAWN,
                "Already a pawn at given ply position : " + ply);

        Board subGame = iBoard.clone();

        //put the pawn at correct place
        subGame.getCase(ply.getDestination().getRow(), ply.getDestination().getColumn())
                .setPawnID(
                        getPawnID(getActivePlayer()));

        return subGame;
    }

    @Override
    public OthelloContext getCurrentContext() {
        return new OthelloContext(
                getElapsedTime(),
                getHistory(),
                getPlayers(),
                getGrantedTime()
        );
    }

    @Override
    public int getNumberOfPlayer() {
        return NUMBERS_OF_PLAYERS;
    }

    @Override
    public int getNumberOfTypedPawns() {
        return NUMBERS_OF_TYPED_PAWN;
    }

    /*=========================================================================
                         PRIVATE i.e Part specific to that game
      =========================================================================*/

    // specific to othello, because there is only one pawn's type
    public int getPawnID(IPlayer player) {
        return player == getPlayers()[WHITE]
                ? WHITE_PAWN_ID
                : BLACK_PAWN_ID;
    }


    public Referee(
            IPlayer firstPlayer,
            IPlayer secondPlayer) {
        super(firstPlayer, secondPlayer);
    }
}
