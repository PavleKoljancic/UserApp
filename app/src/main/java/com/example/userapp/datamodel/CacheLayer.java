package com.example.userapp.datamodel;



import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.util.Objects;

public class CacheLayer  {

    private File cacheDir;

    private static CacheLayer  instance = null;

    public static CacheLayer createCacheLayer(File cacheDir)
    {
        if(instance ==null)
            instance = new CacheLayer(cacheDir);
        return instance;

    }

    public static CacheLayer getInstance()
    {
         return instance;
    }
    private CacheLayer(File cacheDir)
    {
        this.cacheDir = cacheDir;

    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CacheLayer)) return false;
        CacheLayer that = (CacheLayer) o;
        return cacheDir.equals(that.cacheDir);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cacheDir);
    }







    public boolean writeObject (Object object, String fileName)
    {
        if(object!=null)
            try {
                File userFile = new File(cacheDir.getPath(),fileName);

                ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(userFile));
                objectOutputStream.writeObject(object);
                objectOutputStream.close();
                return true;
            } catch (FileNotFoundException e) {


            } catch (IOException e) {

            }

        return false;
    }


    public Object readObject (String fileName)
    {
        try {
            File file = new File(cacheDir.getPath(),fileName);
            if(!file.exists())
                return null;
            ObjectInputStream objectOutputStream = new ObjectInputStream(new FileInputStream(file));
            Object res = objectOutputStream.readObject();
            objectOutputStream.close();
            return res;
        } catch (FileNotFoundException e) {


        } catch (IOException e) {

        } catch (ClassNotFoundException e) {

        }

        return null;

    }


    public void deleteAll ()
    {

        for(File file : this.cacheDir.listFiles())
            if(file.exists()&& file.canWrite())
                file.delete();

    }



}
