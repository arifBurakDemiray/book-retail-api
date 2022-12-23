package com.bookretail.util.service.qr;

import java.awt.image.BufferedImage;

public interface IImageToByteConverter {

    /**
     * Converts a buffered image to byte array
     *
     * @param image that is going to be converted
     * @returns converted image
     */
    byte[] convert(BufferedImage image);
}
