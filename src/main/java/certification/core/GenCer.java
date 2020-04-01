package certification.core;

import certification.core.query.PathQuery;
import org.apache.ibatis.jdbc.Null;

import javax.naming.InsufficientResourcesException;
import java.io.*;
import java.sql.*;

/**.
 * Generate a certificate by request from the client.
 */
public class GenCer {
    private Request request;
    private String openssl = "openssl ";
    private String serial;

    public GenCer(Request request) {
        this.request = request;
        this.serial = getSerial();
    }

    /**
     * Read the next serial number from serial file
     * @return the serial;
     */
    private String getSerial(){
        String serial = null;
        File file = new File(PathQuery.serialPath);
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            serial = reader.readLine();
            reader.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return serial;
    }
    /**
     * Generate a certificate(*.cer|pem) by the requeset;
     * @return -the path of new cer
     */
    public String generateCertificate(){
        String path = PathQuery.democaPath+PathQuery.privatePath+this.serial;
        String keyPath = genClientKey(path);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String csrPath = genCSR(keyPath);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String crtPath = PathQuery.democaPath+PathQuery.certsPath+serial+".crt";
        StringBuffer cmd = new StringBuffer();
        cmd.append( this.openssl + "ca -config "+PathQuery.confPath+" -in "+ csrPath +" -out "+crtPath);
//        cmd.append("powershell");
        try {
            System.out.println(cmd);
            Process ps = Runtime.getRuntime().exec("cmd /c start " + cmd.toString());

        } catch (IOException e) {
            e.printStackTrace();
        }
        int count = 0;
        while (true) {
            File file = new File(crtPath);
            if (file.exists()){
                break;
            }
            try {
                Thread.sleep(1000);
                count++;
                if (count>=10){
                    System.out.println("CA rejects!");
                    return "Reject!";
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        writeCertToSql(crtPath);
        return crtPath;
    }

    /**
     * Generate a client_key
     * @param path - directory of keys
     * @return -file name of the client_key.
     */
    private String genClientKey(String path){
        StringBuffer cmd = new StringBuffer();
        cmd.append(this.openssl);
        cmd.append("genrsa -out "+path+".key 1024");
        try {
            System.out.println(cmd);
            Process ps = Runtime.getRuntime().exec(cmd.toString());
//            cmd.delete(0,cmd.length()-1);
//            cmd.append(this.openssl+ "rsa -in "+path+"pass.key -out "+path+".key");
//            ps = Runtime.getRuntime().exec(cmd.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return path+".key";
    }

    /**
     * Generate a CSR (Requset)
     * @param clientKeyPath -client key path
     * @return -path of csr
     */
    private String genCSR(String clientKeyPath){
        String csrPath = PathQuery.democaPath+PathQuery.csrPath+serial+".csr";
        StringBuffer cmd = new StringBuffer();
        cmd.append(this.openssl);
        cmd.append("req -new -key "+clientKeyPath+" -out "+ csrPath+" -subj \""+request.constructBodyName()+"\"");
        try {
            System.out.println(cmd);
            Process ps = Runtime.getRuntime().exec(cmd.toString());
            cmd.delete(0,cmd.length()-1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return csrPath;
    }

    /**
     * Sign the (*.csr) from client.
     * @param requestPath -client request local path
     * @return
     */
    public String signCSR(String requestPath) {
        String crtPath = PathQuery.democaPath+PathQuery.certsPath+serial+".crt";
        StringBuffer cmd = new StringBuffer();
        cmd.append( this.openssl + "ca -config "+PathQuery.confPath+" -in "+ requestPath +" -out "+crtPath);
//        cmd.append("powershell");
        try {
            System.out.println(cmd);
            Process ps = Runtime.getRuntime().exec("cmd /c start " + cmd.toString());

        } catch (IOException e) {
            e.printStackTrace();
        }
        int count = 0;
        while (true) {
            File file = new File(crtPath);
            if (file.exists()){
                break;
            }
            try {
                Thread.sleep(1000);
                count++;
                if (count>=10){
                    System.out.println("CA rejects!");
                    return "reject";
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if(!writeCertToSql(crtPath)){
        }
        return crtPath;
    }


    /**
     * Sign the request information and client public key.
     * @param clientKeyPath -client public key
     * @param request  -request contains client's information
     * @return
     */
    public String signPublickeyAndRequest(String clientKeyPath,Request request){
        String csrPath = genCSR(clientKeyPath);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String crtPath = PathQuery.democaPath+PathQuery.certsPath+serial+".crt";
        StringBuffer cmd = new StringBuffer();
        cmd.append( this.openssl + "ca -config "+PathQuery.confPath+" -in "+ csrPath +" -out "+crtPath);
//        cmd.append("powershell");
        try {
            System.out.println(cmd);
            Process ps = Runtime.getRuntime().exec("cmd /c start " + cmd.toString());

        } catch (IOException e) {
            e.printStackTrace();
        }
        int count = 0;
        while (true) {
            File file = new File(crtPath);
            if (file.exists()){
                break;
            }
            try {
                Thread.sleep(1000);
                count++;
                if (count>=10){
                    System.out.println("CA rejects!");
                    return "Reject!";
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        writeCertToSql(crtPath);
        return crtPath;
    }

    /**
     * Store the certificate to sql
     * @param fileName  -name of new cert
     * @return -true if store successful.
     */
    private boolean writeCertToSql(String fileName){
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
            String value = "(\'"+request.getCN()+"\', \'"+serial+"\', \'"+fileName+"\')";
            String writeToSql = "insert into certs (commonName, serial, fileName) values "+value;
            System.out.println(writeToSql);
            // 查询
            //String sql = "select * from certs";
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
