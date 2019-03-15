//package com.trievosoftware.application.web.rest;
//
//import com.trievosoftware.application.SiaApp;
//
//import com.trievosoftware.application.domain.Songs;
//import com.trievosoftware.application.domain.Playlist;
//import com.trievosoftware.application.repository.SongsRepository;
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
// * Test class for the SongsResource REST controller.
// *
// * @see SongsResource
// */
//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = SiaApp.class)
//public class SongsResourceIntTest {
//
//    private static final String DEFAULT_SONG_NAME = "AAAAAAAAAA";
//    private static final String UPDATED_SONG_NAME = "BBBBBBBBBB";
//
//    private static final String DEFAULT_SONG_QUERY = "AAAAAAAAAA";
//    private static final String UPDATED_SONG_QUERY = "BBBBBBBBBB";
//
//    private static final String DEFAULT_PLAYLIST_NAME = "AAAAAAAAAA";
//    private static final Playlist DEFAULT_PLAYLIST = new Playlist(DEFAULT_PLAYLIST_NAME);
//
//    @Autowired
//    private SongsRepository songsRepository;
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
//    private MockMvc restSongsMockMvc;
//
//    private Songs songs;
//
//    @Before
//    public void setup() {
//        MockitoAnnotations.initMocks(this);
//        final SongsResource songsResource = new SongsResource(songsRepository);
//        this.restSongsMockMvc = MockMvcBuilders.standaloneSetup(songsResource)
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
//    public static Songs createEntity(EntityManager em) {
//
//        // Add required entity
//        Playlist playlist = PlaylistResourceIntTest.createEntity(em);
//        em.persist(playlist);
//        em.flush();
//        Songs songs = new Songs(DEFAULT_SONG_NAME, DEFAULT_PLAYLIST)
//            .songQuery(DEFAULT_SONG_QUERY);
//        songs.setPlaylist(playlist);
//        return songs;
//    }
//
//    @Before
//    public void initTest() {
//        songs = createEntity(em);
//    }
//
//    @Test
//    @Transactional
//    public void createSongs() throws Exception {
//        int databaseSizeBeforeCreate = songsRepository.findAll().size();
//
//        // Create the Songs
//        restSongsMockMvc.perform(post("/api/songs")
//            .contentType(TestUtil.APPLICATION_JSON_UTF8)
//            .content(TestUtil.convertObjectToJsonBytes(songs)))
//            .andExpect(status().isCreated());
//
//        // Validate the Songs in the database
//        List<Songs> songsList = songsRepository.findAll();
//        assertThat(songsList).hasSize(databaseSizeBeforeCreate + 1);
//        Songs testSongs = songsList.get(songsList.size() - 1);
//        assertThat(testSongs.getSongName()).isEqualTo(DEFAULT_SONG_NAME);
//        assertThat(testSongs.getSongQuery()).isEqualTo(DEFAULT_SONG_QUERY);
//    }
//
//    @Test
//    @Transactional
//    public void createSongsWithExistingId() throws Exception {
//        int databaseSizeBeforeCreate = songsRepository.findAll().size();
//
//        // Create the Songs with an existing ID
//        songs.setId(1L);
//
//        // An entity with an existing ID cannot be created, so this API call must fail
//        restSongsMockMvc.perform(post("/api/songs")
//            .contentType(TestUtil.APPLICATION_JSON_UTF8)
//            .content(TestUtil.convertObjectToJsonBytes(songs)))
//            .andExpect(status().isBadRequest());
//
//        // Validate the Songs in the database
//        List<Songs> songsList = songsRepository.findAll();
//        assertThat(songsList).hasSize(databaseSizeBeforeCreate);
//    }
//
//    @Test
//    @Transactional
//    public void checkSongQueryIsRequired() throws Exception {
//        int databaseSizeBeforeTest = songsRepository.findAll().size();
//        // set the field null
//        songs.setSongQuery(null);
//
//        // Create the Songs, which fails.
//
//        restSongsMockMvc.perform(post("/api/songs")
//            .contentType(TestUtil.APPLICATION_JSON_UTF8)
//            .content(TestUtil.convertObjectToJsonBytes(songs)))
//            .andExpect(status().isBadRequest());
//
//        List<Songs> songsList = songsRepository.findAll();
//        assertThat(songsList).hasSize(databaseSizeBeforeTest);
//    }
//
//    @Test
//    @Transactional
//    public void getAllSongs() throws Exception {
//        // Initialize the database
//        songsRepository.saveAndFlush(songs);
//
//        // Get all the songsList
//        restSongsMockMvc.perform(get("/api/songs?sort=id,desc"))
//            .andExpect(status().isOk())
//            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
//            .andExpect(jsonPath("$.[*].id").value(hasItem(songs.getId().intValue())))
//            .andExpect(jsonPath("$.[*].songName").value(hasItem(DEFAULT_SONG_NAME.toString())))
//            .andExpect(jsonPath("$.[*].songQuery").value(hasItem(DEFAULT_SONG_QUERY.toString())));
//    }
//
//    @Test
//    @Transactional
//    public void getSongs() throws Exception {
//        // Initialize the database
//        songsRepository.saveAndFlush(songs);
//
//        // Get the songs
//        restSongsMockMvc.perform(get("/api/songs/{id}", songs.getId()))
//            .andExpect(status().isOk())
//            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
//            .andExpect(jsonPath("$.id").value(songs.getId().intValue()))
//            .andExpect(jsonPath("$.songName").value(DEFAULT_SONG_NAME.toString()))
//            .andExpect(jsonPath("$.songQuery").value(DEFAULT_SONG_QUERY.toString()));
//    }
//
//    @Test
//    @Transactional
//    public void getNonExistingSongs() throws Exception {
//        // Get the songs
//        restSongsMockMvc.perform(get("/api/songs/{id}", Long.MAX_VALUE))
//            .andExpect(status().isNotFound());
//    }
//
//    @Test
//    @Transactional
//    public void updateSongs() throws Exception {
//        // Initialize the database
//        songsRepository.saveAndFlush(songs);
//
//        int databaseSizeBeforeUpdate = songsRepository.findAll().size();
//
//        // Update the songs
//        Songs updatedSongs = songsRepository.findById(songs.getId()).get();
//        // Disconnect from session so that the updates on updatedSongs are not directly saved in db
//        em.detach(updatedSongs);
//        updatedSongs
//            .songName(UPDATED_SONG_NAME)
//            .songQuery(UPDATED_SONG_QUERY);
//
//        restSongsMockMvc.perform(put("/api/songs")
//            .contentType(TestUtil.APPLICATION_JSON_UTF8)
//            .content(TestUtil.convertObjectToJsonBytes(updatedSongs)))
//            .andExpect(status().isOk());
//
//        // Validate the Songs in the database
//        List<Songs> songsList = songsRepository.findAll();
//        assertThat(songsList).hasSize(databaseSizeBeforeUpdate);
//        Songs testSongs = songsList.get(songsList.size() - 1);
//        assertThat(testSongs.getSongName()).isEqualTo(UPDATED_SONG_NAME);
//        assertThat(testSongs.getSongQuery()).isEqualTo(UPDATED_SONG_QUERY);
//    }
//
//    @Test
//    @Transactional
//    public void updateNonExistingSongs() throws Exception {
//        int databaseSizeBeforeUpdate = songsRepository.findAll().size();
//
//        // Create the Songs
//
//        // If the entity doesn't have an ID, it will throw BadRequestAlertException
//        restSongsMockMvc.perform(put("/api/songs")
//            .contentType(TestUtil.APPLICATION_JSON_UTF8)
//            .content(TestUtil.convertObjectToJsonBytes(songs)))
//            .andExpect(status().isBadRequest());
//
//        // Validate the Songs in the database
//        List<Songs> songsList = songsRepository.findAll();
//        assertThat(songsList).hasSize(databaseSizeBeforeUpdate);
//    }
//
//    @Test
//    @Transactional
//    public void deleteSongs() throws Exception {
//        // Initialize the database
//        songsRepository.saveAndFlush(songs);
//
//        int databaseSizeBeforeDelete = songsRepository.findAll().size();
//
//        // Delete the songs
//        restSongsMockMvc.perform(delete("/api/songs/{id}", songs.getId())
//            .accept(TestUtil.APPLICATION_JSON_UTF8))
//            .andExpect(status().isOk());
//
//        // Validate the database is empty
//        List<Songs> songsList = songsRepository.findAll();
//        assertThat(songsList).hasSize(databaseSizeBeforeDelete - 1);
//    }
//
//    @Test
//    @Transactional
//    public void equalsVerifier() throws Exception {
//        TestUtil.equalsVerifier(Songs.class);
//        Songs songs1 = new Songs(DEFAULT_SONG_NAME, DEFAULT_PLAYLIST);
//        songs1.setId(1L);
//        Songs songs2 = new Songs(DEFAULT_SONG_NAME, DEFAULT_PLAYLIST);
//        songs2.setId(songs1.getId());
//        assertThat(songs1).isEqualTo(songs2);
//        songs2.setId(2L);
//        assertThat(songs1).isNotEqualTo(songs2);
//        songs1.setId(null);
//        assertThat(songs1).isNotEqualTo(songs2);
//    }
//}
