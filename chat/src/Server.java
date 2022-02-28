import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

//사용자로부터 전송된 문자열을 받아 상대방에게 해당 문자열을 보내주는 클래스
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
                           //사용자로부터 문자열 받기
                           str=br.readLine();
                           //상대방이 접속을 끊기면 벡터에서 제거하고 break;
                           if(str==null){
                                 vec.remove(socket);
                                 break;
                           }

                           //연결된 소켓들을 통해서 다른 사용자에게 문자열 보내주기
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


       //전송받은 문자열 다른 사용자들에게 보내주는 메서드
       public void sendMsg(String str){
             try{
                 //현재의 소켓이 데이터를 보낸 동일한 사용자인 경우를 제외하고 나머지 소켓들에게만 데이터를 전송하는 for문
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
             //사용자와 연결된 소켓들을 배열처럼 저장할 벡터 객체 생성
             Vector<Socket> vec = new Vector<Socket>();
             
             try{
                    server = new ServerSocket(80);
                    while(true){
                           System.out.println("대기 중입니다.");
                           socket = server.accept();
                           //사용자와 연결된 소켓을 벡터에 저장
                           vec.add(socket);
                           
                           new EchoThread(socket, vec).start(); //위에서 선언한 클래스의 객체 생성
                    }
             }catch(IOException ie){
                    System.out.println(ie.getMessage());
             }
       }
}