package com.trievosoftware.application.web.rest;

import com.trievosoftware.application.SiaApp;

import com.trievosoftware.application.domain.AuditCache;
import com.trievosoftware.application.repository.AuditCacheRepository;
import com.trievosoftware.application.service.AuditCacheService;
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
 * Test class for the AuditCacheResource REST controller.
 *
 * @see AuditCacheResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SiaApp.class)
public class AuditCacheResourceIntTest {

    private static final Long DEFAULT_GUILD_ID = 1L;
    private static final Long UPDATED_GUILD_ID = 2L;

    private static final Long DEFAULT_OLD = 1L;
    private static final Long UPDATED_OLD = 2L;

    private static final Long DEFAULT_OLDER = 1L;
    private static final Long UPDATED_OLDER = 2L;

    private static final Long DEFAULT_OLDEST = 1L;
    private static final Long UPDATED_OLDEST = 2L;

    @Autowired
    private AuditCacheRepository auditCacheRepository;

    @Autowired
    private AuditCacheService auditCacheService;

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

    private MockMvc restAuditCacheMockMvc;

    private AuditCache auditCache;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final AuditCacheResource auditCacheResource = new AuditCacheResource(auditCacheService);
        this.restAuditCacheMockMvc = MockMvcBuilders.standaloneSetup(auditCacheResource)
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
    public static AuditCache createEntity(EntityManager em) {
        AuditCache auditCache = new AuditCache()
            .guildId(DEFAULT_GUILD_ID)
            .old(DEFAULT_OLD)
            .older(DEFAULT_OLDER)
            .oldest(DEFAULT_OLDEST);
        return auditCache;
    }

    @Before
    public void initTest() {
        auditCache = createEntity(em);
    }

    @Test
    @Transactional
    public void createAuditCache() throws Exception {
        int databaseSizeBeforeCreate = auditCacheRepository.findAll().size();

        // Create the AuditCache
        restAuditCacheMockMvc.perform(post("/api/audit-caches")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(auditCache)))
            .andExpect(status().isCreated());

        // Validate the AuditCache in the database
        List<AuditCache> auditCacheList = auditCacheRepository.findAll();
        assertThat(auditCacheList).hasSize(databaseSizeBeforeCreate + 1);
        AuditCache testAuditCache = auditCacheList.get(auditCacheList.size() - 1);
        assertThat(testAuditCache.getGuildId()).isEqualTo(DEFAULT_GUILD_ID);
        assertThat(testAuditCache.getOld()).isEqualTo(DEFAULT_OLD);
        assertThat(testAuditCache.getOlder()).isEqualTo(DEFAULT_OLDER);
        assertThat(testAuditCache.getOldest()).isEqualTo(DEFAULT_OLDEST);
    }

    @Test
    @Transactional
    public void createAuditCacheWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = auditCacheRepository.findAll().size();

        // Create the AuditCache with an existing ID
        auditCache.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAuditCacheMockMvc.perform(post("/api/audit-caches")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(auditCache)))
            .andExpect(status().isBadRequest());

        // Validate the AuditCache in the database
        List<AuditCache> auditCacheList = auditCacheRepository.findAll();
        assertThat(auditCacheList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkGuildIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = auditCacheRepository.findAll().size();
        // set the field null
        auditCache.setGuildId(null);

        // Create the AuditCache, which fails.

        restAuditCacheMockMvc.perform(post("/api/audit-caches")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(auditCache)))
            .andExpect(status().isBadRequest());

        List<AuditCache> auditCacheList = auditCacheRepository.findAll();
        assertThat(auditCacheList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkOldIsRequired() throws Exception {
        int databaseSizeBeforeTest = auditCacheRepository.findAll().size();
        // set the field null
        auditCache.setOld(null);

        // Create the AuditCache, which fails.

        restAuditCacheMockMvc.perform(post("/api/audit-caches")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(auditCache)))
            .andExpect(status().isBadRequest());

        List<AuditCache> auditCacheList = auditCacheRepository.findAll();
        assertThat(auditCacheList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkOlderIsRequired() throws Exception {
        int databaseSizeBeforeTest = auditCacheRepository.findAll().size();
        // set the field null
        auditCache.setOlder(null);

        // Create the AuditCache, which fails.

        restAuditCacheMockMvc.perform(post("/api/audit-caches")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(auditCache)))
            .andExpect(status().isBadRequest());

        List<AuditCache> auditCacheList = auditCacheRepository.findAll();
        assertThat(auditCacheList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkOldestIsRequired() throws Exception {
        int databaseSizeBeforeTest = auditCacheRepository.findAll().size();
        // set the field null
        auditCache.setOldest(null);

        // Create the AuditCache, which fails.

        restAuditCacheMockMvc.perform(post("/api/audit-caches")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(auditCache)))
            .andExpect(status().isBadRequest());

        List<AuditCache> auditCacheList = auditCacheRepository.findAll();
        assertThat(auditCacheList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAuditCaches() throws Exception {
        // Initialize the database
        auditCacheRepository.saveAndFlush(auditCache);

        // Get all the auditCacheList
        restAuditCacheMockMvc.perform(get("/api/audit-caches?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(auditCache.getId().intValue())))
            .andExpect(jsonPath("$.[*].guildId").value(hasItem(DEFAULT_GUILD_ID.intValue())))
            .andExpect(jsonPath("$.[*].old").value(hasItem(DEFAULT_OLD.intValue())))
            .andExpect(jsonPath("$.[*].older").value(hasItem(DEFAULT_OLDER.intValue())))
            .andExpect(jsonPath("$.[*].oldest").value(hasItem(DEFAULT_OLDEST.intValue())));
    }
    
    @Test
    @Transactional
    public void getAuditCache() throws Exception {
        // Initialize the database
        auditCacheRepository.saveAndFlush(auditCache);

        // Get the auditCache
        restAuditCacheMockMvc.perform(get("/api/audit-caches/{id}", auditCache.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(auditCache.getId().intValue()))
            .andExpect(jsonPath("$.guildId").value(DEFAULT_GUILD_ID.intValue()))
            .andExpect(jsonPath("$.old").value(DEFAULT_OLD.intValue()))
            .andExpect(jsonPath("$.older").value(DEFAULT_OLDER.intValue()))
            .andExpect(jsonPath("$.oldest").value(DEFAULT_OLDEST.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingAuditCache() throws Exception {
        // Get the auditCache
        restAuditCacheMockMvc.perform(get("/api/audit-caches/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAuditCache() throws Exception {
        // Initialize the database
        auditCacheService.save(auditCache);

        int databaseSizeBeforeUpdate = auditCacheRepository.findAll().size();

        // Update the auditCache
        AuditCache updatedAuditCache = auditCacheRepository.findById(auditCache.getId()).get();
        // Disconnect from session so that the updates on updatedAuditCache are not directly saved in db
        em.detach(updatedAuditCache);
        updatedAuditCache
            .guildId(UPDATED_GUILD_ID)
            .old(UPDATED_OLD)
            .older(UPDATED_OLDER)
            .oldest(UPDATED_OLDEST);

        restAuditCacheMockMvc.perform(put("/api/audit-caches")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedAuditCache)))
            .andExpect(status().isOk());

        // Validate the AuditCache in the database
        List<AuditCache> auditCacheList = auditCacheRepository.findAll();
        assertThat(auditCacheList).hasSize(databaseSizeBeforeUpdate);
        AuditCache testAuditCache = auditCacheList.get(auditCacheList.size() - 1);
        assertThat(testAuditCache.getGuildId()).isEqualTo(UPDATED_GUILD_ID);
        assertThat(testAuditCache.getOld()).isEqualTo(UPDATED_OLD);
        assertThat(testAuditCache.getOlder()).isEqualTo(UPDATED_OLDER);
        assertThat(testAuditCache.getOldest()).isEqualTo(UPDATED_OLDEST);
    }

    @Test
    @Transactional
    public void updateNonExistingAuditCache() throws Exception {
        int databaseSizeBeforeUpdate = auditCacheRepository.findAll().size();

        // Create the AuditCache

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAuditCacheMockMvc.perform(put("/api/audit-caches")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(auditCache)))
            .andExpect(status().isBadRequest());

        // Validate the AuditCache in the database
        List<AuditCache> auditCacheList = auditCacheRepository.findAll();
        assertThat(auditCacheList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteAuditCache() throws Exception {
        // Initialize the database
        auditCacheService.save(auditCache);

        int databaseSizeBeforeDelete = auditCacheRepository.findAll().size();

        // Delete the auditCache
        restAuditCacheMockMvc.perform(delete("/api/audit-caches/{id}", auditCache.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<AuditCache> auditCacheList = auditCacheRepository.findAll();
        assertThat(auditCacheList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AuditCache.class);
        AuditCache auditCache1 = new AuditCache();
        auditCache1.setId(1L);
        AuditCache auditCache2 = new AuditCache();
        auditCache2.setId(auditCache1.getId());
        assertThat(auditCache1).isEqualTo(auditCache2);
        auditCache2.setId(2L);
        assertThat(auditCache1).isNotEqualTo(auditCache2);
        auditCache1.setId(null);
        assertThat(auditCache1).isNotEqualTo(auditCache2);
    }
}
