package com.zzeng.wj.service;

import com.zzeng.wj.dao.CategoryDao;
import com.zzeng.wj.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    @Autowired
    CategoryDao categoryDao;

    public List<Category> list() {
        //以id降序排序
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        return categoryDao.findAll(sort);
    }

    public Category get(int id) {
        //.orElse(null)表示如果一个都没找到返回null
        Category c = categoryDao.findById(id).orElse(null);
        return c;
    }
}
