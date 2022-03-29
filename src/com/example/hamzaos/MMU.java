package com.example.hamzaos;

public class MMU{
    static int fault;
    static int pageF[] = new int[9999];
    public int map(Proccess p) {
                int n[],r=0,index=-1;
                for (int i=0;i<Main.pages.size();i++) {
                    if(Main.pages.get(i).getP().PID == p.PID){
                        index = i;
                    }
                }
                n = Main.pages.get(index).getPageN();
                for (int k=0;k<pageF.length;k++){
                    pageF[k]=0;
                }
                for(int j =0;j<n.length;j++) {
                    if (n[j] != -1){
                        if (n[j] < pMemory.size && pMemory.frames[n[j]] == 500) {
                            pMemory.frames[n[j]] = 0;
                            Main.lru.add(new LRU(n[j], 1));
                            Main.pageTrackers.get(index).isOnMemory[j] = 1;
                            Main.pages.get(index).pageN[j] = -1;
                            Main.frame.add(n[j]);
                        } else {
                            pageF[j] = n[j];
                            Main.t += 300;
                            fault++;
                            r++;
                        }
                }
                }
                if(r >0){
                    for (int j =0;j<n.length;j++){
                        if(n[j] != -1) {
                            pMemory.frames[n[j]] = 500;
                        }
                        Main.pageTrackers.get(index).isOnMemory[j]=0;
                    }
                }
                for (int i=0;i<Main.pages.get(index).pageN.length;i++){
                    System.out.println(Main.pageTrackers.get(index).isOnMemory[i]);
                }

        return r;
    }
}
