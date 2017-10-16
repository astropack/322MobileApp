package a324.mobileapplication;

import java.io.*;
import java.security.MessageDigest;
//import javax.swing.JFileChooser;

public class test2{
    /*
    public static void main(String[] args) throws IOException{
        File tmpFile = File.createTempFile("tmp",".txt");
        selectPDF();
        tmpFile.delete();
    }

    public static void selectPDF()throws IOException{
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        int result = fileChooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION)
        {
            File selectedFile = fileChooser.getSelectedFile();
            convertPDF(selectedFile);
        }
    }
    */
    public static void convertPDF(File pdfin) throws IOException{
        InputStream is = new FileInputStream(pdfin);
        OutputStream oos = new FileOutputStream("tmp.txt");
        byte[] buf = new byte[8192];
        int c = 0;
        while ((c = is.read(buf, 0, buf.length)) > 0) {
            oos.write(buf, 0, c);
            oos.flush();
        }
        oos.close();
        is.close();
        //stringBuilder();
    }
    public static void stringBuilder() throws IOException{
        File toHash = new File("C:\\Users\\User-PC\\Desktop\\tmp.txt");
        BufferedReader reader = new BufferedReader(new FileReader(toHash));
        String line = null;
        StringBuilder  stringBuilder = new StringBuilder();
        String ls = System.getProperty("line.separator");
        try {
            while((line = reader.readLine()) != null) {
                stringBuilder.append(line);
                stringBuilder.append(ls);
            }
            //System.out.println(stringBuilder.toString());
        } finally {
            reader.close();
        }
        File hashFile = new File("C:\\Users\\User-PC\\Desktop\\hash.txt");
        BufferedWriter hashout = new BufferedWriter(new FileWriter(hashFile));
        hashout.write(getSha256(stringBuilder.toString()));
        hashout.close();
        System.out.println(getSha256(stringBuilder.toString()));
    }

    public static String getSha256(String tmp){
        try{
            MessageDigest mstandard = MessageDigest.getInstance("SHA-256");
            mstandard.update(tmp.getBytes());
            return bytesToHex(mstandard.digest());
        } catch(Exception ex){
            throw new RuntimeException(ex);
        }
    }

    private static String bytesToHex(byte[] bytes){
        StringBuffer result = new StringBuffer();
        for (byte b : bytes) result.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
        return result.toString();
    }
}