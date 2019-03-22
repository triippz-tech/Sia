package com.trievosoftware.application.web.rest;

import com.trievosoftware.application.SiaApp;

import com.trievosoftware.application.domain.PollItems;
import com.trievosoftware.application.repository.PollItemsRepository;
import com.trievosoftware.application.service.PollItemsService;
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
 * Test class for the PollItemsResource REST controller.
 *
 * @see PollItemsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SiaApp.class)
public class PollItemsResourceIntTest {

    private static final String DEFAULT_ITEM_NAME = "AAAAAAAAAA";
    private static final String UPDATED_ITEM_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_ITEM_NUMBER = 1;
    private static final Integer UPDATED_ITEM_NUMBER = 2;

    private static final String DEFAULT_REACTION = "AAAAAAAAAA";
    private static final String UPDATED_REACTION = "BBBBBBBBBB";

    @Autowired
    private PollItemsRepository pollItemsRepository;

    @Autowired
    private PollItemsService pollItemsService;

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

    private MockMvc restPollItemsMockMvc;

    private PollItems pollItems;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PollItemsResource pollItemsResource = new PollItemsResource(pollItemsService);
        this.restPollItemsMockMvc = MockMvcBuilders.standaloneSetup(pollItemsResource)
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
    public static PollItems createEntity(EntityManager em) {
        PollItems pollItems = new PollItems()
            .itemName(DEFAULT_ITEM_NAME)
            .votes(DEFAULT_ITEM_NUMBER)
            .reaction(DEFAULT_REACTION);
        return pollItems;
    }

    @Before
    public void initTest() {
        pollItems = createEntity(em);
    }

    @Test
    @Transactional
    public void createPollItems() throws Exception {
        int databaseSizeBeforeCreate = pollItemsRepository.findAll().size();

        // Create the PollItems
        restPollItemsMockMvc.perform(post("/api/poll-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pollItems)))
            .andExpect(status().isCreated());

        // Validate the PollItems in the database
        List<PollItems> pollItemsList = pollItemsRepository.findAll();
        assertThat(pollItemsList).hasSize(databaseSizeBeforeCreate + 1);
        PollItems testPollItems = pollItemsList.get(pollItemsList.size() - 1);
        assertThat(testPollItems.getItemName()).isEqualTo(DEFAULT_ITEM_NAME);
        assertThat(testPollItems.getVotes()).isEqualTo(DEFAULT_ITEM_NUMBER);
        assertThat(testPollItems.getReaction()).isEqualTo(DEFAULT_REACTION);
    }

    @Test
    @Transactional
    public void createPollItemsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = pollItemsRepository.findAll().size();

        // Create the PollItems with an existing ID
        pollItems.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPollItemsMockMvc.perform(post("/api/poll-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pollItems)))
            .andExpect(status().isBadRequest());

        // Validate the PollItems in the database
        List<PollItems> pollItemsList = pollItemsRepository.findAll();
        assertThat(pollItemsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkItemNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = pollItemsRepository.findAll().size();
        // set the field null
        pollItems.setItemName(null);

        // Create the PollItems, which fails.

        restPollItemsMockMvc.perform(post("/api/poll-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pollItems)))
            .andExpect(status().isBadRequest());

        List<PollItems> pollItemsList = pollItemsRepository.findAll();
        assertThat(pollItemsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPollItems() throws Exception {
        // Initialize the database
        pollItemsRepository.saveAndFlush(pollItems);

        // Get all the pollItemsList
        restPollItemsMockMvc.perform(get("/api/poll-items?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pollItems.getId().intValue())))
            .andExpect(jsonPath("$.[*].itemName").value(hasItem(DEFAULT_ITEM_NAME.toString())))
            .andExpect(jsonPath("$.[*].votes").value(hasItem(DEFAULT_ITEM_NUMBER)))
            .andExpect(jsonPath("$.[*].reaction").value(hasItem(DEFAULT_REACTION)));
    }
    
    @Test
    @Transactional
    public void getPollItems() throws Exception {
        // Initialize the database
        pollItemsRepository.saveAndFlush(pollItems);

        // Get the pollItems
        restPollItemsMockMvc.perform(get("/api/poll-items/{id}", pollItems.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(pollItems.getId().intValue()))
            .andExpect(jsonPath("$.itemName").value(DEFAULT_ITEM_NAME.toString()))
            .andExpect(jsonPath("$.votes").value(DEFAULT_ITEM_NUMBER))
            .andExpect(jsonPath("$.reaction").value(DEFAULT_REACTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPollItems() throws Exception {
        // Get the pollItems
        restPollItemsMockMvc.perform(get("/api/poll-items/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePollItems() throws Exception {
        // Initialize the database
        pollItemsService.save(pollItems);

        int databaseSizeBeforeUpdate = pollItemsRepository.findAll().size();

        // Update the pollItems
        PollItems updatedPollItems = pollItemsRepository.findById(pollItems.getId()).get();
        // Disconnect from session so that the updates on updatedPollItems are not directly saved in db
        em.detach(updatedPollItems);
        updatedPollItems
            .itemName(UPDATED_ITEM_NAME)
            .votes(UPDATED_ITEM_NUMBER)
            .reaction(UPDATED_REACTION);

        restPollItemsMockMvc.perform(put("/api/poll-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedPollItems)))
            .andExpect(status().isOk());

        // Validate the PollItems in the database
        List<PollItems> pollItemsList = pollItemsRepository.findAll();
        assertThat(pollItemsList).hasSize(databaseSizeBeforeUpdate);
        PollItems testPollItems = pollItemsList.get(pollItemsList.size() - 1);
        assertThat(testPollItems.getItemName()).isEqualTo(UPDATED_ITEM_NAME);
        assertThat(testPollItems.getVotes()).isEqualTo(UPDATED_ITEM_NUMBER);
        assertThat(testPollItems.getReaction()).isEqualTo(UPDATED_REACTION);
    }

    @Test
    @Transactional
    public void updateNonExistingPollItems() throws Exception {
        int databaseSizeBeforeUpdate = pollItemsRepository.findAll().size();

        // Create the PollItems

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPollItemsMockMvc.perform(put("/api/poll-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pollItems)))
            .andExpect(status().isBadRequest());

        // Validate the PollItems in the database
        List<PollItems> pollItemsList = pollItemsRepository.findAll();
        assertThat(pollItemsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deletePollItems() throws Exception {
        // Initialize the database
        pollItemsService.save(pollItems);

        int databaseSizeBeforeDelete = pollItemsRepository.findAll().size();

        // Delete the pollItems
        restPollItemsMockMvc.perform(delete("/api/poll-items/{id}", pollItems.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<PollItems> pollItemsList = pollItemsRepository.findAll();
        assertThat(pollItemsList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PollItems.class);
        PollItems pollItems1 = new PollItems();
        pollItems1.setId(1L);
        PollItems pollItems2 = new PollItems();
        pollItems2.setId(pollItems1.getId());
        assertThat(pollItems1).isEqualTo(pollItems2);
        pollItems2.setId(2L);
        assertThat(pollItems1).isNotEqualTo(pollItems2);
        pollItems1.setId(null);
        assertThat(pollItems1).isNotEqualTo(pollItems2);
    }
}
