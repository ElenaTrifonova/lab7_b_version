/*package commands;

import app.App;
import app.Organization;
import client.User;
import server.Session;
import utils.SimplePasswordGenerator;
import utils.Validation;

import java.io.Console;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

public class CommandRegister extends Command{
    private User userForRegistration;

    //������������, ���� ��� ��������
    @Override
    public String execute(App application, Session session) {
        if (!application.getDataBaseManager().containsUserName(userForRegistration.getName())) {
            application.getDataBaseManager().addUser(userForRegistration);
            Session sessionActual = new Session(userForRegistration.getName(), userForRegistration.getPass());
            application.addSession(userForRegistration, sessionActual);
            user = userForRegistration;
            return "������������ � ����� " + userForRegistration.getName() + " ������� ���������������\n" +
                    "������ ��� �������� ��� �������, ��� �� ��������� ������� help";
        }
        user = null;
        return "����������� ��������, ������������ � ����� " + userForRegistration.getName() + " ��� ���������������";
    }

    @Override
    public ConcurrentHashMap<Long, Organization> getCollection() {
        return null;
    }

    @Override
    public CopyOnWriteArraySet<Long> getIdList() {
        return null;
    }

    @Override
    public String getCommandInfo() {
        return "register name : �������������� ���������� �� ������� ��� ����� name";
    }

    @Override
    public User getUser() {
        return userForRegistration;
    }

    @Override
    public String toString() {
        return "register";
    }

    @Override
    public boolean withArgument() {
        return true;
    }

    //������ ������ ��� ���������� ��� � ���� ��������������
    //���������
    @Override
    public void act(String argument) throws IOException {
        if (!Validation.checkChars(argument, true, true))
            throw new IndexOutOfBoundsException("��� ������������ ����� �������� ������ �� ���� �������� � ����������� ��������!!!");
        String pass;
        //Console console = System.console();
        Scanner scanner = new Scanner(System.in);
        do {
            System.out.println("������, ����� ������ �������������� �������������?(y/n)");
            String answer = scanner.nextLine();
            if (answer.equals("y") || answer.equals("yes") || answer.equals("�")) {
                SimplePasswordGenerator generator = new SimplePasswordGenerator(true, true, true, true);
                pass = generator.generate(6, 19);
                File file = new File(argument);
                if (file.createNewFile()) {
                    FileWriter fileWriter = new FileWriter(file);
                    fileWriter.write(pass + '\n');
                    fileWriter.flush();
                    System.out.println("��� ��������������� ������ ��������� � ����� " + argument);
                } else System.out.println("��� ��������������� ������: " + pass);
                break;
            }
            System.out.println("������� ������ ��� �����������(�� 6 �� 20 ��������):");
            pass = scanner.nextLine();
        } while (!Validation.checkInterval((long) pass.length(), 6, false, "������ �� ������������� ��������. ���������� �����"));
        userForRegistration = new User(argument, pass);
    }
}

 */
