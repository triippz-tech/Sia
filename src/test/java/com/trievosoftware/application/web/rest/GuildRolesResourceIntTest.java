package com.trievosoftware.application.web.rest;

import com.trievosoftware.application.SiaApp;

import com.trievosoftware.application.domain.GuildRoles;
import com.trievosoftware.application.repository.GuildRolesRepository;
import com.trievosoftware.application.service.GuildRolesService;
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
 * Test class for the GuildRolesResource REST controller.
 *
 * @see GuildRolesResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SiaApp.class)
public class GuildRolesResourceIntTest {

    private static final Long DEFAULT_GUILD_ID = 1L;
    private static final Long UPDATED_GUILD_ID = 2L;

    private static final Long DEFAULT_ROLE_ID = 1L;
    private static final Long UPDATED_ROLE_ID = 2L;

    private static final String DEFAULT_ROLE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_ROLE_NAME = "BBBBBBBBBB";

    @Autowired
    private GuildRolesRepository guildRolesRepository;

    @Autowired
    private GuildRolesService guildRolesService;

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

    private MockMvc restGuildRolesMockMvc;

    private GuildRoles guildRoles;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final GuildRolesResource guildRolesResource = new GuildRolesResource(guildRolesService);
        this.restGuildRolesMockMvc = MockMvcBuilders.standaloneSetup(guildRolesResource)
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
    public static GuildRoles createEntity(EntityManager em) {
        GuildRoles guildRoles = new GuildRoles()
            .guildId(DEFAULT_GUILD_ID)
            .roleId(DEFAULT_ROLE_ID)
            .roleName(DEFAULT_ROLE_NAME);
        return guildRoles;
    }

    @Before
    public void initTest() {
        guildRoles = createEntity(em);
    }

    @Test
    @Transactional
    public void createGuildRoles() throws Exception {
        int databaseSizeBeforeCreate = guildRolesRepository.findAll().size();

        // Create the GuildRoles
        restGuildRolesMockMvc.perform(post("/api/guild-roles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(guildRoles)))
            .andExpect(status().isCreated());

        // Validate the GuildRoles in the database
        List<GuildRoles> guildRolesList = guildRolesRepository.findAll();
        assertThat(guildRolesList).hasSize(databaseSizeBeforeCreate + 1);
        GuildRoles testGuildRoles = guildRolesList.get(guildRolesList.size() - 1);
        assertThat(testGuildRoles.getGuildId()).isEqualTo(DEFAULT_GUILD_ID);
        assertThat(testGuildRoles.getRoleId()).isEqualTo(DEFAULT_ROLE_ID);
        assertThat(testGuildRoles.getRoleName()).isEqualTo(DEFAULT_ROLE_NAME);
    }

    @Test
    @Transactional
    public void createGuildRolesWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = guildRolesRepository.findAll().size();

        // Create the GuildRoles with an existing ID
        guildRoles.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restGuildRolesMockMvc.perform(post("/api/guild-roles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(guildRoles)))
            .andExpect(status().isBadRequest());

        // Validate the GuildRoles in the database
        List<GuildRoles> guildRolesList = guildRolesRepository.findAll();
        assertThat(guildRolesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkGuildIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = guildRolesRepository.findAll().size();
        // set the field null
        guildRoles.setGuildId(null);

        // Create the GuildRoles, which fails.

        restGuildRolesMockMvc.perform(post("/api/guild-roles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(guildRoles)))
            .andExpect(status().isBadRequest());

        List<GuildRoles> guildRolesList = guildRolesRepository.findAll();
        assertThat(guildRolesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkRoleIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = guildRolesRepository.findAll().size();
        // set the field null
        guildRoles.setRoleId(null);

        // Create the GuildRoles, which fails.

        restGuildRolesMockMvc.perform(post("/api/guild-roles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(guildRoles)))
            .andExpect(status().isBadRequest());

        List<GuildRoles> guildRolesList = guildRolesRepository.findAll();
        assertThat(guildRolesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllGuildRoles() throws Exception {
        // Initialize the database
        guildRolesRepository.saveAndFlush(guildRoles);

        // Get all the guildRolesList
        restGuildRolesMockMvc.perform(get("/api/guild-roles?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(guildRoles.getId().intValue())))
            .andExpect(jsonPath("$.[*].guildId").value(hasItem(DEFAULT_GUILD_ID.intValue())))
            .andExpect(jsonPath("$.[*].roleId").value(hasItem(DEFAULT_ROLE_ID.intValue())))
            .andExpect(jsonPath("$.[*].roleName").value(hasItem(DEFAULT_ROLE_NAME.toString())));
    }
    
    @Test
    @Transactional
    public void getGuildRoles() throws Exception {
        // Initialize the database
        guildRolesRepository.saveAndFlush(guildRoles);

        // Get the guildRoles
        restGuildRolesMockMvc.perform(get("/api/guild-roles/{id}", guildRoles.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(guildRoles.getId().intValue()))
            .andExpect(jsonPath("$.guildId").value(DEFAULT_GUILD_ID.intValue()))
            .andExpect(jsonPath("$.roleId").value(DEFAULT_ROLE_ID.intValue()))
            .andExpect(jsonPath("$.roleName").value(DEFAULT_ROLE_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingGuildRoles() throws Exception {
        // Get the guildRoles
        restGuildRolesMockMvc.perform(get("/api/guild-roles/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateGuildRoles() throws Exception {
        // Initialize the database
        guildRolesService.save(guildRoles);

        int databaseSizeBeforeUpdate = guildRolesRepository.findAll().size();

        // Update the guildRoles
        GuildRoles updatedGuildRoles = guildRolesRepository.findById(guildRoles.getId()).get();
        // Disconnect from session so that the updates on updatedGuildRoles are not directly saved in db
        em.detach(updatedGuildRoles);
        updatedGuildRoles
            .guildId(UPDATED_GUILD_ID)
            .roleId(UPDATED_ROLE_ID)
            .roleName(UPDATED_ROLE_NAME);

        restGuildRolesMockMvc.perform(put("/api/guild-roles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedGuildRoles)))
            .andExpect(status().isOk());

        // Validate the GuildRoles in the database
        List<GuildRoles> guildRolesList = guildRolesRepository.findAll();
        assertThat(guildRolesList).hasSize(databaseSizeBeforeUpdate);
        GuildRoles testGuildRoles = guildRolesList.get(guildRolesList.size() - 1);
        assertThat(testGuildRoles.getGuildId()).isEqualTo(UPDATED_GUILD_ID);
        assertThat(testGuildRoles.getRoleId()).isEqualTo(UPDATED_ROLE_ID);
        assertThat(testGuildRoles.getRoleName()).isEqualTo(UPDATED_ROLE_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingGuildRoles() throws Exception {
        int databaseSizeBeforeUpdate = guildRolesRepository.findAll().size();

        // Create the GuildRoles

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGuildRolesMockMvc.perform(put("/api/guild-roles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(guildRoles)))
            .andExpect(status().isBadRequest());

        // Validate the GuildRoles in the database
        List<GuildRoles> guildRolesList = guildRolesRepository.findAll();
        assertThat(guildRolesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteGuildRoles() throws Exception {
        // Initialize the database
        guildRolesService.save(guildRoles);

        int databaseSizeBeforeDelete = guildRolesRepository.findAll().size();

        // Delete the guildRoles
        restGuildRolesMockMvc.perform(delete("/api/guild-roles/{id}", guildRoles.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<GuildRoles> guildRolesList = guildRolesRepository.findAll();
        assertThat(guildRolesList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(GuildRoles.class);
        GuildRoles guildRoles1 = new GuildRoles();
        guildRoles1.setId(1L);
        GuildRoles guildRoles2 = new GuildRoles();
        guildRoles2.setId(guildRoles1.getId());
        assertThat(guildRoles1).isEqualTo(guildRoles2);
        guildRoles2.setId(2L);
        assertThat(guildRoles1).isNotEqualTo(guildRoles2);
        guildRoles1.setId(null);
        assertThat(guildRoles1).isNotEqualTo(guildRoles2);
    }
}
