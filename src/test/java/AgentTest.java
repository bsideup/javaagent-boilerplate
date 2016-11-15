import feign.Response;
import org.junit.ClassRule;
import org.junit.Test;
import support.BasicTestApp;

import static org.assertj.core.api.Assertions.assertThat;

public class AgentTest {

    @ClassRule
    public static BasicTestApp app = new BasicTestApp();

    @Test
    public void testIt() throws Exception {
        Response response = app.getClient().getHello();

        System.out.println("Got response:\n" + response);

        assertThat(response.headers().get("X-My-Super-Header"))
                .isNotNull()
                .hasSize(1)
                .containsExactly("header value");
    }
}
