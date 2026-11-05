package com.project.web.controller.system;

import com.project.common.utils.ShiroUtils;
import com.project.system.service.WalletService;
import java.math.BigDecimal;
import java.util.*;
import javax.servlet.http.HttpSession;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/campus/wallet")
public class WalletController {
    @Autowired private WalletService wallet;
    private boolean finance(){return SecurityUtils.getSubject().hasRole("admin")||SecurityUtils.getSubject().isPermitted("campus:finance:manage")||SecurityUtils.getSubject().isPermitted("campus:wallet:list")||SecurityUtils.getSubject().isPermitted("campus:wallet:recharge")||SecurityUtils.getSubject().isPermitted("campus:wallet:debit")||SecurityUtils.getSubject().isPermitted("campus:wallet:status")||SecurityUtils.getSubject().isPermitted("campus:wallet:verify")||SecurityUtils.getSubject().isPermitted("campus:wallet:report");}
    private long target(Long user){long self=ShiroUtils.getUserId();if(user!=null&&user!=self&&!finance())throw new IllegalArgumentException("不可操作他人钱包");return user==null?self:user;}
    private void token(HttpSession session,String token){if(token==null||!token.equals(session.getAttribute("walletToken")))throw new IllegalArgumentException("请求校验失败，请刷新页面");}
    @GetMapping public String page(Model model,HttpSession session){if(session.getAttribute("walletToken")==null)session.setAttribute("walletToken",UUID.randomUUID().toString());model.addAttribute("token",session.getAttribute("walletToken"));model.addAttribute("finance",finance());model.addAttribute("selfId",ShiroUtils.getUserId());for(String key:Arrays.asList("recharge","debit","status","verify","list"))model.addAttribute("can"+key,SecurityUtils.getSubject().isPermitted("campus:wallet:"+key));return "wallet-list";}

    private void requireFinance(){if(!finance())throw new org.apache.shiro.authz.UnauthorizedException("需要钱包管理权限");}
    @GetMapping("/search") @ResponseBody public Object search(String name,String identity,String card,String student,@RequestParam(defaultValue="1") int page){requireFinance();return wallet.search(name,identity,card,student,page);}
    @GetMapping("/detail/{user}") public String detail(@PathVariable Long user,Model model,HttpSession session){target(user);page(model,session);model.addAttribute("userId",user);return "wallet-detail";}
    @GetMapping("/person") @ResponseBody public Object person(Long user){return wallet.person(target(user));}
    @GetMapping("/history") @ResponseBody public Object history(Long user,String kind,String start,String end,Long merchant,@RequestParam(defaultValue="1") int page){return wallet.history(target(user),kind,start,end,merchant,page);}
    @PostMapping("/verification") @ResponseBody public Object verification(Long user,String identity,String phone,String token,HttpSession session){requireFinance();token(session,token);wallet.completeVerification(target(user),identity,phone);return Collections.singletonMap("ok",true);}
    @GetMapping("/payment/capabilities") @ResponseBody public Object payment(){requireFinance();return Collections.singletonMap("configured",false);}
    @RequestMapping(value={"/payment/create","/payment/query","/payment/close","/payment/notify","/payment/refund"},method=RequestMethod.POST)
    @ResponseBody @ResponseStatus(org.springframework.http.HttpStatus.SERVICE_UNAVAILABLE)
    public Object paymentUnavailable(){return Collections.singletonMap("error","支付平台未配置，未创建支付订单或执行入账");}
    @GetMapping("/data") @ResponseBody public Map<String,Object> data(Long user){long id=target(user);Map<String,Object> result=new LinkedHashMap<>();result.put("wallet",wallet.info(id));result.put("ledger",wallet.ledger(id));result.put("merchants",wallet.merchants());return result;}
    @PostMapping("/issue") @ResponseBody public Object issue(Long user,String token,HttpSession session){token(session,token);return wallet.issue(target(user));}
    @PostMapping("/status") @ResponseBody public Object status(Long user,String status,String token,HttpSession session){token(session,token);if(!"LOST".equals(status)&&!finance())throw new IllegalArgumentException("解挂需要财务权限");wallet.status(target(user),status);return Collections.singletonMap("ok",true);}
    @PostMapping("/trade") @ResponseBody public Object trade(Long user,String kind,BigDecimal amount,Long merchant,Long original,String key,String token,HttpSession session){token(session,token);if(!"PAY".equals(kind)&&!finance())throw new IllegalArgumentException("充值和退款需要财务权限");long id=wallet.trade(target(user),kind,amount,merchant,original,key,ShiroUtils.getUserId());return wallet.receipt(id);}
    @PostMapping("/debit") @ResponseBody public Object debit(Long user,BigDecimal amount,String key,String token,HttpSession session){requireFinance();token(session,token);long id=wallet.trade(target(user),"PAY",amount,3L,null,key,ShiroUtils.getUserId());return wallet.receipt(id);}
    @GetMapping("/reconcile") @ResponseBody public Object reconcile(){if(!finance())throw new IllegalArgumentException("需要财务权限");return wallet.reconcile();}
    @ExceptionHandler(IllegalArgumentException.class) @ResponseBody @ResponseStatus(org.springframework.http.HttpStatus.BAD_REQUEST)
    public Object invalid(IllegalArgumentException e){return Collections.singletonMap("error",e.getMessage());}
}

