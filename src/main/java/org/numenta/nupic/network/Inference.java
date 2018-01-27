/* ---------------------------------------------------------------------
 * Numenta Platform for Intelligent Computing (NuPIC)
 * Copyright (C) 2016, Numenta, Inc.  Unless you have an agreement
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
package org.numenta.nupic.network;

import java.util.Map;
import java.util.Set;

import org.numenta.nupic.algorithms.CLAClassifier;
import org.numenta.nupic.algorithms.Classification;
import org.numenta.nupic.algorithms.SpatialPooler;
import org.numenta.nupic.algorithms.TemporalMemory;
import org.numenta.nupic.encoders.Encoder;
import org.numenta.nupic.model.Cell;
import org.numenta.nupic.model.ComputeCycle;
import org.numenta.nupic.model.Persistable;
import org.numenta.nupic.util.NamedTuple;

import rx.functions.Func1;

/**
 * Container for output from a given {@link Layer}. Represents the
 * result accumulated by the computation of a sequence of algorithms
 * contained in a given Layer and contains information needed at 
 * various stages in the sequence of calculations a Layer may contain.
 * 
 * @author David Ray
 */
public interface Inference extends Persistable {
    /**
     * Returns the input record sequence number associated with 
     * the state of a {@link Layer} which this {@code Inference}
     * represents.
     * 
     * @return
     */
    int getRecordNum();
    /**
     * Returns the {@link ComputeCycle}
     * @return
     */
    ComputeCycle getComputeCycle();
    /**
     * Returns a custom Object during sequence processing where one or more 
     * {@link Func1}(s) were added to a {@link Layer} in between algorithmic
     * components.
     *  
     * @return  the custom object set during processing
     */
    Object getCustomObject();
    /**
     * Returns the {@link Map} used as input into a given {@link CLAClassifier}
     * if it exists.
     * 
     * @return
     */
    Map<String, NamedTuple> getClassifierInput();
    /**
     * Returns a tuple containing key/value pairings of input field
     * names to the {@link CLAClassifier} used in conjunction with it.
     * 
     * @return
     */
    NamedTuple getClassifiers();
    /**
     * Returns the object used as input into a given Layer
     * which is associated with this computation result.
     * @return
     */
    Object getLayerInput();

    /**
     * Returns the <em>Sparse Distributed Representation</em> vector
     * which is the result of all algorithms in a series of algorithms
     * passed up the hierarchy.
     * 
     * @return
     */
    int[] getSDR();
    /**
     * Returns the initial encoding produced by an {@link Encoder} or one
     * of its subtypes.
     * 
     * @return
     */
    int[] getEncoding();
    /**
     * Returns the most recent {@link Classification}
     * 
     * @param fieldName
     * @return
     */
    Classification<Object> getClassification(String fieldName);
    /**
     * Returns the most recent anomaly calculation.
     * @return
     */
    double getAnomalyScore();
    /**
     * Returns the column activation from a {@link SpatialPooler}
     * @return
     */
    int[] getFeedForwardActiveColumns();
    /**
     * Returns the column activations in sparse form
     * @return
     */
    int[] getFeedForwardSparseActives();
    /**
     * Returns the column activation from a {@link TemporalMemory}
     * @return
     */
    Set<Cell> getActiveCells();
    /**
     * Returns the predicted output from the last inference cycle.
     * @return
     */
    Set<Cell> getPreviousPredictiveCells();
    /**
     * Returns the currently predicted columns.
     * @return
     */
    Set<Cell> getPredictiveCells();
}
