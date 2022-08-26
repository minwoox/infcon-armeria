package infcon.armeria;

import com.linecorp.armeria.common.HttpResponse;
import com.linecorp.armeria.server.Server;
import com.linecorp.armeria.server.ServerBuilder;

public final class AuthServer {

    private final Server server;

    public static AuthServer of(int port) {
        return new AuthServer(port);
    }

    private AuthServer(int port) {
        final ServerBuilder serverBuilder = Server.builder();
        server = serverBuilder.http(port)
                              .service("/auth", (ctx, req) -> {
                                  return HttpResponse.of("Authorized");
                              })
                              .build();
    }

    public void start() {
        server.start().join();
    }
}
