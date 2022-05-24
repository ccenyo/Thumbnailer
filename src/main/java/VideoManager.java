import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.Java2DFrameConverter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class VideoManager {
    private final String tempFolderName = "thumbnailer";

    public enum ORIENTATION {
        LANDSCAPE,
        PORTRAIT
    }

    public ORIENTATION getVideoOrientation(File file) {
        FFmpegFrameGrabber frameGrabber = new FFmpegFrameGrabber(file);
        try {
            frameGrabber.start();
            int width = frameGrabber.getImageWidth();
            int height = frameGrabber.getImageHeight();
            frameGrabber.stop();
            return width > height ? ORIENTATION.LANDSCAPE : ORIENTATION.PORTRAIT;
        } catch (FrameGrabber.Exception e) {
            e.printStackTrace();
        }
        return ORIENTATION.PORTRAIT;
    }

    public  List<String> getFramesFromVideo(File file, int numberOfFrame){
        FFmpegFrameGrabber frameGrabber = new FFmpegFrameGrabber(file);
        List<String> picturesPaths = new ArrayList<>();
        try {
            frameGrabber.start();

            Java2DFrameConverter aa = new Java2DFrameConverter();

            int totalNumberOfFrames = frameGrabber.getLengthInFrames();

            var frameInt = totalNumberOfFrames/numberOfFrame;

            String tmpdir = System.getProperty("java.io.tmpdir");
            new File(tmpdir +"/"+tempFolderName+"/").mkdirs();

            for (var i = frameInt; i < totalNumberOfFrames; i += frameInt - 1) {
                int index = i;
                Frame f;
                do {

                    frameGrabber.setFrameNumber(index);
                    f = frameGrabber.grabKeyFrame() ;
                    index --;

                }while (f == null && index >= 0);

                if(f != null) {
                    BufferedImage bi = aa.convert(f);

                    String result = tmpdir +"/"+tempFolderName+"/"+ System.currentTimeMillis() + ".png";
                    try {
                        ImageIO.write(bi, "png", new File(result));
                        picturesPaths.add(result);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
            frameGrabber.stop();
        } catch (FrameGrabber.Exception e) {
            e.printStackTrace();
        }

        return picturesPaths;
    }

}
