package org.omega.typescript.processor.test;

import org.omega.typescript.api.TypeScriptEndpoint;
import org.omega.typescript.processor.test.dto.GenericClass;
import org.omega.typescript.processor.test.dto.SpecificCompositeDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by kibork on 4/3/2018.
 */
@RestController
@TypeScriptEndpoint(moduleName = "GenericClassController")
@RequestMapping(method = RequestMethod.GET, path = "/api/")
public class GenericClassController {

    // ------------------ Constants  --------------------

    // ------------------ Fields     --------------------

    // ------------------ Properties --------------------

    // ------------------ Logic      --------------------

    @GetMapping("get")
    public GenericClass<SpecificCompositeDto> get() {
        return null;
    }

}
