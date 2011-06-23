package org.eisti.game.othello;

import org.eisti.labs.game.IBoard;
import org.eisti.labs.game.IPlayer;
import org.eisti.labs.game.Ply;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.eisti.labs.game.Ply.Coordinate.Coordinate;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author MACHIZAUD Andréa
 * @version 6/22/11
 */
@RunWith(MockitoJUnitRunner.class)
public class RefereeTest
        implements Othello {

    @Mock
    IPlayer playerOne = mock(IPlayer.class, "player one"); // random player
    IPlayer playerTwo = mock(IPlayer.class, "player rwo"); // another random player

    Referee othelloReferee = new Referee(
            playerOne,
            playerTwo
    );

    @Test
    public void testGetLegalMoves() throws Exception {
        // mock context
        OthelloContext basicContext = mock(OthelloContext.class);

        Board initialBoard = new Board();  //initial board game

        // stub board data
        when(basicContext.getBoard())
                .thenReturn(initialBoard);

        List<Ply> expectedPlyResult = new ArrayList<Ply>(4) {{
            add(new Ply(Coordinate(2, 3)));
            add(new Ply(Coordinate(3, 2)));
            add(new Ply(Coordinate(5, 4)));
            add(new Ply(Coordinate(4, 5)));
        }};

        assertEquals("Wrong legal moves suggested",
                expectedPlyResult,
                othelloReferee.getLegalMoves(basicContext));

    }

    @Test
    public void testNoMoveAvailable() {
        Board initialBoard = new Board(); //original game

        /*
         *  XXX
         *  XOX
         *  XXX
         */
        initialBoard.getCase(3, 3).setPawnID(
                BLACK_PAWN_ID);
        initialBoard.getCase(3, 4).setPawnID(
                BLACK_PAWN_ID);
        initialBoard.getCase(3, 5).setPawnID(
                BLACK_PAWN_ID);
        initialBoard.getCase(4, 3).setPawnID(
                BLACK_PAWN_ID);
        initialBoard.getCase(4, 4).setPawnID(
                WHITE_PAWN_ID);
        initialBoard.getCase(4, 5).setPawnID(
                BLACK_PAWN_ID);
        initialBoard.getCase(5, 3).setPawnID(
                BLACK_PAWN_ID);
        initialBoard.getCase(5, 4).setPawnID(
                BLACK_PAWN_ID);
        initialBoard.getCase(5, 5).setPawnID(
                BLACK_PAWN_ID);

        // mock context
        OthelloContext basicContext = mock(OthelloContext.class);

        // stub board data
        when(basicContext.getBoard())
                .thenReturn(initialBoard);

        //check
        assertEquals("Wrong legal moves suggested",
                0,
                othelloReferee.getLegalMoves(basicContext).size());

    }

    @Test
    public void testSubGameGeneration() {
        //legal ply
        Ply playerPly = mock(Ply.class);
        /*
          * 3 | | | |P| | | | |
          * _________________
          * 4 | | | |O|X| | | |
          * _________________
          * 5 | | | |X|O| | | |
          * _________________
          * 6 | | | | | | | | |
          *
          */
        when(playerPly.getDestination())
                .thenReturn(Coordinate(2, 3));

        //initial game
        Board originalGame = new Board(); //original game

        Board generatedSubGame = othelloReferee.generateSubGame(originalGame, playerPly);

        assertTrue("Pawn not put",
                generatedSubGame.getCase(2, 3).getPawnID() != IBoard.ICase.NO_PAWN);
    }

    @Test
    public void expectedOwnerID() {
        assertEquals("Wrong owner for black pawn",
                BLACK,
                othelloReferee.getOwnerID(BLACK_PAWN_ID));
        assertEquals("Wrong owner for white pawn",
                WHITE,
                othelloReferee.getOwnerID(WHITE_PAWN_ID));
    }

    @Test
    public void expectedPawnTypeID() {
        assertEquals("Wrong type for black pawn",
                PAWN,
                othelloReferee.getPawnTypeID(BLACK_PAWN_ID));
        assertEquals("Wrong type for white pawn",
                PAWN,
                othelloReferee.getPawnTypeID(WHITE_PAWN_ID));
    }

}
