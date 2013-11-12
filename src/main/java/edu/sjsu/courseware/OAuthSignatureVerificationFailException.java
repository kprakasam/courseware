package edu.sjsu.courseware;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class OAuthSignatureVerificationFailException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = -5135206745935981741L;

}
