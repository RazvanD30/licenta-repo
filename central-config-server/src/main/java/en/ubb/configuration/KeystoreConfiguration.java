package en.ubb.configuration;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.KeyStore;

public class KeystoreConfiguration {

    public static String getValueFromKeystore(String entry, String keyStorePath, String keyStorePassword) throws Exception{

        KeyStore ks = KeyStore.getInstance("JCEKS");
        ks.load(null, keyStorePassword.toCharArray());
        KeyStore.PasswordProtection keyStorePP = new KeyStore.PasswordProtection(keyStorePassword.toCharArray());

        FileInputStream fIn = new FileInputStream(keyStorePath);

        ks.load(fIn, keyStorePassword.toCharArray());

        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBE");

        KeyStore.SecretKeyEntry ske =
                (KeyStore.SecretKeyEntry)ks.getEntry(entry, keyStorePP);

        PBEKeySpec keySpec = (PBEKeySpec)factory.getKeySpec(
                ske.getSecretKey(),
                PBEKeySpec.class);

        char[] value = keySpec.getPassword();

        return new String(value);

    }

    public static void makeNewKeystoreEntry(String entry, String entryValue, String keyStorePath, String keyStorePassword)
            throws Exception {
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBE");
        SecretKey generatedSecret =
                factory.generateSecret(new PBEKeySpec(
                        entryValue.toCharArray()));

        KeyStore ks = KeyStore.getInstance("JCEKS");
        ks.load(null, keyStorePassword.toCharArray());
        KeyStore.PasswordProtection keyStorePP = new KeyStore.PasswordProtection(keyStorePassword.toCharArray());

        ks.setEntry(entry, new KeyStore.SecretKeyEntry(
                generatedSecret), keyStorePP);

        FileOutputStream fos = new java.io.FileOutputStream(keyStorePath);
        ks.store(fos, keyStorePassword.toCharArray());
    }
}
