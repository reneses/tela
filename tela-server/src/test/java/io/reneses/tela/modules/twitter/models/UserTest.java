package io.reneses.tela.modules.twitter.models;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertEquals;

public class UserTest {

    @Test
    public void deserialize() throws Exception {
        File file = new File(User.class.getResource("/twitter/self.json").toURI());
        User self = new ObjectMapper().readValue(file, User.class);
        assertEquals("Tela Framework", self.getName());
        assertEquals("tela", self.getScreenName());
        assertEquals("tela.jpg", self.getPicture());
        assertEquals(550, self.getNumberOfFollowers());
        assertEquals(152, self.getNumberOfFollowing());
        assertEquals(1, self.getId());
        assertEquals("http://reneses.io", self.getUrl());
    }
}