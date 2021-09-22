package cad;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.util.IOUtils;
import org.kabeja.batik.tools.SAXPDFSerializer;
import org.kabeja.batik.tools.SAXPNGSerializer;
import org.kabeja.dxf.*;
import org.kabeja.parser.ParseException;
import org.kabeja.parser.Parser;
import org.kabeja.parser.ParserBuilder;
import org.kabeja.svg.SVGGenerator;
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
 * Create by leinuo on 2021/3/23
 */
public class KabejaApp {

    public static void main(String[] args) {
        String sourceFile = "/home/leinuo/gitHub/shp/src/main/resource/cadfiles/解放渠渠线定2.dxf";

        String targetFile = "/home/leinuo/gitHub/shp/src/main/resource/cadfiles/解放渠渠线定2.png";

        //parseFile(sourceFile, targetFile);


         sourceFile = "/home/leinuo/gitHub/shp/src/main/resource/cadfiles/点.dxf";

         targetFile = "/home/leinuo/gitHub/shp/src/main/resource/cadfiles/点.png";


         //parseFile(sourceFile, targetFile);
        JSONObject docContent1 = getDocContent1(sourceFile);
        System.out.println(docContent1);

    }

    public static void parseFile1(String sourceFile, String targetFile) {

        //InputStream in = new FileInputStream("C:/Users/Admin/Desktop/svg/draft.dxf");
        // Parser dxfParser = DXFParserBuilder.createDefaultParser();
        Parser dxfParser = ParserBuilder.createDefaultParser();
        try {
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
            SAXSerializer out = new SAXPDFSerializer();
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
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (FileNotFoundException | SAXException e) {
            e.printStackTrace();
        }
    }


    public static void parseFile(String sourceFile, String targetFile) {

        Parser parser = ParserBuilder.createDefaultParser();

        try {
            parser.parse(sourceFile,"GBK");

            DXFDocument doc = parser.getDocument();

            //the SVG will be emitted as SAX-Events
            //see org.xml.sax.ContentHandler for more information

            //  ContentHandler myhandler = new ContentHandlerImpl();

            SAXSerializer out = new SAXPNGSerializer();

            OutputStream fileo = new FileOutputStream(targetFile);//转换所得的文件

            out.setOutput(fileo);

            //the output - create first a SAXGenerator (SVG here)
            SAXGenerator generator = new SVGGenerator();

            //start the output
            generator.generate(doc, out, new HashMap());

            IOUtils.close(fileo);

        } catch (SAXException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
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
                List<?> ltext = layer.getDXFEntities(DXFConstants.ENTITY_TYPE_LINE);
                if (ltext != null) {
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

    public static List<DXFEntity> getLayerEntities(DXFDocument dxfdoc,String layerID){
        DXFLayer Layer = dxfdoc.getDXFLayer(layerID);
        Iterator TypeIterator = Layer.getDXFEntityTypeIterator();
        List<DXFEntity> LayerEntities = null ;
        while(TypeIterator.hasNext()){
            LayerEntities.addAll(Layer.getDXFEntities(TypeIterator.toString()));
        }
        return LayerEntities;
    }
}
