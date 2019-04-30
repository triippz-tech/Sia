//package com.trievosoftware.application.web.rest;
//
//import com.trievosoftware.application.SiaApp;
//
//import com.trievosoftware.application.domain.DiscordGuild;
//import com.trievosoftware.application.domain.GuildSettings;
//import com.trievosoftware.application.domain.AuditCache;
//import com.trievosoftware.application.domain.AutoMod;
//import com.trievosoftware.application.domain.GuildMusicSettings;
//import com.trievosoftware.application.domain.DiscordUser;
//import com.trievosoftware.application.domain.Ignored;
//import com.trievosoftware.application.domain.TempMutes;
//import com.trievosoftware.application.domain.TempBans;
//import com.trievosoftware.application.domain.Poll;
//import com.trievosoftware.application.domain.GuildRoles;
//import com.trievosoftware.application.domain.CustomCommand;
//import com.trievosoftware.application.domain.Actions;
//import com.trievosoftware.application.domain.Strikes;
//import com.trievosoftware.application.domain.WelcomeMessage;
//import com.trievosoftware.application.domain.GuildEvent;
//import com.trievosoftware.application.domain.GiveAway;
//import com.trievosoftware.application.repository.DiscordGuildRepository;
//import com.trievosoftware.application.service.DiscordGuildService;
//import com.trievosoftware.application.web.rest.errors.ExceptionTranslator;
//import com.trievosoftware.application.service.dto.DiscordGuildCriteria;
//import com.trievosoftware.application.service.DiscordGuildQueryService;
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
// * Test class for the DiscordGuildResource REST controller.
// *
// * @see DiscordGuildResource
// */
//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = SiaApp.class)
//public class DiscordGuildResourceIntTest {
//
//    private static final Long DEFAULT_GUILD_ID = 1L;
//    private static final Long UPDATED_GUILD_ID = 2L;
//
//    private static final String DEFAULT_GUILD_NAME = "AAAAAAAAAA";
//    private static final String UPDATED_GUILD_NAME = "BBBBBBBBBB";
//
//    private static final String DEFAULT_INVITE_LINK = "AAAAAAAAAA";
//    private static final String UPDATED_INVITE_LINK = "BBBBBBBBBB";
//
//    @Autowired
//    private DiscordGuildRepository discordGuildRepository;
//
//    @Autowired
//    private DiscordGuildService discordGuildService;
//
//    @Autowired
//    private DiscordGuildQueryService discordGuildQueryService;
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
//    private MockMvc restDiscordGuildMockMvc;
//
//    private DiscordGuild discordGuild;
//
//    @Before
//    public void setup() {
//        MockitoAnnotations.initMocks(this);
//        final DiscordGuildResource discordGuildResource = new DiscordGuildResource(discordGuildService, discordGuildQueryService);
//        this.restDiscordGuildMockMvc = MockMvcBuilders.standaloneSetup(discordGuildResource)
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
//    public static DiscordGuild createEntity(EntityManager em) {
//        DiscordGuild discordGuild = new DiscordGuild()
//            .guildId(DEFAULT_GUILD_ID)
//            .guildName(DEFAULT_GUILD_NAME)
//            .inviteLink(DEFAULT_INVITE_LINK);
//        return discordGuild;
//    }
//
//    @Before
//    public void initTest() {
//        discordGuild = createEntity(em);
//    }
//
//    @Test
//    @Transactional
//    public void createDiscordGuild() throws Exception {
//        int databaseSizeBeforeCreate = discordGuildRepository.findAll().size();
//
//        // Create the DiscordGuild
//        restDiscordGuildMockMvc.perform(post("/api/discord-guilds")
//            .contentType(TestUtil.APPLICATION_JSON_UTF8)
//            .content(TestUtil.convertObjectToJsonBytes(discordGuild)))
//            .andExpect(status().isCreated());
//
//        // Validate the DiscordGuild in the database
//        List<DiscordGuild> discordGuildList = discordGuildRepository.findAll();
//        assertThat(discordGuildList).hasSize(databaseSizeBeforeCreate + 1);
//        DiscordGuild testDiscordGuild = discordGuildList.get(discordGuildList.size() - 1);
//        assertThat(testDiscordGuild.getGuildId()).isEqualTo(DEFAULT_GUILD_ID);
//        assertThat(testDiscordGuild.getGuildName()).isEqualTo(DEFAULT_GUILD_NAME);
//        assertThat(testDiscordGuild.getInviteLink()).isEqualTo(DEFAULT_INVITE_LINK);
//    }
//
//    @Test
//    @Transactional
//    public void createDiscordGuildWithExistingId() throws Exception {
//        int databaseSizeBeforeCreate = discordGuildRepository.findAll().size();
//
//        // Create the DiscordGuild with an existing ID
//        discordGuild.setId(1L);
//
//        // An entity with an existing ID cannot be created, so this API call must fail
//        restDiscordGuildMockMvc.perform(post("/api/discord-guilds")
//            .contentType(TestUtil.APPLICATION_JSON_UTF8)
//            .content(TestUtil.convertObjectToJsonBytes(discordGuild)))
//            .andExpect(status().isBadRequest());
//
//        // Validate the DiscordGuild in the database
//        List<DiscordGuild> discordGuildList = discordGuildRepository.findAll();
//        assertThat(discordGuildList).hasSize(databaseSizeBeforeCreate);
//    }
//
//    @Test
//    @Transactional
//    public void checkGuildIdIsRequired() throws Exception {
//        int databaseSizeBeforeTest = discordGuildRepository.findAll().size();
//        // set the field null
//        discordGuild.setGuildId(null);
//
//        // Create the DiscordGuild, which fails.
//
//        restDiscordGuildMockMvc.perform(post("/api/discord-guilds")
//            .contentType(TestUtil.APPLICATION_JSON_UTF8)
//            .content(TestUtil.convertObjectToJsonBytes(discordGuild)))
//            .andExpect(status().isBadRequest());
//
//        List<DiscordGuild> discordGuildList = discordGuildRepository.findAll();
//        assertThat(discordGuildList).hasSize(databaseSizeBeforeTest);
//    }
//
//    @Test
//    @Transactional
//    public void checkGuildNameIsRequired() throws Exception {
//        int databaseSizeBeforeTest = discordGuildRepository.findAll().size();
//        // set the field null
//        discordGuild.setGuildName(null);
//
//        // Create the DiscordGuild, which fails.
//
//        restDiscordGuildMockMvc.perform(post("/api/discord-guilds")
//            .contentType(TestUtil.APPLICATION_JSON_UTF8)
//            .content(TestUtil.convertObjectToJsonBytes(discordGuild)))
//            .andExpect(status().isBadRequest());
//
//        List<DiscordGuild> discordGuildList = discordGuildRepository.findAll();
//        assertThat(discordGuildList).hasSize(databaseSizeBeforeTest);
//    }
//
//    @Test
//    @Transactional
//    public void getAllDiscordGuilds() throws Exception {
//        // Initialize the database
//        discordGuildRepository.saveAndFlush(discordGuild);
//
//        // Get all the discordGuildList
//        restDiscordGuildMockMvc.perform(get("/api/discord-guilds?sort=id,desc"))
//            .andExpect(status().isOk())
//            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
//            .andExpect(jsonPath("$.[*].id").value(hasItem(discordGuild.getId().intValue())))
//            .andExpect(jsonPath("$.[*].guildId").value(hasItem(DEFAULT_GUILD_ID.intValue())))
//            .andExpect(jsonPath("$.[*].guildName").value(hasItem(DEFAULT_GUILD_NAME.toString())))
//            .andExpect(jsonPath("$.[*].inviteLink").value(hasItem(DEFAULT_INVITE_LINK.toString())));
//    }
//
//    @Test
//    @Transactional
//    public void getDiscordGuild() throws Exception {
//        // Initialize the database
//        discordGuildRepository.saveAndFlush(discordGuild);
//
//        // Get the discordGuild
//        restDiscordGuildMockMvc.perform(get("/api/discord-guilds/{id}", discordGuild.getId()))
//            .andExpect(status().isOk())
//            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
//            .andExpect(jsonPath("$.id").value(discordGuild.getId().intValue()))
//            .andExpect(jsonPath("$.guildId").value(DEFAULT_GUILD_ID.intValue()))
//            .andExpect(jsonPath("$.guildName").value(DEFAULT_GUILD_NAME.toString()))
//            .andExpect(jsonPath("$.inviteLink").value(DEFAULT_INVITE_LINK.toString()));
//    }
//
//    @Test
//    @Transactional
//    public void getAllDiscordGuildsByGuildIdIsEqualToSomething() throws Exception {
//        // Initialize the database
//        discordGuildRepository.saveAndFlush(discordGuild);
//
//        // Get all the discordGuildList where guildId equals to DEFAULT_GUILD_ID
//        defaultDiscordGuildShouldBeFound("guildId.equals=" + DEFAULT_GUILD_ID);
//
//        // Get all the discordGuildList where guildId equals to UPDATED_GUILD_ID
//        defaultDiscordGuildShouldNotBeFound("guildId.equals=" + UPDATED_GUILD_ID);
//    }
//
//    @Test
//    @Transactional
//    public void getAllDiscordGuildsByGuildIdIsInShouldWork() throws Exception {
//        // Initialize the database
//        discordGuildRepository.saveAndFlush(discordGuild);
//
//        // Get all the discordGuildList where guildId in DEFAULT_GUILD_ID or UPDATED_GUILD_ID
//        defaultDiscordGuildShouldBeFound("guildId.in=" + DEFAULT_GUILD_ID + "," + UPDATED_GUILD_ID);
//
//        // Get all the discordGuildList where guildId equals to UPDATED_GUILD_ID
//        defaultDiscordGuildShouldNotBeFound("guildId.in=" + UPDATED_GUILD_ID);
//    }
//
//    @Test
//    @Transactional
//    public void getAllDiscordGuildsByGuildIdIsNullOrNotNull() throws Exception {
//        // Initialize the database
//        discordGuildRepository.saveAndFlush(discordGuild);
//
//        // Get all the discordGuildList where guildId is not null
//        defaultDiscordGuildShouldBeFound("guildId.specified=true");
//
//        // Get all the discordGuildList where guildId is null
//        defaultDiscordGuildShouldNotBeFound("guildId.specified=false");
//    }
//
//    @Test
//    @Transactional
//    public void getAllDiscordGuildsByGuildIdIsGreaterThanOrEqualToSomething() throws Exception {
//        // Initialize the database
//        discordGuildRepository.saveAndFlush(discordGuild);
//
//        // Get all the discordGuildList where guildId greater than or equals to DEFAULT_GUILD_ID
//        defaultDiscordGuildShouldBeFound("guildId.greaterOrEqualThan=" + DEFAULT_GUILD_ID);
//
//        // Get all the discordGuildList where guildId greater than or equals to UPDATED_GUILD_ID
//        defaultDiscordGuildShouldNotBeFound("guildId.greaterOrEqualThan=" + UPDATED_GUILD_ID);
//    }
//
//    @Test
//    @Transactional
//    public void getAllDiscordGuildsByGuildIdIsLessThanSomething() throws Exception {
//        // Initialize the database
//        discordGuildRepository.saveAndFlush(discordGuild);
//
//        // Get all the discordGuildList where guildId less than or equals to DEFAULT_GUILD_ID
//        defaultDiscordGuildShouldNotBeFound("guildId.lessThan=" + DEFAULT_GUILD_ID);
//
//        // Get all the discordGuildList where guildId less than or equals to UPDATED_GUILD_ID
//        defaultDiscordGuildShouldBeFound("guildId.lessThan=" + UPDATED_GUILD_ID);
//    }
//
//
//    @Test
//    @Transactional
//    public void getAllDiscordGuildsByGuildNameIsEqualToSomething() throws Exception {
//        // Initialize the database
//        discordGuildRepository.saveAndFlush(discordGuild);
//
//        // Get all the discordGuildList where guildName equals to DEFAULT_GUILD_NAME
//        defaultDiscordGuildShouldBeFound("guildName.equals=" + DEFAULT_GUILD_NAME);
//
//        // Get all the discordGuildList where guildName equals to UPDATED_GUILD_NAME
//        defaultDiscordGuildShouldNotBeFound("guildName.equals=" + UPDATED_GUILD_NAME);
//    }
//
//    @Test
//    @Transactional
//    public void getAllDiscordGuildsByGuildNameIsInShouldWork() throws Exception {
//        // Initialize the database
//        discordGuildRepository.saveAndFlush(discordGuild);
//
//        // Get all the discordGuildList where guildName in DEFAULT_GUILD_NAME or UPDATED_GUILD_NAME
//        defaultDiscordGuildShouldBeFound("guildName.in=" + DEFAULT_GUILD_NAME + "," + UPDATED_GUILD_NAME);
//
//        // Get all the discordGuildList where guildName equals to UPDATED_GUILD_NAME
//        defaultDiscordGuildShouldNotBeFound("guildName.in=" + UPDATED_GUILD_NAME);
//    }
//
//    @Test
//    @Transactional
//    public void getAllDiscordGuildsByGuildNameIsNullOrNotNull() throws Exception {
//        // Initialize the database
//        discordGuildRepository.saveAndFlush(discordGuild);
//
//        // Get all the discordGuildList where guildName is not null
//        defaultDiscordGuildShouldBeFound("guildName.specified=true");
//
//        // Get all the discordGuildList where guildName is null
//        defaultDiscordGuildShouldNotBeFound("guildName.specified=false");
//    }
//
//    @Test
//    @Transactional
//    public void getAllDiscordGuildsByInviteLinkIsEqualToSomething() throws Exception {
//        // Initialize the database
//        discordGuildRepository.saveAndFlush(discordGuild);
//
//        // Get all the discordGuildList where inviteLink equals to DEFAULT_INVITE_LINK
//        defaultDiscordGuildShouldBeFound("inviteLink.equals=" + DEFAULT_INVITE_LINK);
//
//        // Get all the discordGuildList where inviteLink equals to UPDATED_INVITE_LINK
//        defaultDiscordGuildShouldNotBeFound("inviteLink.equals=" + UPDATED_INVITE_LINK);
//    }
//
//    @Test
//    @Transactional
//    public void getAllDiscordGuildsByInviteLinkIsInShouldWork() throws Exception {
//        // Initialize the database
//        discordGuildRepository.saveAndFlush(discordGuild);
//
//        // Get all the discordGuildList where inviteLink in DEFAULT_INVITE_LINK or UPDATED_INVITE_LINK
//        defaultDiscordGuildShouldBeFound("inviteLink.in=" + DEFAULT_INVITE_LINK + "," + UPDATED_INVITE_LINK);
//
//        // Get all the discordGuildList where inviteLink equals to UPDATED_INVITE_LINK
//        defaultDiscordGuildShouldNotBeFound("inviteLink.in=" + UPDATED_INVITE_LINK);
//    }
//
//    @Test
//    @Transactional
//    public void getAllDiscordGuildsByInviteLinkIsNullOrNotNull() throws Exception {
//        // Initialize the database
//        discordGuildRepository.saveAndFlush(discordGuild);
//
//        // Get all the discordGuildList where inviteLink is not null
//        defaultDiscordGuildShouldBeFound("inviteLink.specified=true");
//
//        // Get all the discordGuildList where inviteLink is null
//        defaultDiscordGuildShouldNotBeFound("inviteLink.specified=false");
//    }
//
//    @Test
//    @Transactional
//    public void getAllDiscordGuildsByGuildSettingsIsEqualToSomething() throws Exception {
//        // Initialize the database
//        GuildSettings guildSettings = GuildSettingsResourceIntTest.createEntity(em);
//        em.persist(guildSettings);
//        em.flush();
//        discordGuild.setGuildSettings(guildSettings);
//        discordGuildRepository.saveAndFlush(discordGuild);
//        Long guildSettingsId = guildSettings.getId();
//
//        // Get all the discordGuildList where guildSettings equals to guildSettingsId
//        defaultDiscordGuildShouldBeFound("guildSettingsId.equals=" + guildSettingsId);
//
//        // Get all the discordGuildList where guildSettings equals to guildSettingsId + 1
//        defaultDiscordGuildShouldNotBeFound("guildSettingsId.equals=" + (guildSettingsId + 1));
//    }
//
//
//    @Test
//    @Transactional
//    public void getAllDiscordGuildsByAuditCacheIsEqualToSomething() throws Exception {
//        // Initialize the database
//        AuditCache auditCache = AuditCacheResourceIntTest.createEntity(em);
//        em.persist(auditCache);
//        em.flush();
//        discordGuild.setAuditCache(auditCache);
//        discordGuildRepository.saveAndFlush(discordGuild);
//        Long auditCacheId = auditCache.getId();
//
//        // Get all the discordGuildList where auditCache equals to auditCacheId
//        defaultDiscordGuildShouldBeFound("auditCacheId.equals=" + auditCacheId);
//
//        // Get all the discordGuildList where auditCache equals to auditCacheId + 1
//        defaultDiscordGuildShouldNotBeFound("auditCacheId.equals=" + (auditCacheId + 1));
//    }
//
//
//    @Test
//    @Transactional
//    public void getAllDiscordGuildsByAutoModIsEqualToSomething() throws Exception {
//        // Initialize the database
//        AutoMod autoMod = AutoModResourceIntTest.createEntity(em);
//        em.persist(autoMod);
//        em.flush();
//        discordGuild.setAutoMod(autoMod);
//        discordGuildRepository.saveAndFlush(discordGuild);
//        Long autoModId = autoMod.getId();
//
//        // Get all the discordGuildList where autoMod equals to autoModId
//        defaultDiscordGuildShouldBeFound("autoModId.equals=" + autoModId);
//
//        // Get all the discordGuildList where autoMod equals to autoModId + 1
//        defaultDiscordGuildShouldNotBeFound("autoModId.equals=" + (autoModId + 1));
//    }
//
//
//    @Test
//    @Transactional
//    public void getAllDiscordGuildsByGuildMusicSettingsIsEqualToSomething() throws Exception {
//        // Initialize the database
//        GuildMusicSettings guildMusicSettings = GuildMusicSettingsResourceIntTest.createEntity(em);
//        em.persist(guildMusicSettings);
//        em.flush();
//        discordGuild.setGuildMusicSettings(guildMusicSettings);
//        discordGuildRepository.saveAndFlush(discordGuild);
//        Long guildMusicSettingsId = guildMusicSettings.getId();
//
//        // Get all the discordGuildList where guildMusicSettings equals to guildMusicSettingsId
//        defaultDiscordGuildShouldBeFound("guildMusicSettingsId.equals=" + guildMusicSettingsId);
//
//        // Get all the discordGuildList where guildMusicSettings equals to guildMusicSettingsId + 1
//        defaultDiscordGuildShouldNotBeFound("guildMusicSettingsId.equals=" + (guildMusicSettingsId + 1));
//    }
//
//
//    @Test
//    @Transactional
//    public void getAllDiscordGuildsByDiscordUserIsEqualToSomething() throws Exception {
//        // Initialize the database
//        DiscordUser discordUser = DiscordUserResourceIntTest.createEntity(em);
//        em.persist(discordUser);
//        em.flush();
//        discordGuild.addDiscordUser(discordUser);
//        discordGuildRepository.saveAndFlush(discordGuild);
//        Long discordUserId = discordUser.getId();
//
//        // Get all the discordGuildList where discordUser equals to discordUserId
//        defaultDiscordGuildShouldBeFound("discordUserId.equals=" + discordUserId);
//
//        // Get all the discordGuildList where discordUser equals to discordUserId + 1
//        defaultDiscordGuildShouldNotBeFound("discordUserId.equals=" + (discordUserId + 1));
//    }
//
//
//    @Test
//    @Transactional
//    public void getAllDiscordGuildsByIgnoredIsEqualToSomething() throws Exception {
//        // Initialize the database
//        Ignored ignored = IgnoredResourceIntTest.createEntity(em);
//        em.persist(ignored);
//        em.flush();
//        discordGuild.addIgnored(ignored);
//        discordGuildRepository.saveAndFlush(discordGuild);
//        Long ignoredId = ignored.getId();
//
//        // Get all the discordGuildList where ignored equals to ignoredId
//        defaultDiscordGuildShouldBeFound("ignoredId.equals=" + ignoredId);
//
//        // Get all the discordGuildList where ignored equals to ignoredId + 1
//        defaultDiscordGuildShouldNotBeFound("ignoredId.equals=" + (ignoredId + 1));
//    }
//
//
//    @Test
//    @Transactional
//    public void getAllDiscordGuildsByTempMutesIsEqualToSomething() throws Exception {
//        // Initialize the database
//        TempMutes tempMutes = TempMutesResourceIntTest.createEntity(em);
//        em.persist(tempMutes);
//        em.flush();
//        discordGuild.addTempMutes(tempMutes);
//        discordGuildRepository.saveAndFlush(discordGuild);
//        Long tempMutesId = tempMutes.getId();
//
//        // Get all the discordGuildList where tempMutes equals to tempMutesId
//        defaultDiscordGuildShouldBeFound("tempMutesId.equals=" + tempMutesId);
//
//        // Get all the discordGuildList where tempMutes equals to tempMutesId + 1
//        defaultDiscordGuildShouldNotBeFound("tempMutesId.equals=" + (tempMutesId + 1));
//    }
//
//
//    @Test
//    @Transactional
//    public void getAllDiscordGuildsByTempBansIsEqualToSomething() throws Exception {
//        // Initialize the database
//        TempBans tempBans = TempBansResourceIntTest.createEntity(em);
//        em.persist(tempBans);
//        em.flush();
//        discordGuild.addTempBans(tempBans);
//        discordGuildRepository.saveAndFlush(discordGuild);
//        Long tempBansId = tempBans.getId();
//
//        // Get all the discordGuildList where tempBans equals to tempBansId
//        defaultDiscordGuildShouldBeFound("tempBansId.equals=" + tempBansId);
//
//        // Get all the discordGuildList where tempBans equals to tempBansId + 1
//        defaultDiscordGuildShouldNotBeFound("tempBansId.equals=" + (tempBansId + 1));
//    }
//
//
//    @Test
//    @Transactional
//    public void getAllDiscordGuildsByPollIsEqualToSomething() throws Exception {
//        // Initialize the database
//        Poll poll = PollResourceIntTest.createEntity(em);
//        em.persist(poll);
//        em.flush();
//        discordGuild.addPoll(poll);
//        discordGuildRepository.saveAndFlush(discordGuild);
//        Long pollId = poll.getId();
//
//        // Get all the discordGuildList where poll equals to pollId
//        defaultDiscordGuildShouldBeFound("pollId.equals=" + pollId);
//
//        // Get all the discordGuildList where poll equals to pollId + 1
//        defaultDiscordGuildShouldNotBeFound("pollId.equals=" + (pollId + 1));
//    }
//
//
//    @Test
//    @Transactional
//    public void getAllDiscordGuildsByGuildRolesIsEqualToSomething() throws Exception {
//        // Initialize the database
//        GuildRoles guildRoles = GuildRolesResourceIntTest.createEntity(em);
//        em.persist(guildRoles);
//        em.flush();
//        discordGuild.addGuildRoles(guildRoles);
//        discordGuildRepository.saveAndFlush(discordGuild);
//        Long guildRolesId = guildRoles.getId();
//
//        // Get all the discordGuildList where guildRoles equals to guildRolesId
//        defaultDiscordGuildShouldBeFound("guildRolesId.equals=" + guildRolesId);
//
//        // Get all the discordGuildList where guildRoles equals to guildRolesId + 1
//        defaultDiscordGuildShouldNotBeFound("guildRolesId.equals=" + (guildRolesId + 1));
//    }
//
//
//    @Test
//    @Transactional
//    public void getAllDiscordGuildsByCustomCommandIsEqualToSomething() throws Exception {
//        // Initialize the database
//        CustomCommand customCommand = CustomCommandResourceIntTest.createEntity(em);
//        em.persist(customCommand);
//        em.flush();
//        discordGuild.addCustomCommand(customCommand);
//        discordGuildRepository.saveAndFlush(discordGuild);
//        Long customCommandId = customCommand.getId();
//
//        // Get all the discordGuildList where customCommand equals to customCommandId
//        defaultDiscordGuildShouldBeFound("customCommandId.equals=" + customCommandId);
//
//        // Get all the discordGuildList where customCommand equals to customCommandId + 1
//        defaultDiscordGuildShouldNotBeFound("customCommandId.equals=" + (customCommandId + 1));
//    }
//
//
//    @Test
//    @Transactional
//    public void getAllDiscordGuildsByActionsIsEqualToSomething() throws Exception {
//        // Initialize the database
//        Actions actions = ActionsResourceIntTest.createEntity(em);
//        em.persist(actions);
//        em.flush();
//        discordGuild.addActions(actions);
//        discordGuildRepository.saveAndFlush(discordGuild);
//        Long actionsId = actions.getId();
//
//        // Get all the discordGuildList where actions equals to actionsId
//        defaultDiscordGuildShouldBeFound("actionsId.equals=" + actionsId);
//
//        // Get all the discordGuildList where actions equals to actionsId + 1
//        defaultDiscordGuildShouldNotBeFound("actionsId.equals=" + (actionsId + 1));
//    }
//
//
//    @Test
//    @Transactional
//    public void getAllDiscordGuildsByStrikesIsEqualToSomething() throws Exception {
//        // Initialize the database
//        Strikes strikes = StrikesResourceIntTest.createEntity(em);
//        em.persist(strikes);
//        em.flush();
//        discordGuild.addStrikes(strikes);
//        discordGuildRepository.saveAndFlush(discordGuild);
//        Long strikesId = strikes.getId();
//
//        // Get all the discordGuildList where strikes equals to strikesId
//        defaultDiscordGuildShouldBeFound("strikesId.equals=" + strikesId);
//
//        // Get all the discordGuildList where strikes equals to strikesId + 1
//        defaultDiscordGuildShouldNotBeFound("strikesId.equals=" + (strikesId + 1));
//    }
//
//
//    @Test
//    @Transactional
//    public void getAllDiscordGuildsByWelcomeMessageIsEqualToSomething() throws Exception {
//        // Initialize the database
//        WelcomeMessage welcomeMessage = WelcomeMessageResourceIntTest.createEntity(em);
//        em.persist(welcomeMessage);
//        em.flush();
//        discordGuild.addWelcomeMessage(welcomeMessage);
//        discordGuildRepository.saveAndFlush(discordGuild);
//        Long welcomeMessageId = welcomeMessage.getId();
//
//        // Get all the discordGuildList where welcomeMessage equals to welcomeMessageId
//        defaultDiscordGuildShouldBeFound("welcomeMessageId.equals=" + welcomeMessageId);
//
//        // Get all the discordGuildList where welcomeMessage equals to welcomeMessageId + 1
//        defaultDiscordGuildShouldNotBeFound("welcomeMessageId.equals=" + (welcomeMessageId + 1));
//    }
//
//
//    @Test
//    @Transactional
//    public void getAllDiscordGuildsByGuildEventIsEqualToSomething() throws Exception {
//        // Initialize the database
//        GuildEvent guildEvent = GuildEventResourceIntTest.createEntity(em);
//        em.persist(guildEvent);
//        em.flush();
//        discordGuild.addGuildEvent(guildEvent);
//        discordGuildRepository.saveAndFlush(discordGuild);
//        Long guildEventId = guildEvent.getId();
//
//        // Get all the discordGuildList where guildEvent equals to guildEventId
//        defaultDiscordGuildShouldBeFound("guildEventId.equals=" + guildEventId);
//
//        // Get all the discordGuildList where guildEvent equals to guildEventId + 1
//        defaultDiscordGuildShouldNotBeFound("guildEventId.equals=" + (guildEventId + 1));
//    }
//
//
//    @Test
//    @Transactional
//    public void getAllDiscordGuildsByGiveAwayIsEqualToSomething() throws Exception {
//        // Initialize the database
//        GiveAway giveAway = GiveAwayResourceIntTest.createEntity(em);
//        em.persist(giveAway);
//        em.flush();
//        discordGuild.addGiveAway(giveAway);
//        discordGuildRepository.saveAndFlush(discordGuild);
//        Long giveAwayId = giveAway.getId();
//
//        // Get all the discordGuildList where giveAway equals to giveAwayId
//        defaultDiscordGuildShouldBeFound("giveAwayId.equals=" + giveAwayId);
//
//        // Get all the discordGuildList where giveAway equals to giveAwayId + 1
//        defaultDiscordGuildShouldNotBeFound("giveAwayId.equals=" + (giveAwayId + 1));
//    }
//
//    /**
//     * Executes the search, and checks that the default entity is returned
//     */
//    private void defaultDiscordGuildShouldBeFound(String filter) throws Exception {
//        restDiscordGuildMockMvc.perform(get("/api/discord-guilds?sort=id,desc&" + filter))
//            .andExpect(status().isOk())
//            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
//            .andExpect(jsonPath("$.[*].id").value(hasItem(discordGuild.getId().intValue())))
//            .andExpect(jsonPath("$.[*].guildId").value(hasItem(DEFAULT_GUILD_ID.intValue())))
//            .andExpect(jsonPath("$.[*].guildName").value(hasItem(DEFAULT_GUILD_NAME)))
//            .andExpect(jsonPath("$.[*].inviteLink").value(hasItem(DEFAULT_INVITE_LINK)));
//
//        // Check, that the count call also returns 1
//        restDiscordGuildMockMvc.perform(get("/api/discord-guilds/count?sort=id,desc&" + filter))
//            .andExpect(status().isOk())
//            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
//            .andExpect(content().string("1"));
//    }
//
//    /**
//     * Executes the search, and checks that the default entity is not returned
//     */
//    private void defaultDiscordGuildShouldNotBeFound(String filter) throws Exception {
//        restDiscordGuildMockMvc.perform(get("/api/discord-guilds?sort=id,desc&" + filter))
//            .andExpect(status().isOk())
//            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
//            .andExpect(jsonPath("$").isArray())
//            .andExpect(jsonPath("$").isEmpty());
//
//        // Check, that the count call also returns 0
//        restDiscordGuildMockMvc.perform(get("/api/discord-guilds/count?sort=id,desc&" + filter))
//            .andExpect(status().isOk())
//            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
//            .andExpect(content().string("0"));
//    }
//
//
//    @Test
//    @Transactional
//    public void getNonExistingDiscordGuild() throws Exception {
//        // Get the discordGuild
//        restDiscordGuildMockMvc.perform(get("/api/discord-guilds/{id}", Long.MAX_VALUE))
//            .andExpect(status().isNotFound());
//    }
//
//    @Test
//    @Transactional
//    public void updateDiscordGuild() throws Exception {
//        // Initialize the database
//        discordGuildService.save(discordGuild);
//
//        int databaseSizeBeforeUpdate = discordGuildRepository.findAll().size();
//
//        // Update the discordGuild
//        DiscordGuild updatedDiscordGuild = discordGuildRepository.findById(discordGuild.getId()).get();
//        // Disconnect from session so that the updates on updatedDiscordGuild are not directly saved in db
//        em.detach(updatedDiscordGuild);
//        updatedDiscordGuild
//            .guildId(UPDATED_GUILD_ID)
//            .guildName(UPDATED_GUILD_NAME)
//            .inviteLink(UPDATED_INVITE_LINK);
//
//        restDiscordGuildMockMvc.perform(put("/api/discord-guilds")
//            .contentType(TestUtil.APPLICATION_JSON_UTF8)
//            .content(TestUtil.convertObjectToJsonBytes(updatedDiscordGuild)))
//            .andExpect(status().isOk());
//
//        // Validate the DiscordGuild in the database
//        List<DiscordGuild> discordGuildList = discordGuildRepository.findAll();
//        assertThat(discordGuildList).hasSize(databaseSizeBeforeUpdate);
//        DiscordGuild testDiscordGuild = discordGuildList.get(discordGuildList.size() - 1);
//        assertThat(testDiscordGuild.getGuildId()).isEqualTo(UPDATED_GUILD_ID);
//        assertThat(testDiscordGuild.getGuildName()).isEqualTo(UPDATED_GUILD_NAME);
//        assertThat(testDiscordGuild.getInviteLink()).isEqualTo(UPDATED_INVITE_LINK);
//    }
//
//    @Test
//    @Transactional
//    public void updateNonExistingDiscordGuild() throws Exception {
//        int databaseSizeBeforeUpdate = discordGuildRepository.findAll().size();
//
//        // Create the DiscordGuild
//
//        // If the entity doesn't have an ID, it will throw BadRequestAlertException
//        restDiscordGuildMockMvc.perform(put("/api/discord-guilds")
//            .contentType(TestUtil.APPLICATION_JSON_UTF8)
//            .content(TestUtil.convertObjectToJsonBytes(discordGuild)))
//            .andExpect(status().isBadRequest());
//
//        // Validate the DiscordGuild in the database
//        List<DiscordGuild> discordGuildList = discordGuildRepository.findAll();
//        assertThat(discordGuildList).hasSize(databaseSizeBeforeUpdate);
//    }
//
//    @Test
//    @Transactional
//    public void deleteDiscordGuild() throws Exception {
//        // Initialize the database
//        discordGuildService.save(discordGuild);
//
//        int databaseSizeBeforeDelete = discordGuildRepository.findAll().size();
//
//        // Delete the discordGuild
//        restDiscordGuildMockMvc.perform(delete("/api/discord-guilds/{id}", discordGuild.getId())
//            .accept(TestUtil.APPLICATION_JSON_UTF8))
//            .andExpect(status().isOk());
//
//        // Validate the database is empty
//        List<DiscordGuild> discordGuildList = discordGuildRepository.findAll();
//        assertThat(discordGuildList).hasSize(databaseSizeBeforeDelete - 1);
//    }
//
//    @Test
//    @Transactional
//    public void equalsVerifier() throws Exception {
//        TestUtil.equalsVerifier(DiscordGuild.class);
//        DiscordGuild discordGuild1 = new DiscordGuild();
//        discordGuild1.setId(1L);
//        DiscordGuild discordGuild2 = new DiscordGuild();
//        discordGuild2.setId(discordGuild1.getId());
//        assertThat(discordGuild1).isEqualTo(discordGuild2);
//        discordGuild2.setId(2L);
//        assertThat(discordGuild1).isNotEqualTo(discordGuild2);
//        discordGuild1.setId(null);
//        assertThat(discordGuild1).isNotEqualTo(discordGuild2);
//    }
//}
