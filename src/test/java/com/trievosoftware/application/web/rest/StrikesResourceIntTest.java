//package com.trievosoftware.application.web.rest;
//
//import com.trievosoftware.application.SiaApp;
//
//import com.trievosoftware.application.domain.Strikes;
//import com.trievosoftware.application.repository.StrikesRepository;
//import com.trievosoftware.application.service.StrikesService;
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
// * Test class for the StrikesResource REST controller.
// *
// * @see StrikesResource
// */
//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = SiaApp.class)
//public class StrikesResourceIntTest {
//
//    private static final Long DEFAULT_GUILD_ID = 1L;
//    private static final Long UPDATED_GUILD_ID = 2L;
//
//    private static final Long DEFAULT_USER_ID = 1L;
//    private static final Long UPDATED_USER_ID = 2L;
//
//    private static final Integer DEFAULT_STRIKES = 1;
//    private static final Integer UPDATED_STRIKES = 2;
//
//    @Autowired
//    private StrikesRepository strikesRepository;
//
//    @Autowired
//    private StrikesService strikesService;
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
//    private MockMvc restStrikesMockMvc;
//
//    private Strikes strikes;
//
//    @Before
//    public void setup() {
//        MockitoAnnotations.initMocks(this);
//        final StrikesResource strikesResource = new StrikesResource(strikesService);
//        this.restStrikesMockMvc = MockMvcBuilders.standaloneSetup(strikesResource)
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
//    public static Strikes createEntity(EntityManager em) {
//        Strikes strikes = new Strikes()
//            .guildId(DEFAULT_GUILD_ID)
//            .userId(DEFAULT_USER_ID)
//            .strikes(DEFAULT_STRIKES);
//        return strikes;
//    }
//
//    @Before
//    public void initTest() {
//        strikes = createEntity(em);
//    }
//
//    @Test
//    @Transactional
//    public void createStrikes() throws Exception {
//        int databaseSizeBeforeCreate = strikesRepository.findAll().size();
//
//        // Create the Strikes
//        restStrikesMockMvc.perform(post("/api/strikes")
//            .contentType(TestUtil.APPLICATION_JSON_UTF8)
//            .content(TestUtil.convertObjectToJsonBytes(strikes)))
//            .andExpect(status().isCreated());
//
//        // Validate the Strikes in the database
//        List<Strikes> strikesList = strikesRepository.findAll();
//        assertThat(strikesList).hasSize(databaseSizeBeforeCreate + 1);
//        Strikes testStrikes = strikesList.get(strikesList.size() - 1);
//        assertThat(testStrikes.getGuildId()).isEqualTo(DEFAULT_GUILD_ID);
//        assertThat(testStrikes.getUserId()).isEqualTo(DEFAULT_USER_ID);
//        assertThat(testStrikes.getStrikes()).isEqualTo(DEFAULT_STRIKES);
//    }
//
//    @Test
//    @Transactional
//    public void createStrikesWithExistingId() throws Exception {
//        int databaseSizeBeforeCreate = strikesRepository.findAll().size();
//
//        // Create the Strikes with an existing ID
//        strikes.setId(1L);
//
//        // An entity with an existing ID cannot be created, so this API call must fail
//        restStrikesMockMvc.perform(post("/api/strikes")
//            .contentType(TestUtil.APPLICATION_JSON_UTF8)
//            .content(TestUtil.convertObjectToJsonBytes(strikes)))
//            .andExpect(status().isBadRequest());
//
//        // Validate the Strikes in the database
//        List<Strikes> strikesList = strikesRepository.findAll();
//        assertThat(strikesList).hasSize(databaseSizeBeforeCreate);
//    }
//
//    @Test
//    @Transactional
//    public void checkGuildIdIsRequired() throws Exception {
//        int databaseSizeBeforeTest = strikesRepository.findAll().size();
//        // set the field null
//        strikes.setGuildId(null);
//
//        // Create the Strikes, which fails.
//
//        restStrikesMockMvc.perform(post("/api/strikes")
//            .contentType(TestUtil.APPLICATION_JSON_UTF8)
//            .content(TestUtil.convertObjectToJsonBytes(strikes)))
//            .andExpect(status().isBadRequest());
//
//        List<Strikes> strikesList = strikesRepository.findAll();
//        assertThat(strikesList).hasSize(databaseSizeBeforeTest);
//    }
//
//    @Test
//    @Transactional
//    public void checkUserIdIsRequired() throws Exception {
//        int databaseSizeBeforeTest = strikesRepository.findAll().size();
//        // set the field null
//        strikes.setUserId(null);
//
//        // Create the Strikes, which fails.
//
//        restStrikesMockMvc.perform(post("/api/strikes")
//            .contentType(TestUtil.APPLICATION_JSON_UTF8)
//            .content(TestUtil.convertObjectToJsonBytes(strikes)))
//            .andExpect(status().isBadRequest());
//
//        List<Strikes> strikesList = strikesRepository.findAll();
//        assertThat(strikesList).hasSize(databaseSizeBeforeTest);
//    }
//
//    @Test
//    @Transactional
//    public void checkStrikesIsRequired() throws Exception {
//        int databaseSizeBeforeTest = strikesRepository.findAll().size();
//        // set the field null
//        strikes.setStrikes(null);
//
//        // Create the Strikes, which fails.
//
//        restStrikesMockMvc.perform(post("/api/strikes")
//            .contentType(TestUtil.APPLICATION_JSON_UTF8)
//            .content(TestUtil.convertObjectToJsonBytes(strikes)))
//            .andExpect(status().isBadRequest());
//
//        List<Strikes> strikesList = strikesRepository.findAll();
//        assertThat(strikesList).hasSize(databaseSizeBeforeTest);
//    }
//
//    @Test
//    @Transactional
//    public void getAllStrikes() throws Exception {
//        // Initialize the database
//        strikesRepository.saveAndFlush(strikes);
//
//        // Get all the strikesList
//        restStrikesMockMvc.perform(get("/api/strikes?sort=id,desc"))
//            .andExpect(status().isOk())
//            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
//            .andExpect(jsonPath("$.[*].id").value(hasItem(strikes.getId().intValue())))
//            .andExpect(jsonPath("$.[*].guildId").value(hasItem(DEFAULT_GUILD_ID.intValue())))
//            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID.intValue())))
//            .andExpect(jsonPath("$.[*].strikes").value(hasItem(DEFAULT_STRIKES)));
//    }
//
//    @Test
//    @Transactional
//    public void getStrikes() throws Exception {
//        // Initialize the database
//        strikesRepository.saveAndFlush(strikes);
//
//        // Get the strikes
//        restStrikesMockMvc.perform(get("/api/strikes/{id}", strikes.getId()))
//            .andExpect(status().isOk())
//            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
//            .andExpect(jsonPath("$.id").value(strikes.getId().intValue()))
//            .andExpect(jsonPath("$.guildId").value(DEFAULT_GUILD_ID.intValue()))
//            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID.intValue()))
//            .andExpect(jsonPath("$.strikes").value(DEFAULT_STRIKES));
//    }
//
//    @Test
//    @Transactional
//    public void getNonExistingStrikes() throws Exception {
//        // Get the strikes
//        restStrikesMockMvc.perform(get("/api/strikes/{id}", Long.MAX_VALUE))
//            .andExpect(status().isNotFound());
//    }
//
//    @Test
//    @Transactional
//    public void updateStrikes() throws Exception {
//        // Initialize the database
//        strikesService.save(strikes);
//
//        int databaseSizeBeforeUpdate = strikesRepository.findAll().size();
//
//        // Update the strikes
//        Strikes updatedStrikes = strikesRepository.findById(strikes.getId()).get();
//        // Disconnect from session so that the updates on updatedStrikes are not directly saved in db
//        em.detach(updatedStrikes);
//        updatedStrikes
//            .guildId(UPDATED_GUILD_ID)
//            .userId(UPDATED_USER_ID)
//            .strikes(UPDATED_STRIKES);
//
//        restStrikesMockMvc.perform(put("/api/strikes")
//            .contentType(TestUtil.APPLICATION_JSON_UTF8)
//            .content(TestUtil.convertObjectToJsonBytes(updatedStrikes)))
//            .andExpect(status().isOk());
//
//        // Validate the Strikes in the database
//        List<Strikes> strikesList = strikesRepository.findAll();
//        assertThat(strikesList).hasSize(databaseSizeBeforeUpdate);
//        Strikes testStrikes = strikesList.get(strikesList.size() - 1);
//        assertThat(testStrikes.getGuildId()).isEqualTo(UPDATED_GUILD_ID);
//        assertThat(testStrikes.getUserId()).isEqualTo(UPDATED_USER_ID);
//        assertThat(testStrikes.getStrikes()).isEqualTo(UPDATED_STRIKES);
//    }
//
//    @Test
//    @Transactional
//    public void updateNonExistingStrikes() throws Exception {
//        int databaseSizeBeforeUpdate = strikesRepository.findAll().size();
//
//        // Create the Strikes
//
//        // If the entity doesn't have an ID, it will throw BadRequestAlertException
//        restStrikesMockMvc.perform(put("/api/strikes")
//            .contentType(TestUtil.APPLICATION_JSON_UTF8)
//            .content(TestUtil.convertObjectToJsonBytes(strikes)))
//            .andExpect(status().isBadRequest());
//
//        // Validate the Strikes in the database
//        List<Strikes> strikesList = strikesRepository.findAll();
//        assertThat(strikesList).hasSize(databaseSizeBeforeUpdate);
//    }
//
//    @Test
//    @Transactional
//    public void deleteStrikes() throws Exception {
//        // Initialize the database
//        strikesService.save(strikes);
//
//        int databaseSizeBeforeDelete = strikesRepository.findAll().size();
//
//        // Delete the strikes
//        restStrikesMockMvc.perform(delete("/api/strikes/{id}", strikes.getId())
//            .accept(TestUtil.APPLICATION_JSON_UTF8))
//            .andExpect(status().isOk());
//
//        // Validate the database is empty
//        List<Strikes> strikesList = strikesRepository.findAll();
//        assertThat(strikesList).hasSize(databaseSizeBeforeDelete - 1);
//    }
//
//    @Test
//    @Transactional
//    public void equalsVerifier() throws Exception {
//        TestUtil.equalsVerifier(Strikes.class);
//        Strikes strikes1 = new Strikes();
//        strikes1.setId(1L);
//        Strikes strikes2 = new Strikes();
//        strikes2.setId(strikes1.getId());
//        assertThat(strikes1).isEqualTo(strikes2);
//        strikes2.setId(2L);
//        assertThat(strikes1).isNotEqualTo(strikes2);
//        strikes1.setId(null);
//        assertThat(strikes1).isNotEqualTo(strikes2);
//    }
//}
