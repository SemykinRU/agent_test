package ru.semykin.clients;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;
import ru.semykin.dto.Response;
import ru.semykin.exception.SupplierException;
import ru.semykin.dto.Supplier;

import java.io.IOException;


@Service
@Slf4j
@RequiredArgsConstructor
public class HttpSupplierClient {

    public Response get(final Supplier supplier, final Long phone) {
        try (final CloseableHttpClient client = HttpClients.createDefault()) {
            final HttpGet get = new HttpGet(getUrl(supplier.getBaseUrl(), phone));
            get.addHeader("API-Key", supplier.getApiKey());
            return client.execute(get, httpResponse -> {
                final Response response = new Response();
                response.setStatus(httpResponse.getStatusLine().getStatusCode());
                response.setMessage(EntityUtils.toString(httpResponse.getEntity()));
                return response;
            });
        } catch (IOException e) {
            throw new SupplierException(e.getMessage(), e);
        }
    }

    private String getUrl(final String baseUrl, final Long phone) {
        return baseUrl + "/subscribers/" + phone;
    }
}
