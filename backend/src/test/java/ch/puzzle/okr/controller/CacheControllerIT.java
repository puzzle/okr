package ch.puzzle.okr.controller;

import ch.puzzle.okr.service.CacheService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WithMockUser(value = "spring")
@ExtendWith(MockitoExtension.class)
@WebMvcTest(CacheController.class)
class CacheControllerIT {
    @Autowired
    private MockMvc mvc;
    @MockBean
    private CacheService cacheService;

    @Test
    void shouldEmptyUsersCache() throws Exception {
        mvc.perform(post("/api/v2/caches/emptyUsersCache").with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
        verify(cacheService, times(1)).emptyUsersCache();
    }

    @Test
    void shouldEmptyAllCaches() throws Exception {
        mvc.perform(post("/api/v2/caches/emptyAllCaches").with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
        verify(cacheService, times(1)).emptyAllCaches();
    }
}
