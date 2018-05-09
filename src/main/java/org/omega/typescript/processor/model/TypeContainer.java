package org.omega.typescript.processor.model;

import lombok.Data;

/**
 * Created by kibork on 5/2/2018.
 */
@Data
public class TypeContainer {

    // ------------------ Constants  --------------------

    // ------------------ Fields     --------------------

    private boolean packageElement;

    private String fullName;

    private String shortName;

    private TypeContainer container;

    // ------------------ Properties --------------------

    // ------------------ Logic      --------------------

}