import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
 
public class WebServer{
 
    private static Socket socket;
 
    public static void main(String[] args){
        //create the main server socket
        int port = 5678;
        ServerSocket serverSocket;
        try{
            serverSocket = new ServerSocket(port);
        }catch(Exception e){
            System.out.println("Error: " + e);
            return;
        }

        System.out.println("Server Started and listening to the port 5678");
        while(true){
            try{                
     
                socket = serverSocket.accept();
                InputStream is = socket.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(isr);
                
                //get data from client
                String[] msgFromClient = new String[100];

                String[] requestHeader = {};
                String request = br.readLine();
                requestHeader = request.split(" ");

                //split data from client to get elements of the header request
                int ctr = 0;
                String string=" ";
                while (!string.equals("")) {
                    string = br.readLine();
                    msgFromClient[ctr] = string;
                    ctr++;
            	}
     
                OutputStream os = socket.getOutputStream();
                OutputStreamWriter osw = new OutputStreamWriter(os);
                BufferedWriter bw = new BufferedWriter(osw);
                PrintWriter output = new PrintWriter(bw);
                
                //print the http request in the html page in table form
                output.println("HTTP/1.0 200 OK");
                output.println("Server: Bot");
                output.println("Content-Type: text/html");
                output.println("");
                output.println("<H1>CMSC 137 LECTURE PROJECT 2</H1>\n");
                output.println("<H2>MINI WEB SERVER</H2>");

                output.println("<html><table border = '1' cellpadding='5'>");
                output.println("<tr><td>Method:</td><td>" + requestHeader[0]+"</td></tr>");
                output.println("<tr><td>Request URL:</td><td>" + requestHeader[1] + "</td></tr>");
                output.println("<tr><td>HTTP Version:</td><td>" + requestHeader[2] + "</td></tr>");
                
                //print per row
                for(int i=0; i<ctr-1; i++){
                     String[] str = msgFromClient[i].split(" ");
                     output.println("<tr><td>" + str[0] + "</td>");
                     output.println("<td>");
                     for(int j = 1; j<str.length; j++){
                         output.println(str[j]);
                     }
                     output.println("</td>");
                }
                output.println("</table>");

                //get and open the file to be accessed and show its content/output together with the table 
                String filepath = requestHeader[1].substring(1, requestHeader[1].length());
                File file = new File(filepath);

                //if there is file to be accessed
                if(!requestHeader[1].equals("/")){
                    //if the file exists, show the output/content of the file
                    if(file.exists()){
                        
                        FileReader fw = new FileReader(file);
                        BufferedReader bfr = new BufferedReader(fw);
                        String line;

                        //if css file
                        if(requestHeader[1].contains(".css")){
                            output.println("<style>");
                        }

                        //if javascript file
                        else if(requestHeader[1].contains(".js")){
                            output.println("<script>");
                        }

                        //get the output of the file
                        while((line = bfr.readLine()) != null){
                            output.println(line);;
                        }

                        //if css file
                        if(requestHeader[1].contains(".css")){
                            output.println("</style>");
                        }

                        //if javascript file
                        else if(requestHeader[1].contains(".js")){
                            output.println("</script>");
                        }

                        bw.close(); 
                    }
                    //if the file to be accessed does not exist, the file will bre created
                    else{
                        
                        output.println("<H1>File not Found</h1>");  

                        file.createNewFile();                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               
                    }
                }

                bw.flush();
                socket.close();

            }
            catch (Exception e)
            {
                System.out.println(e);
            }
       }
    }
    
}
