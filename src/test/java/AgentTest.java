import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Test;
import support.BasicTestApp;

import java.net.HttpURLConnection;
import java.net.URI;

public class AgentTest {

    @ClassRule
    public static BasicTestApp app = new BasicTestApp("app.groovy");

    @Test
    public void testIt() throws Exception {
        URI uri = URI.create(app.getURL() + "/hello/");

        HttpURLConnection connection = (HttpURLConnection) uri.toURL().openConnection();
        try {
            Assert.assertEquals(connection.getHeaderField("X-My-Super-Header"), "header value");
        } finally {
            connection.disconnect();
        }
    }
}
