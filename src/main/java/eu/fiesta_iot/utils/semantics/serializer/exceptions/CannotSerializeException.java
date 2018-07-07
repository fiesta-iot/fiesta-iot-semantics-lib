/*******************************************************************************
 * Copyright (c) 2018 Jorge Lanza, 
 *                    David Gomez, 
 *                    Luis Sanchez,
 *                    Juan Ramon Santana
 *
 * For the full copyright and license information, please view the LICENSE
 * file that is distributed with this source code.
 *******************************************************************************/
package eu.fiesta_iot.utils.semantics.serializer.exceptions;

import java.io.IOException;

import javax.ws.rs.core.MediaType;

/**
 * No available serialization engine for kind of object and mime type.
 * Errors during serialization process reports RiotException.
 * 
 */
public class CannotSerializeException extends Exception {
	public CannotSerializeException() {
		super("Cannot serialize.");
	}

	public CannotSerializeException(IOException e) {
		super(e);
	}

	public CannotSerializeException(MediaType type) {
		super("Cannot serialize media type " + type.toString());
	}

	public CannotSerializeException(Object o, MediaType type) {
		super("Cannot serialize " + o.getClass() + " with media type "
		      + type.toString());
	}

	public CannotSerializeException(String string) {
		super(string);
	}

	public CannotSerializeException(String string, Throwable e) {
		super(string, e);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
