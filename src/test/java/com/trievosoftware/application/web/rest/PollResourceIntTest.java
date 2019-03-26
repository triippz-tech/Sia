package com.trievosoftware.application.web.rest;

import com.trievosoftware.application.SiaApp;

import com.trievosoftware.application.domain.Poll;
import com.trievosoftware.application.repository.PollRepository;
import com.trievosoftware.application.service.PollService;
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
 * Test class for the PollResource REST controller.
 *
 * @see PollResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SiaApp.class)
public class PollResourceIntTest {

    private static final Long DEFAULT_GUILD_ID = 1L;
    private static final Long UPDATED_GUILD_ID = 2L;

    private static final Long DEFAULT_USER_ID = 1L;
    private static final Long UPDATED_USER_ID = 2L;

    private static final Long DEFAULT_TEXT_CHANNEL_ID = 1L;
    private static final Long UPDATED_TEXT_CHANNEL_ID = 2L;

    private static final Long DEFAULT_MESSAGE_ID = 1L;
    private static final Long UPDATED_MESSAGE_ID = 2L;

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final Instant DEFAULT_FINISH_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FINISH_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Boolean DEFAULT_EXPIRED = false;
    private static final Boolean UPDATED_EXPIRED = true;

    @Autowired
    private PollRepository pollRepository;

    @Autowired
    private PollService pollService;

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

    private MockMvc restPollMockMvc;

    private Poll poll;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PollResource pollResource = new PollResource(pollService);
        this.restPollMockMvc = MockMvcBuilders.standaloneSetup(pollResource)
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
    public static Poll createEntity(EntityManager em) {
        Poll poll = new Poll()
            .guildId(DEFAULT_GUILD_ID)
            .userId(DEFAULT_USER_ID)
            .textChannelId(DEFAULT_TEXT_CHANNEL_ID)
            .title(DEFAULT_TITLE)
            .messageId(DEFAULT_MESSAGE_ID)
            .expired(DEFAULT_EXPIRED)
            .finishTime(DEFAULT_FINISH_TIME);
        return poll;
    }

    @Before
    public void initTest() {
        poll = createEntity(em);
    }

    @Test
    @Transactional
    public void createPoll() throws Exception {
        int databaseSizeBeforeCreate = pollRepository.findAll().size();

        // Create the Poll
        restPollMockMvc.perform(post("/api/polls")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(poll)))
            .andExpect(status().isCreated());

        // Validate the Poll in the database
        List<Poll> pollList = pollRepository.findAll();
        assertThat(pollList).hasSize(databaseSizeBeforeCreate + 1);
        Poll testPoll = pollList.get(pollList.size() - 1);
        assertThat(testPoll.getGuildId()).isEqualTo(DEFAULT_GUILD_ID);
        assertThat(testPoll.getUserId()).isEqualTo(DEFAULT_USER_ID);
        assertThat(testPoll.getTextChannelId()).isEqualTo(DEFAULT_TEXT_CHANNEL_ID);
        assertThat(testPoll.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testPoll.getFinishTime()).isEqualTo(DEFAULT_FINISH_TIME);
        assertThat(testPoll.isExpired()).isEqualTo(DEFAULT_EXPIRED);
        assertThat(testPoll.getMessageId()).isEqualTo(DEFAULT_MESSAGE_ID);
    }

    @Test
    @Transactional
    public void createPollWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = pollRepository.findAll().size();

        // Create the Poll with an existing ID
        poll.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPollMockMvc.perform(post("/api/polls")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(poll)))
            .andExpect(status().isBadRequest());

        // Validate the Poll in the database
        List<Poll> pollList = pollRepository.findAll();
        assertThat(pollList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkGuildIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = pollRepository.findAll().size();
        // set the field null
        poll.setGuildId(null);

        // Create the Poll, which fails.

        restPollMockMvc.perform(post("/api/polls")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(poll)))
            .andExpect(status().isBadRequest());

        List<Poll> pollList = pollRepository.findAll();
        assertThat(pollList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkUserIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = pollRepository.findAll().size();
        // set the field null
        poll.setUserId(null);

        // Create the Poll, which fails.

        restPollMockMvc.perform(post("/api/polls")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(poll)))
            .andExpect(status().isBadRequest());

        List<Poll> pollList = pollRepository.findAll();
        assertThat(pollList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = pollRepository.findAll().size();
        // set the field null
        poll.setTitle(null);

        // Create the Poll, which fails.

        restPollMockMvc.perform(post("/api/polls")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(poll)))
            .andExpect(status().isBadRequest());

        List<Poll> pollList = pollRepository.findAll();
        assertThat(pollList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkFinishTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = pollRepository.findAll().size();
        // set the field null
        poll.setFinishTime(Instant.now());

        // Create the Poll, which fails.

        restPollMockMvc.perform(post("/api/polls")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(poll)))
            .andExpect(status().isCreated());

        List<Poll> pollList = pollRepository.findAll();
        assertThat(pollList).isNotSameAs(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPolls() throws Exception {
        // Initialize the database
        pollRepository.saveAndFlush(poll);

        // Get all the pollList
        restPollMockMvc.perform(get("/api/polls?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(poll.getId().intValue())))
            .andExpect(jsonPath("$.[*].guildId").value(hasItem(DEFAULT_GUILD_ID.intValue())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID.intValue())))
            .andExpect(jsonPath("$.[*].textChannelId").value(hasItem(DEFAULT_TEXT_CHANNEL_ID.intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
            .andExpect(jsonPath("$.[*].finishTime").value(hasItem(DEFAULT_FINISH_TIME.toString())))
            .andExpect(jsonPath("$.[*].expired").value(hasItem(DEFAULT_EXPIRED)))
            .andExpect(jsonPath("$.[*].messageId").value(hasItem(DEFAULT_MESSAGE_ID.intValue())));
    }
    
    @Test
    @Transactional
    public void getPoll() throws Exception {
        // Initialize the database
        pollRepository.saveAndFlush(poll);

        // Get the poll
        restPollMockMvc.perform(get("/api/polls/{id}", poll.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(poll.getId().intValue()))
            .andExpect(jsonPath("$.guildId").value(DEFAULT_GUILD_ID.intValue()))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID.intValue()))
            .andExpect(jsonPath("$.textChannelId").value(DEFAULT_TEXT_CHANNEL_ID.intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.finishTime").value(DEFAULT_FINISH_TIME.toString()))
            .andExpect(jsonPath("$.expired").value(DEFAULT_EXPIRED.booleanValue()))
            .andExpect(jsonPath("$.messageId").value(DEFAULT_MESSAGE_ID.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPoll() throws Exception {
        // Get the poll
        restPollMockMvc.perform(get("/api/polls/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePoll() throws Exception {
        // Initialize the database
        pollService.save(poll);

        int databaseSizeBeforeUpdate = pollRepository.findAll().size();

        // Update the poll
        Poll updatedPoll = pollRepository.findById(poll.getId()).get();
        // Disconnect from session so that the updates on updatedPoll are not directly saved in db
        em.detach(updatedPoll);
        updatedPoll
            .guildId(UPDATED_GUILD_ID)
            .userId(UPDATED_USER_ID)
            .textChannelId(UPDATED_TEXT_CHANNEL_ID)
            .title(UPDATED_TITLE)
            .expired(UPDATED_EXPIRED)
            .messageId(UPDATED_MESSAGE_ID)
            .finishTime(UPDATED_FINISH_TIME);

        restPollMockMvc.perform(put("/api/polls")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedPoll)))
            .andExpect(status().isOk());

        // Validate the Poll in the database
        List<Poll> pollList = pollRepository.findAll();
        assertThat(pollList).hasSize(databaseSizeBeforeUpdate);
        Poll testPoll = pollList.get(pollList.size() - 1);
        assertThat(testPoll.getGuildId()).isEqualTo(UPDATED_GUILD_ID);
        assertThat(testPoll.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testPoll.getTextChannelId()).isEqualTo(UPDATED_TEXT_CHANNEL_ID);
        assertThat(testPoll.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testPoll.getFinishTime()).isEqualTo(UPDATED_FINISH_TIME);
        assertThat(testPoll.isExpired()).isEqualTo(UPDATED_EXPIRED);
        assertThat(testPoll.getMessageId()).isEqualTo(UPDATED_MESSAGE_ID);
    }

    @Test
    @Transactional
    public void updateNonExistingPoll() throws Exception {
        int databaseSizeBeforeUpdate = pollRepository.findAll().size();

        // Create the Poll

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPollMockMvc.perform(put("/api/polls")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(poll)))
            .andExpect(status().isBadRequest());

        // Validate the Poll in the database
        List<Poll> pollList = pollRepository.findAll();
        assertThat(pollList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deletePoll() throws Exception {
        // Initialize the database
        pollService.save(poll);

        int databaseSizeBeforeDelete = pollRepository.findAll().size();

        // Delete the poll
        restPollMockMvc.perform(delete("/api/polls/{id}", poll.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Poll> pollList = pollRepository.findAll();
        assertThat(pollList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Poll.class);
        Poll poll1 = new Poll();
        poll1.setId(1L);
        Poll poll2 = new Poll();
        poll2.setId(poll1.getId());
        assertThat(poll1).isEqualTo(poll2);
        poll2.setId(2L);
        assertThat(poll1).isNotEqualTo(poll2);
        poll1.setId(null);
        assertThat(poll1).isNotEqualTo(poll2);
    }
}
