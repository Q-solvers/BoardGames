/*
 * $Id: CaptchaIntegrationService.java 22 2009-02-14 20:29:08Z iskakoff $
 */
package org.a2union.gamesystem.security.captcha;

import com.octo.captcha.service.CaptchaServiceException;
import com.octo.captcha.service.image.ImageCaptchaService;
import org.apache.tapestry5.StreamResponse;
import org.apache.tapestry5.services.Response;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

/**
 * @author Iskakoff
 */
public class CaptchaIntegrationService implements ICaptchaIntegrationService {
    private ImageCaptchaService serviceInstance;

    public StreamResponse generateImageChallenge(String captchaId) {

        //get generated image from captcha service
        BufferedImage imageChallenge = serviceInstance.getImageChallengeForID(captchaId, Locale.US);
        ByteArrayOutputStream os = new ByteArrayOutputStream();

        try {
            // convert BufferedImage to PNG image and save it to BAOS
            ImageIO.write(imageChallenge, "PNG", os);
            // generate StreamResponse from BAOS
            final InputStream is = new ByteArrayInputStream(os.toByteArray());
            return new StreamResponse() {
                public String getContentType() {
                    return "image/png";
                }

                public InputStream getStream() throws IOException {
                    return is;
                }

                public void prepareResponse(Response response) {
                    response.setHeader("Pragma", "no-cache");
                    response.setHeader("Cache-Control", "no-cache");
                    response.setDateHeader("Expires", 0);
                }
            };
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isValidUserResponse(String verificationCode, String captchaId) {
        try {
            return serviceInstance.validateResponseForID(captchaId, verificationCode);
        } catch (CaptchaServiceException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Spring setter-injection of captcha service
     * @param captchaService captcha service
     */
    public void setCaptchaService(ImageCaptchaService captchaService) {
        serviceInstance = captchaService;
    }
}
