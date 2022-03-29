package com.example.hamzaos;

public class Pages {
    Proccess p;
    int pageN[];
    public Pages(Proccess p) {
        this.p = p;
        pages(this);
    }
    public static void pages(Pages m){
        String[] temp = m.getP().getTrace().split(" ");
        int n[] = new int[temp.length];
        for(int i=0; i<temp.length;i++){
            n[i]=(Integer.parseInt(temp[i].substring(0, temp[i].length()-4),16));
        }
        m.setPageN(n);
    }

    public Proccess getP() {
        return p;
    }

    public int[] getPageN() {
        return pageN;
    }

    public void setP(Proccess p) {
        this.p = p;
    }

    public void setPageN(int[] pageN) {
        this.pageN = pageN;
    }

}
