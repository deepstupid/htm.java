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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.numenta.nupic.algorithms.SpatialPooler;

/**
 * Abstraction of both an input bit and a columnal collection of
 * {@link Cell}s which have behavior associated with membership to
 * a given {@code Column}
 * 
 * @author Chetan Surpur
 * @author David Ray
 *
 */
public class Column implements Comparable<Column>, Serializable {

    
    /** The flat non-topological index of this column */
    public final int index;

    /** Configuration of cell count */
    public final int cellCount;
    /** Connects {@link SpatialPooler} input pools */
    private final ProximalDendrite proximalDendrite;

    public final Cell[] cells;

    private final int hashcode;

    /**
     * Constructs a new {@code Column}
     * 
     * @param cellCount      number of cells per column
     * @param index         the index of this column
     */
    public Column(int cellCount, int index) {
        this.cellCount = cellCount;
        this.index = index;
        this.hashcode = hashCode();
        cells = new Cell[cellCount];
        for(int i = 0; i < cellCount; i++)
            cells[i] = new Cell(this, i);

        proximalDendrite = new ProximalDendrite(index);
    }

    /**
     * Returns the {@link Cell} residing at the specified index.
     * <p>
     * <b>IMPORTANT NOTE:</b> the index provided is the index of the Cell within this
     * column and is <b>not</b> the actual index of the Cell within the total
     * list of Cells of all columns. Each Cell maintains it's own <i><b>GLOBAL</i></b>
     * index which is the index describing the occurrence of a cell within the
     * total list of all cells. Thus, {@link Cell#getIndex()} returns the <i><b>GLOBAL</i></b>
     * index and <b>not</b> the index within this column.
     * 
     * @param index     the index of the {@link Cell} to return.
     * @return          the {@link Cell} residing at the specified index.
     */
    public Cell getCell(int index) {
        return cells[index];
    }

    /**
     * Returns the {@link Cell} with the least number of {@link DistalDendrite}s.
     * 
     * @param c         the connections state of the temporal memory
     * @param random
     * @return
     */
    public Cell getLeastUsedCell(Connections c, Random random) {
        List<Cell> leastUsedCells = new ArrayList<>();
        int minNumSegments = Integer.MAX_VALUE;

        for(Cell cell : cells) {
            int numSegments = cell.getSegments(c).size();

            if(numSegments < minNumSegments) {
                minNumSegments = numSegments;
                leastUsedCells.clear();
            }

            if(numSegments == minNumSegments) {
                leastUsedCells.add(cell);
            }
        }
        int index = random.nextInt(leastUsedCells.size());
        Collections.sort(leastUsedCells);
        return leastUsedCells.get(index); 
    }

    /**
     * Returns this {@code Column}'s single {@link ProximalDendrite}
     * @return
     */
    public ProximalDendrite getProximalDendrite() {
        return proximalDendrite;
    }

    /**
     * Delegates the potential synapse creation to the one {@link ProximalDendrite}.
     * 
     * @param c						the {@link Connections} memory
     * @param inputVectorIndexes	indexes specifying the input vector bit
     */
    public Pool createPotentialPool(Connections c, int[] inputVectorIndexes) {
        return proximalDendrite.createPool(c, inputVectorIndexes);
    }

    /**
     * Sets the permanences on the {@link ProximalDendrite} {@link Synapse}s
     * 
     * @param c				the {@link Connections} memory object
     * @param permanences	floating point degree of connectedness
     */
    public void setProximalPermanences(Connections c, double[] permanences) {
        proximalDendrite.setPermanences(c, permanences);
    }

    /**
     * Sets the permanences on the {@link ProximalDendrite} {@link Synapse}s
     * 
     * @param c				the {@link Connections} memory object
     * @param permanences	floating point degree of connectedness
     */
    public void setProximalPermanencesSparse(Connections c, double[] permanences, int[] indexes) {
        proximalDendrite.setPermanences(c, permanences, indexes);
    }

    /**
     * Delegates the call to set synapse connected indexes to this 
     * {@code Column}'s {@link ProximalDendrite}
     * @param c
     * @param connections
     */
    public void setProximalConnectedSynapsesForTest(Connections c, int[] connections) {
        proximalDendrite.setConnectedSynapsesForTest(c, connections);
    }

    /**
     * {@inheritDoc}
     */
    public String toString() {
        return String.valueOf(index);
    }

    /**
     * {@inheritDoc}
     * @param otherColumn     the {@code Column} to compare to
     * @return
     */
    @Override
    public int compareTo(Column otherColumn) {
        return Integer.compare(index, otherColumn.index);
    }

    @Override
    public int hashCode() {
        if(hashcode == 0) {
            final int prime = 31;
            int result = 1;
            result = prime * result + index;
            return result;
        }
        return hashcode;
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj)
            return true;
        if(obj == null)
            return false;
        if(getClass() != obj.getClass())
            return false;
        Column other = (Column)obj;
        return index == other.index;
    }
}
