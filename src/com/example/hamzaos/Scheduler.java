package com.example.hamzaos;

import java.util.ArrayList;
import java.util.List;

public class Scheduler extends Thread{
    List <Proccess> p;
    int quantum ;
    public Scheduler(List<Proccess> p, int quantum) {
        this.p = p;
        this.quantum = quantum;
    }
    public void run(){
        while (true) {
            boolean done=true;
            Main.ready = RR(p, quantum, Main.t);
            for(int i=0;i<p.size();i++){
                if(p.get(i).getRem()!=0){
                    done = false;
                }
            }
            if(done==true){
                break;
            }
        }
    }
    public static List <Proccess> RR(List <Proccess> p,int quantum,int t){
        List <Proccess> ready = new ArrayList<>();
        for(int i=0;i<p.size();i++){
            p.get(i).setRem(p.get(i).getDuration());
        }
        while (true) {
            boolean done = true;
                for (int i = 0; i < p.size(); i++) {
                    if (p.get(i).getStart() <= Main.t) {
                        if (p.get(i).getRem() > 0) {
                            done = false;
                            if (p.get(i).getRem() >= quantum) {
                                if(!ready.contains(p.get(i))) {
                                    ready.add(p.get(i));
                                    p.get(i).wait+=Main.t;
                                    System.out.println("Process: " +p.get(i).getPID());
                                    System.out.println("added to ready queue");
                                    System.out.println();
                                }
                                Main.t = Main.t + quantum;
                                p.get(i).setRem(p.get(i).rem - quantum);
                            } else {
                                Main.t += p.get(i).getRem();
                                p.get(i).setRem(0);
                            }
                        }
                    }
                        p.get(i).turnAround += Main.t;
                }
            if(done == true){
                Main.t++;
                break;
            }
        }
        return ready;
    }
    }

