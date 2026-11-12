package ru.semykin.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SupplierController {

    @GetMapping("/test")
    public String test() {
        return "test";
    }
}
