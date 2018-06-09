package com.messengerclient.ui;

import com.sun.javafx.font.FontFactory;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class MainWindow {
    private Panel mainPanel= new Panel();
    JPanel contactList;
    JPanel messageList=new JPanel();
    JTextField messageInput=new JTextField(100);
    JButton sendButton=new JButton("Send");


    private JFrame window=null;
    public MainWindow(){
        this.window=new JFrame("Messenging Client");

        this.window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.window.setContentPane(this.mainPanel);
        init();
        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addMessage(messageInput.getText(),true);
            }
        };
        sendButton.addActionListener(actionListener);
    }

    public void init() {
        messageList.setLayout(new BoxLayout(messageList,BoxLayout.Y_AXIS));
        JPanel pTop = new JPanel();
        JPanel pLeft = contactList=new JPanel();
        pLeft.setAlignmentX(Component.CENTER_ALIGNMENT);

        pLeft.setMaximumSize(new Dimension(250,0));
        JPanel pCenter = new JPanel();
        JPanel pProperties = new JPanel();


        Container c = mainPanel;
        c.setLayout(new BorderLayout());
        JButton bNew = new JButton("Test Connectivity");
        pTop.add(bNew);
        JButton bOpen = new JButton("Subscribe");
        pTop.add(bOpen);
        JButton bSaveAll = new JButton("FetchMessages");


        BoxLayout leftLayout=new BoxLayout(pLeft,BoxLayout.Y_AXIS);
        pLeft.setLayout(leftLayout);


        addContact("Sudip Bhattarai");
        addContact("Sunita Khanal");

        pProperties.setLayout(new BoxLayout(pProperties, BoxLayout.Y_AXIS));



        //The center content.
        Panel pMid=new Panel();
        pMid.setLayout(new BorderLayout());

        Panel pInput=new Panel();
        pInput.setLayout(new BoxLayout(pInput,BoxLayout.X_AXIS));
        pInput.add(messageInput);
        messageInput.setBorder(BorderFactory.createEmptyBorder(0,0,0,20));
        messageInput.setMaximumSize(new Dimension(2000,25));
        pInput.add(sendButton);

        pMid.add(messageList,BorderLayout.CENTER);
        pMid.add(pInput,BorderLayout.SOUTH);

        c.add(pMid, BorderLayout.CENTER);

        c.add(pTop, BorderLayout.NORTH);
        c.add(pLeft, BorderLayout.WEST);
        c.add(new Label("Project Loaded Successfully!"), BorderLayout.SOUTH);
        c.add(pProperties, BorderLayout.EAST);
        addMessage("Not here",false);
    }
    public JFrame getFrame(){
        return window;
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
    public void show(){

        window.pack();
        window.setVisible(true);
    }
    public void addContact(String name){
        JLabel contact=new JLabel(name);
        contact.setBackground(Color.gray);
        contact.setMinimumSize(new Dimension(0,40));
        contact.setBorder(BorderFactory.createEmptyBorder(20,10,0,10));
        contact.setFont(contact.getFont().deriveFont(contact.getFont().getStyle()&~Font.BOLD));
        contact.addMouseListener(this.contactMouseListener);

        contactList.add(contact);
    }
    public void addMessage(String message,boolean sent){
        JLabel label=new JLabel(message);
        if(sent) {
            label.setAlignmentX(Component.LEFT_ALIGNMENT);
            label.setBackground(Color.pink);
        }
        else{
            label.setAlignmentX(Component.RIGHT_ALIGNMENT);
            label.setBackground(Color.lightGray);
        }
        label.setBorder(BorderFactory.createEmptyBorder(5,20,5,20));
        messageList.add(label);
        label.revalidate();
    }
    MouseListener contactMouseListener=new MouseListener(){

        JLabel earlierSelection=null;
        @Override
        public void mouseClicked(MouseEvent e) {

        }

        @Override
        public void mousePressed(MouseEvent e) {
            if(earlierSelection==e.getComponent()){
                return;
            }
            JLabel l= (JLabel) e.getComponent();
            if(l==null){
                return;
            }
            Font f=l.getFont();
            l.setFont(f.deriveFont(f.getStyle()|Font.BOLD));
            l.setBackground(Color.GREEN);
            if(earlierSelection!=null){

                f = earlierSelection.getFont();
                earlierSelection.setFont(f.deriveFont(f.getStyle()&~Font.BOLD));
                earlierSelection.setBackground(Color.lightGray);
            }
            earlierSelection=l;

        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {
            e.getComponent().setBackground(new Color(0xaaaaee));
        }

        @Override
        public void mouseExited(MouseEvent e) {
            e.getComponent().setBackground(Color.lightGray);
        }
    };

}
