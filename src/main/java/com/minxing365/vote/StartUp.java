package com.minxing365.vote;
import com.minxing365.dbschema.migrate.MigrateTool;
import com.minxing365.vote.util.SecretUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static com.minxing365.vote.util.SecretUtils.PASSWORD_CRYPT_KEY;

@EnableSwagger2
@ServletComponentScan(basePackages = "com.minxing365.vote.filter")
@SpringBootApplication
public class StartUp {

    public static void main(String[] args) throws IOException, SQLException, ClassNotFoundException {
        if (args != null && args.length > 0 && "update".equals(args[0])){
            //migrate list
            List<String> migrateList = new ArrayList<>();
            migrateList.add("2018111201.sql");
            migrateList.add("2018111202.sql");
            if (migrateList.isEmpty()){
                return;
            }
            String configPath = System.getProperty("spring.config.location");
            if (null == configPath) {
                System.err.println("未指定配置文件！");
                System.exit(0);
            }
            Properties properties = new Properties();
            properties.load(new FileReader(configPath));
            String driverClass = properties.getProperty("db.driverClass");
            String dburl = properties.getProperty("db.url");
            String username = properties.getProperty("db.username");
            String password=SecretUtils.decode3Des( PASSWORD_CRYPT_KEY, properties.getProperty("db.password"));
           // String password = properties.getProperty("db.password");
            MigrateTool migrateTool = MigrateTool.getInstance(driverClass, dburl, username, password, "mx_vote", migrateList);
            migrateTool.update();
        }else {
            SpringApplication.run( StartUp.class, args );
        }

    }
}
