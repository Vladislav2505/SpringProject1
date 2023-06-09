package com.topchiev.springcourse.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import com.topchiev.springcourse.dao.BookDAO;
import com.topchiev.springcourse.dao.PersonDAO;
import com.topchiev.springcourse.models.Book;
import com.topchiev.springcourse.models.Person;

import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping("/books")
public class BooksController {
    private final BookDAO bookDAO;
    private final PersonDAO personDAO;

    @Autowired
    public BooksController(BookDAO bookDAO, PersonDAO personDAO) {
        this.bookDAO = bookDAO;
        this.personDAO = personDAO;
    }

    @GetMapping()
    public String showBooks(Model model) {
        model.addAttribute("books", bookDAO.allBooks());
        return "/books/show_books";
    }

    @GetMapping("/{id}")
    public String showBook(@PathVariable("id") int id, Model model,
                           @ModelAttribute("person") Person person) {
        model.addAttribute("book", bookDAO.getBook(id));
        Optional<Person> owner = bookDAO.getBookOwner(id);
        if (owner.isPresent())
            model.addAttribute("owner", owner.get());
        else
            model.addAttribute("people", personDAO.allPeople());

        return "/books/show_book";
    }

    @GetMapping("/new")
    public String createBook(@ModelAttribute("book") Book book) {
        return "/books/add_book";
    }

    @PostMapping("/new")
    public String addBook(@ModelAttribute("book") @Valid Book book, BindingResult result) {
        if (result.hasErrors())
            return "/books/add_book";

        bookDAO.addBook(book);
        return "redirect:/books";
    }

    @GetMapping("/{id}/edit")
    public String editBook(@PathVariable("id") int id, Model model) {
        Book book = bookDAO.getBook(id);
        model.addAttribute("book", book);
        return "/books/update_book";
    }

    @PatchMapping("/{id}/edit")
    public String updateBook(@PathVariable("id") int id,
                             @ModelAttribute("book") @Valid Book book, BindingResult result) {
        if (result.hasErrors())
            return "/books/update_book";

        bookDAO.updateBook(id, book);
        return "redirect:/books";
    }

    @DeleteMapping("/{id}/delete")
    public String deleteBook(@PathVariable("id") int id) {
        bookDAO.deleteBook(id);
        return "redirect:/books";
    }

    @PatchMapping("/{id}/release")
    public String releaseBook(@PathVariable("id") int id) {
        bookDAO.releaseBook(id);
        return "redirect:/books";
    }

    @PatchMapping("/{id}/assign")
    public String assignBook(@PathVariable("id") int id, @ModelAttribute("person") Person person) {
        bookDAO.assignBook(id, person);
        return "redirect:/books";
    }
}
