package infcon.armeria;

import com.linecorp.armeria.common.HttpRequest;
import com.linecorp.armeria.common.HttpResponse;
import com.linecorp.armeria.server.DecoratingHttpServiceFunction;
import com.linecorp.armeria.server.HttpService;
import com.linecorp.armeria.server.ServiceRequestContext;

public final class AuthDecorator implements DecoratingHttpServiceFunction {

    @Override
    public HttpResponse serve(
            HttpService delegate, ServiceRequestContext ctx, HttpRequest req) throws Exception {
        return null;
    }
}
