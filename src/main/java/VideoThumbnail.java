import exceptions.VideoThumbnailException;

import java.io.File;
import java.util.Optional;

public class VideoThumbnail {
    private final PictureManager pictureManager;
    private final VideoManager videoManager;
    private String destinationFolderPath = "./";
    private final File file;

    private Integer numberOfFrame = 4;
    private ImageDivisor imageDivisor;
    private boolean useAutomaticDimensions = false;

    public VideoThumbnail(File file) {
        this.file = file;
        pictureManager = new PictureManager();
        videoManager = new VideoManager();
        imageDivisor = new ImageDivisor(1, 3);
    }

    public VideoThumbnail setNumberOfFrame(Integer numberOfFrame) {
        this.numberOfFrame = numberOfFrame;
        return this;
    }

    public VideoThumbnail setImageDivisor(ImageDivisor imageDivisor) {
        this.imageDivisor = imageDivisor;
        return this;
    }

    public VideoThumbnail automaticDimensions() {
        this.useAutomaticDimensions = true;
        return this;
    }

    public VideoThumbnail setDestinationFolderPath(String destinationFolderPath) {
        this.destinationFolderPath = destinationFolderPath;
        return this;
    }

    public File generate() throws VideoThumbnailException {
        validate();
        handleAutomaticValues();
        var paths = videoManager.getFramesFromVideo(file, numberOfFrame);
        var file = pictureManager.mergeSquarePicture(paths, destinationFolderPath, imageDivisor.numberOfLines(), imageDivisor.numberOfColumns());
        paths.forEach(f -> new File(f).delete());
        return file;
    }

    private void validate() throws VideoThumbnailException {
        Optional.ofNullable(numberOfFrame).orElseThrow(() -> new VideoThumbnailException("Number of frame can not be null"));
        Optional.ofNullable(imageDivisor).orElseThrow(() -> new VideoThumbnailException("Image divisor can not be null"));
        Optional.ofNullable(destinationFolderPath).orElseThrow(() -> new VideoThumbnailException("Destination folder can not be null"));
        if(new File(destinationFolderPath).isFile() || !new File(destinationFolderPath).exists()) {
            throw new VideoThumbnailException("the destination path is not valid or does not exist");
        }
    }

    private void handleAutomaticValues() {
        if(this.useAutomaticDimensions) {
            var orientation = videoManager.getVideoOrientation(file);
            if(orientation.equals(VideoManager.ORIENTATION.PORTRAIT)) {
                this.numberOfFrame = 3;
                this.imageDivisor = new ImageDivisor(1, 3);
            } else {
                this.numberOfFrame = 6;
                this.imageDivisor = new ImageDivisor(2, 3);
            }
        }
    }

}
