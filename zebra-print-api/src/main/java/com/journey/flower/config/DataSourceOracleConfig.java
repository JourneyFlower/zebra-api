package com.journey.flower.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

/**
 * @author JourneyOfFlower
 * @version v1.0
 * @date 2022.09.23 16:14
 * @description ZB_TODO
 */
@Configuration
@MapperScan(basePackages = {"com.journey.flower.dao"}, sqlSessionFactoryRef = "oracleSqlSessionFactory")
public class DataSourceOracleConfig {

    @Bean(name = "oracleDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.oracle")
    @Primary
    public DataSource oracleDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "oracleSqlSessionFactory")
    @Primary
    public SqlSessionFactory oracleSqlSessionFactory(@Qualifier("oracleDataSource") DataSource oracleDataSource, @Qualifier("oracleConfig") org.apache.ibatis.session.Configuration config) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(oracleDataSource);
        //添加mybatis配置文件读取功能 驼峰命名等配置
        //bean.setConfigLocation(new PathMatchingResourcePatternResolver().getResource("mybatis-config.xml"));
        //如果不使用xml的方式配置mapper，则可以省去下面这行mapper location的配置。
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mybatis-mappers/*"));
        //应用mybatis驼峰命名等配置
        bean.setConfiguration(config);
        try {
            return bean.getObject();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }


    @Bean(name = "oracleTransactionManager")
    @Primary
    public DataSourceTransactionManager oracleTransactionManager(@Qualifier("oracleDataSource") DataSource oracleDataSource) {
        return new DataSourceTransactionManager(oracleDataSource);
    }

    @Bean(name = "oracleSqlSessionTemplate")
    @Primary
    public SqlSessionTemplate oracleSqlSessionTemplate(@Qualifier("oracleSqlSessionFactory") SqlSessionFactory oracleSqlSessionFactory) {
        return new SqlSessionTemplate(oracleSqlSessionFactory);
    }

    @Bean(name = "oracleConfig")
    @ConfigurationProperties(prefix = "mybatis.configuration")
    public org.apache.ibatis.session.Configuration globalConfiguration() {
        return new org.apache.ibatis.session.Configuration();
    }

}
