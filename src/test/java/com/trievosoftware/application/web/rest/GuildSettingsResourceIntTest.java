//package com.trievosoftware.application.web.rest;
//
//import com.trievosoftware.application.SiaApp;
//
//import com.trievosoftware.application.domain.GuildSettings;
//import com.trievosoftware.application.repository.GuildSettingsRepository;
//import com.trievosoftware.application.service.GuildSettingsService;
//import com.trievosoftware.application.web.rest.errors.ExceptionTranslator;
//
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.MockitoAnnotations;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
//import org.springframework.http.MediaType;
//import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.validation.Validator;
//
//import javax.persistence.EntityManager;
//import java.util.List;
//
//
//import static com.trievosoftware.application.web.rest.TestUtil.createFormattingConversionService;
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.hamcrest.Matchers.hasItem;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
///**
// * Test class for the GuildSettingsResource REST controller.
// *
// * @see GuildSettingsResource
// */
//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = SiaApp.class)
//public class GuildSettingsResourceIntTest {
//
//    private static final Long DEFAULT_GUILD_ID = 1L;
//    private static final Long UPDATED_GUILD_ID = 2L;
//
//    private static final Long DEFAULT_MOD_ROLE_ID = 1L;
//    private static final Long UPDATED_MOD_ROLE_ID = 2L;
//
//    private static final Long DEFAULT_MOD_LOG_ID = 1L;
//    private static final Long UPDATED_MOD_LOG_ID = 2L;
//
//    private static final Long DEFAULT_SERVER_LOG_ID = 1L;
//    private static final Long UPDATED_SERVER_LOG_ID = 2L;
//
//    private static final Long DEFAULT_MESSAGE_LOG_ID = 1L;
//    private static final Long UPDATED_MESSAGE_LOG_ID = 2L;
//
//    private static final Long DEFAULT_VOICE_LOG_ID = 1L;
//    private static final Long UPDATED_VOICE_LOG_ID = 2L;
//
//    private static final Long DEFAULT_AVATAR_LOG_ID = 1L;
//    private static final Long UPDATED_AVATAR_LOG_ID = 2L;
//
//    private static final String DEFAULT_PREFIX = "AAAAAAAAAA";
//    private static final String UPDATED_PREFIX = "BBBBBBBBBB";
//
//    private static final String DEFAULT_TIMEZONE = "America/New_York";
//    private static final String UPDATED_TIMEZONE = "GMT+4";
//
//    private static final Integer DEFAULT_RAID_MODE = 1;
//    private static final Integer UPDATED_RAID_MODE = 2;
//
//    private static final Long DEFAULT_MUTE_ROLE = 1L;
//    private static final Long UPDATED_MUTE_ROLE = 2L;
//
//    @Autowired
//    private GuildSettingsRepository guildSettingsRepository;
//
//    @Autowired
//    private GuildSettingsService guildSettingsService;
//
//    @Autowired
//    private MappingJackson2HttpMessageConverter jacksonMessageConverter;
//
//    @Autowired
//    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;
//
//    @Autowired
//    private ExceptionTranslator exceptionTranslator;
//
//    @Autowired
//    private EntityManager em;
//
//    @Autowired
//    private Validator validator;
//
//    private MockMvc restGuildSettingsMockMvc;
//
//    private GuildSettings guildSettings;
//
//    @Before
//    public void setup() {
//        MockitoAnnotations.initMocks(this);
//        final GuildSettingsResource guildSettingsResource = new GuildSettingsResource(guildSettingsService);
//        this.restGuildSettingsMockMvc = MockMvcBuilders.standaloneSetup(guildSettingsResource)
//            .setCustomArgumentResolvers(pageableArgumentResolver)
//            .setControllerAdvice(exceptionTranslator)
//            .setConversionService(createFormattingConversionService())
//            .setMessageConverters(jacksonMessageConverter)
//            .setValidator(validator).build();
//    }
//
//    /**
//     * Create an entity for this test.
//     *
//     * This is a static method, as tests for other entities might also need it,
//     * if they test an entity which requires the current entity.
//     */
//    public static GuildSettings createEntity(EntityManager em) {
//        GuildSettings guildSettings = new GuildSettings()
//            .guildId(DEFAULT_GUILD_ID)
//            .modRoleId(DEFAULT_MOD_ROLE_ID)
//            .modLogId(DEFAULT_MOD_LOG_ID)
//            .serverLogId(DEFAULT_SERVER_LOG_ID)
//            .messageLogId(DEFAULT_MESSAGE_LOG_ID)
//            .voiceLogId(DEFAULT_VOICE_LOG_ID)
//            .avatarLogId(DEFAULT_AVATAR_LOG_ID)
//            .prefix(DEFAULT_PREFIX)
//            .timezone(DEFAULT_TIMEZONE)
//            .raidMode(DEFAULT_RAID_MODE)
//            .muteRole(DEFAULT_MUTE_ROLE);
//        return guildSettings;
//    }
//
//    @Before
//    public void initTest() {
//        guildSettings = createEntity(em);
//    }
//
//    @Test
//    @Transactional
//    public void createGuildSettings() throws Exception {
//        int databaseSizeBeforeCreate = guildSettingsRepository.findAll().size();
//
//        // Create the GuildSettings
//        restGuildSettingsMockMvc.perform(post("/api/guild-settings")
//            .contentType(TestUtil.APPLICATION_JSON_UTF8)
//            .content(TestUtil.convertObjectToJsonBytes(guildSettings)))
//            .andExpect(status().isBadRequest());
//
//        // Validate the GuildSettings in the database
//        List<GuildSettings> guildSettingsList = guildSettingsRepository.findAll();
//        assertThat(guildSettingsList).hasSize(databaseSizeBeforeCreate + 1);
//        GuildSettings testGuildSettings = guildSettingsList.get(guildSettingsList.size() - 1);
//        assertThat(testGuildSettings.getGuildId()).isEqualTo(DEFAULT_GUILD_ID);
//        assertThat(testGuildSettings.getModRoleId()).isEqualTo(DEFAULT_MOD_ROLE_ID);
//        assertThat(testGuildSettings.getModLogId()).isEqualTo(DEFAULT_MOD_LOG_ID);
//        assertThat(testGuildSettings.getServerLogId()).isEqualTo(DEFAULT_SERVER_LOG_ID);
//        assertThat(testGuildSettings.getMessageLogId()).isEqualTo(DEFAULT_MESSAGE_LOG_ID);
//        assertThat(testGuildSettings.getVoiceLogId()).isEqualTo(DEFAULT_VOICE_LOG_ID);
//        assertThat(testGuildSettings.getAvatarLogId()).isEqualTo(DEFAULT_AVATAR_LOG_ID);
//        assertThat(testGuildSettings.getPrefix()).isEqualTo(DEFAULT_PREFIX);
//        assertThat(testGuildSettings.getTimezone()).isEqualTo(DEFAULT_TIMEZONE);
//        assertThat(testGuildSettings.getRaidMode()).isEqualTo(DEFAULT_RAID_MODE);
//        assertThat(testGuildSettings.getMuteRole()).isEqualTo(DEFAULT_MUTE_ROLE);
//    }
//
//    @Test
//    @Transactional
//    public void createGuildSettingsWithExistingId() throws Exception {
//        int databaseSizeBeforeCreate = guildSettingsRepository.findAll().size();
//
//        // Create the GuildSettings with an existing ID
//        guildSettings.setId(1L);
//
//        // An entity with an existing ID cannot be created, so this API call must fail
//        restGuildSettingsMockMvc.perform(post("/api/guild-settings")
//            .contentType(TestUtil.APPLICATION_JSON_UTF8)
//            .content(TestUtil.convertObjectToJsonBytes(guildSettings)))
//            .andExpect(status().isBadRequest());
//
//        // Validate the GuildSettings in the database
//        List<GuildSettings> guildSettingsList = guildSettingsRepository.findAll();
//        assertThat(guildSettingsList).hasSize(databaseSizeBeforeCreate);
//    }
//
//    @Test
//    @Transactional
//    public void checkGuildIdIsRequired() throws Exception {
//        int databaseSizeBeforeTest = guildSettingsRepository.findAll().size();
//        // set the field null
//        guildSettings.setGuildId(null);
//
//        // Create the GuildSettings, which fails.
//
//        restGuildSettingsMockMvc.perform(post("/api/guild-settings")
//            .contentType(TestUtil.APPLICATION_JSON_UTF8)
//            .content(TestUtil.convertObjectToJsonBytes(guildSettings)))
//            .andExpect(status().isBadRequest());
//
//        List<GuildSettings> guildSettingsList = guildSettingsRepository.findAll();
//        assertThat(guildSettingsList).hasSize(databaseSizeBeforeTest);
//    }
//
//    @Test
//    @Transactional
//    public void checkModRoleIdIsRequired() throws Exception {
//        int databaseSizeBeforeTest = guildSettingsRepository.findAll().size();
//        // set the field null
//        guildSettings.setModRoleId(null);
//
//        // Create the GuildSettings, which fails.
//
//        restGuildSettingsMockMvc.perform(post("/api/guild-settings")
//            .contentType(TestUtil.APPLICATION_JSON_UTF8)
//            .content(TestUtil.convertObjectToJsonBytes(guildSettings)))
//            .andExpect(status().isBadRequest());
//
//        List<GuildSettings> guildSettingsList = guildSettingsRepository.findAll();
//        assertThat(guildSettingsList).hasSize(databaseSizeBeforeTest);
//    }
//
//    @Test
//    @Transactional
//    public void checkModLogIdIsRequired() throws Exception {
//        int databaseSizeBeforeTest = guildSettingsRepository.findAll().size();
//        // set the field null
//        guildSettings.setModLogId(null);
//
//        // Create the GuildSettings, which fails.
//
//        restGuildSettingsMockMvc.perform(post("/api/guild-settings")
//            .contentType(TestUtil.APPLICATION_JSON_UTF8)
//            .content(TestUtil.convertObjectToJsonBytes(guildSettings)))
//            .andExpect(status().isBadRequest());
//
//        List<GuildSettings> guildSettingsList = guildSettingsRepository.findAll();
//        assertThat(guildSettingsList).hasSize(databaseSizeBeforeTest);
//    }
//
//    @Test
//    @Transactional
//    public void checkServerLogIdIsRequired() throws Exception {
//        int databaseSizeBeforeTest = guildSettingsRepository.findAll().size();
//        // set the field null
//        guildSettings.setServerLogId(null);
//
//        // Create the GuildSettings, which fails.
//
//        restGuildSettingsMockMvc.perform(post("/api/guild-settings")
//            .contentType(TestUtil.APPLICATION_JSON_UTF8)
//            .content(TestUtil.convertObjectToJsonBytes(guildSettings)))
//            .andExpect(status().isBadRequest());
//
//        List<GuildSettings> guildSettingsList = guildSettingsRepository.findAll();
//        assertThat(guildSettingsList).hasSize(databaseSizeBeforeTest);
//    }
//
//    @Test
//    @Transactional
//    public void checkMessageLogIdIsRequired() throws Exception {
//        int databaseSizeBeforeTest = guildSettingsRepository.findAll().size();
//        // set the field null
//        guildSettings.setMessageLogId(null);
//
//        // Create the GuildSettings, which fails.
//
//        restGuildSettingsMockMvc.perform(post("/api/guild-settings")
//            .contentType(TestUtil.APPLICATION_JSON_UTF8)
//            .content(TestUtil.convertObjectToJsonBytes(guildSettings)))
//            .andExpect(status().isBadRequest());
//
//        List<GuildSettings> guildSettingsList = guildSettingsRepository.findAll();
//        assertThat(guildSettingsList).hasSize(databaseSizeBeforeTest);
//    }
//
//    @Test
//    @Transactional
//    public void checkVoiceLogIdIsRequired() throws Exception {
//        int databaseSizeBeforeTest = guildSettingsRepository.findAll().size();
//        // set the field null
//        guildSettings.setVoiceLogId(null);
//
//        // Create the GuildSettings, which fails.
//
//        restGuildSettingsMockMvc.perform(post("/api/guild-settings")
//            .contentType(TestUtil.APPLICATION_JSON_UTF8)
//            .content(TestUtil.convertObjectToJsonBytes(guildSettings)))
//            .andExpect(status().isBadRequest());
//
//        List<GuildSettings> guildSettingsList = guildSettingsRepository.findAll();
//        assertThat(guildSettingsList).hasSize(databaseSizeBeforeTest);
//    }
//
//    @Test
//    @Transactional
//    public void checkAvatarLogIdIsRequired() throws Exception {
//        int databaseSizeBeforeTest = guildSettingsRepository.findAll().size();
//        // set the field null
//        guildSettings.setAvatarLogId(null);
//
//        // Create the GuildSettings, which fails.
//
//        restGuildSettingsMockMvc.perform(post("/api/guild-settings")
//            .contentType(TestUtil.APPLICATION_JSON_UTF8)
//            .content(TestUtil.convertObjectToJsonBytes(guildSettings)))
//            .andExpect(status().isBadRequest());
//
//        List<GuildSettings> guildSettingsList = guildSettingsRepository.findAll();
//        assertThat(guildSettingsList).hasSize(databaseSizeBeforeTest);
//    }
//
//    @Test
//    @Transactional
//    public void checkRaidModeIsRequired() throws Exception {
//        int databaseSizeBeforeTest = guildSettingsRepository.findAll().size();
//        // set the field null
//        guildSettings.setRaidMode(null);
//
//        // Create the GuildSettings, which fails.
//
//        restGuildSettingsMockMvc.perform(post("/api/guild-settings")
//            .contentType(TestUtil.APPLICATION_JSON_UTF8)
//            .content(TestUtil.convertObjectToJsonBytes(guildSettings)))
//            .andExpect(status().isBadRequest());
//
//        List<GuildSettings> guildSettingsList = guildSettingsRepository.findAll();
//        assertThat(guildSettingsList).hasSize(databaseSizeBeforeTest);
//    }
//
//    @Test
//    @Transactional
//    public void checkMuteRoleIsRequired() throws Exception {
//        int databaseSizeBeforeTest = guildSettingsRepository.findAll().size();
//        // set the field null
//        guildSettings.setMuteRole(null);
//
//        // Create the GuildSettings, which fails.
//
//        restGuildSettingsMockMvc.perform(post("/api/guild-settings")
//            .contentType(TestUtil.APPLICATION_JSON_UTF8)
//            .content(TestUtil.convertObjectToJsonBytes(guildSettings)))
//            .andExpect(status().isBadRequest());
//
//        List<GuildSettings> guildSettingsList = guildSettingsRepository.findAll();
//        assertThat(guildSettingsList).hasSize(databaseSizeBeforeTest);
//    }
//
//    @Test
//    @Transactional
//    public void getAllGuildSettings() throws Exception {
//        // Initialize the database
//        guildSettingsRepository.saveAndFlush(guildSettings);
//
//        // Get all the guildSettingsList
//        restGuildSettingsMockMvc.perform(get("/api/guild-settings?sort=id,desc"))
//            .andExpect(status().isOk())
//            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
//            .andExpect(jsonPath("$.[*].id").value(hasItem(guildSettings.getId().intValue())))
//            .andExpect(jsonPath("$.[*].guildId").value(hasItem(DEFAULT_GUILD_ID.intValue())))
//            .andExpect(jsonPath("$.[*].modRoleId").value(hasItem(DEFAULT_MOD_ROLE_ID.intValue())))
//            .andExpect(jsonPath("$.[*].modLogId").value(hasItem(DEFAULT_MOD_LOG_ID.intValue())))
//            .andExpect(jsonPath("$.[*].serverLogId").value(hasItem(DEFAULT_SERVER_LOG_ID.intValue())))
//            .andExpect(jsonPath("$.[*].messageLogId").value(hasItem(DEFAULT_MESSAGE_LOG_ID.intValue())))
//            .andExpect(jsonPath("$.[*].voiceLogId").value(hasItem(DEFAULT_VOICE_LOG_ID.intValue())))
//            .andExpect(jsonPath("$.[*].avatarLogId").value(hasItem(DEFAULT_AVATAR_LOG_ID.intValue())))
//            .andExpect(jsonPath("$.[*].prefix").value(hasItem(DEFAULT_PREFIX.toString())))
//            .andExpect(jsonPath("$.[*].timezone").value(hasItem(DEFAULT_TIMEZONE.toString())))
//            .andExpect(jsonPath("$.[*].raidMode").value(hasItem(DEFAULT_RAID_MODE)))
//            .andExpect(jsonPath("$.[*].muteRole").value(hasItem(DEFAULT_MUTE_ROLE.intValue())));
//    }
//
//    @Test
//    @Transactional
//    public void getGuildSettings() throws Exception {
//        // Initialize the database
//        guildSettingsRepository.saveAndFlush(guildSettings);
//
//        // Get the guildSettings
//        restGuildSettingsMockMvc.perform(get("/api/guild-settings/{id}", guildSettings.getId()))
//            .andExpect(status().isOk())
//            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
//            .andExpect(jsonPath("$.id").value(guildSettings.getId().intValue()))
//            .andExpect(jsonPath("$.guildId").value(DEFAULT_GUILD_ID.intValue()))
//            .andExpect(jsonPath("$.modRoleId").value(DEFAULT_MOD_ROLE_ID.intValue()))
//            .andExpect(jsonPath("$.modLogId").value(DEFAULT_MOD_LOG_ID.intValue()))
//            .andExpect(jsonPath("$.serverLogId").value(DEFAULT_SERVER_LOG_ID.intValue()))
//            .andExpect(jsonPath("$.messageLogId").value(DEFAULT_MESSAGE_LOG_ID.intValue()))
//            .andExpect(jsonPath("$.voiceLogId").value(DEFAULT_VOICE_LOG_ID.intValue()))
//            .andExpect(jsonPath("$.avatarLogId").value(DEFAULT_AVATAR_LOG_ID.intValue()))
//            .andExpect(jsonPath("$.prefix").value(DEFAULT_PREFIX.toString()))
//            .andExpect(jsonPath("$.timezone").value(DEFAULT_TIMEZONE.toString()))
//            .andExpect(jsonPath("$.raidMode").value(DEFAULT_RAID_MODE))
//            .andExpect(jsonPath("$.muteRole").value(DEFAULT_MUTE_ROLE.intValue()));
//    }
//
//    @Test
//    @Transactional
//    public void getNonExistingGuildSettings() throws Exception {
//        // Get the guildSettings
//        restGuildSettingsMockMvc.perform(get("/api/guild-settings/{id}", Long.MAX_VALUE))
//            .andExpect(status().isNotFound());
//    }
//
//    @Test
//    @Transactional
//    public void updateGuildSettings() throws Exception {
//        // Initialize the database
//        guildSettingsService.save(guildSettings);
//
//        int databaseSizeBeforeUpdate = guildSettingsRepository.findAll().size();
//
//        // Update the guildSettings
//        GuildSettings updatedGuildSettings = guildSettingsRepository.findById(guildSettings.getId()).get();
//        // Disconnect from session so that the updates on updatedGuildSettings are not directly saved in db
//        em.detach(updatedGuildSettings);
//        updatedGuildSettings
//            .guildId(UPDATED_GUILD_ID)
//            .modRoleId(UPDATED_MOD_ROLE_ID)
//            .modLogId(UPDATED_MOD_LOG_ID)
//            .serverLogId(UPDATED_SERVER_LOG_ID)
//            .messageLogId(UPDATED_MESSAGE_LOG_ID)
//            .voiceLogId(UPDATED_VOICE_LOG_ID)
//            .avatarLogId(UPDATED_AVATAR_LOG_ID)
//            .prefix(UPDATED_PREFIX)
//            .timezone(UPDATED_TIMEZONE)
//            .raidMode(UPDATED_RAID_MODE)
//            .muteRole(UPDATED_MUTE_ROLE);
//
//        restGuildSettingsMockMvc.perform(put("/api/guild-settings")
//            .contentType(TestUtil.APPLICATION_JSON_UTF8)
//            .content(TestUtil.convertObjectToJsonBytes(updatedGuildSettings)))
//            .andExpect(status().isBadRequest());
//
//        // Validate the GuildSettings in the database
//        List<GuildSettings> guildSettingsList = guildSettingsRepository.findAll();
//        assertThat(guildSettingsList).hasSize(databaseSizeBeforeUpdate);
//        GuildSettings testGuildSettings = guildSettingsList.get(guildSettingsList.size() - 1);
//        assertThat(testGuildSettings.getGuildId()).isEqualTo(UPDATED_GUILD_ID);
//        assertThat(testGuildSettings.getModRoleId()).isEqualTo(UPDATED_MOD_ROLE_ID);
//        assertThat(testGuildSettings.getModLogId()).isEqualTo(UPDATED_MOD_LOG_ID);
//        assertThat(testGuildSettings.getServerLogId()).isEqualTo(UPDATED_SERVER_LOG_ID);
//        assertThat(testGuildSettings.getMessageLogId()).isEqualTo(UPDATED_MESSAGE_LOG_ID);
//        assertThat(testGuildSettings.getVoiceLogId()).isEqualTo(UPDATED_VOICE_LOG_ID);
//        assertThat(testGuildSettings.getAvatarLogId()).isEqualTo(UPDATED_AVATAR_LOG_ID);
//        assertThat(testGuildSettings.getPrefix()).isEqualTo(UPDATED_PREFIX);
//        assertThat(testGuildSettings.getTimezone()).isEqualTo(UPDATED_TIMEZONE);
//        assertThat(testGuildSettings.getRaidMode()).isEqualTo(UPDATED_RAID_MODE);
//        assertThat(testGuildSettings.getMuteRole()).isEqualTo(UPDATED_MUTE_ROLE);
//    }
//
//    @Test
//    @Transactional
//    public void updateNonExistingGuildSettings() throws Exception {
//        int databaseSizeBeforeUpdate = guildSettingsRepository.findAll().size();
//
//        // Create the GuildSettings
//
//        // If the entity doesn't have an ID, it will throw BadRequestAlertException
//        restGuildSettingsMockMvc.perform(put("/api/guild-settings")
//            .contentType(TestUtil.APPLICATION_JSON_UTF8)
//            .content(TestUtil.convertObjectToJsonBytes(guildSettings)))
//            .andExpect(status().isBadRequest());
//
//        // Validate the GuildSettings in the database
//        List<GuildSettings> guildSettingsList = guildSettingsRepository.findAll();
//        assertThat(guildSettingsList).hasSize(databaseSizeBeforeUpdate);
//    }
//
//    @Test
//    @Transactional
//    public void deleteGuildSettings() throws Exception {
//        // Initialize the database
//        guildSettingsService.save(guildSettings);
//
//        int databaseSizeBeforeDelete = guildSettingsRepository.findAll().size();
//
//        // Delete the guildSettings
//        restGuildSettingsMockMvc.perform(delete("/api/guild-settings/{id}", guildSettings.getId())
//            .accept(TestUtil.APPLICATION_JSON_UTF8))
//            .andExpect(status().isOk());
//
//        // Validate the database is empty
//        List<GuildSettings> guildSettingsList = guildSettingsRepository.findAll();
//        assertThat(guildSettingsList).hasSize(databaseSizeBeforeDelete - 1);
//    }
//
//    @Test
//    @Transactional
//    public void equalsVerifier() throws Exception {
//        TestUtil.equalsVerifier(GuildSettings.class);
//        GuildSettings guildSettings1 = new GuildSettings();
//        guildSettings1.setId(1L);
//        GuildSettings guildSettings2 = new GuildSettings();
//        guildSettings2.setId(guildSettings1.getId());
//        assertThat(guildSettings1).isEqualTo(guildSettings2);
//        guildSettings2.setId(2L);
//        assertThat(guildSettings1).isNotEqualTo(guildSettings2);
//        guildSettings1.setId(null);
//        assertThat(guildSettings1).isNotEqualTo(guildSettings2);
//    }
//}
