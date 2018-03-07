package org.omega.typescript.processor.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kibork on 2/2/2018.
 */
@Data
public class EndpointMethod {

    // ------------------ Constants  --------------------

    // ------------------ Fields     --------------------

    private Endpoint endpoint;

    private String methodName;

    private String urlTemplate;

    private RequestMethod requestMethod;

    private TypeDefinition returnType;

    private List<MethodParameter> params = new ArrayList<>();

    // ------------------ Properties --------------------

    // ------------------ Logic      --------------------

}
