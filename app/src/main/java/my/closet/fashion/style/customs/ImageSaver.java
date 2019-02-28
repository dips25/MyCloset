package my.closet.fashion.style.customs;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by biswa on 7/19/2017.
 */

public class ImageSaver {

    private Context context;
     private String directoryName="mycloset";
     private String imageName="image.png";

    public ImageSaver(Context context){
        this.context=context;
    }

    public ImageSaver setFileName(String imageName){
        this.imageName=imageName;
        return this;
    }


    public ImageSaver setDirectoryName(String directoryName){

        this.directoryName=directoryName;
        return this;
    }

    public void saveImage(Bitmap bmp) {

        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(createFile());
            bmp.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {

                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Bitmap load(){
        FileInputStream fileInputStream=null;
        try {
            fileInputStream=new FileInputStream(createFile());
            return BitmapFactory.decodeStream(fileInputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }finally {
            try {
                if (fileInputStream!=null){
                    fileInputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public void delete(){

        File finaldelete;
        finaldelete=context.getDir(directoryName,Context.MODE_PRIVATE);
        File mdelete=new File(finaldelete,imageName);
        mdelete.delete();
        return ;
    }

    private File createFile() {
        File directory;
        directory=context.getDir(directoryName,Context.MODE_PRIVATE);
        return new File(directory,imageName);
    }

}



