import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

//����ڷκ��� ���۵� ���ڿ��� �޾� ���濡�� �ش� ���ڿ��� �����ִ� Ŭ����
class EchoThread extends Thread{
       Socket socket;
       Vector<Socket> vec;
       
       public EchoThread(Socket socket, Vector<Socket> vec){
             this.socket = socket;
             this.vec = vec;
       }
       
       public void run(){
             BufferedReader br = null;
             try{
                    br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    String str = null;
                    while(true){
                           //����ڷκ��� ���ڿ� �ޱ�
                           str=br.readLine();
                           //������ ������ ����� ���Ϳ��� �����ϰ� break;
                           if(str==null){
                                 vec.remove(socket);
                                 break;
                           }

                           //����� ���ϵ��� ���ؼ� �ٸ� ����ڿ��� ���ڿ� �����ֱ�
                           sendMsg(str);                   
                    }
             }catch(IOException ie){
                    System.out.println(ie.getMessage());
             }finally{
                    try{
                           if(br != null) br.close();
                           if(socket != null) socket.close();
                    }catch(IOException ie){
                           System.out.println(ie.getMessage());
                    }
             }
       }


       //���۹��� ���ڿ� �ٸ� ����ڵ鿡�� �����ִ� �޼���
       public void sendMsg(String str){
             try{
                 //������ ������ �����͸� ���� ������ ������� ��츦 �����ϰ� ������ ���ϵ鿡�Ը� �����͸� �����ϴ� for��
                    for(Socket socket:vec){
                           if(socket != this.socket){
                                 PrintWriter pw = new PrintWriter(socket.getOutputStream(), true);
                                 pw.println(str);
                                 pw.flush();
                           }
                    }
             } catch(IOException ie){
                    System.out.println(ie.getMessage());
             }
       }
}

public class Server {
       public static void main(String[] args) {
             ServerSocket server = null;
             Socket socket =null;
             //����ڿ� ����� ���ϵ��� �迭ó�� ������ ���� ��ü ����
             Vector<Socket> vec = new Vector<Socket>();
             
             try{
                    server = new ServerSocket(80);
                    while(true){
                           System.out.println("��� ���Դϴ�.");
                           socket = server.accept();
                           //����ڿ� ����� ������ ���Ϳ� ����
                           vec.add(socket);
                           
                           new EchoThread(socket, vec).start(); //������ ������ Ŭ������ ��ü ����
                    }
             }catch(IOException ie){
                    System.out.println(ie.getMessage());
             }
       }
}