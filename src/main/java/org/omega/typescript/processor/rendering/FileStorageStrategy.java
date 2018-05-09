package org.omega.typescript.processor.rendering;

import org.omega.typescript.processor.ProcessingContext;
import org.omega.typescript.processor.model.Endpoint;
import org.omega.typescript.processor.model.TypeContainer;
import org.omega.typescript.processor.model.TypeDefinition;

import java.io.*;

/**
 * Created by kibork on 5/2/2018.
 */
public class FileStorageStrategy implements StorageStrategy {

    // ------------------ Constants  --------------------

    // ------------------ Fields     --------------------

    private final ProcessingContext context;

    // ------------------ Properties --------------------

    // ------------------ Logic      --------------------


    public FileStorageStrategy(final ProcessingContext context) {
        this.context = context;
    }

    @Override
    public PrintWriter createWriter(final TypeDefinition definition) throws IOException {
        return createWriter(getFileName(definition));
    }

    @Override
    public PrintWriter createWriter(final String filename) throws IOException {
        final File targetFile = getFile(filename);
        return new PrintWriter(new FileWriter(targetFile, false));
    }

    @Override
    public File getFile(final String filename) {
        final File targetFile = new File("gen/" + filename).getAbsoluteFile();
        if (targetFile.exists()) {
            final boolean result = targetFile.delete();
            if (!result) {
                context.error("Failed to delete file " + targetFile);
            }
        } else if (!targetFile.getParentFile().exists()) {
            final boolean result = targetFile.getParentFile().mkdirs();
            if (!result) {
                context.error("Failed to create containing folder " + targetFile.getParentFile());
            }
        }
        return targetFile;
    }

    @Override
    public String getFileName(final TypeDefinition definition) {
        return getName(definition) + ".ts";
    }

    private String getName(final TypeDefinition definition) {
        final StringBuilder prefix = new StringBuilder();
        TypeContainer container = definition.getContainer();
        while ((container != null) && (!container.isPackageElement())) {
            prefix.insert(0, container.getShortName() + "$");
            container = container.getContainer();
        }
        return prefix + definition.getShortName() + ".generated";
    }

    @Override
    public String getRelativeFileName(final TypeDefinition from, final TypeDefinition to) {
        return "./" + getName(to);
    }

    @Override
    public String getIncludeFileName(final Endpoint endpoint) {
        return "./" + getName(endpoint);
    }

    @Override
    public String getRelativeFileName(final Endpoint endpoint, final TypeDefinition to) {
        return "./" + getName(to);
    }

    @Override
    public String getFileName(final Endpoint endpoint) {
        return getName(endpoint) + ".ts";
    }

    private String getName(final Endpoint endpoint) {
        return endpoint.getControllerName() + ".generated";
    }

    @Override
    public PrintWriter createWriter(final Endpoint endpoint) throws IOException {
        return createWriter(getFileName(endpoint));
    }

}