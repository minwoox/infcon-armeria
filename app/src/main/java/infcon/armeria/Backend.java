package infcon.armeria;

import java.time.Duration;

import com.linecorp.armeria.common.HttpResponse;
import com.linecorp.armeria.server.Server;
import com.linecorp.armeria.server.ServerBuilder;

public final class Backend {

    private final Server server;

    public static Backend of(String name, int port) {
        return new Backend(name, port);
    }

    private Backend(String name, int port) {
        final ServerBuilder serverBuilder = Server.builder();
        server = serverBuilder.http(port)
                              .service('/' + name, (ctx, req) -> {
                                  return HttpResponse.delayed(
                                          HttpResponse.of("Response from: " + name),
                                          Duration.ofSeconds(3));
                              })
                              .build();
    }

    public void start() {
        server.start().join();
    }
}
