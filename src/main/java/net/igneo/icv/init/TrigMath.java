package net.igneo.icv.init;

public class TrigMath {
    private static final double SECOND_ORDER_COEFFICIENT = 0.00000000109111226653103698;
    
    private static double quasiCos2(double x) {
        return 1.0d - SECOND_ORDER_COEFFICIENT * x * x;
    }
    
    public static double[] sincos(int intAngle) {
        int shifter = (intAngle ^ (intAngle << 1)) & 0xC000;
        double x = ((intAngle + shifter) << 17) >> 16;
        
        double cosx = quasiCos2(x);
        double sinx = Math.sqrt(1.0d - cosx * cosx);
        
        if ((shifter & 0x4000) != 0) {
            double temp = cosx;
            cosx = sinx;
            sinx = temp;
        }
        
        if (intAngle < 0) {
            sinx = -sinx;
        }
        
        if ((shifter & 0x8000) != 0) {
            cosx = -cosx;
        }
        
        return new double[]{sinx, cosx};
    }
    
    public static double tan(int intAngle) {
        double[] result = sincos(intAngle);
        double sin = result[0];
        double cos = result[1];
        
        if (Math.abs(cos) < 1e-10) {
            return sin > 0 ? Double.POSITIVE_INFINITY : Double.NEGATIVE_INFINITY;
        }
        
        return sin / cos;
    }
    
    public static int radiansToInternalAngle(double radians) {
        return (int) ((radians % (2 * Math.PI)) * 32768 / (2 * Math.PI)) & 0xFFFF;
    }
    
    
}