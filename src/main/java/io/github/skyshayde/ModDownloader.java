package io.github.skyshayde;

import net.minecraftforge.fml.relauncher.FMLInjectionData;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 * Created by skysh on 8/20/2017.
 */
public class ModDownloader {
    static File mcDir = (File) FMLInjectionData.data()[6];
    private static File modsDir = new File(mcDir, "mods");
    private static File cfgDir = new File(mcDir, "config");

    public static void downloadList(String serverURL, List<Map> modslist, List<Map> cfglist) {
        modslist.forEach(fileName -> {
            String fname = fileName.get("path").toString().replace("\\", "/");
            download(serverURL + "/mods/" + fname, fname, fileName.get("hash").toString(), true);
        });
        cfglist.forEach(fileName -> {
            String fname = fileName.get("path").toString().replace("\\", "/");
            download(serverURL + "/config/" + fname, fname, fileName.get("hash").toString(), false);
        });
    }

    public static void download(String url, String fileName, String hash, boolean isMod) {
        url = url.replace(" ", "%20");
        if (fileName.equals("modsync.cfg")) {
            return;
        }
        File f = null;
        if (isMod == true) {
            f = new File(modsDir + "/" + fileName);
        } else {
            f = new File(cfgDir + "/" + fileName);
        }
        // If the file exists we only want to copy it if the hash is different on the server.  We'll just assume the server version is right
        if (f.exists()) {
            try {
                if (DigestUtils.sha1Hex(new FileInputStream(f)).equals(hash)) {
                    return;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        InputStream in = null;
        try {
            System.out.println("Downloading " + fileName);
            FileUtils.copyURLToFile(new URL(url), f);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

