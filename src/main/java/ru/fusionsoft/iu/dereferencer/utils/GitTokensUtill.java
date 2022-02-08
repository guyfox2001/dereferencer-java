package ru.fusionsoft.iu.dereferencer.utils;

import com.fasterxml.jackson.databind.JsonNode;
import org.bouncycastle.jce.provider.BouncyCastleProvider;


import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.Security;
import java.util.HashMap;
import java.util.Map;

import static javax.crypto.Cipher.*;

public class GitTokensUtill {

    private static Map <String, String> gitTokens;
    private static JsonNode _serialaizedNode;
    private static String keyAES = "default-value-key";



    private static void _deserializeTokens(File serealizedTokens){
        if(gitTokens != null) return;

        if(serealizedTokens.exists()){
            String inputLine;
            StringBuilder generalString = new StringBuilder();
            try(BufferedReader br = new BufferedReader(new FileReader(serealizedTokens))) {
                while ((inputLine = br.readLine())!= null){
                    generalString.append(inputLine);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            Security.addProvider( new BouncyCastleProvider());

            try{
                Cipher cipher = Cipher.getInstance("AES");
                cipher.init(DECRYPT_MODE, new SecretKeySpec(keyAES.getBytes(), "RawBytes"));
                gitTokens = MapperUtil.getMapperInstance().readValue(cipher.doFinal(generalString.toString().getBytes()), Map.class);
            } catch (Exception e){
                e.printStackTrace();
            }
            return;
        }

       gitTokens = new HashMap<>();
    }

    public static void setKeyAES(String keyAES) {
        GitTokensUtill.keyAES = keyAES;
    }


    public static Map<String, String> getGitTokensInstance() {
        _deserializeTokens(new File(System.getProperty("user.home") + "/.dereferencer-cache/"+MD5Util.getMD5("git-cahce-tokens")));
        return gitTokens;
    }
}
