import com.project.system.service.*;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.*;
import org.springframework.context.annotation.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.*;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import javax.sql.DataSource;
public class WalletV2Integration {
 @Configuration @EnableTransactionManagement static class Config {
 @Bean public DataSource dataSource(){return new DriverManagerDataSource("jdbc:mysql://127.0.0.1:13306/cc_thesis_v2_test?useSSL=false&serverTimezone=Asia/Shanghai","root","");}
 @Bean public JdbcTemplate jdbcTemplate(DataSource d){return new JdbcTemplate(d);}
 @Bean public DataSourceTransactionManager transactionManager(DataSource d){return new DataSourceTransactionManager(d);}
 @Bean public WalletService wallet(){return new WalletService();}
 @Bean public CardService cards(){return new CardService();}
 }
 static int count;
 static void check(String name,boolean ok){if(!ok)throw new AssertionError(name);count++;System.out.println("PASS "+name);}
 static void reject(String name,Runnable work){try{work.run();}catch(IllegalArgumentException expected){check(name,true);return;}throw new AssertionError(name);}
 static String key(){return UUID.randomUUID().toString();}
 public static void main(String[]args)throws Exception{try(AnnotationConfigApplicationContext c=new AnnotationConfigApplicationContext(Config.class)){
 WalletService s=c.getBean(WalletService.class);JdbcTemplate db=c.getBean(JdbcTemplate.class);
 check("issue idempotence",s.issue(1).get("card_no").equals(s.issue(1).get("card_no")));
 check("reuse campus card",s.info(1).get("card_no").equals(c.getBean(CardService.class).info(1).get("card_no")));
 reject("unknown user",()->s.issue(999));
 String k=key();long recharge=s.trade(1,"RECHARGE",new BigDecimal("100"),null,null,k,1);
 check("duplicate request",recharge==s.trade(1,"RECHARGE",new BigDecimal("100"),null,null,k,1));
 reject("conflicting retry",()->s.trade(1,"RECHARGE",new BigDecimal("99"),null,null,k,1));
 long pay=s.trade(1,"PAY",new BigDecimal("30"),1L,null,key(),1);
 s.trade(1,"REFUND",new BigDecimal("10"),null,pay,key(),1);
 reject("refund overflow",()->s.trade(1,"REFUND",new BigDecimal("21"),null,pay,key(),1));
 reject("insufficient funds",()->s.trade(1,"PAY",new BigDecimal("81"),1L,null,key(),1));
 reject("invalid precision",()->s.trade(1,"PAY",new BigDecimal("0.001"),1L,null,key(),1));
 reject("negative amount",()->s.trade(1,"RECHARGE",new BigDecimal("-1"),null,null,key(),1));
 reject("unknown merchant",()->s.trade(1,"PAY",BigDecimal.ONE,999L,null,key(),1));
 s.status(1,"LOST");reject("lost card",()->s.trade(1,"PAY",BigDecimal.ONE,1L,null,key(),1));s.status(1,"ACTIVE");
 ExecutorService pool=Executors.newFixedThreadPool(2);List<Future<Boolean>> fs=new ArrayList<>();for(int i=0;i<2;i++)fs.add(pool.submit(()->{try{s.trade(1,"PAY",new BigDecimal("60"),2L,null,key(),1);return true;}catch(IllegalArgumentException e){return false;}}));int success=0;for(Future<Boolean> f:fs)if(f.get())success++;pool.shutdown();check("concurrent overspend",success==1);
 check("final balance",((BigDecimal)s.info(1).get("balance")).compareTo(new BigDecimal("20"))==0);
 check("no failed ledger rows",s.ledger(1).size()==4);
 for(Map<String,Object> r:s.reconcile())check("ledger reconciliation",((BigDecimal)r.get("difference")).signum()==0);
 db.update("update sys_user set status='1' where user_id=1");reject("disabled account",()->s.trade(1,"PAY",BigDecimal.ONE,1L,null,key(),1));
 System.out.println("PASS TOTAL "+count);
 }}
}
