package com.project.common.config;
import org.springframework.beans.factory.annotation.Autowired;import org.springframework.context.annotation.Configuration;import org.springframework.jdbc.core.JdbcTemplate;import org.springframework.boot.ApplicationRunner;import org.springframework.boot.ApplicationArguments;import com.project.system.service.MerchantApiService;
/** Initializes API identities only. Transaction outcomes are returned synchronously. */
@Configuration public class MerchantApiJobs implements ApplicationRunner{
 @Autowired private JdbcTemplate db;@Autowired private MerchantApiService api;
 @Override public void run(ApplicationArguments args){for(Long id:db.queryForList("select id from campus_merchant where id<>3",Long.class))api.ensure(id);}
}
