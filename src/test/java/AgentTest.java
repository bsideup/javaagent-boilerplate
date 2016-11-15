import feign.Response;
import org.junit.ClassRule;
import org.junit.Test;
import support.BasicTestApp;

import static org.assertj.core.api.Assertions.assertThat;

public class AgentTest {

    @ClassRule
    public static BasicTestApp app = new BasicTestApp("app.groovy");

    @Test
    public void testIt() throws Exception {
        Response response = app.getClient().getHello();

        assertThat(response.headers().get("X-My-Super-Header"))
                .isNotNull()
                .hasSize(1)
                .containsExactly("header value");
    }
}
