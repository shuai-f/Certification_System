package certification.core;

import certification.core.query.PathQuery;
import org.junit.Test;

public class CRLTest {

    @Test
    public void doRequestCRL() {
        CRL crl = new CRL();
        String serial = "01";//must in repo
        String commonName = "123";
        crl.doRequestCRL(serial,commonName);
    }

    @Test
    public void doCRL() {
        CRL crl = new CRL();
        String serial = "03";
        String commonName = "12";
        String crtPath = PathQuery.democaPath+PathQuery.certsPath+serial+".crt";
        crl.doCRL(crtPath,commonName,serial);
    }

    @Test
    public void writeCrlToSql() {
    }
}
