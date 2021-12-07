package com.zzeng.wj.service;

import com.zzeng.wj.dao.BookDao;
import com.zzeng.wj.entity.Book;
import com.zzeng.wj.entity.Category;
import com.zzeng.wj.redis.RedisService;
import com.zzeng.wj.util.CastUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {
    @Autowired
    private BookDao bookDao;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private RedisService redisService;

    /**
     * 根据id升序查出所有书籍
     * @return  List<Book>
     */
    public List<Book> list() {
        System.out.println("根据id升序查出所有书籍");
        List<Book> books;
        String key = "booklist";
        Object bookCache = redisService.get(key);

        if (bookCache == null) {
            Sort sort = Sort.by(Sort.Direction.ASC, "id");
            books = bookDao.findAll(sort);
            redisService.set(key, books);
        } else {
            // 返回的bookCache是Object类型，需要转换为List类型
            books = CastUtils.objectConvertToList(bookCache, Book.class);
        }
        return books;
    }

//    直接用注解实现缓存
//    @Cacheable(value = RedisConfig.REDIS_KEY_DATABASE)
//    public List<Book> list() {
//        List<Book> books;
//        Sort sort = new Sort(Sort.Direction.DESC, "id");
//        books = bookDAO.findAll(sort);
//        return books;
//    }

    /**
     * 添加或者更新书籍
     * save() 方法的作用是，当主键存在时更新数据，当主键不存在时插入数据
     * @param book 书籍对象
     */
    public void addOrUpdate(Book book) {
        System.out.println("添加或者更新书籍");

        // 延时双删策略：先清除缓存，在更新数据库后等待一段时间后，再次执行删除操作
        redisService.delete("booklist");
        bookDao.save(book);
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        redisService.delete("booklist");
    }

    /**
     * 根据书籍id删除书籍
     * @param id 书籍id
     */
    public void deleteById(int id) {
        System.out.println("根据书籍id删除书籍");
        redisService.delete("booklist");
        bookDao.deleteById(id);
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        redisService.delete("booklist");
    }

    /**
     * 根据分类查书籍
     * @param cid 类别id
     * @return  List<Book>
     */
    public List<Book> listByCategory(int cid) {
        System.out.println("根据分类查书籍");
        Category category = categoryService.get(cid);
        return bookDao.findAllByCategory(category);
    }

    /**
     * 根据标题或者作者进行模糊查询
     * @param keywords  关键字：标题或作者
     * @return  List<Book>
     */
    public List<Book> Search(String keywords) {
        System.out.println("根据标题或者作者进行模糊查询");
        return bookDao.findAllByTitleLikeOrAuthorLike('%' + keywords + '%', '%' + keywords + '%');
    }
}
