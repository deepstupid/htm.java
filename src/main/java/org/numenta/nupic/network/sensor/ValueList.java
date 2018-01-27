package org.numenta.nupic.network.sensor;

import org.numenta.nupic.util.Tuple;

/**
 * Contains rows of immutable {@link Tuple}s
 * 
 * @author David Ray
 */
public interface ValueList {
    /** 
     * Returns a collection of values in the form of a {@link Tuple}
     * @return Tuple of values 
     */
    Tuple getRow(int row);
    /**
     * Returns the number of rows.
     * @return the number of rows
     */
    int size();
    
}
