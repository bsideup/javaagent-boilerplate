package support;

import feign.Feign;
import feign.RequestLine;
import feign.Response;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.containers.wait.HttpWaitStrategy;
import org.testcontainers.shaded.com.github.dockerjava.api.command.InspectContainerResponse;

import java.time.Duration;

public class BasicTestApp<SELF extends BasicTestApp<SELF>> extends GenericContainer<SELF> {

    Client client;

    public BasicTestApp(String script) {
        super("zeroturnaround/groovy:2.4.5");

        withClasspathResourceMapping("agent.jar", "/agent.jar", BindMode.READ_ONLY);
        withClasspathResourceMapping(script, "/app/app.groovy", BindMode.READ_ONLY);

        // Cache Grapes
        addFileSystemBind(System.getProperty("user.home") + "/.groovy", "/root/.groovy/", BindMode.READ_WRITE);

        withEnv("JAVA_OPTS",
                new StringBuilder()
                        .append("-javaagent:/agent.jar ")
                        .append("-Dgrape.report.downloads=true -Divy.message.logger.level=2 ")
                        .toString()
        );

        withExposedPorts(4567);
        withCommand("/opt/groovy/bin/groovy /app/app.groovy");

        withLogConsumer(new Slf4jLogConsumer(LoggerFactory.getLogger("BasicTestApp")));

        setWaitStrategy(
                new HttpWaitStrategy()
                        .forPath("/hello/")
                        .withStartupTimeout(Duration.ofMinutes(1))
        );
    }

    public Client getClient() {
        return client;
    }

    @Override
    protected void containerIsStarted(InspectContainerResponse containerInfo) {
        super.containerIsStarted(containerInfo);

        client = Feign.builder().target(Client.class, "http://" + getContainerIpAddress() + ":" + getMappedPort(4567));
    }

    public interface Client {
        @RequestLine("GET /hello/")
        Response getHello();
    }
}
