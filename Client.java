
import java.io.*;
import java.net.*;

public class Client {
	public static void main(String argv[]) throws Exception {

		InetAddress my_ip = InetAddress.getLocalHost();

		System.out.println("Client Screen");
		System.out.println("Client information: " + my_ip.getHostAddress());
		try {
			String sentence; // declaring string that will save the text
			BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in)); // connecting
																								// text
																								// stream
																								// that
																								// will
																								// be
																								// sent
																								// to
																								// the
																								// server
			Socket clientSocket = new Socket(my_ip, 50000); // Client's ip and
															// port 50000 that I
															// chose randomly
			BufferedWriter outToServer = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream())); // Text
																														// sent
																														// to
																														// server
			BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream())); // Text
																													// received
																													// from
																													// server

			new Thread() // Starting Thread
			{
				public void run() {
					try {
						String sentence = " ";

						while ((sentence = inFromServer.readLine()) != null) // Continues
																				// until
																				// it
																				// gets
																				// disconnected
																				// with
																				// the
																				// server
						{
							System.out.println("Server [0.0.0.0:" + 50000 + "] says : " + sentence); // Printing
																										// the
																										// server's
																										// text.
																										// Didn't
																										// know
																										// how
																										// to
																										// get
																										// information
																										// of
																										// the
																										// server's
																										// ip

							if (sentence.equals("quit")) {
								break;
							} else if (sentence.contains("SENT")) { // receives
																	// data
								DataInputStream abc = new DataInputStream(clientSocket.getInputStream());

								String[] temporary_name1 = sentence.split(" ");
								String file_name = temporary_name1[1];
								File f = new File(file_name); // getting the
																// file name
																// creating file
																// and output
																// stream from
																// the file
								FileOutputStream FOS = new FileOutputStream(f);
								BufferedOutputStream BOS = new BufferedOutputStream(FOS);
								System.out.println("Client [" + my_ip.getHostAddress() + ":" + 50000
										+ "] says : Received: " + file_name);
								// sending the message as well
								BOS.flush();
								BOS.close();
								FOS.close();

							}
						}
					}

					catch (IOException ex) {
						ex.printStackTrace();
					}
				}
			}.start();

			while (true) {
				sentence = inFromUser.readLine(); // Reading text from server
				System.out.println("Client [" + my_ip.getHostAddress() + ":" + 50000 + "] says : " + sentence);
				outToServer.write(sentence + '\n'); // changing line
				outToServer.flush(); // Sending the buffer

				if (sentence.equals("quit"))
					break;
			}
			clientSocket.close();
		} catch (IOException e) {
			System.err.println(e);
		}
	}
}
