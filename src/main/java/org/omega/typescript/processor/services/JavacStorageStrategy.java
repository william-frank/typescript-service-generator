package org.omega.typescript.processor.services;

import org.omega.typescript.processor.utils.LogUtil;

import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.File;
import java.net.URI;

/**
 * Created by kibork on 5/1/2020.
 */
public class JavacStorageStrategy extends BaseStorageStrategy {

    // ---------------- Fields & Constants --------------

    // ------------------ Logic      --------------------


    public JavacStorageStrategy(ProcessingContext context, FileNamingStrategy fileNamingStrategy) {
        super(context, fileNamingStrategy);
    }

    @Override
    public FileObject getFile(final String filename) {
        try {
            final FileObject fileObject = context.getProcessingEnv().getFiler()
                    .createResource(StandardLocation.SOURCE_OUTPUT, "", filename);
            final URI uri = fileObject.toUri();
            if (uri.getScheme().equals("file")) {
                final File resultFile = new File(uri);
                if (!resultFile.getParentFile().exists()) {
                    final boolean parentCreated = resultFile.getParentFile().mkdirs();
                    if (!parentCreated) {
                        LogUtil.error(context.getProcessingEnv(), "Unable to create file " + resultFile.getParentFile());
                    }
                }
            }
            return fileObject;
        } catch (Exception e) {
            throw new RuntimeException("Unable to create file for " + filename, e);
        }
    }

}
