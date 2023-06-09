package com.topchiev.springcourse.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import com.topchiev.springcourse.dao.PersonDAO;
import com.topchiev.springcourse.models.Person;
import com.topchiev.springcourse.util.PersonValidator;

import javax.validation.Valid;

@Controller
@RequestMapping("/people")
public class PeopleController {
    private final PersonDAO personDAO;
    private final PersonValidator personValidator;

    @Autowired
    public PeopleController(PersonDAO personDAO, PersonValidator personValidator) {
        this.personDAO = personDAO;
        this.personValidator = personValidator;
    }

    @GetMapping()
    public String showPeople(Model model) {
        model.addAttribute("people", personDAO.allPeople());
        return "/people/show_people";
    }

    @GetMapping("/{id}")
    public String showPerson(@PathVariable("id") int id, Model model) {
        model.addAttribute("person", personDAO.getPerson(id));
        model.addAttribute("books", personDAO.getBooksByPersonId(id));
        return "/people/show_person";
    }

    @GetMapping("/new")
    public String createPerson(@ModelAttribute("person") Person person) {
        return "/people/add_person";
    }

    @PostMapping("/new")
    public String addPerson(@ModelAttribute("person") @Valid Person person, BindingResult result) {

        personValidator.validate(person, result);
        if (result.hasErrors()) {
            return "/people/add_person";
        }
        personDAO.addPerson(person);
        return "redirect:/people";
    }

    @GetMapping("/{id}/edit")
    public String editPerson(@PathVariable("id") int id, Model model) {
        Person person = personDAO.getPerson(id);
        model.addAttribute("person", person);
        return "/people/update_person";
    }

    @PatchMapping("/{id}/edit")
    public String updatePerson(@PathVariable("id") int id,
                               @ModelAttribute("person") @Valid Person person, BindingResult result) {
        if (result.hasErrors()) {
            return "/people/update_person";
        }
        personDAO.updatePerson(id, person);
        return "redirect:/people";
    }

    @DeleteMapping("/{id}/delete")
    public String deletePerson(@PathVariable("id") int id) {
        personDAO.deletePerson(id);
        return "redirect:/people";
    }
}
