//package com.trievosoftware.application.web.rest;
//
//import com.trievosoftware.application.SiaApp;
//
//import com.trievosoftware.application.domain.Actions;
//import com.trievosoftware.application.repository.ActionsRepository;
//import com.trievosoftware.application.service.ActionsService;
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
// * Test class for the ActionsResource REST controller.
// *
// * @see ActionsResource
// */
//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = SiaApp.class)
//public class ActionsResourceIntTest {
//
//    private static final Long DEFAULT_GUILD_ID = 1L;
//    private static final Long UPDATED_GUILD_ID = 2L;
//
//    private static final Integer DEFAULT_NUM_STRIKES = 1;
//    private static final Integer UPDATED_NUM_STRIKES = 2;
//
//    private static final Integer DEFAULT_ACTION = 1;
//    private static final Integer UPDATED_ACTION = 2;
//
//    private static final Integer DEFAULT_TIME = 1;
//    private static final Integer UPDATED_TIME = 2;
//
//    @Autowired
//    private ActionsRepository actionsRepository;
//
//    @Autowired
//    private ActionsService actionsService;
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
//    private MockMvc restActionsMockMvc;
//
//    private Actions actions;
//
//    @Before
//    public void setup() {
//        MockitoAnnotations.initMocks(this);
//        final ActionsResource actionsResource = new ActionsResource(actionsService);
//        this.restActionsMockMvc = MockMvcBuilders.standaloneSetup(actionsResource)
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
//    public static Actions createEntity(EntityManager em) {
//        Actions actions = new Actions()
//            .guildId(DEFAULT_GUILD_ID)
//            .numStrikes(DEFAULT_NUM_STRIKES)
//            .action(DEFAULT_ACTION)
//            .time(DEFAULT_TIME);
//        return actions;
//    }
//
//    @Before
//    public void initTest() {
//        actions = createEntity(em);
//    }
//
//    @Test
//    @Transactional
//    public void createActions() throws Exception {
//        int databaseSizeBeforeCreate = actionsRepository.findAll().size();
//
//        // Create the Actions
//        restActionsMockMvc.perform(post("/api/actions")
//            .contentType(TestUtil.APPLICATION_JSON_UTF8)
//            .content(TestUtil.convertObjectToJsonBytes(actions)))
//            .andExpect(status().isCreated());
//
//        // Validate the Actions in the database
//        List<Actions> actionsList = actionsRepository.findAll();
//        assertThat(actionsList).hasSize(databaseSizeBeforeCreate + 1);
//        Actions testActions = actionsList.get(actionsList.size() - 1);
//        assertThat(testActions.getGuildId()).isEqualTo(DEFAULT_GUILD_ID);
//        assertThat(testActions.getNumStrikes()).isEqualTo(DEFAULT_NUM_STRIKES);
//        assertThat(testActions.getAction()).isEqualTo(DEFAULT_ACTION);
//        assertThat(testActions.getTime()).isEqualTo(DEFAULT_TIME);
//    }
//
//    @Test
//    @Transactional
//    public void createActionsWithExistingId() throws Exception {
//        int databaseSizeBeforeCreate = actionsRepository.findAll().size();
//
//        // Create the Actions with an existing ID
//        actions.setId(1L);
//
//        // An entity with an existing ID cannot be created, so this API call must fail
//        restActionsMockMvc.perform(post("/api/actions")
//            .contentType(TestUtil.APPLICATION_JSON_UTF8)
//            .content(TestUtil.convertObjectToJsonBytes(actions)))
//            .andExpect(status().isBadRequest());
//
//        // Validate the Actions in the database
//        List<Actions> actionsList = actionsRepository.findAll();
//        assertThat(actionsList).hasSize(databaseSizeBeforeCreate);
//    }
//
//    @Test
//    @Transactional
//    public void checkGuildIdIsRequired() throws Exception {
//        int databaseSizeBeforeTest = actionsRepository.findAll().size();
//        // set the field null
//        actions.setGuildId(null);
//
//        // Create the Actions, which fails.
//
//        restActionsMockMvc.perform(post("/api/actions")
//            .contentType(TestUtil.APPLICATION_JSON_UTF8)
//            .content(TestUtil.convertObjectToJsonBytes(actions)))
//            .andExpect(status().isBadRequest());
//
//        List<Actions> actionsList = actionsRepository.findAll();
//        assertThat(actionsList).hasSize(databaseSizeBeforeTest);
//    }
//
//    @Test
//    @Transactional
//    public void checkNumStrikesIsRequired() throws Exception {
//        int databaseSizeBeforeTest = actionsRepository.findAll().size();
//        // set the field null
//        actions.setNumStrikes(null);
//
//        // Create the Actions, which fails.
//
//        restActionsMockMvc.perform(post("/api/actions")
//            .contentType(TestUtil.APPLICATION_JSON_UTF8)
//            .content(TestUtil.convertObjectToJsonBytes(actions)))
//            .andExpect(status().isBadRequest());
//
//        List<Actions> actionsList = actionsRepository.findAll();
//        assertThat(actionsList).hasSize(databaseSizeBeforeTest);
//    }
//
//    @Test
//    @Transactional
//    public void checkActionIsRequired() throws Exception {
//        int databaseSizeBeforeTest = actionsRepository.findAll().size();
//        // set the field null
//        actions.setAction(null);
//
//        // Create the Actions, which fails.
//
//        restActionsMockMvc.perform(post("/api/actions")
//            .contentType(TestUtil.APPLICATION_JSON_UTF8)
//            .content(TestUtil.convertObjectToJsonBytes(actions)))
//            .andExpect(status().isBadRequest());
//
//        List<Actions> actionsList = actionsRepository.findAll();
//        assertThat(actionsList).hasSize(databaseSizeBeforeTest);
//    }
//
//    @Test
//    @Transactional
//    public void checkTimeIsRequired() throws Exception {
//        int databaseSizeBeforeTest = actionsRepository.findAll().size();
//        // set the field null
//        actions.setTime(null);
//
//        // Create the Actions, which fails.
//
//        restActionsMockMvc.perform(post("/api/actions")
//            .contentType(TestUtil.APPLICATION_JSON_UTF8)
//            .content(TestUtil.convertObjectToJsonBytes(actions)))
//            .andExpect(status().isBadRequest());
//
//        List<Actions> actionsList = actionsRepository.findAll();
//        assertThat(actionsList).hasSize(databaseSizeBeforeTest);
//    }
//
//    @Test
//    @Transactional
//    public void getAllActions() throws Exception {
//        // Initialize the database
//        actionsRepository.saveAndFlush(actions);
//
//        // Get all the actionsList
//        restActionsMockMvc.perform(get("/api/actions?sort=id,desc"))
//            .andExpect(status().isOk())
//            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
//            .andExpect(jsonPath("$.[*].id").value(hasItem(actions.getId().intValue())))
//            .andExpect(jsonPath("$.[*].guildId").value(hasItem(DEFAULT_GUILD_ID.intValue())))
//            .andExpect(jsonPath("$.[*].numStrikes").value(hasItem(DEFAULT_NUM_STRIKES)))
//            .andExpect(jsonPath("$.[*].action").value(hasItem(DEFAULT_ACTION)))
//            .andExpect(jsonPath("$.[*].time").value(hasItem(DEFAULT_TIME)));
//    }
//
//    @Test
//    @Transactional
//    public void getActions() throws Exception {
//        // Initialize the database
//        actionsRepository.saveAndFlush(actions);
//
//        // Get the actions
//        restActionsMockMvc.perform(get("/api/actions/{id}", actions.getId()))
//            .andExpect(status().isOk())
//            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
//            .andExpect(jsonPath("$.id").value(actions.getId().intValue()))
//            .andExpect(jsonPath("$.guildId").value(DEFAULT_GUILD_ID.intValue()))
//            .andExpect(jsonPath("$.numStrikes").value(DEFAULT_NUM_STRIKES))
//            .andExpect(jsonPath("$.action").value(DEFAULT_ACTION))
//            .andExpect(jsonPath("$.time").value(DEFAULT_TIME));
//    }
//
//    @Test
//    @Transactional
//    public void getNonExistingActions() throws Exception {
//        // Get the actions
//        restActionsMockMvc.perform(get("/api/actions/{id}", Long.MAX_VALUE))
//            .andExpect(status().isNotFound());
//    }
//
//    @Test
//    @Transactional
//    public void updateActions() throws Exception {
//        // Initialize the database
//        actionsService.save(actions);
//
//        int databaseSizeBeforeUpdate = actionsRepository.findAll().size();
//
//        // Update the actions
//        Actions updatedActions = actionsRepository.findById(actions.getId()).get();
//        // Disconnect from session so that the updates on updatedActions are not directly saved in db
//        em.detach(updatedActions);
//        updatedActions
//            .guildId(UPDATED_GUILD_ID)
//            .numStrikes(UPDATED_NUM_STRIKES)
//            .action(UPDATED_ACTION)
//            .time(UPDATED_TIME);
//
//        restActionsMockMvc.perform(put("/api/actions")
//            .contentType(TestUtil.APPLICATION_JSON_UTF8)
//            .content(TestUtil.convertObjectToJsonBytes(updatedActions)))
//            .andExpect(status().isOk());
//
//        // Validate the Actions in the database
//        List<Actions> actionsList = actionsRepository.findAll();
//        assertThat(actionsList).hasSize(databaseSizeBeforeUpdate);
//        Actions testActions = actionsList.get(actionsList.size() - 1);
//        assertThat(testActions.getGuildId()).isEqualTo(UPDATED_GUILD_ID);
//        assertThat(testActions.getNumStrikes()).isEqualTo(UPDATED_NUM_STRIKES);
//        assertThat(testActions.getAction()).isEqualTo(UPDATED_ACTION);
//        assertThat(testActions.getTime()).isEqualTo(UPDATED_TIME);
//    }
//
//    @Test
//    @Transactional
//    public void updateNonExistingActions() throws Exception {
//        int databaseSizeBeforeUpdate = actionsRepository.findAll().size();
//
//        // Create the Actions
//
//        // If the entity doesn't have an ID, it will throw BadRequestAlertException
//        restActionsMockMvc.perform(put("/api/actions")
//            .contentType(TestUtil.APPLICATION_JSON_UTF8)
//            .content(TestUtil.convertObjectToJsonBytes(actions)))
//            .andExpect(status().isBadRequest());
//
//        // Validate the Actions in the database
//        List<Actions> actionsList = actionsRepository.findAll();
//        assertThat(actionsList).hasSize(databaseSizeBeforeUpdate);
//    }
//
//    @Test
//    @Transactional
//    public void deleteActions() throws Exception {
//        // Initialize the database
//        actionsService.save(actions);
//
//        int databaseSizeBeforeDelete = actionsRepository.findAll().size();
//
//        // Delete the actions
//        restActionsMockMvc.perform(delete("/api/actions/{id}", actions.getId())
//            .accept(TestUtil.APPLICATION_JSON_UTF8))
//            .andExpect(status().isOk());
//
//        // Validate the database is empty
//        List<Actions> actionsList = actionsRepository.findAll();
//        assertThat(actionsList).hasSize(databaseSizeBeforeDelete - 1);
//    }
//
//    @Test
//    @Transactional
//    public void equalsVerifier() throws Exception {
//        TestUtil.equalsVerifier(Actions.class);
//        Actions actions1 = new Actions();
//        actions1.setId(1L);
//        Actions actions2 = new Actions();
//        actions2.setId(actions1.getId());
//        assertThat(actions1).isEqualTo(actions2);
//        actions2.setId(2L);
//        assertThat(actions1).isNotEqualTo(actions2);
//        actions1.setId(null);
//        assertThat(actions1).isNotEqualTo(actions2);
//    }
//}
