//package com.trievosoftware.application.web.rest;
//
//import com.trievosoftware.application.SiaApp;
//
//import com.trievosoftware.application.domain.GuildEvent;
//import com.trievosoftware.application.repository.GuildEventRepository;
//import com.trievosoftware.application.service.GuildEventService;
//import com.trievosoftware.application.web.rest.errors.ExceptionTranslator;
//import com.trievosoftware.application.service.dto.GuildEventCriteria;
//import com.trievosoftware.application.service.GuildEventQueryService;
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
//import org.springframework.util.Base64Utils;
//import org.springframework.validation.Validator;
//
//import javax.persistence.EntityManager;
//import java.time.Instant;
//import java.time.temporal.ChronoUnit;
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
// * Test class for the GuildEventResource REST controller.
// *
// * @see GuildEventResource
// */
//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = SiaApp.class)
//public class GuildEventResourceIntTest {
//
//    private static final String DEFAULT_EVENT_NAME = "AAAAAAAAAA";
//    private static final String UPDATED_EVENT_NAME = "BBBBBBBBBB";
//
//    private static final String DEFAULT_EVENT_IMAGE_URL = "AAAAAAAAAA";
//    private static final String UPDATED_EVENT_IMAGE_URL = "BBBBBBBBBB";
//
//    private static final String DEFAULT_EVENT_MESSAGE = "AAAAAAAAAA";
//    private static final String UPDATED_EVENT_MESSAGE = "BBBBBBBBBB";
//
//    private static final Instant DEFAULT_EVENT_START = Instant.ofEpochMilli(0L);
//    private static final Instant UPDATED_EVENT_START = Instant.now().truncatedTo(ChronoUnit.MILLIS);
//
//    @Autowired
//    private GuildEventRepository guildEventRepository;
//
//    @Autowired
//    private GuildEventService guildEventService;
//
//    @Autowired
//    private GuildEventQueryService guildEventQueryService;
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
//    private MockMvc restGuildEventMockMvc;
//
//    private GuildEvent guildEvent;
//
//    @Before
//    public void setup() {
//        MockitoAnnotations.initMocks(this);
//        final GuildEventResource guildEventResource = new GuildEventResource(guildEventService, guildEventQueryService);
//        this.restGuildEventMockMvc = MockMvcBuilders.standaloneSetup(guildEventResource)
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
//    public static GuildEvent createEntity(EntityManager em) {
//        GuildEvent guildEvent = new GuildEvent()
//            .eventName(DEFAULT_EVENT_NAME)
//            .eventImageUrl(DEFAULT_EVENT_IMAGE_URL)
//            .eventMessage(DEFAULT_EVENT_MESSAGE)
//            .eventStart(DEFAULT_EVENT_START);
//        return guildEvent;
//    }
//
//    @Before
//    public void initTest() {
//        guildEvent = createEntity(em);
//    }
//
//    @Test
//    @Transactional
//    public void createGuildEvent() throws Exception {
//        int databaseSizeBeforeCreate = guildEventRepository.findAll().size();
//
//        // Create the GuildEvent
//        restGuildEventMockMvc.perform(post("/api/guild-events")
//            .contentType(TestUtil.APPLICATION_JSON_UTF8)
//            .content(TestUtil.convertObjectToJsonBytes(guildEvent)))
//            .andExpect(status().isCreated());
//
//        // Validate the GuildEvent in the database
//        List<GuildEvent> guildEventList = guildEventRepository.findAll();
//        assertThat(guildEventList).hasSize(databaseSizeBeforeCreate + 1);
//        GuildEvent testGuildEvent = guildEventList.get(guildEventList.size() - 1);
//        assertThat(testGuildEvent.getEventName()).isEqualTo(DEFAULT_EVENT_NAME);
//        assertThat(testGuildEvent.getEventImageUrl()).isEqualTo(DEFAULT_EVENT_IMAGE_URL);
//        assertThat(testGuildEvent.getEventMessage()).isEqualTo(DEFAULT_EVENT_MESSAGE);
//        assertThat(testGuildEvent.getEventStart()).isEqualTo(DEFAULT_EVENT_START);
//    }
//
//    @Test
//    @Transactional
//    public void createGuildEventWithExistingId() throws Exception {
//        int databaseSizeBeforeCreate = guildEventRepository.findAll().size();
//
//        // Create the GuildEvent with an existing ID
//        guildEvent.setId(1L);
//
//        // An entity with an existing ID cannot be created, so this API call must fail
//        restGuildEventMockMvc.perform(post("/api/guild-events")
//            .contentType(TestUtil.APPLICATION_JSON_UTF8)
//            .content(TestUtil.convertObjectToJsonBytes(guildEvent)))
//            .andExpect(status().isBadRequest());
//
//        // Validate the GuildEvent in the database
//        List<GuildEvent> guildEventList = guildEventRepository.findAll();
//        assertThat(guildEventList).hasSize(databaseSizeBeforeCreate);
//    }
//
//    @Test
//    @Transactional
//    public void checkEventNameIsRequired() throws Exception {
//        int databaseSizeBeforeTest = guildEventRepository.findAll().size();
//        // set the field null
//        guildEvent.setEventName(null);
//
//        // Create the GuildEvent, which fails.
//
//        restGuildEventMockMvc.perform(post("/api/guild-events")
//            .contentType(TestUtil.APPLICATION_JSON_UTF8)
//            .content(TestUtil.convertObjectToJsonBytes(guildEvent)))
//            .andExpect(status().isBadRequest());
//
//        List<GuildEvent> guildEventList = guildEventRepository.findAll();
//        assertThat(guildEventList).hasSize(databaseSizeBeforeTest);
//    }
//
//    @Test
//    @Transactional
//    public void checkEventImageUrlIsRequired() throws Exception {
//        int databaseSizeBeforeTest = guildEventRepository.findAll().size();
//        // set the field null
//        guildEvent.setEventImageUrl(null);
//
//        // Create the GuildEvent, which fails.
//
//        restGuildEventMockMvc.perform(post("/api/guild-events")
//            .contentType(TestUtil.APPLICATION_JSON_UTF8)
//            .content(TestUtil.convertObjectToJsonBytes(guildEvent)))
//            .andExpect(status().isBadRequest());
//
//        List<GuildEvent> guildEventList = guildEventRepository.findAll();
//        assertThat(guildEventList).hasSize(databaseSizeBeforeTest);
//    }
//
//    @Test
//    @Transactional
//    public void checkEventStartIsRequired() throws Exception {
//        int databaseSizeBeforeTest = guildEventRepository.findAll().size();
//        // set the field null
//        guildEvent.setEventStart(null);
//
//        // Create the GuildEvent, which fails.
//
//        restGuildEventMockMvc.perform(post("/api/guild-events")
//            .contentType(TestUtil.APPLICATION_JSON_UTF8)
//            .content(TestUtil.convertObjectToJsonBytes(guildEvent)))
//            .andExpect(status().isBadRequest());
//
//        List<GuildEvent> guildEventList = guildEventRepository.findAll();
//        assertThat(guildEventList).hasSize(databaseSizeBeforeTest);
//    }
//
//    @Test
//    @Transactional
//    public void getAllGuildEvents() throws Exception {
//        // Initialize the database
//        guildEventRepository.saveAndFlush(guildEvent);
//
//        // Get all the guildEventList
//        restGuildEventMockMvc.perform(get("/api/guild-events?sort=id,desc"))
//            .andExpect(status().isOk())
//            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
//            .andExpect(jsonPath("$.[*].id").value(hasItem(guildEvent.getId().intValue())))
//            .andExpect(jsonPath("$.[*].eventName").value(hasItem(DEFAULT_EVENT_NAME.toString())))
//            .andExpect(jsonPath("$.[*].eventImageUrl").value(hasItem(DEFAULT_EVENT_IMAGE_URL.toString())))
//            .andExpect(jsonPath("$.[*].eventMessage").value(hasItem(DEFAULT_EVENT_MESSAGE.toString())))
//            .andExpect(jsonPath("$.[*].eventStart").value(hasItem(DEFAULT_EVENT_START.toString())));
//    }
//
//    @Test
//    @Transactional
//    public void getGuildEvent() throws Exception {
//        // Initialize the database
//        guildEventRepository.saveAndFlush(guildEvent);
//
//        // Get the guildEvent
//        restGuildEventMockMvc.perform(get("/api/guild-events/{id}", guildEvent.getId()))
//            .andExpect(status().isOk())
//            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
//            .andExpect(jsonPath("$.id").value(guildEvent.getId().intValue()))
//            .andExpect(jsonPath("$.eventName").value(DEFAULT_EVENT_NAME.toString()))
//            .andExpect(jsonPath("$.eventImageUrl").value(DEFAULT_EVENT_IMAGE_URL.toString()))
//            .andExpect(jsonPath("$.eventMessage").value(DEFAULT_EVENT_MESSAGE.toString()))
//            .andExpect(jsonPath("$.eventStart").value(DEFAULT_EVENT_START.toString()));
//    }
//
//    @Test
//    @Transactional
//    public void getAllGuildEventsByEventNameIsEqualToSomething() throws Exception {
//        // Initialize the database
//        guildEventRepository.saveAndFlush(guildEvent);
//
//        // Get all the guildEventList where eventName equals to DEFAULT_EVENT_NAME
//        defaultGuildEventShouldBeFound("eventName.equals=" + DEFAULT_EVENT_NAME);
//
//        // Get all the guildEventList where eventName equals to UPDATED_EVENT_NAME
//        defaultGuildEventShouldNotBeFound("eventName.equals=" + UPDATED_EVENT_NAME);
//    }
//
//    @Test
//    @Transactional
//    public void getAllGuildEventsByEventNameIsInShouldWork() throws Exception {
//        // Initialize the database
//        guildEventRepository.saveAndFlush(guildEvent);
//
//        // Get all the guildEventList where eventName in DEFAULT_EVENT_NAME or UPDATED_EVENT_NAME
//        defaultGuildEventShouldBeFound("eventName.in=" + DEFAULT_EVENT_NAME + "," + UPDATED_EVENT_NAME);
//
//        // Get all the guildEventList where eventName equals to UPDATED_EVENT_NAME
//        defaultGuildEventShouldNotBeFound("eventName.in=" + UPDATED_EVENT_NAME);
//    }
//
//    @Test
//    @Transactional
//    public void getAllGuildEventsByEventNameIsNullOrNotNull() throws Exception {
//        // Initialize the database
//        guildEventRepository.saveAndFlush(guildEvent);
//
//        // Get all the guildEventList where eventName is not null
//        defaultGuildEventShouldBeFound("eventName.specified=true");
//
//        // Get all the guildEventList where eventName is null
//        defaultGuildEventShouldNotBeFound("eventName.specified=false");
//    }
//
//    @Test
//    @Transactional
//    public void getAllGuildEventsByEventImageUrlIsEqualToSomething() throws Exception {
//        // Initialize the database
//        guildEventRepository.saveAndFlush(guildEvent);
//
//        // Get all the guildEventList where eventImageUrl equals to DEFAULT_EVENT_IMAGE_URL
//        defaultGuildEventShouldBeFound("eventImageUrl.equals=" + DEFAULT_EVENT_IMAGE_URL);
//
//        // Get all the guildEventList where eventImageUrl equals to UPDATED_EVENT_IMAGE_URL
//        defaultGuildEventShouldNotBeFound("eventImageUrl.equals=" + UPDATED_EVENT_IMAGE_URL);
//    }
//
//    @Test
//    @Transactional
//    public void getAllGuildEventsByEventImageUrlIsInShouldWork() throws Exception {
//        // Initialize the database
//        guildEventRepository.saveAndFlush(guildEvent);
//
//        // Get all the guildEventList where eventImageUrl in DEFAULT_EVENT_IMAGE_URL or UPDATED_EVENT_IMAGE_URL
//        defaultGuildEventShouldBeFound("eventImageUrl.in=" + DEFAULT_EVENT_IMAGE_URL + "," + UPDATED_EVENT_IMAGE_URL);
//
//        // Get all the guildEventList where eventImageUrl equals to UPDATED_EVENT_IMAGE_URL
//        defaultGuildEventShouldNotBeFound("eventImageUrl.in=" + UPDATED_EVENT_IMAGE_URL);
//    }
//
//    @Test
//    @Transactional
//    public void getAllGuildEventsByEventImageUrlIsNullOrNotNull() throws Exception {
//        // Initialize the database
//        guildEventRepository.saveAndFlush(guildEvent);
//
//        // Get all the guildEventList where eventImageUrl is not null
//        defaultGuildEventShouldBeFound("eventImageUrl.specified=true");
//
//        // Get all the guildEventList where eventImageUrl is null
//        defaultGuildEventShouldNotBeFound("eventImageUrl.specified=false");
//    }
//
//    @Test
//    @Transactional
//    public void getAllGuildEventsByEventStartIsEqualToSomething() throws Exception {
//        // Initialize the database
//        guildEventRepository.saveAndFlush(guildEvent);
//
//        // Get all the guildEventList where eventStart equals to DEFAULT_EVENT_START
//        defaultGuildEventShouldBeFound("eventStart.equals=" + DEFAULT_EVENT_START);
//
//        // Get all the guildEventList where eventStart equals to UPDATED_EVENT_START
//        defaultGuildEventShouldNotBeFound("eventStart.equals=" + UPDATED_EVENT_START);
//    }
//
//    @Test
//    @Transactional
//    public void getAllGuildEventsByEventStartIsInShouldWork() throws Exception {
//        // Initialize the database
//        guildEventRepository.saveAndFlush(guildEvent);
//
//        // Get all the guildEventList where eventStart in DEFAULT_EVENT_START or UPDATED_EVENT_START
//        defaultGuildEventShouldBeFound("eventStart.in=" + DEFAULT_EVENT_START + "," + UPDATED_EVENT_START);
//
//        // Get all the guildEventList where eventStart equals to UPDATED_EVENT_START
//        defaultGuildEventShouldNotBeFound("eventStart.in=" + UPDATED_EVENT_START);
//    }
//
//    @Test
//    @Transactional
//    public void getAllGuildEventsByEventStartIsNullOrNotNull() throws Exception {
//        // Initialize the database
//        guildEventRepository.saveAndFlush(guildEvent);
//
//        // Get all the guildEventList where eventStart is not null
//        defaultGuildEventShouldBeFound("eventStart.specified=true");
//
//        // Get all the guildEventList where eventStart is null
//        defaultGuildEventShouldNotBeFound("eventStart.specified=false");
//    }
//    /**
//     * Executes the search, and checks that the default entity is returned
//     */
//    private void defaultGuildEventShouldBeFound(String filter) throws Exception {
//        restGuildEventMockMvc.perform(get("/api/guild-events?sort=id,desc&" + filter))
//            .andExpect(status().isOk())
//            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
//            .andExpect(jsonPath("$.[*].id").value(hasItem(guildEvent.getId().intValue())))
//            .andExpect(jsonPath("$.[*].eventName").value(hasItem(DEFAULT_EVENT_NAME)))
//            .andExpect(jsonPath("$.[*].eventImageUrl").value(hasItem(DEFAULT_EVENT_IMAGE_URL)))
//            .andExpect(jsonPath("$.[*].eventMessage").value(hasItem(DEFAULT_EVENT_MESSAGE.toString())))
//            .andExpect(jsonPath("$.[*].eventStart").value(hasItem(DEFAULT_EVENT_START.toString())));
//
//        // Check, that the count call also returns 1
//        restGuildEventMockMvc.perform(get("/api/guild-events/count?sort=id,desc&" + filter))
//            .andExpect(status().isOk())
//            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
//            .andExpect(content().string("1"));
//    }
//
//    /**
//     * Executes the search, and checks that the default entity is not returned
//     */
//    private void defaultGuildEventShouldNotBeFound(String filter) throws Exception {
//        restGuildEventMockMvc.perform(get("/api/guild-events?sort=id,desc&" + filter))
//            .andExpect(status().isOk())
//            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
//            .andExpect(jsonPath("$").isArray())
//            .andExpect(jsonPath("$").isEmpty());
//
//        // Check, that the count call also returns 0
//        restGuildEventMockMvc.perform(get("/api/guild-events/count?sort=id,desc&" + filter))
//            .andExpect(status().isOk())
//            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
//            .andExpect(content().string("0"));
//    }
//
//
//    @Test
//    @Transactional
//    public void getNonExistingGuildEvent() throws Exception {
//        // Get the guildEvent
//        restGuildEventMockMvc.perform(get("/api/guild-events/{id}", Long.MAX_VALUE))
//            .andExpect(status().isNotFound());
//    }
//
//    @Test
//    @Transactional
//    public void updateGuildEvent() throws Exception {
//        // Initialize the database
//        guildEventService.save(guildEvent);
//
//        int databaseSizeBeforeUpdate = guildEventRepository.findAll().size();
//
//        // Update the guildEvent
//        GuildEvent updatedGuildEvent = guildEventRepository.findById(guildEvent.getId()).get();
//        // Disconnect from session so that the updates on updatedGuildEvent are not directly saved in db
//        em.detach(updatedGuildEvent);
//        updatedGuildEvent
//            .eventName(UPDATED_EVENT_NAME)
//            .eventImageUrl(UPDATED_EVENT_IMAGE_URL)
//            .eventMessage(UPDATED_EVENT_MESSAGE)
//            .eventStart(UPDATED_EVENT_START);
//
//        restGuildEventMockMvc.perform(put("/api/guild-events")
//            .contentType(TestUtil.APPLICATION_JSON_UTF8)
//            .content(TestUtil.convertObjectToJsonBytes(updatedGuildEvent)))
//            .andExpect(status().isOk());
//
//        // Validate the GuildEvent in the database
//        List<GuildEvent> guildEventList = guildEventRepository.findAll();
//        assertThat(guildEventList).hasSize(databaseSizeBeforeUpdate);
//        GuildEvent testGuildEvent = guildEventList.get(guildEventList.size() - 1);
//        assertThat(testGuildEvent.getEventName()).isEqualTo(UPDATED_EVENT_NAME);
//        assertThat(testGuildEvent.getEventImageUrl()).isEqualTo(UPDATED_EVENT_IMAGE_URL);
//        assertThat(testGuildEvent.getEventMessage()).isEqualTo(UPDATED_EVENT_MESSAGE);
//        assertThat(testGuildEvent.getEventStart()).isEqualTo(UPDATED_EVENT_START);
//    }
//
//    @Test
//    @Transactional
//    public void updateNonExistingGuildEvent() throws Exception {
//        int databaseSizeBeforeUpdate = guildEventRepository.findAll().size();
//
//        // Create the GuildEvent
//
//        // If the entity doesn't have an ID, it will throw BadRequestAlertException
//        restGuildEventMockMvc.perform(put("/api/guild-events")
//            .contentType(TestUtil.APPLICATION_JSON_UTF8)
//            .content(TestUtil.convertObjectToJsonBytes(guildEvent)))
//            .andExpect(status().isBadRequest());
//
//        // Validate the GuildEvent in the database
//        List<GuildEvent> guildEventList = guildEventRepository.findAll();
//        assertThat(guildEventList).hasSize(databaseSizeBeforeUpdate);
//    }
//
//    @Test
//    @Transactional
//    public void deleteGuildEvent() throws Exception {
//        // Initialize the database
//        guildEventService.save(guildEvent);
//
//        int databaseSizeBeforeDelete = guildEventRepository.findAll().size();
//
//        // Delete the guildEvent
//        restGuildEventMockMvc.perform(delete("/api/guild-events/{id}", guildEvent.getId())
//            .accept(TestUtil.APPLICATION_JSON_UTF8))
//            .andExpect(status().isOk());
//
//        // Validate the database is empty
//        List<GuildEvent> guildEventList = guildEventRepository.findAll();
//        assertThat(guildEventList).hasSize(databaseSizeBeforeDelete - 1);
//    }
//
//    @Test
//    @Transactional
//    public void equalsVerifier() throws Exception {
//        TestUtil.equalsVerifier(GuildEvent.class);
//        GuildEvent guildEvent1 = new GuildEvent();
//        guildEvent1.setId(1L);
//        GuildEvent guildEvent2 = new GuildEvent();
//        guildEvent2.setId(guildEvent1.getId());
//        assertThat(guildEvent1).isEqualTo(guildEvent2);
//        guildEvent2.setId(2L);
//        assertThat(guildEvent1).isNotEqualTo(guildEvent2);
//        guildEvent1.setId(null);
//        assertThat(guildEvent1).isNotEqualTo(guildEvent2);
//    }
//}
