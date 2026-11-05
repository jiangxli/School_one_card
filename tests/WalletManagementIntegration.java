import java.util.*;
import com.project.system.service.*;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
public class WalletManagementIntegration {
 static void check(boolean ok,String name){if(!ok)throw new AssertionError(name);System.out.println("PASS "+name);}
 public static void main(String[] args){try(AnnotationConfigApplicationContext c=new AnnotationConfigApplicationContext(WalletIdentityIntegration.Config.class)){
 WalletService s=c.getBean(WalletService.class);JdbcTemplate db=c.getBean(JdbcTemplate.class);
 check(((Number)s.search(null,null,null,"S002",1).get("total")).intValue()==1,"student exact search");
 check(((Number)s.search("Test Person",null,null,null,1).get("total")).intValue()==1,"name search");
 s.identity(2,"11010519491231002X");
 check(((Number)s.search(null,"11010519491231002X",null,null,1).get("total")).intValue()==1,"identity exact search");
 check("110***********002X".equals(s.person(2).get("identity_no")),"identity masked");
 try{s.identity(2,"110105194912310021");throw new AssertionError("invalid identity accepted");}catch(IllegalArgumentException expected){System.out.println("PASS invalid identity rejected");}
 check(((Number)s.history(1,"PAY",null,null,null,1).get("total")).intValue()==2,"ledger type filter");
 String old=db.queryForObject("select card_no from campus_card_cancelled where user_id=1 limit 1",String.class);
 check(((Number)s.search(null,null,old,null,1).get("total")).intValue()==1,"cancelled card tracing");
 check(((Number)s.search("NoMatch",null,null,"S002",1).get("total")).intValue()==0,"combined filters");
 System.out.println("PASS TOTAL 8");}}
}
