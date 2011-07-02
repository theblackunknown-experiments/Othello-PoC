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

import static org.eisti.game.othello.Othello.OTHELLO_DIMENSION;

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
         * @param row    current row
         * @param column current column
         * @param goal   target position
         * @return if for-loop can go on
         */
        boolean verify(int row, int column, Ply.Coordinate goal);

        /**
         * Row variable update
         *
         * @param previous previous value for row variable
         * @return next value for row variable
         */
        int updateRow(int previous);

        /**
         * Column variable update
         *
         * @param previous previous value for column variable
         * @return next value for column variable
         */
        int updateColumn(int previous);
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
        public boolean verify(int row, int column, Ply.Coordinate goal) {
            return goal == null
                    ? row >= 0
                    : row > goal.getRow();
        }

        @Override
        public int updateRow(int previous) {
            return previous - 1;
        }

        @Override
        public int updateColumn(int previous) {
            return previous;
        }
    };
    //Iterator when we go north east
    private static final LineIterator NORTH_EAST_ITERATOR = new LineIterator() {
        @Override
        public boolean verify(int row, int column, Ply.Coordinate goal) {
            return goal == null
                    ? row >= 0 && column < OTHELLO_DIMENSION.width
                    : row > goal.getRow() && column < goal.getColumn();
        }

        @Override
        public int updateRow(int previous) {
            return previous - 1;
        }

        @Override
        public int updateColumn(int previous) {
            return previous + 1;
        }
    };
    //Iterator when we go east
    private static final LineIterator EAST_ITERATOR = new LineIterator() {
        @Override
        public boolean verify(int row, int column, Ply.Coordinate goal) {
            return goal == null
                    ? column < OTHELLO_DIMENSION.width
                    : column < goal.getColumn();
        }

        @Override
        public int updateRow(int previous) {
            return previous;
        }

        @Override
        public int updateColumn(int previous) {
            return previous + 1;
        }
    };
    //Iterator when we go south east
    private static final LineIterator SOUTH_EAST_ITERATOR = new LineIterator() {
        @Override
        public boolean verify(int row, int column, Ply.Coordinate goal) {
            return goal == null
                    ? row < OTHELLO_DIMENSION.height && column < OTHELLO_DIMENSION.width
                    : row < goal.getRow() && column < goal.getColumn();
        }

        @Override
        public int updateRow(int previous) {
            return previous + 1;
        }

        @Override
        public int updateColumn(int previous) {
            return previous + 1;
        }
    };
    //Iterator when we go south
    private static final LineIterator SOUTH_ITERATOR = new LineIterator() {
        @Override
        public boolean verify(int row, int column, Ply.Coordinate goal) {
            return goal == null
                    ? row < OTHELLO_DIMENSION.height
                    : row < goal.getRow();
        }

        @Override
        public int updateRow(int previous) {
            return previous + 1;
        }

        @Override
        public int updateColumn(int previous) {
            return previous;
        }
    };
    //Iterator when we go south west
    private static final LineIterator SOUTH_WEST_ITERATOR = new LineIterator() {
        @Override
        public boolean verify(int row, int column, Ply.Coordinate goal) {
            return goal == null
                    ? row < OTHELLO_DIMENSION.height && column >= 0
                    : row < goal.getRow() && column > goal.getColumn();
        }

        @Override
        public int updateRow(int previous) {
            return previous + 1;
        }

        @Override
        public int updateColumn(int previous) {
            return previous - 1;
        }
    };
    //Iterator when we go west
    private static final LineIterator WEST_ITERATOR = new LineIterator() {
        @Override
        public boolean verify(int row, int column, Ply.Coordinate goal) {
            return goal == null
                    ? column >= 0
                    : column > goal.getColumn();
        }

        @Override
        public int updateRow(int previous) {
            return previous;
        }

        @Override
        public int updateColumn(int previous) {
            return previous - 1;
        }
    };
    //Iterator when we go north west
    private static final LineIterator NORTH_WEST_ITERATOR = new LineIterator() {
        @Override
        public boolean verify(int row, int column, Ply.Coordinate goal) {
            return goal == null
                    ? row >= 0 && column >= 0
                    : row > goal.getRow() && column > goal.getColumn();
        }

        @Override
        public int updateRow(int previous) {
            return previous - 1;
        }

        @Override
        public int updateColumn(int previous) {
            return previous - 1;
        }
    };

}
