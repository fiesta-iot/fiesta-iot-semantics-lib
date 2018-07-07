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

import java.util.List;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Variant;
import javax.ws.rs.core.Variant.VariantListBuilder;

import eu.fiesta_iot.utils.semantics.serializer.exceptions.CannotSerializeException;

public abstract class HttpStreamer {

	protected static MediaType DEFAULT_MEDIA_TYPE = MediaType.TEXT_PLAIN_TYPE;

	protected abstract List<MediaType> fillSupportedMediaTypes();

	protected abstract List<MediaType> listSupportedMediaTypes();

	/**
	 * @deprecated  Has been replaced by Request.selectVariant().
	 */
	@Deprecated
	// To be removed
	// mediaTypes is an ordered list based on q parameter
	public static MediaType
	        getAcceptableMediaType(List<MediaType> mediaTypes,
	                               List<MediaType> supportedMediaTypes) throws CannotSerializeException {
		if (mediaTypes == null || mediaTypes.isEmpty()) {
			return DEFAULT_MEDIA_TYPE;
		}
		
		// Try any of the available MediaType
		for (MediaType m : mediaTypes) {
			// Default Accept
			if (m.isWildcardType() && m.isWildcardSubtype()) {
				return DEFAULT_MEDIA_TYPE;
			} else {
				MediaType mt = MediaType.valueOf(m.getType() + "/" + m.getSubtype());
				if (supportedMediaTypes.stream().anyMatch(mt::equals)) {
					return m;
				}
			}
		}

		throw new CannotSerializeException("Cannot render any of the media types included: "
		                                   + mediaTypes);
	}

	protected static List<Variant>
	        getAvailableVariants(List<MediaType> supportedMediaTypes) {
		MediaType[] types = supportedMediaTypes
		        .toArray(new MediaType[supportedMediaTypes.size()]);
		return VariantListBuilder.newInstance().mediaTypes(types).build();
	}
}
