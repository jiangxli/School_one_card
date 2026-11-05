import java.util.*;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.context.Context;
import org.thymeleaf.context.IExpressionContext;
import org.thymeleaf.linkbuilder.ILinkBuilder;
public class WalletTemplateRenderCheck {
 public static void main(String[]args){SpringTemplateEngine engine=new SpringTemplateEngine();ClassLoaderTemplateResolver resolver=new ClassLoaderTemplateResolver();resolver.setCharacterEncoding("UTF-8");resolver.setPrefix("templates/");resolver.setSuffix(".html");resolver.setTemplateMode("HTML");engine.setTemplateResolver(resolver);engine.setLinkBuilder(new ILinkBuilder(){public String getName(){return "test";}public Integer getOrder(){return 1;}public String buildLink(IExpressionContext c,String base,Map<String,Object> p){return base;}});Context ctx=new Context(Locale.CHINA);ctx.setVariable("finance",true);ctx.setVariable("token","test-token");ctx.setVariable("userId",2L);ctx.setVariable("selfId",2L);ctx.setVariable("canRole",true);ctx.setVariable("canGrant",true);ctx.setVariable("canAssign",true);ctx.setVariable("merchant",1L);ctx.setVariable("today","2026-11-04");ctx.setVariable("canrun",true);ctx.setVariable("canimport",true);ctx.setVariable("canhandle",true);ctx.setVariable("canreview",true);ctx.setVariable("canexport",true);String finance=engine.process("finance",ctx);if(!finance.contains("batchDialog")||!finance.contains("importBill()"))throw new AssertionError("finance template failed");String api=engine.process("merchant-api",ctx);if(!api.contains("X-Signature")||!api.contains("requestBody"))throw new AssertionError("merchant API template failed");String permissions=engine.process("permission",ctx);if(!permissions.contains("saveBinding()"))throw new AssertionError("permission template failed");String consumption=engine.process("merchant-consumption",ctx);if(!consumption.contains("confirmPay"))throw new AssertionError("consumption template failed");String merchant=engine.process("merchant",ctx);if(!merchant.contains("fetchDetail()"))throw new AssertionError("merchant template failed");String list=engine.process("wallet-list",ctx);if(!list.contains("pagination-bar")||!list.contains("search-actions"))throw new AssertionError("list render failed");String html=engine.process("wallet-detail",ctx);if(!html.contains("function beginRecharge()")||!html.contains("verificationDialog")||!html.contains("正在加载用户基本信息")||html.contains("返回钱包查询")||html.contains("人员钱包详情"))throw new AssertionError();System.out.println("PASS full wallet detail template renders, basic info precedes recharge, handlers preserved");}
}






