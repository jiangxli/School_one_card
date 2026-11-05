import org.junit.*;import static org.junit.Assert.*;import java.nio.file.*;import java.util.concurrent.TimeUnit;
public class BridgeIntegrationTest {
 void run(String name)throws Exception{Path dir=Paths.get("verification/results/junit/integration");Files.createDirectories(dir);Process p=new ProcessBuilder(System.getProperty("java.home")+"/bin/java.exe","-Dfile.encoding=UTF-8","-cp",System.getProperty("java.class.path"),name).redirectErrorStream(true).redirectOutput(dir.resolve(name+".log").toFile()).start();assertTrue(name+" timed out",p.waitFor(90,TimeUnit.SECONDS));assertEquals(name+": see integration log",0,p.exitValue());}
 @Test public void paymentSignatureIdempotenceAndConcurrency()throws Exception{run("CampusPaymentChannelIntegration");}
 @Test public void financeImportDifferenceReviewArchive()throws Exception{run("FinanceIntegration");run("FinanceEvidenceIntegration");}
 @Test public void multipleRolesRevocationAndServerGuard()throws Exception{run("PermissionIntegration");}
 @Test public void thymeleafTemplates()throws Exception{run("WalletTemplateRenderCheck");}
}
