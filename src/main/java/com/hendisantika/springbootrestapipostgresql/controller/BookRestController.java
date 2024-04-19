package com.hendisantika.springbootrestapipostgresql.controller;

import com.hendisantika.springbootrestapipostgresql.entity.Book;
import com.hendisantika.springbootrestapipostgresql.repository.BookRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Optional;

@RestController
@RequestMapping("/api/books")
@Slf4j // Import log object from Lombok
public class BookRestController {

    @Autowired
    private BookRepository repository;

    @PostMapping
    public ResponseEntity<?> addBook(@RequestBody Book book) {
        log.info("Adding new book: {}", book);
        return new ResponseEntity<>(repository.save(book), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Collection<Book>> getAllBooks() {
        log.info("Retrieving all books");
        return new ResponseEntity<>(repository.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookWithId(@PathVariable Long id) {
        log.info("Retrieving book with ID: {}", id);
        return new ResponseEntity<Book>(repository.findById(id).get(), HttpStatus.OK);
    }

    @GetMapping(params = {"name"})
    public ResponseEntity<Collection<Book>> findBookWithName(@RequestParam(value = "name") String name) {
        log.info("Searching book with name: {}", name);
        return new ResponseEntity<>(repository.findByName(name), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBookFromDB(@PathVariable("id") long id, @RequestBody Book book) {
        log.info("Updating book with ID: {}", id);
        Optional<Book> currentBookOpt = repository.findById(id);
        Book currentBook = currentBookOpt.get();
        currentBook.setName(book.getName());
        currentBook.setDescription(book.getDescription());
        currentBook.setTags(book.getTags());
        return new ResponseEntity<>(repository.save(currentBook), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public void deleteBookWithId(@PathVariable Long id) {
        log.info("Deleting book with ID: {}", id);
        repository.deleteById(id);
    }

    @DeleteMapping
    public void deleteAllBooks() {
        log.info("Deleting all books");
        repository.deleteAll();
    }
}
