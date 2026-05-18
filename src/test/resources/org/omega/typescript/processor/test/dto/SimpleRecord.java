package org.omega.typescript.processor.test.dto;

import org.omega.typescript.api.TypeScriptIgnore;
import org.omega.typescript.api.TypeScriptName;

import jakarta.validation.constraints.NotNull;

import java.util.Optional;

/**
 * Created by kibork on 4/3/2018.
 */
public record SimpleRecord(String field1,
                           long field2,
                           @TypeScriptName("customName") Integer field3,
                           @NotNull Long field4,
                           @NotNull Optional<String> field5
) {

    // ------------------ Logic      --------------------

}
