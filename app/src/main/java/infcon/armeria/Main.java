package infcon.armeria;

import java.util.concurrent.CompletableFuture;

import com.linecorp.armeria.client.WebClient;
import com.linecorp.armeria.server.Server;
import com.linecorp.armeria.server.ServerBuilder;

public final class Main {

    public static void main(String[] args) {
        final AuthServer authServer = AuthServer.of(8999);
        authServer.start();
        final WebClient authClient = WebClient.of("http://127.0.0.1:8999");
        final Backend foo = Backend.of("foo", 9000);
        foo.start();
        final WebClient fooClient = WebClient.of("http://127.0.0.1:9000");
        final Backend bar = Backend.of("bar", 9001);
        bar.start();
        final WebClient barClient = WebClient.of("http://127.0.0.1:9001");

        final ServerBuilder serverBuilder = Server.builder();
        final Server frontend = serverBuilder.http(8080)
                                             .decorator(new AuthDecorator(authClient))
                                             .service("/infcon", new MyService(fooClient, barClient))
                                             .build();
        final CompletableFuture<Void> start = frontend.start();
        start.join();
    }
}
