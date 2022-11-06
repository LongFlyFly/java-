package ChatRoom;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.JProgressBar;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JLayeredPane;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.rmi.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.awt.event.ActionEvent;

public class kh {

	private JFrame frame;
	private JTextField WriteQ;
	private JTextArea names;
	private JTextField serverIp;
	private JTextField duanKo;
	private JPanel panel;
	private JList inline;
	private JTextArea viewQ;
	private JPanel panel_1;
	private JPanel panel_2;
	private JButton liveButton;
	private JButton linkButton;
	private JButton setButton;
	private int dk2;
	private boolean isConn=false;
	private Socket socket;
	private OutputStream out ;
	private PrintWriter writer;
	private String name;
	private boolean flag=true;
	private BufferedReader reader;
	private InputStream in;
	private JList userList;  
	private DefaultListModel listModel;
	private List<User> userlist=new ArrayList<User>();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					kh window = new kh();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public kh() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setTitle("�ͻ���");
		frame.setBounds(100, 100, 1105, 569);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel label = new JLabel("������");
		label.setBounds(15, 25, 81, 21);
		frame.getContentPane().add(label);
		
		JLabel lblip = new JLabel("������IP��");
		lblip.setBounds(237, 25, 99, 21);
		frame.getContentPane().add(lblip);
		
		JLabel label_1 = new JLabel("�˿ڣ�");
		label_1.setBounds(507, 25, 81, 21);
		frame.getContentPane().add(label_1);
		
	    linkButton = new JButton("����");
		linkButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("---------���߰�ť���----------");
				name =names.getText();
				String fuwuqi =serverIp.getText();
				String Duanko =duanKo.getText();
				String ip=fuwuqi;
				int port=Integer.parseInt(Duanko);
				try {
					 socket =new Socket(ip,port);
					 out =socket.getOutputStream();
					 writer=new PrintWriter(out);
					 in = socket.getInputStream();
					 reader = new BufferedReader(new InputStreamReader(in));
					//3.���߷���������˭
					 writer.println(socket.getInetAddress().getHostAddress()+"@"+name);
					 writer.flush();
					
					//���ܷ�������Ϣ
					ReceleThred receleThred = new ReceleThred(reader);
					receleThred.start();
					
//					User user = new User();
//					for(User u:userlist){
//						
//					}
					//4.��ť����
				}catch(UnknownHostException e1){
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO: handle exception
					e1.printStackTrace();
				}
				if(name.equals("")){
					JOptionPane.showMessageDialog(frame,"��������Ϊ��","����",JOptionPane.WARNING_MESSAGE);
					return;
				}
//				****************************
				if(fuwuqi.equals("")){
					JOptionPane.showMessageDialog(frame,"���������벻��Ϊ��","����",JOptionPane.WARNING_MESSAGE);
					return;
				}
//				****************************
				if(Duanko.equals("")){
					JOptionPane.showMessageDialog(frame,"�˿�Ϊ��","����",JOptionPane.WARNING_MESSAGE);
					return;
				}
				try{
					dk2=Integer.parseInt(Duanko);
				}catch(NumberFormatException e2){
					JOptionPane.showMessageDialog(frame,"�˿ڱ���Ϊ����","����",JOptionPane.WARNING_MESSAGE);
				}
				if(dk2<=0){
					JOptionPane.showMessageDialog(frame,"�˿�Ҫ����0","����",JOptionPane.WARNING_MESSAGE);
					return;
				}
				
				linkButton.setEnabled(false);
				liveButton.setEnabled(true);
				setButton.setEnabled(true);
				names.setEditable(false);
				serverIp.setEditable(false);
				duanKo.setEditable(false);
				
			}
		});
		linkButton.setBounds(727, 21, 123, 29);
		frame.getContentPane().add(linkButton);
		
	    liveButton = new JButton("�Ͽ�");
	    liveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//���߷���������
				writer.println("DELETE@"+name);
				writer.flush();
				try{
					  writer.close();
					  out.close();
					  reader.close();
					  in.close();
					  socket.close();
					  //�߳�ѭ����־����
					  flag=false;
					  listModel.removeAllElements();
				}catch(IOException e1){
					e1.printStackTrace();
				}
				liveButton.setEnabled(false);
				linkButton.setEnabled(true);
				setButton.setEnabled(false);
				names.setEditable(true);
				serverIp.setEditable(true);
				duanKo.setEditable(true);
			}
		});
		liveButton.setBounds(865, 21, 123, 29);
		liveButton.setEnabled(false);
		frame.getContentPane().add(liveButton);
		
		WriteQ = new JTextField();
		WriteQ.setBounds(15, 439, 776, 44);
		frame.getContentPane().add(WriteQ);
		WriteQ.setColumns(10);
		
		setButton = new JButton("����");
		setButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//ȡ
				String info = WriteQ.getText();
				//д
				writer.println("ALL@"+name+"@"+info);
				writer.flush();
//				viewQ.append(name+"��"+info+"\n");
				WriteQ.setText(null);
			}
		});
		setButton.setBounds(803, 439, 123, 44);
		setButton.setEnabled(false);
		frame.getContentPane().add(setButton);
		
		names = new JTextArea();
		names.setBounds(74, 23, 96, 27);
		frame.getContentPane().add(names);
		names.setColumns(10);
		
		serverIp = new JTextField();
		serverIp.setText("127.0.0.1");
		serverIp.setBounds(320, 22, 96, 27);
		frame.getContentPane().add(serverIp);
		serverIp.setColumns(10);
		
		duanKo = new JTextField();
		duanKo.setText("6666");
		duanKo.setBounds(552, 22, 96, 27);
		frame.getContentPane().add(duanKo);
		duanKo.setColumns(10);
		
		panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "д��Ϣ", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setBounds(0, 407, 946, 91);
		frame.getContentPane().add(panel);
		
		listModel = new DefaultListModel();  
		inline = new JList(listModel);
		inline.setBounds(26, 105, 183, 279);
		frame.getContentPane().add(inline);
		
		
		viewQ = new JTextArea();
		viewQ.getText();
		viewQ.setBounds(272, 105, 736, 279);
		frame.getContentPane().add(viewQ);
		viewQ.setEditable(false);
		viewQ.setColumns(10);
		
		panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(null, "�����û�", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_1.setBounds(0, 77, 230, 317);
		frame.getContentPane().add(panel_1);
		
		
		
		panel_2 = new JPanel();
		panel_2.setBorder(new TitledBorder(null, "��Ϣ��ʾ��", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_2.setBounds(255, 77, 776, 317);
		frame.getContentPane().add(panel_2);
	}
	//���ܷ������Ϣ
	public class ReceleThred extends Thread{
		private BufferedReader reader;
		public ReceleThred(BufferedReader reader){
			this.reader=reader;
		}
		 public void run(){
//			 flag = true;
			 while(flag){
				try {
				  String info=reader.readLine();
//				  System.out.println("info:"+info);
//				  viewQ.append(info+"\n");
//				  listModel.addElement(name);
				  StringTokenizer st = new StringTokenizer(info,"@");
				  String type = st.nextToken();
				  if(type.equals("ALL")){
					  viewQ.append(st.nextToken()+"˵��"+st.nextToken()+"\n");
				  }else if(type.equals("USERLIST")){
					  int no = Integer.parseInt(st.nextToken());
					  for(int i=0;i<no;i++){
						  String name = st.nextToken();
						  listModel.addElement(name);
					  }
				  }else if(type.equals("ADD")){
					  String username=st.nextToken();
					  String name=names.getText();
					  if(!(name.equals(username))){
						  listModel.addElement(username);
						  viewQ.append(username+"����"+"\n");
					  }
				  }else if(type.equals("SUCCESS")){
					  String mess = st.nextToken();
					  viewQ.append(mess+"\n");
				  }else if(type.equals("MAX")){
					  String mess = st.nextToken();
					  viewQ.append(mess);
					  writer.close();
					  out.close();
					  reader.close();
					  in.close();
					  socket.close();
					  flag=false;
				  }else if(type.equals("DELETE")){//�˳�
					  String names = st.nextToken();
					  listModel.removeElement(names);
					  viewQ.append(names+"����\n");
				  }else if(type.equals("CLOSE")){//�ر�
					  writer.close();
					  out.close();
					  reader.close();
					  in.close();
					  socket.close();
					  flag=true;
					  viewQ.append("����������\n");
					  listModel.removeAllElements();
				  }
				  else if(type.equals("out")){
					  String outName = st.nextToken();
					  listModel.removeElement(outName);
					  viewQ.append(outName+"�ѱ��߳�Ⱥ��\n");
				  }
				  } catch (IOException e) {
				JOptionPane.showMessageDialog(frame,"�ɹ�����","����",JOptionPane.WARNING_MESSAGE);
				e.printStackTrace();
			} 
		 }
	 }
   }
}
