package com.abcplusd.akka.sample1;

import static akka.actor.SupervisorStrategy.escalate;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.OneForOneStrategy;
import akka.actor.Props;
import akka.actor.SupervisorStrategy;
import akka.japi.pf.DeciderBuilder;
import akka.japi.pf.ReceiveBuilder;
import scala.concurrent.duration.Duration;

public class Supervisor extends AbstractActor{
  
  public static final OneForOneStrategy STRATEGY = new OneForOneStrategy(
      10,
      Duration.create("10 seconds"),
      DeciderBuilder
      .match(RuntimeException.class, ex -> escalate())
      .build()
      );
  
  {
    // /user/supervisor/child
    final ActorRef child = getContext().actorOf(NonTrustworthyChild.props(), "Child");
    
    receive(ReceiveBuilder
        .matchAny(any -> child.forward(any, getContext()))
        .build()
        );
        
  }
  
  @Override
  public SupervisorStrategy supervisorStrategy() {
    return STRATEGY; 
  }
  
  public static Props props() {
    return Props.create(Supervisor.class);
  }

}
