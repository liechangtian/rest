package com.example.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.dao.BookDao;
import com.example.domain.Book;
import com.example.domain.Books;

/***
 * Created on 2017/10/7 at 17:45.  
 ***/

@Service
/**
 * <p>BookService class.</p>
 *
 * @author hanl
 * @version $Id: $Id
 */
public class BookService {
    private static final Logger LOGGER = Logger.getLogger(BookService.class);
    @Autowired
    private BookDao bookDao;

    public BookService() {
    }

    public Book saveBook(final Book book) {
        return bookDao.store(book);
    }


    public Book updateBook(final Long bookId, final Book book) {
        book.setBookId(bookId);
        bookDao.update(book);
        return book;
    }


    public Book getBook(final Long bookId) {
        try {
            return bookDao.findById(bookId);
        } catch (final Exception e) {
            BookService.LOGGER.error(e);
            return new Book(-1L, "");
        }
    }


    public Books getBooks() {
        return new Books(bookDao.findAll());
    }


    public boolean deleteBook(final Long bookId) {
        return bookDao.remove(bookId);
    }
}

