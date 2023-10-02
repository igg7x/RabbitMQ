package com.example.RabbitMQ.lectura;

import java.net.InetAddress;
import java.util.Timer;
import java.util.TimerTask;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.example.RabbitMQ.publisher.LectureModbusPublisher;

import net.wimpi.modbus.io.ModbusTCPTransaction;
import net.wimpi.modbus.io.ModbusTransaction;
import net.wimpi.modbus.msg.ReadMultipleRegistersRequest;
import net.wimpi.modbus.msg.ReadMultipleRegistersResponse;
import net.wimpi.modbus.net.TCPMasterConnection;

@Component
public class Lecture implements InitializingBean {

    InetAddress adress = null;
    TCPMasterConnection conn = null; // the connection
    Integer PORT;
    Boolean state = false;
    Timer timerStart;
    Timer timerReset;

    @Value("${slave.ip:127.0.0.1}")
    private String ipSlave;

    @Value("${slave.puerto:502}")
    private int puerto;

    @Value("${slave.id:1}")
    private int idSlave;

    @Value("${slave.registroRampa:1}")
    private int registroRampa;

    @Value("${slave.registroBobina:1}")
    private int registroBobina;

    @Value("${slave.segsRefresco:1}")
    private Integer readTime = 1;

    @Value("${cantLectRampa:1}")
    private int cantLectRampa = 1;

    @Autowired
    LectureModbusPublisher lectureModbusPublisher;

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("************************************************");
        System.out.println("*              INGENIERIA SCH SRL              *");
        System.out.println("************************************************");
        System.out.println(" ");
        System.out.println("Sistema de Lectura Por Modbus:");
        System.out.println(" ");

        System.out.println("Ip del Esclavo: " + ipSlave);
        System.out.println("Puerto del Esclavo: " + puerto);
        System.out.println("Id del Esclavo: " + idSlave);

        System.out.println("Registro analogico de Lectura: " + registroRampa);
        System.out.println("Registro digital de Lectura: " + registroBobina);
        System.out.println("--------------------------");

        try {
            // 1. Set up the parameters
            adress = InetAddress.getByName(ipSlave);
            PORT = puerto;
            conn = new TCPMasterConnection(adress);
            conn.setPort(PORT);
            conn.connect();
            System.out.println("Conectado al esclavo: " + ipSlave + " en el puerto: " + puerto);
            state = true;

        } catch (Exception e) {
            System.out.println("Error al conectar con el esclavo: " + ipSlave + " en el puerto: " + puerto);
            e.printStackTrace();
            state = false;

        }
        if (state) {
            timerStart = new Timer();
            timerStart.schedule(new Temporizado(), 5000, 2000);
        }

    }

    private void initConexion() {

        try {

            adress = InetAddress.getByName(ipSlave);
            PORT = puerto;
            conn = new TCPMasterConnection(adress);
            conn.setPort(PORT);
            conn.connect();
            System.out.println("Conectado al esclavo: " + ipSlave + " en el puerto: " + puerto);
            state = true;

        } catch (Exception e) {
            state = false;
            e.printStackTrace();
        }

        if (state) {
            timerStart = new Timer();
            timerStart.schedule(new Temporizado(), 5000, 2000);
        }

    }

    // private void resetConexion() {

    // if (timerReset != null) {
    // timerReset.cancel();
    // }

    // if (timerStart != null) {
    // timerStart.cancel();
    // }

    // try {
    // conn.close();
    // } catch (Exception e) {
    // System.out.println(e.getMessage());
    // }
    // }

    class Temporizado extends TimerTask {

        Integer count = 0;

        @Override
        public void run() {
            count++;
            if (count >= readTime) {

                count = 0;
                try {
                    ModbusTransaction transaction;
                    ReadMultipleRegistersRequest req;
                    ReadMultipleRegistersResponse res;
                    req = new ReadMultipleRegistersRequest(registroRampa, cantLectRampa);
                    req.setUnitID(idSlave);

                    transaction = new ModbusTCPTransaction(conn);
                    transaction.setRequest(req);
                    transaction.execute();

                    res = (ReadMultipleRegistersResponse) transaction.getResponse();
                    lectureModbusPublisher.sendMessage("Lectura Analogica: " +
                            res.getRegisterValue(0));

                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("Desconexion a Modbus");
                    state = false;
                }
            }

        }

    }

    class TemporizadoReset extends TimerTask {

        @Override
        public void run() {
            initConexion();
        }

    }
}
