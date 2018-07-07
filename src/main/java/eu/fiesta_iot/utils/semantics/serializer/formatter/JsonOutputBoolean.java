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

import java.io.OutputStream;

import org.apache.jena.atlas.io.IndentedWriter;

public class JsonOutputBoolean {
	IndentedWriter out;

	public JsonOutputBoolean(OutputStream outStream) {
		this(new IndentedWriter(outStream));
	}

	public JsonOutputBoolean(IndentedWriter indentedOut) {
		out = indentedOut;
	}

	public void exec(boolean result) {

		out.println("{");
		out.incIndent();
		out.print("\"bool\": " + result);
		out.decIndent();
		out.println("}");
		out.flush();
	}
}
