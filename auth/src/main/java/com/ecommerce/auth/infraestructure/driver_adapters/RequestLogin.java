package com.ecommerce.auth.infraestructure.driver_adapters;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RequestLogin {

    private String email;
    private String pass;


}
