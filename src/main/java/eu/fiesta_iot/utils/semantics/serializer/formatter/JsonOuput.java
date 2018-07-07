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

import org.apache.jena.query.ResultSet;

import org.apache.jena.sparql.resultset.ResultSetApply;

public class JsonOuput {

    public void format(OutputStream out, ResultSet resultSet) {
        // Use direct string output - more control
        JsonOutputResultSet jsonOut =  new JsonOutputResultSet(out) ;
        ResultSetApply a = new ResultSetApply(resultSet, jsonOut) ;
        a.apply() ;
    }

    public void format(OutputStream out, boolean booleanResult)  {
        JsonOutputBoolean jsonOut = new JsonOutputBoolean(out) ;
        jsonOut.exec(booleanResult) ;
    }
    

}
