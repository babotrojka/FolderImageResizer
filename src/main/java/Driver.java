import net.coobird.thumbnailator.Thumbnails;
import org.imgscalr.Scalr;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * Takes content of given folder and resizes it to chosen size using ImgScalr library
 * Size can be passed through args. Default size is 1920
 */
public class Driver {
    static int imgSize = 1920;

    public static void main(String[] args) {
        Path folderPath;

        if(args.length > 1) {
            System.err.println("Please provide one argument meaning folder path!");
            return;
        }

        if(args.length == 1) {
            try {
                imgSize = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                System.err.println("Please provide int!");
                return;
            }
        }

        JFileChooser jfc = new JFileChooser();
        jfc.setDialogTitle("Choose your directory!");
        jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int ret = jfc.showOpenDialog(null);
        if(ret == JFileChooser.CANCEL_OPTION) return;
        folderPath = jfc.getSelectedFile().toPath();

        try {
            Files.walkFileTree(folderPath, new SimpleFileVisitor<>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    BufferedImage image = ImageIO.read(file.toFile());
                    ImageIO.write(resizeImageThumbnail(image), "png", file.toFile());
                    //Thumbnails.of(image).size(1920, 1280).toFile(file.toFile());
                    System.out.println("Resizing: " + file.getFileName());
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Finished!");
    }

    private static BufferedImage resizeImageScalr(BufferedImage image) {
        return Scalr.resize(image, imgSize);
    }

    private static BufferedImage resizeImageThumbnail(BufferedImage image) throws IOException {
        return Thumbnails.of(image).size(640, 480).asBufferedImage();
    }
}
