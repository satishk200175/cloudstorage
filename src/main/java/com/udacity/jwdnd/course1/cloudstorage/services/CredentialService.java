package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialsMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;

@Service
public class CredentialService {
    private CredentialsMapper credentialsMapper;

    public CredentialService(CredentialsMapper credentialsMapper) {
        this.credentialsMapper = credentialsMapper;
    }

    public int saveCredential(Credential credential){

        EncryptionService encryptionService = new EncryptionService();
        SecureRandom random = new SecureRandom();
        byte[] key = new byte[16];
        random.nextBytes(key);
        String encodedKey = Base64.getEncoder().encodeToString(key);
        String password = credential.getPassword();
        String encryptedPassword = encryptionService.encryptValue(password, encodedKey);
        credential.setPassword(encryptedPassword);
        credential.setKey(encodedKey);
        System.out.println("Decrypted password is: "+ encryptionService.decryptValue(encryptedPassword, encodedKey));
        return credentialsMapper.insert(credential);
    }

    public int updateCredential(Credential credential){

        byte[] key = new byte[16];
        SecureRandom random = new SecureRandom();
        EncryptionService encryptionService = new EncryptionService();
        random.nextBytes(key);
        String encodedKey = Base64.getEncoder().encodeToString(key);
        String password = credential.getPassword();
        String encryptedPassword = encryptionService.encryptValue(password, encodedKey);
        credential.setPassword(encryptedPassword);
        credential.setKey(encodedKey);
        return credentialsMapper.update(credential);
    }

    public List<Credential> getAllCredentials(Integer userId){

        List<Credential> credentials= credentialsMapper.getAllCredentials(userId);
        EncryptionService encryptionService = new EncryptionService();
        for (Credential credential: credentials) {
            credential.setClearPassword(encryptionService.decryptValue(credential.getPassword(), credential.getKey()));
        }
        return credentials;
    }

    public int deleteCredential(Integer credentialId, Integer userId){
        return credentialsMapper.deleteCredential(userId, credentialId);
    }
}
