import java.nio.file.*;import java.net.*;import java.util.*;
/** IDEA entry point for the pinned offline JUnit runtime in verification/tools. */
public class VerificationJUnitLauncher {
 public static void main(String[] args)throws Exception {
  List<URL> urls=new ArrayList<>();for(String name:new String[]{"verification/classes","verification/tools/junit-4.13.2.jar","verification/tools/hamcrest-core-1.3.jar"})urls.add(Paths.get(name).toAbsolutePath().toUri().toURL());
  String cp=System.getProperty("java.class.path")+";"+Paths.get("verification/classes").toAbsolutePath()+";"+Paths.get("verification/tools/junit-4.13.2.jar").toAbsolutePath()+";"+Paths.get("verification/tools/hamcrest-core-1.3.jar").toAbsolutePath();System.setProperty("java.class.path",cp);
  try(URLClassLoader loader=new URLClassLoader(urls.toArray(new URL[0]),VerificationJUnitLauncher.class.getClassLoader())){Thread.currentThread().setContextClassLoader(loader);Class.forName("JUnitReportRunner",true,loader).getMethod("main",String[].class).invoke(null,(Object)new String[]{args.length>0?args[0]:"BridgeIntegrationTest"});}
 }
}
