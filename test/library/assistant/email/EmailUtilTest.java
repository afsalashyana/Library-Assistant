package library.assistant.email;

import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetup;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import library.assistant.data.callback.GenericCallback;
import library.assistant.data.model.MailServerInfo;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class EmailUtilTest {

    private GreenMail greenMail;
    // Use an arbitrary port or find a free one. 3025 is default for GreenMail SMTP.
    private static final int SMTP_PORT = 3025;

    @Before
    public void setUp() {
        ServerSetup setup = new ServerSetup(SMTP_PORT, "localhost", ServerSetup.PROTOCOL_SMTP);
        greenMail = new GreenMail(setup);
        greenMail.setUser("test@library.com", "password");
        greenMail.start();
    }

    @After
    public void tearDown() {
        if (greenMail != null) {
            greenMail.stop();
        }
    }

    @Test
    public void testSendMail() throws Exception {
        // Prepare mail server info pointing to our local GreenMail instance
        // SSL disabled for simplicity in this test
        MailServerInfo mailServerInfo = new MailServerInfo("localhost", SMTP_PORT, "test@library.com", "password", false);

        String recipient = "user@example.com";
        String subject = "Test Subject";
        String content = "<h1>Test Content</h1>";

        // Use a latch to wait for the async operation
        CountDownLatch latch = new CountDownLatch(1);
        final boolean[] success = {false};

        GenericCallback callback = new GenericCallback() {
            @Override
            public Object taskCompleted(Object val) {
                if (val instanceof Boolean) {
                    success[0] = (Boolean) val;
                }
                latch.countDown();
                return null;
            }
        };

        // Invoke the method under test
        EmailUtil.sendMail(mailServerInfo, recipient, content, subject, callback);

        // Wait for the email sending thread to complete
        boolean completed = latch.await(10, TimeUnit.SECONDS);

        Assert.assertTrue("Timeout waiting for email to be sent", completed);
        Assert.assertTrue("Callback should return true indicating success", success[0]);

        // Verify email reception on GreenMail
        MimeMessage[] receivedMessages = greenMail.getReceivedMessages();
        Assert.assertEquals("Should have received one email", 1, receivedMessages.length);

        MimeMessage received = receivedMessages[0];
        Assert.assertEquals(subject, received.getSubject());
        Assert.assertEquals(recipient, received.getRecipients(Message.RecipientType.TO)[0].toString());
        // Simple check for content presence
        // Note: content might be multipart depending on implementation details
        // In EmailUtil: message.setContent(content, "text/html");
        // So it should be text/html.
        Assert.assertTrue(received.getContent().toString().contains("Test Content"));
    }
}
