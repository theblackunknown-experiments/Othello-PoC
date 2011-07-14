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
import org.eisti.labs.game.Ply;

import static org.eisti.game.othello.OthelloProperties.OTHELLO_DIMENSION;
import static org.eisti.labs.game.Ply.Coordinate.*;

/**
 * Represent task which traverse a grid line
 * FIXME Generalize bound collisions for traversor
 * TODO Migrate it to core framework
 *
 * @author MACHIZAUD Andréa
 * @version 7/2/11
 */
abstract public class LineTraversor {

    final Board _board;
    final Ply.Coordinate _start;
    final GridTraversor _direction;

    /**
     * Task constructor
     *
     * @param board     board on which we iterate
     * @param start     start position of traversal
     * @param direction direction in which we iterate
     */
    LineTraversor(
            Board board,
            Ply.Coordinate start,
            GridTraversor direction) {
        this._board = board;
        this._start = start;
        this._direction = direction;
    }

    /**
     * Represent possible direction for traversing a grid line
     */
    public enum GridTraversor {
        NORTH_WEST, NORTH, NORTH_EAST,
        WEST, EAST,
        SOUTH_WEST, SOUTH, SOUTH_EAST
    }

    /**
     * Interface designing as a utility for for-loop condition checking and variable update utility
     */
    protected interface LineIterator {

        /**
         * For-loop condition checking
         *
         * @param current - current location
         * @param goal    - target position
         * @return if for-loop can go on
         */
        boolean verify(final Ply.Coordinate current, final Ply.Coordinate goal);

        /**
         * Loop variable initialization, generally there is a little gap with given location
         *
         * @param startLocation - start location argument passed
         * @return start value to iterate
         */
        Ply.Coordinate initialize(final Ply.Coordinate startLocation);

        /**
         * Loop variable update
         *
         * @param previous previous value from loop
         * @return next value
         */
        Ply.Coordinate update(final Ply.Coordinate previous);
    }

    /**
     * Factory method to get the right LineIterator depending in which direction we go
     *
     * @param direction direction in which we want follow
     * @return LineIterator that guarantees we can in the right direction
     */
    static LineIterator getIterator(GridTraversor direction) {
        switch (direction) {
            case NORTH:
                return NORTH_ITERATOR;
            case NORTH_EAST:
                return NORTH_EAST_ITERATOR;
            case EAST:
                return EAST_ITERATOR;
            case SOUTH_EAST:
                return SOUTH_EAST_ITERATOR;
            case SOUTH:
                return SOUTH_ITERATOR;
            case SOUTH_WEST:
                return SOUTH_WEST_ITERATOR;
            case WEST:
                return WEST_ITERATOR;
            case NORTH_WEST:
                return NORTH_WEST_ITERATOR;
            default:
                throw new Error("Unknown direction : " + direction);
        }
    }

    //Iterator when we got north
    private static final LineIterator NORTH_ITERATOR = new LineIterator() {

        @Override
        public final boolean verify(final Ply.Coordinate current, final Ply.Coordinate goal) {
            return goal == null
                    ? !current.getRow().equals(BEFORE_FIRST_ROW)
                    : rowLabel2index(current.getRow()) > rowLabel2index(goal.getRow());
        }

        @Override
        public final Ply.Coordinate initialize(final Ply.Coordinate startLocation) {
            return Coordinate(
                    startLocation.getColumn(),
                    rowIndex2Label(rowLabel2index(startLocation.getRow()) - 1)
            );
        }

        @Override
        public final Ply.Coordinate update(final Ply.Coordinate previous) {
            //same gap...
            return initialize(previous);
        }
    };
    //Iterator when we go north east
    private static final LineIterator NORTH_EAST_ITERATOR = new LineIterator() {

        @Override
        public final boolean verify(final Ply.Coordinate current, final Ply.Coordinate goal) {
            return goal == null
                    ? !current.getRow().equals(BEFORE_FIRST_ROW)
                    && columnLabel2index(current.getColumn()) < OTHELLO_DIMENSION.width
                    : rowLabel2index(current.getRow()) > rowLabel2index(goal.getRow())
                    && columnLabel2index(current.getColumn()) < columnLabel2index(goal.getColumn());
        }

        @Override
        public final Ply.Coordinate initialize(final Ply.Coordinate startLocation) {
            return Coordinate(
                    columnIndex2Label(columnLabel2index(startLocation.getColumn()) + 1),
                    rowIndex2Label(rowLabel2index(startLocation.getRow()) - 1)
            );
        }

        @Override
        public final Ply.Coordinate update(final Ply.Coordinate previous) {
            //same gap...
            return initialize(previous);
        }
    };
    //Iterator when we go east
    private static final LineIterator EAST_ITERATOR = new LineIterator() {

        @Override
        public final boolean verify(final Ply.Coordinate current, final Ply.Coordinate goal) {
            return goal == null
                    ? columnLabel2index(current.getColumn()) < OTHELLO_DIMENSION.width
                    : columnLabel2index(current.getColumn()) < columnLabel2index(goal.getColumn());
        }

        @Override
        public final Ply.Coordinate initialize(final Ply.Coordinate startLocation) {
            return Coordinate(
                    columnIndex2Label(columnLabel2index(startLocation.getColumn()) + 1),
                    startLocation.getRow()
            );
        }

        @Override
        public final Ply.Coordinate update(final Ply.Coordinate previous) {
            //same gap...
            return initialize(previous);
        }
    };
    //Iterator when we go south east
    private static final LineIterator SOUTH_EAST_ITERATOR = new LineIterator() {

        @Override
        public boolean verify(Ply.Coordinate current, Ply.Coordinate goal) {
            return goal == null
                    ? rowLabel2index(current.getRow()) < OTHELLO_DIMENSION.height
                    && columnLabel2index(current.getColumn()) < OTHELLO_DIMENSION.width
                    : rowLabel2index(current.getRow()) < rowLabel2index(goal.getRow())
                    && columnLabel2index(current.getColumn()) < columnLabel2index(goal.getColumn());
        }

        @Override
        public Ply.Coordinate initialize(Ply.Coordinate startLocation) {
            return Coordinate(
                    columnIndex2Label(columnLabel2index(startLocation.getColumn()) + 1),
                    rowIndex2Label(rowLabel2index(startLocation.getRow()) + 1)
            );
        }

        @Override
        public final Ply.Coordinate update(final Ply.Coordinate previous) {
            //same gap...
            return initialize(previous);
        }
    };
    //Iterator when we go south
    private static final LineIterator SOUTH_ITERATOR = new LineIterator() {

        @Override
        public boolean verify(Ply.Coordinate current, Ply.Coordinate goal) {
            return goal == null
                    ? rowLabel2index(current.getRow()) < OTHELLO_DIMENSION.height
                    : rowLabel2index(current.getRow()) < rowLabel2index(goal.getRow());
        }

        @Override
        public Ply.Coordinate initialize(Ply.Coordinate startLocation) {
            return Coordinate(
                    startLocation.getColumn(),
                    rowIndex2Label(rowLabel2index(startLocation.getRow()) + 1)
            );
        }

        @Override
        public final Ply.Coordinate update(final Ply.Coordinate previous) {
            //same gap...
            return initialize(previous);
        }
    };
    //Iterator when we go south west
    private static final LineIterator SOUTH_WEST_ITERATOR = new LineIterator() {

        @Override
        public boolean verify(Ply.Coordinate current, Ply.Coordinate goal) {
            return goal == null
                    ? rowLabel2index(current.getRow()) < OTHELLO_DIMENSION.height
                    && !current.getColumn().equals(BEFORE_FIRST_COLUMN)
                    : rowLabel2index(current.getRow()) < rowLabel2index(goal.getRow())
                    && columnLabel2index(current.getColumn()) > columnLabel2index(goal.getColumn());
        }

        @Override
        public Ply.Coordinate initialize(Ply.Coordinate startLocation) {
            return Coordinate(
                    columnIndex2Label(columnLabel2index(startLocation.getColumn()) - 1),
                    rowIndex2Label(rowLabel2index(startLocation.getRow()) + 1)
            );
        }

        @Override
        public final Ply.Coordinate update(final Ply.Coordinate previous) {
            //same gap...
            return initialize(previous);
        }
    };
    //Iterator when we go west
    private static final LineIterator WEST_ITERATOR = new LineIterator() {

        @Override
        public boolean verify(Ply.Coordinate current, Ply.Coordinate goal) {
            return goal == null
                    ? !current.getColumn().equals(BEFORE_FIRST_COLUMN)
                    : columnLabel2index(current.getColumn()) > columnLabel2index(goal.getColumn());
        }

        @Override
        public Ply.Coordinate initialize(Ply.Coordinate startLocation) {
            return Coordinate(
                    columnIndex2Label(columnLabel2index(startLocation.getColumn()) - 1),
                    startLocation.getRow()
            );
        }

        @Override
        public final Ply.Coordinate update(final Ply.Coordinate previous) {
            //same gap...
            return initialize(previous);
        }
    };
    //Iterator when we go north west
    private static final LineIterator NORTH_WEST_ITERATOR = new LineIterator() {

        @Override
        public boolean verify(Ply.Coordinate current, Ply.Coordinate goal) {
            return goal == null
                    ? !current.getRow().equals(BEFORE_FIRST_ROW) && !current.getColumn().equals(BEFORE_FIRST_COLUMN)
                    : rowLabel2index(current.getRow()) > rowLabel2index(goal.getRow())
                    && columnLabel2index(current.getColumn()) > columnLabel2index(goal.getColumn());
        }

        @Override
        public Ply.Coordinate initialize(Ply.Coordinate startLocation) {
            return Coordinate(
                    columnIndex2Label(columnLabel2index(startLocation.getColumn()) - 1),
                    rowIndex2Label(rowLabel2index(startLocation.getRow()) - 1)
            );
        }

        @Override
        public final Ply.Coordinate update(final Ply.Coordinate previous) {
            //same gap...
            return initialize(previous);
        }
    };

}
