package certification.core.query;

/**.
 * Query certificate by serial number.
 */
public class Query {
    /**
     * Query the certificate by serial number.
     * @param seq -serial number
     * @return true if the certificate is in the DB, otherwise false
     */
    public boolean queryCertificate(String seq){

        return true;

    }

    public boolean querySQL(String seq){
        return false;
    }
}
