package com.trievosoftware.application.web.rest;

import com.trievosoftware.application.SiaApp;

import com.trievosoftware.application.domain.DiscordUser;
import com.trievosoftware.application.repository.DiscordUserRepository;
import com.trievosoftware.application.service.DiscordUserService;
import com.trievosoftware.application.web.rest.errors.ExceptionTranslator;
import com.trievosoftware.application.service.dto.DiscordUserCriteria;
import com.trievosoftware.application.service.DiscordUserQueryService;

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
 * Test class for the DiscordUserResource REST controller.
 *
 * @see DiscordUserResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SiaApp.class)
public class DiscordUserResourceIntTest {

    private static final Long DEFAULT_USER_ID = 1L;
    private static final Long UPDATED_USER_ID = 2L;

    private static final Integer DEFAULT_COMMANDS_ISSUED = 1;
    private static final Integer UPDATED_COMMANDS_ISSUED = 2;

    private static final Boolean DEFAULT_BLACKLISTED = false;
    private static final Boolean UPDATED_BLACKLISTED = true;

    @Autowired
    private DiscordUserRepository discordUserRepository;

    @Autowired
    private DiscordUserService discordUserService;

    @Autowired
    private DiscordUserQueryService discordUserQueryService;

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

    private MockMvc restDiscordUserMockMvc;

    private DiscordUser discordUser;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final DiscordUserResource discordUserResource = new DiscordUserResource(discordUserService, discordUserQueryService);
        this.restDiscordUserMockMvc = MockMvcBuilders.standaloneSetup(discordUserResource)
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
    public static DiscordUser createEntity(EntityManager em) {
        DiscordUser discordUser = new DiscordUser()
            .userId(DEFAULT_USER_ID)
            .commandsIssued(DEFAULT_COMMANDS_ISSUED)
            .blacklisted(DEFAULT_BLACKLISTED);
        return discordUser;
    }

    @Before
    public void initTest() {
        discordUser = createEntity(em);
    }

    @Test
    @Transactional
    public void createDiscordUser() throws Exception {
        int databaseSizeBeforeCreate = discordUserRepository.findAll().size();

        // Create the DiscordUser
        restDiscordUserMockMvc.perform(post("/api/discord-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(discordUser)))
            .andExpect(status().isCreated());

        // Validate the DiscordUser in the database
        List<DiscordUser> discordUserList = discordUserRepository.findAll();
        assertThat(discordUserList).hasSize(databaseSizeBeforeCreate + 1);
        DiscordUser testDiscordUser = discordUserList.get(discordUserList.size() - 1);
        assertThat(testDiscordUser.getUserId()).isEqualTo(DEFAULT_USER_ID);
        assertThat(testDiscordUser.getCommandsIssued()).isEqualTo(DEFAULT_COMMANDS_ISSUED);
        assertThat(testDiscordUser.isBlacklisted()).isEqualTo(DEFAULT_BLACKLISTED);
    }

    @Test
    @Transactional
    public void createDiscordUserWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = discordUserRepository.findAll().size();

        // Create the DiscordUser with an existing ID
        discordUser.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDiscordUserMockMvc.perform(post("/api/discord-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(discordUser)))
            .andExpect(status().isBadRequest());

        // Validate the DiscordUser in the database
        List<DiscordUser> discordUserList = discordUserRepository.findAll();
        assertThat(discordUserList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkUserIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = discordUserRepository.findAll().size();
        // set the field null
        discordUser.setUserId(null);

        // Create the DiscordUser, which fails.

        restDiscordUserMockMvc.perform(post("/api/discord-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(discordUser)))
            .andExpect(status().isBadRequest());

        List<DiscordUser> discordUserList = discordUserRepository.findAll();
        assertThat(discordUserList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCommandsIssuedIsRequired() throws Exception {
        int databaseSizeBeforeTest = discordUserRepository.findAll().size();
        // set the field null
        discordUser.setCommandsIssued(null);

        // Create the DiscordUser, which fails.

        restDiscordUserMockMvc.perform(post("/api/discord-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(discordUser)))
            .andExpect(status().isBadRequest());

        List<DiscordUser> discordUserList = discordUserRepository.findAll();
        assertThat(discordUserList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkBlacklistedIsRequired() throws Exception {
        int databaseSizeBeforeTest = discordUserRepository.findAll().size();
        // set the field null
        discordUser.setBlacklisted(null);

        // Create the DiscordUser, which fails.

        restDiscordUserMockMvc.perform(post("/api/discord-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(discordUser)))
            .andExpect(status().isBadRequest());

        List<DiscordUser> discordUserList = discordUserRepository.findAll();
        assertThat(discordUserList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllDiscordUsers() throws Exception {
        // Initialize the database
        discordUserRepository.saveAndFlush(discordUser);

        // Get all the discordUserList
        restDiscordUserMockMvc.perform(get("/api/discord-users?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(discordUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID.intValue())))
            .andExpect(jsonPath("$.[*].commandsIssued").value(hasItem(DEFAULT_COMMANDS_ISSUED)))
            .andExpect(jsonPath("$.[*].blacklisted").value(hasItem(DEFAULT_BLACKLISTED.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getDiscordUser() throws Exception {
        // Initialize the database
        discordUserRepository.saveAndFlush(discordUser);

        // Get the discordUser
        restDiscordUserMockMvc.perform(get("/api/discord-users/{id}", discordUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(discordUser.getId().intValue()))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID.intValue()))
            .andExpect(jsonPath("$.commandsIssued").value(DEFAULT_COMMANDS_ISSUED))
            .andExpect(jsonPath("$.blacklisted").value(DEFAULT_BLACKLISTED.booleanValue()));
    }

    @Test
    @Transactional
    public void getAllDiscordUsersByUserIdIsEqualToSomething() throws Exception {
        // Initialize the database
        discordUserRepository.saveAndFlush(discordUser);

        // Get all the discordUserList where userId equals to DEFAULT_USER_ID
        defaultDiscordUserShouldBeFound("userId.equals=" + DEFAULT_USER_ID);

        // Get all the discordUserList where userId equals to UPDATED_USER_ID
        defaultDiscordUserShouldNotBeFound("userId.equals=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    public void getAllDiscordUsersByUserIdIsInShouldWork() throws Exception {
        // Initialize the database
        discordUserRepository.saveAndFlush(discordUser);

        // Get all the discordUserList where userId in DEFAULT_USER_ID or UPDATED_USER_ID
        defaultDiscordUserShouldBeFound("userId.in=" + DEFAULT_USER_ID + "," + UPDATED_USER_ID);

        // Get all the discordUserList where userId equals to UPDATED_USER_ID
        defaultDiscordUserShouldNotBeFound("userId.in=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    public void getAllDiscordUsersByUserIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        discordUserRepository.saveAndFlush(discordUser);

        // Get all the discordUserList where userId is not null
        defaultDiscordUserShouldBeFound("userId.specified=true");

        // Get all the discordUserList where userId is null
        defaultDiscordUserShouldNotBeFound("userId.specified=false");
    }

    @Test
    @Transactional
    public void getAllDiscordUsersByUserIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        discordUserRepository.saveAndFlush(discordUser);

        // Get all the discordUserList where userId greater than or equals to DEFAULT_USER_ID
        defaultDiscordUserShouldBeFound("userId.greaterOrEqualThan=" + DEFAULT_USER_ID);

        // Get all the discordUserList where userId greater than or equals to UPDATED_USER_ID
        defaultDiscordUserShouldNotBeFound("userId.greaterOrEqualThan=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    public void getAllDiscordUsersByUserIdIsLessThanSomething() throws Exception {
        // Initialize the database
        discordUserRepository.saveAndFlush(discordUser);

        // Get all the discordUserList where userId less than or equals to DEFAULT_USER_ID
        defaultDiscordUserShouldNotBeFound("userId.lessThan=" + DEFAULT_USER_ID);

        // Get all the discordUserList where userId less than or equals to UPDATED_USER_ID
        defaultDiscordUserShouldBeFound("userId.lessThan=" + UPDATED_USER_ID);
    }


    @Test
    @Transactional
    public void getAllDiscordUsersByCommandsIssuedIsEqualToSomething() throws Exception {
        // Initialize the database
        discordUserRepository.saveAndFlush(discordUser);

        // Get all the discordUserList where commandsIssued equals to DEFAULT_COMMANDS_ISSUED
        defaultDiscordUserShouldBeFound("commandsIssued.equals=" + DEFAULT_COMMANDS_ISSUED);

        // Get all the discordUserList where commandsIssued equals to UPDATED_COMMANDS_ISSUED
        defaultDiscordUserShouldNotBeFound("commandsIssued.equals=" + UPDATED_COMMANDS_ISSUED);
    }

    @Test
    @Transactional
    public void getAllDiscordUsersByCommandsIssuedIsInShouldWork() throws Exception {
        // Initialize the database
        discordUserRepository.saveAndFlush(discordUser);

        // Get all the discordUserList where commandsIssued in DEFAULT_COMMANDS_ISSUED or UPDATED_COMMANDS_ISSUED
        defaultDiscordUserShouldBeFound("commandsIssued.in=" + DEFAULT_COMMANDS_ISSUED + "," + UPDATED_COMMANDS_ISSUED);

        // Get all the discordUserList where commandsIssued equals to UPDATED_COMMANDS_ISSUED
        defaultDiscordUserShouldNotBeFound("commandsIssued.in=" + UPDATED_COMMANDS_ISSUED);
    }

    @Test
    @Transactional
    public void getAllDiscordUsersByCommandsIssuedIsNullOrNotNull() throws Exception {
        // Initialize the database
        discordUserRepository.saveAndFlush(discordUser);

        // Get all the discordUserList where commandsIssued is not null
        defaultDiscordUserShouldBeFound("commandsIssued.specified=true");

        // Get all the discordUserList where commandsIssued is null
        defaultDiscordUserShouldNotBeFound("commandsIssued.specified=false");
    }

    @Test
    @Transactional
    public void getAllDiscordUsersByCommandsIssuedIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        discordUserRepository.saveAndFlush(discordUser);

        // Get all the discordUserList where commandsIssued greater than or equals to DEFAULT_COMMANDS_ISSUED
        defaultDiscordUserShouldBeFound("commandsIssued.greaterOrEqualThan=" + DEFAULT_COMMANDS_ISSUED);

        // Get all the discordUserList where commandsIssued greater than or equals to UPDATED_COMMANDS_ISSUED
        defaultDiscordUserShouldNotBeFound("commandsIssued.greaterOrEqualThan=" + UPDATED_COMMANDS_ISSUED);
    }

    @Test
    @Transactional
    public void getAllDiscordUsersByCommandsIssuedIsLessThanSomething() throws Exception {
        // Initialize the database
        discordUserRepository.saveAndFlush(discordUser);

        // Get all the discordUserList where commandsIssued less than or equals to DEFAULT_COMMANDS_ISSUED
        defaultDiscordUserShouldNotBeFound("commandsIssued.lessThan=" + DEFAULT_COMMANDS_ISSUED);

        // Get all the discordUserList where commandsIssued less than or equals to UPDATED_COMMANDS_ISSUED
        defaultDiscordUserShouldBeFound("commandsIssued.lessThan=" + UPDATED_COMMANDS_ISSUED);
    }


    @Test
    @Transactional
    public void getAllDiscordUsersByBlacklistedIsEqualToSomething() throws Exception {
        // Initialize the database
        discordUserRepository.saveAndFlush(discordUser);

        // Get all the discordUserList where blacklisted equals to DEFAULT_BLACKLISTED
        defaultDiscordUserShouldBeFound("blacklisted.equals=" + DEFAULT_BLACKLISTED);

        // Get all the discordUserList where blacklisted equals to UPDATED_BLACKLISTED
        defaultDiscordUserShouldNotBeFound("blacklisted.equals=" + UPDATED_BLACKLISTED);
    }

    @Test
    @Transactional
    public void getAllDiscordUsersByBlacklistedIsInShouldWork() throws Exception {
        // Initialize the database
        discordUserRepository.saveAndFlush(discordUser);

        // Get all the discordUserList where blacklisted in DEFAULT_BLACKLISTED or UPDATED_BLACKLISTED
        defaultDiscordUserShouldBeFound("blacklisted.in=" + DEFAULT_BLACKLISTED + "," + UPDATED_BLACKLISTED);

        // Get all the discordUserList where blacklisted equals to UPDATED_BLACKLISTED
        defaultDiscordUserShouldNotBeFound("blacklisted.in=" + UPDATED_BLACKLISTED);
    }

    @Test
    @Transactional
    public void getAllDiscordUsersByBlacklistedIsNullOrNotNull() throws Exception {
        // Initialize the database
        discordUserRepository.saveAndFlush(discordUser);

        // Get all the discordUserList where blacklisted is not null
        defaultDiscordUserShouldBeFound("blacklisted.specified=true");

        // Get all the discordUserList where blacklisted is null
        defaultDiscordUserShouldNotBeFound("blacklisted.specified=false");
    }
    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultDiscordUserShouldBeFound(String filter) throws Exception {
        restDiscordUserMockMvc.perform(get("/api/discord-users?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(discordUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID.intValue())))
            .andExpect(jsonPath("$.[*].commandsIssued").value(hasItem(DEFAULT_COMMANDS_ISSUED)))
            .andExpect(jsonPath("$.[*].blacklisted").value(hasItem(DEFAULT_BLACKLISTED.booleanValue())));

        // Check, that the count call also returns 1
        restDiscordUserMockMvc.perform(get("/api/discord-users/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultDiscordUserShouldNotBeFound(String filter) throws Exception {
        restDiscordUserMockMvc.perform(get("/api/discord-users?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDiscordUserMockMvc.perform(get("/api/discord-users/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingDiscordUser() throws Exception {
        // Get the discordUser
        restDiscordUserMockMvc.perform(get("/api/discord-users/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDiscordUser() throws Exception {
        // Initialize the database
        discordUserService.save(discordUser);

        int databaseSizeBeforeUpdate = discordUserRepository.findAll().size();

        // Update the discordUser
        DiscordUser updatedDiscordUser = discordUserRepository.findById(discordUser.getId()).get();
        // Disconnect from session so that the updates on updatedDiscordUser are not directly saved in db
        em.detach(updatedDiscordUser);
        updatedDiscordUser
            .userId(UPDATED_USER_ID)
            .commandsIssued(UPDATED_COMMANDS_ISSUED)
            .blacklisted(UPDATED_BLACKLISTED);

        restDiscordUserMockMvc.perform(put("/api/discord-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedDiscordUser)))
            .andExpect(status().isOk());

        // Validate the DiscordUser in the database
        List<DiscordUser> discordUserList = discordUserRepository.findAll();
        assertThat(discordUserList).hasSize(databaseSizeBeforeUpdate);
        DiscordUser testDiscordUser = discordUserList.get(discordUserList.size() - 1);
        assertThat(testDiscordUser.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testDiscordUser.getCommandsIssued()).isEqualTo(UPDATED_COMMANDS_ISSUED);
        assertThat(testDiscordUser.isBlacklisted()).isEqualTo(UPDATED_BLACKLISTED);
    }

    @Test
    @Transactional
    public void updateNonExistingDiscordUser() throws Exception {
        int databaseSizeBeforeUpdate = discordUserRepository.findAll().size();

        // Create the DiscordUser

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDiscordUserMockMvc.perform(put("/api/discord-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(discordUser)))
            .andExpect(status().isBadRequest());

        // Validate the DiscordUser in the database
        List<DiscordUser> discordUserList = discordUserRepository.findAll();
        assertThat(discordUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteDiscordUser() throws Exception {
        // Initialize the database
        discordUserService.save(discordUser);

        int databaseSizeBeforeDelete = discordUserRepository.findAll().size();

        // Delete the discordUser
        restDiscordUserMockMvc.perform(delete("/api/discord-users/{id}", discordUser.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<DiscordUser> discordUserList = discordUserRepository.findAll();
        assertThat(discordUserList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DiscordUser.class);
        DiscordUser discordUser1 = new DiscordUser();
        discordUser1.setId(1L);
        DiscordUser discordUser2 = new DiscordUser();
        discordUser2.setId(discordUser1.getId());
        assertThat(discordUser1).isEqualTo(discordUser2);
        discordUser2.setId(2L);
        assertThat(discordUser1).isNotEqualTo(discordUser2);
        discordUser1.setId(null);
        assertThat(discordUser1).isNotEqualTo(discordUser2);
    }
}
