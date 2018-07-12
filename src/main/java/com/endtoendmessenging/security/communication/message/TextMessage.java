package com.endtoendmessenging.security.communication.message;

@MessageType(type=1,version=1)
public class TextMessage extends Message{
    public String text;
    public TextMessage(){
        text="";
    }
    public TextMessage(String message){
        text=message;
    }
    @Override
    public void handle(int version) {
        System.out.println("Received new Text messages : "+text);
    }

    @Override
    public byte[] toBytes() {
        return text.getBytes();
    }

    @Override
    public void readBytes(int version,byte[] input) {
        this.text=new String(input);
    }


}
