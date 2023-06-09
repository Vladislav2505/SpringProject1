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
public class PersonDAO {
    private final JdbcTemplate template;

    @Autowired
    public PersonDAO(JdbcTemplate template) {
        this.template = template;
    }

    public List<Person> allPeople() {
        return template.query("SELECT * FROM person",
                new BeanPropertyRowMapper<>(Person.class));
    }

    public Person getPerson(int id) {
        return template.query("SELECT * FROM person WHERE id=?", new Object[]{id},
                        new BeanPropertyRowMapper<>(Person.class))
                .stream().findAny().orElse(null);
    }

    public Optional<Person> getPersonByFullName(String fullName) {
        return template.query("SELECT * FROM person WHERE full_name=?", new Object[]{fullName},
                new BeanPropertyRowMapper<>(Person.class)).stream().findAny();
    }

    public void addPerson(Person person) {
        template.update("INSERT INTO person (full_name, year_of_birth) VALUES (?, ?)",
                person.getFullName(), person.getYearOfBirth());
    }

    public void updatePerson(int id, Person person) {
        template.update("UPDATE person SET full_name=?, year_of_birth=? WHERE id=?",
                person.getFullName(), person.getYearOfBirth(), id);
    }

    public void deletePerson(int id) {
        template.update("DELETE from person WHERE id=?", id);
    }

    public List<Book> getBooksByPersonId(int id) {
        return template.query("SELECT * FROM book WHERE person_id=?", new Object[]{id},
                new BeanPropertyRowMapper<>(Book.class));
    }
}
