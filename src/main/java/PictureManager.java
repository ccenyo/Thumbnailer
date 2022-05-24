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
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class PictureManager {
    public File mergeSquarePicture(List<String> files, String folderPath, int lines, int columns) {
        try {
            List<BufferedImage> images = files.stream().map(f -> {
                try {
                    return ImageIO.read(new File(f));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }).collect(Collectors.toList());
            BufferedImage concatImage = computeConcatImage(lines, columns, images);
            return computeImageFile(folderPath, concatImage);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private File computeImageFile(String folderPath, BufferedImage concatImage) throws IOException {
        File imageFile = new File(folderPath +"/"+System.currentTimeMillis()+".jpeg");
        OutputStream outputStream = new FileOutputStream(imageFile);

        float imageQuality = 0.7f;
        Iterator<ImageWriter> imageWriters = ImageIO.getImageWritersByFormatName("jpeg");

        if (!imageWriters.hasNext())
            throw new IllegalStateException("Writers Not Found!!");

        ImageWriter imageWriter = imageWriters.next();
        ImageOutputStream imageOutputStream = ImageIO.createImageOutputStream(outputStream);
        imageWriter.setOutput(imageOutputStream);

        ImageWriteParam imageWriteParam = imageWriter.getDefaultWriteParam();

        imageWriteParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        imageWriteParam.setCompressionQuality(imageQuality);
        imageWriter.write(null, new IIOImage(concatImage, null, null), imageWriteParam);

        outputStream.close();
        imageOutputStream.close();
        imageWriter.dispose();
        return imageFile;
    }

    private BufferedImage computeConcatImage(int lines, int columns, List<BufferedImage> images) {
        var dimensions =  computeNewImageDimensions(images, lines, columns);

        int widthCurr = 0;
        int heightCurr = 0;
        BufferedImage concatImage = new BufferedImage( dimensions.width(), dimensions.height(), BufferedImage.TYPE_INT_RGB);
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
        return concatImage;
    }

    private ImageDimension computeNewImageDimensions(List<BufferedImage> images, int lines, int columns) {
        int widthTotal = 0;
        int maxheight = 0;

        for (int i = 0, imagesSize = images.size(); i < imagesSize; i++) {
            BufferedImage bufferedImage = images.get(i);
            if (i < columns) {
                widthTotal += bufferedImage.getWidth();
            }

            if (i % columns == 0) {
                maxheight += bufferedImage.getHeight();
            }
        }

        return new ImageDimension(widthTotal, maxheight);
    }
}
