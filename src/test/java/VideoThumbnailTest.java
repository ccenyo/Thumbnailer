import exceptions.VideoThumbnailException;
import org.junit.jupiter.api.*;

import java.io.File;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class VideoThumbnailTest {

    public String destinationPath;

    @BeforeAll
    public void init() {
        destinationPath = System.getProperty("java.io.tmpdir") + "VideoThumbnailTest/";
        new File(destinationPath).mkdirs();
    }

    @AfterAll
    public void after() {
        new File(destinationPath).delete();
    }

    @Test
    public void testVideoThumbnailPortrait () throws VideoThumbnailException {
       var file = new VideoThumbnail(new File("src/test/resources/portrait.mp4"), destinationPath)
                .automaticDimensions()
                .generate();
        Assertions.assertNotNull(file);
    }

    @Test
    public void testVideoThumbnailLandscape () throws VideoThumbnailException {
        var file = new VideoThumbnail(new File("src/test/resources/landscape.mp4"), destinationPath)
                .automaticDimensions()
                .generate();
        Assertions.assertNotNull(file);
    }

    @Test
    public void testVideoCustomThumbnailPortrait () throws VideoThumbnailException {
        var file = new VideoThumbnail(new File("src/test/resources/portrait.mp4"), destinationPath)
                .setNumberOfFrame(4)
                .setImageDivisor(new ImageDivisor(1,4))
                .generate();
        Assertions.assertNotNull(file);
    }

    @Test
    public void testVideoErrorDestinationPathNotExists () {
        Assertions.assertThrows(VideoThumbnailException.class, () -> {
            new VideoThumbnail(new File("src/test/resources/portrait.mp4"), "destinationPath")
                    .generate();
        });
    }

    @Test
    public void testVideoErrorDestinationPathNull () {
        Assertions.assertThrows(VideoThumbnailException.class, () -> {
            new VideoThumbnail(new File("src/test/resources/portrait.mp4"), null)
                    .generate();
        });
    }
}
