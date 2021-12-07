package com.zzeng.wj.service;

import com.zzeng.wj.dao.JotterArticleDao;
import com.zzeng.wj.entity.JotterArticle;
import com.zzeng.wj.redis.RedisService;
import com.zzeng.wj.util.MyPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class JotterArticleService {
    @Autowired
    JotterArticleDao jotterArticleDao;
    @Autowired
    RedisService redisService;

    /**
     * 列出所有文章
     * MyPage类时自定义的类，用于替代Spring Date JPA Page
     * @param page
     * @param size
     * @return
     */
    public MyPage list(int page, int size) {
        MyPage<JotterArticle> articles;
        // 用户访问列表页面时按页缓存文章
        String key = "articlepage:" + page;
        Object articlePageCache = redisService.get(key);

        // 如果缓存中有则直接从缓存中获取，没有就从数据库中获取数据
        if (articlePageCache == null) {
            Sort sort = Sort.by(Sort.Direction.DESC, "id");
            Page<JotterArticle> articlesInDB = jotterArticleDao.findAll(PageRequest.of(page, size, sort));
            articles = new MyPage<>(articlesInDB);
            redisService.set(key, articles);
        } else {
            articles = (MyPage<JotterArticle>) articlePageCache;    //返回值为Object，应该转为对应的类型
        }

        return articles;
    }

//    用于复现异常
//    @Cacheable(value = RedisConfig.REDIS_KEY_DATABASE)
//    public Page list(int page, int size) {
//        Sort sort = new Sort(Sort.Direction.DESC, "id");
//        return jotterArticleDAO.findAll(PageRequest.of(page, size, sort));
//    }

    /**
     * 根据id查找具体文章
     * @param id
     * @return
     */
    public JotterArticle findById(int id) {
        JotterArticle article;
        // 用户访问具体文章时缓存单篇文章，通过id区分
        String key = "article:" + id;
        Object articleCache = redisService.get(key);

        // 如果缓存中有则直接从缓存中获取，没有就从数据库中获取数据
        if (articleCache == null) {
            article = jotterArticleDao.findById(id);
            redisService.set(key, article);
        } else {
            article = (JotterArticle) articleCache;
        }

        return article;
    }

    /**
     * 添加或者更新文章
     * @param article
     */
    public void addOrUpdate(JotterArticle article) {
        jotterArticleDao.save(article);

        //删除当前选中的文章和所有文章中页面的缓存
        redisService.delete("article" + article.getId());
        Set<String> keys = redisService.getKeysByPattern("articlepage*");
        redisService.delete(keys);
    }

    public void delete(int id) {
        jotterArticleDao.deleteById(id);

        //删除当前选中的文章和所有文章中页面的缓存
        redisService.delete("article:" + id);
        Set<String> keys = redisService.getKeysByPattern("articlepage*");
        redisService.delete(keys);
    }
}
