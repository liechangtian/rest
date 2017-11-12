package com.example.dao;

import com.example.domain.Book;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

/**
 * <p>BookDao class.</p>
 *
 * @author hanl
 * @version $Id: $Id
 */
@Repository
public class BookDao {
    private static final Logger LOGGER = Logger.getLogger(BookDao.class);
    @PersistenceContext
    private EntityManager entityManager;

    public BookDao() {
    }


    public Book findById(final Long id) {
        Assert.notNull(id);
        try {
           return entityManager.find(Book.class, id);
        } catch (final Exception e) {
            BookDao.LOGGER.error(e);
            return null;
        }
    }

    public List<Book> findAll() {
        return findAll(false, 0, 0);
    }


    public List<Book> findAll(final boolean isPaging, final int firstResult, final int maxResults) {
        final CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        final CriteriaQuery<Book> cq = cb.createQuery(Book.class);
        final TypedQuery<Book> q = entityManager.createQuery(cq);
        if (isPaging) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }


    @Transactional
    public boolean remove(final Long bookId) {
        final Book book0 = findById(bookId);
        if (book0 != null) {
            entityManager.remove(book0);
            return true;
        } else {
            return false;
        }
    }


    @Transactional
    public Book store(final Book entity) {
        return entityManager.merge(entity);
    }

    @Transactional
    public void update(final Book entity) {
        final StringBuilder jpql = new StringBuilder();
        jpql.append("UPDATE Book b SET b.bookName='").append(entity.getBookName());
        jpql.append("', b.publisher='").append(entity.getPublisher());
        jpql.append("' WHERE b.bookId=").append(entity.getBookId());
        entityManager.createQuery(jpql.toString()).executeUpdate();
    }

    @Transactional
    public void save(final Book entity) {
        entityManager.persist(entity);
    }

}
