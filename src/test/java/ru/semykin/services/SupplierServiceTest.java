package ru.semykin.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mockito;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import ru.semykin.ApplicationTest;
import ru.semykin.clients.HttpSupplierClient;
import ru.semykin.dto.Response;
import ru.semykin.dto.Supplier;

import java.io.IOException;
import java.util.List;
import java.util.stream.LongStream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

class SupplierServiceTest extends ApplicationTest {

    private SupplierService supplierService;
    private HttpSupplierClient httpClientMock;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final Long SUPPLIER_ID = 132L;
    private static final Long PHONE_NUMBER = 1234567L;

    @BeforeEach
    void setUp() {
        httpClientMock = Mockito.mock(HttpSupplierClient.class);
        supplierService = new SupplierService(httpClientMock, objectMapper);
        supplierService.updateSupplierList(List.of(createSupplier(SUPPLIER_ID)));
    }

    @Test
    void updateSupplierList() {
        final long supplierIdStart = 1L;
        final long supplierIdEnd = 10L;
        final List<Supplier> supplierList = createSuppliersList(supplierIdStart, supplierIdEnd);
        supplierService.updateSupplierList(supplierList);
        LongStream.rangeClosed(supplierIdStart, supplierIdEnd).forEach(supplierId -> {
            assertThat(supplierService.getSupplierById(supplierId)).isNotNull();
        });
    }

    @Test
    void updateSuppliersFromFile() {
        final long isNotfoundSupplierId = 1;
        final long isFirsSuccessSupplierId = 10001;
        final long isSecondSuccessSupplierId = 2222;
        try {
            byte[] content = new ClassPathResource("test.json").getInputStream().readAllBytes();
            final MultipartFile file =  new MockMultipartFile(
                    "file", "test.json", "application/json", content
            );
            supplierService.updateSuppliersFromFile(file);
            assertThat(supplierService.getSupplierById(isNotfoundSupplierId)).isNull();
            assertThat(supplierService.getSupplierById(isFirsSuccessSupplierId)).isNotNull();
            assertThat(supplierService.getSupplierById(isSecondSuccessSupplierId)).isNotNull();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @ParameterizedTest
    @CsvSource({
            "200, {\"status\":\"ok\"}",
            "404, {\"status\":\"Not Found\"}"
    })
    void whenHttpStatusThenReturnStatus(final int status, final String responseBody) {
        Mockito.when(httpClientMock.get(any(), anyLong()))
                .thenReturn(new Response(status, responseBody));
        final Response response = supplierService.checkSupplier(SUPPLIER_ID, PHONE_NUMBER);
        assertThat(response.getStatus()).isEqualTo(status);
    }
}