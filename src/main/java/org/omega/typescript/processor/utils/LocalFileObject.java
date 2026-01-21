package org.omega.typescript.processor.utils;

import org.springframework.util.StreamUtils;

import javax.tools.FileObject;
import java.io.*;
import java.net.URI;
import java.nio.charset.StandardCharsets;

public class LocalFileObject implements FileObject {

    // --------------------- Constants & Fields -------------------

    private final File file;

    // --------------------------- Methods ------------------------


    public LocalFileObject(final File file) {
        this.file = file;
    }

    @Override
    public URI toUri() {
        return file.toURI();
    }

    @Override
    public String getName() {
        return file.getName();
    }

    @Override
    public InputStream openInputStream() throws IOException {
        return new FileInputStream(file);
    }

    @Override
    public OutputStream openOutputStream() throws IOException {
        return new FileOutputStream(file);
    }

    @Override
    public Reader openReader(boolean ignoreEncodingErrors) throws IOException {
        return new FileReader(file);
    }

    @Override
    public CharSequence getCharContent(boolean ignoreEncodingErrors) throws IOException {
        try (InputStream iStream = openInputStream()) {
            return StreamUtils.copyToString(iStream, StandardCharsets.UTF_8);
        }
    }

    @Override
    public Writer openWriter() throws IOException {
        return new FileWriter(file);
    }

    @Override
    public long getLastModified() {
        return file.lastModified();
    }

    @Override
    public boolean delete() {
        return file.delete();
    }


    // ---------------------- Inner Definitions -------------------

}
