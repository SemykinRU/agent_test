package ru.semykin.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.semykin.clients.HttpSupplierClient;
import ru.semykin.dto.Response;
import ru.semykin.exception.ExceptionMessage;
import ru.semykin.exception.SupplierException;
import ru.semykin.dto.Supplier;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
@RequiredArgsConstructor
public class SupplierService {

    private final HttpSupplierClient httpSupplierClient;
    private final ObjectMapper objectMapper;

    private static final String JSON_FORMAT =  "application/json";

    private static final ConcurrentHashMap<Long, Supplier> SUPPLIERS = new ConcurrentHashMap<>();

    public void updateSupplierList(final List<Supplier> suppliers) {
        if (suppliers == null || suppliers.isEmpty()) {
            log.error(ExceptionMessage.SUPPLIER_LIST_EMPTY);
            throw new SupplierException(ExceptionMessage.SUPPLIER_LIST_EMPTY);
        }
        SUPPLIERS.clear();
        suppliers.forEach(supplier -> {
            SUPPLIERS.put(supplier.getId(), supplier);
        });
    }

    public Supplier getSupplierById(final Long id) {
        return SUPPLIERS.get(id);
    }

    private List<Supplier> getSuppliersFromJsonFile(final MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return null;
        }
        if (file.getContentType() == null || !file.getContentType().startsWith(JSON_FORMAT)) {
            log.error(ExceptionMessage.NON_JSON_FILE);
            throw new SupplierException(ExceptionMessage.NON_JSON_FILE);
        }
        try {
            final String value = new String(file.getBytes(), StandardCharsets.UTF_8);
            return objectMapper.readValue(value,
                    objectMapper.getTypeFactory().constructCollectionType(List.class, Supplier.class));
        } catch (IOException e) {
            throw new SupplierException(e);
        }
    }

    public void updateSuppliersFromFile(final MultipartFile file) {
        if (file == null || file.isEmpty()) {
            log.error(ExceptionMessage.EMPTY_FILE);
            throw new SupplierException(ExceptionMessage.EMPTY_FILE);
        }
        final List<Supplier> suppliersFromFile = getSuppliersFromJsonFile(file);
        updateSupplierList(suppliersFromFile);
    }

    public Response checkSupplier(final Long id, final Long phone) {
        if (id == null) {
            log.error(ExceptionMessage.SUPPLIER_ID_IS_NULL);
            throw new SupplierException(ExceptionMessage.SUPPLIER_ID_IS_NULL);
        }
        if (phone == null) {
            log.error(ExceptionMessage.SUPPLIER_PHONE_IS_NULL);
            throw new SupplierException(ExceptionMessage.SUPPLIER_PHONE_IS_NULL);
        }
        final Supplier supplierById = getSupplierById(id);
        if (supplierById == null) {
            log.error(String.format(ExceptionMessage.SUPPLIER_NOT_FOUND_BY_ID, id).toString());
            throw new SupplierException(String.format(ExceptionMessage.SUPPLIER_NOT_FOUND_BY_ID, id));
        }
        return httpSupplierClient.get(supplierById, phone);
    }
}
