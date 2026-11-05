CREATE TABLE IF NOT EXISTS campus_merchant_profile(
 merchant_id BIGINT PRIMARY KEY,category VARCHAR(20) NOT NULL,contact_name VARCHAR(50) NOT NULL,
 phone VARCHAR(30) NOT NULL,address VARCHAR(200) NOT NULL,remark VARCHAR(500) NOT NULL,
 created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
CREATE TABLE IF NOT EXISTS campus_merchant_sequence(id INT PRIMARY KEY,seq BIGINT NOT NULL) ENGINE=InnoDB;
INSERT IGNORE INTO campus_merchant_sequence VALUES(1,1000);
CREATE TABLE IF NOT EXISTS campus_merchant_audit(id BIGINT AUTO_INCREMENT PRIMARY KEY,merchant_id BIGINT NOT NULL,action VARCHAR(20) NOT NULL,detail TEXT NOT NULL,actor_id BIGINT NOT NULL,created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,KEY ix_merchant_audit(merchant_id,id)) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
INSERT IGNORE INTO campus_merchant_profile(merchant_id,category,contact_name,phone,address,remark) SELECT id,case when id=3 then 'SYSTEM' else 'OTHER' end,'','','','存量商户，待完善资料' FROM campus_merchant;
