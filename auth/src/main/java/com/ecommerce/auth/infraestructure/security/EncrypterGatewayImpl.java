package com.ecommerce.auth.infraestructure.security;

import com.ecommerce.auth.domain.model.gateway.EncrypterGateway;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class EncrypterGatewayImpl implements EncrypterGateway {

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Override
    public String encrypt(String pass) {
        return encoder.encode(pass);
    }

    @Override
    public Boolean checkPass(String passUser, String passBD) {
        return encoder.matches(passUser, passBD);
    }
}
