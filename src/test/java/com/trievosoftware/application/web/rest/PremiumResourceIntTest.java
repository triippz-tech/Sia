package com.trievosoftware.application.web.rest;

import com.trievosoftware.application.SiaApp;

import com.trievosoftware.application.domain.Premium;
import com.trievosoftware.application.repository.PremiumRepository;
import com.trievosoftware.application.service.PremiumService;
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
 * Test class for the PremiumResource REST controller.
 *
 * @see PremiumResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SiaApp.class)
public class PremiumResourceIntTest {

    private static final Long DEFAULT_GUILD_ID = 1L;
    private static final Long UPDATED_GUILD_ID = 2L;

    private static final Instant DEFAULT_UNTIL = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UNTIL = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Integer DEFAULT_LEVEL = 1;
    private static final Integer UPDATED_LEVEL = 2;

    @Autowired
    private PremiumRepository premiumRepository;

    @Autowired
    private PremiumService premiumService;

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

    private MockMvc restPremiumMockMvc;

    private Premium premium;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PremiumResource premiumResource = new PremiumResource(premiumService);
        this.restPremiumMockMvc = MockMvcBuilders.standaloneSetup(premiumResource)
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
    public static Premium createEntity(EntityManager em) {
        Premium premium = new Premium()
            .guildId(DEFAULT_GUILD_ID)
            .until(DEFAULT_UNTIL)
            .level(DEFAULT_LEVEL);
        return premium;
    }

    @Before
    public void initTest() {
        premium = createEntity(em);
    }

    @Test
    @Transactional
    public void createPremium() throws Exception {
        int databaseSizeBeforeCreate = premiumRepository.findAll().size();

        // Create the Premium
        restPremiumMockMvc.perform(post("/api/premiums")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(premium)))
            .andExpect(status().isCreated());

        // Validate the Premium in the database
        List<Premium> premiumList = premiumRepository.findAll();
        assertThat(premiumList).hasSize(databaseSizeBeforeCreate + 1);
        Premium testPremium = premiumList.get(premiumList.size() - 1);
        assertThat(testPremium.getGuildId()).isEqualTo(DEFAULT_GUILD_ID);
        assertThat(testPremium.getUntil()).isEqualTo(DEFAULT_UNTIL);
        assertThat(testPremium.getLevel()).isEqualTo(DEFAULT_LEVEL);
    }

    @Test
    @Transactional
    public void createPremiumWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = premiumRepository.findAll().size();

        // Create the Premium with an existing ID
        premium.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPremiumMockMvc.perform(post("/api/premiums")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(premium)))
            .andExpect(status().isBadRequest());

        // Validate the Premium in the database
        List<Premium> premiumList = premiumRepository.findAll();
        assertThat(premiumList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkGuildIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = premiumRepository.findAll().size();
        // set the field null
        premium.setGuildId(null);

        // Create the Premium, which fails.

        restPremiumMockMvc.perform(post("/api/premiums")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(premium)))
            .andExpect(status().isBadRequest());

        List<Premium> premiumList = premiumRepository.findAll();
        assertThat(premiumList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkUntilIsRequired() throws Exception {
        int databaseSizeBeforeTest = premiumRepository.findAll().size();
        // set the field null
        premium.setUntil(null);

        // Create the Premium, which fails.

        restPremiumMockMvc.perform(post("/api/premiums")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(premium)))
            .andExpect(status().isBadRequest());

        List<Premium> premiumList = premiumRepository.findAll();
        assertThat(premiumList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLevelIsRequired() throws Exception {
        int databaseSizeBeforeTest = premiumRepository.findAll().size();
        // set the field null
        premium.setLevel(null);

        // Create the Premium, which fails.

        restPremiumMockMvc.perform(post("/api/premiums")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(premium)))
            .andExpect(status().isBadRequest());

        List<Premium> premiumList = premiumRepository.findAll();
        assertThat(premiumList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPremiums() throws Exception {
        // Initialize the database
        premiumRepository.saveAndFlush(premium);

        // Get all the premiumList
        restPremiumMockMvc.perform(get("/api/premiums?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(premium.getId().intValue())))
            .andExpect(jsonPath("$.[*].guildId").value(hasItem(DEFAULT_GUILD_ID.intValue())))
            .andExpect(jsonPath("$.[*].until").value(hasItem(DEFAULT_UNTIL.toString())))
            .andExpect(jsonPath("$.[*].level").value(hasItem(DEFAULT_LEVEL)));
    }
    
    @Test
    @Transactional
    public void getPremium() throws Exception {
        // Initialize the database
        premiumRepository.saveAndFlush(premium);

        // Get the premium
        restPremiumMockMvc.perform(get("/api/premiums/{id}", premium.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(premium.getId().intValue()))
            .andExpect(jsonPath("$.guildId").value(DEFAULT_GUILD_ID.intValue()))
            .andExpect(jsonPath("$.until").value(DEFAULT_UNTIL.toString()))
            .andExpect(jsonPath("$.level").value(DEFAULT_LEVEL));
    }

    @Test
    @Transactional
    public void getNonExistingPremium() throws Exception {
        // Get the premium
        restPremiumMockMvc.perform(get("/api/premiums/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePremium() throws Exception {
        // Initialize the database
        premiumService.save(premium);

        int databaseSizeBeforeUpdate = premiumRepository.findAll().size();

        // Update the premium
        Premium updatedPremium = premiumRepository.findById(premium.getId()).get();
        // Disconnect from session so that the updates on updatedPremium are not directly saved in db
        em.detach(updatedPremium);
        updatedPremium
            .guildId(UPDATED_GUILD_ID)
            .until(UPDATED_UNTIL)
            .level(UPDATED_LEVEL);

        restPremiumMockMvc.perform(put("/api/premiums")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedPremium)))
            .andExpect(status().isOk());

        // Validate the Premium in the database
        List<Premium> premiumList = premiumRepository.findAll();
        assertThat(premiumList).hasSize(databaseSizeBeforeUpdate);
        Premium testPremium = premiumList.get(premiumList.size() - 1);
        assertThat(testPremium.getGuildId()).isEqualTo(UPDATED_GUILD_ID);
        assertThat(testPremium.getUntil()).isEqualTo(UPDATED_UNTIL);
        assertThat(testPremium.getLevel()).isEqualTo(UPDATED_LEVEL);
    }

    @Test
    @Transactional
    public void updateNonExistingPremium() throws Exception {
        int databaseSizeBeforeUpdate = premiumRepository.findAll().size();

        // Create the Premium

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPremiumMockMvc.perform(put("/api/premiums")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(premium)))
            .andExpect(status().isBadRequest());

        // Validate the Premium in the database
        List<Premium> premiumList = premiumRepository.findAll();
        assertThat(premiumList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deletePremium() throws Exception {
        // Initialize the database
        premiumService.save(premium);

        int databaseSizeBeforeDelete = premiumRepository.findAll().size();

        // Delete the premium
        restPremiumMockMvc.perform(delete("/api/premiums/{id}", premium.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Premium> premiumList = premiumRepository.findAll();
        assertThat(premiumList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Premium.class);
        Premium premium1 = new Premium();
        premium1.setId(1L);
        Premium premium2 = new Premium();
        premium2.setId(premium1.getId());
        assertThat(premium1).isEqualTo(premium2);
        premium2.setId(2L);
        assertThat(premium1).isNotEqualTo(premium2);
        premium1.setId(null);
        assertThat(premium1).isNotEqualTo(premium2);
    }
}
