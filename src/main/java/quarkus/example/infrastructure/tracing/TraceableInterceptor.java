package quarkus.example.infrastructure.tracing;

import quarkus.example.library.UseCaseException;
import quarkus.example.library.DomainException;

import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import java.util.Arrays;
import java.util.TreeMap;

import org.jboss.logging.Logger;

@Interceptor
@Traceable
public class TraceableInterceptor {

    private static final Logger LOGGER = Logger.getLogger(TraceableInterceptor.class);

    @Inject
    Tracer tracer;

    @AroundInvoke
    public Object aroundInvoke(InvocationContext context) throws Exception {
        tracer.trace(new Trace("Test"));

        String useCase = context.getMethod().getDeclaringClass().getSuperclass().getSimpleName();
        Object[] parameters = context.getParameters();

        LOGGER.info(useCase + " requested with values: " + Arrays.toString(parameters));
        Object object;
        try {
            object = context.proceed();
        } catch (UseCaseException | DomainException e) {
            LOGGER.info(useCase + " refused: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            LOGGER.info(useCase + " failed: " + e.getMessage());
            throw e;
        }

        LOGGER.info(useCase + " accomplished with output: " + object);
        return object;
    }

}
