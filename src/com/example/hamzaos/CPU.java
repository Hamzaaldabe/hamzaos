package com.example.hamzaos;

import java.util.ArrayList;
import java.util.List;

public class CPU extends Thread{
    MMU mmu;
    static List <Proccess> dequeue = new ArrayList<>();
    static List <Proccess> queue = new ArrayList<>();
    static boolean mode = false;
    public CPU(MMU mmu) {
        this.mmu = mmu;
    }
    public void run(){
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        running(mmu);
        for (int i=0;i<Main.list.size();i++){
            System.out.println();
            System.out.println("Process: " +Main.list.get(i).getPID());
            System.out.println("elapsed " + Main.list.get(i).elapsed);
            System.out.println("turnaround " + Main.list.get(i).turnAround);
            System.out.println("wait " + Main.list.get(i).wait);
        }
    }
    public static void running(MMU mmu){
        int result,index=-1;
        while (Main.ready.size() >0) {
            for (int i = 0; i < Main.list.size(); i++) {
                if (Main.ready.contains(Main.list.get(i))) {
                    for (int z=0;z<Main.ready.size();z++){
                        Main.t++;
                        if(Main.ready.get(z).PID== Main.list.get(i).PID){
                            index =z;
                        }
                    }
                    queue.add(Main.ready.get(index));
                    if(Main.ready.get(index).elapsed ==0){
                        Main.ready.get(index).elapsed += Main.t;
                    }else Main.ready.get(index).elapsed += Main.t;
                    Main.ready.remove(Main.ready.get(index));
                    Main.list.get(index).wait += Main.t;
                    for (int j = 0; j < queue.size(); j++) {
                        result = mmu.map(queue.get(j));
                        if (result > 0) {
                            dequeue.add(queue.get(j));
                            queue.remove(queue.get(j));
                            if (mode)
                                faultFIFO();
                            else
                                faultLRU();
                            j--;
                            System.out.println("Page fault");
                        }else {
                            Main.ready.remove(queue.get(j));
                            Main.list.get(index).wait += Main.t;
                            queue.remove(queue.get(j));
                            j--;
                        }

                    }
                }
            }
        }
    }
    public static void faultFIFO(){
        int index=-1;
        for (int i=0;i<dequeue.size();i++){
            for (int k=0;k<Main.pages.size();k++) {
                if(Main.pages.get(k).getP().PID == dequeue.get(i).PID){
                    index = k;
                }
            }
            for (int j=0;j<MMU.pageF.length;j++){
                if(MMU.pageF[j]!=0){
                    for(int x =0 ;x<Main.frame.size();x++) {
                        if (MMU.pageF[j] != Main.frame.get(x) && MMU.pageF[j] !=0 ) {
                            if(Main.frame.get(x)!=-1) {
                                pMemory.frames[Main.frame.get(x)] = 500;
                                Main.pages.get(index).pageN[j] = Main.frame.get(x);
                                Main.frame.remove(Main.frame.get(x));
                                MMU.pageF[j] = 0;
                            }
                        }
                    }
                }
            }
            Main.ready.add(dequeue.get(i));
            dequeue.remove(dequeue.get(i));
        }
    }

    public static void faultLRU(){
        int index=-1;
        int min=Main.lru.get(0).frame,temp=0;
        for (int y=0;y<Main.lru.size();y++){
            if(Main.lru.get(y).times < Main.lru.get(0).times){
                min = Main.lru.get(y).frame;
                temp=y;
            }
        }
        Main.lru.remove(Main.lru.get(temp));
        for (int i=0;i<dequeue.size();i++){
            for (int k=0;k<Main.pages.size();k++) {
                if(Main.pages.get(k).getP().PID == dequeue.get(i).PID){
                    index = k;
                }
            }
            for (int j=0;j<MMU.pageF.length;j++){
                if(MMU.pageF[j]!=0 || MMU.pageF[j]!=-1) {
                    for (int x = 0; x < Main.frame.size(); x++) {
                        if (MMU.pageF[j] != min && MMU.pageF[j] != 0) {
                            pMemory.frames[min] = 500;
                            Main.pages.get(index).pageN[j] = min;
                            MMU.pageF[j] = 0;
                        }

                    }

                    for (int y = 0; y < Main.lru.size(); y++) {
                        if (Main.lru.get(y).times < Main.lru.get(0).times) {
                            min = Main.lru.get(y).frame;
                        }
                    }
                }
            }
            Main.ready.add(dequeue.get(i));

            dequeue.remove(dequeue.get(i));
        }
    }
}
