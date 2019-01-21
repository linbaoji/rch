package com.rch.http;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/7/28.
 */

public class RequestParam {

    private final Map<String,String> strMap=new HashMap<>();
    private final List<FileWrapper> files=new ArrayList<>();


    public void add(String key,String value)
    {
        strMap.put(key,value);
    }

    public void addFile(String key,File file)
    {
        if(file!=null&&file.exists()&&!file.isDirectory())
        files.add(new FileWrapper(key,file));
    }

    //添加文件容器是否为空
    public boolean hasFiles()
    {
        return files.isEmpty();
    }

    public int filesCount()
    {
        return files.size();
    }

    //key、value迭代器
    void IteratorString(KeyValueIteratorListener keyValueIteratorListener)
    {
        if(strMap!=null&&!strMap.isEmpty())
        {
            for (Map.Entry<String,String> entry:strMap.entrySet()) {
                keyValueIteratorListener.onIterator(entry.getKey(),entry.getValue());
            }
        }
    }

    //添加文件、或者图片迭代器
    void IteratorFile(KeyFilesIteratorListener keyFilesIteratorListener)
    {
        if(files!=null&&!files.isEmpty())
        {
            for (FileWrapper filewrapper:files) {
               keyFilesIteratorListener.onIterator(filewrapper.key,filewrapper.file);
            }
        }
    }


    //key,value参数接口
    interface KeyValueIteratorListener
    {
        void onIterator(String key, String value);
    }

    //文件接口
    interface KeyFilesIteratorListener
    {
        void onIterator(String key, File file);
    }

    //文件实体类
    class FileWrapper
    {
        private String key;
        private File file;
        public FileWrapper(String key,File file)
        {
            this.key=key;
            this.file=file;
        }
    }

}
