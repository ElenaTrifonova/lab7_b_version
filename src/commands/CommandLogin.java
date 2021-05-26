/*package commands;

import app.App;
import app.Organization;
import client.User;
import server.Session;
import utils.Validation;

import java.io.Console;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

public class CommandLogin extends Command{
    private User userForLogin;

    //�����������, ���� ����� ������������ ���������������
    //���������
    @Override
    public String execute(App application, Session session) {
        if (application.getDataBaseManager().containsUser(userForLogin)) {
            Session sessionActual = new Session(userForLogin.getName(), userForLogin.getPass());
            if(!application.addSession(userForLogin, sessionActual)) return "������������ � ����� " + userForLogin.getName() + " ��� �����������.\n����������� � ���� ��������� ���������!";
            user = userForLogin;
            return "������������ � ����� " + userForLogin.getName() + " ������� �����������\n" +
                    "������ ��� �������� ��� �������, ��� �� ��������� ������� help";
        }
        user = null;
        return "������ ��� ����� �����������, ����������� ��������";
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
        return "login name : �������������� �� ����� name";
    }

    @Override
    public String toString() {
        return "login";
    }

    @Override
    public boolean withArgument() {
        return true;
    }

    //������ ������
    @Override
    public void act(String argument) {
        String pass;
        //Console console = System.console();
        Scanner scanner = new Scanner(System.in);
        do {
            System.out.println("������� ������ ��� �����������(�� 6 �� 20 ��������):");
            pass = scanner.nextLine();
        } while (!Validation.checkInterval((long) pass.length(), 6, false, "������ �� ������������� ��������. ���������� �����"));
        userForLogin = new User(argument, pass);
    }

    @Override
    public User getUser() {
        return userForLogin;
    }
}

 */
