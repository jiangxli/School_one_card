import com.project.system.service.CardService;
import java.util.*;
import org.springframework.context.annotation.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.*;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.support.TransactionTemplate;
import javax.sql.DataSource;
public class CardIntegration {
@Configuration @EnableTransactionManagement static class Config {
@Bean public DataSource dataSource(){return new DriverManagerDataSource("jdbc:mysql://127.0.0.1:13306/cc_card_test?useSSL=false&serverTimezone=Asia/Shanghai","root", "");}
@Bean public JdbcTemplate jdbcTemplate(DataSource d){return new JdbcTemplate(d);}
@Bean public DataSourceTransactionManager transactionManager(DataSource d){return new DataSourceTransactionManager(d);}
@Bean public CardService card(){return new CardService();}}
static int checks=0;static void check(boolean b){if(!b)throw new AssertionError();checks++;}
static void reject(Runnable r){try{r.run();}catch(RuntimeException e){checks++;return;}throw new AssertionError();}
public static void main(String[] args){try(AnnotationConfigApplicationContext c=new AnnotationConfigApplicationContext(Config.class)){CardService s=c.getBean(CardService.class);JdbcTemplate db=c.getBean(JdbcTemplate.class);
Map<String,Object> first=s.issue(1);check(first.get("card_no").equals(s.issue(1).get("card_no")));check(s.info(2).isEmpty());
String key=UUID.randomUUID().toString();s.write(1,key,1);s.write(1,key,1);check(((Number)s.info(1).get("write_count")).intValue()==1);check(s.history(1).size()==1);check(s.history(1).get(0).get("payload").toString().contains(first.get("card_no").toString()));
reject(()->s.write(2,UUID.randomUUID().toString(),1));reject(()->s.issue(999));s.issue(2);check(!s.info(2).get("card_no").equals(first.get("card_no")));reject(()->s.write(2,key,1));db.update("update sys_user set status='1' where user_id=1");reject(()->s.write(1,UUID.randomUUID().toString(),1));check(s.history(1).size()==1);
TransactionTemplate tx=new TransactionTemplate(c.getBean(DataSourceTransactionManager.class));try{tx.execute(status->{db.update("insert into sys_user values(3,'0','0')");s.issue(3);throw new IllegalArgumentException("rollback");});}catch(IllegalArgumentException expected){}check(s.info(3).isEmpty());check(db.queryForObject("select count(*) from sys_user where user_id=3",Integer.class)==0);
System.out.println("PASS "+checks+" card integration checks");}}}
