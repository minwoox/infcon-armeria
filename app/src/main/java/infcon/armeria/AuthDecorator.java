package infcon.armeria;

import java.util.concurrent.CompletableFuture;

import com.linecorp.armeria.client.WebClient;
import com.linecorp.armeria.common.HttpRequest;
import com.linecorp.armeria.common.HttpResponse;
import com.linecorp.armeria.server.DecoratingHttpServiceFunction;
import com.linecorp.armeria.server.HttpService;
import com.linecorp.armeria.server.ServiceRequestContext;

public final class AuthDecorator implements DecoratingHttpServiceFunction {

    private final WebClient authClient;

    public AuthDecorator(WebClient authClient) {
        this.authClient = authClient;
    }

    @Override
    public HttpResponse serve(
            HttpService delegate, ServiceRequestContext ctx, HttpRequest req) throws Exception {
        final CompletableFuture<HttpResponse> future = new CompletableFuture<>();

        authClient.get("/auth").aggregate().thenAccept(aggregatedHttpResponse -> {
            if ("authorized".equals(aggregatedHttpResponse.contentUtf8())) {
                try {
                    future.complete(delegate.serve(ctx, req));
                } catch (Exception e) {
                    future.completeExceptionally(e);
                }
            } else {
                future.complete(HttpResponse.of(401));
            }
        });

        return HttpResponse.from(future);
    }
}
