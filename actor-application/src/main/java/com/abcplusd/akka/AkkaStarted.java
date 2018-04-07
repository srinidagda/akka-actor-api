package com.abcplusd.akka;

import akka.actor.AbstractLoggingActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.japi.pf.ReceiveBuilder;

public class AkkaStarted {
  static class Counter extends AbstractLoggingActor {
    static class Message {
    }
    private int count = 0;
    {
      receive(ReceiveBuilder
          .match(Message.class, this::onMessage)
          .build());
    }
    private void onMessage(Message message) {
      count++;
      System.out.println("Increased Counter: " + count);
    }
    public static Props props() {
      return Props.create(Counter.class);
    }
  }
  public static void main(String[] args) {
    ActorSystem system = ActorSystem.create("Sample1");
    try {
      final ActorRef counter = system.actorOf(Counter.props(), "Counter");
      for (int i=0; i<5; i++) {
        new Thread(new Runnable() {
          @Override
          public void run() {
            for(int j=0;j<5;j++) {
              counter.tell(new Counter.Message(), ActorRef.noSender());
            }
          }
        }).start();
      }
      System.out.println("Enter to Terminate");
      System.in.read();
    } catch (Exception e) {
      // TODO: handle exception
    } finally {
      system.terminate();
    }
    
  }

}