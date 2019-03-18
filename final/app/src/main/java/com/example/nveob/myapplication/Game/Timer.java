package com.example.nveob.myapplication.game;


public class Timer extends Thread {

    private int milesimas;
    private boolean stopped; // estado del contador
    private int segundos;

    // clase interna que representa una tarea, se puede crear varias tareas y asignarle al timer luego

    public void run() {
        milesimas++;
        if (stopped != true) {
            update();
        }
    }

    public void update() {
        if (this.milesimas == 100) {
            segundos++;
            this.milesimas = 0;
        }

    }

    public synchronized void start() {
        stopped = false;
        segundos = 0;
        milesimas = 0;
    }


    public void reset() {
        segundos = 0;
        milesimas = 0;

    }// end Reset

    public int getSegundos() {
        return this.segundos;
    }
}
