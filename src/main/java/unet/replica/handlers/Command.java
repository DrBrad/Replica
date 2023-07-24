package unet.replica.handlers;

import unet.replica.libs.json.variables.JsonArray;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Command extends Replica {

    private JsonArray cmd;
    private File directory;

    public Command(JsonArray cmd){
        this.cmd = cmd;
    }

    public Command(JsonArray cmd, String path){
        this.cmd = cmd;
        this.directory = new File(path);
    }

    public void execute(){
        List<String> command = new ArrayList<>();

        for(int i = 0; i < cmd.size(); i++){
            command.add(cmd.getString(i));
        }

        ProcessBuilder pb = new ProcessBuilder(command);

        if(directory != null){
            if(!directory.exists()){
                directory.mkdirs();
            }

            pb.directory(directory);
        }

        try{
            pb.start();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
