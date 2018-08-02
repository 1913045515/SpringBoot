package com.example.demo.config;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.singletonList;

/**
 * @author linzhiqiang
 * @create 2018/08/01
 */
@Configuration
public class ApplicationConfig extends AbstractMongoConfiguration {

    @Value("${spring.data.mongodb.database}")
    private String dbname;
    @Value("${spring.data.mongodb.ip-and-ports}")

    private String ipAndPort;
    @Value("${spring.data.mongodb.username}")

    private String userName;
    @Value("${spring.data.mongodb.pwd}")
    private String pwd;


    @Override
    protected String getDatabaseName() {
        return dbname;
    }

    @Override
    public Mongo mongo() {
        // 存放ip地址的list
        List<ServerAddress> addresses = new ArrayList<ServerAddress>();
        // 循环遍历出所有的ip和port
        String[] ipAndPorts = ipAndPort.split(",");
        for (String ipAndPortStr : ipAndPorts) {
            String mongodbIp = ipAndPortStr.split(":")[0];
            String mongodbPort = ipAndPortStr.split(":")[1];
            addresses.add(new ServerAddress(mongodbIp, Integer.valueOf(mongodbPort)));
        }
        if (StringUtils.isBlank(pwd)) {
            pwd = "";
        }
        return new MongoClient(addresses, singletonList(MongoCredential.createCredential(userName, dbname, pwd.toCharArray())));
    }

    @Override
    public MongoTemplate mongoTemplate() throws Exception {
        return super.mongoTemplate();
    }

    @Override
    public MongoMappingContext mongoMappingContext() throws ClassNotFoundException {
        return super.mongoMappingContext();
    }

    @Override
    public MappingMongoConverter mappingMongoConverter() throws Exception {
        MappingMongoConverter mongoConverter = super.mappingMongoConverter();
        return mongoConverter;
    }

    @Bean
    public GridFsTemplate gridFsTemplate() throws Exception {
        return new GridFsTemplate(mongoDbFactory(), mappingMongoConverter());
    }
}
