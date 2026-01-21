package org.omega.typescript.processor.builders.properties;

import org.omega.typescript.processor.services.ProcessingContext;
import org.omega.typescript.processor.utils.AnnotationUtils;
import org.omega.typescript.processor.utils.TypeUtils;

import javax.lang.model.AnnotatedConstruct;
import javax.lang.model.element.AnnotationMirror;
import java.util.List;
import java.util.Set;

public class JakartaPropertyClassifier implements PropertyClassifier {

    // --------------------- Constants & Fields -------------------

    private static final Set<String> nonNullAnnotations = Set.of(
            "jakarta.validation.constraints.NotNull",
            "jakarta.validation.constraints.NotEmpty"
    );

    // --------------------------- Methods ------------------------

    public boolean isNotNull(final AnnotatedConstruct annotatedConstruct, final ProcessingContext context) {
        final List<? extends AnnotationMirror> allAnnotations = AnnotationUtils.getAllAnnotations(annotatedConstruct);
        return allAnnotations.stream()
                .anyMatch(annotationMirror ->
                        nonNullAnnotations
                                .contains(
                                        TypeUtils.getClassName(annotationMirror.getAnnotationType(), context)
                                )
                );
    }

    // ---------------------- Inner Definitions -------------------

}
