package com.bookretail.util.service.qr;

import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Component
@NoArgsConstructor
public class PngImageToByteConverter implements IImageToByteConverter {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public byte[] convert(BufferedImage image) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, "png", byteArrayOutputStream);
        } catch (IOException e) {
            logger.warn("In PngImageToByteConverter at line 23 {}", e.getMessage());
        }
        return byteArrayOutputStream.toByteArray();
    }
}
