package com.hendisantika.springbootrestapipostgresql.BookRestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hendisantika.springbootrestapipostgresql.controller.BookRestController;
import com.hendisantika.springbootrestapipostgresql.entity.Book;
import com.hendisantika.springbootrestapipostgresql.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class BookRestControllerTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookRestController bookRestController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(bookRestController).build();
    }

    @Test
    public void testAddBook() throws Exception {
        Book book = new Book("Book Title", "Book Description");
        when(bookRepository.save(any(Book.class))).thenReturn(book);
        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(book)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(book.getName()));
    }

    @Test
    public void testGetAllBooks() throws Exception {
        Book book = new Book("Book Title", "Book Description");
        when(bookRepository.findAll()).thenReturn(Collections.singletonList(book));
        mockMvc.perform(get("/api/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value(book.getName()));
    }

    @Test
    public void testGetBookWithId() throws Exception {
        Book book = new Book("Book Title", "Book Description");
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        mockMvc.perform(get("/api/books/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(book.getName()));
    }

    @Test
    public void testFindBookWithName() throws Exception {
        Book book = new Book("Book Title", "Book Description");
        when(bookRepository.findByName("Book Title")).thenReturn(Collections.singletonList(book));
        mockMvc.perform(get("/api/books?name=Book Title"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value(book.getName()));
    }

    @Test
    public void testUpdateBookFromDB() throws Exception {
        Book book = new Book("Book Title", "Book Description");
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        book.setName("Updated Book Title");
        when(bookRepository.save(book)).thenReturn(book);
        mockMvc.perform(put("/api/books/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(book)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(book.getName()));
    }

    @Test
    public void testDeleteBookWithId() throws Exception {
        mockMvc.perform(delete("/api/books/1"))
                .andExpect(status().isOk());
    }

    @Test
    public void testDeleteAllBooks() throws Exception {
        mockMvc.perform(delete("/api/books"))
                .andExpect(status().isOk());
    }
}
