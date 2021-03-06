/*
 * Copyright (c) 2018 William Frank (info@williamfrank.net)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.omega.typescript.processor.services;

import lombok.Data;
import org.omega.typescript.processor.GenConfig;
import org.omega.typescript.processor.model.Endpoint;
import org.omega.typescript.processor.model.TypeContainer;
import org.omega.typescript.processor.model.TypeDefinition;
import org.omega.typescript.processor.utils.StringUtils;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by kibork on 5/16/2018.
 */
public class GenConfigBasedNamingStrategy implements FileNamingStrategy {

    // ---------------- Fields & Constants --------------

    @Data
    private static class PathOverride {
        final String path;
        final String override;
    }

    private final ProcessingContext context;

    private final GenConfig genConfig;

    private List<PathOverride> overrides;

    // ------------------ Logic      --------------------


    public GenConfigBasedNamingStrategy(final ProcessingContext execContext) {
        this.context = execContext;
        this.genConfig = execContext.getGenConfig();
        init();
    }

    private void init() {
        if (overrides == null) {
            overrides = genConfig.getPathOverrides().entrySet().stream()
                    .map(e -> new PathOverride(StringUtils.endWith(e.getKey(), "."), StringUtils.endWith(e.getValue(), "/")))
                    .sorted(Comparator.<PathOverride, Integer>comparing(pathOverride -> pathOverride.getPath().length()).reversed())
                    .collect(Collectors.toList());
        }
    }

    private String getTargetFolder(final String containerPackage) {
        final Optional<PathOverride> override = overrides.stream()
                .filter(p -> StringUtils.endWith(containerPackage, ".").startsWith(p.getPath()))
                .findFirst();

        String targetDir = containerPackage.replace(".", "/");
        if (override.isPresent()) {
            targetDir = addPaths(override.get().getOverride(), targetDir.substring(override.get().getPath().length() - 1));
        }
        final String result = StringUtils.endWith(genConfig.getOutputFolder(), "/") + StringUtils.endWith(targetDir, "/");
        return result.replaceAll("//", "/");
    }

    private String addPaths(String base, String subPath) {
        base = StringUtils.endWith(base, "/");
        while ((!subPath.isBlank()) && (subPath.startsWith("/"))) {
            subPath = subPath.substring(1);
        }
        return base + subPath;
    }

    private String getFullName(final String containerPackage, final String fileName) {
        return getTargetFolder(containerPackage) + fileName;
    }

    private String getSimpleName(final TypeContainer typeContainer, final String elementName) {
        final StringBuilder prefix = new StringBuilder();
        TypeContainer container = typeContainer;
        while ((container != null) && (!container.isPackageElement())) {
            prefix.insert(0, container.getShortName() + "$");
            container = container.getContainer();
        }
        return prefix + elementName + genConfig.getGeneratedFilesSuffix();
    }

    private String getTypeFileBase(final TypeDefinition definition){
        return getFullName(definition.getContainer().getPackageName(), getSimpleName(definition.getContainer(), definition.getShortName()));
    }

    private String getRelativeFileName(final TypeDefinition to, final String fromPathStr, final String toPathStr) {
        final String path = getRelativePath(fromPathStr, toPathStr);

        final String fileName = getSimpleName(to.getContainer(), to.getShortName());
        return path + fileName;
    }

    private String getRelativePath(String fromPathStr, String toPathStr) {
        final Path fromPath = Paths.get(fromPathStr);
        final Path toPath = Paths.get(toPathStr);
        final String relativePath = StringUtils.endWith(
                fromPath.relativize(toPath).toString()
                .replace("\\", "/"), "/");
        if (relativePath.startsWith(".")) {
            return relativePath;
        } else if (StringUtils.hasText(relativePath) && (!"/".contentEquals(relativePath))) {
            return "./" + relativePath;
        }
        return "./";
    }

    public String getEndpointBaseName(final Endpoint endpoint) {
        return getTargetFolder(endpoint.getContainer().getPackageName())
                + getSimpleName(endpoint.getContainer(), endpoint.getControllerName());
    }

    @Override
    public String getGetFullFileName(final Endpoint endpoint) {
        return getEndpointBaseName(endpoint) + ".ts";
    }

    @Override
    public String getFullFileName(final String fileName) {
        return StringUtils.endWith(genConfig.getOutputFolder(), "/") + fileName;
    }

    @Override
    public String getFullTypeFileName(final TypeDefinition definition) {
        return StringUtils.endWith(getTypeFileBase(definition), ".ts");
    }

    @Override
    public String getFullModuleName(final String moduleName) {
        return getFullFileName(StringUtils.endWith(moduleName, ".module.ts"));
    }

    @Override
    public String getRelativeFileName(final Endpoint endpoint, final TypeDefinition to) {
        final String toTargetFolder = getTargetFolder(to.getContainer().getPackageName());
        return getRelativeFileName(to,
                getTargetFolder(endpoint.getContainer().getPackageName()),
                toTargetFolder
        );
    }

    @Override
    public String getRelativeFileName(final Endpoint endpoint, final String toFile) {
        final String path = getRelativePath(getTargetFolder(endpoint.getContainer().getPackageName()), genConfig.getOutputFolder() + toFile);
        if (path.endsWith("/")) {
            return path.substring(0, path.length() - 1);
        }
        return path;
    }

    @Override
    public String getRelativeFileName(final TypeDefinition from, final TypeDefinition to) {
        final String fromPathStr = getTargetFolder(from.getContainer().getPackageName());
        final String toPathStr = getTargetFolder(to.getContainer().getPackageName());
        return getRelativeFileName(to, fromPathStr, toPathStr);
    }

    @Override
    public String getIncludeFileName(final Endpoint endpoint) {
        final String endpointPath = getEndpointBaseName(endpoint).substring(genConfig.getOutputFolder().length());
        return "." + (endpointPath.startsWith("/") ? "" : "/") + endpointPath;
    }
}
