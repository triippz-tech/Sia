package com.trievosoftware.application.web.rest;

import com.trievosoftware.application.SiaApp;

import com.trievosoftware.application.domain.Ignored;
import com.trievosoftware.application.repository.IgnoredRepository;
import com.trievosoftware.application.service.IgnoredService;
import com.trievosoftware.application.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.List;


import static com.trievosoftware.application.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the IgnoredResource REST controller.
 *
 * @see IgnoredResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SiaApp.class)
public class IgnoredResourceIntTest {

    private static final Long DEFAULT_GUILD_ID = 1L;
    private static final Long UPDATED_GUILD_ID = 2L;

    private static final Long DEFAULT_ENTITY_ID = 1L;
    private static final Long UPDATED_ENTITY_ID = 2L;

    private static final Integer DEFAULT_TYPE = 1;
    private static final Integer UPDATED_TYPE = 2;

    @Autowired
    private IgnoredRepository ignoredRepository;

    @Autowired
    private IgnoredService ignoredService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restIgnoredMockMvc;

    private Ignored ignored;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final IgnoredResource ignoredResource = new IgnoredResource(ignoredService);
        this.restIgnoredMockMvc = MockMvcBuilders.standaloneSetup(ignoredResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ignored createEntity(EntityManager em) {
        Ignored ignored = new Ignored()
            .guildId(DEFAULT_GUILD_ID)
            .entityId(DEFAULT_ENTITY_ID)
            .type(DEFAULT_TYPE);
        return ignored;
    }

    @Before
    public void initTest() {
        ignored = createEntity(em);
    }

    @Test
    @Transactional
    public void createIgnored() throws Exception {
        int databaseSizeBeforeCreate = ignoredRepository.findAll().size();

        // Create the Ignored
        restIgnoredMockMvc.perform(post("/api/ignoreds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ignored)))
            .andExpect(status().isCreated());

        // Validate the Ignored in the database
        List<Ignored> ignoredList = ignoredRepository.findAll();
        assertThat(ignoredList).hasSize(databaseSizeBeforeCreate + 1);
        Ignored testIgnored = ignoredList.get(ignoredList.size() - 1);
        assertThat(testIgnored.getGuildId()).isEqualTo(DEFAULT_GUILD_ID);
        assertThat(testIgnored.getEntityId()).isEqualTo(DEFAULT_ENTITY_ID);
        assertThat(testIgnored.getType()).isEqualTo(DEFAULT_TYPE);
    }

    @Test
    @Transactional
    public void createIgnoredWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = ignoredRepository.findAll().size();

        // Create the Ignored with an existing ID
        ignored.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restIgnoredMockMvc.perform(post("/api/ignoreds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ignored)))
            .andExpect(status().isBadRequest());

        // Validate the Ignored in the database
        List<Ignored> ignoredList = ignoredRepository.findAll();
        assertThat(ignoredList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkGuildIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = ignoredRepository.findAll().size();
        // set the field null
        ignored.setGuildId(null);

        // Create the Ignored, which fails.

        restIgnoredMockMvc.perform(post("/api/ignoreds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ignored)))
            .andExpect(status().isBadRequest());

        List<Ignored> ignoredList = ignoredRepository.findAll();
        assertThat(ignoredList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEntityIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = ignoredRepository.findAll().size();
        // set the field null
        ignored.setEntityId(null);

        // Create the Ignored, which fails.

        restIgnoredMockMvc.perform(post("/api/ignoreds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ignored)))
            .andExpect(status().isBadRequest());

        List<Ignored> ignoredList = ignoredRepository.findAll();
        assertThat(ignoredList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = ignoredRepository.findAll().size();
        // set the field null
        ignored.setType(null);

        // Create the Ignored, which fails.

        restIgnoredMockMvc.perform(post("/api/ignoreds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ignored)))
            .andExpect(status().isBadRequest());

        List<Ignored> ignoredList = ignoredRepository.findAll();
        assertThat(ignoredList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllIgnoreds() throws Exception {
        // Initialize the database
        ignoredRepository.saveAndFlush(ignored);

        // Get all the ignoredList
        restIgnoredMockMvc.perform(get("/api/ignoreds?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ignored.getId().intValue())))
            .andExpect(jsonPath("$.[*].guildId").value(hasItem(DEFAULT_GUILD_ID.intValue())))
            .andExpect(jsonPath("$.[*].entityId").value(hasItem(DEFAULT_ENTITY_ID.intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)));
    }
    
    @Test
    @Transactional
    public void getIgnored() throws Exception {
        // Initialize the database
        ignoredRepository.saveAndFlush(ignored);

        // Get the ignored
        restIgnoredMockMvc.perform(get("/api/ignoreds/{id}", ignored.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(ignored.getId().intValue()))
            .andExpect(jsonPath("$.guildId").value(DEFAULT_GUILD_ID.intValue()))
            .andExpect(jsonPath("$.entityId").value(DEFAULT_ENTITY_ID.intValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE));
    }

    @Test
    @Transactional
    public void getNonExistingIgnored() throws Exception {
        // Get the ignored
        restIgnoredMockMvc.perform(get("/api/ignoreds/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateIgnored() throws Exception {
        // Initialize the database
        ignoredService.save(ignored);

        int databaseSizeBeforeUpdate = ignoredRepository.findAll().size();

        // Update the ignored
        Ignored updatedIgnored = ignoredRepository.findById(ignored.getId()).get();
        // Disconnect from session so that the updates on updatedIgnored are not directly saved in db
        em.detach(updatedIgnored);
        updatedIgnored
            .guildId(UPDATED_GUILD_ID)
            .entityId(UPDATED_ENTITY_ID)
            .type(UPDATED_TYPE);

        restIgnoredMockMvc.perform(put("/api/ignoreds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedIgnored)))
            .andExpect(status().isOk());

        // Validate the Ignored in the database
        List<Ignored> ignoredList = ignoredRepository.findAll();
        assertThat(ignoredList).hasSize(databaseSizeBeforeUpdate);
        Ignored testIgnored = ignoredList.get(ignoredList.size() - 1);
        assertThat(testIgnored.getGuildId()).isEqualTo(UPDATED_GUILD_ID);
        assertThat(testIgnored.getEntityId()).isEqualTo(UPDATED_ENTITY_ID);
        assertThat(testIgnored.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void updateNonExistingIgnored() throws Exception {
        int databaseSizeBeforeUpdate = ignoredRepository.findAll().size();

        // Create the Ignored

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIgnoredMockMvc.perform(put("/api/ignoreds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ignored)))
            .andExpect(status().isBadRequest());

        // Validate the Ignored in the database
        List<Ignored> ignoredList = ignoredRepository.findAll();
        assertThat(ignoredList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteIgnored() throws Exception {
        // Initialize the database
        ignoredService.save(ignored);

        int databaseSizeBeforeDelete = ignoredRepository.findAll().size();

        // Delete the ignored
        restIgnoredMockMvc.perform(delete("/api/ignoreds/{id}", ignored.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Ignored> ignoredList = ignoredRepository.findAll();
        assertThat(ignoredList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Ignored.class);
        Ignored ignored1 = new Ignored();
        ignored1.setId(1L);
        Ignored ignored2 = new Ignored();
        ignored2.setId(ignored1.getId());
        assertThat(ignored1).isEqualTo(ignored2);
        ignored2.setId(2L);
        assertThat(ignored1).isNotEqualTo(ignored2);
        ignored1.setId(null);
        assertThat(ignored1).isNotEqualTo(ignored2);
    }
}
