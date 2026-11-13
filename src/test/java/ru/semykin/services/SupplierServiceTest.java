package ru.semykin.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import ru.semykin.ApplicationTest;
import ru.semykin.clients.HttpSupplierClient;
import ru.semykin.models.Supplier;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.LongStream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class SupplierServiceTest extends ApplicationTest {

    private SupplierService supplierService;
    private HttpSupplierClient httpClientMock;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        httpClientMock = Mockito.mock(HttpSupplierClient.class);
        supplierService = new SupplierService(httpClientMock, objectMapper);
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
        try {
            byte[] content = new ClassPathResource("test.json").getInputStream().readAllBytes();
            final MultipartFile file =  new MockMultipartFile(
                    "file", "test.json", "application/json", content
            );
            supplierService.updateSuppliersFromFile(file);
            assertThat(supplierService.getSupplierById(1L)).isNull();
            assertThat(supplierService.getSupplierById(10001L)).isNotNull();
            assertThat(supplierService.getSupplierById(2222L)).isNotNull();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Test
    void checkSupplier() {
    }

    private List<Supplier> createSuppliersList(final long supplierIdStart, final long supplierIdEnd) {
        final List<Supplier> supplierList = new ArrayList<>();
        LongStream.rangeClosed(supplierIdStart, supplierIdEnd).forEach(
                supplierId -> {
                    final Supplier supplier = createSupplier(supplierId);
                    supplierList.add(supplier);
                }
        );
        return supplierList;
    }

    private Supplier createSupplier(final long supplierId) {
        final Supplier supplier = new Supplier();
        supplier.setId(supplierId);
        supplier.setBaseUrl("http://test.ru");
        supplier.setApiKey("apiKey");
        return supplier;
    }
}