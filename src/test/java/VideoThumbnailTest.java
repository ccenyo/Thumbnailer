import exceptions.VideoThumbnailException;
import org.junit.jupiter.api.*;

import java.io.File;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class VideoThumbnailTest {

    public File destinationPath;

    @AfterEach
    public void after() {
        if(destinationPath != null && destinationPath.exists()) {
            destinationPath.delete();
        }
    }

    @Test
    public void testVideoThumbnailPortrait () throws VideoThumbnailException {
       var file = new VideoThumbnail(new File("src/test/resources/portrait.mp4"))
                .automaticDimensions()
                .generate();
        destinationPath = file;
        Assertions.assertNotNull(file);
    }

    @Test
    public void testVideoThumbnailLandscape () throws VideoThumbnailException {
        var file = new VideoThumbnail(new File("src/test/resources/landscape.mp4"))
                .automaticDimensions()
                .generate();
        destinationPath = file;
        Assertions.assertNotNull(file);
    }

    @Test
    public void testVideoCustomThumbnailPortrait () throws VideoThumbnailException {
        var file = new VideoThumbnail(new File("src/test/resources/portrait.mp4"))
                .setNumberOfFrame(4)
                .setImageDivisor(new ImageDivisor(1,4))
                .generate();
        destinationPath = file;
        Assertions.assertNotNull(file);
    }

    @Test
    public void testVideoErrorDestinationPathNotExists () {
        Assertions.assertThrows(VideoThumbnailException.class, () -> {
            new VideoThumbnail(new File("src/test/resources/portrait.mp4"))
                    .setDestinationFolderPath("destinationPath")
                    .generate();
        });
    }

    @Test
    public void testVideoErrorDestinationPathNull () {
        Assertions.assertThrows(VideoThumbnailException.class, () -> {
            new VideoThumbnail(new File("src/test/resources/portrait.mp4"))
                    .setDestinationFolderPath(null)
                    .generate();
        });
    }
}
