-- Additive migration; original tables and archived cc_ experiments remain untouched.
CREATE TABLE IF NOT EXISTS campus_wallet (
 user_id BIGINT PRIMARY KEY, card_no VARCHAR(34) NOT NULL UNIQUE,
 status VARCHAR(12) NOT NULL DEFAULT 'ACTIVE', balance DECIMAL(12,2) NOT NULL DEFAULT 0,
 created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
 FOREIGN KEY(user_id) REFERENCES campus_card(user_id),
 FOREIGN KEY(card_no) REFERENCES campus_card(card_no)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
CREATE TABLE IF NOT EXISTS campus_merchant (
 id BIGINT PRIMARY KEY, name VARCHAR(100) NOT NULL, active TINYINT NOT NULL DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
CREATE TABLE IF NOT EXISTS campus_ledger (
 id BIGINT PRIMARY KEY AUTO_INCREMENT, user_id BIGINT NOT NULL,
 kind VARCHAR(12) NOT NULL, amount DECIMAL(12,2) NOT NULL,
 balance_after DECIMAL(12,2) NOT NULL, merchant_id BIGINT NULL, original_id BIGINT NULL,
 request_key VARCHAR(64) NOT NULL, actor_id BIGINT NOT NULL,
 created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
 UNIQUE KEY uk_wallet_request(user_id,request_key), KEY ix_wallet_original(original_id),
 FOREIGN KEY(user_id) REFERENCES campus_wallet(user_id),
 FOREIGN KEY(merchant_id) REFERENCES campus_merchant(id),
 FOREIGN KEY(original_id) REFERENCES campus_ledger(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
INSERT INTO campus_merchant(id,name) VALUES(1,'校园食堂'),(2,'校园超市') ON DUPLICATE KEY UPDATE id=id;
