package com.zzeng.wj.dao;

import com.zzeng.wj.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

//这个dao不需要额外构造方法，JPA提供的默认方法就够了！！！
public interface CategoryDao extends JpaRepository<Category, Integer> {

}
