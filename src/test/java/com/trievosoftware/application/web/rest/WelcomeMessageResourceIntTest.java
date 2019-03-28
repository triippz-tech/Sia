//package com.trievosoftware.application.web.rest;
//
//import com.trievosoftware.application.SiaApp;
//import com.trievosoftware.application.domain.GuildSettings;
//import com.trievosoftware.application.domain.WelcomeMessage;
//import com.trievosoftware.application.repository.WelcomeMessageRepository;
//import com.trievosoftware.application.service.WelcomeMessageQueryService;
//import com.trievosoftware.application.service.WelcomeMessageService;
//import com.trievosoftware.application.web.rest.errors.ExceptionTranslator;
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
//import static com.trievosoftware.application.web.rest.TestUtil.createFormattingConversionService;
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.hamcrest.Matchers.hasItem;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
///**
// * Test class for the WelcomeMessageResource REST controller.
// *
// * @see WelcomeMessageResource
// */
//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = SiaApp.class)
//public class WelcomeMessageResourceIntTest {
//
//    private static final String DEFAULT_NAME = "AAAAAAAAAA";
//    private static final String UPDATED_NAME = "BBBBBBBBBB";
//
//    private static final String DEFAULT_MESSAGE_TITLE = "AAAAAAAAAA";
//    private static final String UPDATED_MESSAGE_TITLE = "BBBBBBBBBB";
//
//    private static final String DEFAULT_BODY = "AAAAAAAAAA";
//    private static final String UPDATED_BODY = "BBBBBBBBBB";
//
//    private static final String DEFAULT_FOOTER = "AAAAAAAAAA";
//    private static final String UPDATED_FOOTER = "BBBBBBBBBB";
//
//    private static final String DEFAULT_WEBSITE_URL = "AAAAAAAAAA";
//    private static final String UPDATED_WEBSITE_URL = "BBBBBBBBBB";
//
//    private static final String DEFAULT_LOGO_URL = "AAAAAAAAAA";
//    private static final String UPDATED_LOGO_URL = "BBBBBBBBBB";
//
//    private static final Boolean DEFAULT_ACTIVE = false;
//    private static final Boolean UPDATED_ACTIVE = true;
//
//    @Autowired
//    private WelcomeMessageRepository welcomeMessageRepository;
//
//    @Autowired
//    private WelcomeMessageService welcomeMessageService;
//
//    @Autowired
//    private WelcomeMessageQueryService welcomeMessageQueryService;
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
//    private MockMvc restWelcomeMessageMockMvc;
//
//    private WelcomeMessage welcomeMessage;
//
//    @Before
//    public void setup() {
//        MockitoAnnotations.initMocks(this);
//        final WelcomeMessageResource welcomeMessageResource = new WelcomeMessageResource(welcomeMessageService, welcomeMessageQueryService);
//        this.restWelcomeMessageMockMvc = MockMvcBuilders.standaloneSetup(welcomeMessageResource)
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
//    public static WelcomeMessage createEntity(EntityManager em) {
//        WelcomeMessage welcomeMessage = new WelcomeMessage()
//            .name(DEFAULT_NAME)
//            .messageTitle(DEFAULT_MESSAGE_TITLE)
//            .body(DEFAULT_BODY)
//            .footer(DEFAULT_FOOTER)
//            .websiteUrl(DEFAULT_WEBSITE_URL)
//            .logoUrl(DEFAULT_LOGO_URL)
//            .active(DEFAULT_ACTIVE);
//        return welcomeMessage;
//    }
//
//    @Before
//    public void initTest() {
//        welcomeMessage = createEntity(em);
//    }
//
//    @Test
//    @Transactional
//    public void createWelcomeMessage() throws Exception {
//        int databaseSizeBeforeCreate = welcomeMessageRepository.findAll().size();
//
//        // Create the WelcomeMessage
//        restWelcomeMessageMockMvc.perform(post("/api/welcome-messages")
//            .contentType(TestUtil.APPLICATION_JSON_UTF8)
//            .content(TestUtil.convertObjectToJsonBytes(welcomeMessage)))
//            .andExpect(status().isCreated());
//
//        // Validate the WelcomeMessage in the database
//        List<WelcomeMessage> welcomeMessageList = welcomeMessageRepository.findAll();
//        assertThat(welcomeMessageList).hasSize(databaseSizeBeforeCreate + 1);
//        WelcomeMessage testWelcomeMessage = welcomeMessageList.get(welcomeMessageList.size() - 1);
//        assertThat(testWelcomeMessage.getName()).isEqualTo(DEFAULT_NAME);
//        assertThat(testWelcomeMessage.getMessageTitle()).isEqualTo(DEFAULT_MESSAGE_TITLE);
//        assertThat(testWelcomeMessage.getBody()).isEqualTo(DEFAULT_BODY);
//        assertThat(testWelcomeMessage.getFooter()).isEqualTo(DEFAULT_FOOTER);
//        assertThat(testWelcomeMessage.getWebsiteUrl()).isEqualTo(DEFAULT_WEBSITE_URL);
//        assertThat(testWelcomeMessage.getLogoUrl()).isEqualTo(DEFAULT_LOGO_URL);
//        assertThat(testWelcomeMessage.isActive()).isEqualTo(DEFAULT_ACTIVE);
//    }
//
//    @Test
//    @Transactional
//    public void createWelcomeMessageWithExistingId() throws Exception {
//        int databaseSizeBeforeCreate = welcomeMessageRepository.findAll().size();
//
//        // Create the WelcomeMessage with an existing ID
//        welcomeMessage.setId(1L);
//
//        // An entity with an existing ID cannot be created, so this API call must fail
//        restWelcomeMessageMockMvc.perform(post("/api/welcome-messages")
//            .contentType(TestUtil.APPLICATION_JSON_UTF8)
//            .content(TestUtil.convertObjectToJsonBytes(welcomeMessage)))
//            .andExpect(status().isBadRequest());
//
//        // Validate the WelcomeMessage in the database
//        List<WelcomeMessage> welcomeMessageList = welcomeMessageRepository.findAll();
//        assertThat(welcomeMessageList).hasSize(databaseSizeBeforeCreate);
//    }
//
//    @Test
//    @Transactional
//    public void checkNameIsRequired() throws Exception {
//        int databaseSizeBeforeTest = welcomeMessageRepository.findAll().size();
//        // set the field null
//        welcomeMessage.setName(null);
//
//        // Create the WelcomeMessage, which fails.
//
//        restWelcomeMessageMockMvc.perform(post("/api/welcome-messages")
//            .contentType(TestUtil.APPLICATION_JSON_UTF8)
//            .content(TestUtil.convertObjectToJsonBytes(welcomeMessage)))
//            .andExpect(status().isBadRequest());
//
//        List<WelcomeMessage> welcomeMessageList = welcomeMessageRepository.findAll();
//        assertThat(welcomeMessageList).hasSize(databaseSizeBeforeTest);
//    }
//
//    @Test
//    @Transactional
//    public void checkMessageTitleIsRequired() throws Exception {
//        int databaseSizeBeforeTest = welcomeMessageRepository.findAll().size();
//        // set the field null
//        welcomeMessage.setMessageTitle(null);
//
//        // Create the WelcomeMessage, which fails.
//
//        restWelcomeMessageMockMvc.perform(post("/api/welcome-messages")
//            .contentType(TestUtil.APPLICATION_JSON_UTF8)
//            .content(TestUtil.convertObjectToJsonBytes(welcomeMessage)))
//            .andExpect(status().isBadRequest());
//
//        List<WelcomeMessage> welcomeMessageList = welcomeMessageRepository.findAll();
//        assertThat(welcomeMessageList).hasSize(databaseSizeBeforeTest);
//    }
//
//    @Test
//    @Transactional
//    public void checkFooterIsRequired() throws Exception {
//        int databaseSizeBeforeTest = welcomeMessageRepository.findAll().size();
//        // set the field null
//        welcomeMessage.setFooter(null);
//
//        // Create the WelcomeMessage, which fails.
//
//        restWelcomeMessageMockMvc.perform(post("/api/welcome-messages")
//            .contentType(TestUtil.APPLICATION_JSON_UTF8)
//            .content(TestUtil.convertObjectToJsonBytes(welcomeMessage)))
//            .andExpect(status().isBadRequest());
//
//        List<WelcomeMessage> welcomeMessageList = welcomeMessageRepository.findAll();
//        assertThat(welcomeMessageList).hasSize(databaseSizeBeforeTest);
//    }
//
//    @Test
//    @Transactional
//    public void checkActiveIsRequired() throws Exception {
//        int databaseSizeBeforeTest = welcomeMessageRepository.findAll().size();
//        // set the field null
//        welcomeMessage.setActive(null);
//
//        // Create the WelcomeMessage, which fails.
//
//        restWelcomeMessageMockMvc.perform(post("/api/welcome-messages")
//            .contentType(TestUtil.APPLICATION_JSON_UTF8)
//            .content(TestUtil.convertObjectToJsonBytes(welcomeMessage)))
//            .andExpect(status().isBadRequest());
//
//        List<WelcomeMessage> welcomeMessageList = welcomeMessageRepository.findAll();
//        assertThat(welcomeMessageList).hasSize(databaseSizeBeforeTest);
//    }
//
//    @Test
//    @Transactional
//    public void getAllWelcomeMessages() throws Exception {
//        // Initialize the database
//        welcomeMessageRepository.saveAndFlush(welcomeMessage);
//
//        // Get all the welcomeMessageList
//        restWelcomeMessageMockMvc.perform(get("/api/welcome-messages?sort=id,desc"))
//            .andExpect(status().isOk())
//            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
//            .andExpect(jsonPath("$.[*].id").value(hasItem(welcomeMessage.getId().intValue())))
//            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
//            .andExpect(jsonPath("$.[*].messageTitle").value(hasItem(DEFAULT_MESSAGE_TITLE.toString())))
//            .andExpect(jsonPath("$.[*].body").value(hasItem(DEFAULT_BODY.toString())))
//            .andExpect(jsonPath("$.[*].footer").value(hasItem(DEFAULT_FOOTER.toString())))
//            .andExpect(jsonPath("$.[*].websiteUrl").value(hasItem(DEFAULT_WEBSITE_URL.toString())))
//            .andExpect(jsonPath("$.[*].logoUrl").value(hasItem(DEFAULT_LOGO_URL.toString())))
//            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())));
//    }
//
//    @Test
//    @Transactional
//    public void getWelcomeMessage() throws Exception {
//        // Initialize the database
//        welcomeMessageRepository.saveAndFlush(welcomeMessage);
//
//        // Get the welcomeMessage
//        restWelcomeMessageMockMvc.perform(get("/api/welcome-messages/{id}", welcomeMessage.getId()))
//            .andExpect(status().isOk())
//            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
//            .andExpect(jsonPath("$.id").value(welcomeMessage.getId().intValue()))
//            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
//            .andExpect(jsonPath("$.messageTitle").value(DEFAULT_MESSAGE_TITLE.toString()))
//            .andExpect(jsonPath("$.body").value(DEFAULT_BODY.toString()))
//            .andExpect(jsonPath("$.footer").value(DEFAULT_FOOTER.toString()))
//            .andExpect(jsonPath("$.websiteUrl").value(DEFAULT_WEBSITE_URL.toString()))
//            .andExpect(jsonPath("$.logoUrl").value(DEFAULT_LOGO_URL.toString()))
//            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()));
//    }
//
//    @Test
//    @Transactional
//    public void getAllWelcomeMessagesByNameIsEqualToSomething() throws Exception {
//        // Initialize the database
//        welcomeMessageRepository.saveAndFlush(welcomeMessage);
//
//        // Get all the welcomeMessageList where name equals to DEFAULT_NAME
//        defaultWelcomeMessageShouldBeFound("name.equals=" + DEFAULT_NAME);
//
//        // Get all the welcomeMessageList where name equals to UPDATED_NAME
//        defaultWelcomeMessageShouldNotBeFound("name.equals=" + UPDATED_NAME);
//    }
//
//    @Test
//    @Transactional
//    public void getAllWelcomeMessagesByNameIsInShouldWork() throws Exception {
//        // Initialize the database
//        welcomeMessageRepository.saveAndFlush(welcomeMessage);
//
//        // Get all the welcomeMessageList where name in DEFAULT_NAME or UPDATED_NAME
//        defaultWelcomeMessageShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);
//
//        // Get all the welcomeMessageList where name equals to UPDATED_NAME
//        defaultWelcomeMessageShouldNotBeFound("name.in=" + UPDATED_NAME);
//    }
//
//    @Test
//    @Transactional
//    public void getAllWelcomeMessagesByNameIsNullOrNotNull() throws Exception {
//        // Initialize the database
//        welcomeMessageRepository.saveAndFlush(welcomeMessage);
//
//        // Get all the welcomeMessageList where name is not null
//        defaultWelcomeMessageShouldBeFound("name.specified=true");
//
//        // Get all the welcomeMessageList where name is null
//        defaultWelcomeMessageShouldNotBeFound("name.specified=false");
//    }
//
//    @Test
//    @Transactional
//    public void getAllWelcomeMessagesByMessageTitleIsEqualToSomething() throws Exception {
//        // Initialize the database
//        welcomeMessageRepository.saveAndFlush(welcomeMessage);
//
//        // Get all the welcomeMessageList where messageTitle equals to DEFAULT_MESSAGE_TITLE
//        defaultWelcomeMessageShouldBeFound("messageTitle.equals=" + DEFAULT_MESSAGE_TITLE);
//
//        // Get all the welcomeMessageList where messageTitle equals to UPDATED_MESSAGE_TITLE
//        defaultWelcomeMessageShouldNotBeFound("messageTitle.equals=" + UPDATED_MESSAGE_TITLE);
//    }
//
//    @Test
//    @Transactional
//    public void getAllWelcomeMessagesByMessageTitleIsInShouldWork() throws Exception {
//        // Initialize the database
//        welcomeMessageRepository.saveAndFlush(welcomeMessage);
//
//        // Get all the welcomeMessageList where messageTitle in DEFAULT_MESSAGE_TITLE or UPDATED_MESSAGE_TITLE
//        defaultWelcomeMessageShouldBeFound("messageTitle.in=" + DEFAULT_MESSAGE_TITLE + "," + UPDATED_MESSAGE_TITLE);
//
//        // Get all the welcomeMessageList where messageTitle equals to UPDATED_MESSAGE_TITLE
//        defaultWelcomeMessageShouldNotBeFound("messageTitle.in=" + UPDATED_MESSAGE_TITLE);
//    }
//
//    @Test
//    @Transactional
//    public void getAllWelcomeMessagesByMessageTitleIsNullOrNotNull() throws Exception {
//        // Initialize the database
//        welcomeMessageRepository.saveAndFlush(welcomeMessage);
//
//        // Get all the welcomeMessageList where messageTitle is not null
//        defaultWelcomeMessageShouldBeFound("messageTitle.specified=true");
//
//        // Get all the welcomeMessageList where messageTitle is null
//        defaultWelcomeMessageShouldNotBeFound("messageTitle.specified=false");
//    }
//
//    @Test
//    @Transactional
//    public void getAllWelcomeMessagesByFooterIsEqualToSomething() throws Exception {
//        // Initialize the database
//        welcomeMessageRepository.saveAndFlush(welcomeMessage);
//
//        // Get all the welcomeMessageList where footer equals to DEFAULT_FOOTER
//        defaultWelcomeMessageShouldBeFound("footer.equals=" + DEFAULT_FOOTER);
//
//        // Get all the welcomeMessageList where footer equals to UPDATED_FOOTER
//        defaultWelcomeMessageShouldNotBeFound("footer.equals=" + UPDATED_FOOTER);
//    }
//
//    @Test
//    @Transactional
//    public void getAllWelcomeMessagesByFooterIsInShouldWork() throws Exception {
//        // Initialize the database
//        welcomeMessageRepository.saveAndFlush(welcomeMessage);
//
//        // Get all the welcomeMessageList where footer in DEFAULT_FOOTER or UPDATED_FOOTER
//        defaultWelcomeMessageShouldBeFound("footer.in=" + DEFAULT_FOOTER + "," + UPDATED_FOOTER);
//
//        // Get all the welcomeMessageList where footer equals to UPDATED_FOOTER
//        defaultWelcomeMessageShouldNotBeFound("footer.in=" + UPDATED_FOOTER);
//    }
//
//    @Test
//    @Transactional
//    public void getAllWelcomeMessagesByFooterIsNullOrNotNull() throws Exception {
//        // Initialize the database
//        welcomeMessageRepository.saveAndFlush(welcomeMessage);
//
//        // Get all the welcomeMessageList where footer is not null
//        defaultWelcomeMessageShouldBeFound("footer.specified=true");
//
//        // Get all the welcomeMessageList where footer is null
//        defaultWelcomeMessageShouldNotBeFound("footer.specified=false");
//    }
//
//    @Test
//    @Transactional
//    public void getAllWelcomeMessagesByWebsiteUrlIsEqualToSomething() throws Exception {
//        // Initialize the database
//        welcomeMessageRepository.saveAndFlush(welcomeMessage);
//
//        // Get all the welcomeMessageList where websiteUrl equals to DEFAULT_WEBSITE_URL
//        defaultWelcomeMessageShouldBeFound("websiteUrl.equals=" + DEFAULT_WEBSITE_URL);
//
//        // Get all the welcomeMessageList where websiteUrl equals to UPDATED_WEBSITE_URL
//        defaultWelcomeMessageShouldNotBeFound("websiteUrl.equals=" + UPDATED_WEBSITE_URL);
//    }
//
//    @Test
//    @Transactional
//    public void getAllWelcomeMessagesByWebsiteUrlIsInShouldWork() throws Exception {
//        // Initialize the database
//        welcomeMessageRepository.saveAndFlush(welcomeMessage);
//
//        // Get all the welcomeMessageList where websiteUrl in DEFAULT_WEBSITE_URL or UPDATED_WEBSITE_URL
//        defaultWelcomeMessageShouldBeFound("websiteUrl.in=" + DEFAULT_WEBSITE_URL + "," + UPDATED_WEBSITE_URL);
//
//        // Get all the welcomeMessageList where websiteUrl equals to UPDATED_WEBSITE_URL
//        defaultWelcomeMessageShouldNotBeFound("websiteUrl.in=" + UPDATED_WEBSITE_URL);
//    }
//
//    @Test
//    @Transactional
//    public void getAllWelcomeMessagesByWebsiteUrlIsNullOrNotNull() throws Exception {
//        // Initialize the database
//        welcomeMessageRepository.saveAndFlush(welcomeMessage);
//
//        // Get all the welcomeMessageList where websiteUrl is not null
//        defaultWelcomeMessageShouldBeFound("websiteUrl.specified=true");
//
//        // Get all the welcomeMessageList where websiteUrl is null
//        defaultWelcomeMessageShouldNotBeFound("websiteUrl.specified=false");
//    }
//
//    @Test
//    @Transactional
//    public void getAllWelcomeMessagesByLogoUrlIsEqualToSomething() throws Exception {
//        // Initialize the database
//        welcomeMessageRepository.saveAndFlush(welcomeMessage);
//
//        // Get all the welcomeMessageList where logoUrl equals to DEFAULT_LOGO_URL
//        defaultWelcomeMessageShouldBeFound("logoUrl.equals=" + DEFAULT_LOGO_URL);
//
//        // Get all the welcomeMessageList where logoUrl equals to UPDATED_LOGO_URL
//        defaultWelcomeMessageShouldNotBeFound("logoUrl.equals=" + UPDATED_LOGO_URL);
//    }
//
//    @Test
//    @Transactional
//    public void getAllWelcomeMessagesByLogoUrlIsInShouldWork() throws Exception {
//        // Initialize the database
//        welcomeMessageRepository.saveAndFlush(welcomeMessage);
//
//        // Get all the welcomeMessageList where logoUrl in DEFAULT_LOGO_URL or UPDATED_LOGO_URL
//        defaultWelcomeMessageShouldBeFound("logoUrl.in=" + DEFAULT_LOGO_URL + "," + UPDATED_LOGO_URL);
//
//        // Get all the welcomeMessageList where logoUrl equals to UPDATED_LOGO_URL
//        defaultWelcomeMessageShouldNotBeFound("logoUrl.in=" + UPDATED_LOGO_URL);
//    }
//
//    @Test
//    @Transactional
//    public void getAllWelcomeMessagesByLogoUrlIsNullOrNotNull() throws Exception {
//        // Initialize the database
//        welcomeMessageRepository.saveAndFlush(welcomeMessage);
//
//        // Get all the welcomeMessageList where logoUrl is not null
//        defaultWelcomeMessageShouldBeFound("logoUrl.specified=true");
//
//        // Get all the welcomeMessageList where logoUrl is null
//        defaultWelcomeMessageShouldNotBeFound("logoUrl.specified=false");
//    }
//
//    @Test
//    @Transactional
//    public void getAllWelcomeMessagesByActiveIsEqualToSomething() throws Exception {
//        // Initialize the database
//        welcomeMessageRepository.saveAndFlush(welcomeMessage);
//
//        // Get all the welcomeMessageList where active equals to DEFAULT_ACTIVE
//        defaultWelcomeMessageShouldBeFound("active.equals=" + DEFAULT_ACTIVE);
//
//        // Get all the welcomeMessageList where active equals to UPDATED_ACTIVE
//        defaultWelcomeMessageShouldNotBeFound("active.equals=" + UPDATED_ACTIVE);
//    }
//
//    @Test
//    @Transactional
//    public void getAllWelcomeMessagesByActiveIsInShouldWork() throws Exception {
//        // Initialize the database
//        welcomeMessageRepository.saveAndFlush(welcomeMessage);
//
//        // Get all the welcomeMessageList where active in DEFAULT_ACTIVE or UPDATED_ACTIVE
//        defaultWelcomeMessageShouldBeFound("active.in=" + DEFAULT_ACTIVE + "," + UPDATED_ACTIVE);
//
//        // Get all the welcomeMessageList where active equals to UPDATED_ACTIVE
//        defaultWelcomeMessageShouldNotBeFound("active.in=" + UPDATED_ACTIVE);
//    }
//
//    @Test
//    @Transactional
//    public void getAllWelcomeMessagesByActiveIsNullOrNotNull() throws Exception {
//        // Initialize the database
//        welcomeMessageRepository.saveAndFlush(welcomeMessage);
//
//        // Get all the welcomeMessageList where active is not null
//        defaultWelcomeMessageShouldBeFound("active.specified=true");
//
//        // Get all the welcomeMessageList where active is null
//        defaultWelcomeMessageShouldNotBeFound("active.specified=false");
//    }
//
//    @Test
//    @Transactional
//    public void getAllWelcomeMessagesByGuildsettingsIsEqualToSomething() throws Exception {
//        // Initialize the database
//        GuildSettings guildsettings = GuildSettingsResourceIntTest.createEntity(em);
//        em.persist(guildsettings);
//        em.flush();
//        welcomeMessage.setGuildsettings(guildsettings);
//        welcomeMessageRepository.saveAndFlush(welcomeMessage);
//        Long guildsettingsId = guildsettings.getId();
//
//        // Get all the welcomeMessageList where guildsettings equals to guildsettingsId
//        defaultWelcomeMessageShouldBeFound("guildsettingsId.equals=" + guildsettingsId);
//
//        // Get all the welcomeMessageList where guildsettings equals to guildsettingsId + 1
//        defaultWelcomeMessageShouldNotBeFound("guildsettingsId.equals=" + (guildsettingsId + 1));
//    }
//
//    /**
//     * Executes the search, and checks that the default entity is returned
//     */
//    private void defaultWelcomeMessageShouldBeFound(String filter) throws Exception {
//        restWelcomeMessageMockMvc.perform(get("/api/welcome-messages?sort=id,desc&" + filter))
//            .andExpect(status().isOk())
//            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
//            .andExpect(jsonPath("$.[*].id").value(hasItem(welcomeMessage.getId().intValue())))
//            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
//            .andExpect(jsonPath("$.[*].messageTitle").value(hasItem(DEFAULT_MESSAGE_TITLE)))
//            .andExpect(jsonPath("$.[*].body").value(hasItem(DEFAULT_BODY.toString())))
//            .andExpect(jsonPath("$.[*].footer").value(hasItem(DEFAULT_FOOTER)))
//            .andExpect(jsonPath("$.[*].websiteUrl").value(hasItem(DEFAULT_WEBSITE_URL)))
//            .andExpect(jsonPath("$.[*].logoUrl").value(hasItem(DEFAULT_LOGO_URL)))
//            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())));
//
//        // Check, that the count call also returns 1
//        restWelcomeMessageMockMvc.perform(get("/api/welcome-messages/count?sort=id,desc&" + filter))
//            .andExpect(status().isOk())
//            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
//            .andExpect(content().string("1"));
//    }
//
//    /**
//     * Executes the search, and checks that the default entity is not returned
//     */
//    private void defaultWelcomeMessageShouldNotBeFound(String filter) throws Exception {
//        restWelcomeMessageMockMvc.perform(get("/api/welcome-messages?sort=id,desc&" + filter))
//            .andExpect(status().isOk())
//            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
//            .andExpect(jsonPath("$").isArray())
//            .andExpect(jsonPath("$").isEmpty());
//
//        // Check, that the count call also returns 0
//        restWelcomeMessageMockMvc.perform(get("/api/welcome-messages/count?sort=id,desc&" + filter))
//            .andExpect(status().isOk())
//            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
//            .andExpect(content().string("0"));
//    }
//
//
//    @Test
//    @Transactional
//    public void getNonExistingWelcomeMessage() throws Exception {
//        // Get the welcomeMessage
//        restWelcomeMessageMockMvc.perform(get("/api/welcome-messages/{id}", Long.MAX_VALUE))
//            .andExpect(status().isNotFound());
//    }
//
//    @Test
//    @Transactional
//    public void updateWelcomeMessage() throws Exception {
//        // Initialize the database
//        welcomeMessageService.save(welcomeMessage);
//
//        int databaseSizeBeforeUpdate = welcomeMessageRepository.findAll().size();
//
//        // Update the welcomeMessage
//        WelcomeMessage updatedWelcomeMessage = welcomeMessageRepository.findById(welcomeMessage.getId()).get();
//        // Disconnect from session so that the updates on updatedWelcomeMessage are not directly saved in db
//        em.detach(updatedWelcomeMessage);
//        updatedWelcomeMessage
//            .name(UPDATED_NAME)
//            .messageTitle(UPDATED_MESSAGE_TITLE)
//            .body(UPDATED_BODY)
//            .footer(UPDATED_FOOTER)
//            .websiteUrl(UPDATED_WEBSITE_URL)
//            .logoUrl(UPDATED_LOGO_URL)
//            .active(UPDATED_ACTIVE);
//
//        restWelcomeMessageMockMvc.perform(put("/api/welcome-messages")
//            .contentType(TestUtil.APPLICATION_JSON_UTF8)
//            .content(TestUtil.convertObjectToJsonBytes(updatedWelcomeMessage)))
//            .andExpect(status().isOk());
//
//        // Validate the WelcomeMessage in the database
//        List<WelcomeMessage> welcomeMessageList = welcomeMessageRepository.findAll();
//        assertThat(welcomeMessageList).hasSize(databaseSizeBeforeUpdate);
//        WelcomeMessage testWelcomeMessage = welcomeMessageList.get(welcomeMessageList.size() - 1);
//        assertThat(testWelcomeMessage.getName()).isEqualTo(UPDATED_NAME);
//        assertThat(testWelcomeMessage.getMessageTitle()).isEqualTo(UPDATED_MESSAGE_TITLE);
//        assertThat(testWelcomeMessage.getBody()).isEqualTo(UPDATED_BODY);
//        assertThat(testWelcomeMessage.getFooter()).isEqualTo(UPDATED_FOOTER);
//        assertThat(testWelcomeMessage.getWebsiteUrl()).isEqualTo(UPDATED_WEBSITE_URL);
//        assertThat(testWelcomeMessage.getLogoUrl()).isEqualTo(UPDATED_LOGO_URL);
//        assertThat(testWelcomeMessage.isActive()).isEqualTo(UPDATED_ACTIVE);
//    }
//
//    @Test
//    @Transactional
//    public void updateNonExistingWelcomeMessage() throws Exception {
//        int databaseSizeBeforeUpdate = welcomeMessageRepository.findAll().size();
//
//        // Create the WelcomeMessage
//
//        // If the entity doesn't have an ID, it will throw BadRequestAlertException
//        restWelcomeMessageMockMvc.perform(put("/api/welcome-messages")
//            .contentType(TestUtil.APPLICATION_JSON_UTF8)
//            .content(TestUtil.convertObjectToJsonBytes(welcomeMessage)))
//            .andExpect(status().isBadRequest());
//
//        // Validate the WelcomeMessage in the database
//        List<WelcomeMessage> welcomeMessageList = welcomeMessageRepository.findAll();
//        assertThat(welcomeMessageList).hasSize(databaseSizeBeforeUpdate);
//    }
//
//    @Test
//    @Transactional
//    public void deleteWelcomeMessage() throws Exception {
//        // Initialize the database
//        welcomeMessageService.save(welcomeMessage);
//
//        int databaseSizeBeforeDelete = welcomeMessageRepository.findAll().size();
//
//        // Delete the welcomeMessage
//        restWelcomeMessageMockMvc.perform(delete("/api/welcome-messages/{id}", welcomeMessage.getId())
//            .accept(TestUtil.APPLICATION_JSON_UTF8))
//            .andExpect(status().isOk());
//
//        // Validate the database is empty
//        List<WelcomeMessage> welcomeMessageList = welcomeMessageRepository.findAll();
//        assertThat(welcomeMessageList).hasSize(databaseSizeBeforeDelete - 1);
//    }
//
//    @Test
//    @Transactional
//    public void equalsVerifier() throws Exception {
//        TestUtil.equalsVerifier(WelcomeMessage.class);
//        WelcomeMessage welcomeMessage1 = new WelcomeMessage();
//        welcomeMessage1.setId(1L);
//        WelcomeMessage welcomeMessage2 = new WelcomeMessage();
//        welcomeMessage2.setId(welcomeMessage1.getId());
//        assertThat(welcomeMessage1).isEqualTo(welcomeMessage2);
//        welcomeMessage2.setId(2L);
//        assertThat(welcomeMessage1).isNotEqualTo(welcomeMessage2);
//        welcomeMessage1.setId(null);
//        assertThat(welcomeMessage1).isNotEqualTo(welcomeMessage2);
//    }
//}
