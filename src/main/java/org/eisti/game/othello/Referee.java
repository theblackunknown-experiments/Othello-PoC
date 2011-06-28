package org.eisti.game.othello;

import org.eisti.labs.game.*;
import org.eisti.labs.util.Tuple;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.*;

import static org.eisti.labs.game.IBoard.ICase.NO_PAWN;
import static org.eisti.labs.game.Ply.Coordinate.Coordinate;
import static org.eisti.labs.util.Validation.require;

/**
 * @author MACHIZAUD Andréa
 * @version 6/20/11
 */
public class Referee
        extends AbstractReferee<Board, OthelloContext>
        implements Othello {

    //Thread pool as many as possible direction checking
    private static final ExecutorService LINE_CHECKER =
            Executors.newFixedThreadPool(GridTraversor.values().length);

    @Override
    public Set<Ply> getLegalMoves(OthelloContext context) {
        IPlayer activePlayer = context.getActivePlayer().getFirst();
        Board currentBoard = context.getBoard();
        Set<Ply> legalPlys = new HashSet<Ply>();

        int playerPawn = getPawnID(activePlayer);
        int rivalPawn = playerPawn == BLACK_PAWN_ID
                ? WHITE_PAWN_ID
                : BLACK_PAWN_ID;

        for (IBoard.ICase area : currentBoard)
            if (area.getPawnID() == playerPawn) { //check in every direction where we can put a black pawn
                //TODO Parallel search ; dependent part : rowCursorInit, columnCursorInit, legalPlys (ConcurrentSet)

                Ply.Coordinate start = area.getPosition();

                { //lookup north direction
                    boolean rivalPawnEncountered = false;
                    lookup:
                    for (int rowCursor = start.getRow() - 1,
                                 columnCursor = start.getColumn();//don't start on the same row and with an offset of one
                         rowCursor >= 0;//don't go out of board
                         rowCursor--) {//go north

                        int pawnID = currentBoard.getCase(rowCursor, columnCursor).getPawnID();

                        //stop on a empty case
                        if (pawnID == NO_PAWN) {
                            //it's a legal move if we already encounter a rival pawn,
                            // thus we can reverse a line
                            if (rivalPawnEncountered)
                                legalPlys.add(new Ply(Coordinate(
                                        (char) (columnCursor + 'A'),
                                        (char) (rowCursor + '1')
                                )));
                            break lookup; //Muahahah GOTO !
                        } else if (pawnID == playerPawn) // own pawn encountered, no line reverse possible
                        {
                            break lookup; //Muahahah GOTO !
                        } else if (pawnID == rivalPawn) // rival pawn encountered, try to find where we can put a pawn
                        {
                            rivalPawnEncountered = true;
                        }
                    }
                }
                { //lookup north-east direction
                    boolean rivalPawnEncountered = false;
                    lookup:
                    for (int rowCursor = start.getRow() - 1,
                                 columnCursor = start.getColumn() + 1;//don't start on the same row and with an offset of one
                         rowCursor >= 0 && columnCursor < OTHELLO_DIMENSION.width;//don't go out of board
                         rowCursor--, columnCursor++) {//go north-east

                        int pawnID = currentBoard.getCase(rowCursor, columnCursor).getPawnID();

                        //stop on a empty case
                        if (pawnID == NO_PAWN) {
                            //it's a legal move if we already encounter a rival pawn,
                            // thus we can reverse a line
                            if (rivalPawnEncountered)
                                legalPlys.add(new Ply(Coordinate(
                                        (char) (columnCursor + 'A'),
                                        (char) (rowCursor + '1')
                                )));
                            break lookup; //Muahahah GOTO !
                        } else if (pawnID == playerPawn) // own pawn encountered, no line reverse possible
                        {
                            break lookup;
                        } else if (pawnID == rivalPawn) // rival pawn encountered, try to find where we can put a pawn
                        {
                            rivalPawnEncountered = true;
                        }
                    }
                }
                { //lookup east direction
                    boolean rivalPawnEncountered = false;
                    lookup:
                    for (int rowCursor = start.getRow(),
                                 columnCursor = start.getColumn() + 1;//don't start on the same row and with an offset of one
                         columnCursor < OTHELLO_DIMENSION.width;//don't go out of board
                         columnCursor++) {//go east

                        int pawnID = currentBoard.getCase(rowCursor, columnCursor).getPawnID();

                        //stop on a empty case
                        if (pawnID == NO_PAWN) {
                            //it's a legal move if we already encounter a rival pawn,
                            // thus we can reverse a line
                            if (rivalPawnEncountered)
                                legalPlys.add(new Ply(Coordinate(
                                        (char) (columnCursor + 'A'),
                                        (char) (rowCursor + '1')
                                )));
                            break lookup; //Muahahah GOTO !
                        } else if (pawnID == playerPawn) // own pawn encountered, no line reverse possible
                        {
                            break lookup;
                        } else if (pawnID == rivalPawn) // rival pawn encountered, try to find where we can put a pawn
                        {
                            rivalPawnEncountered = true;
                        }
                    }
                }
                { //lookup south-east direction
                    boolean rivalPawnEncountered = false;
                    lookup:
                    for (int rowCursor = start.getRow() + 1,
                                 columnCursor = start.getColumn() + 1;//don't start on the same row and with an offset of one
                         rowCursor < OTHELLO_DIMENSION.height && columnCursor < OTHELLO_DIMENSION.width;//don't go out of board
                         rowCursor++, columnCursor++) {//go south-east

                        int pawnID = currentBoard.getCase(rowCursor, columnCursor).getPawnID();

                        //stop on a empty case
                        if (pawnID == NO_PAWN) {
                            //it's a legal move if we already encounter a rival pawn,
                            // thus we can reverse a line
                            if (rivalPawnEncountered)
                                legalPlys.add(new Ply(Coordinate(
                                        (char) (columnCursor + 'A'),
                                        (char) (rowCursor + '1')
                                )));
                            break lookup; //Muahahah GOTO !
                        } else if (pawnID == playerPawn) // own pawn encountered, no line reverse possible
                        {
                            break lookup;
                        } else if (pawnID == rivalPawn) // rival pawn encountered, try to find where we can put a pawn
                        {
                            rivalPawnEncountered = true;
                        }
                    }
                }
                { //lookup south direction
                    boolean rivalPawnEncountered = false;
                    lookup:
                    for (int rowCursor = start.getRow() + 1,
                                 columnCursor = start.getColumn();//don't start on the same row and with an offset of one
                         rowCursor < OTHELLO_DIMENSION.height;//don't go out of board
                         rowCursor++) {//go south

                        int pawnID = currentBoard.getCase(rowCursor, columnCursor).getPawnID();

                        //stop on a empty case
                        if (pawnID == NO_PAWN) {
                            //it's a legal move if we already encounter a rival pawn,
                            // thus we can reverse a line
                            if (rivalPawnEncountered)
                                legalPlys.add(new Ply(Coordinate(
                                        (char) (columnCursor + 'A'),
                                        (char) (rowCursor + '1')
                                )));
                            break lookup; //Muahahah GOTO !
                        } else if (pawnID == playerPawn) // own pawn encountered, no line reverse possible
                        {
                            break lookup;
                        } else if (pawnID == rivalPawn) // rival pawn encountered, try to find where we can put a pawn
                        {
                            rivalPawnEncountered = true;
                        }
                    }
                }
                { //lookup south-west direction
                    boolean rivalPawnEncountered = false;
                    lookup:
                    for (int rowCursor = start.getRow() + 1,
                                 columnCursor = start.getColumn() - 1;//don't start on the same row and with an offset of one
                         rowCursor < OTHELLO_DIMENSION.height && columnCursor >= 0;//don't go out of board
                         rowCursor++, columnCursor--) {//go south-west

                        int pawnID = currentBoard.getCase(rowCursor, columnCursor).getPawnID();

                        //stop on a empty case
                        if (pawnID == NO_PAWN) {
                            //it's a legal move if we already encounter a rival pawn,
                            // thus we can reverse a line
                            if (rivalPawnEncountered)
                                legalPlys.add(new Ply(Coordinate(
                                        (char) (columnCursor + 'A'),
                                        (char) (rowCursor + '1')
                                )));
                            break lookup; //Muahahah GOTO !
                        } else if (pawnID == playerPawn) // own pawn encountered, no line reverse possible
                        {
                            break lookup;
                        } else if (pawnID == rivalPawn) // rival pawn encountered, try to find where we can put a pawn
                        {
                            rivalPawnEncountered = true;
                        }
                    }
                }
                { //lookup west direction
                    boolean rivalPawnEncountered = false;
                    lookup:
                    for (int rowCursor = start.getRow(),
                                 columnCursor = start.getColumn() - 1;//don't start on the same row and with an offset of one
                         columnCursor >= 0;//don't go out of board
                         columnCursor--) {//go west

                        int pawnID = currentBoard.getCase(rowCursor, columnCursor).getPawnID();

                        //stop on a empty case
                        if (pawnID == NO_PAWN) {
                            //it's a legal move if we already encounter a rival pawn,
                            // thus we can reverse a line
                            if (rivalPawnEncountered)
                                legalPlys.add(new Ply(Coordinate(
                                        (char) (columnCursor + 'A'),
                                        (char) (rowCursor + '1')
                                )));
                            break lookup; //Muahahah GOTO !
                        } else if (pawnID == playerPawn) // own pawn encountered, no line reverse possible
                        {
                            break lookup;
                        } else if (pawnID == rivalPawn) // rival pawn encountered, try to find where we can put a pawn
                        {
                            rivalPawnEncountered = true;
                        }
                    }
                }
                { //lookup north-west direction
                    boolean rivalPawnEncountered = false;
                    lookup:
                    for (int rowCursor = start.getRow() - 1,
                                 columnCursor = start.getColumn() - 1;//don't start on the same row and with an offset of one
                         rowCursor >= 0 && columnCursor >= 0;//don't go out of board
                         rowCursor--, columnCursor--) {//go north-west

                        int pawnID = currentBoard.getCase(rowCursor, columnCursor).getPawnID();

                        //stop on a empty case
                        if (pawnID == NO_PAWN) {
                            //it's a legal move if we already encounter a rival pawn,
                            // thus we can reverse a line
                            if (rivalPawnEncountered)
                                legalPlys.add(new Ply(Coordinate(
                                        (char) (columnCursor + 'A'),
                                        (char) (rowCursor + '1')
                                )));
                            break lookup; //Muahahah GOTO !
                        } else if (pawnID == playerPawn) // own pawn encountered, no line reverse possible
                        {
                            break lookup;
                        } else if (pawnID == rivalPawn) // rival pawn encountered, try to find where we can put a pawn
                        {
                            rivalPawnEncountered = true;
                        }
                    }
                }
            }

        return legalPlys;
    }

    @Override
    public OthelloContext generateNewContextFrom(OthelloContext previousContext, Ply ply) {
        Board oldBoard = previousContext.getBoard();
        Tuple<IPlayer, Duration> currentPlayer = previousContext.getActivePlayer();

        require(oldBoard.getPawnID(ply.getDestination()) == NO_PAWN,
                "Already a pawn at given ply position : " + ply);

        Board subGame = oldBoard.clone();
        Ply.Coordinate destination = ply.getDestination();
        int playerPawn = getPawnID(currentPlayer.getFirst());
        int rivalPawn = playerPawn == BLACK_PAWN_ID
                ? WHITE_PAWN_ID
                : BLACK_PAWN_ID;

        //put the pawn at correct place
        subGame.getCase(
                destination.getRow(),
                destination.getColumn())
                .setPawnID(playerPawn);

        /*
         * Reverse pawns following othello's rules
         */
        //TODO Parallel update ; check for data conflict
        //lookup north direction
        for (int rowCursor = destination.getRow() - 1;//don't start on the same row and with an offset of one
             rowCursor >= 0;//don't go out of board
             rowCursor--) {//go north

            //stop lookup when a same player pawn is encountered
            if (subGame
                    .getCase(rowCursor, destination.getColumn())
                    .getPawnID() == playerPawn) {
                //TODO Inline method here when parallelize it
                //reverse pawn in this range
                reversePawn(
                        subGame,
                        currentPlayer.getFirst(),
                        GridTraversor.NORTH,
                        destination,
                        Coordinate(
                                (char) (destination.getColumn() + 'A'),
                                (char) (rowCursor + '1')
                        ));
                break;
            }
        }
        //lookup north-east direction
        for (int rowCursor = destination.getRow() - 1,
                     columnCursor = destination.getColumn() + 1;//don't start on the same row and with an offset of one
             rowCursor >= 0 && columnCursor < OTHELLO_DIMENSION.width;//don't go out of board
             rowCursor--, columnCursor++) {//go north-east

            //stop lookup when a same player pawn is encountered
            if (subGame
                    .getCase(rowCursor, columnCursor)
                    .getPawnID() == playerPawn) {
                //reverse pawn in this range
                reversePawn(
                        subGame,
                        currentPlayer.getFirst(),
                        GridTraversor.NORTH_EAST,
                        destination,
                        Coordinate(
                                (char) (columnCursor + 'A'),
                                (char) (rowCursor + '1')
                        ));
                break;
            }
        }
        //lookup east direction
        for (int columnCursor = destination.getColumn() + 1;//don't start on the same row and with an offset of one
             columnCursor < OTHELLO_DIMENSION.width;//don't go out of board
             columnCursor++) {//go east

            //stop lookup when a same player pawn is encountered
            if (subGame
                    .getCase(destination.getRow(), columnCursor)
                    .getPawnID() == playerPawn) {
                //reverse pawn in this range
                reversePawn(
                        subGame,
                        currentPlayer.getFirst(),
                        GridTraversor.EAST,
                        destination,
                        Coordinate(
                                (char) (columnCursor + 'A'),
                                (char) (destination.getRow() + '1')
                        ));
                break;
            }
        }
        //lookup south-east direction
        for (int rowCursor = destination.getRow() + 1,
                     columnCursor = destination.getColumn() + 1;//don't start on the same row and with an offset of one
             rowCursor < OTHELLO_DIMENSION.height && columnCursor < OTHELLO_DIMENSION.width;//don't go out of board
             rowCursor++, columnCursor++) {//go south-east

            //stop lookup when a same player pawn is encountered
            if (subGame
                    .getCase(rowCursor, columnCursor)
                    .getPawnID() == playerPawn) {
                //reverse pawn in this range
                reversePawn(
                        subGame,
                        currentPlayer.getFirst(),
                        GridTraversor.SOUTH_EAST,
                        destination,
                        Coordinate(
                                (char) (columnCursor + 'A'),
                                (char) (rowCursor + '1')
                        ));
                break;
            }
        }
        //lookup south direction
        for (int rowCursor = destination.getRow() + 1;//don't start on the same row and with an offset of one
             rowCursor < OTHELLO_DIMENSION.height;//don't go out of board
             rowCursor++) {//go south

            //stop lookup when a same player pawn is encountered
            if (subGame
                    .getCase(rowCursor, destination.getColumn())
                    .getPawnID() == playerPawn) {
                //reverse pawn in this range
                reversePawn(
                        subGame,
                        currentPlayer.getFirst(),
                        GridTraversor.SOUTH,
                        destination,
                        Coordinate(
                                (char) (destination.getColumn() + 'A'),
                                (char) (rowCursor + '1')
                        ));
                break;
            }
        }
        //lookup south-west direction
        for (int rowCursor = destination.getRow() + 1,
                     columnCursor = destination.getColumn() - 1;//don't start on the same row and with an offset of one
             rowCursor < OTHELLO_DIMENSION.height && columnCursor >= 0;//don't go out of board
             rowCursor++, columnCursor--) {//go south-west

            //stop lookup when a same player pawn is encountered
            if (subGame
                    .getCase(rowCursor, columnCursor)
                    .getPawnID() == playerPawn) {
                //reverse pawn in this range
                reversePawn(
                        subGame,
                        currentPlayer.getFirst(),
                        GridTraversor.SOUTH_WEST,
                        destination,
                        Coordinate(
                                (char) (columnCursor + 'A'),
                                (char) (rowCursor + '1')
                        ));
                break;
            }
        }
        //lookup west direction
        for (int columnCursor = destination.getColumn() - 1;//don't start on the same row and with an offset of one
             columnCursor >= 0;//don't go out of board
             columnCursor--) {//go west

            //stop lookup when a same player pawn is encountered
            if (subGame
                    .getCase(destination.getRow(), columnCursor)
                    .getPawnID() == playerPawn) {
                //reverse pawn in this range
                reversePawn(
                        subGame,
                        currentPlayer.getFirst(),
                        GridTraversor.WEST,
                        destination,
                        Coordinate(
                                (char) (columnCursor + 'A'),
                                (char) (destination.getRow() + '1')
                        ));
                break;
            }
        }
        //lookup north-west direction
        for (int rowCursor = destination.getRow() - 1,
                     columnCursor = destination.getColumn() - 1;//don't start on the same row and with an offset of one
             rowCursor >= 0 && columnCursor >= 0;//don't go out of board
             rowCursor--, columnCursor--) {//go north-west

            //stop lookup when a same player pawn is encountered
            if (subGame
                    .getCase(rowCursor, columnCursor)
                    .getPawnID() == playerPawn) {
                //reverse pawn in this range
                reversePawn(
                        subGame,
                        currentPlayer.getFirst(),
                        GridTraversor.NORTH_WEST,
                        destination,
                        Coordinate(
                                (char) (columnCursor + 'A'),
                                (char) (rowCursor + '1')
                        ));
                break;
            }
        }

        return previousContext.branchOff(subGame);
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
                         OTHELLO PART
      =========================================================================*/

    private enum GridTraversor {
        NORTH_WEST, NORTH, NORTH_EAST,
        WEST, EAST,
        SOUTH_WEST, SOUTH, SOUTH_EAST
    }

    private static abstract class LineTraversor<V>
            implements Callable<V> {

        private LineTraversor(
                Board board,
                Ply.Coordinate start,
                GridTraversor direction) {
        }

        abstract V action();

        public V call() throws Exception {
            //TODO Implement basic skeleton
            V value = action();
            //TODO Implement basic skeleton
            return value;
        }

    }

    private static class ReverseLineTraversor extends LineTraversor<Void> {

        private ReverseLineTraversor(
                Board board,
                Ply.Coordinate start,
                GridTraversor direction) {
            super(board, start, direction);
        }

        @Override
        public Void action() {
            //TODO Reverse a line
            return null;
        }
    }

    private static class RegisterLegalPly extends LineTraversor<Ply> {

        private RegisterLegalPly(
                Board board,
                Ply.Coordinate start,
                GridTraversor direction) {
            super(board, start, direction);
        }

        @Override
        public Ply action() {
            //TODO Register legal plies
            return null;
        }
    }

    private static void reversePawn(
            Board subGame,
            IPlayer player,
            GridTraversor traversor,
            Ply.Coordinate startPosition,
            Ply.Coordinate endPosition) {

        int playerPawn = getPawnID(player);
        int rivalPawn = playerPawn == BLACK_PAWN_ID
                ? WHITE_PAWN_ID
                : BLACK_PAWN_ID;

        switch (traversor) {
            case NORTH:
                for (int i = startPosition.getRow(),
                             j = startPosition.getColumn();
                     i > endPosition.getRow();
                     i--) {
                    IBoard.ICase area = subGame.getCase(i, j);
                    if (area.getPawnID() == rivalPawn)
                        area.setPawnID(playerPawn);
                }
                break;
            case NORTH_EAST:
                for (int i = startPosition.getRow(),
                             j = startPosition.getColumn();
                     i > endPosition.getRow() && j < endPosition.getColumn();
                     i--, j++) {
                    IBoard.ICase area = subGame.getCase(i, j);
                    if (area.getPawnID() == rivalPawn)
                        area.setPawnID(playerPawn);
                }
                break;
            case EAST:
                for (int i = startPosition.getRow(),
                             j = startPosition.getColumn();
                     j < endPosition.getColumn();
                     j++) {
                    IBoard.ICase area = subGame.getCase(i, j);
                    if (area.getPawnID() == rivalPawn)
                        area.setPawnID(playerPawn);
                }
                break;
            case SOUTH_EAST:
                for (int i = startPosition.getRow(),
                             j = startPosition.getColumn();
                     i < endPosition.getRow() && j < endPosition.getColumn();
                     i++, j++) {
                    IBoard.ICase area = subGame.getCase(i, j);
                    if (area.getPawnID() == rivalPawn)
                        area.setPawnID(playerPawn);
                }
                break;
            case SOUTH:
                for (int i = startPosition.getRow(),
                             j = startPosition.getColumn();
                     i < endPosition.getRow();
                     i++) {
                    IBoard.ICase area = subGame.getCase(i, j);
                    if (area.getPawnID() == rivalPawn)
                        area.setPawnID(playerPawn);
                }
                break;
            case SOUTH_WEST:
                for (int i = startPosition.getRow(),
                             j = startPosition.getColumn();
                     i < endPosition.getRow() && j > endPosition.getColumn();
                     i++, j--) {
                    IBoard.ICase area = subGame.getCase(i, j);
                    if (area.getPawnID() == rivalPawn)
                        area.setPawnID(playerPawn);
                }
                break;
            case WEST:
                for (int i = startPosition.getRow(),
                             j = startPosition.getColumn();
                     j > endPosition.getColumn();
                     j--) {
                    IBoard.ICase area = subGame.getCase(i, j);
                    if (area.getPawnID() == rivalPawn)
                        area.setPawnID(playerPawn);
                }
                break;
            case NORTH_WEST:
                for (int i = startPosition.getRow(),
                             j = startPosition.getColumn();
                     i > endPosition.getRow() && j > endPosition.getColumn();
                     i--, j--) {
                    IBoard.ICase area = subGame.getCase(i, j);
                    if (area.getPawnID() == rivalPawn)
                        area.setPawnID(playerPawn);
                }
                break;
        }
    }

    // specific to othello, because there is only one pawn's type
    public static int getPawnID(IPlayer player) {
        return player.getIdentifier() == Othello.WHITE
                ? WHITE_PAWN_ID
                : BLACK_PAWN_ID;
    }
}
