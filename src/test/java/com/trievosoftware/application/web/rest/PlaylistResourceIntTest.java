//package com.trievosoftware.application.web.rest;
//
//import com.trievosoftware.application.SiaApp;
//
//import com.trievosoftware.application.domain.Playlist;
//import com.trievosoftware.application.domain.GuildMusicSettings;
//import com.trievosoftware.application.repository.PlaylistRepository;
//import com.trievosoftware.application.service.PlaylistService;
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
// * Test class for the PlaylistResource REST controller.
// *
// * @see PlaylistResource
// */
//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = SiaApp.class)
//public class PlaylistResourceIntTest {
//
//    private static final Long DEFAULT_GUILD_ID = 1L;
//    private static final Long UPDATED_GUILD_ID = 2L;
//
//    private static final Long DEFAULT_USER_ID = 1L;
//    private static final Long UPDATED_USER_ID = 2L;
//
//    private static final String DEFAULT_PLAYLIST_NAME = "AAAAAAAAAA";
//    private static final String UPDATED_PLAYLIST_NAME = "BBBBBBBBBB";
//
//    @Autowired
//    private PlaylistService playlistService;
//
//    @Autowired
//    PlaylistRepository playlistRepository;
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
//    private MockMvc restPlaylistMockMvc;
//
//    private Playlist playlist;
//
//    @Before
//    public void setup() {
//        MockitoAnnotations.initMocks(this);
//        final PlaylistResource playlistResource = new PlaylistResource(playlistService);
//        this.restPlaylistMockMvc = MockMvcBuilders.standaloneSetup(playlistResource)
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
//    public static Playlist createEntity(EntityManager em) {
//        Playlist playlist = new Playlist()
//            .guildId(DEFAULT_GUILD_ID)
//            .userId(DEFAULT_USER_ID)
//            .playlistName(DEFAULT_PLAYLIST_NAME);
//        // Add required entity
//        GuildMusicSettings guildMusicSettings = GuildMusicSettingsResourceIntTest.createEntity(em);
//        em.persist(guildMusicSettings);
//        em.flush();
//        playlist.setGuildmusicsettings(guildMusicSettings);
//        return playlist;
//    }
//
//    @Before
//    public void initTest() {
//        playlist = createEntity(em);
//    }
//
//    @Test
//    @Transactional
//    public void createPlaylist() throws Exception {
//        int databaseSizeBeforeCreate = playlistService.findAll().size();
//
//        // Create the Playlist
//        restPlaylistMockMvc.perform(post("/api/playlists")
//            .contentType(TestUtil.APPLICATION_JSON_UTF8)
//            .content(TestUtil.convertObjectToJsonBytes(playlist)))
//            .andExpect(status().isCreated());
//
//        // Validate the Playlist in the database
//        List<Playlist> playlistList = playlistService.findAll();
//        assertThat(playlistList).hasSize(databaseSizeBeforeCreate + 1);
//        Playlist testPlaylist = playlistList.get(playlistList.size() - 1);
//        assertThat(testPlaylist.getDiscordId()).isEqualTo(DEFAULT_GUILD_ID);
//        assertThat(testPlaylist.getUserId()).isEqualTo(DEFAULT_USER_ID);
//        assertThat(testPlaylist.getPlaylistName()).isEqualTo(DEFAULT_PLAYLIST_NAME);
//    }
//
//    @Test
//    @Transactional
//    public void createPlaylistWithExistingId() throws Exception {
//        int databaseSizeBeforeCreate = playlistService.findAll().size();
//
//        // Create the Playlist with an existing ID
//        playlist.setId(1L);
//
//        // An entity with an existing ID cannot be created, so this API call must fail
//        restPlaylistMockMvc.perform(post("/api/playlists")
//            .contentType(TestUtil.APPLICATION_JSON_UTF8)
//            .content(TestUtil.convertObjectToJsonBytes(playlist)))
//            .andExpect(status().isBadRequest());
//
//        // Validate the Playlist in the database
//        List<Playlist> playlistList = playlistService.findAll();
//        assertThat(playlistList).hasSize(databaseSizeBeforeCreate);
//    }
//
//    @Test
//    @Transactional
//    public void checkPlaylistNameIsRequired() throws Exception {
//        int databaseSizeBeforeTest = playlistService.findAll().size();
//        // set the field null
//        playlist.setPlaylistName(null);
//
//        // Create the Playlist, which fails.
//
//        restPlaylistMockMvc.perform(post("/api/playlists")
//            .contentType(TestUtil.APPLICATION_JSON_UTF8)
//            .content(TestUtil.convertObjectToJsonBytes(playlist)))
//            .andExpect(status().isBadRequest());
//
//        List<Playlist> playlistList = playlistService.findAll();
//        assertThat(playlistList).hasSize(databaseSizeBeforeTest);
//    }
//
//    @Test
//    @Transactional
//    public void getAllPlaylists() throws Exception {
//        // Initialize the database
//        playlistRepository.saveAndFlush(playlist);
//
//        // Get all the playlistList
//        restPlaylistMockMvc.perform(get("/api/playlists?sort=id,desc"))
//            .andExpect(status().isOk())
//            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
//            .andExpect(jsonPath("$.[*].id").value(hasItem(playlist.getId().intValue())))
//            .andExpect(jsonPath("$.[*].guildId").value(hasItem(DEFAULT_GUILD_ID.intValue())))
//            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID.intValue())))
//            .andExpect(jsonPath("$.[*].playlistName").value(hasItem(DEFAULT_PLAYLIST_NAME.toString())));
//    }
//
//    @Test
//    @Transactional
//    public void getPlaylist() throws Exception {
//        // Initialize the database
//        playlistRepository.saveAndFlush(playlist);
//
//        // Get the playlist
//        restPlaylistMockMvc.perform(get("/api/playlists/{id}", playlist.getId()))
//            .andExpect(status().isOk())
//            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
//            .andExpect(jsonPath("$.id").value(playlist.getId().intValue()))
//            .andExpect(jsonPath("$.guildId").value(DEFAULT_GUILD_ID.intValue()))
//            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID.intValue()))
//            .andExpect(jsonPath("$.playlistName").value(DEFAULT_PLAYLIST_NAME.toString()));
//    }
//
//    @Test
//    @Transactional
//    public void getNonExistingPlaylist() throws Exception {
//        // Get the playlist
//        restPlaylistMockMvc.perform(get("/api/playlists/{id}", Long.MAX_VALUE))
//            .andExpect(status().isNotFound());
//    }
//
//    @Test
//    @Transactional
//    public void updatePlaylist() throws Exception {
//        // Initialize the database
//        playlistRepository.saveAndFlush(playlist);
//
//        int databaseSizeBeforeUpdate = playlistService.findAll().size();
//
//        // Update the playlist
//        Playlist updatedPlaylist = playlistRepository.findById(playlist.getId()).get();
//        // Disconnect from session so that the updates on updatedPlaylist are not directly saved in db
//        em.detach(updatedPlaylist);
//        updatedPlaylist
//            .guildId(UPDATED_GUILD_ID)
//            .userId(UPDATED_USER_ID)
//            .playlistName(UPDATED_PLAYLIST_NAME);
//
//        restPlaylistMockMvc.perform(put("/api/playlists")
//            .contentType(TestUtil.APPLICATION_JSON_UTF8)
//            .content(TestUtil.convertObjectToJsonBytes(updatedPlaylist)))
//            .andExpect(status().isOk());
//
//        // Validate the Playlist in the database
//        List<Playlist> playlistList = playlistService.findAll();
//        assertThat(playlistList).hasSize(databaseSizeBeforeUpdate);
//        Playlist testPlaylist = playlistList.get(playlistList.size() - 1);
//        assertThat(testPlaylist.getDiscordId()).isEqualTo(UPDATED_GUILD_ID);
//        assertThat(testPlaylist.getUserId()).isEqualTo(UPDATED_USER_ID);
//        assertThat(testPlaylist.getPlaylistName()).isEqualTo(UPDATED_PLAYLIST_NAME);
//    }
//
//    @Test
//    @Transactional
//    public void updateNonExistingPlaylist() throws Exception {
//        int databaseSizeBeforeUpdate = playlistService.findAll().size();
//
//        // Create the Playlist
//
//        // If the entity doesn't have an ID, it will throw BadRequestAlertException
//        restPlaylistMockMvc.perform(put("/api/playlists")
//            .contentType(TestUtil.APPLICATION_JSON_UTF8)
//            .content(TestUtil.convertObjectToJsonBytes(playlist)))
//            .andExpect(status().isBadRequest());
//
//        // Validate the Playlist in the database
//        List<Playlist> playlistList = playlistService.findAll();
//        assertThat(playlistList).hasSize(databaseSizeBeforeUpdate);
//    }
//
//    @Test
//    @Transactional
//    public void deletePlaylist() throws Exception {
//        // Initialize the database
//        playlistRepository.saveAndFlush(playlist);
//
//        int databaseSizeBeforeDelete = playlistService.findAll().size();
//
//        // Delete the playlist
//        restPlaylistMockMvc.perform(delete("/api/playlists/{id}", playlist.getId())
//            .accept(TestUtil.APPLICATION_JSON_UTF8))
//            .andExpect(status().isOk());
//
//        // Validate the database is empty
//        List<Playlist> playlistList = playlistService.findAll();
//        assertThat(playlistList).hasSize(databaseSizeBeforeDelete - 1);
//    }
//
//    @Test
//    @Transactional
//    public void equalsVerifier() throws Exception {
//        TestUtil.equalsVerifier(Playlist.class);
//        Playlist playlist1 = new Playlist();
//        playlist1.setId(1L);
//        Playlist playlist2 = new Playlist();
//        playlist2.setId(playlist1.getId());
//        assertThat(playlist1).isEqualTo(playlist2);
//        playlist2.setId(2L);
//        assertThat(playlist1).isNotEqualTo(playlist2);
//        playlist1.setId(null);
//        assertThat(playlist1).isNotEqualTo(playlist2);
//    }
//}
