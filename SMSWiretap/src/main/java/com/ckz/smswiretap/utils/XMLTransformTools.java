package com.ckz.smswiretap.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.xmlpull.v1.XmlSerializer;

import android.content.Context;
import android.util.Xml;
import android.widget.Toast;

import com.ckz.smswiretap.app.MainActivity;

/**
 * XML序列化，XML解析等
 * @author Administrator
 *
 */
public class XMLTransformTools extends MainActivity{
	
	/*public class XMLTransformTools<T> {
	
	private Class<T> entity;
	
	public XMLTransformTools(Class<T> entity){
		this.entity=entity;
	}*/

	/**
	 * 实体集序列化为XML，并保存XML文件到指定位置
	 *
	 * @param list 需要序列化的实体集
	 * @param file 文件流，为null时默认：new File(context.getFilesDir(), "SMSWiretap.xml")
	 * @param rootTag 根节点，为null时默认：SMS
	 * @param rootNextTag 次于根节点，为null时默认：everyContent
	 * @param encoding xml序列化编码，为null时默认：UTF-8
	 * @param message 提示消息，为null时默认：xml序列化成功!
	 * @param context 上下文
	 * @return 返回存储的位置
	 */
	public static File xmlSerializer(List<Map<String,String>> list,File file,String rootTag,String rootNextTag,String encoding,String message,Context context){
		try {
			XmlSerializer serializer =  Xml.newSerializer();
			if(file == null)
				file = new File(context.getFilesDir(), "SMSWiretap.xml");
			if(rootTag == null)
				rootTag="SMS";
			if(rootNextTag==null)
				rootNextTag="everyContent";
			if(encoding==null)
				encoding="UTF-8";
			if(message==null)
				message="xml序列化成功!";
			FileOutputStream fos = new FileOutputStream(file);//创建文件，并保存位置
			serializer.setOutput(fos, encoding);
			serializer.startDocument(encoding, true);
			serializer.startTag(null, rootTag);
			int size = list.size();
			int i=0;
			size=10;
			for(;i<size;i++){
				Map<String,String> values = list.get(i);//值的集合
				Iterator<String> iterator = values.keySet().iterator();
				List<String> fields = new ArrayList<String>();//键的集合
				while (iterator.hasNext()) {
					fields.add(iterator.next());
				}
				serializer.startTag(null, rootNextTag);
				int j=0;
				int len = fields.size();
				for(;j<len;j++){
					String field = fields.get(j);
					serializer.startTag(null, field);
					serializer.text(values.get(field));
					serializer.endTag(null, field);
				}
				serializer.endTag(null, rootNextTag);
			}
			serializer.endTag(null, rootTag);
			serializer.endDocument();
			Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
			return file;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}








}
