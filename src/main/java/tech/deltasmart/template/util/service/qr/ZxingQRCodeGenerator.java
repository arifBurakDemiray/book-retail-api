package com.bookretail.util.service.qr;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.awt.image.BufferedImage;

@Component
@NoArgsConstructor
public class ZxingQRCodeGenerator implements IQRCodeGenerator {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public BufferedImage createQRImageWithText(String text) {
        QRCodeWriter barcodeWriter = new QRCodeWriter();

        var barcodeText = "This QR Code is invalid.";

        if (text != null && !text.isBlank()) {
            barcodeText = text;
        }

        BitMatrix bitMatrix = new BitMatrix(1, 1);
        try {
            bitMatrix = barcodeWriter.encode(barcodeText, BarcodeFormat.QR_CODE, 200, 200);
        } catch (WriterException e) {
            logger.warn("In ZxingQRCodeGenerator at line 34 ---> {}", e.getMessage());
        }
        return MatrixToImageWriter.toBufferedImage(bitMatrix);
    }
}