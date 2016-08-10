package nl.deknik.application;

import nl.deknik.cardgames.application.CardGameApplication;
import nl.deknik.cardgames.controller.HomeController;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.ApplicationPath;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class CardGameApplicationTest {

    private CardGameApplication cardGameApplication;

    @Before
    public void setUp() throws Exception {
        this.cardGameApplication = new CardGameApplication();

    }

    @Test
    public void testGetClasses() throws Exception {
        final Set<Class<?>> classes = this.cardGameApplication.getClasses();
        assertNotNull(classes);
        assertEquals(2, classes.size());
        assertThat("contains the JacksonFeature", classes.contains(JacksonFeature.class));
        assertThat("contains the HomeController", classes.contains(HomeController.class));
    }

    @Test
    public void testAnnotations() throws Exception {


        assertThat("The Class has the annotation ApplicationPath ", this.cardGameApplication.getClass()
                                                                                        .isAnnotationPresent(
                                                                                                ApplicationPath.class));
        final ApplicationPath annotation = this.cardGameApplication.getClass()
                                                               .getAnnotation(ApplicationPath.class);
        assertThat("The containing string is 'service'", annotation.value(), is("service"));
    }
}