import akka.actor.UntypedActor;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * Created by abbas on 11/21/15.
 */
public class WorkerClass extends UntypedActor{


    static ArrayList<Byte> characters = new ArrayList<>();

    public static ArrayList<Byte> read( int start, int end ) throws FileNotFoundException {

        byte[] bytes = new byte[0];
        String fPath = System.getProperty("user.dir")+"/bigFatFile_2.4MB.txt";
        try {
            bytes = Files.readAllBytes(Paths.get(fPath));
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (int i = start; i < end; i++) {
            characters.add(bytes[i]);
        }
        return characters;

    }

    @Override
    public void onReceive(Object message) throws Exception {

        if (message instanceof int[]) {

            int msg[] = (int[]) message;
            int start = msg[0];
            int end = msg[1];

            getSender().tell(read(start,end), getSelf());
        }else {
            unhandled(message);
        }
    }
}
