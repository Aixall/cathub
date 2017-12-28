package cathub;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ronin.muserver.ContentTypes;
import ronin.muserver.Method;
import ronin.muserver.MuServer;
import ronin.muserver.handlers.ResourceHandler;

import java.time.Instant;
import java.util.Map;

import static ronin.muserver.MuServerBuilder.httpServer;

public class App {
    public static final Logger log = LoggerFactory.getLogger(App.class);
    public static void main(String[] args) throws Exception {
        Map<String, String> settings = System.getenv();
        String appName = settings.getOrDefault("APP_NAME", "cathub");
        log.info("Starting " + appName);

        MuServer server = httpServer()
            .withHttpConnection(Integer.parseInt(settings.getOrDefault("APP_PORT", String.valueOf(8710))))
            .addHandler(Method.GET, "/cathub/current-time", (request, response) -> {
                response.contentType(ContentTypes.TEXT_PLAIN);
                response.write("The time is " + Instant.now());
                return true;
            })
            .addHandler(ResourceHandler.fileOrClasspath("src/main/resources/web", "/web").withPathToServeFrom("/cathub").build())
            .start();

        log.info("Started at " + server.uri().resolve("/" + appName + "/"));
    }

}