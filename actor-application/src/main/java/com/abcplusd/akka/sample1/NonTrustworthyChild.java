package com.abcplusd.akka.sample1;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.japi.pf.ReceiveBuilder;

public class NonTrustworthyChild extends AbstractActor{
  public static class Command {}
  
  private long message = 0L;
  
  {
    receive(ReceiveBuilder
        .match(Command.class, this::onCommand)
        .build()
        );
  }
  
  public void onCommand(Command command) {
    message ++;
    if (message % 4 == 0) {
      throw new RuntimeException("Oh no, I got four commands, I can't handle any more");
    } else {
      System.out.println("Got any more: " + message);
    }
  }
  
  public static  Props props() {
    return Props.create(NonTrustworthyChild.class);
  }

}
