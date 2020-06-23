package shp;


import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.gce.geotiff.GeoTiffReader;
import org.geotools.geometry.Envelope2D;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import java.io.File;

public class GeoTiffApp {


    public static void main(String[] args) {
        GetImageExt("");
    }

    public static String GetImageExt(String strImageUrl) {
        String strExtent = "";
        try {
            String ImageUrl = strImageUrl.replace("\\", "\\\\");
            File ImageFile = new File(ImageUrl);
            GeoTiffReader reader = new GeoTiffReader(ImageFile);
            GridCoverage2D coverage = reader.read(null);
            CoordinateReferenceSystem crs = coverage.getCoordinateReferenceSystem2D();
            Envelope2D coverageEnvelope = coverage.getEnvelope2D();
            double coverageMinX = coverageEnvelope.getBounds().getMinX();
            double coverageMaxX = coverageEnvelope.getBounds().getMaxX();
            double coverageMinY = coverageEnvelope.getBounds().getMinY();
            double coverageMaxY = coverageEnvelope.getBounds().getMaxY();
            System.out.println(coverageMinX);
        } catch (Exception e) {
            // TODO: handle exception
        }

        return strExtent;
    }
}