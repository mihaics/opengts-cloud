// ----------------------------------------------------------------------------
// Copyright 2007-2015, SysOP Consulting
// All rights reserved
// ----------------------------------------------------------------------------
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
// 
// http://www.apache.org/licenses/LICENSE-2.0
// 
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//
// ----------------------------------------------------------------------------
// Description:
//  Accelerometer information container
// ----------------------------------------------------------------------------
// Change History:
//  2013/03/01  Martin D. Flynn
//     -Initial release
// ----------------------------------------------------------------------------
package org.opengts.util;

import java.lang.*;
import java.util.*;
import java.math.*;

/**
*** Accerometer XYZ-axis container.
**/

public class Accelerometer
{

    // ------------------------------------------------------------------------
    // ------------------------------------------------------------------------
    // ------------------------------------------------------------------------
    // ------------------------------------------------------------------------

    private static final int    X_AXIS                  = 0;
    private static final int    Y_AXIS                  = 1;
    private static final int    Z_AXIS                  = 2;

    // ------------------------------------------------------------------------
    // ------------------------------------------------------------------------
    // -- Accelerometer list support
    // -  (used for impact reconstruction, etc)

    private static final String LIST_START[]            = { "[", "(" };
    private static final String LIST_END[]              = { "]", ")" };
    private static final char   LIST_SEPARATOR_CHAR     = '|';
    private static final char   SCALAR_SEPARATOR_CHAR   = ',';

    /**
    *** Gets the index of the start-of-list character
    **/
    public static int IndexOfListStartChar(String xyzListStr, int fromNdx)
    {
        return StringTools.indexOf(xyzListStr, fromNdx, LIST_START);
    }

    /**
    *** Gets the index of the end-of-list character
    **/
    public static int IndexOfListEndChar(String xyzListStr, int fromNdx)
    {
        return StringTools.indexOf(xyzListStr, fromNdx, LIST_END);
    }

    /**
    *** Returns true if the specified list starts with a Accelerometer list character
    **/
    public static boolean startsWithListChar(String xyzListStr)
    {
        if (xyzListStr == null) {
            return false;
        } else {
            for (String sol : LIST_START) {
                if (xyzListStr.startsWith(sol)) {
                    return true;
                }
            }
            return false;
        }
    }

    // ------------------------------------------------------------------------

    /**
    *** Parse Accelerometer list.
    *** Leading/Trailing list characters (..) or [..] MUST NOT be specified.
    *** Does not return null.
    **/
    private static Accelerometer[] _ParseAccelerometer(String xyzListStr)
    {
        // "0.00,0.00,0.00|0.50,0.50,0.00|1.00,1.00,0.00"
        char listSep = LIST_SEPARATOR_CHAR;

        /* profile string not specified */
        if (StringTools.isBlank(xyzListStr)) {
            // -- no list specified
            return new Accelerometer[0];
        }

        /* parse */
        String P[] = StringTools.split(xyzListStr,listSep);
        Vector<Accelerometer> accList = new Vector<Accelerometer>();
        for (int i = 0; i < P.length; i++) {
            Accelerometer acc = new Accelerometer(P[i]);
            accList.add(acc);
        }

        /* return array */
        if (accList.size() >= 1) {
            return accList.toArray(new Accelerometer[accList.size()]);
        } else {
            // -- list is empty
            return new Accelerometer[0];
        }

    }

    /**
    *** Parse Accelerometer list.
    *** Leading/Trailing list characters (..) or [..], are optional.
    *** Does not return null.
    **/
    public static Accelerometer[] ParseAccelerometer(String xyzListStr)
    {
        // "0.00,0.00|0.50,0.50|1.00,1.00"

        /* remove optional leading trailing list characters */
        int sNdx = IndexOfListStartChar(xyzListStr,0/*fromNdx*/);
        int eNdx = (sNdx >= 0)? IndexOfListEndChar(xyzListStr,sNdx+1) : -1;
        if ((sNdx >= 0) || (eNdx >= 0)) {
            // -- trim to list only
            int S = (sNdx >= 0)? (sNdx + 1) : 0;
            int E = (eNdx >= 0)? eNdx : xyzListStr.length();
            xyzListStr = xyzListStr.substring(S,E).trim(); // just the list
        }

        /* parse/return */
        return _ParseAccelerometer(xyzListStr);

    }

    /**
    *** Parse Accelerometer list.
    *** Leading/Trailing list characters (..) or [..], are required.
    *** Does not return null.
    **/
    public static Accelerometer[] ParseAccelerometer(String xyzListStr, int fromNdx)
    {
        // "xyz:(0.00,0.00|0.50,0.50|1.00,1.00)"

        /* find/remove leading/trailing brackets */
        // -- leading/trailing list characters required
        if (fromNdx < 0) { fromNdx = 0; }
        int sNdx = IndexOfListStartChar(xyzListStr,fromNdx);
        int eNdx = (sNdx >= 0)? IndexOfListEndChar(xyzListStr,sNdx+1) : -1;
        if ((sNdx >= 0) && (eNdx > (sNdx + 1))) {
            // -- trim to list only
            xyzListStr = xyzListStr.substring(sNdx+1,eNdx).trim(); // just the list
        } else {
            // -- invalid list, no list specified
            xyzListStr = null;
        }

        /* parse */
        return _ParseAccelerometer(xyzListStr);

    }

    // ------------------------------------------------------------------------

    /**
    *** Appends a properly formatted Accelerometer list to the specified StringBuffer
    **/
    public static StringBuffer ToListString(Accelerometer accList[], StringBuffer sb)
    {
        if (sb == null) { sb = new StringBuffer(); }
        sb.append(LIST_START[0]);
        if (accList != null) {
            int len = accList.length;
            for (int i = 0; i < len; i++) {
                if (i > 0) { sb.append(LIST_SEPARATOR_CHAR); }
                Accelerometer A = accList[i];
                A.toString(sb);
            }
        }
        sb.append(LIST_END[0]);
        return sb;
    }

    /** 
    *** Returns a properly formatted Accelerometer list String
    **/
    public static String ToListString(Accelerometer accList[])
    {
        return Accelerometer.ToListString(accList,null).toString();
    }

    // --------------------------------

    /**
    *** Appends a properly formatted Accelerometer list to the specified StringBuffer
    **/
    public static StringBuffer ToListString(java.util.List<Accelerometer> accList, StringBuffer sb)
    {
        if (sb == null) { sb = new StringBuffer(); }
        sb.append(LIST_START[0]);
        if (accList != null) {
            int len = accList.size();
            for (int i = 0; i < len; i++) {
                if (i > 0) { sb.append(LIST_SEPARATOR_CHAR); }
                Accelerometer A = accList.get(i);
                A.toString(sb);
            }
        }
        sb.append(LIST_END[0]);
        return sb;
    }

    /** 
    *** Returns a properly formatted Accelerometer list String
    **/
    public static String ToListString(java.util.List<Accelerometer> accList)
    {
        return Accelerometer.ToListString(accList,null).toString();
    }

    // ------------------------------------------------------------------------
    // ------------------------------------------------------------------------

    public static final double METERS_PER_SEC_SQ_PER_G      = 9.80665;
    public static final double MPSS_PER_G_FORCE             = METERS_PER_SEC_SQ_PER_G;
    public static final double G_PER_MPSS_FORCE             = 1.0 / METERS_PER_SEC_SQ_PER_G;    // 0.101971621297793
    public static final double MPH_PER_SEC_PER_MPSS         = GeoPoint.METERS_PER_MILE / DateTime.SECONDS_PER_HOUR; // 0.44704
    public static final double MPH_PER_SEC_PER_G            = MPSS_PER_G_FORCE * DateTime.SECONDS_PER_HOUR * GeoPoint.MILES_PER_METER; // 21.9368512884753

    public enum ForceUnits implements EnumTools.StringLocale {
        MPSS     (I18N.getString(Accelerometer.class,"Accelerometer.metersPerSecSquared","m/s/s" ), 1.0                  ),
        CMPSS    (I18N.getString(Accelerometer.class,"Accelerometer.centimPerSecSquared","cm/s/s"), 100.0                ),
        G        (I18N.getString(Accelerometer.class,"Accelerometer.gForce"             ,"G"     ), G_PER_MPSS_FORCE     ),
        MPHPS    (I18N.getString(Accelerometer.class,"Accelerometer.milesPerHourPerSec" ,"mph/s" ), MPH_PER_SEC_PER_MPSS );
        // ---
        private I18N.Text   aa = null;
        private double      mm = 1.0;
        ForceUnits(I18N.Text a, double m)                { aa=a; mm=m; }
        public String  toString()                        { return aa.toString(); }
        public String  toString(Locale loc)              { return aa.toString(loc); }
        public double  getMultiplier()                   { return mm; }
        public double  convertFromMetersPerSS(double v)  { return v * mm; }
        public double  convertToMetersPerSS(double v)    { return v / mm; }
    };

    /**
    *** Convert G-Force to Meters/Second/Second
    **/
    public static double Convert_G_to_MSS(double G)
    {
        if (Double.isNaN(G)) {
            return Double.NaN;
        } else {
            return Accelerometer.ForceUnits.G.convertToMetersPerSS(G);
        }
    }

    /**
    *** Convert Meters/Second/Second to G-Force
    **/
    public static double Convert_MSS_to_G(double MSS)
    {
        if (Double.isNaN(MSS)) {
            return Double.NaN;
        } else {
            return Accelerometer.ForceUnits.G.convertFromMetersPerSS(MSS);
        }
    }

    /**
    *** Gets the ForceUnits enum for the specified name
    **/
    public static ForceUnits GetForceUnitsForName(String name)
    {
        return Accelerometer.GetForceUnitsForName(name, ForceUnits.MPSS);
    }

    /**
    *** Gets the ForceUnits enum for the specified name
    **/
    public static ForceUnits GetForceUnitsForName(String name, ForceUnits dft)
    {
        // -- name not specified?
        if (StringTools.isBlank(name)) {
            return dft;
        }
        // -- exact name match?
        ForceUnits funits = EnumTools.getValueOf(ForceUnits.class,name);
        if (funits != null) {
            return funits;
        }
        // -- alias names
        if (name.equalsIgnoreCase("mss")) {
            return ForceUnits.MPSS;
        }
        // -- default
        return dft;
    }

    // ------------------------------------------------------------------------
    // ------------------------------------------------------------------------

    /**
    *** Gets the min/max values for the specified Axis.
    *** Does not return null.
    *** @param axis     The desired axis (0=X, 1=Y, 2=Z)
    *** @param G        True to return value in G-force units, Meters/Second/Second otherwise
    *** @param ACCList  The Accelerometer array
    *** @return A 2-element double array containing the min/max values.
    **/
    private static double[] _GetAxisMinMax(int axis, boolean G, Accelerometer... ACCList)
    {
        double min = Double.NaN;
        double max = Double.NaN;
        if (ListTools.size(ACCList) > 0) {
            for (Accelerometer A : ACCList) {
                if ((A != null) && A.hasAxis(axis)) {
                    double V = A.getAxis(axis, G);
                    // -- minimum
                    if (Double.isNaN(min) || (min < V)) {
                        min = V;
                    }
                    // -- maximum
                    if (Double.isNaN(max) || (max > V)) {
                        max = V;
                    }
                }
            }
        }
        return new double[] { min, max };
    }

    /**
    *** Gets the X-Axis min/max values for the specified Axis.
    *** Does not return null.
    *** @param G        True to return value in G-force units, Meters/Second/Second otherwise
    *** @param ACCList  The Accelerometer array
    *** @return A 2-element double array containing the min/max values.
    **/
    public static double[] GetXAxisMinMax(boolean G, Accelerometer... ACCList)
    {
        return _GetAxisMinMax(X_AXIS, G, ACCList);
    }

    /**
    *** Gets the Y-Axis min/max values for the specified Axis.
    *** Does not return null.
    *** @param G        True to return value in G-force units, Meters/Second/Second otherwise
    *** @param ACCList  The Accelerometer array
    *** @return A 2-element double array containing the min/max values.
    **/
    public static double[] GetYAxisMinMax(boolean G, Accelerometer... ACCList)
    {
        return _GetAxisMinMax(Y_AXIS, G, ACCList);
    }

    /**
    *** Gets the Y-Axis min/max values for the specified Axis.
    *** Does not return null.
    *** @param G        True to return value in G-force units, Meters/Second/Second otherwise
    *** @param ACCList  The Accelerometer array
    *** @return A 2-element double array containing the min/max values.
    **/
    public static double[] GetZAxisMinMax(boolean G, Accelerometer... ACCList)
    {
        return _GetAxisMinMax(Z_AXIS, G, ACCList);
    }

    // ------------------------------------------------------------------------
    // ------------------------------------------------------------------------

    /**
    *** Gets the maximum absolute-value Accelerometer magnitude
    **/
    public static Accelerometer GetMaximumMagnitude(Accelerometer... ACCList)
    {
        Accelerometer max = null;
        if (!ListTools.isEmpty(ACCList)) {
            // -- find maximum absolute-value magnitude
            for (Accelerometer A : ACCList) {
                if (!Accelerometer.isValid(A)) {
                    // -- skip this element
                } else
                if ((max == null) || (max.getMagnitude() < A.getMagnitude())) {
                    // -- latest maximum
                    max = A;
                }
            }
        }
        // -- return maximum maginitude
        return max;
    }

    /**
    *** Gets the maximum absolute-value Accelerometer magnitude
    **/
    public static Accelerometer GetMaximumMagnitude(java.util.List<Accelerometer> ACCList)
    {
        Accelerometer max = null;
        if (!ListTools.isEmpty(ACCList)) {
            // -- find maximum absolute-value magnitude
            for (Accelerometer A : ACCList) {
                if (!Accelerometer.isValid(A)) {
                    // -- skip this element
                } else
                if ((max == null) || (max.getMagnitude() < A.getMagnitude())) {
                    // -- latest maximum
                    max = A;
                }
            }
        }
        // -- return maximum maginitude
        return max;
    }

    // ------------------------------------------------------------------------
    // ------------------------------------------------------------------------

    /**
    *** Creates a Google DataTable JSON object containing the specified Accelerometer elements.
    *** @param intervMS  The time interval between accelerometer elements (in milliseconds).
    *** @param G         True to return the DataTable in G-force units.
    *** @param ACCList   The array of Accelerometer elements.
    *** @return The Google DataTable JSON object
    **/
    public static String CreateGoogleDataTableJSON(int intervMS, boolean G, Accelerometer... ACCList)
    {
        // {
        //   cols: [
        //       { id: "time" , label: "Time" , type: "number" },
        //       { id: "x"    , label: "X"    , type: "number" },
        //       { id: "y"    , label: "Y"    , type: "number" },
        //       { id: "z"    , label: "Z"    , type: "number" },
        //       { id: "total", label: "Total", type: "number" }
        //   ],
        //   rows: [
        //       { c: [ { v:   0 }, { v: -12.6 }, { v: -18.1 }, { v: -18.1 }, { v: 18.1 } ] },
        //       { c: [ { v:  40 }, { v:  -5.1 }, { v:  -7.3 }, { v: -18.1 }, { v: 18.1 } ] },
        //       { c: [ { v:  80 }, { v:  -5.1 }, { v:  -2.1 }, { v: -18.1 }, { v: 18.1 } ] },
        //       { c: [ { v: 120 }, { v:  -2.0 }, { v:  -5.1 }, { v: -18.1 }, { v: 18.1 } ] },
        //       { c: [ { v: 160 }, { v:   5.8 }, { v:   6.7 }, { v: -18.1 }, { v: 18.1 } ] }
        //   ]
        // }

        /* init */
        StringBuffer sb = new StringBuffer();
        sb.append("{").append("\n");

        /* "cols" */
        sb.append("  cols: [").append("\n");
        // --
        sb.append("    { id:\"time\" , label:\"Time\" , type:\"number\" },").append("\n");
        sb.append("    { id:\"x\"    , label:\"X\"    , type:\"number\" },").append("\n");
        sb.append("    { id:\"y\"    , label:\"Y\"    , type:\"number\" },").append("\n");
        sb.append("    { id:\"z\"    , label:\"Z\"    , type:\"number\" },").append("\n");
        sb.append("    { id:\"total\", label:\"Total\", type:\"number\" } ").append("\n");
        // --
        sb.append("  ],").append("\n");

        /* "rows" */
        // { c: [{v:40},{v:-12.6},{v:-18.1},{v:-18.1},{v:18.1}] },
        sb.append("  rows: [").append("\n");
        int rows = 0, rowCnt = ListTools.size(ACCList);
        for (int a = 0; a < rowCnt; a++) {
            Accelerometer A = ACCList[a];
            sb.append("    { c: [");
            sb.append("{v:"+(a*intervMS)                               +"},");
            sb.append("{v:"+(A.hasXAxis()    ?A.getXAxis(G)    :"null")+"},");
            sb.append("{v:"+(A.hasYAxis()    ?A.getYAxis(G)    :"null")+"},");
            sb.append("{v:"+(A.hasZAxis()    ?A.getZAxis(G)    :"null")+"},");
            sb.append("{v:"+(A.hasMagnitude()?A.getMagnitude(G):"null")+"}" );
            sb.append("] }");
            if (rows < (rowCnt - 1)) { sb.append(","); }
            sb.append("\n");
            rows++;
        }
        sb.append("  ]").append("\n");

        /* return */
        sb.append("}");
        return sb.toString();

    }

    // ------------------------------------------------------------------------
    // ------------------------------------------------------------------------

    /**
    *** A private function performing the 'square' of the argument
    *** @param X  The argument to 'square'
    *** @return The square of X (ie. 'X' raised to the 2nd power)
    **/
    private static double SQ(double X) { return X * X; }

    // ------------------------------------------------------------------------
    // ------------------------------------------------------------------------
    // ------------------------------------------------------------------------

    private double  xAxis   = Double.NaN; // NaN if invalid
    private double  yAxis   = Double.NaN; // NaN if invalid
    private double  zAxis   = Double.NaN; // NaN if invalid

    private double  magVal  = Double.NaN;

    /**
    *** Constructor
    **/
    public Accelerometer()
    {
        this(Double.NaN,Double.NaN,Double.NaN);
    }

    /**
    *** Constructor
    *** @param x  X-Axis acceleration (Meters/Second/Second)
    *** @param y  Y-Axis acceleration (Meters/Second/Second)
    *** @param z  Z-Axis acceleration (Meters/Second/Second)
    **/
    public Accelerometer(double x, double y, double z)
    {
        this.xAxis  = x; // NaN if invalid/unavailable
        this.yAxis  = y; // NaN if invalid/unavailable
        this.zAxis  = z; // NaN if invalid/unavailable
        this.magVal = Double.NaN; // clear maginitude
    }

    /**
    *** Constructor
    *** @param xyz  Comma separated XYZ-Axis acceleration (in Meters/Second/Second)
    **/
    public Accelerometer(String xyz)
    {
        if (StringTools.isBlank(xyz)) {
            this.xAxis = Double.NaN;
            this.yAxis = Double.NaN;
            this.zAxis = Double.NaN;
        } else {
            String A[];
            if (xyz.indexOf(SCALAR_SEPARATOR_CHAR) >= 0) {
                A = StringTools.split(xyz,SCALAR_SEPARATOR_CHAR); // "0.0,0.0,0.0"
            } else 
            if (xyz.indexOf("/") >= 0) {
                A = StringTools.split(xyz,'/');                   // "0.0/0.0/0.0"
            } else {
                A = new String[] { xyz };
            }
            this.xAxis = (A.length > 0)? StringTools.parseDouble(A[0],Double.NaN) : Double.NaN;
            this.yAxis = (A.length > 1)? StringTools.parseDouble(A[1],Double.NaN) : Double.NaN;
            this.zAxis = (A.length > 2)? StringTools.parseDouble(A[2],Double.NaN) : Double.NaN;
        }
        this.magVal = Double.NaN; // clear maginitude
    }

    /**
    *** Copy Constructor
    **/
    public Accelerometer(Accelerometer other)
    {
        this();
        if (other != null) {
            this.xAxis  = other.xAxis;
            this.yAxis  = other.yAxis;
            this.zAxis  = other.zAxis;
            this.magVal = other.magVal; // clear maginitude
        }
    }

    // ------------------------------------------------------------------------

    /**
    *** Returns true if the X-Axis value is valid
    **/
    public boolean hasXAxis()
    {
        return !Double.isNaN(this.xAxis);
    }

    /**
    *** Gets the X-Axis accelerometer value (Meters/Second/Second).
    *** Returns NaN if axis data is not available.
    *** @return The X-Axis accelerometer value (Meters/Second/Second)
    **/
    public double getXAxis()
    {
        return this.xAxis; // NaN if invalid
    }

    /**
    *** Gets the X-Axis accelerometer value (G-Force).
    *** Returns NaN if axis data is not available.
    *** @return The X-Axis accelerometer value (G-Force)
    **/
    public double getXAxisG()
    {
        return Convert_MSS_to_G(this.getXAxis()); // NaN if invalid
    }

    /**
    *** Gets the X-Axis accelerometer value.
    *** Returns NaN if axis data is not available.
    *** @param G  True to return value in G-force units, Meters/Second/Second otherwise
    *** @return The X-Axis accelerometer value
    **/
    public double getXAxis(boolean G)
    {
        return G? this.getXAxisG() : this.getXAxis(); // NaN if invalid
    }

    /**
    *** Sets the X-Axis accelerometer value (Meters/Second/Second).
    *** @param MSS The X-Axis accelerometer value (Meters/Second/Second)
    **/
    public void setXAxis(double MSS)
    {
        this.xAxis  = MSS; // NaN if invalid
        this.magVal = Double.NaN; // clear maginitude
    }

    /**
    *** Sets the X-Axis accelerometer value (G-Force).
    *** @param G The X-Axis accelerometer value (G-Force)
    **/
    public void setXAxisG(double G)
    {
        this.setXAxis(Convert_G_to_MSS(G)); // NaN if invalid
    }

    // ------------------------------------------------------------------------

    /**
    *** Returns true if the Y-Axis value is valid
    **/
    public boolean hasYAxis()
    {
        return !Double.isNaN(this.yAxis);
    }

    /**
    *** Gets the Y-Axis accelerometer value (Meters/Second/Second).
    *** Returns NaN if axis data is not available.
    *** @return The Y-Axis accelerometer value (Meters/Second/Second)
    **/
    public double getYAxis()
    {
        return this.yAxis; // NaN if invalid
    }

    /**
    *** Gets the Y-Axis accelerometer value (G-Force).
    *** Returns NaN if axis data is not available.
    *** @return The Y-Axis accelerometer value (G-Force)
    **/
    public double getYAxisG()
    {
        return Convert_MSS_to_G(this.getYAxis()); // NaN if invalid
    }

    /**
    *** Gets the Y-Axis accelerometer value.
    *** Returns NaN if axis data is not available.
    *** @param G  True to return value in G-force units, Meters/Second/Second otherwise
    *** @return The Y-Axis accelerometer value
    **/
    public double getYAxis(boolean G)
    {
        return G? this.getYAxisG() : this.getYAxis(); // NaN if invalid
    }

    /**
    *** Sets the Y-Axis accelerometer value (Meters/Second/Second).
    *** @param MSS The Y-Axis accelerometer value (Meters/Second/Second)
    **/
    public void setYAxis(double MSS)
    {
        this.yAxis  = MSS; // NaN if invalid
        this.magVal = Double.NaN; // clear maginitude
    }

    /**
    *** Sets the Y-Axis accelerometer value (G-Force).
    *** @param G The Y-Axis accelerometer value (G-Force)
    **/
    public void setYAxisG(double G)
    {
        this.setYAxis(Convert_G_to_MSS(G)); // NaN if invalid
    }

    // ------------------------------------------------------------------------

    /**
    *** Returns true if the Z-Axis value is valid
    **/
    public boolean hasZAxis()
    {
        return !Double.isNaN(this.zAxis);
    }

    /**
    *** Gets the Z-Axis accelerometer value (Meters/Second/Second).
    *** Returns NaN if axis data is not available.
    *** @return The Z-Axis accelerometer value (Meters/Second/Second)
    **/
    public double getZAxis()
    {
        return this.zAxis; // NaN if invalid
    }

    /**
    *** Gets the Z-Axis accelerometer value (G-Force).
    *** Returns NaN if axis data is not available.
    *** @return The Z-Axis accelerometer value (G-Force)
    **/
    public double getZAxisG()
    {
        return Convert_MSS_to_G(this.getZAxis()); // NaN if invalid
    }

    /**
    *** Gets the Z-Axis accelerometer value.
    *** Returns NaN if axis data is not available.
    *** @param G  True to return value in G-force units, Meters/Second/Second otherwise
    *** @return The Z-Axis accelerometer value
    **/
    public double getZAxis(boolean G)
    {
        return G? this.getZAxisG() : this.getZAxis(); // NaN if invalid
    }

    /**
    *** Sets the Z-Axis accelerometer value (Meters/Second/Second).
    *** @param MSS The Z-Axis accelerometer value (Meters/Second/Second)
    **/
    public void setZAxis(double MSS)
    {
        this.zAxis  = MSS; // NaN if invalid
        this.magVal = Double.NaN; // clear maginitude
    }

    /**
    *** Sets the Z-Axis accelerometer value (G-Force).
    *** @param G The Z-Axis accelerometer value (G-Force)
    **/
    public void setZAxisG(double G)
    {
        this.setZAxis(Convert_G_to_MSS(G)); // NaN if invalid
    }

    // ------------------------------------------------------------------------

    /**
    *** Returns true if the specified axis is available
    *** @param axis The desired axis (0=X, 1=Y, 2=Z)
    *** @return True if the specified axis is available
    **/
    public boolean hasAxis(int axis)
    {
        switch (axis) {
            case X_AXIS: return this.hasXAxis();
            case Y_AXIS: return this.hasYAxis();
            case Z_AXIS: return this.hasZAxis();
            default    : return false;
        }
    }

    /**
    *** Gets the specified axis accelerometer value.
    *** Returns NaN if axis data is not available.
    *** @param axis The desired axis (0=X, 1=Y, 2=Z)
    *** @param G    True to return value in G-force units, Meters/Second/Second otherwise
    *** @return The Z-Axis accelerometer value
    **/
    public double getAxis(int axis, boolean G)
    {
        switch (axis) {
            case X_AXIS: return G? this.getXAxisG() : this.getXAxis();
            case Y_AXIS: return G? this.getYAxisG() : this.getYAxis();
            case Z_AXIS: return G? this.getZAxisG() : this.getZAxis();
            default    : return Double.NaN;
        }
    }

    // ------------------------------------------------------------------------

    /**
    *** Gets the valid axis bitmap
    **/
    public int getValidAxis()
    {
        int mask = 0;
        if (this.hasXAxis()) { mask |= 0x01; }
        if (this.hasYAxis()) { mask |= 0x02; }
        if (this.hasZAxis()) { mask |= 0x04; }
        return mask;
    }

    /**
    *** Returns true if all axis' are valid
    **/
    public boolean isValid()
    {
        return (this.getValidAxis() == 0x07)? true : false;
    }

    /**
    *** Returns true if the specified Accelerometer is valid
    **/
    public static boolean isValid(Accelerometer A)
    {
        return ((A != null) && A.isValid())? true : false;
    }

    // ------------------------------------------------------------------------

    /**
    *** Returns true if this Accelerometer instance has a valid magnitude
    **/
    public boolean hasMagnitude()
    {
        return this.isValid(); // magnitude can be calculated if this Accelerometer is valid
    }

    /**
    *** Gets the vector magnitude (Meters/Second/Second)
    *** @return The vector magnitude (Meters/Second/Second)
    **/
    public double getMagnitude()
    {
        if (Double.isNaN(this.magVal) && this.isValid()) {
            // -- cache magnitude
            this.magVal = Math.sqrt(SQ(this.getXAxis()) + SQ(this.getYAxis()) + SQ(this.getZAxis()));
        }
        return this.magVal;
    }

    /**
    *** Gets the vector magnitude (G-Force)
    *** @return The vector magnitude in (G-Force)
    **/
    public double getMagnitudeG()
    {
        return Convert_MSS_to_G(this.getMagnitude());
    }

    /**
    *** Gets the vector magnitude.
    *** @param G  True to return value in G-force units, Meters/Second/Second otherwise
    *** @return The vector magnitude.
    **/
    public double getMagnitude(boolean G)
    {
        return G? this.getMagnitudeG() : this.getMagnitude();
    }

    // ------------------------------------------------------------------------

    /**
    *** Returns true if the specified Accelerometer is equal to this instance.
    *** @param other The other Accelerometer instance.
    *** @return True if the specified Accelerometer is equal to this instance.
    **/
    public boolean equals(Object other)
    {
        if (other instanceof Accelerometer) {
            Accelerometer A = (Accelerometer)other;
            if (A.getValidAxis() != this.getValidAxis()) {
                return false;
            } else
            if (this.hasXAxis() && (this.getXAxis() != A.getXAxis())) {
                return false;
            } else
            if (this.hasYAxis() && (this.getYAxis() != A.getYAxis())) {
                return false;
            } else
            if (this.hasZAxis() && (this.getZAxis() != A.getZAxis())) {
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    // ------------------------------------------------------------------------

    /**
    *** Gets the String representation of this instance
    *** @return The String representation of this instance
    **/
    public String toString()
    {
        return this.toString(null, false/*mpss?*/, null/*format*/);
    }

    /**
    *** Gets the String representation of this instance
    *** @return The String representation of this instance
    **/
    public String toString(StringBuffer sb)
    {
        return this.toString(sb, false/*mpss?*/, null/*format*/);
    }

    /**
    *** Gets the String representation of this instance
    *** @return The String representation of this instance
    **/
    public String toString(StringBuffer sb, boolean G, String format)
    {
        // "<X>,<Y>,<Z>"
        if (sb == null) { sb = new StringBuffer(); }
        String NaN = "NaN";
        String fmt = !StringTools.isBlank(format)? format : "0.000";
        // -- X
        double X = this.getXAxis(G);
        sb.append(Double.isNaN(X)? NaN: StringTools.format(X,fmt));
        sb.append(SCALAR_SEPARATOR_CHAR);
        // -- Y
        double Y = this.getYAxis(G);
        sb.append(Double.isNaN(Y)? NaN: StringTools.format(Y,fmt));
        sb.append(SCALAR_SEPARATOR_CHAR);
        // -- Z
        double Z = this.getZAxis(G);
        sb.append(Double.isNaN(Z)? NaN: StringTools.format(Z,fmt));
        return sb.toString();
    }

    // ------------------------------------------------------------------------
    // ------------------------------------------------------------------------

}
