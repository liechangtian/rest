package com.example.domain;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.List;

/**
 * <p>Books class.</p>
 *
 * @author hanl
 * @version $Id: $Id
 */
@XmlRootElement(name = "books")
public class Books implements Serializable {
    private static final long serialVersionUID = -5070487415443208853L;
    private List<Book> bookList;

    public Books() {
        super();
    }

    public Books(final List<Book> bookList) {
        super();
        this.bookList = bookList;
    }


    @XmlElement(name = "book")
    @XmlElementWrapper
    public List<Book> getBookList() {
        return bookList;
    }

    public void setBookList(final List<Book> bookList) {
        this.bookList = bookList;
    }


    @Override
    public String toString() {
        return "{" + bookList + "}";
    }
}