package com.project.system.service;
import java.util.*;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
@Service
public class CardService {
 @Autowired private JdbcTemplate db;
 @Autowired private WalletService wallets;
 private void user(long id){if(db.queryForList("select user_id from sys_user where user_id=? and del_flag='0' for update",id).isEmpty())throw new IllegalArgumentException("用户不存在或已删除");}
 @Transactional public Map<String,Object> issue(long id){user(id);wallets.issue(id);List<Map<String,Object>> rows=db.queryForList("select * from campus_card where user_id=?",id);if(!rows.isEmpty())throw new IllegalArgumentException("已存在有效卡，请先注销原卡");String number="CC"+UUID.randomUUID().toString().replace("-", "").toUpperCase(Locale.ROOT);db.update("insert into campus_card(user_id,card_no) values(?,?)",id,number);return info(id);}
 @Transactional public void cancel(long id,long actor){user(id);Map<String,Object> card=info(id);if(card.isEmpty())throw new IllegalArgumentException("没有可注销的有效卡");db.update("insert into campus_card_cancelled(user_id,card_no,write_count,last_written_at,created_at,cancelled_by) values(?,?,?,?,?,?)",id,card.get("card_no"),card.get("write_count"),card.get("last_written_at"),card.get("created_at"),actor);db.update("delete from campus_card where user_id=?",id);}
 public List<Map<String,Object>> cancelled(long id){return db.queryForList("select * from campus_card_cancelled where user_id=? order by cancelled_at desc",id);}
 public Map<String,Object> info(long id){List<Map<String,Object>> rows=db.queryForList("select * from campus_card where user_id=?",id);return rows.isEmpty()?Collections.emptyMap():rows.get(0);}
 public List<Map<String,Object>> history(long id){return db.queryForList("select id,card_no,operator_id,written_at,payload from campus_card_write where user_id=? order by id desc limit 20",id);}
 @Transactional public Map<String,Object> write(long id,String key,long operator){user(id);if(key==null||!key.matches("[a-fA-F0-9-]{36}"))throw new IllegalArgumentException("写卡请求编号无效");Map<String,Object> card=info(id);if(card.isEmpty())throw new IllegalArgumentException("请先补充开卡");if(db.queryForObject("select count(*) from sys_user where user_id=? and status='0'",Integer.class,id)!=1)throw new IllegalArgumentException("停用用户不可写卡");List<Map<String,Object>> old=db.queryForList("select * from campus_card_write where request_key=?",key);if(!old.isEmpty()){if(((Number)old.get(0).get("user_id")).longValue()!=id)throw new IllegalArgumentException("请求编号冲突");return old.get(0);}String payload="CAMPUS-CARD-V1|"+card.get("card_no")+"|"+id;db.update("insert into campus_card_write(user_id,card_no,request_key,operator_id,payload) values(?,?,?,?,?)",id,card.get("card_no"),key,operator,payload);db.update("update campus_card set write_count=write_count+1,last_written_at=NOW() where user_id=?",id);return db.queryForMap("select * from campus_card_write where request_key=?",key);}
}
