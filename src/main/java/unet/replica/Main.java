package unet.replica;

import unet.replica.handlers.Command;
import unet.replica.handlers.Replica;
import unet.replica.handlers.Replicate;
import unet.replica.libs.json.io.JsonReader;
import unet.replica.libs.json.variables.JsonArray;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class Main {

    public static void main(String[] args){
        System.out.println("\u001B[35m  _____  ______ _____  _      _____ _____");
        System.out.println("\u001B[35m |  __ \\|  ____|  __ \\| |    |_   _/ ____|   /\\");
        System.out.println("\u001B[35m | |__) | |__  | |__) | |      | || |       /  \\");
        System.out.println("\u001B[35m |  _  /|  __| |  ___/| |      | || |      / /\\ \\");
        System.out.println("\u001B[35m | | \\ \\| |____| |    | |____ _| || |____ / ____ \\");
        System.out.println("\u001B[35m |_|  \\_\\______|_|    |______|_____\\_____/_/    \\_\\");
        System.out.println("\u001B[0m");

        try{
            File f = new File("config.json");
            JsonReader r = new JsonReader(new FileInputStream(f));
            JsonArray j = r.readJsonArray();

            for(int i = 0; i < j.size(); i++){
                Replica z;
                switch(j.getJsonObject(i).getString("type")){
                    case "command":
                        if(j.getJsonObject(i).containsKey("directory")){
                            z = new Command(j.getJsonObject(i).getJsonArray("list"), j.getJsonObject(i).getString("directory"));

                        }else{
                            z = new Command(j.getJsonObject(i).getJsonArray("command"));
                        }
                        break;

                    case "replicate":
                        z = new Replicate(j.getJsonObject(i).getString("from"), j.getJsonObject(i).getString("to"));
                        break;

                    default:
                        continue;
                }

                z.execute();
            }

            //System.out.println(j.toString());

        }catch(IOException e){
            e.printStackTrace();
        }
    }


}