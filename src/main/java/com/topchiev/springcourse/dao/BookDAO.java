package com.topchiev.springcourse.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import com.topchiev.springcourse.models.Book;
import com.topchiev.springcourse.models.Person;

import java.util.List;
import java.util.Optional;

@Component
public class BookDAO {
    private final JdbcTemplate template;

    @Autowired
    public BookDAO(JdbcTemplate template) {
        this.template = template;
    }

    public List<Book> allBooks() {
        return template.query("SELECT * FROM book",
                new BeanPropertyRowMapper<>(Book.class));
    }

    public Book getBook(int id) {
        Book book = template.query("SELECT * FROM book WHERE id=?", new Object[]{id},
                        new BeanPropertyRowMapper<>(Book.class))
                .stream().findAny().orElse(null);
        System.out.println(book);
        return book;
    }

    public void addBook(Book book) {
        template.update("INSERT INTO book (title, author, year) VALUES (?, ?, ?)",
                book.getTitle(), book.getAuthor(), book.getYear());
    }

    public void updateBook(int id, Book book) {
        template.update("UPDATE book SET title=?, author=?, year=? WHERE id=?", book.getTitle(),
                book.getAuthor(), book.getYear(), id);
    }

    public void deleteBook(int id) {
        template.update("DELETE from book WHERE id=?", id);
    }

    public Optional<Person> getBookOwner(int id) {
        return template.query("SELECT p.* FROM book JOIN person p ON p.id = book.person_id WHERE book.id=?",
                new Object[]{id}, new BeanPropertyRowMapper<>(Person.class)).stream().findAny();
    }

    public void releaseBook(int id) {
        template.update("UPDATE book SET person_id=NULL WHERE id=?", id);
    }

    public void assignBook(int id, Person person) {
        template.update("UPDATE book SET person_id=? WHERE id=?", person.getId(), id);
    }
}
