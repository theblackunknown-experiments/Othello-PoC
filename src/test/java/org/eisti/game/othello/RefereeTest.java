package org.eisti.game.othello;

import org.eisti.labs.game.Duration;
import org.eisti.labs.game.IBoard;
import org.eisti.labs.game.IPlayer;
import org.eisti.labs.game.Ply;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.eisti.labs.game.Ply.Coordinate.Coordinate;
import static org.eisti.labs.util.Tuple.Tuple;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
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
    IPlayer playerOne;
    @Mock
    IPlayer playerTwo;

    Referee othelloReferee;

    @Before
    public void createReferee() {
        othelloReferee = new Referee();
    }

    @Test
    public void testGetLegalMoves() throws Exception {
        // mock context
        OthelloContext basicContext = mock(OthelloContext.class);

        Board initialBoard = new Board();  //initial board game

        // stub board data
        when(basicContext.getBoard())
                .thenReturn(initialBoard);
        when(basicContext.getActivePlayer())
                .thenReturn(Tuple(playerOne, new Duration(10L, TimeUnit.SECONDS)));

        List<Ply> expectedPlyResult = new ArrayList<Ply>(4) {{
            add(new Ply(Coordinate('D', '3')));
            add(new Ply(Coordinate('C', '4')));
            add(new Ply(Coordinate('E', '6')));
            add(new Ply(Coordinate('F', '5')));
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
        when(basicContext.getActivePlayer())
                .thenReturn(Tuple(playerOne, new Duration(10L, TimeUnit.SECONDS)));

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
                .thenReturn(Coordinate('D', '3'));

        // mock context
        OthelloContext basicContext = mock(OthelloContext.class);

        Board initialBoard = new Board();  //initial board game

        OthelloContext emptyMock = mock(OthelloContext.class);

        // stub board data
        when(basicContext.getBoard())
                .thenReturn(initialBoard);
        when(basicContext.getActivePlayer())
                .thenReturn(Tuple(playerOne, new Duration(10L, TimeUnit.SECONDS)));
        when(basicContext.buildEmptyContext())
                .thenReturn(emptyMock);


        OthelloContext newContext =
                othelloReferee.generateNewContextFrom(basicContext, playerPly);

        assertTrue("Pawn not put",
                newContext.getBoard().getCase(2, 3).getPawnID() != IBoard.ICase.NO_PAWN);
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
