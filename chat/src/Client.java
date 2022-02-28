import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

// 1. ����ڰ� ���ڸ� �Է��ϰ� �̸� ������ ������
// 2. ������ ������ ���� ���ڸ� �޾Ƽ� ä�ù濡 ����� �ֱ� -> ����ڰ� �� �� ����(������)
// 

// Ű����� ���۹��ڿ� �Է¹޾� ������ �����ϴ� Ŭ����
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
             //Ű����κ��� ���ڿ��� �о���� ���� ��ü ����
             BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
             PrintWriter pw=null;

             try{
                    //������ ���ڿ��� �����ϱ� ���� ��ü ����
                    pw=new PrintWriter(socket.getOutputStream(),true);

                    //���濡�� �� id�� �Բ� �� IP�� ����
                    if(cf.isFirst==true){
                           InetAddress iaddr=socket.getLocalAddress();                      
                           String ip = iaddr.getHostAddress();                        
                           getId();
                           System.out.println("ip:"+ip+" id:"+id);
                           str = "["+id+"]�� �α��� ("+ip+")";
                    }else{
                           str= "["+id+"] "+cf.txtF.getText();
                    }
                    
                    //�Է¹��� ���ڿ��� ������ ����
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

//������ ������ ���ڿ��� ���۹޴� Ŭ����
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
                    //�����κ��� ���۵� ���ڿ� �о���� ���� ��ü ����
                    br=new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    while(true){
                           //�������κ��� ���ڿ� �о��
                           String str=br.readLine();
                           if(str==null){
                                 System.out.println("������ ������ϴ�.");
                                 break;
                           }
                           //���۹��� ���ڿ��� ȭ�鿡 ���
                           System.out.println("[Ȯ�ο�] " + str);
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
                    System.out.println("���������� ����Ǿ����ϴ�.");
                    cf = new ClientFrame(socket);
                    new ReadThread(socket, cf).start();
             }catch(IOException ie){
                    System.out.println(ie.getMessage());
             }
       }
}