import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.routing.*;

import java.util.ArrayList;

/**
 * Created by abbas on 11/21/15.
 */
public class SeparetActorsClass extends UntypedActor {

    private Router WorkerRouter;
    private int numberOfWorker;
    private int numberOfResult;
    ArrayList<Routee> routees = new ArrayList<>();

    public SeparetActorsClass(int numWork){

        numberOfWorker = numWork;
        for (int i = 0; i < numberOfWorker; i++){
            ActorRef r = getContext().actorOf(Props.create(WorkerClass.class));
            getContext().watch(r);
            routees.add(new ActorRefRoutee(r));

        }

        WorkerRouter = new Router(new BalancingRoutingLogic(), routees);

    }

    @Override
    public void onReceive(Object message) throws Exception {

        if (message instanceof int[]){
            int msg[] = (int[]) message;
            int start = msg[0];
            int end = msg[1];


            int numberOfNumber = end - start;
            int segmantLength =  numberOfNumber / numberOfWorker;
            for (int i = 0; i < numberOfWorker; i++){
                int startNumber = start + (i* segmantLength);
                int endNumber = startNumber + segmantLength - 1;

                if (1 == numberOfWorker - 1)
                    endNumber = end;

                int send[] = {startNumber, endNumber};
                WorkerRouter.route(send, getSelf());

            }
        }else if (message instanceof ArrayList){
            ArrayList<Byte> result  = (ArrayList<Byte>) message;
            for (int n: result) {
                    System.out.print((char) n);
            }

            if (++numberOfResult >= numberOfWorker)
                getContext().stop(getSelf());
            getContext().system().terminate();


        }else {
            unhandled(message);
        }
    }
}
