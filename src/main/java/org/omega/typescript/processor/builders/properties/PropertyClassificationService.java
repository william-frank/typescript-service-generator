package org.omega.typescript.processor.builders.properties;

import org.omega.typescript.api.TypeScriptIgnore;
import org.omega.typescript.processor.services.ProcessingContext;
import org.omega.typescript.processor.utils.AnnotationUtils;
import org.omega.typescript.processor.utils.ServiceUtils;
import org.omega.typescript.processor.utils.TypeUtils;

import javax.lang.model.AnnotatedConstruct;
import javax.lang.model.type.TypeMirror;
import java.util.List;

import static org.omega.typescript.processor.utils.TypeUtils.isJavaOptionalType;

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
        } else if (isJavaOptionalType(TypeUtils.getClassName(returnType, context))) {
            return false;
        }
        return propertyClassifiers.stream()
                .anyMatch(propertyClassifier -> propertyClassifier.isNotNull(annotatedConstruct, context));
    }

    public boolean shouldIgnoreProperty(final AnnotatedConstruct annotatedConstruct) {
        return AnnotationUtils.getAnnotation(annotatedConstruct, TypeScriptIgnore.class).isPresent() ||
                propertyClassifiers.stream()
                        .anyMatch(propertyClassifier -> propertyClassifier.shouldIgnoreProperty(annotatedConstruct, context));
    }

    // ---------------------- Inner Definitions -------------------

}
