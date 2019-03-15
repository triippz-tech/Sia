//package com.trievosoftware.application.web.rest;
//
//import com.trievosoftware.application.SiaApp;
//
//import com.trievosoftware.application.domain.GuildMusicSettings;
//import com.trievosoftware.application.repository.GuildMusicSettingsRepository;
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
// * Test class for the GuildMusicSettingsResource REST controller.
// *
// * @see GuildMusicSettingsResource
// */
//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = SiaApp.class)
//public class GuildMusicSettingsResourceIntTest {
//
//    private static final Long DEFAULT_GUILD_ID = 1L;
//    private static final Long UPDATED_GUILD_ID = 2L;
//
//    private static final Long DEFAULT_TEXT_CHANNEL_ID = 1L;
//    private static final Long UPDATED_TEXT_CHANNEL_ID = 2L;
//
//    private static final Long DEFAULT_VOICE_CHANNEL_ID = 1L;
//    private static final Long UPDATED_VOICE_CHANNEL_ID = 2L;
//
//    private static final Long DEFAULT_DJ_ROLE_ID = 1L;
//    private static final Long UPDATED_DJ_ROLE_ID = 2L;
//
//    private static final Integer DEFAULT_VOLUME = 1;
//    private static final Integer UPDATED_VOLUME = 2;
//
//    private static final Boolean DEFAULT_REPEAT = false;
//    private static final Boolean UPDATED_REPEAT = true;
//
//    @Autowired
//    private GuildMusicSettingsRepository guildMusicSettingsRepository;
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
//    private MockMvc restGuildMusicSettingsMockMvc;
//
//    private GuildMusicSettings guildMusicSettings;
//
//    @Before
//    public void setup() {
//        MockitoAnnotations.initMocks(this);
//        final GuildMusicSettingsResource guildMusicSettingsResource = new GuildMusicSettingsResource(guildMusicSettingsRepository);
//        this.restGuildMusicSettingsMockMvc = MockMvcBuilders.standaloneSetup(guildMusicSettingsResource)
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
//    public static GuildMusicSettings createEntity(EntityManager em) {
//        GuildMusicSettings guildMusicSettings = new GuildMusicSettings()
//            .guildId(DEFAULT_GUILD_ID)
//            .textChannelId(DEFAULT_TEXT_CHANNEL_ID)
//            .voiceChannelId(DEFAULT_VOICE_CHANNEL_ID)
//            .djRoleId(DEFAULT_DJ_ROLE_ID)
//            .volume(DEFAULT_VOLUME)
//            .repeat(DEFAULT_REPEAT);
//        return guildMusicSettings;
//    }
//
//    @Before
//    public void initTest() {
//        guildMusicSettings = createEntity(em);
//    }
//
//    @Test
//    @Transactional
//    public void createGuildMusicSettings() throws Exception {
//        int databaseSizeBeforeCreate = guildMusicSettingsRepository.findAll().size();
//
//        // Create the GuildMusicSettings
//        restGuildMusicSettingsMockMvc.perform(post("/api/guild-music-settings")
//            .contentType(TestUtil.APPLICATION_JSON_UTF8)
//            .content(TestUtil.convertObjectToJsonBytes(guildMusicSettings)))
//            .andExpect(status().isCreated());
//
//        // Validate the GuildMusicSettings in the database
//        List<GuildMusicSettings> guildMusicSettingsList = guildMusicSettingsRepository.findAll();
//        assertThat(guildMusicSettingsList).hasSize(databaseSizeBeforeCreate + 1);
//        GuildMusicSettings testGuildMusicSettings = guildMusicSettingsList.get(guildMusicSettingsList.size() - 1);
//        assertThat(testGuildMusicSettings.getGuildId()).isEqualTo(DEFAULT_GUILD_ID);
//        assertThat(testGuildMusicSettings.getTextChannelId()).isEqualTo(DEFAULT_TEXT_CHANNEL_ID);
//        assertThat(testGuildMusicSettings.getVoiceChannelId()).isEqualTo(DEFAULT_VOICE_CHANNEL_ID);
//        assertThat(testGuildMusicSettings.getDjRoleId()).isEqualTo(DEFAULT_DJ_ROLE_ID);
//        assertThat(testGuildMusicSettings.getVolume()).isEqualTo(DEFAULT_VOLUME);
//        assertThat(testGuildMusicSettings.isRepeat()).isEqualTo(DEFAULT_REPEAT);
//    }
//
//    @Test
//    @Transactional
//    public void createGuildMusicSettingsWithExistingId() throws Exception {
//        int databaseSizeBeforeCreate = guildMusicSettingsRepository.findAll().size();
//
//        // Create the GuildMusicSettings with an existing ID
//        guildMusicSettings.setId(1L);
//
//        // An entity with an existing ID cannot be created, so this API call must fail
//        restGuildMusicSettingsMockMvc.perform(post("/api/guild-music-settings")
//            .contentType(TestUtil.APPLICATION_JSON_UTF8)
//            .content(TestUtil.convertObjectToJsonBytes(guildMusicSettings)))
//            .andExpect(status().isBadRequest());
//
//        // Validate the GuildMusicSettings in the database
//        List<GuildMusicSettings> guildMusicSettingsList = guildMusicSettingsRepository.findAll();
//        assertThat(guildMusicSettingsList).hasSize(databaseSizeBeforeCreate);
//    }
//
//    @Test
//    @Transactional
//    public void checkGuildIdIsRequired() throws Exception {
//        int databaseSizeBeforeTest = guildMusicSettingsRepository.findAll().size();
//        // set the field null
//        guildMusicSettings.setGuildId(DEFAULT_GUILD_ID);
//
//        // Create the GuildMusicSettings, which fails.
//
//        restGuildMusicSettingsMockMvc.perform(post("/api/guild-music-settings")
//            .contentType(TestUtil.APPLICATION_JSON_UTF8)
//            .content(TestUtil.convertObjectToJsonBytes(guildMusicSettings)))
//            .andExpect(status().isBadRequest());
//
//        List<GuildMusicSettings> guildMusicSettingsList = guildMusicSettingsRepository.findAll();
//        assertThat(guildMusicSettingsList).hasSize(databaseSizeBeforeTest);
//    }
//
//    @Test
//    @Transactional
//    public void checkTextChannelIdIsRequired() throws Exception {
//        int databaseSizeBeforeTest = guildMusicSettingsRepository.findAll().size();
//        // set the field null
//        guildMusicSettings.setTextChannelId(DEFAULT_TEXT_CHANNEL_ID);
//
//        // Create the GuildMusicSettings, which fails.
//
//        restGuildMusicSettingsMockMvc.perform(post("/api/guild-music-settings")
//            .contentType(TestUtil.APPLICATION_JSON_UTF8)
//            .content(TestUtil.convertObjectToJsonBytes(guildMusicSettings)))
//            .andExpect(status().isBadRequest());
//
//        List<GuildMusicSettings> guildMusicSettingsList = guildMusicSettingsRepository.findAll();
//        assertThat(guildMusicSettingsList).hasSize(databaseSizeBeforeTest);
//    }
//
//    @Test
//    @Transactional
//    public void checkVoiceChannelIdIsRequired() throws Exception {
//        int databaseSizeBeforeTest = guildMusicSettingsRepository.findAll().size();
//        // set the field null
//        guildMusicSettings.setVoiceChannelId(DEFAULT_VOICE_CHANNEL_ID);
//
//        // Create the GuildMusicSettings, which fails.
//
//        restGuildMusicSettingsMockMvc.perform(post("/api/guild-music-settings")
//            .contentType(TestUtil.APPLICATION_JSON_UTF8)
//            .content(TestUtil.convertObjectToJsonBytes(guildMusicSettings)))
//            .andExpect(status().isBadRequest());
//
//        List<GuildMusicSettings> guildMusicSettingsList = guildMusicSettingsRepository.findAll();
//        assertThat(guildMusicSettingsList).hasSize(databaseSizeBeforeTest);
//    }
//
//    @Test
//    @Transactional
//    public void checkDjRoleIdIsRequired() throws Exception {
//        int databaseSizeBeforeTest = guildMusicSettingsRepository.findAll().size();
//        // set the field null
//        guildMusicSettings.setDjRoleId(DEFAULT_DJ_ROLE_ID);
//
//        // Create the GuildMusicSettings, which fails.
//
//        restGuildMusicSettingsMockMvc.perform(post("/api/guild-music-settings")
//            .contentType(TestUtil.APPLICATION_JSON_UTF8)
//            .content(TestUtil.convertObjectToJsonBytes(guildMusicSettings)))
//            .andExpect(status().isBadRequest());
//
//        List<GuildMusicSettings> guildMusicSettingsList = guildMusicSettingsRepository.findAll();
//        assertThat(guildMusicSettingsList).hasSize(databaseSizeBeforeTest);
//    }
//
//    @Test
//    @Transactional
//    public void checkVolumeIsRequired() throws Exception {
//        int databaseSizeBeforeTest = guildMusicSettingsRepository.findAll().size();
//        // set the field null
//        guildMusicSettings.setVolume(100);
//
//        // Create the GuildMusicSettings, which fails.
//
//        restGuildMusicSettingsMockMvc.perform(post("/api/guild-music-settings")
//            .contentType(TestUtil.APPLICATION_JSON_UTF8)
//            .content(TestUtil.convertObjectToJsonBytes(guildMusicSettings)))
//            .andExpect(status().isBadRequest());
//
//        List<GuildMusicSettings> guildMusicSettingsList = guildMusicSettingsRepository.findAll();
//        assertThat(guildMusicSettingsList).hasSize(databaseSizeBeforeTest);
//    }
//
//    @Test
//    @Transactional
//    public void checkRepeatIsRequired() throws Exception {
//        int databaseSizeBeforeTest = guildMusicSettingsRepository.findAll().size();
//        // set the field null
//        guildMusicSettings.setRepeat(false);
//
//        // Create the GuildMusicSettings, which fails.
//
//        restGuildMusicSettingsMockMvc.perform(post("/api/guild-music-settings")
//            .contentType(TestUtil.APPLICATION_JSON_UTF8)
//            .content(TestUtil.convertObjectToJsonBytes(guildMusicSettings)))
//            .andExpect(status().isBadRequest());
//
//        List<GuildMusicSettings> guildMusicSettingsList = guildMusicSettingsRepository.findAll();
//        assertThat(guildMusicSettingsList).hasSize(databaseSizeBeforeTest);
//    }
//
//    @Test
//    @Transactional
//    public void getAllGuildMusicSettings() throws Exception {
//        // Initialize the database
//        guildMusicSettingsRepository.saveAndFlush(guildMusicSettings);
//
//        // Get all the guildMusicSettingsList
//        restGuildMusicSettingsMockMvc.perform(get("/api/guild-music-settings?sort=id,desc"))
//            .andExpect(status().isOk())
//            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
//            .andExpect(jsonPath("$.[*].id").value(hasItem(guildMusicSettings.getId().intValue())))
//            .andExpect(jsonPath("$.[*].guildId").value(hasItem(DEFAULT_GUILD_ID.intValue())))
//            .andExpect(jsonPath("$.[*].textChannelId").value(hasItem(DEFAULT_TEXT_CHANNEL_ID.intValue())))
//            .andExpect(jsonPath("$.[*].voiceChannelId").value(hasItem(DEFAULT_VOICE_CHANNEL_ID.intValue())))
//            .andExpect(jsonPath("$.[*].djRoleId").value(hasItem(DEFAULT_DJ_ROLE_ID.intValue())))
//            .andExpect(jsonPath("$.[*].volume").value(hasItem(DEFAULT_VOLUME)))
//            .andExpect(jsonPath("$.[*].repeat").value(hasItem(DEFAULT_REPEAT.booleanValue())));
//    }
//
//    @Test
//    @Transactional
//    public void getGuildMusicSettings() throws Exception {
//        // Initialize the database
//        guildMusicSettingsRepository.saveAndFlush(guildMusicSettings);
//
//        // Get the guildMusicSettings
//        restGuildMusicSettingsMockMvc.perform(get("/api/guild-music-settings/{id}", guildMusicSettings.getId()))
//            .andExpect(status().isOk())
//            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
//            .andExpect(jsonPath("$.id").value(guildMusicSettings.getId().intValue()))
//            .andExpect(jsonPath("$.guildId").value(DEFAULT_GUILD_ID.intValue()))
//            .andExpect(jsonPath("$.textChannelId").value(DEFAULT_TEXT_CHANNEL_ID.intValue()))
//            .andExpect(jsonPath("$.voiceChannelId").value(DEFAULT_VOICE_CHANNEL_ID.intValue()))
//            .andExpect(jsonPath("$.djRoleId").value(DEFAULT_DJ_ROLE_ID.intValue()))
//            .andExpect(jsonPath("$.volume").value(DEFAULT_VOLUME))
//            .andExpect(jsonPath("$.repeat").value(DEFAULT_REPEAT.booleanValue()));
//    }
//
//    @Test
//    @Transactional
//    public void getNonExistingGuildMusicSettings() throws Exception {
//        // Get the guildMusicSettings
//        restGuildMusicSettingsMockMvc.perform(get("/api/guild-music-settings/{id}", Long.MAX_VALUE))
//            .andExpect(status().isNotFound());
//    }
//
//    @Test
//    @Transactional
//    public void updateGuildMusicSettings() throws Exception {
//        // Initialize the database
//        guildMusicSettingsRepository.saveAndFlush(guildMusicSettings);
//
//        int databaseSizeBeforeUpdate = guildMusicSettingsRepository.findAll().size();
//
//        // Update the guildMusicSettings
//        GuildMusicSettings updatedGuildMusicSettings = guildMusicSettingsRepository.findById(guildMusicSettings.getId()).get();
//        // Disconnect from session so that the updates on updatedGuildMusicSettings are not directly saved in db
//        em.detach(updatedGuildMusicSettings);
//        updatedGuildMusicSettings
//            .guildId(UPDATED_GUILD_ID)
//            .textChannelId(UPDATED_TEXT_CHANNEL_ID)
//            .voiceChannelId(UPDATED_VOICE_CHANNEL_ID)
//            .djRoleId(UPDATED_DJ_ROLE_ID)
//            .volume(UPDATED_VOLUME)
//            .repeat(UPDATED_REPEAT);
//
//        restGuildMusicSettingsMockMvc.perform(put("/api/guild-music-settings")
//            .contentType(TestUtil.APPLICATION_JSON_UTF8)
//            .content(TestUtil.convertObjectToJsonBytes(updatedGuildMusicSettings)))
//            .andExpect(status().isOk());
//
//        // Validate the GuildMusicSettings in the database
//        List<GuildMusicSettings> guildMusicSettingsList = guildMusicSettingsRepository.findAll();
//        assertThat(guildMusicSettingsList).hasSize(databaseSizeBeforeUpdate);
//        GuildMusicSettings testGuildMusicSettings = guildMusicSettingsList.get(guildMusicSettingsList.size() - 1);
//        assertThat(testGuildMusicSettings.getGuildId()).isEqualTo(UPDATED_GUILD_ID);
//        assertThat(testGuildMusicSettings.getTextChannelId()).isEqualTo(UPDATED_TEXT_CHANNEL_ID);
//        assertThat(testGuildMusicSettings.getVoiceChannelId()).isEqualTo(UPDATED_VOICE_CHANNEL_ID);
//        assertThat(testGuildMusicSettings.getDjRoleId()).isEqualTo(UPDATED_DJ_ROLE_ID);
//        assertThat(testGuildMusicSettings.getVolume()).isEqualTo(UPDATED_VOLUME);
//        assertThat(testGuildMusicSettings.isRepeat()).isEqualTo(UPDATED_REPEAT);
//    }
//
//    @Test
//    @Transactional
//    public void updateNonExistingGuildMusicSettings() throws Exception {
//        int databaseSizeBeforeUpdate = guildMusicSettingsRepository.findAll().size();
//
//        // Create the GuildMusicSettings
//
//        // If the entity doesn't have an ID, it will throw BadRequestAlertException
//        restGuildMusicSettingsMockMvc.perform(put("/api/guild-music-settings")
//            .contentType(TestUtil.APPLICATION_JSON_UTF8)
//            .content(TestUtil.convertObjectToJsonBytes(guildMusicSettings)))
//            .andExpect(status().isBadRequest());
//
//        // Validate the GuildMusicSettings in the database
//        List<GuildMusicSettings> guildMusicSettingsList = guildMusicSettingsRepository.findAll();
//        assertThat(guildMusicSettingsList).hasSize(databaseSizeBeforeUpdate);
//    }
//
//    @Test
//    @Transactional
//    public void deleteGuildMusicSettings() throws Exception {
//        // Initialize the database
//        guildMusicSettingsRepository.saveAndFlush(guildMusicSettings);
//
//        int databaseSizeBeforeDelete = guildMusicSettingsRepository.findAll().size();
//
//        // Delete the guildMusicSettings
//        restGuildMusicSettingsMockMvc.perform(delete("/api/guild-music-settings/{id}", guildMusicSettings.getId())
//            .accept(TestUtil.APPLICATION_JSON_UTF8))
//            .andExpect(status().isOk());
//
//        // Validate the database is empty
//        List<GuildMusicSettings> guildMusicSettingsList = guildMusicSettingsRepository.findAll();
//        assertThat(guildMusicSettingsList).hasSize(databaseSizeBeforeDelete - 1);
//    }
//
//    @Test
//    @Transactional
//    public void equalsVerifier() throws Exception {
//        TestUtil.equalsVerifier(GuildMusicSettings.class);
//        GuildMusicSettings guildMusicSettings1 = new GuildMusicSettings();
//        guildMusicSettings1.setId(1L);
//        GuildMusicSettings guildMusicSettings2 = new GuildMusicSettings();
//        guildMusicSettings2.setId(guildMusicSettings1.getId());
//        assertThat(guildMusicSettings1).isEqualTo(guildMusicSettings2);
//        guildMusicSettings2.setId(2L);
//        assertThat(guildMusicSettings1).isNotEqualTo(guildMusicSettings2);
//        guildMusicSettings1.setId(null);
//        assertThat(guildMusicSettings1).isNotEqualTo(guildMusicSettings2);
//    }
//}
