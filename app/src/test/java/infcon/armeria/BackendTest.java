package infcon.armeria;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;

import com.linecorp.armeria.client.WebClient;
import com.linecorp.armeria.common.AggregatedHttpResponse;
import com.linecorp.armeria.common.HttpResponse;

class BackendTest {

    @Test
    void backend() throws InterruptedException {
        final Backend foo = Backend.of("foo", 9000);
        foo.start();

        final WebClient webClient = WebClient.of("http://127.0.0.1:9000");
        final HttpResponse httpResponse = webClient.get("/foo");
        final CompletableFuture<AggregatedHttpResponse> future = httpResponse.aggregate();
        System.err.println(future.join().contentUtf8());
    }

    // Run main() before run this test.
    @Test
    void multipleRequests() throws InterruptedException {
        final long start = System.nanoTime();
        final CountDownLatch latch = new CountDownLatch(10000);
        for (int i = 0; i < 100000; i++) {
            final WebClient webClient = WebClient.of("http://127.0.0.1:8000");
            webClient.get("/infcon").aggregate().handle((aggregatedHttpResponse, throwable) -> {
                latch.countDown();
                return null;
            });
        }
        latch.await();
        System.err.println("Elapsed time: " + TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start));
    }
}