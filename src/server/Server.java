package server;

import commands.Command;
import commands.CommandExit;

import app.App;
import utils.DataBaseManager;
import utils.Serialization;
import utils.Task;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

import java.util.concurrent.*;
import java.util.logging.*;

public class Server {
    private DatagramChannel channel;
    private SocketAddress address;
    private byte[] buffer;
    private static final Serialization<String> responseSerialization_auto = new Serialization();
    private static String clientLogin;


    public Server() {
        buffer = new byte[65536];
    }

    /**
     * Method that accepts connections
     * @param port
     * @throws IOException
     */
    public void connect(int port) throws IOException {
        address = new InetSocketAddress(port);
        channel = DatagramChannel.open();
        channel.configureBlocking(false);
        channel.bind(address);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> System.out.println("Сервер закончил работу...")));
    }

    /**
     * Method that reads received information
     * and send the answer back
     * @param application
     */
    public void run(App application) {
        try {
            Callable<SocketAddress> task = getTask();
            ExecutorService service = Executors.newCachedThreadPool();
            while (true) {
                Future<SocketAddress> result = service.submit(task);
                SocketAddress socketAddress = result.get();
                byte[] copyData = new byte[buffer.length];
                System.arraycopy(buffer, 0, copyData, 0, buffer.length);
                new ForkJoinPool().invoke(new Task(application, copyData, socketAddress, channel));
            }
        } catch (ClassCastException e) {
            System.out.println("The server was expecting a command, but got something wrong...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //получение запроса
    private Callable<SocketAddress> getTask() {
        return () -> {
            ByteBuffer byteBuffer = ByteBuffer.wrap(buffer);
            SocketAddress socketAddress;
            do {
                socketAddress = channel.receive(byteBuffer);
            } while (socketAddress == null);
            return socketAddress;
        };
    }

    /**
     * Модуль отправки ответов клиенту
     *
     * @param work - режим работы
     * @throws IOException - ошибка чтения запроса
     */
    /*
    public static void write(boolean work, byte[] buffer, DatagramChannel channel, SocketAddress address) throws IOException {

        ByteBuffer byteBuffer = ByteBuffer.wrap(buffer);
        do {
            address = channel.receive(byteBuffer);
        } while (address == null);


        String messageToClient = "";
        if (work) messageToClient = "\nВы успешно авторизировались";
        else messageToClient = "\nВо время авторизации произошла ошибка, повторите попытку";
        System.out.println(messageToClient);
        //byte[] msg = responseSerialization_auto.writeObject(messageToClient);


        //Response response = new Response(messageToClient);
        byte[] answer = responseSerialization_auto.writeObject(messageToClient);
        byteBuffer = ByteBuffer.wrap(answer);
        channel.send(byteBuffer, address);
    }

     */

    /**
     * Модуль чтения запроса
     *
     * @return - возвращает десериализованную команду
     * @throws IOException - ошибка чтения запроса
     */
    public static String read(App application, byte[] buffer,SocketAddress address,DatagramChannel channel) throws IOException, ClassNotFoundException {

        ByteBuffer byteBuffer = ByteBuffer.wrap(buffer);
        do {
            address = channel.receive(byteBuffer);
        } while (address == null);
        String msg = responseSerialization_auto.readObject(buffer);
        System.out.println(msg);
        String[] fields = msg.split(" ");
        boolean work = false;
        if (fields.length == 3){
            clientLogin = fields[0];
            if (fields[2].equals("2")) work = application.getDataBaseManager().addUser(fields[0], fields[1]);
            else if (fields[2].equals("1")) work = DataBaseManager.login(fields[0], fields[1]);
        }
        String messageToClient = "";
        if (work) messageToClient = "\nВы успешно авторизировались";
        else messageToClient = "\nВо время авторизации произошла ошибка, повторите попытку";
        System.out.println(messageToClient);

        byte[] answer = responseSerialization_auto.writeObject(messageToClient);
        byteBuffer = ByteBuffer.wrap(answer);
        channel.send(byteBuffer, address);

        return msg;
    }

    /**
     * Модуль авторизации
     */
    /*
    public void authorisation(App application) {
        while (true) {
            try {
                String[] fields = read(buffer, address, channel).split(" ");
                boolean work = false;
                if (fields.length == 3){
                    clientLogin = fields[0];
                    if (fields[2].equals("2")) work = application.getDataBaseManager().addUser(fields[0], fields[1]);
                    else if (fields[2].equals("1")) work = DataBaseManager.login(fields[0], fields[1]);
                }
                byte[] copyData = new byte[buffer.length];
                write(work, copyData, channel, address);
                if (work) break;
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Ошибка авторизации");
                e.printStackTrace();
            }
        }
    }

     */
    public void authorisation(App application)  {
        try{
            read(application, buffer, address, channel);
    } catch (IOException | ClassNotFoundException e) {
        System.out.println("Ошибка авторизации");
        e.printStackTrace();
    }
    }

}
