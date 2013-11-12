package edu.sjsu.courseware.util;

import java.util.List;
import java.util.Map.Entry;

import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import net.oauth.OAuthAccessor;
import net.oauth.OAuthConsumer;
import net.oauth.OAuthException;
import net.oauth.OAuthMessage;
import net.oauth.OAuthServiceProvider;
import net.oauth.OAuthValidator;
import net.oauth.SimpleOAuthValidator;
import net.oauth.server.OAuthServlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Named
public class OAuthSignatureValidator {
    private Logger logger = LoggerFactory.getLogger(OAuthSignatureValidator.class);
    private OAuthValidator oAuthValidator = new SimpleOAuthValidator();

    public boolean verifySignature(HttpServletRequest request, String consumerSecret) {
        try {
            OAuthMessage message = OAuthServlet.getMessage(request, null);

            if (logger.isDebugEnabled()) {
                List<Entry<String, String>> parameters = message.getParameters();

                for (Entry<String, String> entry : parameters)
                    logger.debug(entry.getKey() + ": " + entry.getValue());
            }

            OAuthServiceProvider serviceProvider = new OAuthServiceProvider(null, null, null);

            // try to load from local cache if not throw exception

            String consumerKey = message.getConsumerKey();
            OAuthConsumer consumer = new OAuthConsumer(null, consumerKey, consumerSecret, serviceProvider);
            OAuthAccessor accessor = new OAuthAccessor(consumer);
            accessor.tokenSecret = "";
            oAuthValidator.validateMessage(message, accessor);
            return true;
        } catch (OAuthException e) {
            logger.error("Invalid oauth signature" + e);
            return false;
        } catch (Exception e) {
            logger.error("Exception validating oauth signature" + e);
            return false;
        }
    }
}
