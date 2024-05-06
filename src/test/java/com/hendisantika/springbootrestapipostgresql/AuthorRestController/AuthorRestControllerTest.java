package com.hendisantika.springbootrestapipostgresql.AuthorRestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hendisantika.springbootrestapipostgresql.controller.AuthorRestController;
import com.hendisantika.springbootrestapipostgresql.entity.Author;
import com.hendisantika.springbootrestapipostgresql.repository.AuthorRepository;
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

public class AuthorRestControllerTest {

    @Mock
    private AuthorRepository authorRepository;

    @InjectMocks
    private AuthorRestController authorRestController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(authorRestController).build();
    }

    @Test
    public void testAddAuthor() throws Exception {
        Author author = new Author("John", "Doe", "123456");
        when(authorRepository.save(any(Author.class))).thenReturn(author);
        mockMvc.perform(post("/api/author")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(author)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(author.getName()));
    }

    @Test
    public void testGetAllAuthors() throws Exception {
        Author author = new Author("John", "Doe", "123456");
        when(authorRepository.findAll()).thenReturn(Collections.singletonList(author));
        mockMvc.perform(get("/api/author"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value(author.getName()));
    }

    @Test
    public void testGetAuthorWithId() throws Exception {
        Author author = new Author("John", "Doe", "123456");
        when(authorRepository.findById(1L)).thenReturn(Optional.of(author));
        mockMvc.perform(get("/api/author/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(author.getName()));
    }

    @Test
    public void testFindAuthorWithName() throws Exception {
        Author author = new Author("John", "Doe", "123456");
        when(authorRepository.findByName("John")).thenReturn(Collections.singletonList(author));
        mockMvc.perform(get("/api/author?name=John"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value(author.getName()));
    }

    @Test
    public void testUpdateAuthorFromDB() throws Exception {
        Author author = new Author("John", "Doe", "123456");
        when(authorRepository.findById(1L)).thenReturn(Optional.of(author));
        author.setName("Updated Name");
        when(authorRepository.save(author)).thenReturn(author);
        mockMvc.perform(put("/api/author/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(author)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(author.getName()));
    }

    @Test
    public void testDeleteAuthorWithId() throws Exception {
        mockMvc.perform(delete("/api/author/1"))
                .andExpect(status().isOk());
    }

    @Test
    public void testDeleteAllAuthors() throws Exception {
        mockMvc.perform(delete("/api/author"))
                .andExpect(status().isOk());
    }
}
