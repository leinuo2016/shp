package shp;

/**
 * Create by leinuo on 2020/7/30
 * qq:1321404703 https://github.com/leinuo2016
 */

import java.awt.Polygon;
import java.io.File;
import java.io.FileInputStream;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.Iterator;
import java.util.Properties;
import org.kabeja.dxf.DXFConstants;
import org.kabeja.dxf.DXFDocument;
import org.kabeja.dxf.DXFEntity;
import org.kabeja.dxf.DXFLWPolyline;
import org.kabeja.dxf.DXFLayer;
import org.kabeja.dxf.DXFLine;
import org.kabeja.dxf.DXFMText;
import org.kabeja.dxf.DXFVertex;
import org.kabeja.dxf.helpers.Point;
import org.kabeja.parser.DXFParser;
import org.kabeja.parser.Parser;
import org.kabeja.parser.ParserBuilder;
import org.kabeja.dxf.DXFBlock;
import org.kabeja.dxf.DXFLineType;
import org.kabeja.dxf.DXFDimensionStyle;
import org.kabeja.dxf.objects.DXFObject;
import org.kabeja.dxf.DXFStyle;
import org.kabeja.dxf.DXFView;
import org.kabeja.dxf.DXFViewport;
import org.kabeja.dxf.DXFVariable;
import org.kabeja.dxf.objects.DXFDictionary;
import org.kabeja.dxf.DXFHatchPattern;
import org.kabeja.dxf.DXFInsert;
import org.kabeja.dxf.DXFLeader;
import org.kabeja.dxf.Bounds;

    public class DXFUtil {
        public static void main(String[] args) throws Exception{
            Parser dxfParser = ParserBuilder.createDefaultParser();
            dxfParser.parse(new FileInputStream("C:/Users/Administrator/Desktop/CAD_/28F平面图-0819.dxf"), "UTF-8");
            DXFDocument doc = dxfParser.getDocument();
//	    getDXFViewports(doc);
//	    while(doc.getDXFHeader().getVarialbeIterator().hasNext()){
//	    	DXFVariable dv = (DXFVariable)doc.getDXFHeader().getVarialbeIterator().next();
//	    	System.out.println(dv.getName());
//	    }
//	    getDXFBlocks(doc);
//	    getDXFLineTypes(doc);
//	    getDXFStyles(doc);
//	    getDXFViews(doc);
            getDXFLayers(doc);
//	    getRootDXFDictionarys(doc);
//	    getDXFHatchPatternIterators(doc);
//	    for(DXFBlock d : dxfBlocks){
//	    	System.out.println(d.getName());
//	    	System.out.println(d.getLayerID());
//	    }
        }

        //块列表 获取所有的的块（block）
        public static List<DXFBlock> getDXFBlocks(DXFDocument dxfdoc){
            List<DXFBlock> dxfBlocks = new ArrayList<DXFBlock>();
            Iterator iter = dxfdoc.getDXFBlockIterator();
            while(iter.hasNext()){
                DXFBlock dXFBlock = (DXFBlock)iter.next();

//			System.out.println("LayerID:  "+dXFBlock.getLayerID()+"  Name:  "+dXFBlock.getName()+"  description:  "+dXFBlock.getDescription()+" document:  "+dXFBlock.getDXFDocument()+" Ponit: "+dXFBlock.getReferencePoint());
//			System.out.println("LayerID:  "+dXFBlock.getLayerID()+"  Name:  "+dXFBlock.getName()+"  description:  "+dXFBlock.getDescription()+" Ponit: "+dXFBlock.getReferencePoint()+"  Buonds:  "+dXFBlock.getBounds());
                Bounds b = dXFBlock.getBounds();
                System.out.println("  Name:  "+dXFBlock.getName()+"  depth: "+b.getDepth()+" width: "+b.getWidth()+"  height: "+b.getHeight()+" "+b.getMaximumX()+","+b.getMaximumY()+","+b.getMaximumZ()+"  "+b.getMinimumX()+","+b.getMinimumY()+","+b.getMinimumZ());
//			List<DXFEntity> l = getEntityByDXFBlock(dXFBlock);
//			for(DXFEntity d : l){
//				System.out.println("ID: "+d.getID()+"  Flags: "+d.getFlags()+" TYPE: "+d.getType());
//			}
//			System.out.println(l.size());
//			getLines(dxfdoc,dXFBlock.getLayerID());
//			while(dXFBlock.getDXFEntitiesIterator().hasNext()){
//				DXFEntity d = (DXFEntity)dXFBlock.getDXFEntitiesIterator().next();
//				if(d.isVisibile()){
//					System.out.println("Type: "+d.getType()+" ID: "+d.getID()+"  LayerName: "+d.getLayerName());
//				}
//			}
                dxfBlocks.add(dXFBlock);
            }
            return dxfBlocks;
        }

        //获取所有的entity
        public static List<DXFEntity> getDXFEntities(DXFDocument dxfdoc){
            List<DXFEntity> list = new ArrayList<DXFEntity>();
            Iterator iter = dxfdoc.getDXFLayerIterator();
            while(iter.hasNext()){
                DXFLayer dXFLayer = (DXFLayer)iter.next();
                List<String> l = getDXFEntityTypes(dXFLayer);
                for(String s : l){
                    List<DXFEntity> ld = dXFLayer.getDXFEntities(s);
                    for(DXFEntity d : ld){
                        list.add(d);
                        System.out.println("ID: "+d.getID()+" Boolean:"+d.isBlockEntity());
                    }
                }
            }
            return list;
        }

        //根据图层名称获取entity
        public static List<DXFEntity> getDXFEntitiesByLayerName(DXFDocument dxfdoc,String layerName){
            List<DXFEntity> list = new ArrayList<DXFEntity>();
            Iterator iter = dxfdoc.getDXFLayerIterator();
            while(iter.hasNext()){
                DXFLayer dXFLayer = (DXFLayer)iter.next();
                if(dXFLayer.getName().equals(layerName)){
                    List<String> l = getDXFEntityTypes(dXFLayer);
                    for(String s : l){
                        List<DXFEntity> ld = dXFLayer.getDXFEntities(s);
                        for(DXFEntity d : ld){
                            list.add(d);
                            System.out.println("ID: "+d.getID()+" Boolean:"+d.isBlockEntity());
                        }
                    }
                }
            }
            return list;
        }

        //图层列表 获取所有图层（layer）
        public static List<DXFLayer> getDXFLayers(DXFDocument dxfdoc){
            List<DXFLayer> dXFLayers = new ArrayList<DXFLayer>();
            Iterator iter = dxfdoc.getDXFLayerIterator();
            while(iter.hasNext()){
                DXFLayer dXFLayer = (DXFLayer)iter.next();
//			System.out.println("LayerName:  "+dXFLayer.getName());
//			if(dXFLayer.isVisible() && dXFLayer.getName().equals("I-FURN")){
                if(dXFLayer.getName().equals("I-ROOM-NUB")){
                    List<String> list = getDXFEntityTypes(dXFLayer);
                    for(String s : list){
                        System.out.println(s);
//					List<DXFEntity> l = dXFLayer.getDXFEntities(s);
//					for(DXFEntity d : l){
//						System.out.println("ID: "+d.getID()+" Boolean:"+d.isBlockEntity());
//					}
//					System.out.println(s);
//					if(s.equals("INSERT")){
//						List<DXFEntity> l = dXFLayer.getDXFEntities(s);
//						for(DXFEntity d : l){
//							DXFInsert i = (DXFInsert)d;
//							System.out.println("BlockID: "+i.getBlockID());
//						}
//					}
                        if(s.equals("LEADER")){
                            List<DXFEntity> l = dXFLayer.getDXFEntities(s);
                            for(DXFEntity d : l){
                                DXFLeader i = (DXFLeader)d;
                                int a = i.getCoordinateCount();
                                for(int k=0;k<a;k++){
                                    System.out.print(i.getCoordinateAt(k));
                                    System.out.print("  ");
                                }
                                System.out.println();
                            }
                        }
                    }
                }
                dXFLayers.add(dXFLayer);
            }
            return dXFLayers;
        }

        //通过blockName获取entity 这个其实是一个坑，你从cad制图工具中你会发现DXFBlock是可以被引用的，引用DXFBlock的entity的类型就是INSERT
        public static List<DXFInsert> getDXFInsertByBlockName(DXFDocument dxfdoc,String blockName){
            List<DXFInsert> inserts = new ArrayList<DXFInsert>();
            Iterator iter = dxfdoc.getDXFLayerIterator();
            while(iter.hasNext()){
                DXFLayer dXFLayer = (DXFLayer)iter.next();
                System.out.println("LayerName:  "+dXFLayer.getName());
                if(dXFLayer.isVisible()){
                    List<String> list = getDXFEntityTypes(dXFLayer);
                    for(String s : list){
//					System.out.println(s);
                        if(s.equals("INSERT")){
                            List<DXFEntity> l = dXFLayer.getDXFEntities(s);
                            for(DXFEntity d : l){
                                DXFInsert i = (DXFInsert)d;
//							System.out.println("ID: "+d.getID()+" Boolean:"+d.isBlockEntity());
                                System.out.println("BlockID: "+i.getBlockID());
                                if(blockName.equals(i.getBlockID())){
                                    inserts.add(i);
                                }
                            }
                        }
                    }
                }
            }
            return inserts;
        }

        //通过DXFBlock获取entity
        public static List<DXFEntity> getEntityByDXFBlock(DXFBlock dXFBlock){
            List<DXFEntity> list = new ArrayList<DXFEntity>();
            Iterator iter = dXFBlock.getDXFEntitiesIterator();
            while(iter.hasNext()){
                DXFEntity d = (DXFEntity)iter.next();
//			if(d.isVisibile()){
//				list.add(d);
//			}
                list.add(d);
            }
            return list;
        }

        //通过图层获取DXFEntityType
        public static List<String> getDXFEntityTypes(DXFLayer dXFLayer){
            List<String> list = new ArrayList<String>();
            Iterator iter = dXFLayer.getDXFEntityTypeIterator();
            while(iter.hasNext()){
                String d = (String)iter.next();
                list.add(d);
//			System.out.println(d);
            }
            return list;
        }

        //getDXFLineTypeIterator()  线型
        public static List<DXFLineType> getDXFLineTypes(DXFDocument dxfdoc){
            List<DXFLineType> dXFLineTypes = new ArrayList<DXFLineType>();
            Iterator iter = dxfdoc.getDXFLineTypeIterator();
            while(iter.hasNext()){
                DXFLineType dXFLineType = (DXFLineType)iter.next();
                System.out.println("LineTypeName: "+dXFLineType.getName()+"  Scale:  "+dXFLineType.getScale()+"  Description:  "+dXFLineType.getDescritpion());
                dXFLineTypes.add(dXFLineType);
            }
            return dXFLineTypes;
        }

        //getDXFStyleIterator()
        public static List<DXFStyle> getDXFStyles(DXFDocument dxfdoc){
            List<DXFStyle> dXFStyles = new ArrayList<DXFStyle>();
            Iterator iter = dxfdoc.getDXFStyleIterator();
            while(iter.hasNext()){
                DXFStyle dXFStyle = (DXFStyle)iter.next();
                System.out.println("FontFile:  "+dXFStyle.getFontFile()+"  Name:  "+dXFStyle.getName());
                dXFStyles.add(dXFStyle);
            }
            return dXFStyles;
        }

        //getDXFViewIterator()
        public static List<DXFView> getDXFViews(DXFDocument dxfdoc){
            List<DXFView> dXFViews = new ArrayList<DXFView>();
            Iterator iter = dxfdoc.getDXFViewIterator();
            while(iter.hasNext()){
                DXFView dXFView = (DXFView)iter.next();
                System.out.println("centerPoint:  "+dXFView.getCenterPoint()+"  Name:  "+dXFView.getName()+" Vector: "+dXFView.getViewDirection());
                dXFViews.add(dXFView);
            }
            return dXFViews;
        }

        //	getDXFViewportIterator()
        public static List<DXFViewport> getDXFViewports(DXFDocument dxfdoc){
            List<DXFViewport> dXFViewports = new ArrayList<DXFViewport>();
            Iterator iter = dxfdoc.getDXFViewportIterator();
            while(iter.hasNext()){
                DXFViewport dXFViewport = (DXFViewport)iter.next();
                System.out.println("CenterPoint: "+dXFViewport.getCenterPoint()+" PlotStyleName: "+dXFViewport.getPlotStyleName()+"  VIewPointID: "+dXFViewport.getViewportID()+" VectorX:  "+dXFViewport.getViewDirectionVector().getX()+" VectorY:  "+dXFViewport.getViewDirectionVector().getY()+" VectorZ:  "+dXFViewport.getViewDirectionVector().getZ());
                dXFViewports.add(dXFViewport);
            }
            return dXFViewports;
        }

        //getRootDXFDictionary()
        public static void getRootDXFDictionarys(DXFDocument dxfdoc){
            DXFDictionary d = dxfdoc.getRootDXFDictionary();
            Iterator iter = d.getDXFObjectIterator();
            while(iter.hasNext()){
                System.out.println(iter.next());
            }
        }

        //获取线
        public static void getLines(DXFDocument dxfdoc,String layerID){
            DXFLayer layer = dxfdoc.getDXFLayer(layerID);
            //get all polylines from the layer
            List plines = layer.getDXFEntities(DXFConstants.ENTITY_TYPE_LINE);
            //work with the first polyline
            DXFLine pline = (DXFLine) plines.get(0);
            System.out.println("start: "+pline.getStartPoint()+"  end: "+pline.getEndPoint()); //使用polyline对象种特有的方法获得数据，如左获得该多边线总共的顶点数据
        }

        //	getDXFHatchPatternIterator()
        public static void 	getDXFHatchPatternIterators(DXFDocument doc){
            Iterator iter = doc.getDXFHatchPatternIterator();
            while(iter.hasNext()){
                DXFHatchPattern d = (DXFHatchPattern)iter.next();
                System.out.println("ID: "+d.getID()+" DXFHatch: "+d.getDXFHatch()+" Count: "+d.getLineFamilyCount());
            }
        }

//	//通过DXFDocument根据Id获取所有的DXFEntity
//	public static List<DXFEntity> getDXFEntitiesByDXFDocument(List<DXFEntity> l){
//		List<DXFEntity> list = new ArrayList<DXFEntity>();
//
//		return list;
//	}

    }



