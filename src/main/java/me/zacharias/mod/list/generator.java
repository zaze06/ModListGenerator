package me.zacharias.mod.list;

import com.moandjiezana.toml.Toml;

import java.io.*;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class generator {
  public static void main(String[] args) throws IOException {
    String path = null;
    if(args.length > 0){
      for(String s : args){
        path = s + " ";
      }
      path = path.trim();
    }else{
      BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
      System.out.print("Input minecraft mods path: ");
      path = in.readLine();
    }
    if(path == null || path.isBlank()){
      System.out.println("Invalid path: "+path);
      return;
    }
    File modsFolder = new File(path);
    if(!modsFolder.isDirectory()){
      System.out.println("\""+path+"\" is not a directory");
    }
    File[] mods = modsFolder.listFiles((dir, name) -> name.endsWith(".jar"));
    if(mods == null || mods.length == 0){
      System.out.println("Mods folder is empty");
      return;
    }
    for(File mod : mods){
      ZipFile modZip = new ZipFile(mod.toString());
      Enumeration<? extends ZipEntry> entries = modZip.entries();
      while(entries.hasMoreElements()){
        ZipEntry zipEntry = entries.nextElement();
        if(zipEntry.getName().endsWith("mods.toml")){
          Toml modsToml = new Toml().read(modZip.getInputStream(zipEntry));
          Map<String, Object> modInfo = (HashMap<String, Object>) modsToml.getList("mods").get(0);
          System.out.println(modInfo.get("displayName")+"("+modInfo.get("modId")+"): "+modInfo.get("version"));
        }
      }
    }
  }
}
