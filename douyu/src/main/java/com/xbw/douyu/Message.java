package com.xbw.douyu;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

/**
 *
 * 功能描述：斗鱼消息实体
 *
 * @auther: xubowen
 * @date: 2018-08-06 10:28:48
 * 修改日志:
 *      
 */
public class Message {

    /**
     * 请求消息体包含五部分：
     * 1.计算后四部分的字节长度，占4个字节
     * 2.内容同上
     * 3.请求代码，固定，发到斗鱼是0xb1,0x02,0x00,0x00,接收是0xb2,0x02,0x00,0x00，4个字节
     * 4.消息正文
     * 5.尾部1个空字节
     */
    private int[] length1;
    private int[] length2;
    private int[] magic;
    private String content;
    private int[] end;

    public Message(String content) {
        length1 = new int[]{calcMessageLength(content), 0x00, 0x00, 0x00};
        length2 = new int[]{calcMessageLength(content), 0x00, 0x00, 0x00};
        magic = new int[]{0xb1, 0x02, 0x00, 0x00};
        this.content = content;
        end = new int[]{0x00};
    }

    /**
     * 计算消息体长度
     */
    private int calcMessageLength(String content) {
        return 4 + 4 + (content == null ? 0 : content.length()) + 1;
    }

    @Override
    public String toString() {
        return "Message{" +
                "length1=" + Arrays.toString(length1) +
                ", length2=" + Arrays.toString(length2) +
                ", magic=" + Arrays.toString(magic) +
                ", content='" + content + '\'' +
                ", end=" + Arrays.toString(end) +
                '}';
    }

    /**
     * 将Message对象转化为字节数组
     */
    public byte[] getBytes() throws IOException {
        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
        //写入长度1
        for(int b : length1){
            byteArray.write(b);
        }
        //写入长度2
        for(int b : length2){
            byteArray.write(b);
        }
        //写入消息类型
        for(int b : magic){
            byteArray.write(b);
        }
        //写入消息内容
        if(content != null){
            byteArray.write(content.getBytes("ISO-8859-1"));
        }
        //写入尾部结束符
        for(int b : end){
            byteArray.write(b);
        }
        return byteArray.toByteArray();
    }
}
