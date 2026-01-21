package org.omega.typescript.processor.test;

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
@TypeScriptEndpoint(moduleName = "SimpleRecordController")
@RequestMapping(method = RequestMethod.GET, path = "/api/")
public class SimpleRecordController {

    // ------------------ Constants  --------------------

    // ------------------ Fields     --------------------

    // ------------------ Properties --------------------

    // ------------------ Logic      --------------------

    @GetMapping("get")
    public SimpleRecord getSimpleRecord() {
        return null;
    }

}
