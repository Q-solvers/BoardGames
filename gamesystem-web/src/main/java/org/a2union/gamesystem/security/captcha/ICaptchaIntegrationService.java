/*
 * $Id: ICaptchaIntegrationService.java 22 2009-02-14 20:29:08Z iskakoff $
 */
package org.a2union.gamesystem.security.captcha;

import org.apache.tapestry5.StreamResponse;

/**
 * This service generate CAPTCHA image and check user response for correctness
 * @author Iskakoff
 */
public interface ICaptchaIntegrationService {

    /**
     * Verify Captcha test
     * @param verificationCode verification code for current CAPTCHA
     * @param captchaId identifier of unique captcha code (usually used HTTP Session ID)
     * @return true if verfication code is correct false otherwise
     */
    public boolean isValidUserResponse(String verificationCode, String captchaId);

    /**
     * Generate CAPTCHA-image
     * @param captchaId identifier of unique captcha code (usually used HTTP Session ID)
     * @return Generated captcha image as stream
     */
    public StreamResponse generateImageChallenge(String captchaId);

}
