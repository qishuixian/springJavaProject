package com.example.backend.controller;

import com.example.backend.dto.Result;
import com.example.backend.model.Category;
import com.example.backend.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    @Autowired
    private CategoryRepository categoryRepository;

    // GET /api/categories - 获取所有分类
    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public Result<List<Category>> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return Result.success(categories);
    }

    // POST /api/categories - 新增分类（仅管理员）
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Category> createCategory(@RequestBody Category category) {
        Category saved = categoryRepository.save(category);
        return Result.success(saved);
    }

    // DELETE /api/categories/{id} - 删除分类（仅管理员）
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Void> deleteCategory(@PathVariable Long id) {
        categoryRepository.deleteById(id);
        return Result.success();
    }
}
