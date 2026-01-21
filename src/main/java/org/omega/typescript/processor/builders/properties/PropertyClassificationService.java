package org.omega.typescript.processor.builders.properties;

import org.omega.typescript.processor.services.ProcessingContext;
import org.omega.typescript.processor.utils.ServiceUtils;

import javax.lang.model.AnnotatedConstruct;
import javax.lang.model.type.TypeMirror;
import java.util.List;

public class PropertyClassificationService {

    // --------------------- Constants & Fields -------------------

    private final List<PropertyClassifier> propertyClassifiers;

    private final ProcessingContext context;

    // --------------------------- Methods ------------------------

    public PropertyClassificationService(final ProcessingContext context) {
        this.context = context;
        this.propertyClassifiers = ServiceUtils.getPropertyLocators(context, PropertyClassifier.class);
    }

    public boolean isNotNull(final AnnotatedConstruct annotatedConstruct, final TypeMirror returnType) {
        if (returnType.getKind().isPrimitive()) {
            return true;
        }
        return propertyClassifiers.stream()
                .anyMatch(propertyClassifier -> propertyClassifier.isNotNull(annotatedConstruct, context));
    }

    // ---------------------- Inner Definitions -------------------

}
