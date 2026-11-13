package ru.semykin;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.semykin.dto.Supplier;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.LongStream;


@ExtendWith(MockitoExtension.class)
public class ApplicationTest {

    protected List<Supplier> createSuppliersList(final long supplierIdStart, final long supplierIdEnd) {
        final List<Supplier> supplierList = new ArrayList<>();
        LongStream.rangeClosed(supplierIdStart, supplierIdEnd).forEach(
                supplierId -> {
                    final Supplier supplier = createSupplier(supplierId);
                    supplierList.add(supplier);
                }
        );
        return supplierList;
    }

    protected Supplier createSupplier(final long supplierId) {
        final Supplier supplier = new Supplier();
        supplier.setId(supplierId);
        supplier.setBaseUrl("http://test.ru");
        supplier.setApiKey("apiKey");
        return supplier;
    }
}