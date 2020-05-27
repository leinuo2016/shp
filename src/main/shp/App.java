package shp;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.vividsolutions.jts.geom.*;
import org.geotools.data.FeatureWriter;
import org.geotools.data.Transaction;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.feature.SchemaException;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geojson.feature.FeatureJSON;

import org.geotools.geojson.geom.GeometryJSON;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;


import java.io.*;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.util.*;

/**
 * Create by leinuo on 2020/5/18 上午11:36
 * <p>
 * qq:1321404703 https://github.com/leinuo2016
 */
public class App {


    public static void main(String[] args) throws ParseException, IOException, SchemaException {
        String shapeFile =  "**.shp";
        //Map map = shape2Geojson(shapeFile);
        //testRead(shapeFile);
        String json = shape2Geojson(shapeFile);
        System.out.println("shape2Geojson = " +json );
        geojson2Shape("**.shp",json);
    }
    /**
     * shp转换为Geojson
     * @param shpPath
     * @return
     */
    public static String shape2Geojson(String shpPath){


        FeatureJSON fjson = new FeatureJSON();
        String geoJson = "";
        try{
            StringBuffer sb = new StringBuffer();
            sb.append("{\"type\": \"FeatureCollection\",\"features\": ");

            File file = new File(shpPath);
            ShapefileDataStore shpDataStore = null;

            shpDataStore = new ShapefileDataStore(file.toURL());
            //设置编码
            Charset charset = Charset.forName("utf-8");
            shpDataStore.setCharset(charset);
            String typeName = shpDataStore.getTypeNames()[0];
            SimpleFeatureSource featureSource = null;
            featureSource =  shpDataStore.getFeatureSource (typeName);
            SimpleFeatureCollection result = featureSource.getFeatures();
            SimpleFeatureIterator itertor = result.features();
            JSONArray array = new JSONArray();
            while (itertor.hasNext())
            {
                SimpleFeature feature = itertor.next();
                StringWriter writer = new StringWriter();
                fjson.writeFeature(feature, writer);
                JSONObject json = JSON.parseObject(writer.toString());
                array.add(json);
            }
            itertor.close();
            sb.append(array.toString());
            sb.append("}");
            geoJson = sb.toString();
        }
        catch(Exception e){
            e.printStackTrace();

        }
        return geoJson;
    }

    /**
     * geojson转换为shp文件
     * @param shpPath
     * @return
     */
    public static Map geojson2Shape(String shpPath,String geojson){
        Map map = new HashMap();
        GeometryJSON gjson = new GeometryJSON();
        try{
            JSONObject json = JSON.parseObject(geojson);
            JSONArray features = (JSONArray) json.get("features");
            JSONObject feature0 = JSON.parseObject(features.get(0).toString());
            System.out.println(feature0.toString());
            String strType = ((JSONObject)feature0.get("geometry")).getString("type").toString();
            Class<?> geoType = null;
            switch(strType){
                case "Point":
                    geoType = Point.class;
                case "MultiPoint":
                    geoType = MultiPoint.class;
                case "LineString":
                    geoType = LineString.class;
                case "MultiLineString":
                    geoType = MultiLineString.class;
                case "Polygon":
                    geoType = Polygon.class;
                case "MultiPolygon":
                    geoType = MultiPolygon.class;
            }
            //创建shape文件对象
            File file = new File(shpPath);
            Map<String, Serializable> params = new HashMap<String, Serializable>();
            params.put( ShapefileDataStoreFactory.URLP.key, file.toURI().toURL() );
            ShapefileDataStore ds = (ShapefileDataStore) new ShapefileDataStoreFactory().createNewDataStore(params);
            //定义图形信息和属性信息
            SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
            tb.setCRS(DefaultGeographicCRS.WGS84);
            tb.setName("shapefile");
            tb.add("the_geom", geoType);
            tb.add("POIID", Long.class);
            ds.createSchema(tb.buildFeatureType());
            //设置编码
            Charset charset = Charset.forName("utf-8");
            ds.setCharset(charset);
            //设置Writer
            FeatureWriter<SimpleFeatureType, SimpleFeature> writer = ds.getFeatureWriter(ds.getTypeNames()[0], Transaction.AUTO_COMMIT);
            for(int i=0,len=features.size();i<len;i++){
                String strFeature = features.get(i).toString();
                Reader reader = new StringReader(strFeature);
                SimpleFeature feature = writer.next();
                feature.setAttribute("the_geom",gjson.readMultiPolygon(reader));
                feature.setAttribute("POIID",i);
                writer.write();
            }
            writer.close();
            ds.dispose();
            map.put("status", "success");
            map.put("message", shpPath);
        }
        catch(Exception e){
            map.put("status", "failure");
            map.put("message", e.getMessage());
            e.printStackTrace();
        }
        return map;
    }



    public static void testRead(String file) throws IOException {
        long start = System.currentTimeMillis();
        List<Object> list;
        // 使用GeoTools读取ShapeFile文件
        File shapeFile = new File(file);
        ShapefileDataStore store = new ShapefileDataStore(shapeFile.toURI().toURL());
        //设置编码
        Charset charset = Charset.forName("utf-8");
        store.setCharset(charset);
        SimpleFeatureSource sfSource = store.getFeatureSource();
        SimpleFeatureIterator sfIter = sfSource.getFeatures().features();
        // 从ShapeFile文件中遍历每一个Feature，然后将Feature转为GeoJSON字符串
        while (sfIter.hasNext()) {
            SimpleFeature feature = sfIter.next();
            //System.out.println(JsonsUtils.objectToString(feature));
            list = feature.getAttributes();
            System.out.println(list.toString());
        }
        System.out.println("数据导入完成，共耗时"+(System.currentTimeMillis() - start)+"ms");
    }


}
