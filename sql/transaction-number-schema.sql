-- Execute once against the previous schema, then run transaction-number-backfill.sql.
ALTER TABLE campus_ledger ADD transaction_no VARCHAR(48) NULL, ADD UNIQUE KEY uk_transaction_no(transaction_no);
