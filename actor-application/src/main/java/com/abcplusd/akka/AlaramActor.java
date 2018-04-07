package com.abcplusd.akka;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.japi.pf.ReceiveBuilder;
import scala.PartialFunction;
import scala.runtime.BoxedUnit;

public class AlaramActor {
  static class Alaram extends AbstractActor {
    //Protocol Messages
    static class Activity {
      
    }
    static class Disabled {
      private final String password;
      public Disabled(String password) {
        this.password = password;
      }
    }
    static class Enabled {
      private String password;
      public Enabled(String password) {
        this.password = password;
      }
    }
    private final String password;
    private final PartialFunction<Object, BoxedUnit> enabled;
    private final PartialFunction<Object, BoxedUnit> disabled;
    public Alaram(String password) {
      
      this.password = password;
      
      enabled = ReceiveBuilder
          .match(Activity.class, this::onActivity)
          .match(Disabled.class, this::onDisabled)
          .build();
      disabled = ReceiveBuilder
          .match(Enabled.class, this::onEnable)
          .build();
      receive(disabled);
    }
    
    private void onEnable(Enabled enable) {
      if (password.equals(enable.password)) {
        System.out.println("Alaram Enabled");
        getContext().become(enabled);
      } else {
        System.out.println("Someone failed to enable alaram");
      }
    }
    
    private void onActivity(Activity ingnored) {
      System.out.println("OEOEOEOEOEOE Alaram Alaram !!!!!" );
    }
    
    private void onDisabled(Disabled disable) {
      if (password.equals(disable.password)) {
        System.out.println("Alaram Disabled");
        getContext().become(disabled);
      } else {
        System.out.println("Someone who didn't know password tried to disabled");
      }
    }
    
    public static Props props(String password) {
      return Props.create(Alaram.class, password);
    }
  }
  public static void main(String args[]) {
    final ActorSystem system = ActorSystem.create();
    try {
      final ActorRef alaram = system.actorOf(Alaram.props("cat"), "alaram");
      alaram.tell(new Alaram.Activity(), ActorRef.noSender());
      alaram.tell(new Alaram.Enabled("dogs"), ActorRef.noSender());
      alaram.tell(new Alaram.Enabled("cat"), ActorRef.noSender());
      alaram.tell(new Alaram.Activity(), ActorRef.noSender());
      alaram.tell(new Alaram.Disabled("dogs"), ActorRef.noSender());
      alaram.tell(new Alaram.Disabled("cat"), ActorRef.noSender());
      System.out.println("Press Enter to Terminate");
      System.in.read();
    } catch (Exception e) {
      // TODO: handle exception
    } finally {
      system.terminate();
    }
  }
}
