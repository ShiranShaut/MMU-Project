package com.hit.util;

import java.lang.Runnable;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Scanner;
import java.io.*;
import java.lang.String;

public class CLI extends Observable implements Runnable {

	public static final String START = "start";
	public static final String STOP = "stop";
	public static final String NOT_VALID = "error";
	
	private InputStream input;
	private OutputStream output;
	private OutputStreamWriter outputWriter;
	private Map<String,String> commands = new HashMap<String,String>();
	
	public CLI(InputStream in, OutputStream out) {
		input = in;
		output = out;
		outputWriter = null;
		setCommands();
	}
	
	private void setCommands() {
		commands.put(START, "Starting Server");
		commands.put(STOP, "Shutdown server");
		commands.put(NOT_VALID, "Not a valid command, Please enter your command:");
	}

	public void write(String string) {
		try {
			outputWriter.write(string + System.lineSeparator());
			outputWriter.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	@Override
	public void run() {
		boolean runProgram = true;
		Scanner read = new Scanner(input);
		outputWriter = new OutputStreamWriter(output);
		String command = null;
		
		while (runProgram) {
			command = handleInput(read); // Force the user to enter a valid command ("stop" included)
			
			if(!command.equals(STOP)) {
				setChanged();
				notifyObservers(command);
			}
			else {
				runProgram = false;

				try {
					if (outputWriter != null) {
						outputWriter.close();
					}
					if (read != null) {
						read.close();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		setChanged();
		notifyObservers(command);
	}
	
	private String handleInput(Scanner read) {
		
		String inputBuffer = new String();
			
		write("Please enter your command:");
		inputBuffer = read.nextLine();
		
		while(!inputBuffer.equals(START) && !inputBuffer.equals(STOP)) {
			
			write(commands.get(NOT_VALID));
			inputBuffer = read.nextLine();
		}
		
		write(commands.get(inputBuffer));
		
		return inputBuffer;
	}
}