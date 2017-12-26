package com.jarvis.tbaseviewlib.bitmap.takephoto.model;

import com.jarvis.tbaseviewlib.model.image.ImageItem;

import java.util.ArrayList;

/**
 * Created by tansheng on 2017/6/27.
 */
public class TResult {
    private ArrayList<ImageItem> images;

    public ArrayList<ImageItem> getImages() {
        return images;
    }

    public void setImages(ArrayList<ImageItem> images) {
        this.images = images;
    }
    public void addImage(ImageItem image){
        if (images==null){
            images=new ArrayList<>();
        }
        images.add(image);
    }
}
