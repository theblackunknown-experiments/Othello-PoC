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
import static org.eisti.labs.game.Ply.Coordinate.Coordinate;

/**
 * Represent task which traverse a grid line
 *
 * @author MACHIZAUD Andréa
 * @version 7/2/11
 */
abstract public class LineTraversor {

    protected Board _board;
    protected Ply.Coordinate _start;
    protected GridTraversor _direction;

    /**
     * Task constructor
     *
     * @param board     board on which we iterate
     * @param start     start position of traversal
     * @param direction direction in which we iterate
     */
    public LineTraversor(
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
    protected static LineIterator getIterator(GridTraversor direction) {
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
                    ? current.getRow() >= '1'
                    : current.getRow() > goal.getRow();
        }

        @Override
        public final Ply.Coordinate initialize(final Ply.Coordinate startLocation) {
            return Coordinate(
                    (char) startLocation.getColumn(),
                    (char) (startLocation.getRow() - 1)
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
                    ? current.getRow() >= '1' && current.getColumn() < (char) ('A' + OTHELLO_DIMENSION.width)
                    : current.getRow() > goal.getRow() && current.getColumn() < goal.getColumn();
        }

        @Override
        public final Ply.Coordinate initialize(final Ply.Coordinate startLocation) {
            return Coordinate(
                    (char) (startLocation.getColumn() + 1),
                    (char) (startLocation.getRow() - 1)
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
                    ? current.getColumn() < (char) ('A' + OTHELLO_DIMENSION.width)
                    : current.getColumn() < goal.getColumn();
        }

        @Override
        public final Ply.Coordinate initialize(final Ply.Coordinate startLocation) {
            return Coordinate(
                    (char) (startLocation.getColumn() + 1),
                    (char) startLocation.getRow()
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
                    ? current.getRow() < (char) ('1' + OTHELLO_DIMENSION.height)
                    && current.getColumn() < (char) ('A' + OTHELLO_DIMENSION.width)
                    : current.getRow() < goal.getRow() && current.getColumn() < goal.getColumn();
        }

        @Override
        public Ply.Coordinate initialize(Ply.Coordinate startLocation) {
            return Coordinate(
                    (char) (startLocation.getColumn() + 1),
                    (char) (startLocation.getRow() + 1)
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
                    ? current.getRow() < (char) ('1' + OTHELLO_DIMENSION.height)
                    : current.getRow() < goal.getRow();
        }

        @Override
        public Ply.Coordinate initialize(Ply.Coordinate startLocation) {
            return Coordinate(
                    (char) startLocation.getColumn(),
                    (char) (startLocation.getRow() + 1)
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
                    ? current.getRow() < (char) ('1' + OTHELLO_DIMENSION.height) && current.getColumn() >= 'A'
                    : current.getRow() < goal.getRow() && current.getColumn() > goal.getColumn();
        }

        @Override
        public Ply.Coordinate initialize(Ply.Coordinate startLocation) {
            return Coordinate(
                    (char) (startLocation.getColumn() - 1),
                    (char) (startLocation.getRow() + 1)
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
                    ? current.getColumn() >= 'A'
                    : current.getColumn() > goal.getColumn();
        }

        @Override
        public Ply.Coordinate initialize(Ply.Coordinate startLocation) {
            return Coordinate(
                    (char) (startLocation.getColumn() - 1),
                    (char) startLocation.getRow()
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
                    ? current.getRow() >= '1' && current.getColumn() >= 'A'
                    : current.getRow() > goal.getRow() && current.getColumn() > goal.getColumn();
        }

        @Override
        public Ply.Coordinate initialize(Ply.Coordinate startLocation) {
            return Coordinate(
                    (char) (startLocation.getColumn() - 1),
                    (char) (startLocation.getRow() - 1)
            );
        }

        @Override
        public final Ply.Coordinate update(final Ply.Coordinate previous) {
            //same gap...
            return initialize(previous);
        }
    };

}
