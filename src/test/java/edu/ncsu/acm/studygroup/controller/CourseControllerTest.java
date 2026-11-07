package edu.ncsu.acm.studygroup.controller;

import edu.ncsu.acm.studygroup.entity.Course;
import edu.ncsu.acm.studygroup.entity.Section;
import edu.ncsu.acm.studygroup.service.CourseService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CourseControllerTest {

    @Test
    public void testGetCourseSections() throws Exception {
        // 1. Manually create a fake CourseService without Spring annotations
        CourseService courseService = Mockito.mock(CourseService.class);

        // 2. Inject it directly into a new CourseController
        CourseController courseController = new CourseController(courseService);

        // 3. Setup MockMvc in standalone mode (bypasses the missing Spring Boot context!)
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(courseController).build();

        // 4. Create mock data
        Course course = new Course("CSC 201", "Introduction to Artificial Intelligence", "Computer Science");
        course.setCourseId(1L);

        Section section = new Section("001", "Dr. Smith", "Fall 2026", course);
        section.setSectionId(1L);

        // 5. Tell the fake service what to return
        when(courseService.getSectionsForCourseId(1L)).thenReturn(List.of(section));

        // 6. Perform the API call and verify the JSON output
        mockMvc.perform(get("/api/courses/1/sections"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].sectionNumber").value("001"))
                .andExpect(jsonPath("$[0].instructor").value("Dr. Smith"));
    }
}