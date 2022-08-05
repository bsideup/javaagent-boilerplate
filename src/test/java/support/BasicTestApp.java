package support;

import feign.Feign;
import feign.RequestLine;
import feign.Response;
import lombok.Getter;
import org.testcontainers.containers.wait.strategy.HttpWaitStrategy;

public class BasicTestApp extends GroovyTestApp<BasicTestApp> {

    @Getter(lazy = true)
    private final Client client = Feign.builder().target(Client.class, getURL());

    public BasicTestApp() {
        super("app.groovy");

        withExposedPorts(4567);
        setWaitStrategy(new HttpWaitStrategy().forPath("/hello/"));
    }

    public interface Client {
        @RequestLine("GET /hello/")
        Response getHello();
    }
}
