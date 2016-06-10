/*
 * LTIToolProvider - Classes to handle connections with an LTI 1 compliant tool consumer
 *     Copyright (C) 2013  Stephen P Vickers
 *
 *     This program is free software; you can redistribute it and/or modify
 *     it under the terms of the GNU Lesser General Public License as published by
 *     the Free Software Foundation; either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Lesser General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public License along
 *     with this program; if not, see <http://www.gnu.org/licences/>
 *
 *     Contact: stephen@spvsoftwareproducts.com
 */
package org.tsaap.lti.tp;


/**
 * Interface to represent a callback to the tool provider when processing a valid
 * launch request.
 *
 * @author Stephen P Vickers
 * @version 1.1.01 (18-Jun-13)
 */
public abstract interface Callback {

    /**
     * Method to be called when a launch request has been validated.
     *
     * @param toolProvider the tool provider used to process the request
     */
    public abstract boolean execute(ToolProvider toolProvider);

}
