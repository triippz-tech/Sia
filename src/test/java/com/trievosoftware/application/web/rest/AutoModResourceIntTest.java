package com.trievosoftware.application.web.rest;

import com.trievosoftware.application.SiaApp;

import com.trievosoftware.application.domain.AutoMod;
import com.trievosoftware.application.repository.AutoModRepository;
import com.trievosoftware.application.service.AutoModService;
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
 * Test class for the AutoModResource REST controller.
 *
 * @see AutoModResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SiaApp.class)
public class AutoModResourceIntTest {

    private static final Long DEFAULT_GUILD_ID = 1L;
    private static final Long UPDATED_GUILD_ID = 2L;

    private static final Boolean DEFAULT_RESOLVE_URLS = false;
    private static final Boolean UPDATED_RESOLVE_URLS = true;

    private static final Integer DEFAULT_MAX_MENTIONS = 1;
    private static final Integer UPDATED_MAX_MENTIONS = 2;

    private static final Integer DEFAULT_MAX_ROLE_MENTIONS = 1;
    private static final Integer UPDATED_MAX_ROLE_MENTIONS = 2;

    private static final Integer DEFAULT_MAX_LINES = 1;
    private static final Integer UPDATED_MAX_LINES = 2;

    private static final Integer DEFAULT_RAID_MODE_NUMBER = 1;
    private static final Integer UPDATED_RAID_MODE_NUMBER = 2;

    private static final Integer DEFAULT_RAID_MODE_TIME = 1;
    private static final Integer UPDATED_RAID_MODE_TIME = 2;

    private static final Integer DEFAULT_INVITE_STRIKES = 1;
    private static final Integer UPDATED_INVITE_STRIKES = 2;

    private static final Integer DEFAULT_REF_STRIKES = 1;
    private static final Integer UPDATED_REF_STRIKES = 2;

    private static final Integer DEFAULT_COPY_PASTA_STRIKES = 1;
    private static final Integer UPDATED_COPY_PASTA_STRIKES = 2;

    private static final Integer DEFAULT_EVERYONE_STRIKES = 1;
    private static final Integer UPDATED_EVERYONE_STRIKES = 2;

    private static final Integer DEFAULT_DUPE_STRIKES = 1;
    private static final Integer UPDATED_DUPE_STRIKES = 2;

    private static final Integer DEFAULT_DUPE_DELETE_THRESH = 1;
    private static final Integer UPDATED_DUPE_DELETE_THRESH = 2;

    private static final Integer DEFAULT_DUPE_STRIKES_THRESH = 1;
    private static final Integer UPDATED_DUPE_STRIKES_THRESH = 2;

    private static final Integer DEFAULT_DEHOIST_CHAR = 1;
    private static final Integer UPDATED_DEHOIST_CHAR = 2;

    @Autowired
    private AutoModRepository autoModRepository;

    @Autowired
    private AutoModService autoModService;

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

    private MockMvc restAutoModMockMvc;

    private AutoMod autoMod;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final AutoModResource autoModResource = new AutoModResource(autoModService);
        this.restAutoModMockMvc = MockMvcBuilders.standaloneSetup(autoModResource)
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
    public static AutoMod createEntity(EntityManager em) {
        AutoMod autoMod = new AutoMod()
            .guildId(DEFAULT_GUILD_ID)
            .resolveUrls(DEFAULT_RESOLVE_URLS)
            .maxMentions(DEFAULT_MAX_MENTIONS)
            .maxRoleMentions(DEFAULT_MAX_ROLE_MENTIONS)
            .maxLines(DEFAULT_MAX_LINES)
            .raidModeNumber(DEFAULT_RAID_MODE_NUMBER)
            .raidModeTime(DEFAULT_RAID_MODE_TIME)
            .inviteStrikes(DEFAULT_INVITE_STRIKES)
            .refStrikes(DEFAULT_REF_STRIKES)
            .copyPastaStrikes(DEFAULT_COPY_PASTA_STRIKES)
            .everyoneStrikes(DEFAULT_EVERYONE_STRIKES)
            .dupeStrikes(DEFAULT_DUPE_STRIKES)
            .dupeDeleteThresh(DEFAULT_DUPE_DELETE_THRESH)
            .dupeStrikesThresh(DEFAULT_DUPE_STRIKES_THRESH)
            .dehoistChar(DEFAULT_DEHOIST_CHAR);
        return autoMod;
    }

    @Before
    public void initTest() {
        autoMod = createEntity(em);
    }

    @Test
    @Transactional
    public void createAutoMod() throws Exception {
        int databaseSizeBeforeCreate = autoModRepository.findAll().size();

        // Create the AutoMod
        restAutoModMockMvc.perform(post("/api/auto-mods")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(autoMod)))
            .andExpect(status().isCreated());

        // Validate the AutoMod in the database
        List<AutoMod> autoModList = autoModRepository.findAll();
        assertThat(autoModList).hasSize(databaseSizeBeforeCreate + 1);
        AutoMod testAutoMod = autoModList.get(autoModList.size() - 1);
        assertThat(testAutoMod.getGuildId()).isEqualTo(DEFAULT_GUILD_ID);
        assertThat(testAutoMod.isResolveUrls()).isEqualTo(DEFAULT_RESOLVE_URLS);
        assertThat(testAutoMod.getMaxMentions()).isEqualTo(DEFAULT_MAX_MENTIONS);
        assertThat(testAutoMod.getMaxRoleMentions()).isEqualTo(DEFAULT_MAX_ROLE_MENTIONS);
        assertThat(testAutoMod.getMaxLines()).isEqualTo(DEFAULT_MAX_LINES);
        assertThat(testAutoMod.getRaidModeNumber()).isEqualTo(DEFAULT_RAID_MODE_NUMBER);
        assertThat(testAutoMod.getRaidModeTime()).isEqualTo(DEFAULT_RAID_MODE_TIME);
        assertThat(testAutoMod.getInviteStrikes()).isEqualTo(DEFAULT_INVITE_STRIKES);
        assertThat(testAutoMod.getRefStrikes()).isEqualTo(DEFAULT_REF_STRIKES);
        assertThat(testAutoMod.getCopyPastaStrikes()).isEqualTo(DEFAULT_COPY_PASTA_STRIKES);
        assertThat(testAutoMod.getEveryoneStrikes()).isEqualTo(DEFAULT_EVERYONE_STRIKES);
        assertThat(testAutoMod.getDupeStrikes()).isEqualTo(DEFAULT_DUPE_STRIKES);
        assertThat(testAutoMod.getDupeDeleteThresh()).isEqualTo(DEFAULT_DUPE_DELETE_THRESH);
        assertThat(testAutoMod.getDupeStrikesThresh()).isEqualTo(DEFAULT_DUPE_STRIKES_THRESH);
        assertThat(testAutoMod.getDehoistChar()).isEqualTo(DEFAULT_DEHOIST_CHAR);
    }

    @Test
    @Transactional
    public void createAutoModWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = autoModRepository.findAll().size();

        // Create the AutoMod with an existing ID
        autoMod.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAutoModMockMvc.perform(post("/api/auto-mods")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(autoMod)))
            .andExpect(status().isBadRequest());

        // Validate the AutoMod in the database
        List<AutoMod> autoModList = autoModRepository.findAll();
        assertThat(autoModList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkGuildIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = autoModRepository.findAll().size();
        // set the field null
        autoMod.setGuildId(null);

        // Create the AutoMod, which fails.

        restAutoModMockMvc.perform(post("/api/auto-mods")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(autoMod)))
            .andExpect(status().isBadRequest());

        List<AutoMod> autoModList = autoModRepository.findAll();
        assertThat(autoModList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkResolveUrlsIsRequired() throws Exception {
        int databaseSizeBeforeTest = autoModRepository.findAll().size();
        // set the field null
        autoMod.setResolveUrls(null);

        // Create the AutoMod, which fails.

        restAutoModMockMvc.perform(post("/api/auto-mods")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(autoMod)))
            .andExpect(status().isBadRequest());

        List<AutoMod> autoModList = autoModRepository.findAll();
        assertThat(autoModList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkMaxMentionsIsRequired() throws Exception {
        int databaseSizeBeforeTest = autoModRepository.findAll().size();
        // set the field null
        autoMod.setMaxMentions(null);

        // Create the AutoMod, which fails.

        restAutoModMockMvc.perform(post("/api/auto-mods")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(autoMod)))
            .andExpect(status().isBadRequest());

        List<AutoMod> autoModList = autoModRepository.findAll();
        assertThat(autoModList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkMaxRoleMentionsIsRequired() throws Exception {
        int databaseSizeBeforeTest = autoModRepository.findAll().size();
        // set the field null
        autoMod.setMaxRoleMentions(null);

        // Create the AutoMod, which fails.

        restAutoModMockMvc.perform(post("/api/auto-mods")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(autoMod)))
            .andExpect(status().isBadRequest());

        List<AutoMod> autoModList = autoModRepository.findAll();
        assertThat(autoModList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkMaxLinesIsRequired() throws Exception {
        int databaseSizeBeforeTest = autoModRepository.findAll().size();
        // set the field null
        autoMod.setMaxLines(null);

        // Create the AutoMod, which fails.

        restAutoModMockMvc.perform(post("/api/auto-mods")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(autoMod)))
            .andExpect(status().isBadRequest());

        List<AutoMod> autoModList = autoModRepository.findAll();
        assertThat(autoModList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkRaidModeNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = autoModRepository.findAll().size();
        // set the field null
        autoMod.setRaidModeNumber(null);

        // Create the AutoMod, which fails.

        restAutoModMockMvc.perform(post("/api/auto-mods")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(autoMod)))
            .andExpect(status().isBadRequest());

        List<AutoMod> autoModList = autoModRepository.findAll();
        assertThat(autoModList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkRaidModeTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = autoModRepository.findAll().size();
        // set the field null
        autoMod.setRaidModeTime(null);

        // Create the AutoMod, which fails.

        restAutoModMockMvc.perform(post("/api/auto-mods")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(autoMod)))
            .andExpect(status().isBadRequest());

        List<AutoMod> autoModList = autoModRepository.findAll();
        assertThat(autoModList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkInviteStrikesIsRequired() throws Exception {
        int databaseSizeBeforeTest = autoModRepository.findAll().size();
        // set the field null
        autoMod.setInviteStrikes(null);

        // Create the AutoMod, which fails.

        restAutoModMockMvc.perform(post("/api/auto-mods")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(autoMod)))
            .andExpect(status().isBadRequest());

        List<AutoMod> autoModList = autoModRepository.findAll();
        assertThat(autoModList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkRefStrikesIsRequired() throws Exception {
        int databaseSizeBeforeTest = autoModRepository.findAll().size();
        // set the field null
        autoMod.setRefStrikes(null);

        // Create the AutoMod, which fails.

        restAutoModMockMvc.perform(post("/api/auto-mods")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(autoMod)))
            .andExpect(status().isBadRequest());

        List<AutoMod> autoModList = autoModRepository.findAll();
        assertThat(autoModList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCopyPastaStrikesIsRequired() throws Exception {
        int databaseSizeBeforeTest = autoModRepository.findAll().size();
        // set the field null
        autoMod.setCopyPastaStrikes(null);

        // Create the AutoMod, which fails.

        restAutoModMockMvc.perform(post("/api/auto-mods")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(autoMod)))
            .andExpect(status().isBadRequest());

        List<AutoMod> autoModList = autoModRepository.findAll();
        assertThat(autoModList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEveryoneStrikesIsRequired() throws Exception {
        int databaseSizeBeforeTest = autoModRepository.findAll().size();
        // set the field null
        autoMod.setEveryoneStrikes(null);

        // Create the AutoMod, which fails.

        restAutoModMockMvc.perform(post("/api/auto-mods")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(autoMod)))
            .andExpect(status().isBadRequest());

        List<AutoMod> autoModList = autoModRepository.findAll();
        assertThat(autoModList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDupeStrikesIsRequired() throws Exception {
        int databaseSizeBeforeTest = autoModRepository.findAll().size();
        // set the field null
        autoMod.setDupeStrikes(null);

        // Create the AutoMod, which fails.

        restAutoModMockMvc.perform(post("/api/auto-mods")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(autoMod)))
            .andExpect(status().isBadRequest());

        List<AutoMod> autoModList = autoModRepository.findAll();
        assertThat(autoModList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDupeDeleteThreshIsRequired() throws Exception {
        int databaseSizeBeforeTest = autoModRepository.findAll().size();
        // set the field null
        autoMod.setDupeDeleteThresh(null);

        // Create the AutoMod, which fails.

        restAutoModMockMvc.perform(post("/api/auto-mods")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(autoMod)))
            .andExpect(status().isBadRequest());

        List<AutoMod> autoModList = autoModRepository.findAll();
        assertThat(autoModList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDupeStrikesThreshIsRequired() throws Exception {
        int databaseSizeBeforeTest = autoModRepository.findAll().size();
        // set the field null
        autoMod.setDupeStrikesThresh(null);

        // Create the AutoMod, which fails.

        restAutoModMockMvc.perform(post("/api/auto-mods")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(autoMod)))
            .andExpect(status().isBadRequest());

        List<AutoMod> autoModList = autoModRepository.findAll();
        assertThat(autoModList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDehoistCharIsRequired() throws Exception {
        int databaseSizeBeforeTest = autoModRepository.findAll().size();
        // set the field null
        autoMod.setDehoistChar(null);

        // Create the AutoMod, which fails.

        restAutoModMockMvc.perform(post("/api/auto-mods")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(autoMod)))
            .andExpect(status().isBadRequest());

        List<AutoMod> autoModList = autoModRepository.findAll();
        assertThat(autoModList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAutoMods() throws Exception {
        // Initialize the database
        autoModRepository.saveAndFlush(autoMod);

        // Get all the autoModList
        restAutoModMockMvc.perform(get("/api/auto-mods?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(autoMod.getId().intValue())))
            .andExpect(jsonPath("$.[*].guildId").value(hasItem(DEFAULT_GUILD_ID.intValue())))
            .andExpect(jsonPath("$.[*].resolveUrls").value(hasItem(DEFAULT_RESOLVE_URLS.booleanValue())))
            .andExpect(jsonPath("$.[*].maxMentions").value(hasItem(DEFAULT_MAX_MENTIONS)))
            .andExpect(jsonPath("$.[*].maxRoleMentions").value(hasItem(DEFAULT_MAX_ROLE_MENTIONS)))
            .andExpect(jsonPath("$.[*].maxLines").value(hasItem(DEFAULT_MAX_LINES)))
            .andExpect(jsonPath("$.[*].raidModeNumber").value(hasItem(DEFAULT_RAID_MODE_NUMBER)))
            .andExpect(jsonPath("$.[*].raidModeTime").value(hasItem(DEFAULT_RAID_MODE_TIME)))
            .andExpect(jsonPath("$.[*].inviteStrikes").value(hasItem(DEFAULT_INVITE_STRIKES)))
            .andExpect(jsonPath("$.[*].refStrikes").value(hasItem(DEFAULT_REF_STRIKES)))
            .andExpect(jsonPath("$.[*].copyPastaStrikes").value(hasItem(DEFAULT_COPY_PASTA_STRIKES)))
            .andExpect(jsonPath("$.[*].everyoneStrikes").value(hasItem(DEFAULT_EVERYONE_STRIKES)))
            .andExpect(jsonPath("$.[*].dupeStrikes").value(hasItem(DEFAULT_DUPE_STRIKES)))
            .andExpect(jsonPath("$.[*].dupeDeleteThresh").value(hasItem(DEFAULT_DUPE_DELETE_THRESH)))
            .andExpect(jsonPath("$.[*].dupeStrikesThresh").value(hasItem(DEFAULT_DUPE_STRIKES_THRESH)))
            .andExpect(jsonPath("$.[*].dehoistChar").value(hasItem(DEFAULT_DEHOIST_CHAR)));
    }
    
    @Test
    @Transactional
    public void getAutoMod() throws Exception {
        // Initialize the database
        autoModRepository.saveAndFlush(autoMod);

        // Get the autoMod
        restAutoModMockMvc.perform(get("/api/auto-mods/{id}", autoMod.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(autoMod.getId().intValue()))
            .andExpect(jsonPath("$.guildId").value(DEFAULT_GUILD_ID.intValue()))
            .andExpect(jsonPath("$.resolveUrls").value(DEFAULT_RESOLVE_URLS.booleanValue()))
            .andExpect(jsonPath("$.maxMentions").value(DEFAULT_MAX_MENTIONS))
            .andExpect(jsonPath("$.maxRoleMentions").value(DEFAULT_MAX_ROLE_MENTIONS))
            .andExpect(jsonPath("$.maxLines").value(DEFAULT_MAX_LINES))
            .andExpect(jsonPath("$.raidModeNumber").value(DEFAULT_RAID_MODE_NUMBER))
            .andExpect(jsonPath("$.raidModeTime").value(DEFAULT_RAID_MODE_TIME))
            .andExpect(jsonPath("$.inviteStrikes").value(DEFAULT_INVITE_STRIKES))
            .andExpect(jsonPath("$.refStrikes").value(DEFAULT_REF_STRIKES))
            .andExpect(jsonPath("$.copyPastaStrikes").value(DEFAULT_COPY_PASTA_STRIKES))
            .andExpect(jsonPath("$.everyoneStrikes").value(DEFAULT_EVERYONE_STRIKES))
            .andExpect(jsonPath("$.dupeStrikes").value(DEFAULT_DUPE_STRIKES))
            .andExpect(jsonPath("$.dupeDeleteThresh").value(DEFAULT_DUPE_DELETE_THRESH))
            .andExpect(jsonPath("$.dupeStrikesThresh").value(DEFAULT_DUPE_STRIKES_THRESH))
            .andExpect(jsonPath("$.dehoistChar").value(DEFAULT_DEHOIST_CHAR));
    }

    @Test
    @Transactional
    public void getNonExistingAutoMod() throws Exception {
        // Get the autoMod
        restAutoModMockMvc.perform(get("/api/auto-mods/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAutoMod() throws Exception {
        // Initialize the database
        autoModService.save(autoMod);

        int databaseSizeBeforeUpdate = autoModRepository.findAll().size();

        // Update the autoMod
        AutoMod updatedAutoMod = autoModRepository.findById(autoMod.getId()).get();
        // Disconnect from session so that the updates on updatedAutoMod are not directly saved in db
        em.detach(updatedAutoMod);
        updatedAutoMod
            .guildId(UPDATED_GUILD_ID)
            .resolveUrls(UPDATED_RESOLVE_URLS)
            .maxMentions(UPDATED_MAX_MENTIONS)
            .maxRoleMentions(UPDATED_MAX_ROLE_MENTIONS)
            .maxLines(UPDATED_MAX_LINES)
            .raidModeNumber(UPDATED_RAID_MODE_NUMBER)
            .raidModeTime(UPDATED_RAID_MODE_TIME)
            .inviteStrikes(UPDATED_INVITE_STRIKES)
            .refStrikes(UPDATED_REF_STRIKES)
            .copyPastaStrikes(UPDATED_COPY_PASTA_STRIKES)
            .everyoneStrikes(UPDATED_EVERYONE_STRIKES)
            .dupeStrikes(UPDATED_DUPE_STRIKES)
            .dupeDeleteThresh(UPDATED_DUPE_DELETE_THRESH)
            .dupeStrikesThresh(UPDATED_DUPE_STRIKES_THRESH)
            .dehoistChar(UPDATED_DEHOIST_CHAR);

        restAutoModMockMvc.perform(put("/api/auto-mods")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedAutoMod)))
            .andExpect(status().isOk());

        // Validate the AutoMod in the database
        List<AutoMod> autoModList = autoModRepository.findAll();
        assertThat(autoModList).hasSize(databaseSizeBeforeUpdate);
        AutoMod testAutoMod = autoModList.get(autoModList.size() - 1);
        assertThat(testAutoMod.getGuildId()).isEqualTo(UPDATED_GUILD_ID);
        assertThat(testAutoMod.isResolveUrls()).isEqualTo(UPDATED_RESOLVE_URLS);
        assertThat(testAutoMod.getMaxMentions()).isEqualTo(UPDATED_MAX_MENTIONS);
        assertThat(testAutoMod.getMaxRoleMentions()).isEqualTo(UPDATED_MAX_ROLE_MENTIONS);
        assertThat(testAutoMod.getMaxLines()).isEqualTo(UPDATED_MAX_LINES);
        assertThat(testAutoMod.getRaidModeNumber()).isEqualTo(UPDATED_RAID_MODE_NUMBER);
        assertThat(testAutoMod.getRaidModeTime()).isEqualTo(UPDATED_RAID_MODE_TIME);
        assertThat(testAutoMod.getInviteStrikes()).isEqualTo(UPDATED_INVITE_STRIKES);
        assertThat(testAutoMod.getRefStrikes()).isEqualTo(UPDATED_REF_STRIKES);
        assertThat(testAutoMod.getCopyPastaStrikes()).isEqualTo(UPDATED_COPY_PASTA_STRIKES);
        assertThat(testAutoMod.getEveryoneStrikes()).isEqualTo(UPDATED_EVERYONE_STRIKES);
        assertThat(testAutoMod.getDupeStrikes()).isEqualTo(UPDATED_DUPE_STRIKES);
        assertThat(testAutoMod.getDupeDeleteThresh()).isEqualTo(UPDATED_DUPE_DELETE_THRESH);
        assertThat(testAutoMod.getDupeStrikesThresh()).isEqualTo(UPDATED_DUPE_STRIKES_THRESH);
        assertThat(testAutoMod.getDehoistChar()).isEqualTo(UPDATED_DEHOIST_CHAR);
    }

    @Test
    @Transactional
    public void updateNonExistingAutoMod() throws Exception {
        int databaseSizeBeforeUpdate = autoModRepository.findAll().size();

        // Create the AutoMod

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAutoModMockMvc.perform(put("/api/auto-mods")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(autoMod)))
            .andExpect(status().isBadRequest());

        // Validate the AutoMod in the database
        List<AutoMod> autoModList = autoModRepository.findAll();
        assertThat(autoModList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteAutoMod() throws Exception {
        // Initialize the database
        autoModService.save(autoMod);

        int databaseSizeBeforeDelete = autoModRepository.findAll().size();

        // Delete the autoMod
        restAutoModMockMvc.perform(delete("/api/auto-mods/{id}", autoMod.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<AutoMod> autoModList = autoModRepository.findAll();
        assertThat(autoModList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AutoMod.class);
        AutoMod autoMod1 = new AutoMod();
        autoMod1.setId(1L);
        AutoMod autoMod2 = new AutoMod();
        autoMod2.setId(autoMod1.getId());
        assertThat(autoMod1).isEqualTo(autoMod2);
        autoMod2.setId(2L);
        assertThat(autoMod1).isNotEqualTo(autoMod2);
        autoMod1.setId(null);
        assertThat(autoMod1).isNotEqualTo(autoMod2);
    }
}
