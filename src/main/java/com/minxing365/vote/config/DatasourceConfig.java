package com.minxing365.vote.config;
import com.minxing365.vote.util.SecretUtils;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

import static com.minxing365.vote.util.SecretUtils.PASSWORD_CRYPT_KEY;


/**
 * @author SuZZ
 * 数据库连接配置
 */
@Configuration
@MapperScan(basePackages="com.jnb.gtasks.dao", sqlSessionFactoryRef = "sqlSessionFactory")
public class DatasourceConfig {
    private   Logger log =LoggerFactory.getLogger(DatasourceConfig.class);
    @Autowired
    private Environment env;

    @Bean
    public DataSource dataSource() {
        //解密操作
        String password=   SecretUtils.decode( PASSWORD_CRYPT_KEY,env.getProperty("db.password") );
        log.info("【解密后密码】 "+password);
        HikariConfig config = new HikariConfig();
        // 数据库基础配置
        config.setDriverClassName(env.getProperty("db.driverClass"));
        config.setAutoCommit(false);
        config.setJdbcUrl(env.getProperty("db.url"));
        config.setUsername(env.getProperty("db.username"));
        config.setPassword(password);
      //  config.setPassword(env.getProperty("db.password"));
        // 空闲超时时间
        config.setIdleTimeout(60000);
        // 连接超时时间
        config.setConnectionTimeout(30000);
        // 连接测试语句
        config.setConnectionTestQuery("SELECT 1");
        // 连接初始化语句
        config.setConnectionInitSql("SELECT 1");
        // 最大连接数
        config.setMaximumPoolSize(Integer.valueOf(env.getProperty("db.maximumPoolSize")));
        // 最小空闲数
        config.setMinimumIdle(1);
        // 非只读数据库
        config.setReadOnly(false);
        // 应参照mysql wait_timeout值,短1分钟左右
        config.setMaxLifetime(20000);
        return new HikariDataSource(config);
    }

    @Bean
    public DataSourceTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dataSource());
    }

    @Bean
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
        final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);
        return sessionFactory.getObject();
    }

}
