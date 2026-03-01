package com.project.myinventory.controller;

import com.project.myinventory.entity.Categories;
import com.project.myinventory.entity.Products;
import com.project.myinventory.repository.CategoryRepository;
import com.project.myinventory.service.AdminService;
import com.project.myinventory.service.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/admin")
@Slf4j
public class AdminController {

    @Autowired
    private AdminService adminService;

    @PostMapping("/categories")
    public ResponseEntity<Map<String, Object>> addCategory(@Valid @RequestBody Categories categories) {

        Categories newCategory = adminService.addCategory(categories);
        return buildResponse("Kategori berhasil ditambahkan",newCategory, HttpStatus.CREATED);
    }

    @PostMapping("/products")
    public ResponseEntity<Map<String, Object>> addProduct(@Valid @RequestBody Products products){
        Products newProduct = adminService.addProduct(products);
        return buildResponse("Produk berhasil ditambahkan", newProduct, HttpStatus.CREATED);
    }

    @PutMapping("/products/{id}")
    public ResponseEntity<Map<String, Object>> updateProduct(@PathVariable Long idproduct, @RequestBody Products products){
        Products updateProduct = adminService.updateProduct(idproduct, products);
        return buildResponse("Produk berhasil diupdate", updateProduct, HttpStatus.OK);
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<Map<String, Object>> deleteProduct(@PathVariable Long idProduct){
        adminService.deleteProduct(idProduct);
        Map<String, Object> response = new HashMap<>();
        response.put("Message: ", "produk dengan ID " + idProduct + " berhasil dihapus");
        return ResponseEntity.ok(response);
    }

    private ResponseEntity<Map<String, Object>> buildResponse(String message, Object object, HttpStatus httpStatus){
        Map<String, Object> response = new HashMap<>();
        response.put("Message: ",message);
        response.put("Data: ", object);
        return new ResponseEntity<>(response, httpStatus);
    }
}
