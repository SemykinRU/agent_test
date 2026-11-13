package ru.semykin.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.semykin.dto.Response;
import ru.semykin.dto.Supplier;
import ru.semykin.services.SupplierService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class SupplierController {

    private final SupplierService supplierService;

    @PostMapping("/upload")
    public ResponseEntity<Response> upload(@RequestParam(name = "file") final MultipartFile file) {
        try {
            supplierService.updateSuppliersFromFile(file);
            return ResponseEntity.ok(new Response(HttpStatus.OK.value(), "Ok"));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(new Response(HttpStatus.BAD_REQUEST.value(), ex.getMessage()));
        }
    }

    @PostMapping("/update")
    public ResponseEntity<Response> update(@RequestBody final List<Supplier> suppliers) {
        try {
            supplierService.updateSupplierList(suppliers);
            return ResponseEntity.ok(new Response(HttpStatus.OK.value(), "Ok"));
        }  catch (Exception ex) {
            return ResponseEntity.badRequest().body(new Response(HttpStatus.BAD_REQUEST.value(), ex.getMessage()));
        }
    }

    @GetMapping("/suppliers")
    public ResponseEntity<Response> checkSuppliers(@RequestParam final Long id, @RequestParam final Long phone) {
        try {
            final Response response = supplierService.checkSupplier(id, phone);
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(new Response(HttpStatus.BAD_REQUEST.value(), ex.getMessage()));
        }
    }
}
