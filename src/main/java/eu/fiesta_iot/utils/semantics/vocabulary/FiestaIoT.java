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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class FiestaIoT {

	public static final Map<String, String> PREFIX_MAP;
	
    static {
        Map<String, String> map = new HashMap<>(6);
        map.put(Dul.NS_SHORT, Dul.NS);
        map.put(Geo.NS_SHORT, Geo.NS);
        map.put(IotLite.NS_SHORT, IotLite.NS);
        map.put(M3Lite.NS_SHORT, M3Lite.NS);
        map.put(Ssn.NS_SHORT, Ssn.NS);
        map.put(Time.NS_SHORT, Time.NS);
        PREFIX_MAP = Collections.unmodifiableMap(map);
    }
	
	
}
