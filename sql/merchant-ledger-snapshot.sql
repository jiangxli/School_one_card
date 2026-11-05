-- Run once before the new application version.
ALTER TABLE campus_ledger ADD merchant_name_snapshot VARCHAR(100) NULL;
UPDATE campus_ledger l JOIN campus_merchant m ON m.id=l.merchant_id SET l.merchant_name_snapshot=m.name WHERE l.merchant_name_snapshot IS NULL;
