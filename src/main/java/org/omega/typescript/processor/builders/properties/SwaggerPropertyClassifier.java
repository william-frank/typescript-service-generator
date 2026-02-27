package org.omega.typescript.processor.builders.properties;

import org.omega.typescript.processor.services.ProcessingContext;
import org.omega.typescript.processor.utils.AnnotationUtils;
import org.omega.typescript.processor.utils.TypeUtils;

import javax.lang.model.AnnotatedConstruct;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import java.util.List;
import java.util.Objects;

public class SwaggerPropertyClassifier implements PropertyClassifier {

    // --------------------- Constants & Fields -------------------

    private static final String schemaAnnotation = "io.swagger.v3.oas.annotations.media.Schema";

    // --------------------------- Methods ------------------------

    public boolean isNotNull(final AnnotatedConstruct annotatedConstruct, final ProcessingContext context) {
        final List<? extends AnnotationMirror> allAnnotations = AnnotationUtils.getAllAnnotations(annotatedConstruct);
        return allAnnotations.stream()
                .filter(annotationMirror ->
                        Objects.equals(schemaAnnotation, TypeUtils.getClassName(annotationMirror.getAnnotationType(), context))
                )
                .anyMatch(schema ->
                        Objects.equals("REQUIRED",
                                AnnotationUtils.getValue(schema, "requiredMode", context)
                                        .map(AnnotationValue::toString)
                                        .orElse("")
                        )
                );
    }

    @Override
    public boolean shouldIgnoreProperty(final AnnotatedConstruct annotatedConstruct, final ProcessingContext context) {
        return false;
    }

    // ---------------------- Inner Definitions -------------------

}
