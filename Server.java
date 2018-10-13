
import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Server {
	public static void main(String argv[]) throws Exception {
		String clientSentence; // client's variable
		String request; // answer to the request
		Scanner input = new Scanner(System.in);
		File directory = new File("C:/Users/lg/Documents/Workspace/Socket/Chatting/Picture"); // directory

		final FileInputStream file_data;
		BufferedReader inFromClient, inFromUser;
		final BufferedInputStream file_Send;
		DataOutputStream outToClient;

		InetSocketAddress client_socketAddress, server_socketAddress;

		System.out.println("Server Screen!");

		ServerSocket welcomeSocket = new ServerSocket(50000); 									// create 50000
																								// port

		server_socketAddress = (InetSocketAddress) welcomeSocket.getLocalSocketAddress();
		server_socketAddress.getAddress();														// getting information of server
																								// socket
		System.out.println("Server information: " + server_socketAddress.getAddress());
		System.out.println("Port number: " + 50000);

		System.out.println("Waiting the client to connect");
		Socket connectionSocket = welcomeSocket.accept();										 // waiting for the
																								// client

		client_socketAddress = (InetSocketAddress) connectionSocket.getRemoteSocketAddress();
		client_socketAddress.getAddress(); // return client's IP address

		System.out.println("Client [" + client_socketAddress.getAddress()
				+ ":50000] is Requesting a Connection. Accept Connection (Yes/No)?");

		request = input.nextLine(); // Yes/No

		if (request.equals("Yes")) {
			System.out.println("Connected!!");
			inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream())); // Text
																											// from
																											// client
			outToClient = new DataOutputStream(connectionSocket.getOutputStream()); 				// Text
																									// that
																									// will
																									// be
																									// sent
																									// to
																									// client			
			inFromUser = new BufferedReader(new InputStreamReader(System.in)); 						// Input
																									// Text
																									// from
																									// server

			new Thread() 																			// Starting Thread
			{
				public void run() {
					try {
						String clientSentence;

						while ((clientSentence = inFromClient.readLine()) != null) // It
																					// won't
																					// stop
																					// until
																					// it
																					// gets
																					// disconnected
																					// from
																					// the
																					// client
						{
							System.out.println("Client [" + client_socketAddress.getAddress() + ":50000] says : "
									+ clientSentence); // Printing client's text
							if (clientSentence.equals("quit")) {
								connectionSocket.close();
							} else if (clientSentence.equals("/view")) {

								String files[] = directory.list(); // storing
																	// list of
																	// files in
																	// the
																	// files[]
								System.out.print("Server [" + server_socketAddress.getAddress() + ":50000] says : File list: ");

								for (int i = 0; i < files.length; i++) // printing
																		// the
																		// file
																		// name
																		// in
																		// the
																		// directory
																		// and
																		// sending
																		// to
																		// the
																		// client
																		// as
																		// well
								{

									files[i] = files[i] + " ";
									System.out.print(files[i]);
									outToClient.writeBytes(files[i]);
								}
								System.out.println();
								outToClient.flush();

							} else if (clientSentence.contains("/get")) { // case
																			// of
																			// sending
																			// files
								String[] temporary_name = clientSentence.split(" "); // splitting
																						// words
																						// using
																						// "
																						// "
								String file_name = temporary_name[1]; // storing
																		// each
																		// file
																		// in
																		// the
																		// string
								FileInputStream file_data = new FileInputStream(
										"C:/Users/lg/Documents/Workspace/Socket/Chatting/Picture/" + file_name); // Where
																													// to
																													// call
																													// the
																													// data
																													// from
								BufferedInputStream file_Send = new BufferedInputStream(file_data);
								System.out
										.print("Server [" + server_socketAddress.getAddress() + ":50000] says : SENT ");
								System.out.println(file_name);
								file_name = "SENT " + file_name;
								outToClient.writeBytes(file_name); // send the
																	// messages
																	// as well
								file_Send.close();
								file_data.close();

							}
						}
					}

					catch (IOException ex) {
						ex.printStackTrace();
					}
				}
			}.start();

			while (true) {
				clientSentence = inFromUser.readLine(); // Reading client's text
														// from the socket
				System.out.println("Server [" + server_socketAddress.getAddress() + ":50000] says : " + clientSentence);
				clientSentence = clientSentence + '\n'; // changing the line
				outToClient.writeBytes(clientSentence); // Writing the text that
														// needs to be sent to
														// the client

				if (clientSentence.equals("quit"))
					break; // quits if it is written quit

			}
			connectionSocket.close(); // closing the socket from client
		} else {
			System.out.println("Accept Denied!"); // If the request is not
													// accepted
			connectionSocket.close();
			welcomeSocket.close();
			System.out.println("!!!GOOD BYE!!!");
		}
	}
}
