package unet.replica.handlers;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Replicate extends Replica {

    private File from, to;

    public Replicate(String fromPath, String toPath){
        from = new File(fromPath);
        to = new File(toPath);
    }

    public void execute(){
        if(!from.exists()){
            return;
        }

        if(from.isDirectory()){
            copyDir(from, to);
            return;
        }

        try{
            copyFile(from, to);
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    private void copyDir(File d, File d2){
        if(!d2.exists()){
            d2.mkdirs();
        }

        for(File f : d.listFiles()){
            File t = new File(d2, f.getName());

            if(f.isDirectory()){
                copyDir(f, t);
                continue;
            }

            if(t.exists()){
                if(f.length() != t.length()){
                    System.err.println("B");
                    try{
                        copyFile(f, t);
                    }catch(IOException e){
                        e.printStackTrace();
                    }
                    continue;
                }

                try{
                    if(!getFileChecksum(f).equals(getFileChecksum(t))){
                        System.err.println("A");
                        copyFile(f, t);
                    }
                }catch(NoSuchAlgorithmException | IOException e){
                    e.printStackTrace();
                }

            }else{
                try{
                    System.err.println("C");
                    copyFile(f, t);
                }catch(IOException e){
                    e.printStackTrace();
                }
            }

        }
    }

    private void copyFile(File f, File t)throws IOException {
        InputStream in = new FileInputStream(f);
        OutputStream out = new FileOutputStream(t);

        byte[] buf = new byte[4096];
        int len;

        while((len = in.read(buf)) != -1){
            out.write(buf, 0, len);
        }

        in.close();

        out.flush();
        out.close();
    }

    public String getFileChecksum(File file)throws NoSuchAlgorithmException, IOException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");

        FileInputStream in = new FileInputStream(file);

        byte[] byteArray = new byte[1024];
        int bytesCount = 0;

        while((bytesCount = in.read(byteArray)) != -1){
            digest.update(byteArray, 0, bytesCount);
        }
        in.close();

        byte[] bytes = digest.digest();

        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < bytes.length; i++){
            sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
        }

        return sb.toString();
    }
}
