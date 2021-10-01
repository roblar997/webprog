package com.s349967.oblig3s349967.service;

import com.s349967.oblig3s349967.ServiceInterfaces.IKryptoService;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

@Service
public class KryptoService implements IKryptoService {
    @Override
    public boolean kryptoCompare(String passord, String hashPassord) {
        return BCrypt.checkpw(passord,hashPassord);
    }

    @Override
    public String genererPassord(String passord,int antRunder) {
        return BCrypt.hashpw(passord,BCrypt.gensalt(antRunder));
    }
}
