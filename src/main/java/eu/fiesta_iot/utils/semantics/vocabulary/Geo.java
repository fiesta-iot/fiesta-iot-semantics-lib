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

import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;

public class Geo {

	private static final OntModel model =
	        ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
	
	// The namespace of the vocabulary as a string
	public static final String NS_SHORT = "geo";
	public static final String NS =
	        "http://www.w3.org/2003/01/geo/wgs84_pos#";

	// The namespace of the vocabulary as a Resoruce
	public static final Resource NAMESPACE = model.createResource(NS);

	public static final OntClass Point = model.createClass(NS + "Point");

	public static final Property location =
	        model.createProperty(NS + "location");
	public static final Property longitude = model.createProperty(NS + "long");
	public static final Property latitude =
	        model.createProperty(NS + "lat");
}
