package exceptions;

public class VideoThumbnailException extends Exception {
    public VideoThumbnailException() {
    }

    public VideoThumbnailException(String message) {
        super(message);
    }

    public VideoThumbnailException(Throwable cause) {
        super(cause);
    }
}
