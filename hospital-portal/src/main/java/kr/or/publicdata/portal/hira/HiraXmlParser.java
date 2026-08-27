package kr.or.publicdata.portal.hira;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import kr.or.publicdata.portal.search.HospitalInfo;
import kr.or.publicdata.portal.search.HospitalSearchResult;
import kr.or.publicdata.portal.search.PaginationPolicy;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public final class HiraXmlParser {
    public HospitalSearchResult parse(String responseBody) {
        try {
            Document document = newFactory().newDocumentBuilder().parse(
                    new InputSource(new StringReader(responseBody)));
            XPath xpath = XPathFactory.newInstance().newXPath();

            int totalCount = requiredInt(xpath, document, "/response/body/totalCount");
            int pageNo = requiredInt(xpath, document, "/response/body/pageNo");
            int numOfRows = requiredInt(xpath, document, "/response/body/numOfRows");
            List<HospitalInfo> hospitals = parseHospitals(xpath, document);
            boolean hasNext = PaginationPolicy.hasNext(pageNo, numOfRows, totalCount);

            return new HospitalSearchResult(hospitals, totalCount, pageNo, numOfRows, hasNext);
        } catch (HiraServiceException exception) {
            throw exception;
        } catch (IOException | ParserConfigurationException | SAXException
                | XPathExpressionException exception) {
            throw new HiraServiceException("RESPONSE_PARSE_ERROR", "Unable to parse HIRA response", exception);
        }
    }

    private DocumentBuilderFactory newFactory() throws ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
        factory.setFeature("http://xml.org/sax/features/external-general-entities", false);
        factory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
        factory.setXIncludeAware(false);
        factory.setExpandEntityReferences(false);
        factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
        factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
        return factory;
    }

    private List<HospitalInfo> parseHospitals(XPath xpath, Document document) throws XPathExpressionException {
        NodeList itemNodes = (NodeList) xpath.evaluate(
                "/response/body/items/item",
                document,
                XPathConstants.NODESET);
        List<HospitalInfo> hospitals = new ArrayList<>();
        for (int index = 0; index < itemNodes.getLength(); index++) {
            Node item = itemNodes.item(index);
            hospitals.add(new HospitalInfo(
                    text(xpath, item, "./yadmNm"),
                    text(xpath, item, "./addr"),
                    text(xpath, item, "./telno"),
                    text(xpath, item, "./clCdNm")));
        }
        return hospitals;
    }

    private int requiredInt(XPath xpath, Node context, String expression) throws XPathExpressionException {
        String value = text(xpath, context, expression);
        if (value.isEmpty()) {
            throw new HiraServiceException("RESPONSE_PARSE_ERROR", "Missing response value at " + expression);
        }
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException exception) {
            throw new HiraServiceException(
                    "RESPONSE_PARSE_ERROR",
                    "Expected integer metadata at " + expression,
                    exception);
        }
    }

    private String text(XPath xpath, Node context, String expression) throws XPathExpressionException {
        return xpath.evaluate(expression, context).trim();
    }
}
