package org.omega.typescript.processor.test.dto;

import org.omega.typescript.api.TypeScriptIgnore;
import org.omega.typescript.api.TypeScriptName;

/**
 * Created by kibork on 4/3/2018.
 */
public record SimpleRecord(String field1, long field2, @TypeScriptName("customName") Integer field3) {


    // ------------------ Logic      --------------------

}
