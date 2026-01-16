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

import javax.lang.model.element.*;
import org.omega.typescript.api.TypeScriptIgnore;
import org.omega.typescript.processor.model.PropertyDefinition;
import org.omega.typescript.processor.services.ProcessingContext;
import org.omega.typescript.processor.utils.AnnotationUtils;
import org.omega.typescript.processor.utils.TypeUtils;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by kibork on 1/16/2026.
 */
public class JavaRecordPropertyLocator implements TypePropertyLocator {
    
    // ---------------- Fields & Constants --------------
    
    // ------------------ Properties --------------------
    
    // ------------------ Logic      --------------------
    
    @Override
    public List<PropertyDefinition> locateProperties(final TypeElement typeElement, final ProcessingContext context) {
        final List<? extends RecordComponentElement> recordComponents = typeElement.getRecordComponents();
        
        return recordComponents.stream()
            .filter(e -> AnnotationUtils.getAnnotation(e, TypeScriptIgnore.class).isEmpty())
            .map(recordComponent ->
                PropertyHelpers
                .buildProperty(
                    recordComponent.getAccessor(),
                    buildPropertyName(recordComponent, context),
                    recordComponent.asType(),
                    context
                )
            )
            .toList();
        
    }
    
    private String buildPropertyName(final RecordComponentElement recordComponent, ProcessingContext context) {
        return recordComponent.getSimpleName().toString();
    }
    
    
}
