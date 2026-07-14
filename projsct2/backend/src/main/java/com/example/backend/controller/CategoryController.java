package com.example.backend.controller;

import com.example.backend.dto.Result;
import com.example.backend.model.Category;
import com.example.backend.repository.CategoryRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@Tag(name = "分类管理", description = "图书分类的增删查接口")
public class CategoryController {

    @Autowired
    private CategoryRepository categoryRepository;

    // GET /api/categories - 获取所有分类
    @Operation(summary = "获取所有分类")
    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public Result<List<Category>> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return Result.success(categories);
    }

    // POST /api/categories - 新增分类（仅管理员）
    @Operation(summary = "新增分类", description = "仅管理员可操作")
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Category> createCategory(@RequestBody Category category) {
        Category saved = categoryRepository.save(category);
        return Result.success(saved);
    }

    // DELETE /api/categories/{id} - 删除分类（仅管理员）
    @Operation(summary = "删除分类", description = "仅管理员可操作")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Void> deleteCategory(@PathVariable Long id) {
        categoryRepository.deleteById(id);
        return Result.success();
    }
}
