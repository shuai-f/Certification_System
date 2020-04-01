package certification.core;

import certification.core.query.PathQuery;

import java.io.IOException;
import java.sql.*;

/**
 * Certificate Revoke List.
 */
public class CRL {

    public CRL(){

    }
    /**
     * Revoke the certificate with serial number.
     * @param serial -serial number
     * @param commenName
     * @return - "Revoke successfully!" if revoke the certificate of serial number, "Fail to revoke!"
     *          if fail to revoke;
     */
    public String doRequestCRL(String serial,String commenName){
        String message;
        Connection conn = null;
        Statement st = null;
        // 注册驱动
        try {
            DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/certificationsystem?useUnicode=true&characterEncoding=utf8&serverTimezone=GMT&allowPublicKeyRetrieval=true&useSSL=false", "root", "123456");
            String fileName;
            st = conn.createStatement();
            //写入
            String writeToSql = "select * from certs where serial='"+serial+"'";
            System.out.println(writeToSql);
            // 查询
            ResultSet rs = st.executeQuery(writeToSql);
            // 遍历
            boolean flag = true;
            while (rs.next()) {
                flag = false;
                fileName = rs.getString("fileName");
                String nameCompare = rs.getString("commonName");
                if(!commenName.equals(nameCompare)){
                    return "Fail to revoke!";
                } else {
                    doCRL(fileName,commenName,serial);
                }
            }
            String delete = "delete from certs where serial='"+serial+"'";
            if(st.executeUpdate(writeToSql)!=0) {
                System.out.println("Delete " + serial + " from cert repository successfully！");
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "Revoke successfully!";
    }

    /**
     * Revoke certificate.
     * @param crtPath -path of cert need to be revoked.
     * @return
     */
    public boolean doCRL(String crtPath,String commonName,String serial) {
        String crlPath = PathQuery.crlPath+serial+".crl";
        StringBuffer cmd = new StringBuffer();
        cmd.append("openssl ca -config "+ PathQuery.confPath+" -revoke "+crtPath);
        cmd.append(" & openssl ca -config "+PathQuery.confPath+" -gencrl -out "+crlPath);
        try {
            System.out.println(cmd);
            Process ps = Runtime.getRuntime().exec("cmd /c start " + cmd.toString());

        } catch (IOException e) {
            e.printStackTrace();
        }
        int count = 0;
        while (true) {
            try {
                Thread.sleep(1000);
                count++;
                if (count>=5){
                    break;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        writeCrlToSql(serial,crlPath,commonName);
        return true;
    }
    /**
     * Write crl to sql
     * @return -true if store successfully
     */
    public boolean writeCrlToSql(String serial,String fileName,String commonName){
        Connection conn = null;
        Statement st = null;
        // 注册驱动
        try {
            DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/certificationsystem?useUnicode=true&characterEncoding=utf8&serverTimezone=GMT&allowPublicKeyRetrieval=true&useSSL=false", "root", "123456");

            st = conn.createStatement();
            //写入
            String[] split = fileName.split("\\\\");
            fileName = split[0];
            for (int i=1;i<split.length;i++){
                fileName += "\\\\" + split[i];
            }
            String value = "(\'"+commonName+"\', \'"+serial+"\', \'"+fileName+"\')";
            String writeToSql = "insert into crl (commonName, serial, fileName) values "+value;
            System.out.println(writeToSql);
            // 查询
            int rs;
            rs = st.executeUpdate(writeToSql);
            if (rs!=0){
                System.out.println("插入成功！");
            } else {
                System.out.println("插入失败！");
                return false;
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return true;
    }
}
