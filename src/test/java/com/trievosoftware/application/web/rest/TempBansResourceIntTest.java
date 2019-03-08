package com.trievosoftware.application.web.rest;

import com.trievosoftware.application.SiaApp;

import com.trievosoftware.application.domain.TempBans;
import com.trievosoftware.application.repository.TempBansRepository;
import com.trievosoftware.application.service.TempBansService;
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
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;


import static com.trievosoftware.application.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the TempBansResource REST controller.
 *
 * @see TempBansResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SiaApp.class)
public class TempBansResourceIntTest {

    private static final Long DEFAULT_GUILD_ID = 1L;
    private static final Long UPDATED_GUILD_ID = 2L;

    private static final Long DEFAULT_USER_ID = 1L;
    private static final Long UPDATED_USER_ID = 2L;

    private static final Instant DEFAULT_FINISH = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FINISH = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private TempBansRepository tempBansRepository;

    @Autowired
    private TempBansService tempBansService;

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

    private MockMvc restTempBansMockMvc;

    private TempBans tempBans;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TempBansResource tempBansResource = new TempBansResource(tempBansService);
        this.restTempBansMockMvc = MockMvcBuilders.standaloneSetup(tempBansResource)
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
    public static TempBans createEntity(EntityManager em) {
        TempBans tempBans = new TempBans()
            .guildId(DEFAULT_GUILD_ID)
            .userId(DEFAULT_USER_ID)
            .finish(DEFAULT_FINISH);
        return tempBans;
    }

    @Before
    public void initTest() {
        tempBans = createEntity(em);
    }

    @Test
    @Transactional
    public void createTempBans() throws Exception {
        int databaseSizeBeforeCreate = tempBansRepository.findAll().size();

        // Create the TempBans
        restTempBansMockMvc.perform(post("/api/temp-bans")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tempBans)))
            .andExpect(status().isCreated());

        // Validate the TempBans in the database
        List<TempBans> tempBansList = tempBansRepository.findAll();
        assertThat(tempBansList).hasSize(databaseSizeBeforeCreate + 1);
        TempBans testTempBans = tempBansList.get(tempBansList.size() - 1);
        assertThat(testTempBans.getGuildId()).isEqualTo(DEFAULT_GUILD_ID);
        assertThat(testTempBans.getUserId()).isEqualTo(DEFAULT_USER_ID);
        assertThat(testTempBans.getFinish()).isEqualTo(DEFAULT_FINISH);
    }

    @Test
    @Transactional
    public void createTempBansWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = tempBansRepository.findAll().size();

        // Create the TempBans with an existing ID
        tempBans.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTempBansMockMvc.perform(post("/api/temp-bans")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tempBans)))
            .andExpect(status().isBadRequest());

        // Validate the TempBans in the database
        List<TempBans> tempBansList = tempBansRepository.findAll();
        assertThat(tempBansList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkGuildIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = tempBansRepository.findAll().size();
        // set the field null
        tempBans.setGuildId(null);

        // Create the TempBans, which fails.

        restTempBansMockMvc.perform(post("/api/temp-bans")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tempBans)))
            .andExpect(status().isBadRequest());

        List<TempBans> tempBansList = tempBansRepository.findAll();
        assertThat(tempBansList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkUserIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = tempBansRepository.findAll().size();
        // set the field null
        tempBans.setUserId(null);

        // Create the TempBans, which fails.

        restTempBansMockMvc.perform(post("/api/temp-bans")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tempBans)))
            .andExpect(status().isBadRequest());

        List<TempBans> tempBansList = tempBansRepository.findAll();
        assertThat(tempBansList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkFinishIsRequired() throws Exception {
        int databaseSizeBeforeTest = tempBansRepository.findAll().size();
        // set the field null
        tempBans.setFinish(null);

        // Create the TempBans, which fails.

        restTempBansMockMvc.perform(post("/api/temp-bans")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tempBans)))
            .andExpect(status().isBadRequest());

        List<TempBans> tempBansList = tempBansRepository.findAll();
        assertThat(tempBansList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTempBans() throws Exception {
        // Initialize the database
        tempBansRepository.saveAndFlush(tempBans);

        // Get all the tempBansList
        restTempBansMockMvc.perform(get("/api/temp-bans?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tempBans.getId().intValue())))
            .andExpect(jsonPath("$.[*].guildId").value(hasItem(DEFAULT_GUILD_ID.intValue())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID.intValue())))
            .andExpect(jsonPath("$.[*].finish").value(hasItem(DEFAULT_FINISH.toString())));
    }
    
    @Test
    @Transactional
    public void getTempBans() throws Exception {
        // Initialize the database
        tempBansRepository.saveAndFlush(tempBans);

        // Get the tempBans
        restTempBansMockMvc.perform(get("/api/temp-bans/{id}", tempBans.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(tempBans.getId().intValue()))
            .andExpect(jsonPath("$.guildId").value(DEFAULT_GUILD_ID.intValue()))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID.intValue()))
            .andExpect(jsonPath("$.finish").value(DEFAULT_FINISH.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingTempBans() throws Exception {
        // Get the tempBans
        restTempBansMockMvc.perform(get("/api/temp-bans/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTempBans() throws Exception {
        // Initialize the database
        tempBansService.save(tempBans);

        int databaseSizeBeforeUpdate = tempBansRepository.findAll().size();

        // Update the tempBans
        TempBans updatedTempBans = tempBansRepository.findById(tempBans.getId()).get();
        // Disconnect from session so that the updates on updatedTempBans are not directly saved in db
        em.detach(updatedTempBans);
        updatedTempBans
            .guildId(UPDATED_GUILD_ID)
            .userId(UPDATED_USER_ID)
            .finish(UPDATED_FINISH);

        restTempBansMockMvc.perform(put("/api/temp-bans")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedTempBans)))
            .andExpect(status().isOk());

        // Validate the TempBans in the database
        List<TempBans> tempBansList = tempBansRepository.findAll();
        assertThat(tempBansList).hasSize(databaseSizeBeforeUpdate);
        TempBans testTempBans = tempBansList.get(tempBansList.size() - 1);
        assertThat(testTempBans.getGuildId()).isEqualTo(UPDATED_GUILD_ID);
        assertThat(testTempBans.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testTempBans.getFinish()).isEqualTo(UPDATED_FINISH);
    }

    @Test
    @Transactional
    public void updateNonExistingTempBans() throws Exception {
        int databaseSizeBeforeUpdate = tempBansRepository.findAll().size();

        // Create the TempBans

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTempBansMockMvc.perform(put("/api/temp-bans")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tempBans)))
            .andExpect(status().isBadRequest());

        // Validate the TempBans in the database
        List<TempBans> tempBansList = tempBansRepository.findAll();
        assertThat(tempBansList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteTempBans() throws Exception {
        // Initialize the database
        tempBansService.save(tempBans);

        int databaseSizeBeforeDelete = tempBansRepository.findAll().size();

        // Delete the tempBans
        restTempBansMockMvc.perform(delete("/api/temp-bans/{id}", tempBans.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<TempBans> tempBansList = tempBansRepository.findAll();
        assertThat(tempBansList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TempBans.class);
        TempBans tempBans1 = new TempBans();
        tempBans1.setId(1L);
        TempBans tempBans2 = new TempBans();
        tempBans2.setId(tempBans1.getId());
        assertThat(tempBans1).isEqualTo(tempBans2);
        tempBans2.setId(2L);
        assertThat(tempBans1).isNotEqualTo(tempBans2);
        tempBans1.setId(null);
        assertThat(tempBans1).isNotEqualTo(tempBans2);
    }
}
