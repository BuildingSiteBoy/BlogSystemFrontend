package com.zzeng.wj.dao;

import com.zzeng.wj.entity.JotterArticle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JotterArticleDao extends JpaRepository<JotterArticle, Integer> {
    JotterArticle findById(int id);
}
