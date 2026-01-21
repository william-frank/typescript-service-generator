/*
 * Copyright (c) 2018-2026 William Frank (info@williamfrank.net)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.omega.typescript.processor.builders.properties;

import org.omega.typescript.processor.model.PropertyDefinition;
import org.omega.typescript.processor.services.ProcessingContext;
import org.omega.typescript.processor.utils.ServiceUtils;

import javax.lang.model.element.TypeElement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by kibork on 4/3/2018.
 */
public class PropertyDefinitionBuilder {

    // ------------------ Constants  --------------------

    // ------------------ Fields     --------------------

    private final ProcessingContext context;

    private final Iterable<TypePropertyLocator> propertyLocators;

    private final PropertyClassificationService propertyClassificationService;

    // ------------------ Properties --------------------

    // ------------------ Logic      --------------------


    public PropertyDefinitionBuilder(final ProcessingContext context) {
        this.context = context;
        this.propertyLocators = ServiceUtils.getPropertyLocators(context, TypePropertyLocator.class);
        this.propertyClassificationService = new PropertyClassificationService(context);
    }

    public List<PropertyDefinition> buildProperties(final TypeElement typeElement) {
        final Map<String, PropertyDefinition> properties = new LinkedHashMap<>();
        for (final TypePropertyLocator locator : propertyLocators) {
            final List<PropertyDefinition> propertyDefinitions = locator
                    .locateProperties(typeElement, context, propertyClassificationService);
            propertyDefinitions.forEach(p -> properties.putIfAbsent(p.getName(), p));
        }
        return new ArrayList<>(properties.values());
    }

}
