package com.example.hamzaos;

import javafx.application.Application;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;


public class Main extends Application {
    static List<Proccess> ready = new ArrayList<>();
    static List <Pages> pages = new ArrayList<>();
    static pMemory pMemory = new pMemory();
    static List <Proccess> list = new ArrayList<>();
    static List <Integer> frame = new ArrayList<>();
    static List <PageTracker> pageTrackers = new ArrayList<>();
    static List <LRU> lru = new ArrayList<>();
    static int t = 0;

    @Override
    public void start(Stage stage) {
        ComboBox <String> mode = new ComboBox<>();
        mode.getItems().addAll("FIFO","LRU");
        mode.setValue("FIFO");
        Button readFrom = new Button("start");
        Button showProcesses = new Button("Show Processes");
        Button showPageFaults = new Button("Show Page Faults");
        Button generate = new Button("generated.txt");
        Button exit = new Button("exit");
        Button back = new Button("back");
        Scene mainScene = new Scene(new VBox(mode,readFrom,generate,showProcesses,showPageFaults,exit));
        readFrom.setOnAction(e->{
            CPU.mode="FIFO".equals(mode.getValue());
            Label label = new Label("Enter Directory");
            TextField str = new TextField();
            Button startOS = new Button("Start");
            VBox box = new VBox(label,str,startOS);
            box.setAlignment(Pos.CENTER);
            startOS.setOnAction(r->{
                list = read(str.getText().trim());
                startOS();
                stage.setScene(mainScene);
            });
            stage.setScene(new Scene(box,200,100));
        });
        generate.setOnAction(e->{
            CPU.mode="FIFO".equals(mode.getValue());
            generate();
            list = read("generated.txt");
            startOS();
        });
        showProcesses.setOnAction(e-> {
            TableView<Proccess> processesTable = new TableView<>();
            TableColumn<Proccess,Integer> pid = new TableColumn<>("PID");
            pid.setCellValueFactory(new PropertyValueFactory<>("PID"));
            TableColumn<Proccess,Integer> start = new TableColumn<>("start");
            start.setCellValueFactory(new PropertyValueFactory<>("start"));
            TableColumn<Proccess,Integer> duration = new TableColumn<>("duration");
            duration.setCellValueFactory(new PropertyValueFactory<>("duration"));
            TableColumn<Proccess,Integer> elapsed = new TableColumn<>("elapsed");
            elapsed.setCellValueFactory(new PropertyValueFactory<>("elapsed"));
            TableColumn<Proccess,Integer> turnAround = new TableColumn<>("turnAround");
            turnAround.setCellValueFactory(new PropertyValueFactory<>("turnAround"));
            TableColumn<Proccess,Integer> wait = new TableColumn<>("wait");
            wait.setCellValueFactory(new PropertyValueFactory<>("wait"));
            processesTable.getColumns().addAll(pid,start,duration,elapsed,turnAround,wait);
            processesTable.getItems().addAll(list);
            stage.setScene(new Scene(new VBox(processesTable,back)));
        });
        showPageFaults.setOnAction(e-> {
            ComboBox <Integer> comboBox = new ComboBox<>();
            for (int i=0;i<pageTrackers.size();i++)
                comboBox.getItems().add(pageTrackers.get(i).getPID());
            comboBox.setValue(comboBox.getItems().get(0));
            HBox table = new HBox();
            table.setSpacing(10);
            comboBox.setOnAction(r->{
                table.getChildren().removeAll(table.getChildren());
                for (int i=0;i<pageTrackers.size();i++)
                    if (pageTrackers.get(i).getPID()==comboBox.getValue()) {
                        VBox column1 = new VBox();
                        VBox column2 = new VBox();
                        column1.getChildren().add(new Label("Page Number"));
                        column2.getChildren().add(new Label("Is On Memory"));
                        for (int j = 0; j < pageTrackers.get(i).getPages().getPageN().length; j++) {
                            column1.getChildren().add(new Label("" + j));
                            column2.getChildren().add(new Label("" + pageTrackers.get(i).getIsOnMemory()[j]));
                        }
                        table.getChildren().addAll(column1,column2);
                        break;
                    }
            });
            stage.setScene(new Scene(new VBox(comboBox,table,back),400,400));
        });
        exit.setOnAction(e-> {
            stage.close();
        });
        back.setOnAction(e->{
            stage.setScene(mainScene);
        });
        stage.setScene(mainScene);
        stage.setTitle("OS Project");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
    public static void startOS()  {
        Scheduler s = new Scheduler(list,2);
        MMU mmu = new MMU();
        CPU cpu = new CPU(mmu);
        for (int i=0;i<pages.size();i++){
            int isOnMemory[] = new int[pages.get(i).pageN.length];
            for (int j=0;j<pages.get(i).pageN.length;j++){
                isOnMemory[j]=0;
            }
            pageTrackers.add(new PageTracker(pages.get(i)));
            pageTrackers.get(i).setIsOnMemory(isOnMemory);
        }
        s.start();
        cpu.start();
    }
    public static List <Proccess> read(String str)  {
        List <Proccess> list = new ArrayList<>();
        String text,p[],trace,temp[];
        File file = new File(str);
        Scanner sc = null;
        try {
            sc = new Scanner(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        int index = 0;
        int n;
        int m;
        int s;
        int PID;
        int start;
        int duration;
        int size;
        while (sc.hasNextLine()){
            text = sc.nextLine();
            switch (index){
                case 0:
                    n = Integer.parseInt(text);
                    index++;
                    break;
                case 1:
                    m = Integer.parseInt(text);
                    pMemory x = new pMemory(m);
                    pMemory = x;
                    index++;
                    break;
                case 2:
                    s = Integer.parseInt(text);
                    index++;
                    break;
                default:
                    index++;
                    break;
            }
            if(index >= 4){
                p = text.split(" ",5);
                PID = Integer.parseInt(p[0]);
                start = Integer.parseInt(p[1]);
                duration = Integer.parseInt(p[2]);
                size = Integer.parseInt(p[3]);
                trace = p[4];
                temp = trace.split(" ");
                list.add(new Proccess(PID,start,duration,size,trace));
            }
        }
        int temp2[] = new int[pMemory.size];
        for(int i = 0; i< pMemory.size; i++){
            temp2[i] = 500;
        }
        pMemory.setFrames(temp2);
        for (Proccess proccess : list) {
            pages.add(new Pages(proccess));
        }
        sc.close();
        return list;
    }

    public static void generate(){
        File file = new File("generated.txt");
        PrintWriter out = null;
        try {
            out = new PrintWriter(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("Creating new file failed");;
            return ;
        }
        int numberOfProcesses = (int)((new Random().nextDouble())*100);
        int memorySize = 200;
        int minimumFrames = (int)((50-(new Random().nextDouble())*25));
        out.println(numberOfProcesses);
        out.println(memorySize);
        out.println(minimumFrames);
        for (int i=0;i<numberOfProcesses;i++) {
            int processSize = (int)((new Random().nextDouble())*100)+minimumFrames;
            int start = (int)((new Random().nextDouble())*30);
            int duration = 100-(int)((new Random().nextDouble())*20);
            int tracesNumber = (int)((new Random().nextDouble())* memorySize/5)+1;
            String line = i+" "+start+" "+duration+" " + processSize;
            for (int j=0;j<tracesNumber;j++) {
                line+= " "+Integer.toHexString((int)((new Random().nextDouble())*memorySize))+"0000";
            }
            out.println(line);
        }
        out.close();
    }

}