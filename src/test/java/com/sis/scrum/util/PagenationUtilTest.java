package com.sis.scrum.util;

import com.sis.scrum.model.Retrospective;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
public class PagenationUtilTest {
    @InjectMocks
    PagenationUtil pagenationUtil;

    private List<Retrospective> retrospectiveList;

    @BeforeEach
    public void setUp() {
        retrospectiveList = List.of(
                new Retrospective("Retrospective 0", "Post release retrospective", LocalDate.parse("2024-05-20"), java.util.Arrays.asList("shan", "vel"), null),
                new Retrospective("Retrospective 1", "Post release retrospective", LocalDate.parse("2024-05-20"), java.util.Arrays.asList("shan", "vel"), null),
                new Retrospective("Retrospective 2", "Post release retrospective", LocalDate.parse("2024-05-20"), java.util.Arrays.asList("shan", "vel"), null),
                new Retrospective("Retrospective 3", "Post release retrospective", LocalDate.parse("2024-05-20"), java.util.Arrays.asList("shan", "vel"), null),
                new Retrospective("Retrospective 4", "Post release retrospective", LocalDate.parse("2024-05-20"), java.util.Arrays.asList("shan", "vel"), null)
        );
    }

    @Test
    public void getPage_wheneValidListpageandsizeGiven_thenReturnAsperPage() {
        List<Retrospective> retrospectiveList = pagenationUtil.getPage(this.retrospectiveList, 1, 1);
        assertNotNull(retrospectiveList);
        assertEquals(retrospectiveList.size(), 1);
    }

    @Test
    public void getPage_wheneValidListpage2andsize2Given_thenReturnAsperPage() {
        List<Retrospective> retrospectiveList = pagenationUtil.getPage(this.retrospectiveList, 2, 2);
        assertNotNull(retrospectiveList);
        assertEquals(retrospectiveList.size(), 2);
        assertEquals(retrospectiveList.get(0).getName(), "Retrospective 2");
        assertEquals(retrospectiveList.get(1).getName(), "Retrospective 3");
    }

    @Test
    public void getPage_wheneValidListpage1andsize10Given_thenReturnAsperPage() {
        List<Retrospective> actualList = pagenationUtil.getPage(this.retrospectiveList, 1, 10);
        assertNotNull(retrospectiveList);
        assertEquals(retrospectiveList.size(), actualList.size());
        assertEquals(actualList.get(0).getName(), "Retrospective 0");
        assertEquals(actualList.get(1).getName(), "Retrospective 1");
        assertEquals(actualList.get(4).getName(), "Retrospective 4");
    }
}
