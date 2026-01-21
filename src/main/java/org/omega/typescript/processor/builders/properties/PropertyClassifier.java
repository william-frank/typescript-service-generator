package org.omega.typescript.processor.builders.properties;

import org.omega.typescript.processor.services.ProcessingContext;

import javax.lang.model.AnnotatedConstruct;

public interface PropertyClassifier {

    boolean isNotNull(AnnotatedConstruct annotatedConstruct, ProcessingContext context);

}
