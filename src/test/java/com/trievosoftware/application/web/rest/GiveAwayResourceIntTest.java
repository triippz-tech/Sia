//package com.trievosoftware.application.web.rest;
//
//import com.trievosoftware.application.SiaApp;
//import com.trievosoftware.application.domain.DiscordUser;
//import com.trievosoftware.application.domain.GiveAway;
//import com.trievosoftware.application.domain.GuildSettings;
//import com.trievosoftware.application.repository.GiveAwayRepository;
//import com.trievosoftware.application.service.GiveAwayQueryService;
//import com.trievosoftware.application.service.GiveAwayService;
//import com.trievosoftware.application.web.rest.errors.ExceptionTranslator;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.data.domain.PageImpl;
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
//import java.time.Instant;
//import java.time.temporal.ChronoUnit;
//import java.util.ArrayList;
//import java.util.List;
//
//import static com.trievosoftware.application.web.rest.TestUtil.createFormattingConversionService;
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.hamcrest.Matchers.hasItem;
//import static org.mockito.Mockito.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
///**
// * Test class for the GiveAwayResource REST controller.
// *
// * @see GiveAwayResource
// */
//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = SiaApp.class)
//public class GiveAwayResourceIntTest {
//
//    private static final String DEFAULT_NAME = "AAAAAAAAAA";
//    private static final String UPDATED_NAME = "BBBBBBBBBB";
//
//    private static final String DEFAULT_MESSAGE = "AAAAAAAAAA";
//    private static final String UPDATED_MESSAGE = "BBBBBBBBBB";
//
//    private static final Long DEFAULT_MESSAGE_ID = 1L;
//    private static final Long UPDATED_MESSAGE_ID = 2L;
//
//    private static final Long DEFAULT_TEXT_CHANNEL_ID = 1L;
//    private static final Long UPDATED_TEXT_CHANNEL_ID = 2L;
//
//    private static final Instant DEFAULT_FINISH = Instant.ofEpochMilli(0L);
//    private static final Instant UPDATED_FINISH = Instant.now().truncatedTo(ChronoUnit.MILLIS);
//
//    private static final Long DEFAULT_WINNER = 1L;
//    private static final Long UPDATED_WINNER = 2L;
//
//    private static final Boolean DEFAULT_EXPIRED = false;
//    private static final Boolean UPDATED_EXPIRED = true;
//
//    @Autowired
//    private GiveAwayRepository giveAwayRepository;
//
//    @Mock
//    private GiveAwayRepository giveAwayRepositoryMock;
//
//    @Mock
//    private GiveAwayService giveAwayServiceMock;
//
//    @Autowired
//    private GiveAwayService giveAwayService;
//
//    @Autowired
//    private GiveAwayQueryService giveAwayQueryService;
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
//    private MockMvc restGiveAwayMockMvc;
//
//    private GiveAway giveAway;
//
//    @Before
//    public void setup() {
//        MockitoAnnotations.initMocks(this);
//        final GiveAwayResource giveAwayResource = new GiveAwayResource(giveAwayService, giveAwayQueryService);
//        this.restGiveAwayMockMvc = MockMvcBuilders.standaloneSetup(giveAwayResource)
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
//    public static GiveAway createEntity(EntityManager em) {
//        GiveAway giveAway = new GiveAway()
//            .name(DEFAULT_NAME)
//            .message(DEFAULT_MESSAGE)
//            .messageId(DEFAULT_MESSAGE_ID)
//            .textChannelId(DEFAULT_TEXT_CHANNEL_ID)
//            .finish(DEFAULT_FINISH)
//            .winner(DEFAULT_WINNER)
//            .expired(DEFAULT_EXPIRED);
//        return giveAway;
//    }
//
//    @Before
//    public void initTest() {
//        giveAway = createEntity(em);
//    }
//
//    @Test
//    @Transactional
//    public void createGiveAway() throws Exception {
//        int databaseSizeBeforeCreate = giveAwayRepository.findAll().size();
//
//        // Create the GiveAway
//        restGiveAwayMockMvc.perform(post("/api/give-aways")
//            .contentType(TestUtil.APPLICATION_JSON_UTF8)
//            .content(TestUtil.convertObjectToJsonBytes(giveAway)))
//            .andExpect(status().isCreated());
//
//        // Validate the GiveAway in the database
//        List<GiveAway> giveAwayList = giveAwayRepository.findAll();
//        assertThat(giveAwayList).hasSize(databaseSizeBeforeCreate + 1);
//        GiveAway testGiveAway = giveAwayList.get(giveAwayList.size() - 1);
//        assertThat(testGiveAway.getName()).isEqualTo(DEFAULT_NAME);
//        assertThat(testGiveAway.getMessage()).isEqualTo(DEFAULT_MESSAGE);
//        assertThat(testGiveAway.getMessageId()).isEqualTo(DEFAULT_MESSAGE_ID);
//        assertThat(testGiveAway.getTextChannelId()).isEqualTo(DEFAULT_TEXT_CHANNEL_ID);
//        assertThat(testGiveAway.getFinish()).isEqualTo(DEFAULT_FINISH);
//        assertThat(testGiveAway.getWinner()).isEqualTo(DEFAULT_WINNER);
//        assertThat(testGiveAway.isExpired()).isEqualTo(DEFAULT_EXPIRED);
//    }
//
//    @Test
//    @Transactional
//    public void createGiveAwayWithExistingId() throws Exception {
//        int databaseSizeBeforeCreate = giveAwayRepository.findAll().size();
//
//        // Create the GiveAway with an existing ID
//        giveAway.setId(1L);
//
//        // An entity with an existing ID cannot be created, so this API call must fail
//        restGiveAwayMockMvc.perform(post("/api/give-aways")
//            .contentType(TestUtil.APPLICATION_JSON_UTF8)
//            .content(TestUtil.convertObjectToJsonBytes(giveAway)))
//            .andExpect(status().isBadRequest());
//
//        // Validate the GiveAway in the database
//        List<GiveAway> giveAwayList = giveAwayRepository.findAll();
//        assertThat(giveAwayList).hasSize(databaseSizeBeforeCreate);
//    }
//
//    @Test
//    @Transactional
//    public void checkNameIsRequired() throws Exception {
//        int databaseSizeBeforeTest = giveAwayRepository.findAll().size();
//        // set the field null
//        giveAway.setName(null);
//
//        // Create the GiveAway, which fails.
//
//        restGiveAwayMockMvc.perform(post("/api/give-aways")
//            .contentType(TestUtil.APPLICATION_JSON_UTF8)
//            .content(TestUtil.convertObjectToJsonBytes(giveAway)))
//            .andExpect(status().isBadRequest());
//
//        List<GiveAway> giveAwayList = giveAwayRepository.findAll();
//        assertThat(giveAwayList).hasSize(databaseSizeBeforeTest);
//    }
//
//    @Test
//    @Transactional
//    public void checkMessageIdIsRequired() throws Exception {
//        int databaseSizeBeforeTest = giveAwayRepository.findAll().size();
//        // set the field null
//        giveAway.setMessageId(null);
//
//        // Create the GiveAway, which fails.
//
//        restGiveAwayMockMvc.perform(post("/api/give-aways")
//            .contentType(TestUtil.APPLICATION_JSON_UTF8)
//            .content(TestUtil.convertObjectToJsonBytes(giveAway)))
//            .andExpect(status().isBadRequest());
//
//        List<GiveAway> giveAwayList = giveAwayRepository.findAll();
//        assertThat(giveAwayList).hasSize(databaseSizeBeforeTest);
//    }
//
//    @Test
//    @Transactional
//    public void checkTextChannelIdIsRequired() throws Exception {
//        int databaseSizeBeforeTest = giveAwayRepository.findAll().size();
//        // set the field null
//        giveAway.setTextChannelId(null);
//
//        // Create the GiveAway, which fails.
//
//        restGiveAwayMockMvc.perform(post("/api/give-aways")
//            .contentType(TestUtil.APPLICATION_JSON_UTF8)
//            .content(TestUtil.convertObjectToJsonBytes(giveAway)))
//            .andExpect(status().isBadRequest());
//
//        List<GiveAway> giveAwayList = giveAwayRepository.findAll();
//        assertThat(giveAwayList).hasSize(databaseSizeBeforeTest);
//    }
//
//    @Test
//    @Transactional
//    public void checkFinishIsRequired() throws Exception {
//        int databaseSizeBeforeTest = giveAwayRepository.findAll().size();
//        // set the field null
//        giveAway.setFinish(null);
//
//        // Create the GiveAway, which fails.
//
//        restGiveAwayMockMvc.perform(post("/api/give-aways")
//            .contentType(TestUtil.APPLICATION_JSON_UTF8)
//            .content(TestUtil.convertObjectToJsonBytes(giveAway)))
//            .andExpect(status().isBadRequest());
//
//        List<GiveAway> giveAwayList = giveAwayRepository.findAll();
//        assertThat(giveAwayList).hasSize(databaseSizeBeforeTest);
//    }
//
//    @Test
//    @Transactional
//    public void checkWinnerIsRequired() throws Exception {
//        int databaseSizeBeforeTest = giveAwayRepository.findAll().size();
//        // set the field null
//        giveAway.setWinner(null);
//
//        // Create the GiveAway, which fails.
//
//        restGiveAwayMockMvc.perform(post("/api/give-aways")
//            .contentType(TestUtil.APPLICATION_JSON_UTF8)
//            .content(TestUtil.convertObjectToJsonBytes(giveAway)))
//            .andExpect(status().isBadRequest());
//
//        List<GiveAway> giveAwayList = giveAwayRepository.findAll();
//        assertThat(giveAwayList).hasSize(databaseSizeBeforeTest);
//    }
//
//    @Test
//    @Transactional
//    public void checkExpiredIsRequired() throws Exception {
//        int databaseSizeBeforeTest = giveAwayRepository.findAll().size();
//        // set the field null
//        giveAway.setExpired(null);
//
//        // Create the GiveAway, which fails.
//
//        restGiveAwayMockMvc.perform(post("/api/give-aways")
//            .contentType(TestUtil.APPLICATION_JSON_UTF8)
//            .content(TestUtil.convertObjectToJsonBytes(giveAway)))
//            .andExpect(status().isBadRequest());
//
//        List<GiveAway> giveAwayList = giveAwayRepository.findAll();
//        assertThat(giveAwayList).hasSize(databaseSizeBeforeTest);
//    }
//
//    @Test
//    @Transactional
//    public void getAllGiveAways() throws Exception {
//        // Initialize the database
//        giveAwayRepository.saveAndFlush(giveAway);
//
//        // Get all the giveAwayList
//        restGiveAwayMockMvc.perform(get("/api/give-aways?sort=id,desc"))
//            .andExpect(status().isOk())
//            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
//            .andExpect(jsonPath("$.[*].id").value(hasItem(giveAway.getId().intValue())))
//            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
//            .andExpect(jsonPath("$.[*].message").value(hasItem(DEFAULT_MESSAGE.toString())))
//            .andExpect(jsonPath("$.[*].messageId").value(hasItem(DEFAULT_MESSAGE_ID.intValue())))
//            .andExpect(jsonPath("$.[*].textChannelId").value(hasItem(DEFAULT_TEXT_CHANNEL_ID.intValue())))
//            .andExpect(jsonPath("$.[*].finish").value(hasItem(DEFAULT_FINISH.toString())))
//            .andExpect(jsonPath("$.[*].winner").value(hasItem(DEFAULT_WINNER.intValue())))
//            .andExpect(jsonPath("$.[*].expired").value(hasItem(DEFAULT_EXPIRED.booleanValue())));
//    }
//
//    @SuppressWarnings({"unchecked"})
//    public void getAllGiveAwaysWithEagerRelationshipsIsEnabled() throws Exception {
//        GiveAwayResource giveAwayResource = new GiveAwayResource(giveAwayServiceMock, giveAwayQueryService);
//        when(giveAwayServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));
//
//        MockMvc restGiveAwayMockMvc = MockMvcBuilders.standaloneSetup(giveAwayResource)
//            .setCustomArgumentResolvers(pageableArgumentResolver)
//            .setControllerAdvice(exceptionTranslator)
//            .setConversionService(createFormattingConversionService())
//            .setMessageConverters(jacksonMessageConverter).build();
//
//        restGiveAwayMockMvc.perform(get("/api/give-aways?eagerload=true"))
//        .andExpect(status().isOk());
//
//        verify(giveAwayServiceMock, times(1)).findAllWithEagerRelationships(any());
//    }
//
//    @SuppressWarnings({"unchecked"})
//    public void getAllGiveAwaysWithEagerRelationshipsIsNotEnabled() throws Exception {
//        GiveAwayResource giveAwayResource = new GiveAwayResource(giveAwayServiceMock, giveAwayQueryService);
//            when(giveAwayServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));
//            MockMvc restGiveAwayMockMvc = MockMvcBuilders.standaloneSetup(giveAwayResource)
//            .setCustomArgumentResolvers(pageableArgumentResolver)
//            .setControllerAdvice(exceptionTranslator)
//            .setConversionService(createFormattingConversionService())
//            .setMessageConverters(jacksonMessageConverter).build();
//
//        restGiveAwayMockMvc.perform(get("/api/give-aways?eagerload=true"))
//        .andExpect(status().isOk());
//
//            verify(giveAwayServiceMock, times(1)).findAllWithEagerRelationships(any());
//    }
//
//    @Test
//    @Transactional
//    public void getGiveAway() throws Exception {
//        // Initialize the database
//        giveAwayRepository.saveAndFlush(giveAway);
//
//        // Get the giveAway
//        restGiveAwayMockMvc.perform(get("/api/give-aways/{id}", giveAway.getId()))
//            .andExpect(status().isOk())
//            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
//            .andExpect(jsonPath("$.id").value(giveAway.getId().intValue()))
//            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
//            .andExpect(jsonPath("$.message").value(DEFAULT_MESSAGE.toString()))
//            .andExpect(jsonPath("$.messageId").value(DEFAULT_MESSAGE_ID.intValue()))
//            .andExpect(jsonPath("$.textChannelId").value(DEFAULT_TEXT_CHANNEL_ID.intValue()))
//            .andExpect(jsonPath("$.finish").value(DEFAULT_FINISH.toString()))
//            .andExpect(jsonPath("$.winner").value(DEFAULT_WINNER.intValue()))
//            .andExpect(jsonPath("$.expired").value(DEFAULT_EXPIRED.booleanValue()));
//    }
//
//    @Test
//    @Transactional
//    public void getAllGiveAwaysByNameIsEqualToSomething() throws Exception {
//        // Initialize the database
//        giveAwayRepository.saveAndFlush(giveAway);
//
//        // Get all the giveAwayList where name equals to DEFAULT_NAME
//        defaultGiveAwayShouldBeFound("name.equals=" + DEFAULT_NAME);
//
//        // Get all the giveAwayList where name equals to UPDATED_NAME
//        defaultGiveAwayShouldNotBeFound("name.equals=" + UPDATED_NAME);
//    }
//
//    @Test
//    @Transactional
//    public void getAllGiveAwaysByNameIsInShouldWork() throws Exception {
//        // Initialize the database
//        giveAwayRepository.saveAndFlush(giveAway);
//
//        // Get all the giveAwayList where name in DEFAULT_NAME or UPDATED_NAME
//        defaultGiveAwayShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);
//
//        // Get all the giveAwayList where name equals to UPDATED_NAME
//        defaultGiveAwayShouldNotBeFound("name.in=" + UPDATED_NAME);
//    }
//
//    @Test
//    @Transactional
//    public void getAllGiveAwaysByNameIsNullOrNotNull() throws Exception {
//        // Initialize the database
//        giveAwayRepository.saveAndFlush(giveAway);
//
//        // Get all the giveAwayList where name is not null
//        defaultGiveAwayShouldBeFound("name.specified=true");
//
//        // Get all the giveAwayList where name is null
//        defaultGiveAwayShouldNotBeFound("name.specified=false");
//    }
//
//    @Test
//    @Transactional
//    public void getAllGiveAwaysByMessageIdIsEqualToSomething() throws Exception {
//        // Initialize the database
//        giveAwayRepository.saveAndFlush(giveAway);
//
//        // Get all the giveAwayList where messageId equals to DEFAULT_MESSAGE_ID
//        defaultGiveAwayShouldBeFound("messageId.equals=" + DEFAULT_MESSAGE_ID);
//
//        // Get all the giveAwayList where messageId equals to UPDATED_MESSAGE_ID
//        defaultGiveAwayShouldNotBeFound("messageId.equals=" + UPDATED_MESSAGE_ID);
//    }
//
//    @Test
//    @Transactional
//    public void getAllGiveAwaysByMessageIdIsInShouldWork() throws Exception {
//        // Initialize the database
//        giveAwayRepository.saveAndFlush(giveAway);
//
//        // Get all the giveAwayList where messageId in DEFAULT_MESSAGE_ID or UPDATED_MESSAGE_ID
//        defaultGiveAwayShouldBeFound("messageId.in=" + DEFAULT_MESSAGE_ID + "," + UPDATED_MESSAGE_ID);
//
//        // Get all the giveAwayList where messageId equals to UPDATED_MESSAGE_ID
//        defaultGiveAwayShouldNotBeFound("messageId.in=" + UPDATED_MESSAGE_ID);
//    }
//
//    @Test
//    @Transactional
//    public void getAllGiveAwaysByMessageIdIsNullOrNotNull() throws Exception {
//        // Initialize the database
//        giveAwayRepository.saveAndFlush(giveAway);
//
//        // Get all the giveAwayList where messageId is not null
//        defaultGiveAwayShouldBeFound("messageId.specified=true");
//
//        // Get all the giveAwayList where messageId is null
//        defaultGiveAwayShouldNotBeFound("messageId.specified=false");
//    }
//
//    @Test
//    @Transactional
//    public void getAllGiveAwaysByMessageIdIsGreaterThanOrEqualToSomething() throws Exception {
//        // Initialize the database
//        giveAwayRepository.saveAndFlush(giveAway);
//
//        // Get all the giveAwayList where messageId greater than or equals to DEFAULT_MESSAGE_ID
//        defaultGiveAwayShouldBeFound("messageId.greaterOrEqualThan=" + DEFAULT_MESSAGE_ID);
//
//        // Get all the giveAwayList where messageId greater than or equals to UPDATED_MESSAGE_ID
//        defaultGiveAwayShouldNotBeFound("messageId.greaterOrEqualThan=" + UPDATED_MESSAGE_ID);
//    }
//
//    @Test
//    @Transactional
//    public void getAllGiveAwaysByMessageIdIsLessThanSomething() throws Exception {
//        // Initialize the database
//        giveAwayRepository.saveAndFlush(giveAway);
//
//        // Get all the giveAwayList where messageId less than or equals to DEFAULT_MESSAGE_ID
//        defaultGiveAwayShouldNotBeFound("messageId.lessThan=" + DEFAULT_MESSAGE_ID);
//
//        // Get all the giveAwayList where messageId less than or equals to UPDATED_MESSAGE_ID
//        defaultGiveAwayShouldBeFound("messageId.lessThan=" + UPDATED_MESSAGE_ID);
//    }
//
//
//    @Test
//    @Transactional
//    public void getAllGiveAwaysByTextChannelIdIsEqualToSomething() throws Exception {
//        // Initialize the database
//        giveAwayRepository.saveAndFlush(giveAway);
//
//        // Get all the giveAwayList where textChannelId equals to DEFAULT_TEXT_CHANNEL_ID
//        defaultGiveAwayShouldBeFound("textChannelId.equals=" + DEFAULT_TEXT_CHANNEL_ID);
//
//        // Get all the giveAwayList where textChannelId equals to UPDATED_TEXT_CHANNEL_ID
//        defaultGiveAwayShouldNotBeFound("textChannelId.equals=" + UPDATED_TEXT_CHANNEL_ID);
//    }
//
//    @Test
//    @Transactional
//    public void getAllGiveAwaysByTextChannelIdIsInShouldWork() throws Exception {
//        // Initialize the database
//        giveAwayRepository.saveAndFlush(giveAway);
//
//        // Get all the giveAwayList where textChannelId in DEFAULT_TEXT_CHANNEL_ID or UPDATED_TEXT_CHANNEL_ID
//        defaultGiveAwayShouldBeFound("textChannelId.in=" + DEFAULT_TEXT_CHANNEL_ID + "," + UPDATED_TEXT_CHANNEL_ID);
//
//        // Get all the giveAwayList where textChannelId equals to UPDATED_TEXT_CHANNEL_ID
//        defaultGiveAwayShouldNotBeFound("textChannelId.in=" + UPDATED_TEXT_CHANNEL_ID);
//    }
//
//    @Test
//    @Transactional
//    public void getAllGiveAwaysByTextChannelIdIsNullOrNotNull() throws Exception {
//        // Initialize the database
//        giveAwayRepository.saveAndFlush(giveAway);
//
//        // Get all the giveAwayList where textChannelId is not null
//        defaultGiveAwayShouldBeFound("textChannelId.specified=true");
//
//        // Get all the giveAwayList where textChannelId is null
//        defaultGiveAwayShouldNotBeFound("textChannelId.specified=false");
//    }
//
//    @Test
//    @Transactional
//    public void getAllGiveAwaysByTextChannelIdIsGreaterThanOrEqualToSomething() throws Exception {
//        // Initialize the database
//        giveAwayRepository.saveAndFlush(giveAway);
//
//        // Get all the giveAwayList where textChannelId greater than or equals to DEFAULT_TEXT_CHANNEL_ID
//        defaultGiveAwayShouldBeFound("textChannelId.greaterOrEqualThan=" + DEFAULT_TEXT_CHANNEL_ID);
//
//        // Get all the giveAwayList where textChannelId greater than or equals to UPDATED_TEXT_CHANNEL_ID
//        defaultGiveAwayShouldNotBeFound("textChannelId.greaterOrEqualThan=" + UPDATED_TEXT_CHANNEL_ID);
//    }
//
//    @Test
//    @Transactional
//    public void getAllGiveAwaysByTextChannelIdIsLessThanSomething() throws Exception {
//        // Initialize the database
//        giveAwayRepository.saveAndFlush(giveAway);
//
//        // Get all the giveAwayList where textChannelId less than or equals to DEFAULT_TEXT_CHANNEL_ID
//        defaultGiveAwayShouldNotBeFound("textChannelId.lessThan=" + DEFAULT_TEXT_CHANNEL_ID);
//
//        // Get all the giveAwayList where textChannelId less than or equals to UPDATED_TEXT_CHANNEL_ID
//        defaultGiveAwayShouldBeFound("textChannelId.lessThan=" + UPDATED_TEXT_CHANNEL_ID);
//    }
//
//
//    @Test
//    @Transactional
//    public void getAllGiveAwaysByFinishIsEqualToSomething() throws Exception {
//        // Initialize the database
//        giveAwayRepository.saveAndFlush(giveAway);
//
//        // Get all the giveAwayList where finish equals to DEFAULT_FINISH
//        defaultGiveAwayShouldBeFound("finish.equals=" + DEFAULT_FINISH);
//
//        // Get all the giveAwayList where finish equals to UPDATED_FINISH
//        defaultGiveAwayShouldNotBeFound("finish.equals=" + UPDATED_FINISH);
//    }
//
//    @Test
//    @Transactional
//    public void getAllGiveAwaysByFinishIsInShouldWork() throws Exception {
//        // Initialize the database
//        giveAwayRepository.saveAndFlush(giveAway);
//
//        // Get all the giveAwayList where finish in DEFAULT_FINISH or UPDATED_FINISH
//        defaultGiveAwayShouldBeFound("finish.in=" + DEFAULT_FINISH + "," + UPDATED_FINISH);
//
//        // Get all the giveAwayList where finish equals to UPDATED_FINISH
//        defaultGiveAwayShouldNotBeFound("finish.in=" + UPDATED_FINISH);
//    }
//
//    @Test
//    @Transactional
//    public void getAllGiveAwaysByFinishIsNullOrNotNull() throws Exception {
//        // Initialize the database
//        giveAwayRepository.saveAndFlush(giveAway);
//
//        // Get all the giveAwayList where finish is not null
//        defaultGiveAwayShouldBeFound("finish.specified=true");
//
//        // Get all the giveAwayList where finish is null
//        defaultGiveAwayShouldNotBeFound("finish.specified=false");
//    }
//
//    @Test
//    @Transactional
//    public void getAllGiveAwaysByWinnerIsEqualToSomething() throws Exception {
//        // Initialize the database
//        giveAwayRepository.saveAndFlush(giveAway);
//
//        // Get all the giveAwayList where winner equals to DEFAULT_WINNER
//        defaultGiveAwayShouldBeFound("winner.equals=" + DEFAULT_WINNER);
//
//        // Get all the giveAwayList where winner equals to UPDATED_WINNER
//        defaultGiveAwayShouldNotBeFound("winner.equals=" + UPDATED_WINNER);
//    }
//
//    @Test
//    @Transactional
//    public void getAllGiveAwaysByWinnerIsInShouldWork() throws Exception {
//        // Initialize the database
//        giveAwayRepository.saveAndFlush(giveAway);
//
//        // Get all the giveAwayList where winner in DEFAULT_WINNER or UPDATED_WINNER
//        defaultGiveAwayShouldBeFound("winner.in=" + DEFAULT_WINNER + "," + UPDATED_WINNER);
//
//        // Get all the giveAwayList where winner equals to UPDATED_WINNER
//        defaultGiveAwayShouldNotBeFound("winner.in=" + UPDATED_WINNER);
//    }
//
//    @Test
//    @Transactional
//    public void getAllGiveAwaysByWinnerIsNullOrNotNull() throws Exception {
//        // Initialize the database
//        giveAwayRepository.saveAndFlush(giveAway);
//
//        // Get all the giveAwayList where winner is not null
//        defaultGiveAwayShouldBeFound("winner.specified=true");
//
//        // Get all the giveAwayList where winner is null
//        defaultGiveAwayShouldNotBeFound("winner.specified=false");
//    }
//
//    @Test
//    @Transactional
//    public void getAllGiveAwaysByWinnerIsGreaterThanOrEqualToSomething() throws Exception {
//        // Initialize the database
//        giveAwayRepository.saveAndFlush(giveAway);
//
//        // Get all the giveAwayList where winner greater than or equals to DEFAULT_WINNER
//        defaultGiveAwayShouldBeFound("winner.greaterOrEqualThan=" + DEFAULT_WINNER);
//
//        // Get all the giveAwayList where winner greater than or equals to UPDATED_WINNER
//        defaultGiveAwayShouldNotBeFound("winner.greaterOrEqualThan=" + UPDATED_WINNER);
//    }
//
//    @Test
//    @Transactional
//    public void getAllGiveAwaysByWinnerIsLessThanSomething() throws Exception {
//        // Initialize the database
//        giveAwayRepository.saveAndFlush(giveAway);
//
//        // Get all the giveAwayList where winner less than or equals to DEFAULT_WINNER
//        defaultGiveAwayShouldNotBeFound("winner.lessThan=" + DEFAULT_WINNER);
//
//        // Get all the giveAwayList where winner less than or equals to UPDATED_WINNER
//        defaultGiveAwayShouldBeFound("winner.lessThan=" + UPDATED_WINNER);
//    }
//
//
//    @Test
//    @Transactional
//    public void getAllGiveAwaysByExpiredIsEqualToSomething() throws Exception {
//        // Initialize the database
//        giveAwayRepository.saveAndFlush(giveAway);
//
//        // Get all the giveAwayList where expired equals to DEFAULT_EXPIRED
//        defaultGiveAwayShouldBeFound("expired.equals=" + DEFAULT_EXPIRED);
//
//        // Get all the giveAwayList where expired equals to UPDATED_EXPIRED
//        defaultGiveAwayShouldNotBeFound("expired.equals=" + UPDATED_EXPIRED);
//    }
//
//    @Test
//    @Transactional
//    public void getAllGiveAwaysByExpiredIsInShouldWork() throws Exception {
//        // Initialize the database
//        giveAwayRepository.saveAndFlush(giveAway);
//
//        // Get all the giveAwayList where expired in DEFAULT_EXPIRED or UPDATED_EXPIRED
//        defaultGiveAwayShouldBeFound("expired.in=" + DEFAULT_EXPIRED + "," + UPDATED_EXPIRED);
//
//        // Get all the giveAwayList where expired equals to UPDATED_EXPIRED
//        defaultGiveAwayShouldNotBeFound("expired.in=" + UPDATED_EXPIRED);
//    }
//
//    @Test
//    @Transactional
//    public void getAllGiveAwaysByExpiredIsNullOrNotNull() throws Exception {
//        // Initialize the database
//        giveAwayRepository.saveAndFlush(giveAway);
//
//        // Get all the giveAwayList where expired is not null
//        defaultGiveAwayShouldBeFound("expired.specified=true");
//
//        // Get all the giveAwayList where expired is null
//        defaultGiveAwayShouldNotBeFound("expired.specified=false");
//    }
//
//    @Test
//    @Transactional
//    public void getAllGiveAwaysByDiscordUserIsEqualToSomething() throws Exception {
//        // Initialize the database
//        DiscordUser discordUser = DiscordUserResourceIntTest.createEntity(em);
//        em.persist(discordUser);
//        em.flush();
//        giveAway.addDiscordUser(discordUser);
//        giveAwayRepository.saveAndFlush(giveAway);
//        Long discordUserId = discordUser.getId();
//
//        // Get all the giveAwayList where discordUser equals to discordUserId
//        defaultGiveAwayShouldBeFound("discordUserId.equals=" + discordUserId);
//
//        // Get all the giveAwayList where discordUser equals to discordUserId + 1
//        defaultGiveAwayShouldNotBeFound("discordUserId.equals=" + (discordUserId + 1));
//    }
//
//
//    @Test
//    @Transactional
//    public void getAllGiveAwaysByGuildsettingsIsEqualToSomething() throws Exception {
//        // Initialize the database
//        GuildSettings guildsettings = GuildSettingsResourceIntTest.createEntity(em);
//        em.persist(guildsettings);
//        em.flush();
//        giveAway.setGuildsettings(guildsettings);
//        giveAwayRepository.saveAndFlush(giveAway);
//        Long guildsettingsId = guildsettings.getId();
//
//        // Get all the giveAwayList where guildsettings equals to guildsettingsId
//        defaultGiveAwayShouldBeFound("guildsettingsId.equals=" + guildsettingsId);
//
//        // Get all the giveAwayList where guildsettings equals to guildsettingsId + 1
//        defaultGiveAwayShouldNotBeFound("guildsettingsId.equals=" + (guildsettingsId + 1));
//    }
//
//    /**
//     * Executes the search, and checks that the default entity is returned
//     */
//    private void defaultGiveAwayShouldBeFound(String filter) throws Exception {
//        restGiveAwayMockMvc.perform(get("/api/give-aways?sort=id,desc&" + filter))
//            .andExpect(status().isOk())
//            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
//            .andExpect(jsonPath("$.[*].id").value(hasItem(giveAway.getId().intValue())))
//            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
//            .andExpect(jsonPath("$.[*].message").value(hasItem(DEFAULT_MESSAGE.toString())))
//            .andExpect(jsonPath("$.[*].messageId").value(hasItem(DEFAULT_MESSAGE_ID.intValue())))
//            .andExpect(jsonPath("$.[*].textChannelId").value(hasItem(DEFAULT_TEXT_CHANNEL_ID.intValue())))
//            .andExpect(jsonPath("$.[*].finish").value(hasItem(DEFAULT_FINISH.toString())))
//            .andExpect(jsonPath("$.[*].winner").value(hasItem(DEFAULT_WINNER.intValue())))
//            .andExpect(jsonPath("$.[*].expired").value(hasItem(DEFAULT_EXPIRED.booleanValue())));
//
//        // Check, that the count call also returns 1
//        restGiveAwayMockMvc.perform(get("/api/give-aways/count?sort=id,desc&" + filter))
//            .andExpect(status().isOk())
//            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
//            .andExpect(content().string("1"));
//    }
//
//    /**
//     * Executes the search, and checks that the default entity is not returned
//     */
//    private void defaultGiveAwayShouldNotBeFound(String filter) throws Exception {
//        restGiveAwayMockMvc.perform(get("/api/give-aways?sort=id,desc&" + filter))
//            .andExpect(status().isOk())
//            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
//            .andExpect(jsonPath("$").isArray())
//            .andExpect(jsonPath("$").isEmpty());
//
//        // Check, that the count call also returns 0
//        restGiveAwayMockMvc.perform(get("/api/give-aways/count?sort=id,desc&" + filter))
//            .andExpect(status().isOk())
//            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
//            .andExpect(content().string("0"));
//    }
//
//
//    @Test
//    @Transactional
//    public void getNonExistingGiveAway() throws Exception {
//        // Get the giveAway
//        restGiveAwayMockMvc.perform(get("/api/give-aways/{id}", Long.MAX_VALUE))
//            .andExpect(status().isNotFound());
//    }
//
//    @Test
//    @Transactional
//    public void updateGiveAway() throws Exception {
//        // Initialize the database
//        giveAwayService.save(giveAway);
//
//        int databaseSizeBeforeUpdate = giveAwayRepository.findAll().size();
//
//        // Update the giveAway
//        GiveAway updatedGiveAway = giveAwayRepository.findById(giveAway.getId()).get();
//        // Disconnect from session so that the updates on updatedGiveAway are not directly saved in db
//        em.detach(updatedGiveAway);
//        updatedGiveAway
//            .name(UPDATED_NAME)
//            .message(UPDATED_MESSAGE)
//            .messageId(UPDATED_MESSAGE_ID)
//            .textChannelId(UPDATED_TEXT_CHANNEL_ID)
//            .finish(UPDATED_FINISH)
//            .winner(UPDATED_WINNER)
//            .expired(UPDATED_EXPIRED);
//
//        restGiveAwayMockMvc.perform(put("/api/give-aways")
//            .contentType(TestUtil.APPLICATION_JSON_UTF8)
//            .content(TestUtil.convertObjectToJsonBytes(updatedGiveAway)))
//            .andExpect(status().isOk());
//
//        // Validate the GiveAway in the database
//        List<GiveAway> giveAwayList = giveAwayRepository.findAll();
//        assertThat(giveAwayList).hasSize(databaseSizeBeforeUpdate);
//        GiveAway testGiveAway = giveAwayList.get(giveAwayList.size() - 1);
//        assertThat(testGiveAway.getName()).isEqualTo(UPDATED_NAME);
//        assertThat(testGiveAway.getMessage()).isEqualTo(UPDATED_MESSAGE);
//        assertThat(testGiveAway.getMessageId()).isEqualTo(UPDATED_MESSAGE_ID);
//        assertThat(testGiveAway.getTextChannelId()).isEqualTo(UPDATED_TEXT_CHANNEL_ID);
//        assertThat(testGiveAway.getFinish()).isEqualTo(UPDATED_FINISH);
//        assertThat(testGiveAway.getWinner()).isEqualTo(UPDATED_WINNER);
//        assertThat(testGiveAway.isExpired()).isEqualTo(UPDATED_EXPIRED);
//    }
//
//    @Test
//    @Transactional
//    public void updateNonExistingGiveAway() throws Exception {
//        int databaseSizeBeforeUpdate = giveAwayRepository.findAll().size();
//
//        // Create the GiveAway
//
//        // If the entity doesn't have an ID, it will throw BadRequestAlertException
//        restGiveAwayMockMvc.perform(put("/api/give-aways")
//            .contentType(TestUtil.APPLICATION_JSON_UTF8)
//            .content(TestUtil.convertObjectToJsonBytes(giveAway)))
//            .andExpect(status().isBadRequest());
//
//        // Validate the GiveAway in the database
//        List<GiveAway> giveAwayList = giveAwayRepository.findAll();
//        assertThat(giveAwayList).hasSize(databaseSizeBeforeUpdate);
//    }
//
//    @Test
//    @Transactional
//    public void deleteGiveAway() throws Exception {
//        // Initialize the database
//        giveAwayService.save(giveAway);
//
//        int databaseSizeBeforeDelete = giveAwayRepository.findAll().size();
//
//        // Delete the giveAway
//        restGiveAwayMockMvc.perform(delete("/api/give-aways/{id}", giveAway.getId())
//            .accept(TestUtil.APPLICATION_JSON_UTF8))
//            .andExpect(status().isOk());
//
//        // Validate the database is empty
//        List<GiveAway> giveAwayList = giveAwayRepository.findAll();
//        assertThat(giveAwayList).hasSize(databaseSizeBeforeDelete - 1);
//    }
//
//    @Test
//    @Transactional
//    public void equalsVerifier() throws Exception {
//        TestUtil.equalsVerifier(GiveAway.class);
//        GiveAway giveAway1 = new GiveAway();
//        giveAway1.setId(1L);
//        GiveAway giveAway2 = new GiveAway();
//        giveAway2.setId(giveAway1.getId());
//        assertThat(giveAway1).isEqualTo(giveAway2);
//        giveAway2.setId(2L);
//        assertThat(giveAway1).isNotEqualTo(giveAway2);
//        giveAway1.setId(null);
//        assertThat(giveAway1).isNotEqualTo(giveAway2);
//    }
//}
