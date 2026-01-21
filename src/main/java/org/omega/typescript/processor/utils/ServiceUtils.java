package org.omega.typescript.processor.utils;

import org.omega.typescript.processor.services.ProcessingContext;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.ServiceLoader;

public class ServiceUtils {

    // --------------------- Constants & Fields -------------------

    private static boolean printedServiceWarning = false;

    // --------------------------- Methods ------------------------

    private ServiceUtils() {
    }

    public static <ServiceInterface> List<ServiceInterface>
    getPropertyLocators(final ProcessingContext context, final Class<ServiceInterface> serviceClass) {
        try {
            return loadServiceWithLoader(serviceClass);
        } catch (Exception e) {
            if (!printedServiceWarning) {
                LogUtil.warning(context.getProcessingEnv(), "Unable to use service interface. Try using resource loader.");
                printedServiceWarning = true;
            }
        }
        //Well, load the services at least manually
        try {
            return loadServiceFromResourceFile(context, serviceClass);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        }
    }

    private static <ServiceInterface> List<ServiceInterface>
    loadServiceWithLoader(final Class<ServiceInterface> serviceClass) {
        final ServiceLoader<ServiceInterface> serviceLocator = ReflectionUtils
                .callOnClass(ServiceLoader.class, null, "load",
                        new Class<?>[]{ Class.class, ClassLoader.class, Module.class },
                        new Object[]{ serviceClass, serviceClass.getClassLoader(), serviceClass.getModule() }
                );
        final List<ServiceInterface> services = serviceLocator
                .stream()
                .map(ServiceLoader.Provider::get)
                .toList();

        return services;
    }

    @SuppressWarnings("unchecked")
    private static <ServiceInterface> List<ServiceInterface>
    loadServiceFromResourceFile(final ProcessingContext context, final Class<ServiceInterface> serviceClass) {
        final String content = IOUtils
                .requireClasspathResource("META-INF/services/" + serviceClass.getName(), context);
        return Arrays.stream(content.split("\n"))
                .map(String::trim)
                .filter(StringUtils::hasText)
                .filter(s -> !s.startsWith("#"))
                .map(className -> (ServiceInterface) createServiceInstance(className, context))
                .filter(Objects::nonNull)
                .toList();
    }

    @SuppressWarnings("unchecked")
    private static <ServiceInterface> ServiceInterface
    createServiceInstance(final String className, final ProcessingContext context) {
        try {
            final Object instance = Class.forName(className)
                    .getDeclaredConstructor()
                    .newInstance();
            return (ServiceInterface) instance;
        } catch (Exception e) {
            context.warning(
                    "Failed to instantiate service class: " + className + " due to Exception\n" +
                            StringUtils.exceptionToString(e)
            );
            return null;
        }
    }

    // ---------------------- Inner Definitions -------------------

}
