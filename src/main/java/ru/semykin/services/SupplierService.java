package ru.semykin.services;

import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.semykin.models.Supplier;
import tools.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
@RequiredArgsConstructor
public class SupplierService {

    private final ObjectMapper objectMapper;


    private static final ConcurrentHashMap<Long, Supplier> SUPPLIERS = new ConcurrentHashMap<>();

    public void updateSupplierList(final List<Supplier> suppliers) {
        if (suppliers == null || suppliers.isEmpty()) {
            log.error("Передан пустой список поставщиков");
            throw new IllegalArgumentException("Передан пустой список поставщиков");
        }
        suppliers.forEach(supplier -> {
            SUPPLIERS.put(supplier.getId(), supplier);
        });
    }

    public Supplier getSupplierById(final Long id) {
        return SUPPLIERS.get(id);
    }

    private List<Supplier> getSuppliersFromFile(final String filePath) {
        if (StringUtils.isEmpty(filePath)) {
            log.error("Не указан путь к файлу!");
            throw new IllegalArgumentException("Не указан путь к файлу!");
        }
        try (final BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            return objectMapper.readValue(sb.toString(),
                    objectMapper.getTypeFactory().constructCollectionType(List.class, Supplier.class));
        } catch (IOException e) {
            log.error("Ошибка работы с файлом: {}", e.getMessage(), e);
            throw new RuntimeException("Ошибка работы с файлом", e);
        }
    }
}
