import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

// 1. 사용자가 문자를 입력하고 이를 서버에 보내기
// 2. 상대방이 서버에 보낸 문자를 받아서 채팅방에 출력해 주기 -> 사용자가 볼 수 있음(전해줌)
// 

// 키보드로 전송문자열 입력받아 서버로 전송하는 클래스
class WriteThread{
       Socket socket;
       ClientFrame cf;
       String str;
       String id;
       
       public WriteThread(ClientFrame cf) {
             this.cf  = cf;
             this.socket= cf.socket;
       }
       
       public void sendMsg() {
             //키보드로부터 문자열을 읽어오기 위한 객체 생성
             BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
             PrintWriter pw=null;

             try{
                    //서버로 문자열을 전송하기 위한 객체 생성
                    pw=new PrintWriter(socket.getOutputStream(),true);

                    //상대방에게 내 id와 함께 내 IP를 전송
                    if(cf.isFirst==true){
                           InetAddress iaddr=socket.getLocalAddress();                      
                           String ip = iaddr.getHostAddress();                        
                           getId();
                           System.out.println("ip:"+ip+" id:"+id);
                           str = "["+id+"]님 로그인 ("+ip+")";
                    }else{
                           str= "["+id+"] "+cf.txtF.getText();
                    }
                    
                    //입력받은 문자열을 서버로 전송
                    pw.println(str);
            
             }catch(IOException ie){
                    System.out.println(ie.getMessage());
             }finally{
                    try{
                           if(br!=null) br.close();
                    }catch(IOException ie){
                           System.out.println(ie.getMessage());
                    }
             }
       }     

       public void getId(){            
             id = Id.getId();
       }
}

//서버가 보내온 문자열을 전송받는 클래스
class ReadThread extends Thread{
       Socket socket;
       ClientFrame cf;
       
       public ReadThread(Socket socket, ClientFrame cf) {
             this.cf = cf;
             this.socket=socket;
       }

       public void run() {
             BufferedReader br = null;

             try{
                    //서버로부터 전송된 문자열 읽어오기 위한 객체 생성
                    br=new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    while(true){
                           //소켓으로부터 문자열 읽어옴
                           String str=br.readLine();
                           if(str==null){
                                 System.out.println("접속이 끊겼습니다.");
                                 break;
                           }
                           //전송받은 문자열을 화면에 출력
                           System.out.println("[확인용] " + str);
                           cf.txtA.append(str+"\n");
                    }
             }catch(IOException ie){
                    System.out.println(ie.getMessage());
             }finally{
                    try{
                           if(br!=null) br.close();
                           if(socket!=null) socket.close();
                    }catch(IOException ie){}
             }
       }
}

public class Client {
       public static void main(String[] args) {
             Socket socket=null;
             ClientFrame cf;
             
             try{
                    socket=new Socket("125.240.97.109", 80);
                    System.out.println("성공적으로 연결되었습니다.");
                    cf = new ClientFrame(socket);
                    new ReadThread(socket, cf).start();
             }catch(IOException ie){
                    System.out.println(ie.getMessage());
             }
       }
}