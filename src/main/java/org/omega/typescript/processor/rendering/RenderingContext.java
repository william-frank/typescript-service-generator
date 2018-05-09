package org.omega.typescript.processor.rendering;

import lombok.Getter;
import org.omega.typescript.processor.LogUtil;
import org.omega.typescript.processor.ProcessingContext;

/**
 * Created by kibork on 5/2/2018.
 */
@Getter
public class RenderingContext {

    // ------------------ Constants  --------------------

    // ------------------ Fields     --------------------

    private final ProcessingContext processingContext;

    private final StorageStrategy storageStrategy;

    private final TypeInstanceRenderer instanceRenderer;

    // ------------------ Properties --------------------


    // ------------------ Logic      --------------------


    public RenderingContext(final ProcessingContext processingContext, final StorageStrategy storageStrategy) {
        this.processingContext = processingContext;
        this.storageStrategy = storageStrategy;
        this.instanceRenderer = new TypeInstanceRenderer(this);
    }

    public void debug(final String msg) {
        LogUtil.debug(processingContext.getProcessingEnv(), msg);
    }

    public void warning(final String msg) {
        LogUtil.warning(processingContext.getProcessingEnv(), msg);
    }

    public void error(final String msg) {
        LogUtil.error(processingContext.getProcessingEnv(), msg);
    }

}