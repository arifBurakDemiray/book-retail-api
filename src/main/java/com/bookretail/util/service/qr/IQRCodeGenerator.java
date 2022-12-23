package com.bookretail.util.service.qr;

import java.awt.image.BufferedImage;

public interface IQRCodeGenerator {

    /**
     * Creates a 2D QR Code image by given text
     *
     * @param text additional information about QR code
     * @returns image of QR code
     */
    BufferedImage createQRImageWithText(String text);
}
