package com.hendisantika.springbootrestapipostgresql.controller;

import com.hendisantika.springbootrestapipostgresql.entity.Author;
import com.hendisantika.springbootrestapipostgresql.repository.AuthorRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Optional;


@RestController
@RequestMapping("/api/author")
@Slf4j // Import log object from Lombok
public class AuthorRestController {

    @Autowired
    private AuthorRepository repository;

    @PostMapping
    public ResponseEntity<?> addAuthor(@RequestBody Author author) {
        log.info("Adding new author: {}", author);
        return new ResponseEntity<>(repository.save(author), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Collection<Author>> getAllAuthors() {
        log.info("Retrieving all authors");
        return new ResponseEntity<>(repository.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Author> getAuthorWithId(@PathVariable Long id) {
        log.info("Retrieving author with ID: {}", id);
        return new ResponseEntity<Author>(repository.findById(id).get(), HttpStatus.OK);
    }

    @GetMapping(params = {"name"})
    public ResponseEntity<Collection<Author>> findBookWithName(@RequestParam(value = "name") String name) {
        log.info("Searching author with name: {}", name);
        return new ResponseEntity<>(repository.findByName(name), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Author> updateAuthorFromDB(@PathVariable("id") long id, @RequestBody Author author) {
        log.info("Updating author with ID: {}", id);
        Optional<Author> currentAuthorOpt = repository.findById(id);
        Author currentAuthor = currentAuthorOpt.get();
        currentAuthor.setName(author.getName());
        currentAuthor.setISBN(author.getISBN());
        currentAuthor.setSurname(author.getSurname());
        return new ResponseEntity<>(repository.save(currentAuthor), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public void deleteAuthorWithId(@PathVariable Long id) {
        log.info("Deleting author with ID: {}", id);
        repository.deleteById(id);
    }

    @DeleteMapping
    public void deleteAllAuthors() {
        log.info("Deleting all authors");
        repository.deleteAll();
    }
}
