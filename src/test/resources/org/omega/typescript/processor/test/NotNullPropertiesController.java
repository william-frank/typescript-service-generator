package org.omega.typescript.processor.test;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import org.omega.typescript.api.TypeScriptEndpoint;
import org.omega.typescript.processor.test.dto.SimpleRecord;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by kibork on 1/15/2026.
 */
@RestController
@TypeScriptEndpoint(moduleName = "NotNullPropertiesController")
@RequestMapping(method = RequestMethod.GET, path = "/api/")
public class NotNullPropertiesController {

    // ------------------ Constants  --------------------

    // ------------------ Fields     --------------------

    // ------------------ Properties --------------------

    // ------------------ Logic      --------------------

    @GetMapping("get")
    public TestNotNullRecord getTestNotNullRecord() {
        return null;
    }

    public record TestNotNullRecord(String field1,
                                    long field2,
                                    Long field3,
                                    @NotNull Long field4,
                                    @Schema(requiredMode = Schema.RequiredMode.NOT_REQUIRED) Long field5,
                                    @Schema(requiredMode = Schema.RequiredMode.REQUIRED) Long field6) { }

}
