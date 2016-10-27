package io.reneses.tela.modules.instagram.models;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.reneses.tela.modules.instagram.api.responses.MediaResponse;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class MediaTest {

    List<Media> media;

    @Before
    public void setUp() throws Exception {
        File file = new File(MediaTest.class.getResource("/instagram/media.json").toURI());
        media = new ObjectMapper().readValue(file, MediaResponse.class).getMedia();
        assertNotNull(media);
        assertEquals(2, media.size());
    }

    @Test
    public void testImageUnwrap() throws Exception {

        Media image = media.get(0);

        assertEquals(3, image.getNumberOfComments());

        Comment caption = image.getCaption();
        assertEquals("Inside le truc #foodtruck", caption.getText());
        assertEquals(26621408, caption.getId());
        assertEquals("jack", caption.getUser().getUsername());
        assertEquals("Jack", caption.getUser().getFullName());
        assertEquals(1, caption.getUser().getId());

        assertEquals("http://instagr.am/p/BWrVZ/", image.getLink());

        assertEquals("jack", image.getUser().getUsername());
        assertEquals("Jack", image.getUser().getFullName());
        assertEquals("http://distillery.s3.amazonaws.com/profiles/profile_66_75sq.jpg", image.getUser().getPicture());
        assertEquals(1, image.getUser().getId());

        assertEquals(new Date(1296710327), image.getCreatedTime());

        assertEquals(Media.IMAGE, image.getType());
        assertTrue(image.isImage());

        assertEquals("Earlybird", image.getFilter());

        assertEquals("1", image.getId());

        assertEquals(37.778720183610183, image.getLocation().getLatitude(), 0.00001);
        assertEquals(-122.3962783813477, image.getLocation().getLongitude(), 0.00001);
        assertEquals(520640, image.getLocation().getId());
        assertEquals("", image.getLocation().getStreetAddress());
        assertEquals("Le Truc", image.getLocation().getName());

        assertTrue(image.getTags().contains("foodtruck"));

        assertTrue(image.getTaggedUsers().isEmpty());

        Map<String, MediaResource> images = image.getImages();
        assertTrue(images.containsKey("low_resolution"));
        assertEquals(306, images.get("low_resolution").getWidth());
        assertEquals(306, images.get("low_resolution").getHeight());
        assertEquals("http://distillery.s3.amazonaws.com/media/2011/02/02/6ea7baea55774c5e81e7e3e1f6e791a7_6.jpg",
                images.get("low_resolution").getUrl());
        assertTrue(images.containsKey("low_resolution"));
        assertEquals(150, images.get("thumbnail").getWidth());
        assertEquals(150, images.get("thumbnail").getHeight());
        assertEquals("http://distillery.s3.amazonaws.com/media/2011/02/02/6ea7baea55774c5e81e7e3e1f6e791a7_5.jpg",
                images.get("thumbnail").getUrl());
        assertTrue(images.containsKey("low_resolution"));
        assertEquals(612, images.get("standard_resolution").getWidth());
        assertEquals(612, images.get("standard_resolution").getHeight());
        assertEquals("http://distillery.s3.amazonaws.com/media/2011/02/02/6ea7baea55774c5e81e7e3e1f6e791a7_7.jpg",
                images.get("standard_resolution").getUrl());

        assertTrue(image.getVideos().isEmpty());

        assertEquals(15, image.getNumberOfLikes());

    }

    @Test
    public void testVideoUnwrap() throws Exception {

        Media video = media.get(1);

        assertEquals(2, video.getNumberOfComments());

        assertNull(video.getCaption());

        assertEquals("http://instagr.am/p/D/", video.getLink());

        assertEquals("jack", video.getUser().getUsername());
        assertEquals("Jack", video.getUser().getFullName());
        assertEquals("http://distillery.s3.amazonaws.com/profiles/profile_66_75sq.jpg", video.getUser().getPicture());
        assertEquals(1, video.getUser().getId());

        assertEquals(new Date(1279340983), video.getCreatedTime());

        assertEquals(Media.VIDEO, video.getType());
        assertTrue(video.isVideo());

        assertEquals("Vesper", video.getFilter());

        assertEquals("363839373298", video.getId());

        assertNull(video.getLocation());

        assertNotNull(video.getTags());
        assertTrue(video.getTags().isEmpty());

        assertNotNull(video.getTaggedUsers());
        assertTrue(video.getTaggedUsers().isEmpty());

        Map<String, MediaResource> images = video.getImages();
        assertTrue(images.containsKey("low_resolution"));
        assertEquals(306, images.get("low_resolution").getWidth());
        assertEquals(306, images.get("low_resolution").getHeight());
        assertEquals("http://distilleryimage2.ak.instagram.com/11f75f1cd9cc11e2a0fd22000aa8039a_6.jpg",
                images.get("low_resolution").getUrl());
        assertTrue(images.containsKey("low_resolution"));
        assertEquals(150, images.get("thumbnail").getWidth());
        assertEquals(150, images.get("thumbnail").getHeight());
        assertEquals("http://distilleryimage2.ak.instagram.com/11f75f1cd9cc11e2a0fd22000aa8039a_5.jpg",
                images.get("thumbnail").getUrl());
        assertTrue(images.containsKey("low_resolution"));
        assertEquals(612, images.get("standard_resolution").getWidth());
        assertEquals(612, images.get("standard_resolution").getHeight());
        assertEquals("http://distilleryimage2.ak.instagram.com/11f75f1cd9cc11e2a0fd22000aa8039a_7.jpg",
                images.get("standard_resolution").getUrl());

        Map<String, MediaResource> videos = video.getVideos();
        assertTrue(videos.containsKey("low_resolution"));
        assertEquals(480, videos.get("low_resolution").getWidth());
        assertEquals(480, videos.get("low_resolution").getHeight());
        assertEquals("http://distilleryvesper9-13.ak.instagram.com/090d06dad9cd11e2aa0912313817975d_102.mp4",
                videos.get("low_resolution").getUrl());
        assertTrue(videos.containsKey("standard_resolution"));
        assertEquals(640, videos.get("standard_resolution").getWidth());
        assertEquals(640, videos.get("standard_resolution").getHeight());
        assertEquals("http://distilleryvesper9-13.ak.instagram.com/090d06dad9cd11e2aa0912313817975d_101.mp4",
                videos.get("standard_resolution").getUrl());

        assertEquals(1, video.getNumberOfLikes());

    }

}