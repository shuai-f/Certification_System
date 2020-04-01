package certification.core;

import org.junit.Test;

import static org.junit.Assert.assertFalse;

public class HandlerTest {

    @Test
    public void requestHandler() {
    }

    @Test
    public void timeoutHandler() {
        Handler handler = new Handler();
        assertFalse(handler.timeoutHandler());
    }


    @Test
    public void certVerifiedHandlerTest() {
        Handler handler = new Handler();
        String path = "E:\\OpenSSL-Win64\\demoCA\\certs\\05.crt";
        String emmm = handler.certVerifiedHandler(path);
        System.out.println(emmm);
    }
}
