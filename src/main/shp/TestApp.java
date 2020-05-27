package shp;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.vividsolutions.jts.geom.*;
import com.vividsolutions.jts.io.WKTReader;
import org.geotools.data.*;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.geotools.data.shapefile.dbf.DbaseFileHeader;
import org.geotools.data.shapefile.dbf.DbaseFileReader;
import org.geotools.data.shapefile.files.ShpFiles;
import org.geotools.data.shapefile.shp.ShapefileReader;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;

import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


/**
 * Create by leinuo on 2020/4/22 上午10:03
 * <p>
 * qq:1321404703 https://github.com/leinuo2016
 */
public class TestApp {
    public static void main(String[] args) throws IOException {
        String shapeFile =  "*.shp";
        testRead(shapeFile);
        //getShpFileAttributes(file);
        //readShape(shapeFile);
        //readDBF(dbfFile);
        //write(newFile);
        //transShape(shapeFile,newFile1);
        //testRead1(newFile1);
    }

    public static void readShape(String file) {
        try {
            GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();
            WKTReader reader = new WKTReader( geometryFactory );
            LineString lineString = (LineString) reader.read(
                    "LINESTRING (254058.76074485347 475001.2186020431, 255351.04293761664 474966.9279243938)");
            // 设置保留6位小数，否则GeometryJSON默认保留4位小数
            //GeometryJSON geometryJson = new GeometryJSON(6);
            StringWriter writer = new StringWriter();
           // geometryJson.write(lineString, writer);
            System.out.println(writer.toString());
            writer.close();

            ShpFiles sf = new ShpFiles(file);
            ShapefileReader r = new ShapefileReader( sf, false, false, new GeometryFactory() );
            while (r.hasNext()) {
                Geometry shape = (Geometry) r.nextRecord().shape();
                System.out.println(shape.toString());
            }
            r.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void readDBF(String file) {
        try {
            FileChannel in = new FileInputStream(file).getChannel();
            DbaseFileReader dbfReader =  new DbaseFileReader(in, false,  Charset.forName("utf-8"));
            DbaseFileHeader header = dbfReader.getHeader();
            int fields = header.getNumFields();

            while ( dbfReader.hasNext() ){
                DbaseFileReader.Row row =  dbfReader.readRow();
//              System.out.println(row.toString());
                for (int i=0; i<fields; i++) {
                    System.out.println(header.getFieldName(i) + " : " + row.read(i));
                }
            }
            dbfReader.close();
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void write(String filepath) {
        try {
            //创建shape文件对象
            File file = new File(filepath);
            Map<String, Serializable> params = new HashMap<String, Serializable>();
            params.put( ShapefileDataStoreFactory.URLP.key, file.toURI().toURL() );
            ShapefileDataStore ds = (ShapefileDataStore) new ShapefileDataStoreFactory().createNewDataStore(params);
            //定义图形信息和属性信息
            SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
            tb.setCRS(DefaultGeographicCRS.WGS84);
            tb.setName("shapefile");
            tb.add("the_geom", Point.class);
            tb.add("POIID", Long.class);
            tb.add("NAMEC", String.class);
            ds.createSchema(tb.buildFeatureType());
            ds.setCharset(Charset.forName("utf-8"));
            //设置Writer
            FeatureWriter<SimpleFeatureType, SimpleFeature> writer = ds.getFeatureWriter(ds.getTypeNames()[0], Transaction.AUTO_COMMIT);
            //写下一条
            SimpleFeature feature = writer.next();
            feature.setAttribute("the_geom", new GeometryFactory().createPoint(new Coordinate(116.123, 39.345)));
            feature.setAttribute("POIID", 1234567890l);
            feature.setAttribute("NAMEC", "某兴趣点1");
            feature = writer.next();
            feature.setAttribute("the_geom", new GeometryFactory().createPoint(new Coordinate(116.456, 39.678)));
            feature.setAttribute("POIID", 1234567891l);
            feature.setAttribute("NAMEC", "某兴趣点2");
            writer.write();
            writer.close();
            ds.dispose();

            //读取刚写完shape文件的图形信息
            ShpFiles shpFiles = new ShpFiles(filepath);
            ShapefileReader reader = new ShapefileReader(shpFiles, false, true, new GeometryFactory(), false);
            try {
                while (reader.hasNext()) {
                    System.out.println(reader.nextRecord().shape());
                }
            } finally {
                reader.close();
            }
        } catch (Exception e) { }
    }

    public static void transShape(String srcfilepath, String destfilepath) {
        try {
            //源shape文件
            ShapefileDataStore shapeDS = (ShapefileDataStore) new ShapefileDataStoreFactory().createDataStore(new File(srcfilepath).toURI().toURL());
            //创建目标shape文件对象
            Map<String, Serializable> params = new HashMap<String, Serializable>();
            FileDataStoreFactorySpi factory = new ShapefileDataStoreFactory();
            params.put(ShapefileDataStoreFactory.URLP.key, new File(destfilepath).toURI().toURL());
            ShapefileDataStore ds = (ShapefileDataStore) factory.createNewDataStore(params);
            // 设置属性
            SimpleFeatureSource fs = shapeDS.getFeatureSource(shapeDS.getTypeNames()[0]);
            //下面这行还有其他写法，根据源shape文件的simpleFeatureType可以不用retype，而直接用fs.getSchema设置
            ds.createSchema(SimpleFeatureTypeBuilder.retype(fs.getSchema(), DefaultGeographicCRS.WGS84));

            //设置writer
            FeatureWriter<SimpleFeatureType, SimpleFeature> writer = ds.getFeatureWriter(ds.getTypeNames()[0], Transaction.AUTO_COMMIT);

            //写记录
            SimpleFeatureIterator it = fs.getFeatures().features();
            try {
                while (it.hasNext()) {
                    SimpleFeature f = it.next();
                    SimpleFeature fNew = writer.next();
                    fNew.setAttributes(f.getAttributes());
                    writer.write();
                }
            } finally {
                it.close();
            }
            writer.close();
            ds.dispose();
            shapeDS.dispose();
        } catch (Exception e) { e.printStackTrace();    }
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

    public static JSONArray getShpFileAttributes(String file)  {
        JSONArray jsonArray = new JSONArray();
        try {
            List<Object> list;
            // 使用GeoTools读取ShapeFile文件
            File shapeFile = new File(file);
            FileDataStore store = FileDataStoreFinder.getDataStore(shapeFile);
            // GeoTools读取ShapeFile文件的默认编码为ISO-8859-1。而我们中文操作系统下ShapeFile文件的默认编码一般为utf-8
            ((ShapefileDataStore) store).setCharset(Charset.forName("utf-8"));
            SimpleFeatureSource featureSource = store.getFeatureSource();
            // 提取出属性的 ID 值
            List<String> attributeID = new LinkedList<String>();
            List<AttributeDescriptor> attrList= featureSource.getSchema().getAttributeDescriptors();
            for(AttributeDescriptor attr : attrList){
                String ID = attr.getName().getLocalPart();
                if( !ID.equals("the_geom")){
                    attributeID.add(ID);
                }
            }
            SimpleFeatureCollection featureCollection = featureSource.getFeatures();
            SimpleFeatureIterator featureIterator = featureCollection.features();

            while (featureIterator.hasNext()) {
                SimpleFeature feature = featureIterator.next();
                JSONObject jsonObject = new JSONObject();
                list = feature.getAttributes();
                for(int k = 1; k < list.size(); k++ ){
                    jsonObject.put(attributeID.get(k-1), list.get(k));
                }
                jsonArray.add(jsonObject);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(jsonArray.toString());
        return jsonArray;
    }

}
