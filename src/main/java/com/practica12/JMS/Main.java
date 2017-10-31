package com.practica12.JMS;

import com.practica12.JMS.jms.Productor;
import org.apache.activemq.broker.BrokerService;

import javax.jms.JMSException;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class Main {
    static String cola = "pruebajms.cola";
    static Productor productor = new Productor();

    public static void main(String[] args) throws IOException, JMSException {
        System.out.println("Prueba de Mensajeria Asincrona");

        if(args.length == 0){
            mensajesParametros();
            return;
        }

        /**
         * GENERA LOS VALORES ALEATORIOS
         */
            boolean enviarMensajes =true;
        if(Integer.parseInt(args[0]) == 2){
            if(args.length > 1){
                String parametro = args[1];

                Timer t = new Timer();
                t.scheduleAtFixedRate(new TimerTask() {

                    @Override
                    public void run() {
                        try {
                            productor.enviarMensaje(cola,parametro);
                        } catch (Exception ex) {
                            Thread t = Thread.currentThread();
                            t.getUncaughtExceptionHandler().uncaughtException(t, ex);
                        }
                    }

                }, 0, 1000);


            }else{
                System.out.println("Debe enviar el ID del dispositivo!");
                return;
            }
        }
        /**
         * INICIALIZACION DEL SERVIDOR
         */
        else if(Integer.parseInt(args[0]) == 1){
            System.out.println("Inicializando Servidor JMS");
            try {
                //Subiendo la versión embedded de ActiveMQ.
                //http://activemq.apache.org/how-do-i-embed-a-broker-inside-a-connection.html
                BrokerService broker = new BrokerService();
                //configurando el broker.
                broker.addConnector("tcp://localhost:61616");
                //Inicializando
                broker.start();
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
        else{
            mensajesParametros();
        }

    }
    private static void mensajesParametros(){
        System.out.println("Deben enviar los parametros: aplicacion [mensaje]");
        System.out.println("Si aplicacion == 1, Inicializa el modo Embedded");
        System.out.println("Si aplicacion == 2, debe enviar segundo parametro para el ID del dispositivo");
    }


}
