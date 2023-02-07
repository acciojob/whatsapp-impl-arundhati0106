package com.driver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class Application {
	public static void main(String[] args) {
		//SpringApplication.run(Application.class, args);
		WhatsappController whatsappController = new WhatsappController();
		List<User> userList = new ArrayList<>();
		userList.add(new User("a", "1"));
		userList.add(new User("b", "2"));
		userList.add(new User("c", "5"));
		userList.add(new User("d", "3"));
		userList.add(new User("e", "4"));
		userList.add(new User("f", "6"));
		userList.add(new User("g", "7"));
		userList.add(new User("h", "8"));
		userList.add(new User("i", "9"));

		Group group = whatsappController.createGroup(userList);

		//whatsappController.sendMessage(new Message(, "check 1");


	}
}
