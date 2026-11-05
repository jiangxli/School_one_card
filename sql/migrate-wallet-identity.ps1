param([string]$Database='school_one_card')
$ErrorActionPreference='Stop'
if($Database -notmatch '^[a-zA-Z0-9_]+$'){throw 'Invalid database name'}
$mysql=Join-Path $PSScriptRoot '../../school_one_card/.local/mysql/mysql-5.7.44-winx64/bin/mysql.exe'
function Query([string]$sql){$result=& $mysql --host=127.0.0.1 --port=13306 --user=root --default-character-set=utf8mb4 --batch --skip-column-names $Database --execute=$sql;if($LASTEXITCODE -ne 0){throw 'Migration SQL failed'};return $result}
$duplicates=Query 'SELECT login_name FROM sys_user GROUP BY login_name HAVING COUNT(*)>1'
if($duplicates){throw 'Duplicate student numbers; resolve before migration'}
$keys=Query "SELECT DISTINCT CONSTRAINT_NAME FROM information_schema.KEY_COLUMN_USAGE WHERE TABLE_SCHEMA='$Database' AND TABLE_NAME='campus_wallet' AND REFERENCED_TABLE_NAME='campus_card'"
foreach($key in $keys){Query "ALTER TABLE campus_wallet DROP FOREIGN KEY ``$key``"|Out-Null}
$column=Query "SELECT COUNT(*) FROM information_schema.COLUMNS WHERE TABLE_SCHEMA='$Database' AND TABLE_NAME='campus_wallet' AND COLUMN_NAME='student_no'"
if([int]$column -eq 0){Query 'ALTER TABLE campus_wallet ADD student_no VARCHAR(30) NULL'|Out-Null}
Query 'UPDATE campus_wallet w JOIN sys_user u ON w.user_id=u.user_id SET w.student_no=u.login_name'|Out-Null
$old=Query "SELECT COUNT(*) FROM information_schema.COLUMNS WHERE TABLE_SCHEMA='$Database' AND TABLE_NAME='campus_wallet' AND COLUMN_NAME='card_no'"
if([int]$old -eq 1){Query 'ALTER TABLE campus_wallet DROP COLUMN card_no'|Out-Null}
$idx=Query "SELECT COUNT(*) FROM information_schema.STATISTICS WHERE TABLE_SCHEMA='$Database' AND TABLE_NAME='campus_wallet' AND INDEX_NAME='uk_student_no'"
if([int]$idx -eq 0){Query 'ALTER TABLE campus_wallet MODIFY student_no VARCHAR(30) NOT NULL, ADD UNIQUE KEY uk_student_no(student_no)'|Out-Null}
$idx=Query "SELECT COUNT(*) FROM information_schema.STATISTICS WHERE TABLE_SCHEMA='$Database' AND TABLE_NAME='sys_user' AND INDEX_NAME='uk_login_name'"
if([int]$idx -eq 0){Query 'ALTER TABLE sys_user ADD UNIQUE KEY uk_login_name(login_name)'|Out-Null}
Query (Get-Content (Join-Path $PSScriptRoot 'card-lifecycle.sql') -Raw)|Out-Null
Query "INSERT INTO campus_wallet(user_id,student_no) SELECT u.user_id,u.login_name FROM sys_user u LEFT JOIN campus_wallet w ON w.user_id=u.user_id WHERE u.del_flag='0' AND w.user_id IS NULL"|Out-Null
Write-Output 'Migration completed; existing wallet balances and ledgers preserved.'
