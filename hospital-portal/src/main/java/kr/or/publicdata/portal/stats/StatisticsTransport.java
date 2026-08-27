package kr.or.publicdata.portal.stats;

public interface StatisticsTransport {
    String post(String path, String requestBody);
}
