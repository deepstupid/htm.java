/* ---------------------------------------------------------------------
 * Numenta Platform for Intelligent Computing (NuPIC)
 * Copyright (C) 2014, Numenta, Inc.  Unless you have an agreement
 * with Numenta, Inc., for a separate license for this software code, the
 * following terms and conditions apply:
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero Public License version 3 as
 * published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero Public License for more details.
 *
 * You should have received a copy of the GNU Affero Public License
 * along with this program.  If not, see http://www.gnu.org/licenses.
 *
 * http://numenta.org/licenses/
 * ---------------------------------------------------------------------
 */

package org.numenta.nupic.model;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * Software implementation of a neuron in the neocortical region.
 *
 * @author Chetan Surpur
 * @author David Ray
 */
public class Cell implements Comparable<Cell>, Serializable {


    /**
     * This cell's index
     */
    public final int index;

    /**
     * The owning {@link Column}
     */
    public final Column column;
    /**
     * Cash this because Cells are immutable
     */
    private final int hash;


    /**
     * Constructs a new {@code Cell} object
     *
     * @param column the containing {@link Column}
     * @param colSeq this index of this {@code Cell} within its column
     */
    public Cell(Column column, int colSeq) {
        this.column = column;
        this.index = column.index * column.cellCount + colSeq;
        this.hash = 31 * (1+index);
    }

    /**
     * Returns the Set of {@link Synapse}s which have this cell
     * as their source cells.
     *
     * @param c the connections state of the temporal memory
     *          return an orphaned empty set.
     * @return the Set of {@link Synapse}s which have this cell
     * as their source cells.
     */
    public Set<Synapse> getReceptorSynapses(Connections c) {
        return getReceptorSynapses(c, false);
    }

    /**
     * Returns the Set of {@link Synapse}s which have this cell
     * as their source cells.
     *
     * @param c            the connections state of the temporal memory
     * @param doLazyCreate create a container for future use if true, if false
     *                     return an orphaned empty set.
     * @return the Set of {@link Synapse}s which have this cell
     * as their source cells.
     */
    public Set<Synapse> getReceptorSynapses(Connections c, boolean doLazyCreate) {
        return c.getReceptorSynapses(this, doLazyCreate);
    }

    /**
     * Returns a {@link List} of this {@code Cell}'s {@link DistalDendrite}s
     *
     * @param c            the connections state of the temporal memory
     * @param doLazyCreate create a container for future use if true, if false
     *                     return an orphaned empty set.
     * @return a {@link List} of this {@code Cell}'s {@link DistalDendrite}s
     */
    public List<DistalDendrite> getSegments(Connections c) {
        return getSegments(c, false);
    }

    /**
     * Returns a {@link List} of this {@code Cell}'s {@link DistalDendrite}s
     *
     * @param c            the connections state of the temporal memory
     * @param doLazyCreate create a container for future use if true, if false
     *                     return an orphaned empty set.
     * @return a {@link List} of this {@code Cell}'s {@link DistalDendrite}s
     */
    public List<DistalDendrite> getSegments(Connections c, boolean doLazyCreate) {
        return c.getSegments(this, doLazyCreate);
    }

    /**
     * {@inheritDoc}
     */
    public String toString() {
        return String.valueOf(index);
    }

    /**
     * {@inheritDoc}
     * <p>
     * <em> Note: All comparisons use the cell's index only </em>
     */
    @Override
    public int compareTo(Cell arg0) {
        return Integer.compare(index, arg0.index);
    }

    @Override
    public int hashCode() {

        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Cell other = (Cell) obj;
        return index == other.index;
    }

}
