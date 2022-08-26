package infcon.armeria;

import java.util.concurrent.CompletableFuture;

import com.linecorp.armeria.client.WebClient;
import com.linecorp.armeria.common.HttpRequest;
import com.linecorp.armeria.common.HttpResponse;
import com.linecorp.armeria.server.HttpService;
import com.linecorp.armeria.server.ServiceRequestContext;

public final class MyService implements HttpService {

    private final WebClient fooClient;
    private final WebClient barClient;

    public MyService(WebClient fooClient, WebClient barClient) {
        this.fooClient = fooClient;
        this.barClient = barClient;
    }

    @Override
    public HttpResponse serve(ServiceRequestContext ctx, HttpRequest req) throws Exception {
        final CompletableFuture<HttpResponse> future = new CompletableFuture<>();

        fooClient.get("/foo").aggregate().thenAccept(fooResponse -> {
            barClient.get("/bar").aggregate().thenAccept(barResponse -> {
                future.complete(HttpResponse.of(fooResponse.contentUtf8() + '\n' + barResponse.contentUtf8()));
            });
        });

        return HttpResponse.from(future);
    }
}
