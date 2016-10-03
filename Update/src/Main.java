import com.github.axet.wget.WGet;

import java.io.File;
import java.io.IOException;
import java.net.URL;

/*******************************************************************************
 * Created by Himmelt on 2016/10/1.
 * Copyright (c) 2015-2016. Himmelt All rights reserved.
 * https://opensource.org/licenses/MIT
 ******************************************************************************/

public class Main {
    public static void main(String[] args) {
        System.out.println("updating...");
        if (args.length == 1 && args[0].endsWith(".exe")) {
            File file = new File(args[0]);
            System.out.println(file.getPath());
            file = file.getAbsoluteFile();
            System.out.println(file.getPath());
            try {
                file.delete();
                WGet wGet = new WGet(new URL("https://dn-stc.qbox.me/soraclient/SoraClient.exe"), file);
                wGet.download();
                Runtime.getRuntime().exec(file.getAbsolutePath());
                System.out.println("finished");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("args not match!");
        }
    }
}
