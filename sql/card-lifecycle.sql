CREATE TABLE IF NOT EXISTS campus_card_cancelled (
 card_no VARCHAR(34) PRIMARY KEY,user_id BIGINT NOT NULL,write_count INT NOT NULL,
 last_written_at DATETIME NULL,created_at TIMESTAMP NULL,
 cancelled_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,cancelled_by BIGINT NOT NULL,
 KEY ix_cancelled_user(user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
