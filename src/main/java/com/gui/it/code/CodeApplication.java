package com.gui.it.code;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@MapperScan("com.gui.it.code.dao")
public class CodeApplication 
{
    public static void main( String[] args )
    {
        SpringApplication.run(CodeApplication.class, args);
    }
}
