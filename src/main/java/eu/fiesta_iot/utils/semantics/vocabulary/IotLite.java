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

public class IotLite {

	private static final OntModel model =
	        ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);

	// The namespace of the vocabulary as a string
	public static final String NS_SHORT = "iot-lite";
	public static final String NS =
	        "http://purl.oclc.org/NET/UNIS/fiware/iot-lite#";

	// The namespace of the vocabulary as a Resoruce
	public static final Resource NAMESPACE = model.createResource(NS);

	public static final OntClass Service = model.createClass(NS + "Service");

	public static final Property hasQuantityKind =
	        model.createProperty(NS + "hasQuantityKind");
	public static final Property hasUnit = model.createProperty(NS + "hasUnit");
	public static final Property exposedBy =
	        model.createProperty(NS + "exposedBy");
	public static final Property exposes =
	        model.createProperty(NS + "exposes");
	public static final Property endpoint =
	        model.createProperty(NS + "endpoint");
	public static final Property interfaceType =
	        model.createProperty(NS + "interfaceType");
	public static final Property isSubSystemOf =
	        model.createProperty(NS + "isSubSystemOf");
	public static final Property isMobile =
	        model.createProperty(NS + "isMobile");
}
