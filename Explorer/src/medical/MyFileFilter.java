package medical;
import java.io.File;
import javax.swing.filechooser.FileFilter;
import java.util.Vector;
import java.util.StringTokenizer;
import java.util.*;

//�ļ�������

public class MyFileFilter extends FileFilter
{
	Vector extensions = new Vector(); //����һ����������ʹ���ڲ���������Ĵ�СΪ 10
    String description;   
    public MyFileFilter(String extensions,String description)
    {
    	super();   //���ַ����ֽ�Ϊ���
        StringTokenizer stringTokenizer = new StringTokenizer(extensions,";");   
        while(stringTokenizer.hasMoreElements())   //�����Ƿ���������Ԫ��
        	this.extensions.add(stringTokenizer.nextElement());   
        this.description = description;   
    }   
    public boolean accept(File file)
    {
    	if(file.isDirectory())   
        	return   true;   
        String fileName = file.getName();   
        int periodIndex = fileName.lastIndexOf(".");   
        if(periodIndex > 0 && periodIndex < fileName.length() - 1)
        {
        	String extension = fileName.substring(periodIndex + 1).toLowerCase();   
            for(int i = 0;i < extensions.size();i++)   
            	if(extension.equals(extensions.elementAt(i)))   
                	return true;   
        }   
        return false;   
    }   
    public String getDescription()
    {
    	String s = "";   
        for(int i = 0;i < extensions.size();i++)   
        	s += "*." + extensions.elementAt(i) + " ; ";   
        return description + "(" + s + ")";   
    }   
}