package com.trievosoftware.application.web.rest;

import com.trievosoftware.application.SiaApp;

import com.trievosoftware.application.domain.TempMutes;
import com.trievosoftware.application.repository.TempMutesRepository;
import com.trievosoftware.application.service.TempMutesService;
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
 * Test class for the TempMutesResource REST controller.
 *
 * @see TempMutesResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SiaApp.class)
public class TempMutesResourceIntTest {

    private static final Long DEFAULT_GUILD_ID = 1L;
    private static final Long UPDATED_GUILD_ID = 2L;

    private static final Long DEFAULT_USER_ID = 1L;
    private static final Long UPDATED_USER_ID = 2L;

    private static final Instant DEFAULT_FINISH = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FINISH = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private TempMutesRepository tempMutesRepository;

    @Autowired
    private TempMutesService tempMutesService;

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

    private MockMvc restTempMutesMockMvc;

    private TempMutes tempMutes;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TempMutesResource tempMutesResource = new TempMutesResource(tempMutesService);
        this.restTempMutesMockMvc = MockMvcBuilders.standaloneSetup(tempMutesResource)
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
    public static TempMutes createEntity(EntityManager em) {
        TempMutes tempMutes = new TempMutes()
            .guildId(DEFAULT_GUILD_ID)
            .userId(DEFAULT_USER_ID)
            .finish(DEFAULT_FINISH);
        return tempMutes;
    }

    @Before
    public void initTest() {
        tempMutes = createEntity(em);
    }

    @Test
    @Transactional
    public void createTempMutes() throws Exception {
        int databaseSizeBeforeCreate = tempMutesRepository.findAll().size();

        // Create the TempMutes
        restTempMutesMockMvc.perform(post("/api/temp-mutes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tempMutes)))
            .andExpect(status().isCreated());

        // Validate the TempMutes in the database
        List<TempMutes> tempMutesList = tempMutesRepository.findAll();
        assertThat(tempMutesList).hasSize(databaseSizeBeforeCreate + 1);
        TempMutes testTempMutes = tempMutesList.get(tempMutesList.size() - 1);
        assertThat(testTempMutes.getGuildId()).isEqualTo(DEFAULT_GUILD_ID);
        assertThat(testTempMutes.getUserId()).isEqualTo(DEFAULT_USER_ID);
        assertThat(testTempMutes.getFinish()).isEqualTo(DEFAULT_FINISH);
    }

    @Test
    @Transactional
    public void createTempMutesWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = tempMutesRepository.findAll().size();

        // Create the TempMutes with an existing ID
        tempMutes.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTempMutesMockMvc.perform(post("/api/temp-mutes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tempMutes)))
            .andExpect(status().isBadRequest());

        // Validate the TempMutes in the database
        List<TempMutes> tempMutesList = tempMutesRepository.findAll();
        assertThat(tempMutesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkGuildIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = tempMutesRepository.findAll().size();
        // set the field null
        tempMutes.setGuildId(null);

        // Create the TempMutes, which fails.

        restTempMutesMockMvc.perform(post("/api/temp-mutes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tempMutes)))
            .andExpect(status().isBadRequest());

        List<TempMutes> tempMutesList = tempMutesRepository.findAll();
        assertThat(tempMutesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkUserIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = tempMutesRepository.findAll().size();
        // set the field null
        tempMutes.setUserId(null);

        // Create the TempMutes, which fails.

        restTempMutesMockMvc.perform(post("/api/temp-mutes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tempMutes)))
            .andExpect(status().isBadRequest());

        List<TempMutes> tempMutesList = tempMutesRepository.findAll();
        assertThat(tempMutesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkFinishIsRequired() throws Exception {
        int databaseSizeBeforeTest = tempMutesRepository.findAll().size();
        // set the field null
        tempMutes.setFinish(null);

        // Create the TempMutes, which fails.

        restTempMutesMockMvc.perform(post("/api/temp-mutes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tempMutes)))
            .andExpect(status().isBadRequest());

        List<TempMutes> tempMutesList = tempMutesRepository.findAll();
        assertThat(tempMutesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTempMutes() throws Exception {
        // Initialize the database
        tempMutesRepository.saveAndFlush(tempMutes);

        // Get all the tempMutesList
        restTempMutesMockMvc.perform(get("/api/temp-mutes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tempMutes.getId().intValue())))
            .andExpect(jsonPath("$.[*].guildId").value(hasItem(DEFAULT_GUILD_ID.intValue())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID.intValue())))
            .andExpect(jsonPath("$.[*].finish").value(hasItem(DEFAULT_FINISH.toString())));
    }
    
    @Test
    @Transactional
    public void getTempMutes() throws Exception {
        // Initialize the database
        tempMutesRepository.saveAndFlush(tempMutes);

        // Get the tempMutes
        restTempMutesMockMvc.perform(get("/api/temp-mutes/{id}", tempMutes.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(tempMutes.getId().intValue()))
            .andExpect(jsonPath("$.guildId").value(DEFAULT_GUILD_ID.intValue()))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID.intValue()))
            .andExpect(jsonPath("$.finish").value(DEFAULT_FINISH.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingTempMutes() throws Exception {
        // Get the tempMutes
        restTempMutesMockMvc.perform(get("/api/temp-mutes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTempMutes() throws Exception {
        // Initialize the database
        tempMutesService.save(tempMutes);

        int databaseSizeBeforeUpdate = tempMutesRepository.findAll().size();

        // Update the tempMutes
        TempMutes updatedTempMutes = tempMutesRepository.findById(tempMutes.getId()).get();
        // Disconnect from session so that the updates on updatedTempMutes are not directly saved in db
        em.detach(updatedTempMutes);
        updatedTempMutes
            .guildId(UPDATED_GUILD_ID)
            .userId(UPDATED_USER_ID)
            .finish(UPDATED_FINISH);

        restTempMutesMockMvc.perform(put("/api/temp-mutes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedTempMutes)))
            .andExpect(status().isOk());

        // Validate the TempMutes in the database
        List<TempMutes> tempMutesList = tempMutesRepository.findAll();
        assertThat(tempMutesList).hasSize(databaseSizeBeforeUpdate);
        TempMutes testTempMutes = tempMutesList.get(tempMutesList.size() - 1);
        assertThat(testTempMutes.getGuildId()).isEqualTo(UPDATED_GUILD_ID);
        assertThat(testTempMutes.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testTempMutes.getFinish()).isEqualTo(UPDATED_FINISH);
    }

    @Test
    @Transactional
    public void updateNonExistingTempMutes() throws Exception {
        int databaseSizeBeforeUpdate = tempMutesRepository.findAll().size();

        // Create the TempMutes

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTempMutesMockMvc.perform(put("/api/temp-mutes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tempMutes)))
            .andExpect(status().isBadRequest());

        // Validate the TempMutes in the database
        List<TempMutes> tempMutesList = tempMutesRepository.findAll();
        assertThat(tempMutesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteTempMutes() throws Exception {
        // Initialize the database
        tempMutesService.save(tempMutes);

        int databaseSizeBeforeDelete = tempMutesRepository.findAll().size();

        // Delete the tempMutes
        restTempMutesMockMvc.perform(delete("/api/temp-mutes/{id}", tempMutes.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<TempMutes> tempMutesList = tempMutesRepository.findAll();
        assertThat(tempMutesList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TempMutes.class);
        TempMutes tempMutes1 = new TempMutes();
        tempMutes1.setId(1L);
        TempMutes tempMutes2 = new TempMutes();
        tempMutes2.setId(tempMutes1.getId());
        assertThat(tempMutes1).isEqualTo(tempMutes2);
        tempMutes2.setId(2L);
        assertThat(tempMutes1).isNotEqualTo(tempMutes2);
        tempMutes1.setId(null);
        assertThat(tempMutes1).isNotEqualTo(tempMutes2);
    }
}
