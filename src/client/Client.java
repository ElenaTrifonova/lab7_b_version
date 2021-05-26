package client;

import commands.Command;
import exceptions.StackIsLimitedException;
import app.Handler;
import utils.Serialization;
import server.Response;

import java.io.*;
import java.net.*;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Client {
    private SocketAddress socketAddress;
    private DatagramSocket socket;
    private Handler handler;
    private User user;
    private Serialization<Command> commandSerialization = new Serialization<>();
    private Serialization<Response> responseSerialization = new Serialization<>();
    private int scriptCount = 0;
    private static final int STACK_SIZE = 10;
    private static BufferedReader reader;
    //private static BufferedReader in; // поток чтения из сокета
    //private static BufferedWriter out; // поток записи в сокет
    private static final Serialization<String> responseSerialization_auto = new Serialization();
    private static final Serialization<Response> responseSerialization_auto_r = new Serialization();
    private static final String serializedDate = "serializedDate.txt";



    public Handler getHandler() {
        return handler;
    }

    /**
     * Method that sets handler of commands
     * @param handler
     */
    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    /**
     * Method that creates connection between
     * server and client
     * @param port
     * @throws IOException
     */
    public void connect(int port) throws IOException {
        try {
            //InetAddress address = InetAddress.getByName(host);
            InetAddress address = InetAddress.getLocalHost();
            if (address == null) throw new NullPointerException();
            System.out.println("Connect to " + address);
            socketAddress = new InetSocketAddress(address, port);
            socket = new DatagramSocket();
            socket.connect(socketAddress);
        } catch (NullPointerException e) {
            System.out.println("The entered address does not exist!");
        }
    }

    /**
     * Method that sends command to the server
     * and receives answer
     * @param command
     */
    public void sendCommandAndReceiveAnswer(Command command) {
        try {
            byte[] commandInBytes = commandSerialization.writeObject(command);
            DatagramPacket packet = new DatagramPacket(commandInBytes, commandInBytes.length, socketAddress);
            socket.send(packet);
            System.out.println("Request sent to the server...");
            byte[] answerInBytes = new byte[65536];
            packet = new DatagramPacket(answerInBytes, answerInBytes.length);
            socket.receive(packet);
            Response response = responseSerialization.readObject(answerInBytes);
            String result = response.getAnswer();

            if (user == null) {
                if (response.getUser() != null) handler.setDefaultPack();
                user = response.getUser();
            }
            System.out.println("Received a response from the server: ");
            System.out.println(result);
        } catch (IOException e) {
            System.out.println("The server is currently unavailable...");
        } catch (ClassNotFoundException e) {
            System.out.println("The client was waiting for a response in the form of a class Response, but received something incomprehensible...");
        }
    }

    public int getScriptCount() {
        return scriptCount;
    }

    /**
     * Method that increases the number of scripts in file
     */
    public void incrementScriptCounter() {
        if (scriptCount >= STACK_SIZE) throw new StackIsLimitedException();
        scriptCount++;
    }

    /**
     * Method that decreases the number of scripts in file
     */
    public void decrementScriptCounter() {
        scriptCount--;
    }

    public static String sendAndReceiveAnswer(String msg, SocketAddress socketAddress, DatagramSocket socket) throws IOException, ClassNotFoundException {

            byte[] msgInBytes = responseSerialization_auto.writeObject(msg);
            DatagramPacket packet = new DatagramPacket(msgInBytes, msgInBytes.length, socketAddress);
            socket.send(packet);
            System.out.println("Request sent to the server...");
            byte[] answerInBytes = new byte[65536];
            packet = new DatagramPacket(answerInBytes, answerInBytes.length);
            socket.receive(packet);
            String result = responseSerialization_auto.readObject(answerInBytes);
            //String result = response.getAnswer();

            System.out.println("Received a response from the server: ");
            //System.out.println(result);
            return result;

    }

    /*
    /**
     * Модуль отправки запросов на сервер
     *
     * @param message - сообщение серверу
     * @return - введённая команда
     * @throws IOException - ошибка чтения
     */
    /*
    public static String write(String message) throws IOException {
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
        serialization.SerializeObject((Object) message, serializedDate);
        out.write(message + "\n");
        out.flush();
        return message;
    }

    /**
     * Модуль принятия сообщений от сервера
     *
     * @return - сообщение от сервера
     * @throws IOException - ошибка принятия сообщений
     */
    /*
    public static String read() throws IOException {
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
        String serverWord = in.readLine();
        serverWord = serialization.DeserializeObject(serializedDate);
        return serverWord;
    }

     */
    public  void authorisation() {
        //System.out.println("Для подключения к базе данных необходимо авторизоваться");

        while (true){
            try {
                Scanner scanner = new Scanner(System.in);

                LocalDateTime date = null;
                System.out.println(date.now());

                System.out.println("1 - Вход || 2 - Регистрация");
                System.out.print("Введите команду: "+ "\n");
                String message = scanner.nextLine();
                String password;
                String login;
                if (message.equals("1") || message.equals("Вход")){
                    System.out.print("Введите логин: "+ "\n");
                    login = scanner.nextLine();
                    login = login.toLowerCase();
                    System.out.print("Введите пароль: "+ "\n");
                    password = scanner.nextLine();
                    String messageFromServer = sendAndReceiveAnswer(login + " " + password + " 1", socketAddress, socket);
                    System.out.println(messageFromServer + "\n");
                    if (messageFromServer.equals("\nВы успешно авторизировались")) break;
                    /*
                    write(login + " " + password + " 1");
                    String messageFromServer = read();
                    System.out.println(messageFromServer + "\n");
                    if (messageFromServer.equals("\nВы успешно авторизировались")) break;

                     */
                } else if (message.equals("2") || message.equals("Регистрация")){
                    System.out.print("Введите логин: "+ "\n");
                    login = scanner.nextLine();
                    login = login.toLowerCase();
                    System.out.print("Введите пароль: "+ "\n");
                    password = scanner.nextLine();

                    String messageFromServer = sendAndReceiveAnswer(login + " " + password + " 2", socketAddress, socket);
                    System.out.println(messageFromServer + "\n");
                    if (messageFromServer.equals("\nВы успешно авторизировались")) break;

                    /*
                    write(login + " " + password + " 2");
                    String messageFromServer = read();
                    System.out.println(messageFromServer + "\n");
                    if (messageFromServer.equals("\nВы успешно авторизировались")) break;

                     */
                } else {
                    System.err.print("Неизвестная команда\n\n");
                    TimeUnit.MILLISECONDS.sleep(100);
                }
            } catch (IOException | InterruptedException | ClassNotFoundException e) {
                System.err.print("Ошибка авторизации\n");
            }
        }
    }
}
