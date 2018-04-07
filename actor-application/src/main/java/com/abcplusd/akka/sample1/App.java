package com.abcplusd.akka.sample1;

import java.io.IOException;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;

public class App {
  public static void main(String[] args) throws IOException {
    final ActorSystem system = ActorSystem.create("Sample1");
      final ActorRef supervisor = system.actorOf(Supervisor.props(), "Supervisor");
      for(int i=0; i<10;i++) {
        supervisor.tell(new NonTrustworthyChild.Command(), ActorRef.noSender());
      }
      System.out.println("Press Enter to terminate");
      System.in.read();
      system.terminate();
  }

}
