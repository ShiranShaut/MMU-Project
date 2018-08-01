package com.hit.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.InputMismatchException;
import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import com.hit.services.CacheUnitController;
import com.hit.util.CLI;

public class Server implements Observer {
	
	private final String LRU = "lru";
	private final String MRU = "mru";
	private final String SECONDCHANCE = "secondchance";
	private final String RANDOM = "random";
	public static final int SIZE = 10;
	private int port;
	private ServerSocket serverSocket;
	private static boolean serverOn = false;
	public static Integer numberOfRequests = 0;
	private String algoName = null;
	private Integer capacity = 0;
	
	public Server() {
		port = 12345;
		
		try {
			serverSocket = new ServerSocket(port);
			serverOn = true;
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}

	void start() {
		try {
			Executor executor = Executors.newFixedThreadPool(SIZE);
			
			while(serverOn) {
				Socket socket = serverSocket.accept();
				numberOfRequests++;				
				HandleRequest<String> handler = new HandleRequest<String>(socket, new CacheUnitController<String>(algoName, capacity));
				executor.execute(new Thread(handler));
			}
		} catch (IOException e) {
			if(!serverOn)
				System.out.println("Server is Close");
			else
				System.out.println("Server Error");
		} finally {
			try {
				if(serverSocket != null && !serverSocket.isClosed()) {
					serverSocket.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public void update(Observable o, Object arg) {
		
		String command = arg.toString();
		
		switch(command) {
		case CLI.START: {
			
			getAlgoNameFromUser();
			getCapacityFromUser();
			start();
			break;
		}
		case CLI.STOP: {
			serverOn = false;
			break;
		}
		}
	}

	private void getCapacityFromUser() {
		Scanner scanner = new Scanner(System.in);
		boolean valid = false;
		Integer input = null;

		while (!valid) {
			System.out.println("Please enter your desired capacity: ");

			try {
				input = scanner.nextInt();
				valid = true;
			} catch (InputMismatchException e) {
				System.out.println("Invalid input, Try again please.");
			}
		}

		capacity = input;
	}

	private void getAlgoNameFromUser() {
		Scanner scanner = new Scanner(System.in);
		String input = null;
		
		System.out.println("Please enter your desired algorithm: ");
		input = scanner.nextLine().toLowerCase();
		
		while(!(input.equals(LRU)) && !(input.equals(MRU)) && !(input.equals(RANDOM)) && !(input.equals(SECONDCHANCE))) {
			System.out.println("Invalid input, Please enter your desired algorithm: ");
			input = scanner.nextLine();
		}
		
		algoName = input;
	}
}