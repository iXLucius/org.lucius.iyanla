package org.lucius.components.codec;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;

public class UrlDecodeUtils {

    public static String decodeString(String s) {
        try {
            if (s != null) {
                if(s.equals(new String(s.getBytes("iso8859-1"), "iso8859-1")))
                {
                        s=new String(s.getBytes("iso8859-1"),"utf-8");
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return s;
    }

    public static void decodeObject(Object obj) {
        try {
            Field fields[] = obj.getClass().getDeclaredFields();// 获得对象所有属性
            for (int i = 0; i < fields.length; i++) {
                Field field = fields[i];
                String type = field.getType().toString();//得到此属性的类型  
                if (type.endsWith("String")) {  
                    field.setAccessible(true);
                    Object valueObj = field.get(obj);//得到此属性的值 
                    if(valueObj != null){
                        field.set(obj, UrlDecodeUtils.decodeString((String)valueObj)) ;        //给属性设值  
                    }
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

}
