package ru.semykin.clients;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;
import ru.semykin.exception.SupplierException;
import ru.semykin.models.Supplier;

import java.io.IOException;


@Service
@Slf4j
@RequiredArgsConstructor
public class HttpSupplierClient {

    public String get(final Supplier supplier, final Long phone) {
        String result;
        try (final CloseableHttpClient client = HttpClients.createDefault()) {
            final HttpGet get = new HttpGet(getUrl(supplier.getBaseUrl(), phone));
            get.addHeader("API-Key", supplier.getApiKey());
            result = client.execute(get, httpResponse -> {
                final HttpEntity entity = httpResponse.getEntity();
                EntityUtils.consume(entity);
                return EntityUtils.toString(entity);
            });
        } catch (IOException e) {
            throw new SupplierException(e.getMessage(), e);
        }
        return result;
    }

    private String getUrl(final String baseUrl, final Long phone) {
        return baseUrl + "/subscribers/" + phone;
    }
}
