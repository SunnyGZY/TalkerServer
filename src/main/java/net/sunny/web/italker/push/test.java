package net.sunny.web.italker.push;

public class test {

    private static final double EARTH_RADIUS = 6378137;//赤道半径

    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }

    public static void main(String[] args) {


        double lon1 = 31;
        double lat1 = 118;
        double lon2 = 31.019134604917138;
        double lat2 = 118;

        GetDistance(lon1, lat1, lon2, lat2);
        getLongitude(lon1, lat1, 1000);
    }


    public static double getLongitude(double lon1, double lat1, double distance) {
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat1);

        double qb = -Math.asin(Math.sqrt(Math.pow(Math.sin(distance / (2 * EARTH_RADIUS)), 2) / (Math.cos(radLat1) * Math.cos(radLat2)))) * 2;

        double qlon2 = lon1 - qb * 180.0 / Math.PI;

        System.out.println(qlon2);
        return qlon2;
    }

    public static double GetDistance(double lon1, double lat1, double lon2, double lat2) {
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double a = radLat1 - radLat2;
        double b = rad(lon1) - rad(lon2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
        double d = s * EARTH_RADIUS;
        System.out.println(d);
        return d;//单位米
    }
}
