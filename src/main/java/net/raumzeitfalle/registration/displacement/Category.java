/*-
 * #%L
 * Image-Registration
 * %%
 * Copyright (C) 2019 Oliver Loeffler, Raumzeitfalle.net
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package net.raumzeitfalle.registration.displacement;

import net.raumzeitfalle.registration.firstorder.Alignments;

/**
 * To control which displacements shall be used for alignment or which can be ignore, the {@link Category} enum can be used.
 * Each Displacement has a property which stores the Displacement category. Therefore the {@code getCategory} can be used in Predicates or other ways to filter and sort collections of displacements. 
 * 
 * @author oliver
 *
 */
public enum Category {
	
	/**
	 * This category is and shall be used for positional result calculation and first order result calculation.
	 */
	REG,
	
	/**
	 *  Displacements of this type will be used for correction of translation and rotation for certain {@link Alignments}. Also, in some cases depending on {@link Alignments}, first order result calculation will be limited to this displacement category.  
	 */
	ALIGN,
	
	/**
	 * Like registration, but there may be cases where this mark type can be ignored from evaluations and summries.
	 */
	INFO_ONLY;

	/**
	 * Factory method which produces a {@link Category} from a given {@link String} whereas a null value will produce an instance of {@link Category} REG.
	 * @param value String containing align or info
	 * @return By default REG (also in case of null), in other cases ALIGN (only when value contains &quot;align&quot;) or INFO_ONLY (only when value contains &quot;info&quot;).
	 */
	public static Category fromString(String value) {
		
		if (null == value) {
			return REG;
		}
		
		String description = value.toLowerCase();
		
		if (description.contains("align")) {
			return ALIGN;
		}
		
		if (description.contains("info")) {
			return Category.INFO_ONLY;
		}
		
		return REG;
	}

}
