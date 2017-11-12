package com.example.resource;

import com.example.domain.Book;
import com.example.domain.Books;
import com.example.service.BookService;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.io.File;
import java.nio.file.Paths;
import java.util.List;

@Path("books")
public class BookResource {
    @Context
    HttpServletRequest request;
    @Context
    HttpServletResponse response;

    private static final Logger LOGGER = Logger.getLogger(BookResource.class);
    @Autowired
    private BookService bookService;

    @GET
    @Path("bb")
    @Produces(MediaType.TEXT_PLAIN)
    public String getBb() {
        return "nnnnnn";
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Books getBooks() {
        final Books books = bookService.getBooks();
        BookResource.LOGGER.debug(books);
        return books;
    }

    @GET
    @Path("/book")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Book getBookByQuery(@QueryParam("id") final Long bookId) {
        final Book book = bookService.getBook(bookId);
        BookResource.LOGGER.debug(book);
        return book;
    }

    @GET
    @Path("{bookId:[0-9]*}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Book getBookByPath(@PathParam("bookId") final Long bookId) {
        final Book book = bookService.getBook(bookId);
        BookResource.LOGGER.debug(book);
        return book;
    }

    //可接收客户端上传文件
    @POST
    @Path("upload")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.TEXT_PLAIN)
    public String upLoad() throws Exception {
        //在项目路径下设置upload文件夹作为路径
        String upload_file_path = request.getSession().getServletContext().getRealPath("/") + "upload/";
        if (!Paths.get(upload_file_path).toFile().exists()) {
            Paths.get(upload_file_path).toFile().mkdirs();
        }
        System.out.println(upload_file_path);
        // 设置工厂
        DiskFileItemFactory factory = new DiskFileItemFactory();
        // 设置文件存储位置
        factory.setRepository(Paths.get(upload_file_path).toFile());
        // 设置大小，如果文件小于设置大小的话，放入内存中，如果大于的话则放入磁盘中,单位是byte
        factory.setSizeThreshold(0);

        ServletFileUpload upload = new ServletFileUpload(factory);
        // 这里就是中文文件名处理的代码，其实只有一行
        upload.setHeaderEncoding("utf-8");
        String fileName ;
        List<FileItem> list = upload.parseRequest(request);
        for (FileItem item : list) {
            if (item.isFormField()) {
                String name = item.getFieldName();
                String value = item.getString("utf-8");
                System.out.println(name);
                System.out.println(value);
                request.setAttribute(name, value);
            } else {
                String name = item.getFieldName();
                String value = item.getName();
                System.out.println(name);
                System.out.println(value);

                fileName = Paths.get(value).getFileName().toString();
                request.setAttribute(name, fileName);
                // 写文件到path目录，文件名问filename
                item.write(new File(upload_file_path, fileName));
            }
        }
        return "success";
    }

    @POST
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.TEXT_XML})
    public Book saveBook(final Book book) {
        return bookService.saveBook(book);
    }

    @PUT
    @Path("{bookId:[0-9]*}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.TEXT_XML})
    public Book updateBook(@PathParam("bookId") final Long bookId, final Book book) {
        if (book == null) {
            return null;
        }
        return bookService.updateBook(bookId, book);
    }

    @DELETE
    @Path("/{bookId:[0-9]*}")
    public String deleteBook(@PathParam("bookId") final Long bookId) {
        if (bookService.deleteBook(bookId)) {
            return "Deleted book id=" + bookId;
        } else {
            return "Deleted book failed id=" + bookId;
        }
    }
}