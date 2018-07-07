/*******************************************************************************
 * Copyright (c) 2018 Jorge Lanza, 
 *                    David Gomez, 
 *                    Luis Sanchez,
 *                    Juan Ramon Santana
 *
 * For the full copyright and license information, please view the LICENSE
 * file that is distributed with this source code.
 *******************************************************************************/
package eu.fiesta_iot.utils.semantics.serializer;

import java.io.InputStream;

import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;

import eu.fiesta_iot.utils.semantics.serializer.exceptions.CannotDeserializeException;
import eu.fiesta_iot.utils.semantics.serializer.exceptions.CannotSerializeException;

// Idea from the GitHub project the-open-university/basil

public class SerializerFactory {

	public static final Serializer<? extends Object>
	        getSerializer(Object o) throws CannotSerializeException {
		if (o instanceof Boolean) {
			return new BooleanSerializer((Boolean) o);
		} else if (o instanceof Model) {
			return new ModelSerializer((Model) o);
		} else if (o instanceof ResultSet) {
			return new ResultSetSerializer((ResultSet) o);
		}
		throw new CannotSerializeException();
	}

	public static final Deserializer
	        getDeserializer(InputStream i) throws CannotDeserializeException {
		throw new CannotDeserializeException();
	}
}