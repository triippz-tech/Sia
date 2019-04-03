//package com.trievosoftware.application.web.rest;
//
//import com.trievosoftware.application.SiaApp;
//
//import com.trievosoftware.application.domain.CustomCommand;
//import com.trievosoftware.application.domain.GuildRoles;
//import com.trievosoftware.application.repository.CustomCommandRepository;
//import com.trievosoftware.application.service.CustomCommandService;
//import com.trievosoftware.application.web.rest.errors.ExceptionTranslator;
//import com.trievosoftware.application.service.dto.CustomCommandCriteria;
//import com.trievosoftware.application.service.CustomCommandQueryService;
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
// * Test class for the CustomCommandResource REST controller.
// *
// * @see CustomCommandResource
// */
//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = SiaApp.class)
//public class CustomCommandResourceIntTest {
//
//    private static final Long DEFAULT_GUILD_ID = 1L;
//    private static final Long UPDATED_GUILD_ID = 2L;
//
//    private static final String DEFAULT_COMMAND_NAME = "AAAAAAAAAA";
//    private static final String UPDATED_COMMAND_NAME = "BBBBBBBBBB";
//
//    private static final String DEFAULT_MESSAGE = "AAAAAAAAAA";
//    private static final String UPDATED_MESSAGE = "BBBBBBBBBB";
//
//    @Autowired
//    private CustomCommandRepository customCommandRepository;
//
//    @Autowired
//    private CustomCommandService customCommandService;
//
//    @Autowired
//    private CustomCommandQueryService customCommandQueryService;
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
//    private MockMvc restCustomCommandMockMvc;
//
//    private CustomCommand customCommand;
//
//    @Before
//    public void setup() {
//        MockitoAnnotations.initMocks(this);
//        final CustomCommandResource customCommandResource = new CustomCommandResource(customCommandService, customCommandQueryService);
//        this.restCustomCommandMockMvc = MockMvcBuilders.standaloneSetup(customCommandResource)
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
//    public static CustomCommand createEntity(EntityManager em) {
//        CustomCommand customCommand = new CustomCommand()
//            .guildId(DEFAULT_GUILD_ID)
//            .commandName(DEFAULT_COMMAND_NAME)
//            .message(DEFAULT_MESSAGE);
//        return customCommand;
//    }
//
//    @Before
//    public void initTest() {
//        customCommand = createEntity(em);
//    }
//
//    @Test
//    @Transactional
//    public void createCustomCommand() throws Exception {
//        int databaseSizeBeforeCreate = customCommandRepository.findAll().size();
//
//        // Create the CustomCommand
//        restCustomCommandMockMvc.perform(post("/api/custom-commands")
//            .contentType(TestUtil.APPLICATION_JSON_UTF8)
//            .content(TestUtil.convertObjectToJsonBytes(customCommand)))
//            .andExpect(status().isCreated());
//
//        // Validate the CustomCommand in the database
//        List<CustomCommand> customCommandList = customCommandRepository.findAll();
//        assertThat(customCommandList).hasSize(databaseSizeBeforeCreate + 1);
//        CustomCommand testCustomCommand = customCommandList.get(customCommandList.size() - 1);
//        assertThat(testCustomCommand.getGuildId()).isEqualTo(DEFAULT_GUILD_ID);
//        assertThat(testCustomCommand.getCommandName()).isEqualTo(DEFAULT_COMMAND_NAME);
//        assertThat(testCustomCommand.getMessage()).isEqualTo(DEFAULT_MESSAGE);
//    }
//
//    @Test
//    @Transactional
//    public void createCustomCommandWithExistingId() throws Exception {
//        int databaseSizeBeforeCreate = customCommandRepository.findAll().size();
//
//        // Create the CustomCommand with an existing ID
//        customCommand.setId(1L);
//
//        // An entity with an existing ID cannot be created, so this API call must fail
//        restCustomCommandMockMvc.perform(post("/api/custom-commands")
//            .contentType(TestUtil.APPLICATION_JSON_UTF8)
//            .content(TestUtil.convertObjectToJsonBytes(customCommand)))
//            .andExpect(status().isBadRequest());
//
//        // Validate the CustomCommand in the database
//        List<CustomCommand> customCommandList = customCommandRepository.findAll();
//        assertThat(customCommandList).hasSize(databaseSizeBeforeCreate);
//    }
//
//    @Test
//    @Transactional
//    public void checkGuildIdIsRequired() throws Exception {
//        int databaseSizeBeforeTest = customCommandRepository.findAll().size();
//        // set the field null
//        customCommand.setGuildId(null);
//
//        // Create the CustomCommand, which fails.
//
//        restCustomCommandMockMvc.perform(post("/api/custom-commands")
//            .contentType(TestUtil.APPLICATION_JSON_UTF8)
//            .content(TestUtil.convertObjectToJsonBytes(customCommand)))
//            .andExpect(status().isBadRequest());
//
//        List<CustomCommand> customCommandList = customCommandRepository.findAll();
//        assertThat(customCommandList).hasSize(databaseSizeBeforeTest);
//    }
//
//    @Test
//    @Transactional
//    public void checkCommandNameIsRequired() throws Exception {
//        int databaseSizeBeforeTest = customCommandRepository.findAll().size();
//        // set the field null
//        customCommand.setCommandName(null);
//
//        // Create the CustomCommand, which fails.
//
//        restCustomCommandMockMvc.perform(post("/api/custom-commands")
//            .contentType(TestUtil.APPLICATION_JSON_UTF8)
//            .content(TestUtil.convertObjectToJsonBytes(customCommand)))
//            .andExpect(status().isBadRequest());
//
//        List<CustomCommand> customCommandList = customCommandRepository.findAll();
//        assertThat(customCommandList).hasSize(databaseSizeBeforeTest);
//    }
//
//    @Test
//    @Transactional
//    public void getAllCustomCommands() throws Exception {
//        // Initialize the database
//        customCommandRepository.saveAndFlush(customCommand);
//
//        // Get all the customCommandList
//        restCustomCommandMockMvc.perform(get("/api/custom-commands?sort=id,desc"))
//            .andExpect(status().isOk())
//            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
//            .andExpect(jsonPath("$.[*].id").value(hasItem(customCommand.getId().intValue())))
//            .andExpect(jsonPath("$.[*].guildId").value(hasItem(DEFAULT_GUILD_ID.intValue())))
//            .andExpect(jsonPath("$.[*].commandName").value(hasItem(DEFAULT_COMMAND_NAME.toString())))
//            .andExpect(jsonPath("$.[*].message").value(hasItem(DEFAULT_MESSAGE.toString())));
//    }
//
//    @Test
//    @Transactional
//    public void getCustomCommand() throws Exception {
//        // Initialize the database
//        customCommandRepository.saveAndFlush(customCommand);
//
//        // Get the customCommand
//        restCustomCommandMockMvc.perform(get("/api/custom-commands/{id}", customCommand.getId()))
//            .andExpect(status().isOk())
//            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
//            .andExpect(jsonPath("$.id").value(customCommand.getId().intValue()))
//            .andExpect(jsonPath("$.guildId").value(DEFAULT_GUILD_ID.intValue()))
//            .andExpect(jsonPath("$.commandName").value(DEFAULT_COMMAND_NAME.toString()))
//            .andExpect(jsonPath("$.message").value(DEFAULT_MESSAGE.toString()));
//    }
//
//    @Test
//    @Transactional
//    public void getAllCustomCommandsByGuildIdIsEqualToSomething() throws Exception {
//        // Initialize the database
//        customCommandRepository.saveAndFlush(customCommand);
//
//        // Get all the customCommandList where guildId equals to DEFAULT_GUILD_ID
//        defaultCustomCommandShouldBeFound("guildId.equals=" + DEFAULT_GUILD_ID);
//
//        // Get all the customCommandList where guildId equals to UPDATED_GUILD_ID
//        defaultCustomCommandShouldNotBeFound("guildId.equals=" + UPDATED_GUILD_ID);
//    }
//
//    @Test
//    @Transactional
//    public void getAllCustomCommandsByGuildIdIsInShouldWork() throws Exception {
//        // Initialize the database
//        customCommandRepository.saveAndFlush(customCommand);
//
//        // Get all the customCommandList where guildId in DEFAULT_GUILD_ID or UPDATED_GUILD_ID
//        defaultCustomCommandShouldBeFound("guildId.in=" + DEFAULT_GUILD_ID + "," + UPDATED_GUILD_ID);
//
//        // Get all the customCommandList where guildId equals to UPDATED_GUILD_ID
//        defaultCustomCommandShouldNotBeFound("guildId.in=" + UPDATED_GUILD_ID);
//    }
//
//    @Test
//    @Transactional
//    public void getAllCustomCommandsByGuildIdIsNullOrNotNull() throws Exception {
//        // Initialize the database
//        customCommandRepository.saveAndFlush(customCommand);
//
//        // Get all the customCommandList where guildId is not null
//        defaultCustomCommandShouldBeFound("guildId.specified=true");
//
//        // Get all the customCommandList where guildId is null
//        defaultCustomCommandShouldNotBeFound("guildId.specified=false");
//    }
//
//    @Test
//    @Transactional
//    public void getAllCustomCommandsByGuildIdIsGreaterThanOrEqualToSomething() throws Exception {
//        // Initialize the database
//        customCommandRepository.saveAndFlush(customCommand);
//
//        // Get all the customCommandList where guildId greater than or equals to DEFAULT_GUILD_ID
//        defaultCustomCommandShouldBeFound("guildId.greaterOrEqualThan=" + DEFAULT_GUILD_ID);
//
//        // Get all the customCommandList where guildId greater than or equals to UPDATED_GUILD_ID
//        defaultCustomCommandShouldNotBeFound("guildId.greaterOrEqualThan=" + UPDATED_GUILD_ID);
//    }
//
//    @Test
//    @Transactional
//    public void getAllCustomCommandsByGuildIdIsLessThanSomething() throws Exception {
//        // Initialize the database
//        customCommandRepository.saveAndFlush(customCommand);
//
//        // Get all the customCommandList where guildId less than or equals to DEFAULT_GUILD_ID
//        defaultCustomCommandShouldNotBeFound("guildId.lessThan=" + DEFAULT_GUILD_ID);
//
//        // Get all the customCommandList where guildId less than or equals to UPDATED_GUILD_ID
//        defaultCustomCommandShouldBeFound("guildId.lessThan=" + UPDATED_GUILD_ID);
//    }
//
//
//    @Test
//    @Transactional
//    public void getAllCustomCommandsByCommandNameIsEqualToSomething() throws Exception {
//        // Initialize the database
//        customCommandRepository.saveAndFlush(customCommand);
//
//        // Get all the customCommandList where commandName equals to DEFAULT_COMMAND_NAME
//        defaultCustomCommandShouldBeFound("commandName.equals=" + DEFAULT_COMMAND_NAME);
//
//        // Get all the customCommandList where commandName equals to UPDATED_COMMAND_NAME
//        defaultCustomCommandShouldNotBeFound("commandName.equals=" + UPDATED_COMMAND_NAME);
//    }
//
//    @Test
//    @Transactional
//    public void getAllCustomCommandsByCommandNameIsInShouldWork() throws Exception {
//        // Initialize the database
//        customCommandRepository.saveAndFlush(customCommand);
//
//        // Get all the customCommandList where commandName in DEFAULT_COMMAND_NAME or UPDATED_COMMAND_NAME
//        defaultCustomCommandShouldBeFound("commandName.in=" + DEFAULT_COMMAND_NAME + "," + UPDATED_COMMAND_NAME);
//
//        // Get all the customCommandList where commandName equals to UPDATED_COMMAND_NAME
//        defaultCustomCommandShouldNotBeFound("commandName.in=" + UPDATED_COMMAND_NAME);
//    }
//
//    @Test
//    @Transactional
//    public void getAllCustomCommandsByCommandNameIsNullOrNotNull() throws Exception {
//        // Initialize the database
//        customCommandRepository.saveAndFlush(customCommand);
//
//        // Get all the customCommandList where commandName is not null
//        defaultCustomCommandShouldBeFound("commandName.specified=true");
//
//        // Get all the customCommandList where commandName is null
//        defaultCustomCommandShouldNotBeFound("commandName.specified=false");
//    }
//
//    @Test
//    @Transactional
//    public void getAllCustomCommandsByGuildrolesIsEqualToSomething() throws Exception {
//        // Initialize the database
//        GuildRoles guildroles = GuildRolesResourceIntTest.createEntity(em);
//        em.persist(guildroles);
//        em.flush();
//        customCommand.addGuildroles(guildroles);
//        customCommandRepository.saveAndFlush(customCommand);
//        Long guildrolesId = guildroles.getId();
//
//        // Get all the customCommandList where guildroles equals to guildrolesId
//        defaultCustomCommandShouldBeFound("guildrolesId.equals=" + guildrolesId);
//
//        // Get all the customCommandList where guildroles equals to guildrolesId + 1
//        defaultCustomCommandShouldNotBeFound("guildrolesId.equals=" + (guildrolesId + 1));
//    }
//
//    /**
//     * Executes the search, and checks that the default entity is returned
//     */
//    private void defaultCustomCommandShouldBeFound(String filter) throws Exception {
//        restCustomCommandMockMvc.perform(get("/api/custom-commands?sort=id,desc&" + filter))
//            .andExpect(status().isOk())
//            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
//            .andExpect(jsonPath("$.[*].id").value(hasItem(customCommand.getId().intValue())))
//            .andExpect(jsonPath("$.[*].guildId").value(hasItem(DEFAULT_GUILD_ID.intValue())))
//            .andExpect(jsonPath("$.[*].commandName").value(hasItem(DEFAULT_COMMAND_NAME)))
//            .andExpect(jsonPath("$.[*].message").value(hasItem(DEFAULT_MESSAGE.toString())));
//
//        // Check, that the count call also returns 1
//        restCustomCommandMockMvc.perform(get("/api/custom-commands/count?sort=id,desc&" + filter))
//            .andExpect(status().isOk())
//            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
//            .andExpect(content().string("1"));
//    }
//
//    /**
//     * Executes the search, and checks that the default entity is not returned
//     */
//    private void defaultCustomCommandShouldNotBeFound(String filter) throws Exception {
//        restCustomCommandMockMvc.perform(get("/api/custom-commands?sort=id,desc&" + filter))
//            .andExpect(status().isOk())
//            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
//            .andExpect(jsonPath("$").isArray())
//            .andExpect(jsonPath("$").isEmpty());
//
//        // Check, that the count call also returns 0
//        restCustomCommandMockMvc.perform(get("/api/custom-commands/count?sort=id,desc&" + filter))
//            .andExpect(status().isOk())
//            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
//            .andExpect(content().string("0"));
//    }
//
//
//    @Test
//    @Transactional
//    public void getNonExistingCustomCommand() throws Exception {
//        // Get the customCommand
//        restCustomCommandMockMvc.perform(get("/api/custom-commands/{id}", Long.MAX_VALUE))
//            .andExpect(status().isNotFound());
//    }
//
//    @Test
//    @Transactional
//    public void updateCustomCommand() throws Exception {
//        // Initialize the database
//        customCommandService.save(customCommand);
//
//        int databaseSizeBeforeUpdate = customCommandRepository.findAll().size();
//
//        // Update the customCommand
//        CustomCommand updatedCustomCommand = customCommandRepository.findById(customCommand.getId()).get();
//        // Disconnect from session so that the updates on updatedCustomCommand are not directly saved in db
//        em.detach(updatedCustomCommand);
//        updatedCustomCommand
//            .guildId(UPDATED_GUILD_ID)
//            .commandName(UPDATED_COMMAND_NAME)
//            .message(UPDATED_MESSAGE);
//
//        restCustomCommandMockMvc.perform(put("/api/custom-commands")
//            .contentType(TestUtil.APPLICATION_JSON_UTF8)
//            .content(TestUtil.convertObjectToJsonBytes(updatedCustomCommand)))
//            .andExpect(status().isOk());
//
//        // Validate the CustomCommand in the database
//        List<CustomCommand> customCommandList = customCommandRepository.findAll();
//        assertThat(customCommandList).hasSize(databaseSizeBeforeUpdate);
//        CustomCommand testCustomCommand = customCommandList.get(customCommandList.size() - 1);
//        assertThat(testCustomCommand.getGuildId()).isEqualTo(UPDATED_GUILD_ID);
//        assertThat(testCustomCommand.getCommandName()).isEqualTo(UPDATED_COMMAND_NAME);
//        assertThat(testCustomCommand.getMessage()).isEqualTo(UPDATED_MESSAGE);
//    }
//
//    @Test
//    @Transactional
//    public void updateNonExistingCustomCommand() throws Exception {
//        int databaseSizeBeforeUpdate = customCommandRepository.findAll().size();
//
//        // Create the CustomCommand
//
//        // If the entity doesn't have an ID, it will throw BadRequestAlertException
//        restCustomCommandMockMvc.perform(put("/api/custom-commands")
//            .contentType(TestUtil.APPLICATION_JSON_UTF8)
//            .content(TestUtil.convertObjectToJsonBytes(customCommand)))
//            .andExpect(status().isBadRequest());
//
//        // Validate the CustomCommand in the database
//        List<CustomCommand> customCommandList = customCommandRepository.findAll();
//        assertThat(customCommandList).hasSize(databaseSizeBeforeUpdate);
//    }
//
//    @Test
//    @Transactional
//    public void deleteCustomCommand() throws Exception {
//        // Initialize the database
//        customCommandService.save(customCommand);
//
//        int databaseSizeBeforeDelete = customCommandRepository.findAll().size();
//
//        // Delete the customCommand
//        restCustomCommandMockMvc.perform(delete("/api/custom-commands/{id}", customCommand.getId())
//            .accept(TestUtil.APPLICATION_JSON_UTF8))
//            .andExpect(status().isOk());
//
//        // Validate the database is empty
//        List<CustomCommand> customCommandList = customCommandRepository.findAll();
//        assertThat(customCommandList).hasSize(databaseSizeBeforeDelete - 1);
//    }
//
//    @Test
//    @Transactional
//    public void equalsVerifier() throws Exception {
//        TestUtil.equalsVerifier(CustomCommand.class);
//        CustomCommand customCommand1 = new CustomCommand();
//        customCommand1.setId(1L);
//        CustomCommand customCommand2 = new CustomCommand();
//        customCommand2.setId(customCommand1.getId());
//        assertThat(customCommand1).isEqualTo(customCommand2);
//        customCommand2.setId(2L);
//        assertThat(customCommand1).isNotEqualTo(customCommand2);
//        customCommand1.setId(null);
//        assertThat(customCommand1).isNotEqualTo(customCommand2);
//    }
//}
