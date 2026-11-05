package com.project.system.service;

import java.math.BigDecimal;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** Simulated campus funds only. Every balance change has an immutable ledger entry. */
@Service
public class WalletService {
    @Autowired private JdbcTemplate db;


    private void activeUser(long user) {
        if (db.queryForList("select user_id from sys_user where user_id=? and del_flag='0' and status='0' for update", user).isEmpty())
            throw new IllegalArgumentException("用户不存在或已停用");
    }

    @Transactional public Map<String,Object> issue(long user) {
        List<Map<String,Object>> users=db.queryForList("select login_name from sys_user where user_id=? and del_flag='0' for update",user);
        if(users.isEmpty()) throw new IllegalArgumentException("用户不存在");
        String number=String.valueOf(users.get(0).get("login_name"));
        if(number.trim().isEmpty()) throw new IllegalArgumentException("学工号不能为空");
        if(info(user).isEmpty()) db.update("insert into campus_wallet(user_id,student_no) values(?,?)",user,number);
        return info(user);
    }

    public Map<String,Object> info(long user) {
        List<Map<String,Object>> rows=db.queryForList("select * from campus_wallet where user_id=?",user);
        return rows.isEmpty()?Collections.emptyMap():rows.get(0);
    }

    @Transactional public void status(long user,String status) {
        activeUser(user);
        if (!Arrays.asList("ACTIVE","LOST").contains(status)) throw new IllegalArgumentException("状态无效");
        if(db.update("update campus_wallet set status=? where user_id=?",status,user)!=1) throw new IllegalArgumentException("请先开通钱包");
    }

    @Transactional public long trade(long user,String kind,BigDecimal amount,Long merchant,Long original,String key,long actor) {
        if(amount==null||amount.signum()<=0||amount.scale()>2||amount.compareTo(new BigDecimal("100000"))>0)
            throw new IllegalArgumentException("金额须为0.01至100000元且最多两位小数");
        if(key==null||!key.matches("[a-zA-Z0-9-]{16,64}")) throw new IllegalArgumentException("请求编号无效");
        if(!Arrays.asList("RECHARGE","PAY","REFUND").contains(kind)) throw new IllegalArgumentException("交易类型无效");
        activeUser(user);
        List<Map<String,Object>> wallets=db.queryForList("select * from campus_wallet where user_id=? for update",user);
        if(wallets.isEmpty()) throw new IllegalArgumentException("请先开通钱包");
        List<Map<String,Object>> previous=db.queryForList("select * from campus_ledger where user_id=? and request_key=?",user,key);
        if(!previous.isEmpty()) {
            Map<String,Object> row=previous.get(0);
            if(!kind.equals(row.get("kind"))||amount.compareTo((BigDecimal)row.get("amount"))!=0
                ||!Objects.equals(merchant,row.get("merchant_id"))||!Objects.equals(original,row.get("original_id"))
                ||((Number)row.get("actor_id")).longValue()!=actor) throw new IllegalArgumentException("请求编号已被不同交易使用");
            return ((Number)row.get("id")).longValue();
        }
        if("RECHARGE".equals(kind) && db.queryForObject("select count(*) from campus_person_identity where user_id=? and identity_no is not null and identity_no<>''",Integer.class,user)==0)
            throw new IllegalArgumentException("请先补充身份证及校验信息后再充值");
        Map<String,Object> wallet=wallets.get(0);
        if("LOST".equals(wallet.get("status")) && "PAY".equals(kind)) throw new IllegalArgumentException("钱包已挂失，禁止消费或划扣；仍可充值");
        if(!Arrays.asList("ACTIVE","LOST").contains(String.valueOf(wallet.get("status"))))throw new IllegalArgumentException("钱包状态异常，暂不可交易");
        if("PAY".equals(kind)) {
            if(original!=null||merchant==null||db.queryForList("select id from campus_merchant where id=? and active=1 for update",merchant).isEmpty())
                throw new IllegalArgumentException("消费商户无效");
        } else if("REFUND".equals(kind)) {
            if(merchant!=null||original==null) throw new IllegalArgumentException("退款须指定原消费流水");
            List<Map<String,Object>> source=db.queryForList("select * from campus_ledger where id=? and user_id=? and kind='PAY'",original,user);
            if(source.isEmpty()) throw new IllegalArgumentException("原消费不存在");
            BigDecimal refunded=db.queryForObject("select coalesce(sum(amount),0) from campus_ledger where original_id=? and kind='REFUND'",BigDecimal.class,original);
            if(refunded.add(amount).compareTo((BigDecimal)source.get(0).get("amount"))>0) throw new IllegalArgumentException("累计退款超过原消费");
        } else if(merchant!=null||original!=null) throw new IllegalArgumentException("充值参数无效");
        BigDecimal balance=(BigDecimal)wallet.get("balance");
        BigDecimal next="PAY".equals(kind)?balance.subtract(amount):balance.add(amount);
        if(next.signum()<0) throw new IllegalArgumentException("余额不足");
        if(next.compareTo(new BigDecimal("9999999999.99"))>0) throw new IllegalArgumentException("余额超限");
        db.update("update campus_wallet set balance=? where user_id=?",next,user);
        db.update("insert into campus_ledger(user_id,kind,amount,balance_after,merchant_id,original_id,request_key,actor_id) values(?,?,?,?,?,?,?,?)",user,kind,amount,next,merchant,original,key,actor);
        long id=db.queryForObject("select id from campus_ledger where user_id=? and request_key=?",Long.class,user,key);
        db.update("update campus_ledger set transaction_no=concat(case kind when 'RECHARGE' then 'CZ' when 'PAY' then 'XF' when 'REFUND' then 'TK' else 'JY' end,date_format(created_at,'%Y%m%d%H%i%s'),'-',lpad(cast(id as char),20,'0')) where id=?",id);
        if("RECHARGE".equals(kind)) {
            db.update("insert into campus_recharge_order(user_id,amount,status,ledger_id,request_key,actor_id,created_at,paid_at) select user_id,amount,'SUCCESS',id,request_key,actor_id,created_at,created_at from campus_ledger where id=?",id);
            db.update("update campus_recharge_order set order_no=concat('DD',date_format(created_at,'%Y%m%d%H%i%s'),'-',lpad(cast(id as char),20,'0')) where ledger_id=?",id);
        }
        if("PAY".equals(kind))db.update("update campus_ledger set merchant_name_snapshot=(select name from campus_merchant where id=?) where id=?",merchant,id);
        if("REFUND".equals(kind)){String snapshot=db.queryForObject("select merchant_name_snapshot from campus_ledger where id=?",String.class,original);db.update("update campus_ledger set merchant_name_snapshot=? where id=?",snapshot,id);}
        return id;
    }

    public List<Map<String,Object>> ledger(long user) {
        return db.queryForList("select l.*,coalesce(l.merchant_name_snapshot,m.name) merchant_name from campus_ledger l left join campus_merchant m on l.merchant_id=m.id where l.user_id=? order by l.id desc limit 500",user);
    }
    public List<Map<String,Object>> merchants() {return db.queryForList("select id,name from campus_merchant where active=1 order by id");}
    public List<Map<String,Object>> reconcile() {
        return db.queryForList("select w.user_id,w.balance,coalesce(sum(case when l.kind='PAY' then -l.amount else l.amount end),0) ledger_balance,w.balance-coalesce(sum(case when l.kind='PAY' then -l.amount else l.amount end),0) difference from campus_wallet w left join campus_ledger l on w.user_id=l.user_id group by w.user_id,w.balance");
    }

    public Map<String,Object> search(String name,String identity,String card,String student,int page) {
        String from=" from sys_user u join campus_wallet w on w.user_id=u.user_id left join campus_card c on c.user_id=u.user_id left join campus_person_identity i on i.user_id=u.user_id where u.del_flag='0'";
        List<Object> args=new ArrayList<>();
        if(name!=null&&!name.trim().isEmpty()){from+=" and u.user_name like ?";args.add("%"+name.trim()+"%");}
        if(identity!=null&&!identity.trim().isEmpty()){from+=" and i.identity_no=?";args.add(identity.trim().toUpperCase(Locale.ROOT));}
        if(student!=null&&!student.trim().isEmpty()){from+=" and w.student_no=?";args.add(student.trim());}
        if(card!=null&&!card.trim().isEmpty()){from+=" and (c.card_no=? or exists(select 1 from campus_card_cancelled x where x.user_id=u.user_id and x.card_no=?))";args.add(card.trim());args.add(card.trim());}
        long total=db.queryForObject("select count(*)"+from,Long.class,args.toArray());
        args.add(20);args.add((Math.max(1,Math.min(page,100000))-1)*20);
        List<Map<String,Object>> rows=db.queryForList("select u.user_id,u.user_name,u.phonenumber,w.student_no,w.balance,w.status,c.card_no,i.identity_no"+from+" order by w.student_no limit ? offset ?",args.toArray());
        for(Map<String,Object> row:rows){Object id=row.get("identity_no");if(id!=null)row.put("identity_no",id.toString().substring(0,3)+"***********"+id.toString().substring(14));}
        Map<String,Object> result=new LinkedHashMap<>();result.put("rows",rows);result.put("total",total);return result;
    }
    public Map<String,Object> person(long user){
        Map<String,Object> row=db.queryForMap("select u.user_name,u.phonenumber,w.student_no,w.balance,w.status,w.created_at,c.card_no,i.identity_no from sys_user u join campus_wallet w on w.user_id=u.user_id left join campus_card c on c.user_id=u.user_id left join campus_person_identity i on i.user_id=u.user_id where u.user_id=? and u.del_flag='0'",user);
        Object id=row.get("identity_no");if(id!=null)row.put("identity_no",id.toString().substring(0,3)+"***********"+id.toString().substring(14));return row;
    }
    public Map<String,Object> history(long user,String kind,String start,String end,Long merchant,int page){
        String where=" where l.user_id=?";List<Object> args=new ArrayList<>();args.add(user);
        if(kind!=null&&!kind.isEmpty()){if(!Arrays.asList("PAY","RECHARGE","REFUND").contains(kind))throw new IllegalArgumentException("类型无效");where+=" and l.kind=?";args.add(kind);}
        for(String date:Arrays.asList(start,end))if(date!=null&&!date.isEmpty())java.time.LocalDate.parse(date);
        if(start!=null&&!start.isEmpty()){where+=" and l.created_at>=?";args.add(start);}
        if(end!=null&&!end.isEmpty()){where+=" and l.created_at<DATE_ADD(?,INTERVAL 1 DAY)";args.add(end);}
        if(merchant!=null){where+=" and l.merchant_id=?";args.add(merchant);}
        long total=db.queryForObject("select count(*) from campus_ledger l"+where,Long.class,args.toArray());args.add(20);args.add((Math.max(1,Math.min(page,100000))-1)*20);
        Map<String,Object> out=new LinkedHashMap<>();out.put("total",total);out.put("rows",db.queryForList("select l.id,l.transaction_no,coalesce((select a.order_no from campus_api_order a where a.ledger_id=l.id),(select o.order_no from campus_recharge_order o where o.ledger_id=l.id)) order_no,l.kind,l.amount,l.balance_after,l.created_at,l.original_id,coalesce(l.merchant_name_snapshot,m.name) merchant_name,'SUCCESS' transaction_status from campus_ledger l left join campus_merchant m on l.merchant_id=m.id"+where+" order by l.id desc limit ? offset ?",args.toArray()));return out;
    }
    @Transactional public void identity(long user,String value){
        activeUser(user);String id=value==null?"":value.trim().toUpperCase(Locale.ROOT);
        if(!id.matches("[0-9]{17}[0-9X]"))throw new IllegalArgumentException("请输入18位身份证号");
        int[] weights={7,9,10,5,8,4,2,1,6,3,7,9,10,5,8,4,2};String checks="10X98765432";int sum=0;for(int n=0;n<17;n++)sum+=(id.charAt(n)-'0')*weights[n];
        if(checks.charAt(sum%11)!=id.charAt(17))throw new IllegalArgumentException("身份证校验码不正确");
        try{java.time.LocalDate birth=java.time.LocalDate.parse(id.substring(6,14),java.time.format.DateTimeFormatter.BASIC_ISO_DATE);if(birth.isAfter(java.time.LocalDate.now()))throw new IllegalArgumentException("出生日期无效");}catch(java.time.DateTimeException e){throw new IllegalArgumentException("出生日期无效");}
        db.update("insert into campus_person_identity(user_id,identity_no) values(?,?) on duplicate key update identity_no=values(identity_no)",user,id);
    }

    @Transactional public void completeVerification(long user,String id,String phone){
        activeUser(user);
        if(phone==null||!phone.matches("1[3-9][0-9]{9}"))throw new IllegalArgumentException("请输入有效的11位手机号码");
        if(db.queryForObject("select count(*) from sys_user where phonenumber=? and user_id<>? and del_flag='0'",Integer.class,phone,user)>0)
            throw new IllegalArgumentException("手机号已被其他用户使用");
        if(db.queryForObject("select count(*) from campus_person_identity where user_id=?",Integer.class,user)>0)
            throw new IllegalArgumentException("身份证已登记，请在用户管理中更正");
        identity(user,id);
        db.update("update sys_user set phonenumber=? where user_id=?",phone,user);
    }
    public Map<String,Object> receipt(long id){return db.queryForMap("select l.id,l.transaction_no,o.order_no from campus_ledger l left join campus_recharge_order o on o.ledger_id=l.id where l.id=?",id);}
}
