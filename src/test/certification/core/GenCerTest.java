package certification.core;

import org.junit.Test;

public class GenCerTest {

    @Test
    public void generateCertificate() {
        String CN = "0";
        String OU = "13";
        String L = "13";
        String ST = "123";
        String O = "123";
        String C = "CN";
        String EM = "123";
        Request request = new Request(CN, OU,L,ST,O,C,EM);
        GenCer genCer = new GenCer(request);
        genCer.generateCertificate();
    }
}
