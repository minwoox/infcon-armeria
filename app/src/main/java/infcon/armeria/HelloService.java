package infcon.armeria;

import com.linecorp.armeria.common.HttpRequest;
import com.linecorp.armeria.common.HttpResponse;
import com.linecorp.armeria.server.HttpService;
import com.linecorp.armeria.server.ServiceRequestContext;

public final class HelloService implements HttpService {

    @Override
    public HttpResponse serve(
            ServiceRequestContext ctx, HttpRequest req) throws Exception {
        return HttpResponse.of("Hello, Armeria!");
    }
}
