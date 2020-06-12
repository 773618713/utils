package gui.ava.html.renderer;

import gui.ava.html.exception.RenderException;
import gui.ava.html.parser.DocumentHolder;
import gui.ava.html.renderer.FormatNameUtil;
import gui.ava.html.renderer.ImageRendererImpl;
import org.xhtmlrenderer.util.FSImageWriter;

import java.awt.image.BufferedImage;
import java.io.*;

/**
 * 重写ImageRendererImpl类
 * 修复保存jpg图片时变红的问题。
 *
 */
public class ImageRendererSubImpl extends ImageRendererImpl {

    public ImageRendererSubImpl(DocumentHolder documentHolder) {
        super(documentHolder);
    }

    private String getImageFormat(String filename) {
        if (this.getImageFormat() != null) {
            return this.getImageFormat();
        } else {
            return filename != null ? FormatNameUtil.formatForFilename(filename) : FormatNameUtil.getDefaultFormat();
        }
    }

    private FSImageWriter getImageWriter(String imageFormat) {
        FSImageWriter imageWriter = new FSImageWriter(imageFormat);
        imageWriter.setWriteCompressionMode(this.getWriteCompressionMode());
        imageWriter.setWriteCompressionQuality(this.getWriteCompressionQuality());
        imageWriter.setWriteCompressionType(this.getWriteCompressionType());
        return imageWriter;
    }

    public void saveImage(File file) {
        try {
            BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file));
            this.save(outputStream, file.getName(), true);
        } catch (IOException var3) {
            throw new RenderException("IOException while rendering image to " + file.getAbsolutePath(), var3);
        }
    }

    public void saveImage(String filename) {
        this.saveImage(new File(filename));
    }

    private void save(OutputStream outputStream, String filename, boolean closeStream) {
        try {
            String imageFormat = this.getImageFormat(filename);
            FSImageWriter imageWriter = this.getImageWriter(imageFormat);
            BufferedImage bufferedImage = this.getBufferedImage(getImageType(imageFormat));
            imageWriter.write(bufferedImage, outputStream);
        } catch (IOException var15) {
            throw new RenderException("IOException while rendering image", var15);
        } finally {
            if (closeStream) {
                try {
                    outputStream.close();
                } catch (IOException var14) {
                    ;
                }
            }

        }
    }

    /**
     * 获取图像类型
     * 根据图像的格式
     */
    private int getImageType(String imageFormat){
        if ("jpg".equalsIgnoreCase(imageFormat)){
            return BufferedImage.TYPE_3BYTE_BGR;
        }
        if ("bmp".equalsIgnoreCase(imageFormat)){
            return BufferedImage.TYPE_INT_RGB;
        }
        return BufferedImage.BITMASK;
    }

}
