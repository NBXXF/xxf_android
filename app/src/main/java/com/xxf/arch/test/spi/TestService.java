package com.xxf.arch.test.spi;

import com.xxf.spi.annotation.Service;

@Service(value = IApi.class,path = "Imai")
public class TestService implements IApi {
}
