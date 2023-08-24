package ch.puzzle.okr.controller;

import ch.puzzle.okr.service.OverviewService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WithMockUser(value = "spring")
@ExtendWith(MockitoExtension.class)
@WebMvcTest(OverviewController.class)
public class OverviewControllerIT {
    @Autowired
    private MockMvc mvc;
    @MockBean
    private OverviewService overviewService;

}
