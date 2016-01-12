import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by abbas on 11/21/15.
 */
public class ReadingClass {

    public void calculate(int start, int end){

        int[] range = {start, end};
        ActorSystem actorSystem = ActorSystem.create("SeparetActorsClass");
        Props props = Props.create(SeparetActorsClass.class, 10);
        ActorRef primeMaster = actorSystem.actorOf(props);
        primeMaster.tell(range, primeMaster);
    }

    public static void main(String[] args) throws IOException {


        String fPath = System.getProperty("user.dir")+"/bigFatFile_2.4MB.txt";
        System.out.println(fPath);
        ReadingClass calculator = new ReadingClass();
        byte[] length = Files.readAllBytes(Paths.get(fPath));
        calculator.calculate(1, length.length);

        File reader = new File(fPath);
        FileReader inputStream = new FileReader(reader);
        LineNumberReader numberReader = new LineNumberReader(inputStream);
        numberReader.skip(Long.MAX_VALUE);
        System.out.println("line: " + numberReader.getLineNumber());



    }

}
