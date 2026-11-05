CREATE TABLE IF NOT EXISTS campus_recharge_order(
 id BIGINT PRIMARY KEY AUTO_INCREMENT,order_no VARCHAR(48) NULL UNIQUE,
 user_id BIGINT NOT NULL,amount DECIMAL(12,2) NOT NULL,status VARCHAR(16) NOT NULL,
 ledger_id BIGINT NOT NULL UNIQUE,request_key VARCHAR(64) NOT NULL,actor_id BIGINT NOT NULL,
 created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,paid_at DATETIME NULL,
 UNIQUE KEY uk_recharge_request(user_id,request_key),
 FOREIGN KEY(ledger_id) REFERENCES campus_ledger(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
INSERT INTO campus_recharge_order(user_id,amount,status,ledger_id,request_key,actor_id,created_at,paid_at)
SELECT l.user_id,l.amount,'SUCCESS',l.id,l.request_key,l.actor_id,l.created_at,l.created_at
FROM campus_ledger l LEFT JOIN campus_recharge_order o ON o.ledger_id=l.id WHERE l.kind='RECHARGE' AND o.id IS NULL;
UPDATE campus_recharge_order SET order_no=concat('DD',date_format(created_at,'%Y%m%d%H%i%s'),'-',lpad(cast(id as char),20,'0')) WHERE order_no IS NULL;
