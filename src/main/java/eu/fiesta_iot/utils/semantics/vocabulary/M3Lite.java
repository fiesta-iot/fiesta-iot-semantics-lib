/*******************************************************************************
 * Copyright (c) 2018 Jorge Lanza, 
 *                    David Gomez, 
 *                    Luis Sanchez,
 *                    Juan Ramon Santana
 *
 * For the full copyright and license information, please view the LICENSE
 * file that is distributed with this source code.
 *******************************************************************************/
package eu.fiesta_iot.utils.semantics.vocabulary;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;

public class M3Lite {

	private static final Model model =
	        ModelFactory.createDefaultModel();
	
	// The namespace of the vocabulary as a string
	public static final String NS_SHORT = "m3-lite";
	public static final String NS =
	        "http://purl.org/iot/vocab/m3-lite#";

	// The namespace of the vocabulary as a Resoruce
	public static final Resource NAMESPACE = model.createResource(NS);

	public static final Resource createClass(String className) {
		return model.createResource(NS + className);
	}
	
	public static final Resource DomainOfInterest = model.createResource(NS + "DomainOfInterest");

	public static final Property hasDomainOfInterest =
	        model.createProperty(NS + "hasDomainOfInterest");
}
