package shp;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ReUtil;
import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import org.h2.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Objects;

/**
 * Create by leinuo on 2021/9/8
 */
public class ImageInfoReader {

    public static void main(String[] args) throws ImageProcessingException, IOException {
        String url = "/home/leinuo/桌面/panorama/451311217.jpg";
        //readInfo(url);
        url = "/home/leinuo/桌面/panorama/1616477967121jxb.jpg";
       // readInfo(url);
        //readInfoByPath();

        testReg("00120120");
        testReg("001ceshi");

    }

    public static void testReg(String search){
        String name = "", rtuCode = "";
        if (!StringUtils.isNullOrEmpty(search)) {
            if (ReUtil.isMatch("\\d+", search)) {
                rtuCode = search;
            } else {
                name = search;
            }
        }
        System.out.println("name:"+name+",rtuCode:"+rtuCode);
    }

    public static void readInfoByPath() throws ImageProcessingException, IOException {
        String path = "/home/leinuo/桌面/data/panorama/";
        File file = FileUtil.file(path);
        File[] files = file.listFiles();
        for (File file1 : files) {
            if(file1.isDirectory()){
                continue;
            }
            readInfoByFile(file1);
        }
    }


    private static void readInfoByFile(File file) throws ImageProcessingException, IOException {
        Metadata metadata = ImageMetadataReader.readMetadata(file);
        read(metadata);
    }

    private static void read(Metadata metadata) {
        for (Directory directory : metadata.getDirectories()) {

            Collection<Tag> tags = directory.getTags();

            ImageInfo imageInfo = new ImageInfo();
            for (Tag tag : tags) {

                if(tag.getDirectoryName().contains("GPS")){
                    if(Objects.equals("GPS Latitude",tag.getTagName())){
                        imageInfo.setLat(tag.getDescription());
                    }
                    if(Objects.equals("GPS Longitude",tag.getTagName())){
                        imageInfo.setLng(tag.getDescription());
                    }
                }
                if(tag.getDirectoryName().contains("Photoshop")) {
                    if (Objects.equals("Slices", tag.getTagName())) {
                        imageInfo.setName(tag.getDescription());
                    }
                }
            }
        }
        System.out.println(ImageInfo.toString1());

        System.out.println("----------------------------------------------------");
    }


    private static void readInfo(String url) throws ImageProcessingException, IOException {
        Metadata metadata = ImageMetadataReader.readMetadata(new File(url));
        read(metadata);
    }


    static class ImageInfo{
        private static String name;
        private static String lat;
        private static String lng;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getLat() {
            return lat;
        }

        public void setLat(String lat) {
            this.lat = lat;
        }

        public String getLng() {
            return lng;
        }

        public void setLng(String lng) {
            this.lng = lng;
        }


        //@Override
        public static String toString1() {
            return "ImageInfo{" +
                    "name='" + name + '\'' +
                    ", lat='" + lat + '\'' +
                    ", lng='" + lng + '\'' +
                    '}';
        }
    }

}
