package com.project.web.controller.system;
import com.project.system.service.MerchantService;
import com.project.common.utils.ShiroUtils;
import java.util.*;
import javax.servlet.http.HttpSession;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
@Controller @RequestMapping("/campus/merchant") public class MerchantController {
 @Autowired private MerchantService service;
 @Autowired private com.project.system.service.MerchantConsumptionService consumption;
 private void allowed(){if(!SecurityUtils.getSubject().hasRole("admin")&&!SecurityUtils.getSubject().isPermitted("campus:finance:manage")&&!SecurityUtils.getSubject().isPermitted("campus:merchant:view")&&!SecurityUtils.getSubject().isPermitted("campus:consumption:view"))throw new org.apache.shiro.authz.UnauthorizedException("需要商户管理权限");}
 private void token(HttpSession s,String t){if(t==null||!t.equals(s.getAttribute("merchantToken")))throw new IllegalArgumentException("页面已过期，请刷新");}
 @GetMapping public String page(Model model,HttpSession session){allowed();if(session.getAttribute("merchantToken")==null)session.setAttribute("merchantToken",UUID.randomUUID().toString());model.addAttribute("token",session.getAttribute("merchantToken"));for(String key:Arrays.asList("save","status","category","upload"))model.addAttribute("can"+key,SecurityUtils.getSubject().isPermitted("campus:merchant:"+key));model.addAttribute("canapi",SecurityUtils.getSubject().isPermitted("campus:api:manage"));model.addAttribute("canpay",SecurityUtils.getSubject().isPermitted("campus:consumption:pay"));return "merchant";}
 @GetMapping("/search") @ResponseBody public Object search(String name,String number,String category,Integer active,@RequestParam(defaultValue="1")int page,String owner,String start,String end){allowed();return service.search(name,number,category,active,page,owner,start,end);}
 @GetMapping("/detail") @ResponseBody public Object detail(long id,@RequestParam(defaultValue="1")int page){allowed();return service.detail(id,page);}
 @PostMapping("/save") @ResponseBody public Object save(Long id,String name,String category,String contact,String phone,String address,String remark,String token,HttpSession session,@RequestParam Map<String,String> extras){allowed();token(session,token);return Collections.singletonMap("id",service.saveFull(id,name,category,contact,phone,address,remark,ShiroUtils.getUserId(),extras));}
 @PostMapping("/status") @ResponseBody public Object status(long id,int active,String reason,String token,HttpSession session){allowed();token(session,token);service.status(id,active,reason,ShiroUtils.getUserId());return Collections.singletonMap("ok",true);}
 @GetMapping("/categories") @ResponseBody public Object categories(){allowed();return service.categories();}
 @PostMapping("/category") @ResponseBody public Object category(String name,String token,HttpSession session){allowed();token(session,token);return Collections.singletonMap("code",service.addCategory(name,ShiroUtils.getUserId()));}
 @PostMapping("/upload") @ResponseBody public Object upload(@RequestParam org.springframework.web.multipart.MultipartFile file,String token,HttpSession session)throws java.io.IOException{allowed();token(session,token);if(file.getSize()>3*1024*1024)throw new IllegalArgumentException("图片不能超过3MB");return Collections.singletonMap("id",service.image(file.getBytes(),ShiroUtils.getUserId()));}
 @GetMapping("/image/{id}") public org.springframework.http.ResponseEntity<byte[]> image(@PathVariable String id){allowed();Map<String,Object> row=service.image(id);return org.springframework.http.ResponseEntity.ok().header("X-Content-Type-Options","nosniff").header("Cache-Control","private, no-store").contentType(org.springframework.http.MediaType.parseMediaType((String)row.get("mime"))).body((byte[])row.get("content"));}
 @GetMapping("/consumption") public String consumption(Model model,HttpSession session){page(model,session);return "merchant-consumption";}
 @GetMapping("/consumption/merchants") @ResponseBody public Object consumptionMerchants(){allowed();return consumption.merchants();}
 @GetMapping("/consumption/data") @ResponseBody public Object consumptionData(Long merchant,String student,String phone,String start,String end,@RequestParam(defaultValue="1")int page){allowed();return consumption.query(merchant,student,phone,start,end,page);}
 @PostMapping("/consumption/pay") @ResponseBody public Object consumptionPay(long merchant,String student,java.math.BigDecimal amount,String key,String token,HttpSession session){allowed();token(session,token);return consumption.consume(merchant,student,amount,key,ShiroUtils.getUserId());}
 @ExceptionHandler(IllegalArgumentException.class) @ResponseBody @ResponseStatus(org.springframework.http.HttpStatus.BAD_REQUEST) public Object error(IllegalArgumentException e){return Collections.singletonMap("error",e.getMessage());}
}

