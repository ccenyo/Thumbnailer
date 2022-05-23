import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.Java2DFrameConverter;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class VideoThumbnail {
    public static void main(String[] args) throws FrameGrabber.Exception {
        getFrameFromVideo("D:\\p\\FuckableGirlsSite\\Aeries Steele\\0gwsrh4qnqm6eg7jgamsg_720p.mp4");

    }

    public static String getFrameFromVideo(String path) throws FrameGrabber.Exception {
        FFmpegFrameGrabber frameGrabber = new FFmpegFrameGrabber(path);
        List<String> picturesPaths = new ArrayList<>();
        frameGrabber.start();

        Java2DFrameConverter aa = new Java2DFrameConverter();

        int totalNumberOfFrames = frameGrabber.getLengthInFrames();
        int width = frameGrabber.getImageWidth();
        int height = frameGrabber.getImageHeight();

        var frameInt = totalNumberOfFrames/6;

        for (var i = frameInt; i < totalNumberOfFrames; i += frameInt - 1) {
            frameGrabber.setFrameNumber(i);
            Frame f = frameGrabber.grabKeyFrame() ;
            BufferedImage bi = aa.convert(f);

            String result =  System.currentTimeMillis() + ".png";
            try {
                ImageIO.write(bi, "png", new File(result));
                picturesPaths.add(result);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        frameGrabber.stop();

        try {
            mergeSquarePicture(picturesPaths, "merged.jpeg", 2, 3);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static  File  mergeSquarePicture(List<String> files, String fileName, int lines, int columns) throws IOException {
        int widthTotal = 0;
        int maxheight = 0;

        List<BufferedImage> images = new ArrayList<>();
        try {
            for (String file : files) {
                BufferedImage image = ImageIO.read(new File(file));
                images.add(image);
            }

            for (int i = 0, imagesSize = images.size(); i < imagesSize; i++) {
                BufferedImage bufferedImage = images.get(i);
                if (i < columns) {
                    widthTotal += bufferedImage.getWidth();
                }

                if (i % columns == 0) {
                    maxheight += bufferedImage.getHeight();
                }
            }


            int widthCurr = 0;
            int heightCurr = 0;
            BufferedImage concatImage = new BufferedImage( widthTotal,maxheight, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = concatImage.createGraphics();
            for (int i = 0; i < images.size(); i++) {
                BufferedImage bufferedImage = images.get(i);
                if (i % columns == 0) {
                    widthCurr = 0;
                    if(i != 0) {
                        heightCurr += bufferedImage.getHeight();
                    }
                }
                g2d.drawImage(bufferedImage, widthCurr, heightCurr, null);
                widthCurr += bufferedImage.getWidth();
            }

            File compressedImageFile = new File(fileName);
            OutputStream outputStream = new FileOutputStream(compressedImageFile);

            float imageQuality = 0.7f;
            Iterator<ImageWriter> imageWriters = ImageIO.getImageWritersByFormatName("jpeg");

            if (!imageWriters.hasNext())
                throw new IllegalStateException("Writers Not Found!!");

            ImageWriter imageWriter = imageWriters.next();
            ImageOutputStream imageOutputStream = ImageIO.createImageOutputStream(outputStream);
            imageWriter.setOutput(imageOutputStream);

            ImageWriteParam imageWriteParam = imageWriter.getDefaultWriteParam();

            //Set the compress quality metrics
            imageWriteParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            imageWriteParam.setCompressionQuality(imageQuality);

            //Created image
            imageWriter.write(null, new IIOImage(concatImage, null, null), imageWriteParam);

            // close all streams
            outputStream.close();
            imageOutputStream.close();
            imageWriter.dispose();
            return compressedImageFile;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static File mergePicturesSideBySide(List<String> files, String fileName) {
        int widthTotal = 0;
        int maxheight = 100;

        List<BufferedImage> images = new ArrayList<>();
        try {
            for (String file : files) {
                BufferedImage image = ImageIO.read(new File(file));
                images.add(image);
            }


            for (BufferedImage bufferedImage : images) {
                widthTotal += bufferedImage.getWidth();
                if (bufferedImage.getWidth() > maxheight) {
                    maxheight = bufferedImage.getHeight();
                }
            }


            int widthCurr = 0;
            BufferedImage concatImage = new BufferedImage( widthTotal,maxheight, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = concatImage.createGraphics();
            for (BufferedImage bufferedImage : images) {
                g2d.drawImage(bufferedImage, widthCurr, 0, null);
                widthCurr += bufferedImage.getWidth();
            }

            File compressedImageFile = new File(fileName);
            OutputStream outputStream = new FileOutputStream(compressedImageFile);

            float imageQuality = 0.7f;
            Iterator<ImageWriter> imageWriters = ImageIO.getImageWritersByFormatName("jpeg");

            if (!imageWriters.hasNext())
                throw new IllegalStateException("Writers Not Found!!");

            ImageWriter imageWriter = imageWriters.next();
            ImageOutputStream imageOutputStream = ImageIO.createImageOutputStream(outputStream);
            imageWriter.setOutput(imageOutputStream);

            ImageWriteParam imageWriteParam = imageWriter.getDefaultWriteParam();

            //Set the compress quality metrics
            imageWriteParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            imageWriteParam.setCompressionQuality(imageQuality);

            //Created image
            imageWriter.write(null, new IIOImage(concatImage, null, null), imageWriteParam);

            // close all streams
            outputStream.close();
            imageOutputStream.close();
            imageWriter.dispose();
            return compressedImageFile;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
