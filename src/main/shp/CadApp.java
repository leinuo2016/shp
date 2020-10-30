package shp;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.util.IOUtils;
import org.kabeja.batik.tools.SAXPDFSerializer;
import org.kabeja.batik.tools.SAXPNGSerializer;
import org.kabeja.dxf.*;
import org.kabeja.parser.DXFParser;
import org.kabeja.parser.ParseException;
import org.kabeja.parser.Parser;
import org.kabeja.parser.ParserBuilder;
import org.kabeja.svg.SVGGenerator;
import org.kabeja.xml.AggregatorGenerator;
import org.kabeja.xml.SAXGenerator;
import org.kabeja.xml.SAXSerializer;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Create by leinuo on 2020/7/28
 * qq:1321404703 https://github.com/leinuo2016
 */
public class CadApp {


    public static void main(String[] args) throws FileNotFoundException, ParseException, SAXException {
        String sourceFile =  "/home/leinuo/gitHub/shp/src/main/resource/cadfiles/解放渠渠线定2.dxf";
       // String sourceFile =  "/home/leinuo/gitHub/shp/src/main/resource/cadfiles/点.dxf";
        String targetFile =  "/home/leinuo/gitHub/shp/src/main/resource/cadfiles/点.pdf";

        JSONObject content = getDocContent2(sourceFile);
        System.out.println(content);
       // parseFile(sourceFile,targetFile);
    }


    public static void parseFile(String sourceFile,String targetFile) throws FileNotFoundException, ParseException, SAXException {

        //InputStream in = new FileInputStream("C:/Users/Admin/Desktop/svg/draft.dxf");
        // Parser dxfParser = DXFParserBuilder.createDefaultParser();
        Parser dxfParser = ParserBuilder.createDefaultParser();
        dxfParser.parse(new FileInputStream(sourceFile), "GBK");//需要转换的dxf
        DXFDocument doc = dxfParser.getDocument();
        SAXGenerator generator = new SVGGenerator();
        // generate into outputstream
        // 输出SVG
        //SAXSerializer out = new SAXPDFSerializer();
        // 输出pdf
        // org.kabeja.xml.SAXSerialzer out =
        // org.kabeja.batik.tools.SAXPDFSerializer();
        // 输出tiff
        // org.kabeja.xml.SAXSerialzer out =
        // org.kabeja.batik.tools.SAXTIFFSerializer();
        // 输出png
        //SAXSerializer out =new SAXPNGSerializer();
        SAXSerializer out =new SAXPDFSerializer();
        // 输出jpg
        // org.kabeja.xml.SAXSerialzer out =
        // org.kabeja.batik.tools.SAXJEPGSerializer();
        OutputStream fileo = new FileOutputStream(targetFile);//转换所得的文件
        // out.setOutputStream(response.getOutputStream()) //write direct to
        // ServletResponse
        out.setOutput(fileo);
        // generate
        generator.generate(doc, out, new HashMap());
        IOUtils.close(fileo);
    }


    public static JSONObject getDocContent(String sourceFile){

        JSONObject jsonObject = new JSONObject();
        Parser parser = ParserBuilder.createDefaultParser();
        try {
            FileInputStream in = new FileInputStream(new File(sourceFile));
            //  "/x"参数用 parser.parse(in, "");
            //  "/x2010" 用parser.parse(in, "UTF-8");
            parser.parse(in, "GBK");  //编码集合
            DXFDocument doc = parser.getDocument();
            jsonObject.put("name",sourceFile);
            System.out.println("sourceFile = " + doc.toString());
            Iterator<?> it = doc.getDXFLayerIterator();
            JSONObject data;
            JSONArray jsonArray;
            Iterator a;
            JSONObject jsonObject1;
            while (it.hasNext()) {
                DXFLayer layer = (DXFLayer) it.next();
                List<?> text = layer.getDXFEntities(DXFConstants.ENTITY_TYPE_POLYLINE);
                if (text != null) {
                    jsonObject1 = new JSONObject();
                    jsonObject1.put("type",((DXFPolyline) (text.get(0))).getType());
                    jsonObject1.put("layerName",((DXFPolyline) (text.get(0))).getLayerName());
                    jsonObject1.put("ID",((DXFPolyline) (text.get(0))).getID());
                    List<JSONArray> jsonArrays = new ArrayList<>();
                    for (int i = 0; i < text.size(); i++) {
                        jsonArray = new JSONArray();
                        a = ((DXFPolyline) (text.get(i))).getVertexIterator();
                        while (a.hasNext()) {
                            data = new JSONObject();
                            DXFVertex dxfVertex = (DXFVertex) a.next();
                            data.put("x",dxfVertex.getX());
                            data.put("y",dxfVertex.getY());
                            data.put("z",dxfVertex.getZ());
                            jsonArray.add(data);
                        }
                        jsonArrays.add(jsonArray);
                    }
                    jsonObject1.put(DXFConstants.ENTITY_TYPE_POLYLINE,jsonArrays);
                    jsonObject.put(DXFConstants.ENTITY_TYPE_POLYLINE,jsonObject1);
                }
                List<?> mtext = layer.getDXFEntities(DXFConstants.ENTITY_TYPE_LWPOLYLINE);
                if (mtext != null) {
                    jsonObject1 = new JSONObject();
                    jsonObject1.put("type",((DXFLWPolyline) (mtext.get(0))).getType());
                    jsonObject1.put("layerName",((DXFLWPolyline) (mtext.get(0))).getLayerName());
                    jsonObject1.put("ID",((DXFLWPolyline) (mtext.get(0))).getID());
                    List<JSONArray> jsonArrays = new ArrayList<>();
                    for (int i = 0; i < mtext.size(); i++) {
                        jsonArray = new JSONArray();
                        a = ((DXFLWPolyline) (mtext.get(i))).getVertexIterator();
                        while (a.hasNext()) {
                            DXFVertex dxfVertex = (DXFVertex) a.next();
                            data = new JSONObject();
                            data.put("x",dxfVertex.getX());
                            data.put("y",dxfVertex.getY());
                            data.put("z",dxfVertex.getZ());
                            //System.out.println(dxfVertex.getPoint().toString());
                            jsonArray.add(data);
                        }
                        jsonArrays.add(jsonArray);
                    }
                    jsonObject1.put(DXFConstants.ENTITY_TYPE_LWPOLYLINE,jsonArrays);
                    jsonObject.put(DXFConstants.ENTITY_TYPE_LWPOLYLINE,jsonObject1);
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }


    public static JSONObject getDocContent1(String sourceFile){
        JSONObject jsonObject = new JSONObject();
        Parser parser = ParserBuilder.createDefaultParser();
        try {
            FileInputStream in = new FileInputStream(new File(sourceFile));
            //  "/x"参数用 parser.parse(in, "");
            //  "/x2010" 用parser.parse(in, "UTF-8");
            //parser.parse(in, "UTF-8");  //编码集合
            parser.parse(in, "GBK");  //编码集合
            DXFDocument doc = parser.getDocument();
            jsonObject.put("name",sourceFile);
            System.out.println("sourceFile = " + doc.toString());
            Iterator<?> it = doc.getDXFLayerIterator();
            JSONObject data;
            while (it.hasNext()) {
                DXFLayer layer = (DXFLayer) it.next();
                List<?> text = layer.getDXFEntities(DXFConstants.ENTITY_TYPE_LWPOLYLINE);
                if (text != null) {
                    JSONObject jsonObject1 = new JSONObject();
                    jsonObject1.put("type",((DXFLWPolyline) (text.get(0))).getType());
                    jsonObject1.put("layerName",((DXFLWPolyline) (text.get(0))).getLayerName());
                    jsonObject1.put("ID",((DXFLWPolyline) (text.get(0))).getID());
                    List<JSONArray> jsonArrays = new ArrayList<>();
                    for (int i = 0; i < text.size(); i++) {
                        JSONArray jsonArray = new JSONArray();
                        Iterator a = ((DXFLWPolyline) (text.get(i))).getVertexIterator();
                        while (a.hasNext()) {
                            data = new JSONObject();
                            DXFVertex dxfVertex = (DXFVertex) a.next();
                            data.put("x",dxfVertex.getX());
                            data.put("y",dxfVertex.getY());
                            data.put("z",dxfVertex.getZ());
                            jsonArray.add(data);
                        }
                        jsonArrays.add(jsonArray);
                    }
                    jsonObject1.put(DXFConstants.ENTITY_TYPE_LWPOLYLINE,jsonArrays);
                    jsonObject.put(DXFConstants.ENTITY_TYPE_LWPOLYLINE,jsonObject1);
                }
                List<?> mtext = layer.getDXFEntities(DXFConstants.ENTITY_TYPE_POLYLINE);

                if (mtext != null) {
                    JSONObject jsonObject1 = new JSONObject();
                    jsonObject1.put("type",((DXFPolyline) (mtext.get(0))).getType());
                    jsonObject1.put("layerName",((DXFPolyline) (mtext.get(0))).getLayerName());
                    jsonObject1.put("ID",((DXFPolyline) (mtext.get(0))).getID());
                    List<JSONArray> jsonArrays = new ArrayList<>();
                    for (int i = 0; i < mtext.size(); i++) {
                        JSONArray jsonArray = new JSONArray();
                        Iterator a = ((DXFPolyline) (mtext.get(i))).getVertexIterator();
                        while (a.hasNext()) {
                            DXFVertex dxfVertex = (DXFVertex) a.next();
                            data = new JSONObject();
                            data.put("x",dxfVertex.getX());
                            data.put("y",dxfVertex.getY());
                            data.put("z",dxfVertex.getZ());
                            //System.out.println(dxfVertex.getPoint().toString());
                            jsonArray.add(data);
                        }
                        jsonArrays.add(jsonArray);
                    }
                    jsonObject1.put(DXFConstants.ENTITY_TYPE_POLYLINE,jsonArrays);
                    jsonObject.put(DXFConstants.ENTITY_TYPE_POLYLINE,jsonObject1);
                }
                List<?> ptext = layer.getDXFEntities(DXFConstants.ENTITY_TYPE_POINT);
                if (ptext != null) {
                    System.out.println("sourceFile = " + ptext.toString());
                }
            }

        } catch (ParseException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public static JSONObject getDocContent2(String sourceFile){
        JSONObject jsonObject = new JSONObject();
        Parser parser = ParserBuilder.createDefaultParser();
        try {
            FileInputStream in = new FileInputStream(new File(sourceFile));
            //  "/x"参数用 parser.parse(in, "");
            //  "/x2010" 用parser.parse(in, "UTF-8");
            //parser.parse(in, "UTF-8");  //编码集合

            //parse
            parser.parse(in, DXFParser.DEFAULT_ENCODING);
           // parser.parse(in, "GBK");  //编码集合
            DXFDocument doc = parser.getDocument();

            jsonObject.put("name",sourceFile);
            System.out.println("sourceFile = " + doc.toString());
            Iterator<?> it = doc.getDXFLayerIterator();
            JSONObject data;
            while (it.hasNext()) {
                DXFLayer layer = (DXFLayer) it.next();
                List point = layer.getDXFEntities(DXFConstants.ENTITY_TYPE_POINT);
                Iterator point2 = layer.getDXFEntityTypeIterator();
                List<?> text = layer.getDXFEntities(DXFConstants.ENTITY_TYPE_LWPOLYLINE);
                if (text != null) {
                    JSONObject jsonObject1 = new JSONObject();
                    jsonObject1.put("type",((DXFLWPolyline) (text.get(0))).getType());
                    jsonObject1.put("layerName",((DXFLWPolyline) (text.get(0))).getLayerName());
                    jsonObject1.put("ID",((DXFLWPolyline) (text.get(0))).getID());
                    List<JSONArray> jsonArrays = new ArrayList<>();
                    for (int i = 0; i < text.size(); i++) {
                        JSONArray jsonArray = new JSONArray();
                        Iterator a = ((DXFLWPolyline) (text.get(i))).getVertexIterator();
                        while (a.hasNext()) {
                            data = new JSONObject();
                            DXFVertex dxfVertex = (DXFVertex) a.next();
                            data.put("x",dxfVertex.getX());
                            data.put("y",dxfVertex.getY());
                            data.put("z",dxfVertex.getZ());
                            jsonArray.add(data);
                        }
                        jsonArrays.add(jsonArray);
                    }
                    jsonObject1.put(DXFConstants.ENTITY_TYPE_LWPOLYLINE,jsonArrays);
                    jsonObject.put(DXFConstants.ENTITY_TYPE_LWPOLYLINE,jsonObject1);
                }
                /**
                 * AcDbEntity
                 *   8
                 * 0
                 * 100
                 * AcDbPoint
                 *  10
                 * 3455.872575349593
                 *  20
                 * 4180.550934417399
                 *  30
                 * 0.0
                 *   0
                 * POINT
                 */
                List<?> mtext = layer.getDXFEntities(DXFConstants.ENTITY_TYPE_POLYLINE);

                if (mtext != null) {
                    JSONObject jsonObject1 = new JSONObject();
                    jsonObject1.put("type",((DXFPolyline) (mtext.get(0))).getType());
                    jsonObject1.put("layerName",((DXFPolyline) (mtext.get(0))).getLayerName());
                    jsonObject1.put("ID",((DXFPolyline) (mtext.get(0))).getID());
                    List<JSONArray> jsonArrays = new ArrayList<>();
                    for (int i = 0; i < mtext.size(); i++) {
                        JSONArray jsonArray = new JSONArray();
                        Iterator a = ((DXFPolyline) (mtext.get(i))).getVertexIterator();
                        while (a.hasNext()) {
                            DXFVertex dxfVertex = (DXFVertex) a.next();
                            data = new JSONObject();
                            data.put("x",dxfVertex.getX());
                            data.put("y",dxfVertex.getY());
                            data.put("z",dxfVertex.getZ());
                            //System.out.println(dxfVertex.getPoint().toString());
                            jsonArray.add(data);
                        }
                        jsonArrays.add(jsonArray);
                    }
                    jsonObject1.put(DXFConstants.ENTITY_TYPE_POLYLINE,jsonArrays);
                    jsonObject.put(DXFConstants.ENTITY_TYPE_POLYLINE,jsonObject1);
                }
                List<?> ptext = layer.getDXFEntities(DXFConstants.ENTITY_TYPE_POINT);
                if (ptext != null) {
                    System.out.println("sourceFile = " + ptext.toString());
                }
            }

        } catch (ParseException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }


    public static void  read(InputStream in, String layerid) {

        Parser parser = ParserBuilder.createDefaultParser();
        try {

            //parse
            parser.parse(in, DXFParser.DEFAULT_ENCODING);

            //get the documnet and the layer
            DXFDocument doc = parser.getDocument();
            DXFLayer layer = doc.getDXFLayer(layerid);

            //get all polylines from the layer
            List plines = layer.getDXFEntities(DXFConstants.ENTITY_TYPE_POLYLINE);

            //work with the first polyline
            doSomething((DXFPolyline) plines.get(0));

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public static void doSomething(DXFPolyline pline) {

        //iterate over all vertex of the polyline
        for (int i = 0; i < pline.getVertexCount(); i++) {

            DXFVertex vertex = pline.getVertex(i);

            //do something like collect the data and
            //build a mesh for a FEM system
        }
    }

   /* public static void export(){
        DXFDocument dxfDocument = new DXFDocument();

        //region 定义图层

        //初始化线图层
        LayerTableEntity lineLayer = new LayerTableEntity("line");
        //设置图层颜色
        lineLayer.setLayerColorIndex(2);
        //定义图层到文档
        dxfDocument.createLayer(lineLayer);

        //初始化点图层
        LayerTableEntity pointLayer = new LayerTableEntity("point");
        //设置图层颜色
        pointLayer.setLayerColorIndex(10);
        //定义图层到文档
        dxfDocument.createLayer(pointLayer);

        //endregion

        //region 定义块

        BlockCustom blockCustom = new BlockCustom(dxfDocument, "WEIPI");
        //块内容定义
        LWPolyLineEntity lwPolyLineEntity = new LWPolyLineEntity(0, 0, 0);
        lwPolyLineEntity.addDXFPoint(new DXFPoint(0.1, 0, 0));
        lwPolyLineEntity.addDXFPoint(new DXFPoint(-0.1, 0, 0));
        lwPolyLineEntity.setWidth(0.2);
        blockCustom.addEntity(lwPolyLineEntity);
        //块属性定义
        Att attPointName = new Att("x", "0");
        Att attPointHeight = new Att("y", "0");
        blockCustom.addAtt(attPointName);
        blockCustom.addAtt(attPointHeight);
        blockCustom.addAtt( new Att("PointName", "Point-1"));

        //定义块到文档中
        dxfDocument.defineBlock(blockCustom);
        //endregion
        //region 线、块绘制
        Random random = new Random();
        //产生100条多段线每条线50个点，颜色随机，
        for (int j = 1; j <= 20; j++) {
            LWPolyLineEntity polyLineEntity = new LWPolyLineEntity(0, 0, 0);
            polyLineEntity.setLayer(lineLayer);
            polyLineEntity.setColor(random.nextInt(240));
            for (int i = 0; i < 50; i++) {
                double x = i * j;
                double y = 10 * Math.sin(i * j) + j * 10;
                polyLineEntity.addDXFPoint(new DXFPoint(x, y, 0));
                BlockEntity blockEntity = new BlockEntity(dxfDocument, x, y, 0.0, blockCustom);
                blockEntity.setLayer(pointLayer);
                //添加属性
                Att pn = new Att("PointName", "Point" + j + "-" + i);
                pn.setTextColorIndex(random.nextInt(240));
                Att xAtt = new Att("x", x + "");
                //设置该属性不显示到图中
                xAtt.setAttStatus(Att.ATT_STATUS_UNVISIBLE);
                Att yAtt = new Att("y", y + "");
                yAtt.setAttStatus(Att.ATT_STATUS_UNVISIBLE);
                blockEntity.addAtt(xAtt);
                blockEntity.addAtt(yAtt);
                blockEntity.addAtt(pn);

                blockEntity.setTextColorIndex(random.nextInt(240));
                dxfDocument.addEntity(blockEntity);
            }
            dxfDocument.addEntity(polyLineEntity);
        }

        //endregion

        try {
            dxfDocument.writeToDXF("78.dxf");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/
}
