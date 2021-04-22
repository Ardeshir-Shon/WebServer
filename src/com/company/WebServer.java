package com.company;

import Services.*;
import Services.Services;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class WebServer implements Runnable {

    String type;
    public static int count=0;
    String req;
    Socket socketInstance;
    WebServer(Socket classSocket) {
        this.socketInstance = classSocket;
        type="";
        req="";
    }


    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8086);
        String string=WebServer.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        String path=string.replaceAll("/","\\\\");
        path=path.replaceAll("%20"," ");
        path=path.split("out")[0];
        path+="home_directory";
        path+="\\index.html";
        System.out.println(path);
        File htmlFile = new File(path);
        //Desktop.getDesktop().browse(htmlFile.toURI());
        ThreadPoolExecutor executor =(ThreadPoolExecutor) Executors.newCachedThreadPool();

        System.out.println("Listening");

        while (true) {
            Socket sock = serverSocket.accept();
            System.out.println("Connected");
            executor.execute(new WebServer(sock));
        }
    }

    @Override
    public void run() {
        try {
            boolean notFound=true;
            count++;
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    socketInstance.getInputStream()));
            PrintWriter out = new PrintWriter(socketInstance.getOutputStream());

            String str = "something";
            while (!str.equals("")) {
                str = in.readLine();
                //in.read();
                if (this.type.equals("") && str.contains("GET"))
                    this.type="GET";
                else if (this.type.equals("") && str.contains("POST"))
                    this.type="POST";
                req+=str+"\n";
            }
            String[] request=req.split("\n");
            System.out.println(request[0]);
            String[] header=request[0].split(" ")[1].split("/");
            System.out.println(header.length);
            if (header.length!=0){
                System.out.println(header[header.length-1]);
                System.out.print(req);
                System.out.println(count);


                if (header[header.length-1].contains(".html")){//GET PAGE
                    // GET PAGE
                    String string=WebServer.class.getProtectionDomain().getCodeSource().getLocation().getPath();
                    String path=string.replaceAll("/","\\\\");
                    path=path.replaceAll("%20"," ");
                    path=path.split("out")[0];
                    path+="home_directory\\"+header[header.length-1];
                    File f = new File(path);
                    System.out.println(path);
                    System.out.println(f.exists());
                    if (f.exists() && !f.isDirectory()){
                        notFound=false;
                        String line;
                        FileInputStream fin = null;
                        BufferedReader br = null;
                        InputStreamReader isr = null;
                        String body="";
                        try {
                            fin =  new FileInputStream(path);
                            isr = new InputStreamReader(fin);
                            br = new BufferedReader(isr);
                            while ((line = br.readLine()) != null) {
                                body+=line+"\n";
                            }
                        }
                        finally {
                            if (br != null)  br.close();
                            if (isr != null) isr.close();
                            if (fin != null) fin.close();
                        }
                        out.println("HTTP/1.1 200 OK");
                        out.println("Content-Type: text/html");
                        out.println("Server: Ardeshir-Server");
                        out.println("");
                        out.println(body);
                    }
                }

                else if(header[header.length-1].contains(".do")) {//Service
                    // Run Service
                    notFound = false;
                    String className;
                    Map<String, String> test = new HashMap<>();
                    if (header[header.length-1].contains("?")){
                        className="Services."+header[header.length-1].split("\\?")[0].replace(".do","");
                        String temp=header[header.length-1].split("\\?")[1];
                        String[] arr=temp.split("&");
                        for (int i=0;i<arr.length;i++)
                            test.put(arr[i].split("=")[0],arr[i].split("=")[1]);
                    }
                    else
                        className="Services."+header[header.length-1].replace(".do","");
                    System.out.println(className);
                    //Services inst = (Services) Class.forName("Services.GetHelloWorld").newInstance();
                    Services inst = (Services) Class.forName(className).newInstance();
                    out.println("HTTP/1.1 200 OK");
                    out.println("Content-Type: text/html");
                    out.println("Server: Ardeshir-Server");
                    out.println("");
                    out.print("<H1>");
                    if (this.type.equals("GET"))
                        out.print(inst.GET(test));
                    else
                        out.print("POST :"+inst.POST(test,test));
                    out.println("</H1>");

                }

                if(notFound){//NOT FOUND
                    out.println("HTTP/1.1 404 Not Found");
                    out.println("Content-Type: text/html");
                    out.println("Server: Ardeshir-Server");
                    out.println("");
                    out.println("<!DOCTYPE HTML PUBLIC \"-//IETF//DTD HTML 2.0//EN\">\n" +
                            "<html>\n" +
                            "<head>\n" +
                            "   <title>404 Not Found</title>\n" +
                            "</head>\n" +
                            "<body>\n" +
                            "   <h1>Not Found</h1>\n" +
                            "   <p>The requested URL /t.html was not found on this server.</p>\n" +
                            "</body>\n" +
                            "</html>");
                }
            }
            else {
                out.println("HTTP/1.1 200 OK");
                out.println("Content-Type: text/html");
                out.println("Server: Ardeshir-Server");
                out.println("");
                out.println("Main Page");
            }

            out.flush();
            socketInstance.close();

        } catch (IOException e) {
            System.out.println(e);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}

