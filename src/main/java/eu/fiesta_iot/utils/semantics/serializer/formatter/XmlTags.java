/*******************************************************************************
 * Copyright (c) 2018 Jorge Lanza, 
 *                    David Gomez, 
 *                    Luis Sanchez,
 *                    Juan Ramon Santana
 *
 * For the full copyright and license information, please view the LICENSE
 * file that is distributed with this source code.
 *******************************************************************************/
package eu.fiesta_iot.utils.semantics.serializer.formatter;

public interface XmlTags {
	
	public static final String ROOT_TAG = "data";
	public static final String NAMESPACE =
	        "http://purl.org/iot/ontology/fiesta-iot#";
	public static final String VARS_TAG = "vars";
	public static final String VAR_TAG = "var";
	public static final String ITEMS_TAG = "items";
	public static final String ITEM_TAG = "item";
}
